package com.tangdao.developer.request;

import java.io.Serializable;

import com.tangdao.developer.annotation.ValidateField;

import lombok.Data;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
@Data
public class AuthorizationRequest implements Serializable {

	private static final long serialVersionUID = -7218382770115176499L;

	/**
	 * 开发者接口唯一标识，通行证
	 */
	@ValidateField(value = "appkey", required = true)
	private String appkey;

	/**
	 * 开发者接口签名(经过签名算法的摘要信息)
	 */
	@ValidateField(value = "appsecret", required = true)
	private String appsecret;

	/**
	 * 签名时间戳，用于签名时间有效性校验，防止同一消息暴力调用接口
	 */
	@ValidateField(value = "timestamp", required = true)
	private String timestamp;

	private transient String userId;
	private transient Integer fee;
	private transient Integer totalFee;
	private transient String ip;
	private transient Integer appType;

}
