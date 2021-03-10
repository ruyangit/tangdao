/**
 *
 */
package com.tangdao.core.service;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.context.CommonContext.Status;
import com.tangdao.core.context.SettingsContext.DictType;
import com.tangdao.core.dao.DictDataMapper;
import com.tangdao.core.model.domain.DictData;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
public class DictDataService extends BaseService<DictDataMapper, DictData> {

	public List<DictData> findByDictType(DictType dictType) {
		return super.list(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType).eq(DictData::getStatus,
				Status.NORMAL));
	}

	public DictData getByDictTypeAndLabel(DictType dictType, String label) {
		return super.getOne(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType)
				.eq(DictData::getStatus, Status.NORMAL).eq(DictData::getDictLabel, label));
	}

	public DictData getByDictTypeAndValue(DictType dictType, String value) {
		return super.getOne(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType)
				.eq(DictData::getStatus, Status.NORMAL).eq(DictData::getDictValue, value));
	}
	
	public DictData getByDictTypeAndKey(DictType dictType, String key) {
		return super.getOne(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType)
				.eq(DictData::getStatus, Status.NORMAL).eq(DictData::getDictKey, key));
	}

}
