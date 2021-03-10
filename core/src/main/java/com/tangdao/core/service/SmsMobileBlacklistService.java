package com.tangdao.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.context.SettingsContext.MessageAction;
import com.tangdao.core.context.SmsSettingsContext.MobileBlacklistType;
import com.tangdao.core.dao.SmsMobileBlacklistMapper;
import com.tangdao.core.model.domain.SmsMobileBlacklist;

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
public class SmsMobileBlacklistService extends BaseService<SmsMobileBlacklistMapper, SmsMobileBlacklist> {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 全局手机号码（与REDIS 同步采用广播模式）
	 */
	public static volatile Map<String, Integer> GLOBAL_MOBILE_BLACKLIST = new ConcurrentHashMap<>();

	/**
	 * TODO 消息拼接返回
	 * 
	 * @param code
	 * @param msg
	 * @return
	 */
	private Map<String, Object> response(String code, String msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result_code", code);
		resultMap.put("result_msg", msg);
		return resultMap;
	}

	public boolean isMobileBelongtoBlacklist(String mobile) {
		if (StrUtil.isEmpty(mobile)) {
			return false;
		}
		try {
			return GLOBAL_MOBILE_BLACKLIST.keySet().contains(mobile);
		} catch (Exception e) {
			logger.warn("Redis 手机号黑名单查询失败", e);
		}
		return this.count(Wrappers.<SmsMobileBlacklist>lambdaQuery().eq(SmsMobileBlacklist::getMobile, mobile)) > 0;
	}

	/**
	 * TODO 根据黑名单类型判断是否属于忽略的黑名单（配合短信模板配置使用）
	 * 
	 * @param type
	 * @return
	 */
	private static boolean isBelongtoIgnored(Integer type) {
		return type == null || MobileBlacklistType.NORMAL.getCode() == type
				|| MobileBlacklistType.UNSUBSCRIBE.getCode() == type;
	}

	public List<String> filterBlacklistMobile(List<String> mobiles, boolean isIgnored) {
		try {
			List<String> blackList = new ArrayList<>(mobiles);

			// 与黑名单总集取交集
			blackList.retainAll(GLOBAL_MOBILE_BLACKLIST.keySet());

			// 需要排除忽略的黑名单类型号码 add by 2018-05-02
			if (isIgnored) {
				// 需要删除的集合信息
				List<String> needRemoveList = new ArrayList<>(blackList.size());
				for (String mobile : blackList) {
					if (isBelongtoIgnored(GLOBAL_MOBILE_BLACKLIST.get(mobile))) {
						needRemoveList.add(mobile);
					}
				}

				blackList.removeAll(needRemoveList);
			}

			mobiles.removeAll(blackList);
			return blackList;

		} catch (Exception e) {
			logger.error("黑名单解析失败", e);
			return mobiles;
		}
	}

	@Transactional
	public Map<String, Object> batchInsert(SmsMobileBlacklist black) {
		if (StrUtil.isEmpty(black.getMobile())) {
			return response("-2", "参数不能为空！");
		}

		List<SmsMobileBlacklist> list = new ArrayList<>();
		try {
			// 前台默认是多个手机号码换行添加
			String[] mobiles = black.getMobile().split("\n");
			SmsMobileBlacklist mbl;
			for (String mobile : mobiles) {
				if (StrUtil.isBlank(mobile)) {
					continue;
				}

				// 判断是否重复 重复则不保存
				if (isMobileBelongtoBlacklist(mobile.trim())) {
					continue;
				}

				mbl = new SmsMobileBlacklist();
				mbl.setMobile(mobile.trim());
				mbl.setType(black.getType());
				mbl.setRemarks(black.getRemarks());
				list.add(mbl);
			}

			// 批量添加黑名单
			if (CollUtil.isNotEmpty(list)) {
				super.saveBatch(list);
				// 批量操作无误后添加至缓存REDIS
				for (SmsMobileBlacklist ml : list) {
					publishToRedis(MessageAction.ADD, ml.getMobile(), ml.getType());
				}
			}

			return response("success", "成功！");
		} catch (Exception e) {
			logger.info("添加手机号码黑名单失败", e);
			return response("exption", "操作失败");
		}
	}

	public boolean deleteByPrimaryKey(String id) {
		try {
			SmsMobileBlacklist smsMobileBlackList = super.getById(id);
			publishToRedis(MessageAction.REMOVE, smsMobileBlackList.getMobile(), smsMobileBlackList.getType());

		} catch (Exception e) {
			logger.warn("Redis 删除黑名单数据信息失败, id : {}", id, e);
		}

		return super.removeById(id);
	}

	/**
	 * TODO REDIS队列数据操作(订阅发布模式)
	 *
	 * @param mobile
	 * @param action
	 * @return
	 */
	private void publishToRedis(MessageAction action, String mobile, Integer type) {
		try {

			stringRedisTemplate.convertAndSend(SmsRedisConstant.BROADCAST_MOBILE_BLACKLIST_TOPIC,
					String.format("%d:%s:%d", action.getCode(), mobile, type));
		} catch (Exception e) {
			logger.error("加入黑名单数据错误", e);
		}
	}

	private boolean pushToRedis(final List<SmsMobileBlacklist> list) {
		try {
			long size = stringRedisTemplate.opsForHash().size(SmsRedisConstant.RED_MOBILE_BLACKLIST);
			if (size == list.size()) {
				// 初始化JVM 全局数据
				for (SmsMobileBlacklist mbl : list) {
					GLOBAL_MOBILE_BLACKLIST.put(mbl.getMobile(), mbl.getType());
				}
				return true;
			}

			stringRedisTemplate.delete(SmsRedisConstant.RED_MOBILE_BLACKLIST);
			List<Object> result = stringRedisTemplate.execute((connection) -> {
				RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
				byte[] key = serializer.serialize(SmsRedisConstant.RED_MOBILE_BLACKLIST);
				connection.openPipeline();

				// Map<byte[], byte[]> map = new HashMap<>();
				for (SmsMobileBlacklist mbl : list) {
					// map.put(serializer.serialize(mbl.getMobile()),
					// serializer.serialize(mbl.getType()+ ""));
					GLOBAL_MOBILE_BLACKLIST.put(mbl.getMobile(), mbl.getType());
					connection.hSet(key, serializer.serialize(mbl.getMobile()),
							serializer.serialize(mbl.getType() + ""));
				}
				// connection.hMSet(key, map);
				return connection.closePipeline();
			}, false, true);
			return CollUtil.isNotEmpty(result);
		} catch (Exception e) {
			logger.error("REDIS数据LOAD手机号码黑名单失败", e);
			return false;
		}
	}

	public boolean reloadToRedis() {
		List<SmsMobileBlacklist> list = this.list();
		if (CollUtil.isEmpty(list)) {
			logger.warn("未找到手机号码黑名单数据");
			return false;
		}

		return pushToRedis(list);
	}

}