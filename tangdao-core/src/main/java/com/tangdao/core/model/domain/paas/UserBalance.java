/**
 *
 */
package com.tangdao.core.model.domain.paas;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

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

}
