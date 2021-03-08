package com.tangdao.core.model.domain.sms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 签名扩展Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_signature_extno")
public class SignatureExtno extends DataEntity<SignatureExtno> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String appId; //
	private String signature; // 签名
	private String extNumber; // 扩展号码

	public SignatureExtno() {
		super();
	}
}