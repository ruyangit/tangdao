/**
 *
 */
package com.tangdao.scheduler.model.domain;

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
@TableName
public class SchedulerJobLog extends DataEntity<SchedulerJobLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
