/**
 *
 */
package com.tangdao.core.model.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
@Getter
@Setter
public class SmsPushReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 提交总数
	 */
	private int submitCount;

	/**
	 * 上家回执总数
	 */
	private int deliverCount;

	/**
	 * 上家未回执总数
	 */
	private int undeliverCount;

	/**
	 * 不需要推送的数量（推送设置）
	 */
	private int unecessaryPushCount;

	/**
	 * 准备推送数量
	 */
	private int readyPushCount;

	/**
	 * 推送成功数
	 */
	private int pushedSuccessCount;

	/**
	 * 推送失败数
	 */
	private int pushedFailedCount;

	/**
	 * 回执当前批次
	 */
	private Long serialNo = 0L;
}
