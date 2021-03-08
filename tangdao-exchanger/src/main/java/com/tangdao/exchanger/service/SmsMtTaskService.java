package com.tangdao.exchanger.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tangdao.common.collect.ListUtils;
import org.tangdao.common.lang.StringUtils;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.mapper.SmsMtTaskMapper;
import org.tangdao.modules.sms.model.domain.SmsMtTask;
import org.tangdao.modules.sms.service.ISmsForbiddenWordsService;
import org.tangdao.modules.sms.service.ISmsMtTaskPacketsService;
import org.tangdao.modules.sms.service.ISmsMtTaskService;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 下行短信任务ServiceImpl
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMtTaskService extends CrudService<SmsMtTaskMapper, SmsMtTask> implements ISmsMtTaskService{
	
	@Autowired
	private ISmsMtTaskPacketsService smsMtTaskPacketsService;
	@Autowired
	private ISmsForbiddenWordsService smsForbiddenWordsService;
//	@Autowired
//	private IUserService userService;
	

	private final Logger               logger = LoggerFactory.getLogger(getClass());
	
	@Override
    public boolean save(SmsMtTask task) {
        if (task == null) {
            logger.error("任务数据为空，处理失败");
            return false;
        }

        boolean effect = super.save(task);
        if (effect) {
            if (ListUtils.isNotEmpty(task.getPackets())) {
            	smsMtTaskPacketsService.saveBatch(task.getPackets());
            }
        }
        return effect;
    }
	
	@Override
	public IPage<SmsMtTask> page(IPage<SmsMtTask> page, Wrapper<SmsMtTask> queryWrapper) {
		IPage<SmsMtTask> pageData = baseMapper.selectPage(page, queryWrapper);
		pageData.getRecords().stream().forEach(r->{
			//设置当前用户信息
//			r.setUser(userService.get(r.getUserCode()));
			if (StringUtils.isNotBlank(r.getForbiddenWords())) {
				r.setForbiddenWordLabels(smsForbiddenWordsService.getLabelByWords(r.getForbiddenWords()));
            }
		});
		return pageData;
	}
}