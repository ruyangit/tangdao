package com.tangdao.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.dao.PassageTemplateMapper;
import com.tangdao.core.model.domain.PassageTemplate;
import com.tangdao.core.model.domain.PassageTemplateDetail;
import com.tangdao.core.model.vo.TemplateDetail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class PassageTemplateService extends BaseService<PassageTemplateMapper, PassageTemplate> {

	@Autowired
	private PassageTemplateDetailService passageTemplateDetailService;

	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdatePassageTemplate(PassageTemplate passageTemplate) {
		super.saveOrUpdate(passageTemplate);
		final String templateId = passageTemplate.getId();
		this.passageTemplateDetailService.deleteByTemplateId(templateId);
		if (passageTemplate.getUrl() != null) {
			for (int i = 0; i < passageTemplate.getCallType().size(); i++) {

				String callType = passageTemplate.getCallType().get(i);
				String url = passageTemplate.getUrl().get(i);
				String successCode = passageTemplate.getSuccessCode().get(i);

				PassageTemplateDetail ptd = new PassageTemplateDetail();
				ptd.setTemplateId(templateId);
				ptd.setCallType(callType);
				ptd.setUrl(url);
				ptd.setSuccessCode(successCode);

				if (passageTemplate.getTemplateDetail() != null && passageTemplate.getTemplateDetail().size() > i) {
					List<TemplateDetail> details = passageTemplate.getTemplateDetail().get(i);
					if (CollUtil.isNotEmpty(details)) {
						ptd.setParams(JSON.toJSONString(details));
					}
				}

				if (StrUtil.isEmpty(ptd.getUrl()) && StrUtil.isEmpty(ptd.getSuccessCode())
						&& StrUtil.isEmpty(ptd.getParams())) {
					continue;
				}
				this.passageTemplateDetailService.save(ptd);
			}
		}
	}

	public boolean deletePt(PassageTemplate passageTemplate) {
		super.removeById(passageTemplate);
		this.passageTemplateDetailService.deleteByTemplateId(passageTemplate.getId());
		return true;
	}
}