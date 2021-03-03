package com.tangdao.exchanger.model.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * TODO 调用厂商发送接口返回信息
 * </p>
 *
 * @author ruyang
 * @since 2021年3月3日
 */
@Getter
@Setter
public class ProviderSendResponse implements Serializable {

	private static final long serialVersionUID = -2875315786851717616L;

	/**
	 * 消息ID
	 */
	private String sid;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 状态码
	 */
	private String statusCode;

	/**
	 * 发送时间
	 */
	private String sendTime;

	/**
	 * 是否成功
	 */
	private boolean isSuccess;

	/**
	 * 备注信息（一般存报文详情）
	 */
	private String remarks;

	/**
	 * 通道代码
	 */
	private String passageCode;

}
