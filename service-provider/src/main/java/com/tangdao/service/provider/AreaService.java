package com.tangdao.service.provider;

import org.springframework.stereotype.Service;

import com.tangdao.core.service.TreeService;
import com.tangdao.service.IAreaService;
import com.tangdao.service.mapper.AreaMapper;
import com.tangdao.service.model.domain.Area;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
@Service
public class AreaService extends TreeService<AreaMapper, Area> implements IAreaService {
	
}