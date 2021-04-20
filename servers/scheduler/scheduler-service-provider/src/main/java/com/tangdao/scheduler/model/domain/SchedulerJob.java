/**
 *
 */
package com.tangdao.scheduler.model.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月13日
 */
@Getter
@Setter
@TableName("sys_job")
public class SchedulerJob extends DataEntity<SchedulerJob> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String jobKey;

	private String jobName;

	private String jobGroup;

	private String jobCron;

	private String executorStrategy;

	private String executorHandler;

	private String executorParam;

	private String executorBlockStrategy;

	private Long executorTimeout;

	private Long executorFailRetryCount;

	private Date triggerLastTime;

	private Date triggerNextTime;

	private String status;

	private String alarmEmail;

	private String remarks;

}
