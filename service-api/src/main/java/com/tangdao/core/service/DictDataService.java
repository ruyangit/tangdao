/**
 *
 */
package com.tangdao.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.dao.DictDataMapper;
import com.tangdao.core.model.domain.DictData;
import com.tangdao.core.model.domain.DictType;

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

	public List<DictData> findByDictType(DictType dictType) {
		return super.list(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType).eq(DictData::getStatus,
				DictData.NORMAL));
	}

	public DictData getByDictTypeAndLabel(DictType dictType, String dictLabel) {
		return super.getOne(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType)
				.eq(DictData::getStatus, DictData.NORMAL).eq(DictData::getDictLabel, dictLabel));
	}

	public DictData getByDictTypeAndValue(DictType dictType, String dictValue) {
		return super.getOne(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType)
				.eq(DictData::getStatus, DictData.NORMAL).eq(DictData::getDictValue, dictValue));
	}

	public DictData getByDictTypeAndKey(DictType dictType, String dictKey) {
		return super.getOne(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType)
				.eq(DictData::getStatus, DictData.NORMAL).eq(DictData::getDictKey, dictKey));
	}

}
