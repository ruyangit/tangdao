package com.tangdao.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tangdao.core.dao.SmsMtTaskMapper;
import com.tangdao.core.model.domain.SmsMtTask;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 下行短信任务ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMtTaskService extends BaseService<SmsMtTaskMapper, SmsMtTask> {

	@Autowired
	private SmsMtTaskPacketsService smsMtTaskPacketsService;
	
	@Autowired
	private SmsForbiddenWordsService smsForbiddenWordsService;

	public boolean save(SmsMtTask task) {
		if (task == null) {
			logger.error("任务数据为空，处理失败");
			return false;
		}

		boolean effect = super.save(task);
		if (effect) {
			if (CollUtil.isNotEmpty(task.getPackets())) {
				smsMtTaskPacketsService.saveBatch(task.getPackets());
			}
		}
		return effect;
	}

	public IPage<SmsMtTask> page(IPage<SmsMtTask> page, Wrapper<SmsMtTask> queryWrapper) {
		IPage<SmsMtTask> pageData = this.getBaseMapper().selectPage(page, queryWrapper);
		pageData.getRecords().stream().forEach(r -> {
			// 设置当前用户信息
//			r.setUser(userService.get(r.getUserCode()));
			if (StrUtil.isNotBlank(r.getForbiddenWords())) {
				r.setForbiddenWordLabels(smsForbiddenWordsService.getLabelByWords(r.getForbiddenWords()));
			}
		});
		return pageData;
	}
}