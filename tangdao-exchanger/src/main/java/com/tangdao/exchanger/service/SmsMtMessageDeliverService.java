package com.tangdao.exchanger.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.tangdao.common.collect.ListUtils;
import org.tangdao.common.lang.StringUtils;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.constant.SmsRedisConstant;
import org.tangdao.modules.sms.mapper.SmsMtMessageDeliverMapper;
import org.tangdao.modules.sms.model.domain.SmsMtMessageDeliver;
import org.tangdao.modules.sms.service.ISmsMtDeliverService;
import org.tangdao.modules.sms.service.ISmsMtPushService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 下行短信回执状态ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMtMessageDeliverService extends CrudService<SmsMtMessageDeliverMapper, SmsMtMessageDeliver>
		implements ISmsMtDeliverService {

	@Autowired
	private ISmsMtPushService smsMtPushService;
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public SmsMtMessageDeliver findByMobileAndMsgid(String mobile, String msgId) {
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(msgId)) {
			return null;
		}

		QueryWrapper<SmsMtMessageDeliver> queryWrapper = new QueryWrapper<SmsMtMessageDeliver>();
		queryWrapper.eq("mobile", mobile);
		queryWrapper.eq("msg_id", msgId);
		queryWrapper.last(" limit 1");
		return super.getOne(queryWrapper);
	}

	@Override
	public void batchInsert(List<SmsMtMessageDeliver> list) {
		if (ListUtils.isEmpty(list)) {
			return;
		}
		super.saveBatch(list);
	}

	@Override
	public int doFinishDeliver(List<SmsMtMessageDeliver> delivers) {
		if (CollectionUtils.isEmpty(delivers)) {
			return 0;
		}

		try {

			// 将待推送消息发送至用户队列进行处理（2017-03-20 合包处理），异步执行
			smsMtPushService.compareAndPushBody(delivers);

			batchInsert(delivers);

			logger.info("Deliver messages[" + JSON.toJSONString(delivers) + "] has enqueued");

			return delivers.size();
		} catch (Exception e) {
			logger.error("处理待回执信息REDIS失败，失败信息：{}", JSON.toJSONString(delivers), e);
			throw new RuntimeException("状态报告回执处理失败");
		}
	}

	@Override
	public boolean doDeliverToException(JSONObject obj) {
		try {
			return stringRedisTemplate.opsForList()
					.rightPush(SmsRedisConstant.RED_MESSAGE_STATUS_RECEIPT_EXCEPTION_LIST, JSON.toJSONString(obj)) > 0;
		} catch (Exception e) {
			logger.error("发送回执错误信息失败 {}", JSON.toJSON(obj), e);
			return false;
		}
	}
}