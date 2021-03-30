/**
 *
 */
package com.tangdao.service.model.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月30日
 */
@Getter
@Setter
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	private String roleId;
	
	private List<String> userIds;
	
	private List<String> roleIds;

}
