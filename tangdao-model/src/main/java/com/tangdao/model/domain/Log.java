/**
 *
 */
package com.tangdao.model.domain;

import com.tangdao.model.base.BaseEntity;

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
public class Log extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	
	private String createBy;
	
	private String createByName;
	
}
