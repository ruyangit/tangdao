package com.tangdao.module.core.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.module.core.model.domain.Assertion;

/**
 * <p>
 * 断言表 Mapper 接口
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@Mapper
public interface AssertionMapper extends BaseMapper<Assertion> {

}
