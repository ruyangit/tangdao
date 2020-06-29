package com.tangdao.test;

import com.tangdao.core.mybatis.data.privilege.annotation.DataColumn;
import com.tangdao.core.mybatis.data.privilege.annotation.DataCondition;
import com.tangdao.core.mybatis.data.privilege.annotation.DataObject;
import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;
import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.ColumnType;

/**
 * 
 * @ClassName: DataPrivilegeDao.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
public interface DataPrivilegeDao {

//	@DataPrivilege(
//		relations = {
//			@DataJoinRelation(
//				master = @DataObject(name = "user", alias = "u"),
//				slave = @DataObject(name = "employe", alias = "e"),
//				joinColumns = {
//					@DataJoinColumn(masterColumn = "employe_id", slaveColumn = "id")
//				}
//			),
//			@DataJoinRelation(
//				master = @DataObject(name = "dept", alias = "d"),
//				slave = @DataObject(name = "position", alias = "p"),
//				joinColumns = {
//					@DataJoinColumn(masterColumn = "id", slaveColumn = "dept_id")
//				}
//			),
//			@DataJoinRelation(
//				master = @DataObject(name = "employe", alias = "e"),
//				slave = @DataObject(name = "dept", alias = "d"),
//				joinColumns = {
//					@DataJoinColumn(masterColumn = "dept_id", slaveColumn = "id")
//				}
//			)
//		},
//		conditions = {
//			/*
//			@DataCondition(
//				categoryGroup = "datas",
//				groupCombineType = CombineType.OR,
//				reference = @DataObject(name = "user", alias = "u"),
//				columns = {
//					@DataColumn(
//						categoryKey = "nameKey",
//						name = "name",
//						columnType = ColumnType.String, 
//						object = @DataObject(name = "employe", alias = "e")
//					),
//					@DataColumn(
//						categoryKey = "idKey",
//						name = "id",
//						columnType = ColumnType.String, 
//						object = @DataObject(name = "user", alias = "u")
//					)
//				}
//			)
//			*/
//			@DataCondition(
//				reference = @DataObject(name = "user", alias = "u"),
//				columns = {
//					@DataColumn(
//						categoryKey = "nameKey",
//						name = "name",
//						columnType = ColumnType.String, 
//						object = @DataObject(name = "dept", alias = "d")
//					),
//					@DataColumn(
//						categoryKey = "empKey",
//						name = "name",
//						columnType = ColumnType.String, 
//						object = @DataObject(name = "employe", alias = "e")
//					)
//				}
//			)
//		}
//	)
//	 @DataSql("SELECT *  FROM (SELECT * FROM user u) u, (SELECT * FROM employe e) e WHERE u.employe_id = u.id AND u.name = e.name")
//	@DataSql("SELECT * FROM user u")
//	 @DataSql("DELETE u FROM user u")
//	 @DataSql("Update user u SET u.name = 'zhangsan'")
	
	@DataPrivilege(conditions = { @DataCondition(reference = @DataObject(name = "log", alias = "t"), columns = {
			@DataColumn(categoryKey = "createByKey", name = "create_by", columnType = ColumnType.String, object = @DataObject(name = "log", alias = "t")) }) })
	@DataSql("SELECT * FROM log t")
	void getQueryUserSql();
}
