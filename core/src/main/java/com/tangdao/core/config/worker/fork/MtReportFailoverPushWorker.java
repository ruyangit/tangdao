package com.tangdao.core.config.worker.fork;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.tangdao.core.config.worker.AbstractWorker;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.model.domain.SmsMtMessageDeliver;
import com.tangdao.core.service.SmsMtMessagePushService;

import cn.hutool.core.collection.CollUtil;

/**
 * 
 * <p>
 * TODO 针对短信下行报告推送（首次数据未入库或者REDIS无相关数据，后续追加推送）
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class MtReportFailoverPushWorker extends AbstractWorker<SmsMtMessageDeliver> {

	@Override
	protected String jobTitle() {
		return "短信状态补偿轮训";
	}

	public MtReportFailoverPushWorker(ApplicationContext applicationContext) {
		super(applicationContext);
	}

	@Override
	protected void operate(List<SmsMtMessageDeliver> list) {
		if (CollUtil.isEmpty(list)) {
			return;
		}
		getInstance(SmsMtMessagePushService.class).compareAndPushBody(list);
	}

	@Override
	protected String redisKey() {
		return SmsRedisConstant.RED_QUEUE_SMS_DELIVER_FAILOVER;
	}

	@Override
	protected int scanSize() {
		return 1000;
	}

	@Override
	protected long timeout() {
		return super.timeout();
	}

}
