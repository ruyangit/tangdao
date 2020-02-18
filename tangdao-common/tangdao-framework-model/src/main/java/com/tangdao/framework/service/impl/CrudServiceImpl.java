package com.tangdao.framework.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tangdao.framework.service.ICrudService;

public class CrudServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ICrudService<T> {

}
