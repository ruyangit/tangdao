/**
 *
 */
package com.tangdao.core;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tangdao.core.context.SessionContext;
import com.tangdao.core.context.UserContext.UserStatus;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月5日
 */
public class DateMetaObjectHandler implements MetaObjectHandler {

	private String CREATE_BY_FIELD = "createBy";
	private String CREATE_DATE_FIELD = "createDate";
	private String UPDATE_BY_FIELD = "updateBy";
	private String UPDATE_DATE_FIELD = "updateDate";
	private String STATUS_FIELD = "status";

	@Override
	public void insertFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		Object createDate = getFieldValByName(CREATE_DATE_FIELD, metaObject);
		if (createDate == null) {
			setFieldValByName(CREATE_DATE_FIELD, new Date(), metaObject);
		}
		Object createBy = getFieldValByName(CREATE_BY_FIELD, metaObject);
		if (createBy == null && StrUtil.isNotBlank(SessionContext.getId())) {
			setFieldValByName(CREATE_BY_FIELD, SessionContext.getId(), metaObject);
		}
		Object status = getFieldValByName(STATUS_FIELD, metaObject);
		if (status == null) {
			setFieldValByName(STATUS_FIELD, UserStatus.YES.getValue(), metaObject);
		}

		this.updateFill(metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		Object updateDate = getFieldValByName(UPDATE_DATE_FIELD, metaObject);
		if (updateDate == null) {
			setFieldValByName(UPDATE_DATE_FIELD, new Date(), metaObject);
		}
		Object updateBy = getFieldValByName(UPDATE_BY_FIELD, metaObject);
		if (updateBy == null && StrUtil.isNotBlank(SessionContext.getId())) {
			setFieldValByName(UPDATE_BY_FIELD, SessionContext.getId(), metaObject);
		}
	}

}
