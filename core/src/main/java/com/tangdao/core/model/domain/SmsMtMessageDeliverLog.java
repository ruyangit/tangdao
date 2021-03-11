package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 下行短信回执状态日志Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_mt_message_deliver_log")
public class SmsMtMessageDeliverLog extends DataEntity<SmsMtMessageDeliverLog> {

	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;

	private String passageCode; // 通道简码
	private String msgId; // 消息ID
	private String deliverTime; // 短信提供商回复的时间，可为空
	private String report; // 报文数据

	public SmsMtMessageDeliverLog() {
		super();
	}
}