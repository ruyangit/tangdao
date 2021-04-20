/**
 *
 */
package com.tangdao.scheduler.web;

import com.tangdao.core.config.Global;

import lombok.experimental.UtilityClass;

/**
 * <p>
 * TODO 聚合API
 * </p>
 *
 * @author ruyang
 * @since 2021年4月20日
 */
@UtilityClass
public class ApiPath {

	@UtilityClass
	public class SchedulerJob {
		public static final String ROOT = Global.ROOT + "/scheduler-job";
		public static final String SCHEDULER_JOB_LOG = Global.ROOT + "/scheduler-job-log";
	}
}
