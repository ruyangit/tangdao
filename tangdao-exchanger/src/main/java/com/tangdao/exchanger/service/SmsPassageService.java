package com.tangdao.exchanger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.model.domain.sms.Passage;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.SmsPassageMapper;

/**
 * 通道管理ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPassageService extends BaseService<SmsPassageMapper, Passage>{

	/**
	 * 非中文表达式
	 */
	private static final String NOT_CHINESS_REGEX = "[0-9A-Za-z_.]*";

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SmsPassageAreaService smsPassageAreaService;

	@Autowired
	private SmsPassageParameterService smsPassageParameterService;

	@Autowired
	private SmsMtSubmitService smsMtSubmitService;

	@Autowired(required = false)
	private SmsProxyManager smsProxyManager;

	@Autowired
	private UserPassageService userPassageService;
//    @Autowired
//    private ISmsPassageGroupService   passageGroupService;
//    @Autowired
//    private ISmsMessageSendService       messageSendService;
	@Autowired
	private UserDeveloperService userDeveloperService;
	@Autowired
	private SmsPassageAccessService smsPassageAccessService;

	/**
	 * 是否是中文字符
	 * 
	 * @param code 编码
	 * @return 处理结果
	 */
	private static boolean isLetter(String code) {
		if (StringUtils.isEmpty(code)) {
			return false;
		}

		return code.matches(NOT_CHINESS_REGEX);
	}

	private void validate(SmsPassage passage) {
		if (StringUtils.isEmpty(passage.getCode())) {
			throw new IllegalArgumentException("通道代码为空，无法操作");
		}

		if (!isLetter(passage.getCode().trim())) {
			throw new IllegalArgumentException("通道代码不合法[字母|数字|下划线]");
		}

		if (passage.getId() != null) {
			return;
		}
		SmsPassage originPassage = this
				.getOne(Wrappers.<SmsPassage>lambdaQuery().eq(SmsPassage::getCode, passage.getCode()));
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
		if (StringUtils.isEmpty(areaCodes)) {
			return;
		}

		String[] codeArray = areaCodes.split(",");
		if (codeArray.length == 0) {
			return;
		}

		for (String code : codeArray) {
			passage.getAreaList().add(new SmsPassageArea(passage.getId(), code));
		}

		if (ListUtils.isNotEmpty(passage.getAreaList())) {
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
		if (ListUtils.isEmpty(passage.getParameterList())) {
			return false;
		}

		String passageSendProtocol = null;
		for (SmsPassageParameter parameter : passage.getParameterList()) {
			parameter.setPassageId(passage.getId());
			parameter.setCreateTime(new Date());

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

	@Override
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

			if (StringUtils.isEmpty(parameter.getProtocol())) {
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
		passage.setCreateTime(originPassage.getCreateTime());
//        passage.setModifyTime(new Date());

		// 更新通道信息
		return this.updateById(passage);
	}

	@Override
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
			smsPassageAccessService.updateByModifyPassage(passage.getId());

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

	@Override
	@Transactional
	public boolean deleteById(String id) {
		try {
			SmsPassage passage = this.get(id);
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

			boolean isOk = smsPassageAccessService.deletePassageAccess(id);
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

	@Override
	@Transactional
	public boolean disabledOrActive(String passageId, String status) {
		try {
			SmsPassage passage = new SmsPassage();
			passage.setId(passageId);
			passage.setStatus(status);
			boolean result = this.updateById(passage);
//            int result = smsPassageMapper.updateByPrimaryKeySelective(passage);
			if (!result) {
				throw new RuntimeException("更新通道状态失败");
			}

			// 更新REDIS资源数据
			reloadSmsPassageInRedis(passageId, status);

			// edit by 20180609 禁用/启用都需要重新筛查通道组相关信息（首选通道，备用通道需要及时切换回来）
			boolean isOk = smsPassageAccessService.updateByModifyPassage(passageId);
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

	@Override
	public List<SmsPassage> findAll() {
		try {
			Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(SmsRedisConstant.RED_SMS_PASSAGE);
			if (MapUtils.isNotEmpty(map)) {
				List<SmsPassage> passages = new ArrayList<>();

				map.forEach((k, v) -> {

//                    SmsPassage smsPassage = JSON.parseObject(v.toString(), SmsPassage.class);
					SmsPassage smsPassage = (SmsPassage) v;
					if (passages.contains(smsPassage)) {
						return;
					}

					passages.add(smsPassage);
				});
				return passages;
			}
		} catch (Exception e) {
			logger.warn("通道REDIS加载出错 {}", e.getMessage());
		}

		return this.select();
	}

	@Override
	public SmsPassage findById(String id) {
		SmsPassage smsPassage = null;
		try {
			Object obj = stringRedisTemplate.opsForHash().get(SmsRedisConstant.RED_SMS_PASSAGE, id);
			if (obj != null) {
//                smsPassage = JSON.parseObject(obj.toString(), SmsPassage.class);
				smsPassage = (SmsPassage) obj;
			}
		} catch (Exception e) {
			logger.warn("REDIS 加载失败，将于DB加载", e);
		}

		if (smsPassage == null) {
			smsPassage = this.get(id);
		}

		setPassageParamsIfEmpty(smsPassage);

		return smsPassage;
	}

	/**
	 * 设置通道参数集合信息
	 * 
	 * @param smsPassage 通道
	 */
	private void setPassageParamsIfEmpty(SmsPassage smsPassage) {
		if (smsPassage == null || smsPassage.getId() == null) {
			return;
		}

		if (ListUtils.isNotEmpty(smsPassage.getParameterList())) {
			return;
		}

		smsPassage.getParameterList().addAll(this.smsPassageParameterService.findByPassageId(smsPassage.getId()));

	}

	@Override
	public SmsPassage getBestAvaiable(String groupId) {
		List<SmsPassage> list = findByGroupId(groupId);

		// 此逻辑需要结合REDIS判断

		if (ListUtils.isEmpty(list)) {
			return null;
		}

		SmsPassage smsPassage = list.iterator().next();

		setPassageParamsIfEmpty(smsPassage);

		return ListUtils.isEmpty(list) ? null : smsPassage;
	}

	@Override
	public List<SmsPassage> getByCmcp(String cmcp) {
		return this.select(Wrappers.<SmsPassage>lambdaQuery().eq(SmsPassage::getStatus, SmsPassage.STATUS_NORMAL)
				.eq(SmsPassage::getCmcp, cmcp).or().eq(SmsPassage::getCmcp, "4"));
	}

	@Override
	public List<SmsPassage> findByCmcpOrAll(String cmcp) {
		return this.select(Wrappers.<SmsPassage>lambdaQuery().eq(SmsPassage::getStatus, SmsPassage.STATUS_NORMAL)
				.eq(SmsPassage::getType, "0").eq(SmsPassage::getCmcp, cmcp).or().eq(SmsPassage::getCmcp, "4"));
	}

	@Override
	public boolean reloadToRedis() {
		List<SmsPassage> list = this.select();
		if (ListUtils.isEmpty(list)) {
			logger.warn("短信通道数据为空");
			return false;
		}

		List<Object> con = stringRedisTemplate.execute((connection) -> {
			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();
			for (SmsPassage smsPassage : list) {

				byte[] mainKey = serializer.serialize(SmsRedisConstant.RED_SMS_PASSAGE);
				byte[] assistKey = serializer.serialize(smsPassage.getId().toString());

				connection.hSet(mainKey, assistKey, JSON.toJSONBytes(smsPassage));
			}

			return connection.closePipeline();
		}, false, true);

		return ListUtils.isNotEmpty(con);
	}

	@Override
	public List<SmsPassageArea> getPassageAreaById(String passageId) {
		return this.smsPassageAreaService.selectSmsPassageAreaByPassageId(passageId);
	}

	private boolean pushToRedis(SmsPassage smsPassage) {
		try {
			stringRedisTemplate.opsForHash().put(SmsRedisConstant.RED_SMS_PASSAGE, smsPassage.getId(),
					JSON.toJSONBytes(smsPassage));
			return true;
		} catch (Exception e) {
			logger.warn("REDIS 加载短信通道[" + JSON.toJSONBytes(smsPassage) + "]数据失败", e);
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
	private void reloadSmsPassageInRedis(String passageId, String status) {
		try {
			SmsPassage smsPassage = findById(passageId);
			if (smsPassage != null) {
				smsPassage.setStatus(status);
				stringRedisTemplate.opsForHash().put(SmsRedisConstant.RED_SMS_PASSAGE, passageId,
						JSON.toJSONBytes(smsPassage));
			}

		} catch (Exception e) {
			logger.warn("REDIS 加载短信通道数据失败", e);
		}
	}

	@Override
	@Transactional
	public boolean doMonitorSmsSend(String mobile, String content) {
		String userCode = DictUtils.getDictValue(SystemConfigType.SMS_ALARM_USER.name(),
				SettingsContext.USER_CODE_KEY_NAME, null);
		if (StringUtils.isEmpty(userCode)) {
			logger.error("告警用户数据为空，请配置");
			return false;
		}

		try {
			// 根据用户ID获取开发者相关信息
			UserDeveloper developer = userDeveloperService.getByUserCode(userCode);
			if (developer == null) {
				logger.error("用户：{}，开发者信息为空", userCode);
				return false;
			}

			// 如果用户无效则报错
			if (!UserStatus.YES.getValue().equals(developer.getStatus())) {
				logger.error("用户：{}，开发者信息状态[" + developer.getStatus() + "]无效", userCode);
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

	@Override
	@Transactional
	public boolean doTestPassage(String passageId, String mobile, String content) {
//        String  systemConfig = systemConfigService.findByTypeAndKey(SystemConfigType.PASSAGE_TEST_USER.name(),
//                                                                         SettingsContext.USER_ID_KEY_NAME);
		String userCode = DictUtils.getDictValue(SystemConfigType.PASSAGE_TEST_USER.name(),
				SettingsContext.USER_CODE_KEY_NAME, null);

		if (StringUtils.isEmpty(userCode)) {
			logger.error("通道测试用户数据为空，请配置");
			return false;
		}

		try {
			String passageGroupId = userPassageService.getByUserCodeAndType(userCode,
					PlatformType.SEND_MESSAGE_SERVICE.getCode());
			if (StringUtils.isEmpty(passageGroupId)) {
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
//            result = smsPassageAccessService.updateByModifyPassageGroup(passageGroupId);
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

	@Override
	public List<String> findPassageCodes() {
		return this.getBaseMapper().selectAvaiableCodes();
	}

	@Override
	public boolean isPassageBelongtoDirect(String protocol, String passageCode) {
		if (StringUtils.isNotEmpty(protocol)) {
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

	@Override
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

	@Override
	public List<SmsPassage> findByGroupId(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SmsPassage> findAccessPassages(String groupId, String cmcp, int routeType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SmsPassage> getByAreaAndCmcp(String areaCode, String cmcp) {
		// TODO Auto-generated method stub
		return null;
	}
}