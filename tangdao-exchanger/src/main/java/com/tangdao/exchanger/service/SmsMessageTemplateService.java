package com.tangdao.exchanger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.model.domain.sms.MessageTemplate;

import javax.annotation.Resource;

/**
 * 消息模板ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMessageTemplateService extends BaseService<SmsMessageTemplateMapper, MessageTemplate> {

	@Resource
	private SmsForbiddenWordsService forbiddenWordsService;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 全局短信模板（与REDIS 同步采用广播模式）
	 */
	public static volatile Map<String, List<MessageTemplate>> GLOBAL_MESSAGE_TEMPLATE = new ConcurrentHashMap<>();

	/**
	 * 超级模板正则表达式（一般指无限制）
	 */
	private static final String SUPER_TEMPLATE_REGEX = "^[\\s\\S]*$";

	private String getKey(String userCode) {
		return String.format("%s:%s", SmsRedisConstant.RED_USER_MESSAGE_TEMPLATE, userCode);
	}

	/**
	 * 添加到REDIS
	 *
	 * @param userCode  用户编号
	 * @param templates 短信模板数据
	 */
	private void pushToRedis(String userCode, SmsMessageTemplate... templates) {
		try {

			stringRedisTemplate.execute((connection) -> {
				RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
				connection.openPipeline();
				byte[] key = serializer.serialize(getKey(userCode));

				for (SmsMessageTemplate template : templates) {
					connection.zAdd(key, template.getPriority().doubleValue(), JSON.toJSONBytes(template));

					// publish message for flush jvm map data
					connection.publish(serializer.serialize(SmsRedisConstant.BROADCAST_MESSAGE_TEMPLATE_TOPIC),
							JSON.toJSONBytes(template));
				}

				return connection.closePipeline();
			}, false, true);

		} catch (Exception e) {
			logger.warn("REDIS 短信模板设置失败", e);
		}
	}

	/**
	 * 根据用户编号查询短信模板集合信息
	 * 
	 * @param userCode 用户ID
	 * @return 模板集合数据
	 */
	private List<SmsMessageTemplate> getFromRedis(String userCode) {
		try {
			Set<String> set = stringRedisTemplate.opsForZSet().reverseRangeByScore(getKey(userCode), 0, 1000);
			if (ListUtils.isEmpty(set)) {
				return null;
			}

			List<SmsMessageTemplate> list = new ArrayList<>(set.size());
			for (String s : set) {
				if (StringUtils.isEmpty(s)) {
					continue;
				}

				list.add(JSON.parseObject(s, SmsMessageTemplate.class));
			}

			return list;
		} catch (Exception e) {
			logger.warn("REDIS 短信模板获取失败", e);
			return null;
		}
	}

	/**
	 * 从redis和内存中移除模板数据
	 * 
	 * @param template 模板信息
	 */
	private void removeRedis(SmsMessageTemplate template) {
		try {

			Set<Object> set = redisTemplate.opsForZSet().reverseRangeByScore(getKey(template.getUserCode()), 0, 1000);
			if (ListUtils.isEmpty(set)) {
				logger.warn("redis中未找到短信模板 userId[" + template.getUserCode() + "]信息");
				return;
			}

			for (Object templateJson : set) {
				SmsMessageTemplate messageTemplate = (SmsMessageTemplate) templateJson;
				if (messageTemplate == null) {
					continue;
				}

				if (template.getContent().equals(messageTemplate.getContent())) {
					stringRedisTemplate.opsForZSet().remove(getKey(template.getUserCode()), templateJson);
					stringRedisTemplate.convertAndSend(SmsRedisConstant.BROADCAST_MESSAGE_TEMPLATE_TOPIC, templateJson);
					break;
				}
			}

		} catch (Exception e) {
			logger.warn("REDIS 短信模板移除失败", e);
		}
	}

	@Override
	public boolean update(SmsMessageTemplate template) {
		SmsMessageTemplate originTemplate;
		try {
			originTemplate = isAllowAccess(template.getUserCode(), template.getId());
		} catch (IllegalArgumentException e) {
			logger.error("模板数据鉴权失败 : {}", e.getMessage());
			return false;
		}

		template.setRegexValue(parseContent2Regex(template.getContent()));
		template.setCreateTime(originTemplate.getCreateTime());
		boolean result = super.updateById(template);
		if (result) {
			updateTemplateInRedis(template);
		}
		return result;
	}

	/**
	 * 是否允许被访问（针对用户ID进行鉴权,防止恶意使用userID来篡改其他userId数据）
	 * 
	 * @param userCode   用户ID
	 * @param templateId 模板ID
	 * @return 模板信息
	 */
	private SmsMessageTemplate isAllowAccess(String userCode, String templateId) {
		SmsMessageTemplate template = get(templateId);
		if (template == null) {
			throw new IllegalArgumentException("模板 [" + templateId + "]信息为空");
		}

		// 仅针对WEB用户自己添加的模板进行过滤
		if (AppType.WEB.getCode() == template.getAppType() && !template.getUserCode().equals(userCode)) {
			throw new IllegalArgumentException(
					"用户模板[" + templateId + "]数据不匹配，原模板用户:[" + template.getUserCode() + "] , 本次用户:[" + userCode + "]");
		}

		if (AppType.WEB.getCode() == template.getAppType()
				&& Integer.valueOf(template.getStatus()) != ApproveStatus.WAITING.getValue()) {
			throw new IllegalArgumentException("用户模板[" + templateId + "]模板状态为非待审核状态[" + template.getStatus() + "]不能修改");
		}

		return template;
	}

	@Override
	public boolean deleteById(String id) {
		SmsMessageTemplate template = get(id);
		if (template == null) {
			logger.error("用户短信模板为空，删除失败， ID：{}", id);
			return false;
		}

		try {
			removeRedis(template);
		} catch (Exception e) {
			logger.error("移除REDIS用户模板失败， ID：{}", id, e);
		}

		return super.deleteById(id);
	}

	/**
	 * 根据内容匹配最符合条件的的短信模板数据（按照优先级排序选择，按照有精确模板优先，超级模板次之）
	 * 
	 * @param list    短信模板集合
	 * @param content 短信内容
	 * @return 匹配后的模板数据
	 */
	private SmsMessageTemplate matchAccessTemplate(List<SmsMessageTemplate> list, String content) {
		if (ListUtils.isEmpty(list)) {
			logger.warn("There is no message template result matched");
			return null;
		}

		SmsMessageTemplate superTemplate = null;
		for (SmsMessageTemplate template : list) {
			if (template == null) {
				logger.warn("LOOP当前值为空");
				continue;
			}

			if (StringUtils.isEmpty(template.getRegexValue())) {
				continue;
			}

			if (SUPER_TEMPLATE_REGEX.equalsIgnoreCase(template.getRegexValue())) {
				superTemplate = template;
				continue;
			}

			// 如果普通短信模板存在，则以普通模板为主
			if (PatternUtils.isRight(template.getRegexValue(), content)) {
				return template;
			}
		}

		// 如果普通短信模板未找到，判断是否找到超级模板，有则直接返回
		if (superTemplate != null) {
			return superTemplate;
		}

		return null;
	}

	@Override
	public SmsMessageTemplate getByContent(String userCode, String content) {
		List<SmsMessageTemplate> list = getTemplatesByUserCode(userCode);
		if (ListUtils.isEmpty(list)) {
			return null;
		}

		return matchAccessTemplate(list, content);
	}

	/**
	 * 根据用户ID获取该用户所有的短信模板数据
	 *
	 * @param userCode 用户ID
	 * @return 模板集合
	 */
	private List<SmsMessageTemplate> getTemplatesByUserCode(String userCode) {
		if (ListUtils.isNotEmpty(GLOBAL_MESSAGE_TEMPLATE.get(userCode))) {
			return GLOBAL_MESSAGE_TEMPLATE.get(userCode);
		}

		List<SmsMessageTemplate> list = null;
		try {
			list = getFromRedis(userCode);
			if (ListUtils.isNotEmpty(list)) {
				return list;
			}
			list = super.select(
					Wrappers.<SmsMessageTemplate>lambdaQuery().eq(SmsMessageTemplate::getUserCode, userCode));
			if (ListUtils.isNotEmpty(list)) {
				// 如果DB中存在，REDIS中不存在，则需要加载至REDIS
				pushToRedis(userCode, list.toArray(new SmsMessageTemplate[] {}));
			}

			return list;
		} catch (Exception e) {
			logger.warn("No templates found by userCode [" + userCode + "]", e);
			return null;
		} finally {
			if (ListUtils.isNotEmpty(list)) {
				GLOBAL_MESSAGE_TEMPLATE.put(userCode, list);
			}
		}
	}

	/**
	 * 设置常规属性数据
	 * 
	 * @param template 模板信息
	 */
	private void setProperties(SmsMessageTemplate template) {
		template.setCreateTime(new Date());
		// 平台判断 后台添加 状态默认
//		if (AppType.WEB.getCode() == template.getAppType()) {
//			template.setStatus(ApproveStatus.WAITING.getValue() + "");
//		}
		template.setRegexValue(parseContent2Regex(template.getContent()));

		if (ObjectUtils.toInteger(template.getStatus()).intValue() == ApproveStatus.SUCCESS.getValue()) {
			pushToRedis(template.getUserCode(), template);
		} else {
			removeRedis(template);
		}
	}

	@Override
	public boolean saveOrUpdate(SmsMessageTemplate template) {
		if (StringUtils.isEmpty(template.getContent())) {
			throw new IllegalArgumentException("模板内容不能为空");
		}

		Set<String> words = forbiddenWordsService.filterForbiddenWords(template.getContent());
		if (ListUtils.isNotEmpty(words)) {
			throw new IllegalArgumentException("模板内容包含敏感词[" + words + "]");
		}
		setProperties(template);

		return super.saveOrUpdate(template);
	}

	@Override
	@Transactional
	public boolean saveToBatchContent(SmsMessageTemplate template, String[] contents) {
		Set<String> words = forbiddenWordsService.filterForbiddenWords(template.getContent());
		if (ListUtils.isNotEmpty(words)) {
			throw new IllegalArgumentException("模板内容包含敏感词[" + words + "]");
		}

		if (AppType.WEB.getCode() == template.getAppType()) {
			template.setStatus(ApproveStatus.WAITING.getValue() + "");
		}

		try {
			List<SmsMessageTemplate> list = new ArrayList<>();
			for (String content : contents) {
				SmsMessageTemplate newTemplate = new SmsMessageTemplate();
				BeanUtils.copyProperties(template, newTemplate);

				newTemplate.setContent(content);
				newTemplate.setCreateTime(new Date());
				newTemplate.setRegexValue(parseContent2Regex(content));

				boolean result = super.save(newTemplate);
				if (!result) {
					throw new RuntimeException("模板数据插入失败");
				}

				list.add(newTemplate);
			}

			if (ListUtils.isEmpty(list)) {
				logger.warn("No template data need to process by contents [" + StringUtils.join(contents) + "]");
				return false;
			}

			if (ObjectUtils.toInteger(template.getStatus()).intValue() == ApproveStatus.SUCCESS.getValue()) {
				pushToRedis(template.getUserCode(), list.toArray(new SmsMessageTemplate[] {}));
			}

			return true;

		} catch (Exception e) {
			logger.error("添加短信模板失败失败, {}", JSON.toJSONString(template), e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

	}

	/**
	 * 内容转换正则表达式
	 * 
	 * @param content 短信内容
	 * @return 转换后的内容
	 */
	private static String parseContent2Regex(String content) {
		// modify 变量内容 增加不可见字符
		content = content.replaceAll("#[a-z]*[0-9]*[A-Z]*#", "[\\\\s\\\\S]*").replaceAll("\\{[a-z]*[0-9]*[A-Z]*\\}",
				"[\\\\s\\\\S]*");
		// 去掉末尾可以增加空格等不可见字符，以免提供商模板不通过
		// return prefix+oriStr+"\\s*$";
		return String.format("^%s$", content);

	}

	@Override
	public boolean approve(String id, int status, String remarks) {
		SmsMessageTemplate template = get(id);
		template.setStatus(ObjectUtils.toString2(status));
		template.setRemarks(remarks);
		template.setApproveTime(new Date());

		if (ApproveStatus.SUCCESS.getValue() == status) {
			pushToRedis(template.getUserCode(), template);
		} else {
			removeRedis(template);
		}
		return super.updateById(template);
	}

	@Override
	public boolean isContentMatched(String id, String content) {
		SmsMessageTemplate template = get(id);
		return template != null && PatternUtils.isRight(template.getRegexValue(), content);
	}

	/**
	 * 跟新缓存中模板数据
	 * 
	 * @param template 模板数据
	 */
	private void updateTemplateInRedis(SmsMessageTemplate template) {
		final SmsMessageTemplate originTemplate = super.get(template.getId());
		try {
			stringRedisTemplate.execute((connection) -> {
				RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
				connection.openPipeline();
				byte[] key = serializer.serialize(getKey(template.getUserCode()));

				if (originTemplate != null) {
					// 删除原有的模板数据
					connection.zRem(key, JSON.toJSONBytes(originTemplate));
				}

				connection.zAdd(key, template.getPriority().doubleValue(), JSON.toJSONBytes(template));

				// publish message for flush jvm map data
				connection.publish(serializer.serialize(SmsRedisConstant.BROADCAST_MESSAGE_TEMPLATE_TOPIC),
						JSON.toJSONBytes(template));

				return connection.closePipeline();
			}, false, true);

		} catch (Exception e) {
			logger.warn("REDIS 短信模板[" + JSON.toJSONString(template) + "]加载失败", e);
		}
	}

	@Override
	public boolean reloadToRedis() {
		List<SmsMessageTemplate> list = super.select();
		if (ListUtils.isEmpty(list)) {
			logger.warn("短信模板数据为空");
			return false;
		}

		stringRedisTemplate.delete(stringRedisTemplate.keys(SmsRedisConstant.RED_USER_MESSAGE_TEMPLATE + "*"));
		List<Object> con = stringRedisTemplate.execute((connection) -> {
			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();

			// 查询该用户编号下的所有模板信息
			// Set<byte[]> keys =
			// connection.keys(serializer.serialize(SmsRedisConstant.RED_USER_MESSAGE_TEMPLATE
			// +
			// "*"));
			// if (ListUtils.isNotEmpty(keys)) {
			// connection.del(keys.toArray(new byte[][] {}));
			// }

			for (SmsMessageTemplate template : list) {
				byte[] key = serializer.serialize(getKey(template.getUserCode()));

				connection.zAdd(key, template.getPriority().doubleValue(), JSON.toJSONBytes(template));
			}

			return connection.closePipeline();

		}, false, true);

		return ListUtils.isNotEmpty(con);
	}

	@Override
	public boolean delete(String id, String userCode) {
		SmsMessageTemplate originTemplate;
		try {
			originTemplate = isAllowAccess(userCode, id);
		} catch (IllegalArgumentException e) {
			logger.error("模板数据鉴权失败 : {}", e.getMessage());
			return false;
		}

		try {
			removeRedis(originTemplate);
		} catch (Exception e) {
			logger.error("移除REDIS用户模板失败， ID：{}", id, e);
		}

		return super.deleteById(id);
	}
}