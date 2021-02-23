/**
 *
 */
package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
@Getter
@Setter
@TableName("sys_config")
public class Config extends DataEntity<Config> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;
	
	private String configName;
	
	private String configKey;
	
	private String configValue;
	
	private String isInner;
	
	private String remarks;
}
