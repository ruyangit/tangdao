/**
 *
 */
package com.tangdao.core.mybatis;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月14日
 */
public class DateMetaObjectHandler implements MetaObjectHandler {

	private static final String CREATE_FIELD = "created";

	@Override
	public void insertFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		// 时间
		Object created = getFieldValByName(CREATE_FIELD, metaObject);
		if (created == null) {
			if (created == null) {
				setFieldValByName(CREATE_FIELD, new Date(), metaObject);
			}
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
	}

}
