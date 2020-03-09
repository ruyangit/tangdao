/**
 *
 */
package com.tangdao.framework.persistence.handler;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tangdao.framework.constant.DataStatus;

/**
 *  填充信息抽象
 * @author Ryan Ru(ruyangit@gmail.com)
 */
public abstract class AbstractMetaObjectHandler implements MetaObjectHandler{
	private final static String STATUS = "status";
	private final static String UPDATE_TIME = "updateTime";
	private final static String CREATE_TIME = "createTime";

	@Override
	public void insertFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		Object status = getFieldValByName(STATUS, metaObject);
		Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
		Object createTime = getFieldValByName(CREATE_TIME, metaObject);

		if (status == null || StringUtils.EMPTY.equals(status)) {
			setFieldValByName(STATUS, DataStatus.NORMAL.value(), metaObject);
		}

		if (createTime == null || updateTime == null) {
			Date date = new Date();
			if (createTime == null) {
				setFieldValByName(CREATE_TIME, date, metaObject);
			}
			if (updateTime == null) {
				setFieldValByName(UPDATE_TIME, date, metaObject);
			}
		}

		this.insertFillWithUser(metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		setFieldValByName(UPDATE_TIME, new Date(), metaObject);
		
		this.updateFillWithUser(metaObject);
	}
	
	/**
	 *  新增填充用户
	 * @param metaObject
	 */
	protected abstract void insertFillWithUser(MetaObject metaObject);
	
	/**
	 *  更新填充用户
	 * @param metaObject
	 */
	protected abstract void updateFillWithUser(MetaObject metaObject);
}
