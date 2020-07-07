/**
 *
 */
package com.tangdao.model.vo;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年7月7日
 */
@Data
@NoArgsConstructor
public class Statement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Set<String> permissions = new LinkedHashSet<String>();

	private String resource;

	private String effect;

}
