package com.tangdao.core.config.worker.fork;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.tangdao.core.config.worker.AbstractWorker;
import com.tangdao.core.service.SmsMtMessagePushService;

import cn.hutool.core.collection.CollUtil;

/**
 * 
 * <p>
 * TODO 针对短信下行报告推送给下家（首次数据未入库或者REDIS无相关数据，后续追加推送）
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class MtReportPushToDeveloperWorker extends AbstractWorker<JSONObject> {

	private String developerPushQueueName;

	public MtReportPushToDeveloperWorker(ApplicationContext applicationContext) {
		super(applicationContext);
	}

	public MtReportPushToDeveloperWorker(ApplicationContext applicationContext, String developerPushQueueName) {
		super(applicationContext);
		this.developerPushQueueName = developerPushQueueName;
	}

	@Override
	protected void operate(List<JSONObject> list) {
		if (CollUtil.isEmpty(list)) {
			return;
		}
		getInstance(SmsMtMessagePushService.class).pushMessageBodyToDeveloper(list);
	}

	@Override
	protected String redisKey() {
		return developerPushQueueName;
	}

	@Override
	protected String jobTitle() {
		return "短信状态报告推送";
	}

	@Override
	protected long timeout() {
		return super.timeout();
	}

}
