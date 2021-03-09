package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.model.domain.PassageTemplateDetail;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.PassageTemplateDetailMapper;

/**
 * 通道模板内容ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class PassageTemplateDetailService extends BaseService<PassageTemplateDetailMapper, PassageTemplateDetail> {

	@Transactional(rollbackFor = Exception.class)
	public void deleteByTemplateId(String templateId) {
		super.remove(
				Wrappers.<PassageTemplateDetail>lambdaUpdate().eq(PassageTemplateDetail::getTemplateId, templateId));
	}
}