package com.tangdao.test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;
import com.tangdao.core.mybatis.data.privilege.model.DataPrivilegeParameter;
import com.tangdao.core.mybatis.data.privilege.model.MDataPrivilege;
import com.tangdao.core.mybatis.data.privilege.util.DataPrivilegeUtil;
import com.tangdao.core.mybatis.data.privilege.util.MDataPrivilegeUtil;

/**
 * 
 * @ClassName: DataPrivilegeDaoTest.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
public class DataPrivilegeDaoTest {

	public static void main(String[] args) throws Exception {
		Method method = DataPrivilegeDao.class.getMethod("getQueryUserSql", new Class[] {});
		DataSql dataSql = method.getAnnotation(DataSql.class);
		DataPrivilege dataPrivilege = method.getAnnotation(DataPrivilege.class);
		MDataPrivilege mDataPrivilege = MDataPrivilegeUtil.create(dataPrivilege);
		
		/* single condition */
		DataPrivilegeParameter dataPrivilegeParameter = new DataPrivilegeParameter();
		Map<String, Boolean> filterCategory = new HashMap<String, Boolean>();
		filterCategory.put("nameKey", Boolean.TRUE);
		filterCategory.put("idKey", Boolean.TRUE);
		
		dataPrivilegeParameter.setFilterCategory(filterCategory);
		dataPrivilegeParameter.setMDataPrivilege(mDataPrivilege);
		dataPrivilegeParameter.setOriginalSql(dataSql.value());
		Map<String, Object> privilegeData = new HashMap<String, Object>();
		privilegeData.put("nameKey", 11);
		privilegeData.put("empKey", 12);
		dataPrivilegeParameter.setPrivilegeData(privilegeData);
		String privilegeSql = DataPrivilegeUtil.getPrivilegeSql(dataPrivilegeParameter);
		System.out.println(privilegeSql);
		
		/* group conditions */
		/*
		DataPrivilegeParameter dataPrivilegeParameter = new DataPrivilegeParameter();
		Map<String, Boolean> filterCategory = new HashMap<String, Boolean>();
		filterCategory.put("nameKey", Boolean.TRUE);
		filterCategory.put("idKey", Boolean.TRUE);
		dataPrivilegeParameter.setFilterCategory(filterCategory);
		dataPrivilegeParameter.setMDataPrivilege(mDataPrivilege);
		dataPrivilegeParameter.setOriginalSql(dataSql.value());
		Map<String, Object> privilegeData = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("nameKey", "nameKey-" + (i + 1));
			data.put("idKey", "idKey-" + (i + 1));
			datas.add(data);
		}
		privilegeData.put("datas", datas);
		dataPrivilegeParameter.setPrivilegeData(privilegeData);
		String privilegeSql = DataPrivilegeUtil.getPrivilegeSql(dataPrivilegeParameter);
		System.out.println(privilegeSql);
		*/
	}
}
