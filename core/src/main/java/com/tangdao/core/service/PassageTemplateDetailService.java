package com.tangdao.core.service;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.dao.PassageTemplateDetailMapper;
import com.tangdao.core.model.domain.PassageTemplateDetail;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class PassageTemplateDetailService extends BaseService<PassageTemplateDetailMapper, PassageTemplateDetail> {

	@Transactional(rollbackFor = Exception.class)
	public void deleteByTemplateId(String templateId) {
		super.remove(
				Wrappers.<PassageTemplateDetail>lambdaUpdate().eq(PassageTemplateDetail::getTemplateId, templateId));
	}
}