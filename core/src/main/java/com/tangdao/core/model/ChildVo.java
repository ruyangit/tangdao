/**
 *
 */
package com.tangdao.core.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月9日
 */
@Data
public class ChildVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String pid;

	private String label;

	private String status;

	private List<ChildVo> children;

	public void addChild(ChildVo node) {
		this.children.add(node);
	}
}
