package org.tangdao.modules.sms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.tangdao.common.collect.ListUtils;
import org.tangdao.common.lang.StringUtils;
import org.tangdao.common.service.CrudService;
import org.tangdao.common.utils.PatternUtils;
import org.tangdao.modules.sms.constant.SmsRedisConstant;
import org.tangdao.modules.sms.mapper.SmsPassageMessageTemplateMapper;
import org.tangdao.modules.sms.model.domain.SmsPassageMessageTemplate;
import org.tangdao.modules.sys.constant.PassageContext.PassageMessageTemplateStatus;

import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

/**
 * 通道消息模板ServiceImpl
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPassageMessageTemplateServiceImpl extends CrudService<SmsPassageMessageTemplateMapper, SmsPassageMessageTemplate> implements ISmsPassageMessageTemplateService{

	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private SmsPassageMessageTemplateMapper smsPassageMessageTemplateMapper;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean save(SmsPassageMessageTemplate passageMessageTemplate) {
		try {
			passageMessageTemplate.setStatus(PassageMessageTemplateStatus.AVAIABLE.getValue()+"");
			passageMessageTemplate.setRegexValue(parseContent2Regex(passageMessageTemplate.getTemplateContent()));
			passageMessageTemplate.setParamNames(parseVariableName(passageMessageTemplate.getTemplateContent()));
			
			passageMessageTemplate.setCreateTime(new Date());
			
			int result = smsPassageMessageTemplateMapper.insert(passageMessageTemplate);
			if(result == 0) {
                return false;
            }
			
			pushToRedis(passageMessageTemplate);
			
			return true;
		} catch (Exception e) {
			logger.error("添加短信通道内容模板失败", e);
			return false;
		}
	}

	@Override
	public boolean update(SmsPassageMessageTemplate passageMessageTemplate) {
		try {
			passageMessageTemplate.setRegexValue(parseContent2Regex(passageMessageTemplate.getTemplateContent()));
			passageMessageTemplate.setParamNames(parseVariableName(passageMessageTemplate.getTemplateContent()));
			passageMessageTemplate.setStatus(PassageMessageTemplateStatus.AVAIABLE.getValue()+"");
			boolean result = this.updateById(passageMessageTemplate);
			if(!result) {
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
	   * @param content
	   * 	短信内容
	   * @param regex
	   * 	表达式
	   *  @param paramSize
	   *  	参数数量
	   * 
	   * @return
	 */
	public static String[] pickupValuesByRegex(String content, String regex, int paramSize) {
		if(StringUtils.isEmpty(content)) {
            return null;
        }
		
        try {
        	Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(content);
            List<String> values = new ArrayList<>();
        	while(m.find()) {
        		// 拼接所有匹配的参数数据
        		for(int i=0; i< paramSize; i++) {
        			values.add(m.group(i+1));
        		}
            }
        	
        	return values.toArray(new String[]{});
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error("根据表达式查询短信内容参数异常", e);
			return null;
		}
	}

	public static void main(String[] args) {
		String[] cc = pickupValuesByRegex("【车点点】您的验证码为959258，请尽快完成后续操作。", "^(.*)$", 1);

		System.out.println(cc[0]);
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
        while(m.find()) {
        	if(StringUtils.isEmpty(m.group())) {
                continue;
            }
        	
            variableNames.append(m.group().replaceAll("#", "")).append(",");
        }
        
        if(variableNames.length() == 0) {
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

	@Override
	public boolean delete(String id) {
		try {
			get(id);
			return this.deleteById(id);
		} catch (Exception e) {
			logger.error("删除通道短信模板失败", e);
			return false;
		}
	}

	@Override
	public List<SmsPassageMessageTemplate> findByPassageId(String passageId) {
		return this.select(Wrappers.<SmsPassageMessageTemplate>lambdaQuery().eq(SmsPassageMessageTemplate::getPassageId, passageId));
	}

	@Override
	public SmsPassageMessageTemplate getByTemplateId(String templateId) {
		return this.getOne(Wrappers.<SmsPassageMessageTemplate>lambdaQuery().eq(SmsPassageMessageTemplate::getTemplateId, templateId));
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
	
	private void pushToRedis(SmsPassageMessageTemplate template) {
		try {
			stringRedisTemplate.opsForHash().put(getRedisKey(template.getPassageId()), template.getTemplateId(),
					JSON.toJSONBytes(template));
		} catch (Exception e) {
			logger.error("用户通道模板信息加载REDIS失败", e);
		}
	}

	@Override
	public boolean reloadToRedis() {
		List<SmsPassageMessageTemplate> list = this.select();
		if (ListUtils.isEmpty(list)) {
            return false;
        }
		
		stringRedisTemplate.delete(stringRedisTemplate.keys(SmsRedisConstant.RED_USER_PASSAGE_MESSAGE_TEMPLATE + "*"));
		
		for (SmsPassageMessageTemplate template : list) {
			pushToRedis(template);
		}
		
		return true;
	}
	
	

	@Override
	public SmsPassageMessageTemplate getByMessageContent(String passageId, String messageContent) {
		try {
			List<SmsPassageMessageTemplate> list = findByPassageId(passageId);
			
			if (ListUtils.isNotEmpty(list)) {
				for (SmsPassageMessageTemplate template : list) {
					if (template == null) {
						logger.warn("LOOP当前值为空");
						continue;
					}

					if (StringUtils.isEmpty(template.getRegexValue())) {
                        continue;
                    }

					// 如果普通短信模板存在，则以普通模板为主
					if (PatternUtils.isRight(template.getRegexValue(), messageContent)) {
                        return template;
                    }
					
				}
			}

		} catch (Exception e) {
			logger.error("通道短信模板查询失败", e);
		}
		return null;
	}

	@Override
	public boolean isContentMatched(String id, String content) {
		SmsPassageMessageTemplate smsPassageMessageTemplate = get(id);
		if(smsPassageMessageTemplate == null) {
            return false;
        }
		
		return PatternUtils.isRight(smsPassageMessageTemplate.getRegexValue(), content);
	}
		
}