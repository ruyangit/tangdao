package com.tangdao.exchanger.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.tangdao.core.dao.SmsPassageAreaMapper;
import com.tangdao.core.model.domain.PassageArea;
import com.tangdao.core.service.BaseService;

/**
 * 通道支持省份ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPassageAreaService extends BaseService<SmsPassageAreaMapper, PassageArea> {

	public List<PassageArea> selectSmsPassageAreaByPassageId(String passageId) {
		return this.list(Wrappers.<PassageArea>lambdaQuery().eq(PassageArea::getPassageId, passageId));
	}

	public boolean deleteByPassageId(String passageId) {
		return SqlHelper.retBool(this.baseMapper.deleteByPassageId(passageId));
	}
}