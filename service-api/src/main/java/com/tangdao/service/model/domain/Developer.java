/**
 *
 */
package com.tangdao.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

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
@TableName("sys_user_developer")
public class Developer extends DataEntity<Developer> {

	private static final long serialVersionUID = 1652989610979690232L;

	@TableId
	private String id;
	
	private String appKey;

	private String appSecret;

	private String salt;

	private String userId;

	private String status;
	
	private String remarks;
	
}
