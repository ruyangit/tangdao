/**
 *
 */
package com.tangdao.service.model.dto;

import com.tangdao.service.model.domain.DictType;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月1日
 */
@Getter
@Setter
public class DictTypeDTO extends DictType{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String oldDictType;

}
