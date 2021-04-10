/**
 *
 */
package com.tangdao.core.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public abstract class DataEntity<T> extends BaseModel {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableField(fill = FieldFill.INSERT)
	public String createBy; // 新增人

	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT)
	public Date createDate; // 新增时间

	/**
	 * 
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	public String updateBy; // 更新人

	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	public Date updateDate; // 更新时间

}
