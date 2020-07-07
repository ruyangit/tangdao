/**
 *
 */
package com.tangdao.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class Policies implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Statement> statements = new ArrayList<Statement>();
}
