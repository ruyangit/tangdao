package com.tangdao.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.tangdao.core.dao.PassageAreaMapper;
import com.tangdao.core.model.domain.PassageArea;
import com.tangdao.core.service.BaseService;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class PassageAreaService extends BaseService<PassageAreaMapper, PassageArea> {

	public List<PassageArea> selectSmsPassageAreaByPassageId(String passageId) {
		return this.list(Wrappers.<PassageArea>lambdaQuery().eq(PassageArea::getPassageId, passageId));
	}

	public boolean deleteByPassageId(String passageId) {
		return SqlHelper.retBool(this.baseMapper.deleteByPassageId(passageId));
	}
}