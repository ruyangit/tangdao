/**
 *
 */
package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;
import com.tangdao.core.context.PayContext.PaySource;

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
@TableName("paas_user_balance")
public class UserBalance extends DataEntity<UserBalance> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userId;

	private String mobile;

	private Integer type;

	private Double balance;

	private Integer threshold;

	private Integer payType;
	
	private String remarks;
	
	private String status;
	
	@TableField(exist = false)
	private Double price;

	@TableField(exist = false)
	private Double totalPrice;

	@TableField(exist = false)
	private PaySource paySource;

	public UserBalance() {
	}

	public UserBalance(String userId, Integer type, Double balance) {
		this.userId = userId;
		this.type = type;
		this.balance = balance;
	}

	public UserBalance(String userId, Integer type, Integer payType, Double balance) {
		this.userId = userId;
		this.type = type;
		this.payType = payType;
		this.balance = balance;
	}

}
