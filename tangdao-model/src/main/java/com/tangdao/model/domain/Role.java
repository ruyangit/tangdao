/**
 *
 */
package com.tangdao.model.domain;

import java.util.Date;

import com.tangdao.model.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月2日
 */
@Getter
@Setter
public class Role extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String roleName;
	
	private String roleNameCn;
	
	private String roleType;
	
	private String remark;
	
	private String status;
	
	private Date modified;
	
}
