/**
 *
 */
package com.tangdao.core.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月5日
 */
public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T> {

}
