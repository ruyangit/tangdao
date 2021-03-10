package com.tangdao.core.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.dao.SmsMtTaskMapper;
import com.tangdao.core.model.domain.SmsMtTask;

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

	public IPage<SmsMtTask> page(Page<SmsMtTask> page, Wrapper<SmsMtTask> queryWrapper) {
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