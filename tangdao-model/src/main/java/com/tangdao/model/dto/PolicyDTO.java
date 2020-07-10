/**
 *
 */
package com.tangdao.model.dto;

import java.util.List;

import com.tangdao.model.domain.Policy;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年7月10日
 */
@Getter
@Setter
public class PolicyDTO extends Policy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;

	private String roleId;

	private List<String> policyIds;

	public void setId(String id) {
		this.id = id;
		this.userId = id;
		this.roleId = id;
	}

}
