package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月9日
 */
@Getter
@Setter
@TableName("sms_message_deliver_log")
public class MessageDeliverLog extends DataEntity<MessageDeliverLog> {

	private static final long serialVersionUID = 1L;

	private String passageCode; // 通道简码
	private String msgId; // 消息ID
	private String statusCode; // 状态码
	private String deliverTime; // 短信提供商回复的时间，可为空

	public MessageDeliverLog() {
		super();
	}
}