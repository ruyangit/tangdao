package com.tangdao.core.model.domain.sms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.BaseModel;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 上行消息推送Entity
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName(BaseModel.DB_PREFIX_ + "sms_mo_message_push")
public class MoMessagePush extends DataEntity<MoMessagePush> {
	
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;
	
	private String msgId;		// 消息ID
	private String mobile;		// 手机号码
	private String content;		// 推送内容
	private int    retryTimes;		// 重试次数
	private Long   responseMilliseconds;		// 推送相应时间
	private String responseContent;		// 响应内容
	
}