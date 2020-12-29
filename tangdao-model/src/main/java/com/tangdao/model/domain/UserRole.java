/**
 *
 */
package com.tangdao.model.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@Getter
@Setter
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String roleCode;

	@TableId
	private String userCode;
}
