/**
 *
 */
package com.tangdao.service.provider;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.service.mapper.DictTypeMapper;
import com.tangdao.service.model.domain.DictType;

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

	public boolean checkDictTypeExists(String oldDictType, String dictType) {
		if (dictType != null && oldDictType.equals(dictType)) {
			return true;
		} else if (dictType != null
				&& super.count(Wrappers.<DictType>lambdaQuery().eq(DictType::getDictType, dictType)) == 0) {
			return true;
		}
		return false;
	}
}
