package com.tangdao.exchanger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tangdao.common.collect.ListUtils;
import org.tangdao.common.constant.RabbitConstant;
import org.tangdao.common.constant.RabbitConstant.WordsPriority;
import org.tangdao.common.lang.DateUtils;
import org.tangdao.common.lang.StringUtils;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.config.rabbit.RabbitMessageQueueManager;
import org.tangdao.modules.sms.config.rabbit.listener.SmsWaitSubmitListener;
import org.tangdao.modules.sms.mapper.SmsMtMessageSubmitMapper;
import org.tangdao.modules.sms.model.domain.SmsMtMessageDeliver;
import org.tangdao.modules.sms.model.domain.SmsMtMessagePush;
import org.tangdao.modules.sms.model.domain.SmsMtMessageSubmit;
import org.tangdao.modules.sms.model.domain.SmsMtTaskPackets;
import org.tangdao.modules.sms.model.domain.SmsPassage;
import org.tangdao.modules.sys.constant.PassageContext;
import org.tangdao.modules.sys.constant.PassageContext.DeliverStatus;
import org.tangdao.modules.sys.model.domain.User;
import org.tangdao.modules.sys.service.IUserService;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 下行短信提交ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMtMessageSubmitService extends CrudService<SmsMtMessageSubmitMapper, SmsMtMessageSubmit>
		implements ISmsMtSubmitService {

	@Autowired
	private IUserService userService;

	@Autowired
	private ISmsMtPushService smsMtPushService;
	
	@Autowired
	private ISmsMtDeliverService smsMtDeliverService;
	
	@Autowired
	private ISmsPassageService smsPassageService;

	@Resource
	private RabbitTemplate rabbitTemplate;

	@Autowired(required = false)
	private SmsWaitSubmitListener smsWaitSubmitListener;

	@Autowired(required = false)
	private RabbitMessageQueueManager rabbitMessageQueueManager;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public IPage<SmsMtMessageSubmit> page(IPage<SmsMtMessageSubmit> page, Wrapper<SmsMtMessageSubmit> queryWrapper) {
		IPage<SmsMtMessageSubmit> pageData = baseMapper.selectPage(page, queryWrapper);

		Map<String, SmsMtMessagePush> pushMap = new HashMap<>();

		// 加入内存对象，减少DB的查询次数
		Map<String, User> userMap = new HashMap<>();
		Map<String, String> passageMap = new HashMap<>();

		String key;
		for (SmsMtMessageSubmit record : pageData.getRecords()) {
			record.setMessageDeliver(smsMtDeliverService.findByMobileAndMsgid(record.getMobile(), record.getMsgId()));

			key = String.format("%s_%s", record.getMsgId(), record.getMobile());
			if (record.getUserCode() != null) {
				if (userMap.containsKey(record.getUserCode())) {
					record.setUser(userMap.get(record.getUserCode()));
				} else {
					record.setUser(userService.getByUserCode(record.getUserCode()));
					userMap.put(record.getUserCode(), record.getUser());
				}
			}

			if (record.getNeedPush()) {
				if (pushMap.containsKey(key)) {
					record.setMessagePush(pushMap.get(key));
				} else {
					record.setMessagePush(smsMtPushService.findByMobileAndMsgid(record.getMobile(), record.getMsgId()));
					pushMap.put(key, record.getMessagePush());
				}
			}
			// if(record.getStatus() == 0){
			// if(deliverMap.containsKey(key)) {
			// record.setMessageDeliver(deliverMap.get(key));
			// } else {
			// record.setMessageDeliver(smsMtDeliverService.findByMobileAndMsgid(record.getMobile(),record.getMsgId()));
			// deliverMap.put(key, record.getMessageDeliver());
			// }
			// }
			if (record.getPassageId() != null) {
				if (record.getPassageId() == PassageContext.EXCEPTION_PASSAGE_ID) {
					record.setPassageName(PassageContext.EXCEPTION_PASSAGE_NAME);
				} else {
					if (passageMap.containsKey(record.getUserCode())) {
						record.setPassageName(passageMap.get(record.getPassageId()));
					} else {
						SmsPassage passage = smsPassageService.findById(record.getPassageId());
						if (passage != null) {
							record.setPassageName(passage.getName());
							passageMap.put(record.getPassageId(), passage.getName());
						}
					}
				}
			}
		}
		return pageData;
	}

	@Override
	public SmsMtMessageSubmit findByMobileAndMsgid(String mobile, String msgId) {
		// TODO Auto-generated method stub
		QueryWrapper<SmsMtMessageSubmit> queryWrapper = new QueryWrapper<SmsMtMessageSubmit>();
		queryWrapper.eq("mobile", mobile);
		queryWrapper.eq("msg_id", msgId);
		return this.getOne(queryWrapper);
	}

//    @Override
//    public List<SmsMtMessageSubmit> findBySid(long sid) {
//    	QueryWrapper<SmsMtMessageSubmit> queryWrapper = new QueryWrapper<SmsMtMessageSubmit>();
//    	queryWrapper.eq("msg_id", sid);
//    	queryWrapper.eq("mobile", mobile);
//    	queryWrapper.orderByDesc("id").last(" limit 1");
//    	return super.select(queryWrapper);
//    }
//
////    @Override
////    public boolean save(SmsMtMessageSubmit submit) {
////        submit.setCreateTime(new Date());
////        return smsMtMessageSubmitMapper.insertSelective(submit) > 0;
////    }
//
////    @Override
////    public List<SmsMtMessageSubmit> findList(Map<String, Object> queryParams) {
////
////        changeTimestampeParamsIfExists(queryParams);
////
////        return smsMtMessageSubmitMapper.findList(queryParams);
////    }
////
////    /**
////     * 转换时间戳信息
////     *
////     * @param queryParams
////     */
////    private void changeTimestampeParamsIfExists(Map<String, Object> queryParams) {
////        String startDate = queryParams.get("startDate") == null ? "" : queryParams.get("startDate").toString();
////        String endDate = queryParams.get("endDate") == null ? "" : queryParams.get("endDate").toString();
////
////        if (StringUtils.isNotBlank(startDate)) {
////            queryParams.put("startDate", DateUtil.getSecondDate(startDate).getTime());
////        }
////
////        if (StringUtils.isNotBlank(endDate)) {
////            queryParams.put("endDate", DateUtil.getSecondDate(endDate).getTime());
////        }
////
////    }
//
//    /**
//     * 加入记录关联列数据（主要针对回执数据和推送数据）
//     *
//     * @param submits
//     */
//    private void joinRecordFascade(List<SmsMtMessageSubmit> submits) {
//        Map<String, SmsMtMessagePush> pushMap = new HashMap<>();
//
//        // 加入内存对象，减少DB的查询次数
////        Map<String, User> userModelMap = new HashMap<>();
//        Map<String, String> passageMap = new HashMap<>();
//
//        String key;
//        for (SmsMtMessageSubmit record : submits) {
//            key = String.format("%s_%s", record.getMsgId(), record.getMobile());
////            if (record.getUserCode() != null) {
////                if (userModelMap.containsKey(record.getUserCode())) {
////                    record.setUserModel(userModelMap.get(record.getUserCode()));
////                } else {
////                    record.setUserModel(userService.getByUserId(record.getUserCode()));
////                    userModelMap.put(record.getUserCode(), record.getUserModel());
////                }
////            }
//
//            if (record.getNeedPush()) {
//                if (pushMap.containsKey(key)) {
//                    record.setMessagePush(pushMap.get(key));
//                } else {
//                    record.setMessagePush(smsMtPushService.findByMobileAndMsgid(record.getMobile(), record.getMsgId()));
//                    pushMap.put(key, record.getMessagePush());
//                }
//            }
//            // if(record.getStatus() == 0){
//            // if(deliverMap.containsKey(key)) {
//            // record.setMessageDeliver(deliverMap.get(key));
//            // } else {
//            // record.setMessageDeliver(smsMtDeliverService.findByMobileAndMsgid(record.getMobile(),record.getMsgId()));
//            // deliverMap.put(key, record.getMessageDeliver());
//            // }
//            // }
//            if (record.getPassageId() != null) {
//                if (record.getPassageId() == PassageContext.EXCEPTION_PASSAGE_ID) {
//                    record.setPassageName(PassageContext.EXCEPTION_PASSAGE_NAME);
//                } else {
//                    if (passageMap.containsKey(record.getUserCode())) {
//                        record.setPassageName(passageMap.get(record.getPassageId()));
//                    } else {
//                        SmsPassage passage = smsPassageService.findById(record.getPassageId());
//                        if (passage != null) {
//                            record.setPassageName(passage.getName());
//                            passageMap.put(record.getPassageId(), passage.getName());
//                        }
//                    }
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public List<ConsumptionReport> getConsumeMessageInYestday() {
//        Set<Integer> userList = userService.findAvaiableUserIds();
//        if (ListUtils.isEmpty(userList)) {
//            throw new RuntimeException("用户数据异常，请检修");
//        }
//
//        List<ConsumptionReport> list = new ArrayList<>();
//
//        // 昨日日期
//        String yestday = DateUtil.getDayGoXday(-1);
//        List<SmsMtMessageSubmit> submits = smsMtMessageSubmitMapper.findByDate(yestday);
//
//        // 已存在的用户ID
//        Set<Integer> existsUsers = new HashSet<>();
//        ConsumptionReport report;
//        for (SmsMtMessageSubmit submit : submits) {
//            report = new ConsumptionReport();
//            report.setAmount(submit.getFee());
//            report.setType(PlatformType.SEND_MESSAGE_SERVICE.getCode());
//            report.setRecordDate(DateUtil.getDayDate(yestday));
//            report.setUserId(submit.getUserCode());
//
//            existsUsers.add(submit.getUserCode());
//            list.add(report);
//        }
//
//        userList.removeAll(existsUsers);
//
//        if (ListUtils.isNotEmpty(userList)) {
//            for (Integer userId : userList) {
//                report = new ConsumptionReport();
//                report.setAmount(0);
//                report.setType(PlatformType.SEND_MESSAGE_SERVICE.getCode());
//                report.setRecordDate(DateUtil.getDayDate(yestday));
//                report.setUserId(userId);
//
//                list.add(report);
//            }
//
//        }
//        return list;
//    }
//
//    @Override
//    public SmsLastestRecordVo findLastestRecord(String userCode, String mobile) {
//        SmsLastestRecordVo vo = new SmsLastestRecordVo();
//        vo.setMobile(mobile);
//        vo.setUserId(userId);
//
//        Map<String, Object> map = smsMtMessageSubmitMapper.selectByUserIdAndMobile(userId, mobile);
//        if (MapUtils.isEmpty(map)) {
//            vo.setDescrption("未找到相关记录");
//            return vo;
//        }
//
//        vo.setContent(map.get("content") == null ? "" : map.get("content").toString());
//        vo.setCreateTime(map.get("create_time").toString());
//        vo.setMessageNode(MessageNode.SMS_COMPLETE);
//        vo.setNodeTime(map.get("deliver_time") == null ? "" : map.get("create_time").toString());
//        Object sendStatus = map.get("send_status");
//        Object deliverStatus = map.get("deliver_status");
//
//        if (sendStatus == null) {
//            vo.setDescrption("短信发送未知");
//
//            return vo;
//        } else if (Integer.parseInt(sendStatus.toString()) == MessageSubmitStatus.FAILED.getCode()) {
//            vo.setDescrption("短信发送失败");
//
//            return vo;
//        } else if (deliverStatus == null) {
//            vo.setDescrption("待网关发送");
//
//            return vo;
//        } else if (Integer.parseInt(deliverStatus.toString()) == DeliverStatus.FAILED.getValue()) {
//            vo.setDescrption("短信发送失败，错误码：" + map.get("status_code"));
//
//            return vo;
//        }
//
//        vo.setDescrption("发送成功");
//        return vo;
//    }

	@Override
	public void batchInsertSubmit(List<SmsMtMessageSubmit> list) {
		if (ListUtils.isEmpty(list)) {
			return;
		}
		super.saveBatch(list);

		// SqlSession session =
		// sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH,
		// false);
		// SmsMtMessagesmsMtMessageSubmitMapper smsMtMessageSubmitMapper =
		// session.getMapper(SmsMtMessagesmsMtMessageSubmitMapper.class);
		// int size = 0;
		// try {
		// size = smsMtMessageSubmitMapper.batchInsert(list);
		// session.commit();
		// // 清理缓存，防止溢出
		// session.clearCache();
		// } catch (Exception e) {
		// // 没有提交的数据可以回滚
		// // session.rollback();
		// logger.error("短信提交数据入库失败", e);
		// } finally {
		// session.close();
		// }
		// return size;
	}
//
//    @Override
//    public SmsMtMessageSubmit getSubmitWaitReceipt(String msgId, String mobile) {
//        return smsMtMessageSubmitMapper.selectByMsgIdAndMobile(msgId, mobile);
//    }

	@Override
	public SmsMtMessageSubmit getByMoMapping(String passageId, String msgId, String mobile, String spcode) {
		QueryWrapper<SmsMtMessageSubmit> queryWrapper = null;

		SmsMtMessageSubmit smsMtMessageSubmit = null;
		if (passageId != null && StringUtils.isNotEmpty(msgId)) {
			queryWrapper = new QueryWrapper<SmsMtMessageSubmit>();
			queryWrapper.eq("passage_id", passageId);
			queryWrapper.eq("msg_id", msgId);
			queryWrapper.eq("mobile", mobile);
			queryWrapper.orderByDesc("id").last(" limit 1");
			return super.getOne(queryWrapper);
		}
		if (smsMtMessageSubmit == null && StringUtils.isNotEmpty(msgId)) {
			smsMtMessageSubmit = getByMsgidAndMobile(msgId, mobile);
		}
		if (smsMtMessageSubmit == null) {
			queryWrapper = new QueryWrapper<SmsMtMessageSubmit>();
			queryWrapper.eq("mobile", mobile);
			queryWrapper.orderByDesc("id").last(" limit 1");
			return super.getOne(queryWrapper);
		}
		return smsMtMessageSubmit;
	}

	@Override
	public SmsMtMessageSubmit getByMsgidAndMobile(String msgId, String mobile) {
		QueryWrapper<SmsMtMessageSubmit> queryWrapper = new QueryWrapper<SmsMtMessageSubmit>();
		queryWrapper.eq("msg_id", msgId);
		queryWrapper.eq("mobile", mobile);
		queryWrapper.orderByDesc("id").last(" limit 1");
		return super.getOne(queryWrapper);
	}

	@Override
	public SmsMtMessageSubmit getByMsgid(String msgId) {
		QueryWrapper<SmsMtMessageSubmit> queryWrapper = new QueryWrapper<SmsMtMessageSubmit>();
		queryWrapper.eq("msg_id", msgId);
		queryWrapper.orderByDesc("id").last(" limit 1");
		return super.getOne(queryWrapper);
	}

	@Override
	public boolean doSmsException(List<SmsMtMessageSubmit> submits) {
		List<SmsMtMessageDeliver> delivers = new ArrayList<>();
		SmsMtMessageDeliver deliver;
		for (SmsMtMessageSubmit submit : submits) {
			deliver = new SmsMtMessageDeliver();
			deliver.setCmcp(submit.getCmcp());
			deliver.setMobile(submit.getMobile());
			deliver.setMsgId(submit.getMsgId());
			deliver.setStatusCode(StringUtils.isNotEmpty(submit.getPushErrorCode()) ? submit.getPushErrorCode()
					: submit.getRemarks());
			deliver.setStatus(DeliverStatus.FAILED.getValue());
			deliver.setDeliverTime(DateUtils.getDate());
			deliver.setCreateTime(new Date());
			deliver.setRemarks(submit.getRemarks());
			delivers.add(deliver);
		}

		batchInsertSubmit(submits);

		// 判断短信是否需要推送，需要则设置推送设置信息
		setPushConfigurationIfNecessary(submits);

		try {
			smsMtDeliverService.doFinishDeliver(delivers);
			return true;
		} catch (Exception e) {
			logger.warn("伪造短信回执包信息错误", e);
			return false;
		}
	}

	@Override
	public void setPushConfigurationIfNecessary(List<SmsMtMessageSubmit> submits) {
		// add by 2018-03-24 取出第一个值的信息（推送设置一批任务为一致信息）
		SmsMtMessageSubmit submit = submits.iterator().next();
		if (submit.getNeedPush() == null || !submit.getNeedPush() || StringUtils.isEmpty(submit.getPushUrl())) {
			return;
		}

		// 异步设置需要推送的信息
		// threadPoolTaskExecutor.execute(new SetPushConfigThread(smsMtPushService,
		// submits));

		smsMtPushService.setMessageReadyPushConfigurations(submits);
	}

	@Override
	public boolean declareWaitSubmitMessageQueues() {
		List<String> passageCodes = smsPassageService.findPassageCodes();
		if (ListUtils.isEmpty(passageCodes)) {
			logger.error("无可用通道需要声明队列");
			return false;
		}

		try {
			for (String passageCode : passageCodes) {
				rabbitMessageQueueManager.createQueue(getSubmitMessageQueueName(passageCode),
						smsPassageService.isPassageBelongtoDirect(null, passageCode), smsWaitSubmitListener);
			}

			return true;
		} catch (Exception e) {
			logger.error("初始化消息队列异常");
			return false;
		}
	}

	@Override
	public String getSubmitMessageQueueName(String passageCode) {
		return String.format("%s.%s", RabbitConstant.MQ_SMS_MT_WAIT_SUBMIT, passageCode);
	}

//    @Override
//    public List<SmsMtMessageSubmit> getRecordListToMonitor(Long passageId, Long startTime, Long endTime) {
//        return smsMtMessageSubmitMapper.getRecordListToMonitor(passageId, startTime, endTime);
//    }

	@Override
	public boolean declareNewSubmitMessageQueue(String protocol, String passageCode) {
		String mqName = getSubmitMessageQueueName(passageCode);
		try {
			rabbitMessageQueueManager.createQueue(mqName,
					smsPassageService.isPassageBelongtoDirect(protocol, passageCode), smsWaitSubmitListener);
			logger.info("RabbitMQ添加新队列：{} 成功", mqName);
			return true;
		} catch (Exception e) {
			logger.error("声明新队列：{}失败", passageCode);
			return false;
		}
	}

	@Override
	public boolean removeSubmitMessageQueue(String passageCode) {
		String mqName = getSubmitMessageQueueName(passageCode);
		boolean isSuccess = rabbitMessageQueueManager.removeQueue(mqName);
		if (isSuccess) {
			logger.info("RabbitMQ移除队列：{} 成功", mqName);
		} else {
			logger.error("RabbitMQ移除队列：{} 失败", mqName);
		}

		return isSuccess;
	}

	@Override
	public boolean sendToSubmitQueue(List<SmsMtTaskPackets> packets) {
		if (ListUtils.isEmpty(packets)) {
			logger.warn("子任务数据为空，无需发送队列");
			return false;
		}

		// 发送至待提交信息队列处理
		Map<String, String> passageCodesMap = new HashMap<>();
		for (SmsMtTaskPackets packet : packets) {
			try {
				String passageCode = getPassageCode(passageCodesMap, packet);
				if (StringUtils.isEmpty(passageCode)) {
					logger.error("子任务通道数据为空，无法进行通道代码分队列处理，通道ID：{}", packet.getFinalPassageId());
					continue;
				}

				rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE_SMS, getSubmitMessageQueueName(passageCode),
						packet, (message) -> {

							message.getMessageProperties().setPriority(WordsPriority.getLevel(packet.getContent()));
							return message;
						}, new CorrelationData(packet.getSid() + ""));
			} catch (Exception e) {
				logger.error("子任务发送至待提交任务失败，信息为：{}", JSON.toJSONString(packet), e);
			}
		}
		return true;
	}

	/**
	 * 根据子任务中的通道获取通道代码信息
	 *
	 * @param passageCodesMap
	 * @param packet
	 * @return
	 */
	private String getPassageCode(Map<String, String> passageCodesMap, SmsMtTaskPackets packet) {
		if (StringUtils.isEmpty(packet.getPassageCode())) {
			if (passageCodesMap.containsKey(packet.getPassageId())) {
				return passageCodesMap.get(packet.getPassageId());
			}

			SmsPassage passage = smsPassageService.findById(packet.getFinalPassageId());
			if (passage == null) {
				return null;
			}

			passageCodesMap.put(passage.getId(), passage.getCode());
			return passage.getCode();
		}

		return packet.getPassageCode();
	}
//
//    @Override
//    public List<Map<String, Object>> getSubmitStatReport(Long startTime, Long endTime) {
//
//        if (startTime == null || endTime == null) {
//            return null;
//        }
//
//        List<Map<String, Object>> list = smsMtMessageSubmitMapper.selectSubmitReport(startTime, endTime);
//        if (ListUtils.isEmpty(list)) {
//            return null;
//        }
//
//        return list;
//    }
//
//    @Override
//    public List<Map<String, Object>> getLastHourSubmitReport() {
//        // 截止时间为前一个小时0分0秒
//        Long endTime = DateUtil.getXHourWithMzSzMillis(-1);
//        // 开始时间为前2个小时0分0秒
//        Long startTime = DateUtil.getXHourWithMzSzMillis(-2);
//
//        return getSubmitStatReport(startTime, endTime);
//    }
//
//    @Override
//    public List<Map<String, Object>> getSubmitCmcpReport(Long startTime, Long endTime) {
//        if (startTime == null || endTime == null) {
//            return null;
//        }
//
//        return smsMtMessageSubmitMapper.selectCmcpReport(startTime, endTime);
//    }
}