/**
 *
 */
package com.tangdao.core;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tangdao.core.config.Global;
import com.tangdao.core.context.SessionContextHolder;
import com.tangdao.core.model.BaseModel;

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

	@Override
	public void insertFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		Object createDate = getFieldValByName(Global.FieldName.createDate.name(), metaObject);
		if (createDate == null) {
			setFieldValByName(Global.FieldName.createDate.name(), new Date(), metaObject);
		}
		Object createBy = getFieldValByName(Global.FieldName.createBy.name(), metaObject);
		if (createBy == null && StrUtil.isNotBlank(SessionContextHolder.getId())) {
			setFieldValByName(Global.FieldName.createBy.name(), SessionContextHolder.getId(), metaObject);
		}
		Object status = getFieldValByName(Global.FieldName.status.name(), metaObject);
		if (status == null) {
			setFieldValByName(Global.FieldName.status.name(), BaseModel.NORMAL, metaObject);
		}

		this.updateFill(metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		Object updateDate = getFieldValByName(Global.FieldName.updateDate.name(), metaObject);
		if (updateDate == null) {
			setFieldValByName(Global.FieldName.updateDate.name(), new Date(), metaObject);
		}
		Object updateBy = getFieldValByName(Global.FieldName.updateBy.name(), metaObject);
		if (updateBy == null && StrUtil.isNotBlank(SessionContextHolder.getId())) {
			setFieldValByName(Global.FieldName.updateBy.name(), SessionContextHolder.getId(), metaObject);
		}
	}

}
