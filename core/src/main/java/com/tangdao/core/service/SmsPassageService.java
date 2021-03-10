package com.tangdao.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.context.CommonContext.PassageCallType;
import com.tangdao.core.context.CommonContext.PlatformType;
import com.tangdao.core.context.CommonContext.ProtocolType;
import com.tangdao.core.context.CommonContext.Status;
import com.tangdao.core.context.UserContext.UserStatus;
import com.tangdao.core.dao.SmsPassageMapper;
import com.tangdao.core.model.domain.SmsPassage;
import com.tangdao.core.model.domain.SmsPassageArea;
import com.tangdao.core.model.domain.SmsPassageParameter;
import com.tangdao.core.model.domain.UserDeveloper;
import com.tangdao.core.service.proxy.ISmsProxyManager;

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
public class SmsPassageService extends BaseService<SmsPassageMapper, SmsPassage> {

	/**
	 * 非中文表达式
	 */
	private static final String NOT_CHINESS_REGEX = "[0-9A-Za-z_.]*";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SmsPassageAreaService smsPassageAreaService;

	@Autowired
	private SmsPassageParameterService smsPassageParameterService;

	@Autowired
	private SmsMtMessageSubmitService smsMtSubmitService;

	@Autowired(required = false)
	private ISmsProxyManager smsProxyManager;

	@Autowired
	private UserPassageService userPassageService;
//    @Autowired
//    private IPassageGroupService   passageGroupService;
//    @Autowired
//    private ISmsMessageSendService       messageSendService;
	@Autowired
	private UserDeveloperService userDeveloperService;

	@Autowired
	private SmsPassageAccessService PassageAccessService;

	/**
	 * 是否是中文字符
	 * 
	 * @param code 编码
	 * @return 处理结果
	 */
	private static boolean isLetter(String code) {
		if (StrUtil.isEmpty(code)) {
			return false;
		}

		return code.matches(NOT_CHINESS_REGEX);
	}

	private void validate(SmsPassage passage) {
		if (StrUtil.isEmpty(passage.getCode())) {
			throw new IllegalArgumentException("通道代码为空，无法操作");
		}

		if (!isLetter(passage.getCode().trim())) {
			throw new IllegalArgumentException("通道代码不合法[字母|数字|下划线]");
		}

		if (passage.getId() != null) {
			return;
		}
		SmsPassage originPassage = this.getOne(Wrappers.<SmsPassage>lambdaQuery().eq(SmsPassage::getCode, passage.getCode()));
		if (originPassage != null) {
			throw new IllegalArgumentException("通道编码 [" + passage.getCode().trim() + "] 已存在，无法添加");
		}
	}

	/**
	 * 绑定通道和省份关系记录
	 * 
	 * @param passage       通道
	 * @param provinceCodes 省份代码（半角分号分割）
	 */
	private void bindPassageProvince(SmsPassage passage, String areaCodes) {
		if (StrUtil.isEmpty(areaCodes)) {
			return;
		}

		String[] codeArray = areaCodes.split(",");
		if (codeArray.length == 0) {
			return;
		}

		for (String code : codeArray) {
			passage.getAreaList().add(new SmsPassageArea(passage.getId(), code));
		}

		if (CollUtil.isNotEmpty(passage.getAreaList())) {
			smsPassageAreaService.saveBatch(passage.getAreaList());
		}
	}

	/**
	 * 绑定通道参数信息
	 * 
	 * @param passage  通道
	 * @param isModify 是否为修改模式
	 */
	private boolean bindPassageParameters(SmsPassage passage, boolean isModify) {
		if (CollUtil.isEmpty(passage.getParameterList())) {
			return false;
		}

		String passageSendProtocol = null;
		for (SmsPassageParameter parameter : passage.getParameterList()) {
			parameter.setPassageId(passage.getId());
			parameter.setCreateDate(new Date());

			if (passageSendProtocol != null) {
				continue;
			}

			if (PassageCallType.DATA_SEND.getCode() == parameter.getCallType()) {
				passageSendProtocol = parameter.getProtocol();

				// add by zhengying 20170502 针对通道类型为CMPP等协议类型需要创建PROXY
				startProxyIfMatched(parameter, passage.getPacketsSize());
			}
		}

		if (isModify) {
			smsPassageParameterService.deleteByPassageId(passage.getId());
		}
		boolean result = smsPassageParameterService.saveBatch(passage.getParameterList());
		if (isModify && result) {
			return true;
		}

		if (result) {
			// add by zhengying 20170319 每个通道单独分开 提交队列
			return smsMtSubmitService.declareNewSubmitMessageQueue(passageSendProtocol, passage.getCode());
		}

		return false;
	}

	/**
	 * 释放已经产生的无用资源，如销毁队列和移除REDIS数据
	 * 
	 * @param passage               通道
	 * @param isQueueCreateFinished 队列是否创建完成
	 * @param isRedisPushFinished   是否发送至redis
	 */
	private void release(SmsPassage passage, boolean isQueueCreateFinished, boolean isRedisPushFinished) {
		if (isQueueCreateFinished) {
			smsMtSubmitService.removeSubmitMessageQueue(passage.getCode().trim());
		}

		if (isRedisPushFinished) {
			removeFromRedis(passage.getId());
		}
	}

	@Transactional
	public Map<String, Object> create(SmsPassage passage, String provinceCodes) {
		boolean isQueueCreateFinished = false;
		boolean isRedisPushFinished = false;
		try {
			validate(passage);
			boolean result = this.save(passage);
			if (!result) {
				return response(false, "数据操作异常");
			}

			isQueueCreateFinished = bindPassageParameters(passage, false);

			bindPassageProvince(passage, provinceCodes);

			isRedisPushFinished = pushToRedis(passage);

			return response(true, "添加成功");

		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				return response(false, e.getMessage());
			}

			release(passage, isQueueCreateFinished, isRedisPushFinished);

			logger.error("添加短信通道[{}], provinceCodes[{}] 失败", JSON.toJSONBytes(passage), provinceCodes, e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return response(false, "添加失败");
		}
	}

	/**
	 * 加载通道代理信息
	 * 
	 * @param parameter   通道参数
	 * @param packetsSize 手机号码拆包大小
	 */
	private void startProxyIfMatched(SmsPassageParameter parameter, Integer packetsSize) {
		try {
			if (parameter.getCallType() != PassageCallType.DATA_SEND.getCode()) {
				return;
			}

			if (StrUtil.isEmpty(parameter.getProtocol())) {
				return;
			}

			// 是否需要出发通道代理逻辑(目前主要针对CMPP,SGIP,SGMP等直连协议),通道未使用中不无加载,后续会延迟加载(发短信逻辑初始发现无代理会初始化相关代理)
			if (!smsProxyManager.isProxyAvaiable(parameter.getPassageId())) {
				return;
			}

			// 重新刷新代理信息
			parameter.setPacketsSize(packetsSize);
			smsProxyManager.startProxy(parameter);

		} catch (Exception e) {
			logger.error("加载通道代理失败", e);
		}
	}

	/**
	 * 更新通道信息
	 * 
	 * @param passage 通道
	 * @return 更新结果
	 */
	private boolean updatePassage(SmsPassage passage) {
		SmsPassage originPassage = findById(passage.getId());
		if (originPassage == null) {
			throw new IllegalArgumentException("通道数据不存在");
		}

		passage.setStatus(originPassage.getStatus());
		passage.setCreateDate(originPassage.getCreateDate());
		passage.setUpdateDate(new Date());

		// 更新通道信息
		return this.updateById(passage);
	}

	@Transactional
	public Map<String, Object> update(SmsPassage passage, String provinceCodes) {
		try {

			validate(passage);

			boolean isSuccess = updatePassage(passage);
			if (!isSuccess) {
				return response(false, "更新通道失败");
			}

			// 绑定通道参数信息
			bindPassageParameters(passage, true);

			// 绑定省份通道关系
			bindPassageProvince(passage, provinceCodes);

			// 更新可用通道信息
			PassageAccessService.updateByModifyPassage(passage.getId());

			pushToRedis(passage);

			return response(true, "修改成功");
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				return response(false, e.getMessage());
			}

			logger.error("修改短信通道[{}], provinceCodes[{}] 失败", JSON.toJSONBytes(passage), provinceCodes, e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return response(false, "修改失败");
		}
	}

	/**
	 * 拼接返回值
	 * 
	 * @param result 处理结果
	 * @param msg    消息
	 * @return 结果
	 */
	private Map<String, Object> response(boolean result, String msg) {
		Map<String, Object> report = new HashMap<>();
		report.put("result", result);
		report.put("message", msg);
		return report;
	}

	@Transactional
	public boolean deleteById(String id) {
		try {
			SmsPassage passage = this.findById(id);
			if (passage == null) {
				throw new RuntimeException("查询通道ID：" + id + "数据为空");
			}

			boolean result = this.deleteById(id);
			if (!result) {
				throw new RuntimeException("删除通道失败");
			}

			result = this.smsPassageParameterService.deleteByPassageId(id);
			if (!result) {
				throw new RuntimeException("删除通道参数失败");
			}

			result = smsPassageAreaService.deleteByPassageId(id);
			if (!result) {
				throw new RuntimeException("删除通道省份关系数据失败");
			}

			boolean isOk = PassageAccessService.deletePassageAccess(id);
			if (!isOk) {
				throw new RuntimeException("删除可用通道失败");
			}

			smsMtSubmitService.removeSubmitMessageQueue(passage.getCode());

			return true;
		} catch (Exception e) {
			logger.error("删除通道：{} 信息失败", id, e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return false;
	}

	@Transactional
	public boolean disabledOrActive(String passageId, String status) {
		try {
			SmsPassage passage = new SmsPassage();
			passage.setId(passageId);
			passage.setStatus(status);
			boolean result = this.updateById(passage);
//            int result = PassageMapper.updateByPrimaryKeySelective(passage);
			if (!result) {
				throw new RuntimeException("更新通道状态失败");
			}

			// 更新REDIS资源数据
			reloadPassageInRedis(passageId, status);

			// edit by 20180609 禁用/启用都需要重新筛查通道组相关信息（首选通道，备用通道需要及时切换回来）
			boolean isOk = PassageAccessService.updateByModifyPassage(passageId);
			if (!isOk) {
				throw new RuntimeException("更新可用通道状态失败");
			}

			logger.info("更新通道：{} 状态：{} 成功", passageId, status);
			return true;

		} catch (Exception e) {
			logger.error("通道: {} 状态修改失败：{} 失败", passageId, status, e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
	}

	public List<SmsPassage> findAll() {
		try {
			Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(SmsRedisConstant.RED_SMS_PASSAGE);
			if (CollUtil.isNotEmpty(map)) {
				List<SmsPassage> passages = new ArrayList<>();

				map.forEach((k, v) -> {

//                    Passage Passage = JSON.parseObject(v.toString(), Passage.class);
					SmsPassage Passage = (SmsPassage) v;
					if (passages.contains(Passage)) {
						return;
					}

					passages.add(Passage);
				});
				return passages;
			}
		} catch (Exception e) {
			logger.warn("通道REDIS加载出错 {}", e.getMessage());
		}

		return this.list();
	}

	public SmsPassage findById(String id) {
		SmsPassage Passage = null;
		try {
			Object obj = stringRedisTemplate.opsForHash().get(SmsRedisConstant.RED_SMS_PASSAGE, id);
			if (obj != null) {
				Passage = (SmsPassage) obj;
			}
		} catch (Exception e) {
			logger.warn("REDIS 加载失败，将于DB加载", e);
		}

		if (Passage == null) {
			Passage = this.getById(id);
		}

		setPassageParamsIfEmpty(Passage);

		return Passage;
	}

	/**
	 * 设置通道参数集合信息
	 * 
	 * @param Passage 通道
	 */
	private void setPassageParamsIfEmpty(SmsPassage Passage) {
		if (Passage == null || Passage.getId() == null) {
			return;
		}

		if (CollUtil.isNotEmpty(Passage.getParameterList())) {
			return;
		}

		Passage.getParameterList().addAll(this.smsPassageParameterService.findByPassageId(Passage.getId()));

	}

	public SmsPassage getBestAvaiable(String groupId) {
		List<SmsPassage> list = findByGroupId(groupId);

		// 此逻辑需要结合REDIS判断
		if (CollUtil.isEmpty(list)) {
			return null;
		}
		SmsPassage Passage = list.iterator().next();

		setPassageParamsIfEmpty(Passage);

		return CollUtil.isEmpty(list) ? null : Passage;
	}

	public List<SmsPassage> getByCmcp(String cmcp) {
		return this.list(Wrappers.<SmsPassage>lambdaQuery().eq(SmsPassage::getStatus, Status.NORMAL)
				.eq(SmsPassage::getCmcp, cmcp).or().eq(SmsPassage::getCmcp, "4"));
	}

	public List<SmsPassage> findByCmcpOrAll(String cmcp) {
		return this.list(Wrappers.<SmsPassage>lambdaQuery().eq(SmsPassage::getStatus, Status.NORMAL)
				.eq(SmsPassage::getType, "0").eq(SmsPassage::getCmcp, cmcp).or().eq(SmsPassage::getCmcp, "4"));
	}

	public boolean reloadToRedis() {
		List<SmsPassage> list = this.list();
		if (CollUtil.isEmpty(list)) {
			logger.warn("短信通道数据为空");
			return false;
		}

		List<Object> con = stringRedisTemplate.execute((connection) -> {
			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();
			for (SmsPassage Passage : list) {

				byte[] mainKey = serializer.serialize(SmsRedisConstant.RED_SMS_PASSAGE);
				byte[] assistKey = serializer.serialize(Passage.getId().toString());

				connection.hSet(mainKey, assistKey, JSON.toJSONBytes(Passage));
			}

			return connection.closePipeline();
		}, false, true);

		return CollUtil.isNotEmpty(con);
	}

	public List<SmsPassageArea> getPassageAreaById(String passageId) {
		return this.smsPassageAreaService.selectSmsPassageAreaByPassageId(passageId);
	}

	private boolean pushToRedis(SmsPassage Passage) {
		try {
			stringRedisTemplate.opsForHash().put(SmsRedisConstant.RED_SMS_PASSAGE, Passage.getId(),
					JSON.toJSONBytes(Passage));
			return true;
		} catch (Exception e) {
			logger.warn("REDIS 加载短信通道[" + JSON.toJSONBytes(Passage) + "]数据失败", e);
			return false;
		}
	}

	private void removeFromRedis(String passageId) {
		try {
			stringRedisTemplate.opsForHash().delete(SmsRedisConstant.RED_SMS_PASSAGE, passageId);
		} catch (Exception e) {
			logger.warn("REDIS 删除短信通道[" + passageId + "]数据失败", e);
		}
	}

	/**
	 * 更新REDIS 通道的状态信息
	 * 
	 * @param passageId 通道ID
	 * @param status    状态
	 */
	private void reloadPassageInRedis(String passageId, String status) {
		try {
			SmsPassage Passage = findById(passageId);
			if (Passage != null) {
				Passage.setStatus(status);
				stringRedisTemplate.opsForHash().put(SmsRedisConstant.RED_SMS_PASSAGE, passageId,
						JSON.toJSONBytes(Passage));
			}

		} catch (Exception e) {
			logger.warn("REDIS 加载短信通道数据失败", e);
		}
	}

	@Transactional
	public boolean doMonitorSmsSend(String userId, String mobile, String content) {
//		String userCode = DictUtils.getDictValue(SystemConfigType.SMS_ALARM_USER.name(),
//				SettingsContext.USER_CODE_KEY_NAME, null);
		if (StrUtil.isEmpty(userId)) {
			logger.error("告警用户数据为空，请配置");
			return false;
		}

		try {
			// 根据用户ID获取开发者相关信息
			UserDeveloper developer = userDeveloperService.getById(userId);
			if (developer == null) {
				logger.error("用户：{}，开发者信息为空", userId);
				return false;
			}

			// 如果用户无效则报错
			if (UserStatus.YES.getValue() != Integer.parseInt(developer.getStatus())) {
				logger.error("用户：{}，开发者信息状态[" + developer.getStatus() + "]无效", userId);
				return false;
			}

			// 调用发送短信接口
//            SmsResponse response = messageSendService.sendCustomMessage(developer.getAppKey(), developer.getAppSecret(), mobile, content);
//            return CommonApiCode.SUCCESS.equals(response.getCode());
			return false;
		} catch (Exception e) {
			logger.error("通道告警逻辑失败", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
	}

	@Transactional
	public boolean doTestPassage(String passageId, String userId, String mobile, String content) {
//        String  systemConfig = systemConfigService.findByTypeAndKey(SystemConfigType.PASSAGE_TEST_USER.name(),
//                                                                         SettingsContext.USER_ID_KEY_NAME);
//		String userCode = DictUtils.getDictValue(SystemConfigType.PASSAGE_TEST_USER.name(),
//				SettingsContext.USER_CODE_KEY_NAME, null);

		if (StrUtil.isEmpty(userId)) {
			logger.error("通道测试用户数据为空，请配置");
			return false;
		}

		try {
			String passageGroupId = userPassageService.getByUserIdAndType(userId,
					PlatformType.SEND_MESSAGE_SERVICE.getCode());
			if (StrUtil.isEmpty(passageGroupId)) {
				logger.error("通道测试用户未配置短信通道组信息");
				return false;
			}

//            boolean result = passageGroupService.doChangeGroupPassage(passageGroupId, passageId);
//            if (!result) {
//                logger.error("通道组ID：{}，切换通道ID：{} 失败", passageGroupId, passageId);
//                return false;
//            }
//
//            // 更新通道组下 的可用通道相关
//            result = PassageAccessService.updateByModifyPassageGroup(passageGroupId);
//            if (!result) {
//                logger.error("通道组ID：{}，切换可用通道失败", passageGroupId);
//                return false;
//            }
//
//            // 根据用户ID获取开发者相关信息
//            UserDeveloper developer = userDeveloperService.getByUserCode(userCode);
//            if (developer == null) {
//                logger.error("用户：{}，开发者信息为空", userCode);
//                return false;
//            }
//
//            // 调用发送短信接口
//            SmsResponse response = messageSendService.sendCustomMessage(developer.getAppKey(), developer.getAppSecret(),  mobile, content);
//            return CommonApiCode.SUCCESS.equals(response.getCode());
			return false;
		} catch (Exception e) {
			logger.error("通道测试逻辑失败", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
	}

	public List<String> findPassageCodes() {
		return this.getBaseMapper().selectAvaiableCodes();
	}

	public boolean isPassageBelongtoDirect(String protocol, String passageCode) {
		if (StrUtil.isNotEmpty(protocol)) {
			return ProtocolType.isBelongtoDirect(protocol);
		}

		SmsPassage passage = this.getBaseMapper().getPassageByCode(passageCode.trim());
		if (passage == null) {
			return false;
		}

		SmsPassageParameter parameter = smsPassageParameterService.selectSendProtocol(passage.getId());
		if (parameter == null) {
			return false;
		}

		return ProtocolType.isBelongtoDirect(parameter.getProtocol());
	}

	public boolean kill(String id) {
		try {
			// 是否需要出发通道代理逻辑(目前主要针对CMPP,SGIP,SGMP等直连协议)
			if (smsProxyManager.isProxyAvaiable(id)) {
				if (!smsProxyManager.stopProxy(id)) {
					logger.error("通道 [" + id + "] 断开连接失败");
					return false;
				}
			} else {
				logger.info("通道 [" + id + "] 连接不存在，忽略断开连接操作");
				return true;
			}

			logger.info("通道 [" + id + "] 断开连接成功");
			return true;

		} catch (Exception e) {
			logger.error("通道 [" + id + "] 断开连接操作失败", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
	}

	public List<SmsPassage> findByGroupId(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SmsPassage> findAccessPassages(String groupId, String cmcp, int routeType) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SmsPassage> getByAreaAndCmcp(String areaCode, String cmcp) {
		// TODO Auto-generated method stub
		return null;
	}
}