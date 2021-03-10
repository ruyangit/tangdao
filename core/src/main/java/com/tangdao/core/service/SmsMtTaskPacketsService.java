package com.tangdao.core.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.dao.SmsMtTaskPacketsMapper;
import com.tangdao.core.model.domain.Area;
import com.tangdao.core.model.domain.SmsMtTaskPackets;
import com.tangdao.core.model.domain.SmsPassage;

import cn.hutool.core.util.StrUtil;

/**
 * 下行短信任务分包ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMtTaskPacketsService extends BaseService<SmsMtTaskPacketsMapper, SmsMtTaskPackets> {

	@Autowired
	private AreaService areaService;

	@Autowired
	private SmsPassageService smsPassageService;

	public SmsMtTaskPackets getSmsMtTaskPackets(SmsMtTaskPackets smsMtTaskPackets) {
		if (smsMtTaskPackets == null || StrUtil.isEmpty(smsMtTaskPackets.getId())) {
			smsMtTaskPackets = super.getById(smsMtTaskPackets);
		}
		if (smsMtTaskPackets != null) {
			// 组装省份信息
			if (StrUtil.isNotBlank(smsMtTaskPackets.getAreaCode())) {
				// 根据省份代码查询省份名称
				Area area = areaService.getById(smsMtTaskPackets.getAreaCode());
				smsMtTaskPackets.setAreaName(area == null ? "未知" : area.getAreaName());
			}
			// 组装通道信息
			if (StrUtil.isNotBlank(smsMtTaskPackets.getFinalPassageId())) {
				SmsPassage passage = smsPassageService.findById(smsMtTaskPackets.getFinalPassageId());
				smsMtTaskPackets.setPassageName(passage == null ? "未知" : passage.getName());
			}
		}
		return smsMtTaskPackets;
	}

	public IPage<SmsMtTaskPackets> page(Page<SmsMtTaskPackets> page, Wrapper<SmsMtTaskPackets> queryWrapper) {
		IPage<SmsMtTaskPackets> pageData = baseMapper.selectPage(page, queryWrapper);
		Map<String, String> areaMap = new HashMap<>();
		Map<String, String> passageMap = new HashMap<>();
		pageData.getRecords().stream().forEach(r -> {
			// 组装省份信息
			if (StrUtil.isNotBlank(r.getAreaCode())) {
				if (areaMap.containsKey(r.getAreaCode())) {
					r.setAreaName(areaMap.get(r.getAreaCode()));
				} else {
					// 根据省份代码查询省份名称
					Area area = areaService.getById(r.getAreaCode());
					r.setAreaName(area == null ? "未知" : area.getAreaName());
					areaMap.put(r.getAreaCode(), r.getAreaName());
				}
			}

			// 组装通道信息
			if (StrUtil.isNotBlank(r.getFinalPassageId())) {
				if (passageMap.containsKey(r.getFinalPassageId())) {
					r.setPassageName(passageMap.get(r.getFinalPassageId()));
				} else {
					SmsPassage passage = smsPassageService.findById(r.getFinalPassageId());
					r.setPassageName(passage == null ? "未知" : passage.getName());
					passageMap.put(r.getFinalPassageId(), r.getPassageName());
				}
			}
		});
		return pageData;
	}
}