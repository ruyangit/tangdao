package com.tangdao.core.mybatis.data.privilege.util;

import com.tangdao.core.mybatis.data.privilege.annotation.DataObject;
import com.tangdao.core.mybatis.data.privilege.model.MDataObject;

/**
 * 
 * @ClassName: MDataObjectUtil.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataObjectUtil {

	/**
	 * create MDataObject by @DataObject.
	 * 
	 * @param dataObject
	 * @return
	 * @return MDataObject
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataObject create(DataObject dataObject) {
		MDataObject mDataObject = new MDataObject();
		mDataObject.setAlias(dataObject.alias());
		mDataObject.setName(dataObject.name());
		return mDataObject;
	}
}
