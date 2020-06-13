/**
 *
 */
package com.tangdao.model.dto;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月12日
 */
@Data
public class UserRoleDTO {

	private String id;

	private String userId;

	private String roleId;

	private List<String> userIds;

	private List<String> roleIds;
}
