package com.tangdao.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.context.PassageContext.DeliverStatus;
import com.tangdao.core.context.PassageContext.PushStatus;
import com.tangdao.core.dao.SmsMtMessageDeliverMapper;
import com.tangdao.core.dao.SmsMtMessagePushMapper;
import com.tangdao.core.model.domain.SmsMtMessageDeliver;
import com.tangdao.core.model.domain.SmsMtMessagePush;
import com.tangdao.core.model.domain.SmsMtMessageSubmit;
import com.tangdao.core.model.vo.SmsPushReport;
import com.tangdao.core.utils.HttpClientUtil;
import com.tangdao.core.utils.HttpClientUtil.RetryResponse;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class SmsMtMessagePushService extends BaseService<SmsMtMessagePushMapper, SmsMtMessagePush> {

	@Autowired
	private SmsMtMessageDeliverMapper smsMtMessageDeliverMapper;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SmsMtMessageSubmitService smsMtSubmitService;

	@Value("${thread.poolsize.push:2}")
	private int pushThreadPoolSize;

	@Value("${thread.poolsize:5}")
	private int failoverThreadPoolSize;

	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	/**
	 * 重推回执ID集合间
	 */
	private static final String REPUSH_DELIVER_IDS_KEY = "repush_deliver_ids:";

	/**
	 * 重推送锁
	 */
	private static final Lock REPUSH_MONITOR = new ReentrantLock();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 用户推送报告过滤器
	 */
	private static final PropertyPreFilter USER_PUSH_REPORT_FILTER = new SimplePropertyPreFilter("sid", "mobile",
			"attach", "status", "receiveTime", "errorMsg");

	/**
	 * 用户推送线程映射标识
	 */
	private static volatile Map<String, Long> USER_PUTH_THREAD_FLAG = new ConcurrentHashMap<>();

	/**
	 * 添加用户推送线程锁
	 */
	private static final Lock ADD_PUSH_THREAD_MONITOR = new ReentrantLock();

	/**
	 * 推送分包分割主键（根据推送地址进行切割）
	 */
	private static final String PUSH_BODY_SUBPACKAGE_KEY = "pushUrl";

	public boolean doListenerAllUser() {
		// never invoke, add thread when userId has pushed data
		// Set<Integer> userIds = userService.findAvaiableUserIds();
		// if (CollectionUtils.isEmpty(userIds)) {
		// logger.error("待推送可用用户数据为空，无法监听");
		// return false;
		// }
		//
		// try {
		// for (Integer userId : userIds) {
		// addUserMtPushListener(userId);
		// }
		//
		// return true;
		// } catch (Exception e) {
		// logger.info("用户初始化下行推送队列失败", e);
		// }

		return false;
	}

	/**
	 * 获取当前用户[userId]对应的状态报告推送队列名称
	 * 
	 * @param userId 用户ID，每个用户ID不同的队列（用户独享队列，非共享一个推送队列）
	 * @return 推送队列名称
	 */

	public String getUserPushQueueName(String userId) {
		return String.format("%s:%s", SmsRedisConstant.RED_QUEUE_SMS_MT_WAIT_PUSH, userId);
	}

	/**
	 * 组装报文前半部分信息
	 * 
	 * @param body                  报文信息（上下文使用）
	 * @param deliver               网关回执数据
	 * @param cachedPushArgs        本地缓存推送参数信息（为了加快速度缓存变量，而不是每次都根据MSG_ID和MOBILE查询REDIS或者DB）
	 * @param failoverDeliversQueue 本次处理失败（一般是查询REDIS或者DB都没有推送配置信息）重入待处理回执状态（后续线程重试）
	 * @return true/false
	 */
	private boolean assembleBody(JSONObject body, SmsMtMessageDeliver deliver, Map<String, JSONObject> cachedPushArgs,
			List<SmsMtMessageDeliver> failoverDeliversQueue) {
		// 如果本地缓存中存在相关值，则直接获取，无需请求REDIS或DB
		if (CollUtil.isNotEmpty(cachedPushArgs) && cachedPushArgs.containsKey(deliver.getMsgId())) {
			body.putAll(cachedPushArgs.get(deliver.getMsgId()));
			return true;
		}

		try {
			JSONObject redisArgs = getWaitPushBodyArgs(deliver.getMsgId(), deliver.getMobile());
			if (redisArgs == null) {
				// 如果数据生成时间超过[5分钟]舍弃，不再重新补偿
				// if(System.currentTimeMillis() - deliver.getCreateTime().getTime() >= 5 * 60 *
				// 1000 )

				if (System.currentTimeMillis() - deliver.getCreateDate().getTime() >= 20 * 1000) {
					return false;
				}

				failoverDeliversQueue.add(deliver);
			} else {
				body.putAll(redisArgs);
				cachedPushArgs.put(deliver.getMsgId(), body);
				return true;
			}

		} catch (IllegalStateException e) {
			logger.warn(e.getMessage());
		}

		return false;
	}

	/**
	 * 回执信息按照用户分包，上家一次性回执多个回执报文，报文中很可能是我司多个用户数据回执信息，顾要分包
	 * 
	 * @param body       单个报文信息
	 * @param deliver    上家回执报文信息
	 * @param userBodies 本次需要处理的过程累计的报文集合信息
	 * @return
	 */
	private boolean subQueue(JSONObject body, SmsMtMessageDeliver deliver, Map<String, List<JSONObject>> userBodies) {
		// edit by ruyang 将SID转型为string（部分客户提到推送到客户侧均按照字符类型去解析，顾做了转义）
		body.put("sid", body.getLong("sid").toString());
		body.put("mobile", deliver.getMobile());
		body.put("status", deliver.getStatusCode());
		body.put("receiveTime", deliver.getDeliverTime());
		body.put("errorMsg", Integer.parseInt(deliver.getStatus()) == DeliverStatus.SUCCESS.getValue() ? ""
				: deliver.getStatusCode());

		try {
			// 如果本次处理的用户ID已经包含在上下文处理集合中，则直接追加即可
			if (CollUtil.isNotEmpty(userBodies) && userBodies.containsKey(body.getString("userId"))) {
				userBodies.get(body.getString("userId")).add(body);
			} else {
				// 如果未曾处理过，则重新初始化集合
				// List<JSONObject> ds = new ArrayList<>();
				// ds.add(body);
				userBodies.put(body.getString("userId"), new ArrayList<>(Arrays.asList(body)));
			}

			return true;
		} catch (Exception e) {
			logger.error("解析推送数据报告异常:{}", body.toJSONString(), e);
			return false;
		}
	}

	/**
	 * 添加用户推送线程
	 * 
	 * @param userId 用户ID
	 */
	private void addThreadIfNessary(String userId) {
		if (USER_PUTH_THREAD_FLAG.containsKey(userId)) {
			return;
		}

		boolean isOk = addUserMtPushListener(userId);
		if (isOk) {
			USER_PUTH_THREAD_FLAG.putIfAbsent(userId, System.currentTimeMillis());
			logger.info("There are " + (USER_PUTH_THREAD_FLAG.size() * 2) + " threads[" + USER_PUTH_THREAD_FLAG.keySet()
					+ "] joined");
		}

	}

	/**
	 * 比较报文内容并完成加入推送redis队列（方法异步）
	 * 
	 * @param delivers
	 * @return
	 */

	@Async
	public Future<Boolean> compareAndPushBody(List<SmsMtMessageDeliver> delivers) {
		if (CollectionUtils.isEmpty(delivers)) {
			return new AsyncResult<Boolean>(false);
		}

		Map<String, JSONObject> cachedPushArgs = new HashMap<>();
		// 用户ID对应的 推送报告集合数据
		Map<String, List<JSONObject>> userBodies = new HashMap<>();
		// 针对上家回执数据已回，但我方回执数据未入库以及REDIS没有推送配置信息，后续线程重试补完成
		List<SmsMtMessageDeliver> failoverDeliversQueue = new ArrayList<>();

		try {
			for (SmsMtMessageDeliver deliver : delivers) {
				if (deliver == null) {
					continue;
				}

				JSONObject body = new JSONObject();
				if (!assembleBody(body, deliver, cachedPushArgs, failoverDeliversQueue)) {
					continue;
				}

				removeReadyMtPushConfig(deliver.getMsgId(), deliver.getMobile());

				// 如果用户推送地址为空则表明不需要推送
				if (StrUtil.isEmpty(body.getString(PUSH_BODY_SUBPACKAGE_KEY))) {
					continue;
				}

				if (!subQueue(body, deliver, userBodies)) {
					continue;
				}
			}

			// 如果针对上家已回执我方未入库数据存在则保存至REDIS
			if (CollUtil.isNotEmpty(failoverDeliversQueue)) {
				sendToDeliverdFailoverQueue(failoverDeliversQueue);
			}

			// 根据用户ID分别组装数据，并发送至各自队列, key:userId, value:bodies（推送报文数据）
			for (Entry<String, List<JSONObject>> userBody : userBodies.entrySet()) {

				// 加入用户独立推送线程
				addThreadIfNessary(userBody.getKey());

				stringRedisTemplate.opsForList().rightPush(getUserPushQueueName(userBody.getKey()),
						JSON.toJSONString(userBody.getValue()));
			}
			return new AsyncResult<Boolean>(true);
		} catch (Exception e) {
			logger.error("将上家回执数据发送至待推送队列逻辑失败，回执数据为：{}", JSON.toJSONString(delivers), e);
		}

		// 处理本次资源，加速GC
		userBodies = null;
		cachedPushArgs = null;
		failoverDeliversQueue = null;

		return new AsyncResult<Boolean>(false);
	}

	/**
	 * 针对上家回执数据已回但我方回执数据未入库情况需要 推送集合数据
	 * 
	 * @param failoverDeliversQueue
	 */
	private void sendToDeliverdFailoverQueue(List<SmsMtMessageDeliver> failoverDeliversQueue) {
		try {
			// 目前数据的超时时间按照 创建是时间超过5分钟则超时
			stringRedisTemplate.opsForList().rightPush(SmsRedisConstant.RED_QUEUE_SMS_DELIVER_FAILOVER,
					JSON.toJSONString(failoverDeliversQueue, new SimplePropertyPreFilter("msgId", "mobile",
							"statusCode", "deliverTime", "remarks", "status", "createTime")));
			// stringRedisTemplate.expire(SmsRedisConstant.RED_MESSAGE_DELIVED_WAIT_PUSH_LIST,
			// 5, TimeUnit.MINUTES);
		} catch (Exception e) {
			logger.error("针对上家回执数据已回但我方回执数据未入库情况需要 推送集合数据失败", e);
		}
	}

	/**
	 * 获取待处理的推送报告定义参数信息 eg. （SID, pushUrl, pushTimes ...）
	 * 
	 * @param msgId
	 * @param mobile
	 * @return
	 */

	public JSONObject getWaitPushBodyArgs(String msgId, String mobile) {
		// 首先在REDIS查询是否存在数据
		try {
			HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
			if (hashOperations.hasKey(getMtPushConfigKey(msgId), mobile)) {
				Object o = hashOperations.get(getMtPushConfigKey(msgId), mobile);
				if (o != null) {
					return JSON.parseObject(o.toString());
				}

			}
		} catch (Exception e) {
			logger.warn("回执完成逻辑中获取待推送设置数据REDIS异常，DB补偿, {}", e.getMessage());
		}

		// 如果REDIS没有或者REDIS 异常，需要查询DB是否有数据（REDIS过期后自动释放，顾要做兼容判断）
		return getUserPushConfigFromDatabase(msgId, mobile);
	}

	/**
	 * REDIS 查询不到反查数据库是否需要推送
	 * 
	 * @param msgId
	 * @param mobile
	 * @return
	 */
	private JSONObject getUserPushConfigFromDatabase(String msgId, String mobile) {
		if (StrUtil.isEmpty(msgId) || StrUtil.isEmpty(mobile)) {
			return null;
		}

		SmsMtMessagePush push = findByMobileAndMsgid(mobile, msgId);
		if (push != null) {
			throw new IllegalStateException("msgId:" + msgId + ", mobile:" + mobile + "推送记录已存在，忽略");
		}

		// 此处需要查询数据库是否需要有推送设置，无则不推送
		SmsMtMessageSubmit submit = smsMtSubmitService.findByMobileAndMsgid(mobile, msgId);
		if (submit == null) {
			logger.warn("msg_id : {}, mobile: {} 未找到短信相关提交数据", msgId, mobile);
			return null;
		}

		JSONObject pushSettings = new JSONObject();
		pushSettings.put("sid", submit.getSid());
		pushSettings.put("userId", submit.getUserId());
		pushSettings.put("msgId", msgId);
		pushSettings.put("attach", submit.getAttach());
		pushSettings.put("pushUrl", submit.getPushUrl());
		pushSettings.put("retryTimes", 3);

		return pushSettings;
	}

	/**
	 * TODO
	 * 
	 * @param msgId
	 * @return
	 */

	public String getMtPushConfigKey(String msgId) {
		return String.format("%s:%s", SmsRedisConstant.RED_READY_MT_PUSH_CONFIG, msgId);
	}

	/**
	 * 移除待推送信息配置信息
	 * 
	 * @param msgId
	 * @param mobile
	 */
	private void removeReadyMtPushConfig(String msgId, String mobile) {
		try {
			stringRedisTemplate.opsForHash().delete(getMtPushConfigKey(msgId), mobile);
		} catch (Exception e) {
			logger.error("移除待推送消息参数设置失败, msg_id : {}", msgId, e);
		}
	}

	@Async
	public void setMessageReadyPushConfigurations(List<SmsMtMessageSubmit> submits) {
		try {
			for (SmsMtMessageSubmit submit : submits) {
				stringRedisTemplate.opsForHash().put(getMtPushConfigKey(submit.getMsgId()), submit.getMobile(),
						JSON.toJSONString(submit, new SimplePropertyPreFilter("sid", "userId", "msgId", "attach",
								"pushUrl", "retryTimes")));
				stringRedisTemplate.expire(getMtPushConfigKey(submit.getMsgId()), 3, TimeUnit.HOURS);
			}

		} catch (Exception e) {
			logger.error("设置待推送配置消息: {} 失败", JSON.toJSONString(submits), e);
		}
	}

	/**
	 * 推送守候线程名称
	 * 
	 * @param userId   用户ID
	 * @param sequence 序列号
	 * @return
	 */
	private static String pushThreadName(String userId, Integer sequence) {
		return String.format("push-%s:%d", userId, sequence == null ? 1 : sequence++);
	}

	public boolean addUserMtPushListener(String userId) {
		final Lock lock = ADD_PUSH_THREAD_MONITOR;
		lock.lock();
		try {
			for (int i = 0; i < pushThreadPoolSize; i++) {
//				Thread thread = new Thread(
//						new MtReportPushToDeveloperWorker(applicationContext, getUserPushQueueName(userId)),
//						pushThreadName(userId, i));
//				thread.start();
				System.out.println(pushThreadName(userId, i));
			}

			logger.info("User[" + userId + "] add push thread[" + pushThreadPoolSize + "] successfully");
			return true;
		} catch (Exception e) {
			logger.info("User[" + userId + "] add push thread[" + pushThreadPoolSize + "] failed", e);
		} finally {
			lock.unlock();
		}
		return false;
	}

	public void pushMessageBodyToDeveloper(List<JSONObject> bodies) {
		// 资源URL对应的推送地址(资源地址对应的总量超过500应该分包发送)
		Map<String, List<JSONObject>> urlBodies = new HashMap<>();
		String urlKey = null;

		for (JSONObject body : bodies) {
			if (CollUtil.isEmpty(body)) {
				continue;
			}

			if (StrUtil.isEmpty(body.getString(PUSH_BODY_SUBPACKAGE_KEY))) {
				continue;
			}

			urlKey = body.getString(PUSH_BODY_SUBPACKAGE_KEY);

			// 根据用户的推送'URL'进行拆分组装状态报告
			try {
				if (CollUtil.isNotEmpty(urlBodies) && urlBodies.containsKey(urlKey)) {
					urlBodies.get(urlKey).add(body);
				} else {
					urlBodies.put(urlKey, new ArrayList<>(Arrays.asList(body)));
				}
			} catch (Exception e) {
				logger.error("解析推送数据报文异常:{}", body.toJSONString(), e);
			}
		}

		sendBody(urlBodies);

		urlKey = null;
		urlBodies = null;
	}

	/**
	 * 转义用户推送报告数据
	 * 
	 * @param bodies
	 * @return
	 */
	private String translateBodies(List<JSONObject> bodies) {
		if (CollectionUtils.isEmpty(bodies)) {
			logger.error("推送报告数据为空");
			return null;
		}

		return JSON.toJSONString(bodies, USER_PUSH_REPORT_FILTER, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty);
	}

	/**
	 * 发送短信状态报文至下家并完成异步持久化
	 * 
	 * @param urlBodies 多url对应不同的报文信息
	 * @return 推送结果
	 */
	private boolean sendBody(Map<String, List<JSONObject>> urlBodies) {
		try {
			for (Entry<String, List<JSONObject>> urlBody : urlBodies.entrySet()) {

				long startTime = System.currentTimeMillis();
				String pushContent = translateBodies(urlBody.getValue());

				// print result and cost time
				RetryResponse response = HttpClientUtil.postBody(urlBody.getKey(), pushContent, 1);
				logger.info("Url [" + urlBody.getKey() + "] push body [" + pushContent + "]'s result is "
						+ response.isSuccess() + ", it costs {} ms ", (System.currentTimeMillis() - startTime));

				doPushPersistence(urlBody.getValue(), response);
			}

			return true;
		} catch (Exception e) {
			logger.error("推送下家状态报文或持久化失败 ", e);
			return false;
		}
	}

	/**
	 * 推送报告持久化
	 * 
	 * @param bodies        推送报文
	 * @param retryResponse 推送处理结果
	 */
	private void doPushPersistence(List<JSONObject> bodies, RetryResponse retryResponse) {
		Set<String> waitPushMsgIdRedisKeys = new HashSet<>();
		List<SmsMtMessagePush> persistPushesList = new ArrayList<>();
		for (JSONObject body : bodies) {
			SmsMtMessagePush push = new SmsMtMessagePush();
			push.setMsgId(body.getString("msgId"));
			push.setMobile(body.getString("mobile"));
			if (retryResponse == null) {
				push.setStatus(PushStatus.FAILED.getValue() + "");
				push.setRetryTimes(0);
			} else {
				push.setStatus(retryResponse.isSuccess() ? PushStatus.SUCCESS.getValue() + ""
						: PushStatus.FAILED.getValue() + "");
				push.setRetryTimes(retryResponse.getAttemptTimes());
				push.setResponseMilliseconds(retryResponse.getTimeCost());
			}

			// 暂时先用作批量处理ID
			push.setResponseContent(System.nanoTime() + "");
			push.setContent(JSON.toJSONString(body, USER_PUSH_REPORT_FILTER, SerializerFeature.WriteMapNullValue,
					SerializerFeature.WriteNullStringAsEmpty));
			push.setCreateDate(new Date());

			waitPushMsgIdRedisKeys.add(getMtPushConfigKey(body.getString("msgId")));
			persistPushesList.add(push);
		}

		// 删除待推送消息信息
		stringRedisTemplate.delete(waitPushMsgIdRedisKeys);
		// 发送数据至带持久队列中
		super.saveBatch(persistPushesList);
	}

	public boolean startFailoverListener() {
		for (int i = 0; i < failoverThreadPoolSize; i++) {

			// 用户下行状态延迟推送（针对上家下行状态报告回复过快而短信提交记录未入库情况，后续延迟推送）
//			threadPoolTaskExecutor.execute(new MtReportFailoverPushWorker(applicationContext));
		}

		logger.info("Deliver failover threads[" + failoverThreadPoolSize + "] has joined");

		return true;
	}

	private String getRepushKey(Long serialNo) {
		return REPUSH_DELIVER_IDS_KEY + serialNo;
	}

	public SmsPushReport getPushReport(Map<String, Object> queryParams) {
		SmsPushReport report = new SmsPushReport();
		logger.info(JSON.toJSONString(queryParams));
        List<SmsMtMessageSubmit> list = smsMtSubmitService.findList(queryParams);
		if (CollectionUtils.isEmpty(list)) {
			return report;
		}

		report.setSubmitCount(list.size());

		boolean isIgnoredPushData = isIgnoredPushData(queryParams);

		try {

			List<String> deliverIds = new ArrayList<>();
			for (SmsMtMessageSubmit submit : list) {
				if (submit.getMessageDeliver() == null || submit.getMessageDeliver().getId() == null) {
					report.setUndeliverCount(report.getUndeliverCount() + 1);
					continue;
				}

				report.setDeliverCount(report.getDeliverCount() + 1);
				deliverIds.add(submit.getMessageDeliver().getId() + "");

				if (!submit.getNeedPush().booleanValue()) {
					report.setUnecessaryPushCount(report.getUnecessaryPushCount() + 1);
					continue;
				}

				if (isIgnoredPushData) {
					continue;
				}

				SmsMtMessagePush push = findByMobileAndMsgid(submit.getMobile(), submit.getMsgId());
				if (push == null) {
					report.setReadyPushCount(report.getReadyPushCount() + 1);
				} else if (PushStatus.SUCCESS.getValue() == Integer.parseInt(push.getStatus())) {
					report.setPushedSuccessCount(report.getPushedSuccessCount());
				} else if (PushStatus.FAILED.getValue() == Integer.parseInt(push.getStatus())) {
					report.setPushedFailedCount(report.getPushedFailedCount());
				}
			}

			report.setSerialNo(push2Redis(deliverIds));

		} catch (Exception e) {
			logger.error("Push report generate failed", e);
		}

		return report;
	}

	private Long push2Redis(List<String> deliverIds) {
		Long curentTime = System.currentTimeMillis();
		try {
			stringRedisTemplate.opsForList().leftPushAll(getRepushKey(curentTime), deliverIds);

			// ttl 5 minute, expired if operate data beyond 5 minutes
			stringRedisTemplate.expire(getRepushKey(curentTime), 5, TimeUnit.MINUTES);

		} catch (Exception e) {
			logger.error("Load repush data[" + deliverIds + "] to redis failed", e);
		}

		return curentTime;
	}

	private boolean isIgnoredPushData(Map<String, Object> queryParams) {
		return CollUtil.isNotEmpty(queryParams) && queryParams.containsKey("ignorePushData");
	}

	public boolean repush(Long serialNo) {
		if (serialNo == null || serialNo == 0L) {
			logger.error("Repush args[serialNo] is illegal");
			return false;
		}

		REPUSH_MONITOR.lock();
		try {

			List<String> deliverIds = stringRedisTemplate.opsForList().range(getRepushKey(serialNo), 0, -1);
			if (CollectionUtils.isEmpty(deliverIds)) {
				logger.error("No data in redis [" + getRepushKey(serialNo) + "] found");
				return false;
			}
			List<SmsMtMessageDeliver> delivers = smsMtMessageDeliverMapper.selectBatchIds(deliverIds);
			if (CollUtil.isEmpty(delivers)) {
				logger.error("Can't find any data in deliverIds [" + deliverIds + "]");
				return false;
			}

			compareAndPushBody(delivers);

			return true;

		} catch (Exception e) {
			logger.error("Repush data [" + serialNo + "] failed", e);
		} finally {
			REPUSH_MONITOR.unlock();
		}

		return false;
	}

	public SmsMtMessagePush findByMobileAndMsgid(String mobile, String msgId) {
		// TODO Auto-generated method stub
		QueryWrapper<SmsMtMessagePush> queryWrapper = new QueryWrapper<SmsMtMessagePush>();
		queryWrapper.eq("mobile", mobile);
		queryWrapper.eq("msg_id", msgId);
		return super.getOne(queryWrapper);
	}
}