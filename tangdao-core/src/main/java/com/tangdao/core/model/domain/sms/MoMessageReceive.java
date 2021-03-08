package com.tangdao.core.model.domain.sms;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 上行消息回复Entity
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_mo_message_receive")
public class MoMessageReceive extends DataEntity<MoMessageReceive> {
	
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;
	
	private String userCode;		// 用户编码
	private String passageId;		// 通道标识
	private String msgId;		// 短信标识
	private String mobile;		// 用户手机号
	private String content;		// 短信内容
	private String destnationNo;		// 服务号长号码
	private Boolean needPush = false;		// 是否需要推送，0：不需要，1：需要
	private String pushUrl;		// 推送地址
	private String receiveTime;		// 收到信息的时间
	
	@TableField(exist = false)
	private String passageName;

	//短信上行推送
	@TableField(exist = false)
	private MoMessagePush messagePush;
	
	@TableField(exist = false)
	private String sid;
	@TableField(exist = false)
	private Integer retryTimes;
	
	public MoMessageReceive() {
		super();
	}
}