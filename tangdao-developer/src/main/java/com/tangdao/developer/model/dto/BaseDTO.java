/**
 *
 */
package com.tangdao.developer.model.dto;

import java.io.Serializable;

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
public class BaseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 开发者接口唯一标识
	 */
	private String appkey;

	/**
	 * 开发者接口签名
	 */
	private String appsecret;

	/**
	 * 签名时间戳
	 */
	private String timestamp;

}
