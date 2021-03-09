/**
 *
 */
package com.tangdao.core;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月5日
 */
@Data
public abstract class BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableField(exist = false)
	private LinkedHashMap<String, Object> dataSqlMap;

	public void setDataSqlMap(String key, Object value) {
		this.getDataSqlMap().put(key, value);
	}
}
