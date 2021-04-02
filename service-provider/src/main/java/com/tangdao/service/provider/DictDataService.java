/**
 *
 */
package com.tangdao.service.provider;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.service.mapper.DictDataMapper;
import com.tangdao.service.model.domain.DictData;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
@Service
public class DictDataService extends BaseService<DictDataMapper, DictData> {

	@Transactional(rollbackFor = Exception.class)
	public boolean deleteByDictType(String dictType) {
		return super.remove(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType));
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(DictData dictData) {
		if (StrUtil.isEmpty(dictData.getPid()) || DictData.ROOT_ID.equals(dictData.getPid())) {
			dictData.setPid(DictData.ROOT_ID);
			dictData.setPids(dictData.getPid() + ",");
			dictData.setTreeNames(dictData.getDictLabel());
		}
		// 赋值父类数据
		if (!DictData.ROOT_ID.equals(dictData.getPid())) {
			DictData pDictData = super.getById(dictData.getPid());
			dictData.setPids(pDictData.getPids() + pDictData + ",");
			dictData.setTreeNames(pDictData.getTreeNames() + "/");
		}

		// 保存或更新数据
		super.saveOrUpdate(dictData);

		List<DictData> children = super.list(
				Wrappers.<DictData>lambdaQuery().like(DictData::getPids, dictData.getId()));
		children.stream().forEach(item -> {

		});
		return true;
	}

}
