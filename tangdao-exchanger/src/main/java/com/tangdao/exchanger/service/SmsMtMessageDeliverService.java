package com.tangdao.exchanger.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.model.domain.sms.MtMessageDeliver;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.SmsMtMessageDeliverMapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 下行短信回执状态ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMtMessageDeliverService extends BaseService<SmsMtMessageDeliverMapper, MtMessageDeliver>{

	@Autowired
	private SmsMtPushService smsMtPushService;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public MtMessageDeliver findByMobileAndMsgid(String mobile, String msgId) {
		if (StrUtil.isEmpty(mobile) || StrUtil.isEmpty(msgId)) {
			return null;
		}

		QueryWrapper<MtMessageDeliver> queryWrapper = new QueryWrapper<MtMessageDeliver>();
		queryWrapper.eq("mobile", mobile);
		queryWrapper.eq("msg_id", msgId);
		queryWrapper.last(" limit 1");
		return super.getOne(queryWrapper);
	}

	public void batchInsert(List<MtMessageDeliver> list) {
		if (CollUtil.isEmpty(list)) {
			return;
		}
		super.saveBatch(list);
	}

	public int doFinishDeliver(List<MtMessageDeliver> delivers) {
		if (CollUtil.isEmpty(delivers)) {
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