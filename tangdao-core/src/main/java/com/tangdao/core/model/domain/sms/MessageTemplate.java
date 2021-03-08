package com.tangdao.core.model.domain.sms;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tangdao.core.BaseModel;
import com.tangdao.core.DataEntity;
import com.tangdao.core.context.SmsTemplateContext.IgnoreBlacklist;
import com.tangdao.core.context.SmsTemplateContext.IgnoreForbiddenWords;

import lombok.Getter;
import lombok.Setter;

/**
 * 消息模板Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName(BaseModel.DB_PREFIX_ + "sms_message_template")
public class MessageTemplate extends DataEntity<MessageTemplate> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userCode; // 归属用户，如果为0，则表示为系统模板（使用所有人）
	private String content; // 模板内容
	private Integer appType; // 操作方式，1:融合WEB平台,2:开发者平台,3:运营支撑系统
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date approveTime; // 审批时间
	private String approveUser; // 审批账号
	private Integer noticeMode; // 审核后短信通知类型,0:不需要通知，1：需要通知
	private String mobile; // 审核后通知的手机号码
	private String regexValue; // 匹配正则表达式，后台自动生成
	private Integer submitInterval; // 短信提交时间间隔（同一号码）
	private Integer limitTimes; // 短信每天提交次数上限（同一号码）
	private String whiteWord; // 敏感词白名单 |符号隔开
	private Integer routeType; // 模板路由类型
	private Integer priority; // 优先级（越大越优先）
	private String extNumber; // 扩展号

	private String status;
	private String remarks;

	/**
	 * 忽略手机黑名单拦截
	 */
	private Integer ignoreBlacklist = IgnoreBlacklist.NO.getValue();

	/**
	 * 忽略手机敏感词拦截
	 */
	private Integer ignoreForbiddenWords = IgnoreForbiddenWords.NO.getValue();

	public MessageTemplate() {
		super();
	}
}