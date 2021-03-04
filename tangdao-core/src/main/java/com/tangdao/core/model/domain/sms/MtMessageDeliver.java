package com.tangdao.core.model.domain.sms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.BaseModel;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 下行短信回执状态Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Setter
@Getter
@TableName(BaseModel.DB_PREFIX_ + "sms_mt_message_deliver")
public class MtMessageDeliver extends DataEntity<MtMessageDeliver> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String msgId; // 消息ID
	private int cmcp; // 运营商
	private String mobile; // 用户手机号
	private String statusCode; // 状态码
	private String deliverTime; // 短信提供商回复的时间，可为空
	
	private String status;
	private String remarks;

	public MtMessageDeliver() {
		super();
	}
}