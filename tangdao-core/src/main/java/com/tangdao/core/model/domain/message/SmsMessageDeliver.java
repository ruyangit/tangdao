package com.tangdao.core.model.domain.message;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.BaseModel;
import com.tangdao.core.DataEntity;

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
@TableName(BaseModel.DB_PREFIX_ + "sms_message_deliver")
public class SmsMessageDeliver extends DataEntity<SmsMessageDeliver> {
	
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;
	
	private String deliverstatus;		// deliverStatus
	private String statusdes;		// statusDes
	private Long delivertimestart;		// deliverTimeStart
	private Long delivertimeend;		// deliverTimeEnd
	private String httptrytimes;		// httpTryTimes
	
	public SmsMessageDeliver() {
		super();
	}
}