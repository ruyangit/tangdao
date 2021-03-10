/**
 *
 */
package com.tangdao.developer.service;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.tangdao.core.constant.CommonApiCode;
import com.tangdao.core.constant.RabbitConstant;
import com.tangdao.core.context.CommonContext.PlatformType;
import com.tangdao.core.context.RabbitContext.WordsPriority;
import com.tangdao.core.context.TaskContext.TaskSubmitType;
import com.tangdao.core.exception.QueueProcessException;
import com.tangdao.core.model.domain.SmsMtTask;
import com.tangdao.developer.model.dto.SmsSendDTO;
import com.tangdao.developer.model.vo.SmsSendVo;

import cn.hutool.core.bean.BeanUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月23日
 */
@Service
public class SmsSendService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "smsRabbitTemplate")
	private RabbitTemplate smsRabbitTemplate;

	@Autowired
	private UserBalanceService userBalanceService;

	/**
	 * 
	 * TODO 发送短信
	 * 
	 * @param smsSendDTO
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public SmsSendVo sendMessage(SmsSendDTO smsSendDTO) {
		SmsMtTask task = new SmsMtTask();
		BeanUtil.copyProperties(smsSendDTO, task);
		task.setAppType(smsSendDTO.getAppType());
		try {
			long sid = joinTask2Queue(task, smsSendDTO.getUserId());
			if (sid != 0L) {
				return new SmsSendVo(smsSendDTO.getTotalFee(), sid);
			}
		} catch (Exception e) {
			log.error("发送短信至队列错误， {}", e);
		}
		throw new QueueProcessException(CommonApiCode.INTERNAL_ERROR);
	}

	/**
	 * 提交任务到队列
	 *
	 * @param task 主任务
	 * @return 消息ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public long joinTask2Queue(SmsMtTask task, String userId) {
		try {
			// 更新用户余额
			boolean isSuccess = userBalanceService.deductBalance(userId, -task.getTotalFee(),
					PlatformType.SEND_MESSAGE_SERVICE.getCode(), "developer call");
			if (!isSuccess) {
				log.error("用户: [" + userId + "] 扣除短信余额 " + task.getTotalFee() + " 失败");
				throw new QueueProcessException("发送短信扣除短信余额失败");
			}

			task.setSid(IdWorker.getId());
			task.setCreateDate(new Date());

			// 判断队列的优先级别
			int priority = WordsPriority.getLevel(task.getContent());

			String queueName = RabbitConstant.MQ_SMS_MT_WAIT_PROCESS;
			if (TaskSubmitType.POINT_TO_POINT.getCode() == task.getSubmitType()
					|| TaskSubmitType.TEMPLATE_POINT_TO_POINT.getCode() == task.getSubmitType()) {
				queueName = RabbitConstant.MQ_SMS_MT_P2P_WAIT_PROCESS;
			}
			smsRabbitTemplate.convertAndSend(queueName, task, (message) -> {
				message.getMessageProperties().setPriority(priority);
				return message;
			}, new CorrelationData(task.getSid().toString()));

			return task.getSid();
		} catch (Exception e) {
			log.error("发送短信队列失败", e);
			throw new QueueProcessException("发送短信队列失败");
		}
	}
}
