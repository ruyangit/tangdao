package com.tangdao.exchanger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.context.PassageContext.PassageMessageTemplateStatus;
import com.tangdao.core.dao.SmsPassageMessageTemplateMapper;
import com.tangdao.core.model.domain.PassageMessageTemplate;
import com.tangdao.core.service.BaseService;
import com.tangdao.core.utils.PatternUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 通道消息模板ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPassageMessageTemplateService
		extends BaseService<SmsPassageMessageTemplateMapper, PassageMessageTemplate> {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SmsPassageMessageTemplateMapper smsPassageMessageTemplateMapper;

	public boolean save(PassageMessageTemplate passageMessageTemplate) {
		try {
			passageMessageTemplate.setStatus(PassageMessageTemplateStatus.AVAIABLE.getValue() + "");
			passageMessageTemplate.setRegexValue(parseContent2Regex(passageMessageTemplate.getTemplateContent()));
			passageMessageTemplate.setParamNames(parseVariableName(passageMessageTemplate.getTemplateContent()));

			passageMessageTemplate.setCreateDate(new Date());

			int result = smsPassageMessageTemplateMapper.insert(passageMessageTemplate);
			if (result == 0) {
				return false;
			}

			pushToRedis(passageMessageTemplate);

			return true;
		} catch (Exception e) {
			logger.error("添加短信通道内容模板失败", e);
			return false;
		}
	}

	public boolean update(PassageMessageTemplate passageMessageTemplate) {
		try {
			passageMessageTemplate.setRegexValue(parseContent2Regex(passageMessageTemplate.getTemplateContent()));
			passageMessageTemplate.setParamNames(parseVariableName(passageMessageTemplate.getTemplateContent()));
			passageMessageTemplate.setStatus(PassageMessageTemplateStatus.AVAIABLE.getValue() + "");
			boolean result = this.updateById(passageMessageTemplate);
			if (!result) {
				return false;
			}

			pushToRedis(passageMessageTemplate);

			return true;
		} catch (Exception e) {
			logger.error("修改短信通道内容模板失败", e);
			return false;
		}
	}

	/**
	 * 
	 * TODO 根据表达式提取内容中变量对应的值
	 * 
	 * @param content   短信内容
	 * @param regex     表达式
	 * @param paramSize 参数数量
	 * 
	 * @return
	 */
	public static String[] pickupValuesByRegex(String content, String regex, int paramSize) {
		if (StrUtil.isEmpty(content)) {
			return null;
		}

		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(content);
			List<String> values = new ArrayList<>();
			while (m.find()) {
				// 拼接所有匹配的参数数据
				for (int i = 0; i < paramSize; i++) {
					values.add(m.group(i + 1));
				}
			}

			return values.toArray(new String[] {});
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error("根据表达式查询短信内容参数异常", e);
			return null;
		}
	}

	/**
	 * 
	 * TODO 获取内容中的变量信息
	 * 
	 * @param content
	 * @return
	 */
	private static String parseVariableName(String content) {
		String regex = "#([a-zA-Z0-9]+)#";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		StringBuilder variableNames = new StringBuilder();
		while (m.find()) {
			if (StrUtil.isEmpty(m.group())) {
				continue;
			}

			variableNames.append(m.group().replaceAll("#", "")).append(",");
		}

		if (variableNames.length() == 0) {
			return null;
		}

		return variableNames.substring(0, variableNames.length() - 1);
	}

	/**
	 * 
	 * TODO 内容转换正则表达式
	 * 
	 * @param content
	 * @return
	 */
	private static String parseContent2Regex(String content) {
		// modify 变量内容 增加不可见字符
		content = content.replaceAll("#[a-z]*[0-9]*[A-Z]*#", "(.*)");
		// 去掉末尾可以增加空格等不可见字符，以免提供商模板不通过
		// return prefix+oriStr+"\\s*$";
		return String.format("^%s$", content);
	}

	public boolean delete(String id) {
		try {
			return this.removeById(id);
		} catch (Exception e) {
			logger.error("删除通道短信模板失败", e);
			return false;
		}
	}

	public List<PassageMessageTemplate> findByPassageId(String passageId) {
		return this.list(
				Wrappers.<PassageMessageTemplate>lambdaQuery().eq(PassageMessageTemplate::getPassageId, passageId));
	}

	public PassageMessageTemplate getByTemplateId(String templateId) {
		return this.getOne(
				Wrappers.<PassageMessageTemplate>lambdaQuery().eq(PassageMessageTemplate::getTemplateId, templateId));
	}

	/**
	 * 
	 * TODO 获取REDIS KEY数据
	 * 
	 * @param passageId
	 * @return
	 */
	private static String getRedisKey(String passageId) {
		return String.format("%s:%d", SmsRedisConstant.RED_USER_PASSAGE_MESSAGE_TEMPLATE, passageId);
	}

	private void pushToRedis(PassageMessageTemplate template) {
		try {
			stringRedisTemplate.opsForHash().put(getRedisKey(template.getPassageId()), template.getTemplateId(),
					JSON.toJSONBytes(template));
		} catch (Exception e) {
			logger.error("用户通道模板信息加载REDIS失败", e);
		}
	}

	public boolean reloadToRedis() {
		List<PassageMessageTemplate> list = this.list();
		if (CollUtil.isEmpty(list)) {
			return false;
		}

		stringRedisTemplate.delete(stringRedisTemplate.keys(SmsRedisConstant.RED_USER_PASSAGE_MESSAGE_TEMPLATE + "*"));

		for (PassageMessageTemplate template : list) {
			pushToRedis(template);
		}

		return true;
	}

	public PassageMessageTemplate getByMessageContent(String passageId, String messageContent) {
		try {
			List<PassageMessageTemplate> list = findByPassageId(passageId);

			if (CollUtil.isNotEmpty(list)) {
				for (PassageMessageTemplate template : list) {
					if (template == null) {
						logger.warn("LOOP当前值为空");
						continue;
					}

					if (StrUtil.isEmpty(template.getRegexValue())) {
						continue;
					}

					// 如果普通短信模板存在，则以普通模板为主
					if (PatternUtil.isRight(template.getRegexValue(), messageContent)) {
						return template;
					}

				}
			}

		} catch (Exception e) {
			logger.error("通道短信模板查询失败", e);
		}
		return null;
	}

	public boolean isContentMatched(String id, String content) {
		PassageMessageTemplate smsPassageMessageTemplate = getById(id);
		if (smsPassageMessageTemplate == null) {
			return false;
		}

		return PatternUtil.isRight(smsPassageMessageTemplate.getRegexValue(), content);
	}

}