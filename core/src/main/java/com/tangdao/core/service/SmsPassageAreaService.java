package com.tangdao.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.tangdao.core.dao.SmsPassageAreaMapper;
import com.tangdao.core.model.domain.SmsPassageArea;
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
public class SmsPassageAreaService extends BaseService<SmsPassageAreaMapper, SmsPassageArea> {

	public List<SmsPassageArea> selectSmsPassageAreaByPassageId(String passageId) {
		return this.list(Wrappers.<SmsPassageArea>lambdaQuery().eq(SmsPassageArea::getPassageId, passageId));
	}

	public boolean deleteByPassageId(String passageId) {
		return SqlHelper.retBool(this.baseMapper.deleteByPassageId(passageId));
	}
}