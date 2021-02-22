/**
 *
 */
package com.tangdao.developer.model.vo;

import java.io.Serializable;

import cn.hutool.core.util.StrUtil;
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
public class SmsSendVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 扣费条数
	 */
	private String fee = "0";

	/**
	 * 消息ID，用于后续的状态匹配
	 */
	private String sid = StrUtil.EMPTY;

}
