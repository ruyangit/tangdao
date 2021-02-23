/**
 *
 */
package com.tangdao.developer.model.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月23日
 */
@Getter
@Setter
@TableName("tds_user_balance")
public class UserBalance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userCode;

	private String mobile;

	private Integer type;

	private Double balance;

	// 告警阀值
	private Integer threshold;

	private Integer payType;

}
