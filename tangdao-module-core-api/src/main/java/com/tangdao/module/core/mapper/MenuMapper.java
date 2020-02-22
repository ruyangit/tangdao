package com.tangdao.module.core.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.tangdao.module.core.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}
