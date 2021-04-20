/**
 *
 */
package com.tangdao.scheduler.service;

import javax.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月13日
 */
@Service
public class QuartzSchedulerService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Scheduler scheduler;
	
	@PostConstruct
	public void startScheduler() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error("定时任务初始化，异常：{}", e.getMessage());
		}
	}
	
}
