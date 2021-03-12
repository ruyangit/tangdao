/**
 *
 */
package com.tangdao.portal.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.Menu;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
public interface MenuMapper extends BaseMapper<Menu> {

	public List<Menu> findByRoleMenu(Menu menu);
	
	public List<Menu> findByUserMenu(Menu menu);
}
