/**
 *
 */
package com.tangdao.service.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.service.mapper.DictTypeMapper;
import com.tangdao.service.model.domain.DictType;
import com.tangdao.service.model.dto.DictTypeDTO;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
@Service
public class DictTypeService extends BaseService<DictTypeMapper, DictType> {

	@Autowired
	private DictDataService dictDataService;

	public boolean checkDictTypeExists(String oldDictType, String dictType) {
		if (dictType != null && dictType.equals(oldDictType)) {
			return true;
		} else if (dictType != null
				&& super.count(Wrappers.<DictType>lambdaQuery().eq(DictType::getDictType, dictType)) == 0) {
			return true;
		}
		return false;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean deleteDictType(DictTypeDTO dictType) {
		DictType dt = super.getById(dictType.getId());
		super.removeById(dt.getId());
		this.dictDataService.deleteByDictType(dt.getDictType());
		return true;
	}
}
