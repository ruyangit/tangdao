/**
 *
 */
package com.tangdao.model.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年9月11日
 */
@Data
public class FieldDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private Object value;
}
