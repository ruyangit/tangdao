/**
 *
 */
package com.tangdao.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.mybatis.data.privilege.annotation.DataColumn;
import com.tangdao.core.mybatis.data.privilege.annotation.DataCondition;
import com.tangdao.core.mybatis.data.privilege.annotation.DataObject;
import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;
import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.ColumnType;
import com.tangdao.model.domain.Log;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}
