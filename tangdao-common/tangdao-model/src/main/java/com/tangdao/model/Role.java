/**
 *
 */
package com.tangdao.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月2日
 */
@Getter
@Setter
public class Role extends Model<Role>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;
	
	private String roleName;
	
	private String remark;
	
	private String status;
	
	private Date created;
	
	private Date modified;
	
}
