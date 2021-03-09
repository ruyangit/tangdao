/**
 *
 */
package com.tangdao.core.model.vo;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
@Getter
@Setter
public class MobileCatagory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 数据分隔符
	 */
	public static final String MOBILE_SPLIT_CHARCATOR = ",";

	/**
	 * 处理结果: true or false
	 */
	private boolean success;

	/**
	 * 结果描述，处理错误才有值
	 */
	private String msg;

	/**
	 * 移动号码，以半角逗号分隔
	 */
	private Map<String, String> cmNumbers;

	/**
	 * 联通号码
	 */
	private Map<String, String> cuNumbers;

	/**
	 * 电信号码
	 */
	private Map<String, String> ctNumbers;

	/**
	 * 过滤号码总数
	 */
	private int filterSize;

	/**
	 * 过滤信息
	 */
	private String filterNumbers;

	/**
	 * 重复号码
	 */
	private String repeatNumbers;

	/**
	 * 重复号码总数
	 */
	private int repeatSize;
}
