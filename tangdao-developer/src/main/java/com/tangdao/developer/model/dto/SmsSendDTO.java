/**
 *
 */
package com.tangdao.developer.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
@Getter
@Setter
public class SmsSendDTO extends AuthorizationDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 短信内容
	 */
	private String content;

	/**
	 * 扩展码号
	 */
	private String extNumber;

	/**
	 * 用于调用者自定义内容，主要为了实现调用侧的自行业务逻辑标识，如调用方自己的 会员标识，最终我方会原样返回
	 */
	private String attach;

	/**
	 * 回调URL
	 */
	private String callback;

	private transient String userCode;
	private transient Integer fee;
	private transient Integer totalFee;
	private transient String ip;
	private transient Integer appType;
}
