/**
 *
 */
package com.tangdao.core.mybatis.injector;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tangdao.common.BaseModel;
import com.tangdao.core.session.SessionContext;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月14日
 */
public class DateMetaObjectHandler implements MetaObjectHandler {

	private static final String CREATE_BY_FIELD = "create_by";
	private static final String CREATE_DATE_FIELD = "create_date";
	private static final String UPDATE_BY_FIELD = "update_by";
	private static final String UPDATE_DATE_FIELD = "update_date";
	private static final String STATUS_FIELD = "status";

	@Override
	public void insertFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		Object createDate = getFieldValByName(CREATE_DATE_FIELD, metaObject);
		if (createDate == null) {
			setFieldValByName(CREATE_DATE_FIELD, new Date(), metaObject);
		}
		Object createBy = getFieldValByName(CREATE_BY_FIELD, metaObject);
		if (createBy == null && SessionContext.getId() != null) {
			setFieldValByName(CREATE_DATE_FIELD, SessionContext.getId(), metaObject);
		}
		Object status = getFieldValByName(STATUS_FIELD, metaObject);
		if (status == null) {
			setFieldValByName(STATUS_FIELD, BaseModel.NORMAL, metaObject);
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		Object updateDate = getFieldValByName(UPDATE_DATE_FIELD, metaObject);
		if (updateDate == null) {
			setFieldValByName(UPDATE_DATE_FIELD, new Date(), metaObject);
		}
		Object updateBy = getFieldValByName(UPDATE_BY_FIELD, metaObject);
		if (updateBy == null && SessionContext.getId() != null) {
			setFieldValByName(UPDATE_BY_FIELD, SessionContext.getId(), metaObject);
		}
	}

}
