/**
 *
 */
package com.tangdao.service.provider;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.TreeService;
import com.tangdao.service.mapper.DictDataMapper;
import com.tangdao.service.model.domain.DictData;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
@Service
public class DictDataService extends TreeService<DictDataMapper, DictData> {

	@Transactional(rollbackFor = Exception.class)
	public boolean deleteByDictType(String dictType) {
		return super.remove(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType));
	}
}
