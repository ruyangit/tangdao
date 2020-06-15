/**
 *
 */
package com.tangdao.core.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.mybatis.IModel;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月15日
 */
public interface AbstractMapper<T extends IModel<?>> extends BaseMapper<T> {

	
}
