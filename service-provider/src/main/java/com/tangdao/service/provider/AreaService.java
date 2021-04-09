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

//	public List<ChildVo> tree(String pid) {
//		LambdaQueryWrapper<Area> queryWrapper = Wrappers.<Area>lambdaQuery();
//		queryWrapper.eq(Area::getStatus, Area.NORMAL);
//		if (StrUtil.isNotBlank(pid)) {
//			queryWrapper.eq(Area::getPid, pid);
//		}
//		queryWrapper.orderByAsc(Area::getTreeSort);
//		List<Area> sourceList = super.list(queryWrapper);
//		if (CollUtil.isEmpty(sourceList)) {
//			return CollUtil.newArrayList();
//		}
//		List<ChildVo> list = sourceList.stream().map(area -> {
//			ChildVo nodeVo = new ChildVo();
//			nodeVo.setId(area.getId());
//			nodeVo.setPid(area.getPid());
//			nodeVo.setLabel(area.getAreaName());
//			nodeVo.setStatus(area.getStatus());
//			return nodeVo;
//		}).collect(Collectors.toList());
//		return getChildren(list);
//	}
}