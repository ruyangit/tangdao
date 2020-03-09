/**
 * 
 */
package com.tangdao.module.core.config;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tangdao.framework.context.UserContext;
import com.tangdao.framework.model.UserInfo;
import com.tangdao.framework.persistence.handler.AbstractMetaObjectHandler;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
@Component
public class MybatisMetaObjectHandler extends AbstractMetaObjectHandler {

	@Autowired
	UserContext userContext;

	private final static String UPDATE_BY = "updateBy";
	private final static String CREATE_BY = "createBy";

	@Override
	protected void insertFillWithUser(MetaObject metaObject) {
		// TODO Auto-generated method stub
		Object updateBy = getFieldValByName(UPDATE_BY, metaObject);
		Object createBy = getFieldValByName(CREATE_BY, metaObject);
		UserInfo user = userContext.getUserInfo();
		if (null != user) {
			if (updateBy == null) {
				setFieldValByName(UPDATE_BY, user.getUserId(), metaObject);
			}
			if (createBy == null) {
				setFieldValByName(CREATE_BY, user.getUserId(), metaObject);
			}
		}
	}

	@Override
	protected void updateFillWithUser(MetaObject metaObject) {
		// TODO Auto-generated method stub
		Object updateBy = getFieldValByName(UPDATE_BY, metaObject);
		UserInfo user = userContext.getUserInfo();
		if (null != user) {
			if (updateBy == null) {
				setFieldValByName(UPDATE_BY, user.getUserId(), metaObject);
			}
		}
	}

}
