/**
 *
 */
package com.tangdao.modules.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.model.domain.Menu;
import com.tangdao.model.vo.MenuVo;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

	public List<Menu> findUserMenuList(String userId);
	
	public List<Menu> findRoleMenuList(String roleId);
	
	public List<MenuVo> findMenuVoList(MenuVo menu);
}
