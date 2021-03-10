package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021年3月3日
 */
@Getter
@Setter
@TableName("sms_message_deliver")
public class MessageDeliver extends DataEntity<MessageDeliver> {
	
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;
	
	private String deliverstatus;		// deliverStatus
	private String statusdes;		// statusDes
	private Long delivertimestart;		// deliverTimeStart
	private Long delivertimeend;		// deliverTimeEnd
	private String httptrytimes;		// httpTryTimes
	
	public MessageDeliver() {
		super();
	}
}