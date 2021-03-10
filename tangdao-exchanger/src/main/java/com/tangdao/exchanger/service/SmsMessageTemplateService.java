package com.tangdao.exchanger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.context.CommonContext.AppType;
import com.tangdao.core.context.SmsTemplateContext.ApproveStatus;
import com.tangdao.core.dao.SmsMessageTemplateMapper;
import com.tangdao.core.model.domain.MessageTemplate;
import com.tangdao.core.service.BaseService;
import com.tangdao.core.utils.PatternUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

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
	private void pushToRedis(String userCode, MessageTemplate... templates) {
		try {

			stringRedisTemplate.execute((connection) -> {
				RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
				connection.openPipeline();
				byte[] key = serializer.serialize(getKey(userCode));

				for (MessageTemplate template : templates) {
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
	private List<MessageTemplate> getFromRedis(String userCode) {
		try {
			Set<String> set = stringRedisTemplate.opsForZSet().reverseRangeByScore(getKey(userCode), 0, 1000);
			if (CollUtil.isEmpty(set)) {
				return null;
			}

			List<MessageTemplate> list = new ArrayList<>(set.size());
			for (String s : set) {
				if (StrUtil.isEmpty(s)) {
					continue;
				}

				list.add(JSON.parseObject(s, MessageTemplate.class));
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
	private void removeRedis(MessageTemplate template) {
		try {

			Set<Object> set = redisTemplate.opsForZSet().reverseRangeByScore(getKey(template.getUserId()), 0, 1000);
			if (CollUtil.isEmpty(set)) {
				logger.warn("redis中未找到短信模板 userId[" + template.getUserId() + "]信息");
				return;
			}

			for (Object templateJson : set) {
				MessageTemplate messageTemplate = (MessageTemplate) templateJson;
				if (messageTemplate == null) {
					continue;
				}

				if (template.getContent().equals(messageTemplate.getContent())) {
					stringRedisTemplate.opsForZSet().remove(getKey(template.getUserId()), templateJson);
					stringRedisTemplate.convertAndSend(SmsRedisConstant.BROADCAST_MESSAGE_TEMPLATE_TOPIC, templateJson);
					break;
				}
			}

		} catch (Exception e) {
			logger.warn("REDIS 短信模板移除失败", e);
		}
	}

	public boolean update(MessageTemplate template) {
		MessageTemplate originTemplate;
		try {
			originTemplate = isAllowAccess(template.getUserId(), template.getId());
		} catch (IllegalArgumentException e) {
			logger.error("模板数据鉴权失败 : {}", e.getMessage());
			return false;
		}

		template.setRegexValue(parseContent2Regex(template.getContent()));
		template.setCreateDate(originTemplate.getCreateDate());
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
	private MessageTemplate isAllowAccess(String userCode, String templateId) {
		MessageTemplate template = getById(templateId);
		if (template == null) {
			throw new IllegalArgumentException("模板 [" + templateId + "]信息为空");
		}

		// 仅针对WEB用户自己添加的模板进行过滤
		if (AppType.WEB.getCode() == template.getAppType() && !template.getUserId().equals(userCode)) {
			throw new IllegalArgumentException(
					"用户模板[" + templateId + "]数据不匹配，原模板用户:[" + template.getUserId() + "] , 本次用户:[" + userCode + "]");
		}

		if (AppType.WEB.getCode() == template.getAppType()
				&& Integer.valueOf(template.getStatus()) != ApproveStatus.WAITING.getValue()) {
			throw new IllegalArgumentException("用户模板[" + templateId + "]模板状态为非待审核状态[" + template.getStatus() + "]不能修改");
		}

		return template;
	}

	public boolean deleteById(String id) {
		MessageTemplate template = getById(id);
		if (template == null) {
			logger.error("用户短信模板为空，删除失败， ID：{}", id);
			return false;
		}

		try {
			removeRedis(template);
		} catch (Exception e) {
			logger.error("移除REDIS用户模板失败， ID：{}", id, e);
		}

		return super.removeById(id);
	}

	/**
	 * 根据内容匹配最符合条件的的短信模板数据（按照优先级排序选择，按照有精确模板优先，超级模板次之）
	 * 
	 * @param list    短信模板集合
	 * @param content 短信内容
	 * @return 匹配后的模板数据
	 */
	private MessageTemplate matchAccessTemplate(List<MessageTemplate> list, String content) {
		if (CollUtil.isEmpty(list)) {
			logger.warn("There is no message template result matched");
			return null;
		}

		MessageTemplate superTemplate = null;
		for (MessageTemplate template : list) {
			if (template == null) {
				logger.warn("LOOP当前值为空");
				continue;
			}

			if (StrUtil.isEmpty(template.getRegexValue())) {
				continue;
			}

			if (SUPER_TEMPLATE_REGEX.equalsIgnoreCase(template.getRegexValue())) {
				superTemplate = template;
				continue;
			}

			// 如果普通短信模板存在，则以普通模板为主
			if (PatternUtil.isRight(template.getRegexValue(), content)) {
				return template;
			}
		}

		// 如果普通短信模板未找到，判断是否找到超级模板，有则直接返回
		if (superTemplate != null) {
			return superTemplate;
		}

		return null;
	}

	public MessageTemplate getByContent(String userCode, String content) {
		List<MessageTemplate> list = getTemplatesByUserCode(userCode);
		if (CollUtil.isEmpty(list)) {
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
	private List<MessageTemplate> getTemplatesByUserCode(String userCode) {
		if (CollUtil.isNotEmpty(GLOBAL_MESSAGE_TEMPLATE.get(userCode))) {
			return GLOBAL_MESSAGE_TEMPLATE.get(userCode);
		}

		List<MessageTemplate> list = null;
		try {
			list = getFromRedis(userCode);
			if (CollUtil.isNotEmpty(list)) {
				return list;
			}
			list = super.list(Wrappers.<MessageTemplate>lambdaQuery().eq(MessageTemplate::getUserId, userCode));
			if (CollUtil.isNotEmpty(list)) {
				// 如果DB中存在，REDIS中不存在，则需要加载至REDIS
				pushToRedis(userCode, list.toArray(new MessageTemplate[] {}));
			}

			return list;
		} catch (Exception e) {
			logger.warn("No templates found by userCode [" + userCode + "]", e);
			return null;
		} finally {
			if (CollUtil.isNotEmpty(list)) {
				GLOBAL_MESSAGE_TEMPLATE.put(userCode, list);
			}
		}
	}

	/**
	 * 设置常规属性数据
	 * 
	 * @param template 模板信息
	 */
	private void setProperties(MessageTemplate template) {
		template.setCreateDate(new Date());
		// 平台判断 后台添加 状态默认
//		if (AppType.WEB.getCode() == template.getAppType()) {
//			template.setStatus(ApproveStatus.WAITING.getValue() + "");
//		}
		template.setRegexValue(parseContent2Regex(template.getContent()));

		if (Convert.toInt(template.getStatus()) == ApproveStatus.SUCCESS.getValue()) {
			pushToRedis(template.getUserId(), template);
		} else {
			removeRedis(template);
		}
	}

	public boolean saveOrUpdate(MessageTemplate template) {
		if (StrUtil.isEmpty(template.getContent())) {
			throw new IllegalArgumentException("模板内容不能为空");
		}

		Set<String> words = forbiddenWordsService.filterForbiddenWords(template.getContent());
		if (CollUtil.isNotEmpty(words)) {
			throw new IllegalArgumentException("模板内容包含敏感词[" + words + "]");
		}
		setProperties(template);

		return super.saveOrUpdate(template);
	}

	@Transactional
	public boolean saveToBatchContent(MessageTemplate template, String[] contents) {
		Set<String> words = forbiddenWordsService.filterForbiddenWords(template.getContent());
		if (CollUtil.isNotEmpty(words)) {
			throw new IllegalArgumentException("模板内容包含敏感词[" + words + "]");
		}

		if (AppType.WEB.getCode() == template.getAppType()) {
			template.setStatus(ApproveStatus.WAITING.getValue() + "");
		}

		try {
			List<MessageTemplate> list = new ArrayList<>();
			for (String content : contents) {
				MessageTemplate newTemplate = new MessageTemplate();
				BeanUtils.copyProperties(template, newTemplate);

				newTemplate.setContent(content);
				newTemplate.setCreateDate(new Date());
				newTemplate.setRegexValue(parseContent2Regex(content));

				boolean result = super.save(newTemplate);
				if (!result) {
					throw new RuntimeException("模板数据插入失败");
				}

				list.add(newTemplate);
			}

			if (CollUtil.isEmpty(list)) {
				logger.warn("No template data need to process by contents [" + contents + "]");
				return false;
			}

			if (Convert.toInt(template.getStatus()) == ApproveStatus.SUCCESS.getValue()) {
				pushToRedis(template.getUserId(), list.toArray(new MessageTemplate[] {}));
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

	public boolean approve(String id, int status, String remarks) {
		MessageTemplate template = getById(id);
		template.setStatus(status + "");
		template.setRemarks(remarks);
		template.setApproveTime(new Date());

		if (ApproveStatus.SUCCESS.getValue() == status) {
			pushToRedis(template.getUserId(), template);
		} else {
			removeRedis(template);
		}
		return super.updateById(template);
	}

	public boolean isContentMatched(String id, String content) {
		MessageTemplate template = getById(id);
		return template != null && PatternUtil.isRight(template.getRegexValue(), content);
	}

	/**
	 * 跟新缓存中模板数据
	 * 
	 * @param template 模板数据
	 */
	private void updateTemplateInRedis(MessageTemplate template) {
		final MessageTemplate originTemplate = super.getById(template);
		try {
			stringRedisTemplate.execute((connection) -> {
				RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
				connection.openPipeline();
				byte[] key = serializer.serialize(getKey(template.getUserId()));

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

	public boolean reloadToRedis() {
		List<MessageTemplate> list = super.list();
		if (CollUtil.isEmpty(list)) {
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
			// if (CollUtil.isNotEmpty(keys)) {
			// connection.del(keys.toArray(new byte[][] {}));
			// }

			for (MessageTemplate template : list) {
				byte[] key = serializer.serialize(getKey(template.getUserId()));

				connection.zAdd(key, template.getPriority().doubleValue(), JSON.toJSONBytes(template));
			}

			return connection.closePipeline();

		}, false, true);

		return CollUtil.isNotEmpty(con);
	}

	public boolean delete(String id, String userCode) {
		MessageTemplate originTemplate;
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
		return super.removeById(id);
	}
}