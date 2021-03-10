package com.tangdao.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.context.CommonContext.CMCP;
import com.tangdao.core.context.CommonContext.PassageCallType;
import com.tangdao.core.context.CommonContext.Status;
import com.tangdao.core.context.PassageContext.PassageStatus;
import com.tangdao.core.context.PassageContext.PassageTemplateType;
import com.tangdao.core.context.PassageContext.RouteType;
import com.tangdao.core.dao.SmsPassageAccessMapper;
import com.tangdao.core.model.domain.SmsPassage;
import com.tangdao.core.model.domain.SmsPassageAccess;
import com.tangdao.core.model.domain.SmsPassageGroupDetail;
import com.tangdao.core.model.domain.SmsPassageParameter;
import com.tangdao.core.model.domain.UserPassage;

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
public class SmsPassageAccessService extends BaseService<SmsPassageAccessMapper, SmsPassageAccess> {
	/**
	 * 全局可用通道缓存
	 */
	public static Map<String, SmsPassageAccess> GLOBAL_PASSAGE_ACCESS_CONTAINER = new ConcurrentHashMap<>();

	/**
	 * 通道代码+通道类型组装的共享本地数据
	 */
	private static Map<String, SmsPassageAccess> CODE_TYPE_ACCESS_CONTAINER = new ConcurrentHashMap<>();

	/**
	 * 内存键分隔符
	 */
	public static final String MAP_KEY_SEPERATOR = "-";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SmsPassageService passageService;
	
	@Autowired
	private UserPassageService userPassageService;
	
	@Autowired
	private SmsPassageGroupDetailService smsPassageGroupDetailService;
	
	@Autowired
	private SmsPassageParameterService smsPassageParameterService;
	
	@Autowired
	private SmsPassageMonitorService passageMonitorService;
	
	@Autowired
	private AreaLocalService areaLocalService;

	public List<SmsPassageAccess> findPassageAccess() {
		return this.list();
	}

	public boolean save(SmsPassageAccess access) {
		access.setCreateDate(new Date());
		try {
			loadToRedis(access);

			// 如果通道调用类型为 自取，需要同步到 监管中心
			monitorThreadNotice(access, PassageStatus.ACTIVE.getValue());

		} catch (Exception e) {
			logger.warn("Redis 用户可用通道信息保存失败", e);
		}
		return this.save(access);
	}

	public boolean reload() {
		// 加载所有待发送可用通道信息
		List<SmsPassageAccess> list = this.list(Wrappers.<SmsPassageAccess>lambdaQuery()
				.eq(SmsPassageAccess::getCallType, PassageCallType.DATA_SEND.getCode()));
		if (CollUtil.isEmpty(list)) {
			logger.warn("缓冲可用通道失败，通道可用数据为空，请排查");
			return false;
		}

		batchLoadToRedis(list);

		return true;
	}

	public boolean updateWhenPassageBroken(String passageId) {
		return false;
	}

	/**
	 * 获取REDIS通道组合KEY（用于REDIS用户通道组合使用）
	 *
	 * @param routeType 路由类型
	 * @param cmcp      运营商
	 * @return KEY
	 */
	public String getAssistKey(Integer routeType, Integer cmcp, String areaCode) {
		return String.format("%d:%d:%s", routeType, cmcp, areaCode);
	}

	/**
	 * 获取REDIS主KEY
	 *
	 * @param userId 用户ID
	 * @return KEY
	 */
	private String getFullKey(String userId, Integer callType, Integer routeType, Integer cmcp, String areaCode) {
		return getMainKey(userId, callType) + MAP_KEY_SEPERATOR + getAssistKey(routeType, cmcp, areaCode);
	}

	/**
	 * 获取REDIS主KEY
	 *
	 * @param userId 用户ID
	 * @param callType 调用类型
	 * @return KEY
	 */
	private static String getMainKey(String userId, Integer callType) {
		return String.format("%s:%s:%d", SmsRedisConstant.RED_USER_PASSAGE_ACCESS, userId, callType);
	}

	/**
	 * 获取用户模糊查询REDIS KEY
	 *
	 * @param userId 用户编号
	 * @return 模糊匹配KEY
	 */
	private static String getMainLikeKey(String userId) {
		return String.format("%s:%s*", SmsRedisConstant.RED_USER_PASSAGE_ACCESS, userId);
	}

	public boolean update(SmsPassageAccess access) {
		try {
			loadToRedis(access);

			// 如果通道调用类型为 自取，需要同步到 监管中心
			/*
			 * if (PassageCallType.MT_STATUS_RECEIPT_WITH_SELF_GET.getCode() ==
			 * access.getCallType() || PassageCallType.MO_REPORT_WITH_SELF_GET.getCode() ==
			 * access.getCallType()) { // 修改暂时不同步 监控中心，需要判断通道参数是否发生变动 }
			 */

			return this.updateById(access);

		} catch (Exception e) {
			logger.error("用户可用通道信息更新失败, 用户 : {}, cmcp : {}, routeType :{}", access.getUserId(),
					access.getCmcp(), access.getRouteType(), e);
			return false;
		}
	}

	@Transactional
	public boolean updateByModifyUser(String userId) {
		try {
			// 根据userId 获取用户短信通道组关系信息
			String passageGroupId = userPassageService.getByUserIdAndType(userId,
					PassageTemplateType.SMS.getValue());
			if (StrUtil.isEmpty(passageGroupId)) {
				logger.error("根据用户：{} 查不到相关短信通道对应关系", userId);
				return false;
			}

			rebandPassageAccessInGroup(userId, passageGroupId);

			return true;
		} catch (Exception e) {
			logger.error("用户: [" + userId + "] 更改可用通道失败", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

	}

	/**
	 * 根据通道组内的通道数据重新绑定可用通道信息
	 * 
	 * @param userId         用户ID
	 * @param passageGroupId 通道组ID
	 */
	private void rebandPassageAccessInGroup(String userId, String passageGroupId) {
		try {
			// 根据用户ID删除所有的可用通道信息
			deleteByUserId(userId);
			// 删除该用户的ACCESS Redis中信息
			stringRedisTemplate.delete(stringRedisTemplate.keys(getMainLikeKey(userId)));

			// 根据通道组ID查询通道组详细信息
			List<SmsPassageGroupDetail> detailList = smsPassageGroupDetailService.findPassageByGroupId(passageGroupId);
			if (CollUtil.isEmpty(detailList)) {
				logger.warn("通道组ID：{} 查不到相关短信通道集合数据", passageGroupId);
			}

			// 已经处理过的可用通道信息则不重复处理
			Set<String> distinctAccessKeys = new HashSet<>();
			for (SmsPassageGroupDetail detail : detailList) {

				// 如果通道状态为不可用，直接忽略
				if (detail == null || detail.getSmsPassage() == null || detail.getSmsPassage().getStatus() == null
						|| PassageStatus.ACTIVE.getValue() != Integer.valueOf(detail.getSmsPassage().getStatus())
								.intValue()) {
					continue;
				}

				if (ignoreIfHasDone(distinctAccessKeys, detail)) {
					continue;
				}

				replacePassageValue2Access(detail, userId, passageGroupId);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 去重KEY（方便数据判断，去掉重复操作）
	 *
	 * @param routeType    路由类型
	 * @param cmcp         运营商
	 * @param provinceCode 省份代码
	 * @return KEY
	 */
	private static String repeatConditionKey(Integer routeType, Integer cmcp, String areaCode) {
		return String.format("%d:%d:%d", routeType, cmcp, areaCode);
	}

	/**
	 * 判断是否已经处理完成 根据用户ID确定的情况下， 运营商：路由类型：省份代码 确定唯一性处理，如已处理，则本次忽略
	 *
	 * @param distinctAccessKeys 去重后的KEYS
	 * @param detail             通道组详细信息
	 * @return 处理结果
	 */
	private boolean ignoreIfHasDone(Set<String> distinctAccessKeys, SmsPassageGroupDetail detail) {
		String conditionKey = repeatConditionKey(detail.getRouteType(), detail.getCmcp(), detail.getAreaCode());
		if (CollUtil.isEmpty(distinctAccessKeys)) {
			distinctAccessKeys.add(conditionKey);
			return false;
		}

		if (!distinctAccessKeys.contains(conditionKey)) {
			distinctAccessKeys.add(conditionKey);
			return false;
		}

		return true;
	}

	/**
	 * 替换本次新的通道信息至可用通道中（access）
	 *
	 * @param detail         通道组详细信息
	 * @param userId         用户ID
	 * @param passageGroupId 通道组ID
	 */
	private void replacePassageValue2Access(SmsPassageGroupDetail detail, String userId, String passageGroupId) {
		SmsPassage smsPassage = detail.getSmsPassage();

		try {
			List<SmsPassageParameter> parameterList = smsPassageParameterService.findByPassageId(detail.getPassageId());
			SmsPassageAccess access;
			for (SmsPassageParameter parameter : parameterList) {
				access = new SmsPassageAccess();
				access.setGroupId(passageGroupId);
				access.setCmcp(detail.getCmcp());
				access.setUserId(userId);

				// 扩展号码需要根据用户系统扩展号码和参数传递扩展号码累加计算，总长度不超过扩展号码长度
				access.setExtNumber(smsPassage.getExtNumber());
				access.setAccessCode(smsPassage.getAccessCode());
				access.setMobileSize(smsPassage.getMobileSize());
				access.setPacketsSize(smsPassage.getPacketsSize());
				access.setPassageId(detail.getPassageId());
				access.setPassageCode(smsPassage.getCode());
				access.setStatus(smsPassage.getStatus());
				access.setAreaCode(detail.getAreaCode());
				access.setRouteType(detail.getRouteType());
				access.setSignMode(smsPassage.getSignMode());
				access.setCallType(parameter.getCallType());
				access.setParams(parameter.getParams());
				access.setParamsDefinition(parameter.getParamsDefinition());
				access.setResultFormat(parameter.getResultFormat());
				access.setProtocol(parameter.getProtocol());
				access.setSuccessCode(parameter.getSuccessCode());
				access.setPosition(parameter.getPosition());
				access.setUrl(parameter.getUrl());
				access.setCreateDate(new Date());

				// 最大连接数限制（如HTTP 最大20个， CMPP
				// 1个连接等）集群环境需要考虑
				access.setConnectionSize(smsPassage.getConnectionSize());

				// add by zhengying 2017-08-31 加入请求超时时间（毫秒）
				access.setReadTimeout(smsPassage.getReadTimeout());

				// add by zhengying 2017-09-18 加入网关提交参数中是否需要带入网关指定参数
				access.setSmsTemplateParam(smsPassage.getSmsTemplateParam());

				// 保存可用通道信息至REDIS 和DB
				this.save(access);
			}
		} catch (Exception e) {
			logger.error("替换可用通道新数据出错", e);
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public boolean updateByModifyPassageGroup(String passageGroupId) {
		try {
			List<UserPassage> userPassageList = userPassageService.getPassageGroupListByGroupId(passageGroupId);
			for (UserPassage userPassage : userPassageList) {
				this.updateByModifyUser(userPassage.getUserId());
			}
			return true;
		} catch (Exception e) {
			logger.error("更新通道组失败", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

	}

	@Transactional
	public boolean updateByModifyPassage(String passageId) {
		try {
			List<String> groupIdList = smsPassageGroupDetailService.findGroupIdByPassageId(passageId);
			for (String groupId : groupIdList) {
				this.updateByModifyPassageGroup(groupId);
			}

			return true;
		} catch (Exception e) {
			logger.error("更新通道失败", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
	}

	public SmsPassageAccess get(String id) {
		SmsPassageAccess access = super.getById(id);
		if (access != null) {
			// 根据省份代码查询省份名称
//            Province province = provinceService.get(access.getAreaCode());
//            
//            access.setProvinceName(province == null ? "未知" : province.getName());
			// 设置路由类型名称
			access.setRouteTypeText(RouteType.parse(access.getRouteType()).getName());

			if (access.getCmcp() == 0) {
				access.setCmcpName(CMCP.GLOBAL.getTitle());
			} else {
				access.setCmcpName(CMCP.getByCode(access.getCmcp()).getTitle());
			}
		}

		return access;
	}

	public List<SmsPassageAccess> findWaitPulling(PassageCallType callType) {
		QueryWrapper<SmsPassageAccess> queryWrapper = new QueryWrapper<SmsPassageAccess>();
		queryWrapper.eq("call_type", callType.getCode());
		queryWrapper.eq("status", Status.NORMAL);
		queryWrapper.groupBy("passage_id", "protocol", "call_type", "url", "params", "success_code");
		return super.list(queryWrapper);
	}

	public List<SmsPassageAccess> findPassageBalace() {
		QueryWrapper<SmsPassageAccess> queryWrapper = new QueryWrapper<SmsPassageAccess>();
		queryWrapper.eq("call_type", PassageCallType.PASSAGE_BALANCE_GET.getCode());
		queryWrapper.orderByAsc("user_id");
		return super.list(queryWrapper);
	}

	/**
	 * 配装可用通道KEY
	 * 
	 * @param callType    调用类型
	 * @param passageCode 通道代码
	 * @return key
	 */
	private static String passageAccessKey(PassageCallType callType, String passageCode) {
		return callType.getCode() + ":" + (StrUtil.isEmpty(passageCode) ? "" : passageCode);
	}

	public SmsPassageAccess getByType(PassageCallType callType, String passageCode) {
		String passageAccessKey = passageAccessKey(callType, passageCode);
		if (CODE_TYPE_ACCESS_CONTAINER.containsKey(passageAccessKey)) {
			return CODE_TYPE_ACCESS_CONTAINER.get(passageAccessKey);
		}

		QueryWrapper<SmsPassageAccess> queryWrapper = new QueryWrapper<SmsPassageAccess>();
		queryWrapper.eq("call_type", callType.getCode());
		queryWrapper.eq("url", passageCode);
		queryWrapper.orderByAsc("user_code").last(" limit 1");
		SmsPassageAccess access = this.getOne(queryWrapper);
		if (access != null) {
			CODE_TYPE_ACCESS_CONTAINER.put(passageAccessKey, access);
			return access;
		}

		// 获取通道参数信息
		SmsPassageParameter passageParameter = smsPassageParameterService.getByType(callType, passageCode);
		if (passageParameter == null) {
			return null;
		}

		access = copyProperties(passageParameter);
		CODE_TYPE_ACCESS_CONTAINER.put(passageAccessKey, access);

		logger.warn("通道简码： [" + passageCode + "] 调用类型 [" + callType.getName() + "] 可用通道数据为空，参数failover可用");

		return access;
	}

	/**
	 * 从通道参数拷贝属性值给可用通道
	 * 
	 * @param passageParameter 通道参数信息
	 * @return 可用通道信息
	 */
	private SmsPassageAccess copyProperties(SmsPassageParameter passageParameter) {
		SmsPassageAccess smsPassageAccess = new SmsPassageAccess();
		smsPassageAccess.setPassageId(passageParameter.getPassageId());
		smsPassageAccess.setProtocol(passageParameter.getProtocol());
		smsPassageAccess.setParams(passageParameter.getParams());
		smsPassageAccess.setSuccessCode(passageParameter.getSuccessCode());
		return smsPassageAccess;
	}

	public List<SmsPassageAccess> selectByPassageId(String passageId) {
		return super
				.list(Wrappers.<SmsPassageAccess>lambdaQuery().eq(SmsPassageAccess::getPassageId, passageId));
	}

	public boolean deletePassageAccess(String passageId) {
		try {
			List<SmsPassageAccess> list = selectByPassageId(passageId);
			if (CollUtil.isEmpty(list)) {
				return true;
			}
			boolean result = super.remove(Wrappers.<SmsPassageAccess>lambdaQuery().eq(SmsPassageAccess::getPassageId, passageId));
			if (!result) {
				throw new RuntimeException("删除可用通道持久化异常");
			}

			List<String> ids = new ArrayList<>(list.size());
			for (SmsPassageAccess access : list) {

				removeFromRedis(access);

				// 如果通道调用类型为 自取，需要同步到 监管中心（停止轮训扫描）
				monitorThreadNotice(access, PassageStatus.HANGUP.getValue());

				ids.add(access.getId().toString());
			}

			if (CollUtil.isEmpty(ids)) {
				throw new RuntimeException("查询可用通道Ids数据为空");
			}

			logger.info("删除可用通道: {} 成功", passageId);

			return true;
		} catch (Exception e) {
			logger.error("删除可用通道失败，通道ID: {} 失败信息", passageId, e);
			return false;
		}
	}

	public boolean updateAccessStatus(String passageId, String status) {
		if (passageId == null || status == null) {
			logger.error("数据异常，更改可用通道状态失败，passageId : {}, status : {}", passageId, status);
			return false;
		}

		try {
			List<SmsPassageAccess> list = selectByPassageId(passageId);
			if (CollUtil.isEmpty(list)) {
				return true;
			}
			
			boolean result = updateStatusByPassageId(passageId, status);
			if (!result) {
				throw new RuntimeException("更新可用通道持久化异常");
			}

			for (SmsPassageAccess access : list) {
				// 如果状态一致，则无需修改REDIS缓存数据
				if (access.getStatus() != null && access.getStatus().equals(status)) {
					continue;
				}

				access.setStatus(status);

				loadToRedis(access);

				monitorThreadNotice(access, Integer.valueOf(status));
			}

			logger.info("更新可用通道: {} 状态：{} 成功", passageId, status);

			return true;
		} catch (Exception e) {
			logger.error("修改可用通道状态失败，通道ID: {}， 状态：{} 失败信息", passageId, status, e);
			return false;
		}
	}

	/**
	 * 更新监控应用相关自定义线程信息
	 * 
	 * @param access 可用通道
	 * @param status 状态
	 */
	private void monitorThreadNotice(SmsPassageAccess access, int status) {
		try {
			// 如果通道调用类型为 自取，需要同步到 监管中心（停止轮训扫描）
			if (PassageCallType.MT_STATUS_RECEIPT_WITH_SELF_GET.getCode() == access.getCallType()
					|| PassageCallType.MO_REPORT_WITH_SELF_GET.getCode() == access.getCallType()) {
				if (PassageStatus.ACTIVE.getValue() == status) {
                    passageMonitorService.addPassagePull(access);
					System.out.println("添加监控:" + JSON.toJSONString(access));
				} else {
                    passageMonitorService.removePasagePull(access);
					System.out.println("移除监控:" + JSON.toJSONString(access));
				}
			}
		} catch (Exception e) {
			logger.warn("更新监听服务线程失败 [" + e.getMessage() + "]");
		}
	}

	public Map<String, SmsPassageAccess> getByUserId(String userId) {
		Map<String, SmsPassageAccess> passages = new HashMap<>();
		try {
			Map<Object, Object> entries = stringRedisTemplate.opsForHash()
					.entries(getMainKey(userId, PassageCallType.DATA_SEND.getCode()));
			if (CollUtil.isNotEmpty(entries)) {
				for (Object key : entries.keySet()) {
					SmsPassageAccess passage = (SmsPassageAccess) entries.get(key);
					passages.put(getAssistKey(passage.getRouteType(), passage.getCmcp(), passage.getAreaCode()),
							passage);
				}
				return passages;
			}

		} catch (Exception e) {
			logger.warn("Redis 用户可用通道信息查询失败, 用户 : {}, 将在DB获取", userId, e);
			List<SmsPassageAccess> list = super.list(
					Wrappers.<SmsPassageAccess>lambdaQuery().eq(SmsPassageAccess::getUserId, userId)
							.eq(SmsPassageAccess::getCallType, PassageCallType.DATA_SEND.getCode()));
			if (CollUtil.isEmpty(list)) {
				return null;
			}

			// edity by 20180418 修改之前的redis中值设置
			Map<String, Object> redisPassageValues = new HashMap<>(list.size());
			for (SmsPassageAccess passage : list) {
				String assistKey = getAssistKey(passage.getRouteType(), passage.getCmcp(), passage.getAreaCode());
				passages.put(assistKey, passage);
				redisPassageValues.put(assistKey, JSON.toJSONBytes(passage));
			}

			stringRedisTemplate.opsForHash().putAll(getMainKey(userId, PassageCallType.DATA_SEND.getCode()),
					redisPassageValues);

			return passages;
		}

		return null;
	}

	public SmsPassageAccess get(String userId, int routeType, int cmcp, String areaCode) {
		String dataKey = getFullKey(userId, PassageCallType.DATA_SEND.getCode(), routeType, cmcp, areaCode);
		if (GLOBAL_PASSAGE_ACCESS_CONTAINER.containsKey(dataKey)) {
			return GLOBAL_PASSAGE_ACCESS_CONTAINER.get(dataKey);
		}

		SmsPassageAccess passageAccess = getAccessFromRedis(userId, routeType, cmcp, areaCode);
		if (passageAccess == null) {
			passageAccess = getAccessFromDb(userId, routeType, cmcp, areaCode);
		}

		if (passageAccess == null) {
			return null;
		}

		// 加载可用数据至JVM内存中
		GLOBAL_PASSAGE_ACCESS_CONTAINER.put(dataKey, passageAccess);

		return passageAccess;
	}

	/**
	 * 在DB中查询可用通道数据
	 * 
	 * @param userId       用户ID
	 * @param routeType    路由类型
	 * @param cmcp         运营商
	 * @param provinceCode 省份编码
	 * @return 可用通道信息
	 */
	private SmsPassageAccess getAccessFromDb(String userId, int routeType, int cmcp, String areaCode) {
		try {

			SmsPassageAccess passageAccess = super.getOne(Wrappers.<SmsPassageAccess>lambdaQuery()
					.eq(SmsPassageAccess::getUserId, userId).eq(SmsPassageAccess::getRouteType, routeType)
					.eq(SmsPassageAccess::getCmcp, cmcp).eq(SmsPassageAccess::getAreaCode, areaCode));
			if (passageAccess == null) {
				return null;
			}

			// 加载到REDIS中
			stringRedisTemplate.opsForHash().put(getMainKey(userId, passageAccess.getCallType()),
					getAssistKey(routeType, cmcp, areaCode), JSON.toJSONString(passageAccess));

		} catch (Exception e) {
			logger.warn("Redis 用户可用通道信息添加失败, 用户 : {}, cmcp : {}", userId, cmcp, e);
		}
		return null;
	}

	/**
	 * 在redis中查询可用通道
	 * 
	 * @param userId       用户ID
	 * @param routeType    路由类型
	 * @param cmcp         运营商
	 * @param provinceCode 省份编码
	 * @return 可用通道信息
	 */
	private SmsPassageAccess getAccessFromRedis(String userId, int routeType, int cmcp, String areaCode) {
		String mainKey = getMainKey(userId, PassageCallType.DATA_SEND.getCode());
		String assistKey = getAssistKey(routeType, cmcp, areaCode);
		try {
			Object object = stringRedisTemplate.opsForHash().get(mainKey, assistKey);
			if (object != null) {
				return JSON.parseObject(object.toString(), SmsPassageAccess.class);
			}
		} catch (Exception e) {
			logger.warn("Redis 用户可用通道信息查询失败, 用户 : {}, cmcp : {}", userId, cmcp, e);
		}
		return null;
	}

	/**
	 * 加载到REDIS
	 * 
	 * @param access 可用通道信息
	 */
	private void loadToRedis(SmsPassageAccess access) {
		try {

			stringRedisTemplate.execute((connection) -> {
				RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
				byte[] mainKey = serializer.serialize(getMainKey(access.getUserId(), access.getCallType()));
				byte[] assistKey = serializer
						.serialize(getAssistKey(access.getRouteType(), access.getCmcp(), access.getAreaCode()));

				connection.openPipeline();

				connection.hSet(mainKey, assistKey, serializer.serialize(JSON.toJSONString(access)));

				// 采用订阅发布来通知 分布式节点清理各自的JVM 内存数据
				connection.publish(serializer.serialize(SmsRedisConstant.BROADCAST_PASSAGE_ACCESS_TOPIC),
						JSON.toJSONBytes(access));

				return connection.closePipeline();
			}, false, true);

		} catch (Exception e) {
			logger.warn("Redis 用户可用通道信息加载到REDIS失败, 用户 : {}, cmcp : {}", access.getUserId(), access.getCmcp(), e);
		}
	}

	/**
	 * 移除REDIS数据
	 * 
	 * @param access 可用通道信息
	 */
	private void removeFromRedis(SmsPassageAccess access) {
		try {
			stringRedisTemplate.execute((connection) -> {
				RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
				connection.openPipeline();
				byte[] mainKey = serializer.serialize(getMainKey(access.getUserId(), access.getCallType()));
				byte[] assistKey = serializer
						.serialize(getAssistKey(access.getRouteType(), access.getCmcp(), access.getAreaCode()));

				connection.hDel(mainKey, assistKey);

				// 采用订阅发布来通知 分布式节点清理各自的JVM 内存数据
				connection.publish(serializer.serialize(SmsRedisConstant.BROADCAST_PASSAGE_ACCESS_TOPIC),
						JSON.toJSONBytes(access));
				return connection.closePipeline();
			}, false, true);

		} catch (Exception e) {
			logger.warn("Redis 移除可用通道信息加载到REDIS失败, 用户 : {}, cmcp : {}", access.getAreaCode(), access.getCmcp(),
					e);
		}
	}

	/**
	 * 批量加载可用通道数据至redis
	 * 
	 * @param list 可用通道集合
	 */
	private void batchLoadToRedis(List<SmsPassageAccess> list) {
		try {
			stringRedisTemplate.execute((connection) -> {
				RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
				connection.openPipeline();
				for (SmsPassageAccess access : list) {
					byte[] mainKey = serializer.serialize(getMainKey(access.getUserId(), access.getCallType()));
					byte[] assistKey = serializer
							.serialize(getAssistKey(access.getRouteType(), access.getCmcp(), access.getAreaCode()));

					connection.hSet(mainKey, assistKey, JSON.toJSONBytes(access));
				}

				return connection.closePipeline();

			}, false, true);

		} catch (Exception e) {
			logger.warn("Redis 用户可用通道信息加载到REDIS失败", e);
		}
	}
	
	public boolean deleteByUserId(String userId) {
		return super.remove(Wrappers.<SmsPassageAccess>lambdaQuery().eq(SmsPassageAccess::getUserId, userId));
	}
	
	public boolean updateStatusByPassageId(String passageId, String status) {
		UpdateWrapper<SmsPassageAccess> updateWrapper = new UpdateWrapper<SmsPassageAccess>();
		updateWrapper.set("status", status);
		updateWrapper.eq("passage_id", passageId);
		return super.update(updateWrapper);
	}
}