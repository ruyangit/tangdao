/**
 *
 */
package com.tangdao.service.model.dto;

import com.tangdao.service.model.domain.Config;

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
public class ConfigDTO extends Config {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String oldConfigKey;

}
