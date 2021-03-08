package com.tangdao.exchanger.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.tangdao.common.collect.ListUtils;
import org.tangdao.common.lang.StringUtils;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.constant.SmsRedisConstant;
import org.tangdao.modules.sms.mapper.SmsMobileWhitelistMapper;
import org.tangdao.modules.sms.model.domain.SmsMobileWhitelist;
import org.tangdao.modules.sms.service.ISmsMobileWhiteListService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

/**
 * 手机白名单信息表ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMobileWhitelistService extends CrudService<SmsMobileWhitelistMapper, SmsMobileWhitelist>
		implements ISmsMobileWhiteListService {
//	@Reference
//    private IUserService             userService;
	@Autowired
	private SmsMobileWhitelistMapper smsMobileWhitelistMapper;
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Map<String, Object> response(String code, String msg) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result_code", code);
		resultMap.put("result_msg", msg);
		return resultMap;
	}

	@Override
	public Map<String, Object> batchInsert(SmsMobileWhitelist white) {
		if (StringUtils.isEmpty(white.getMobile())) {
			return response("-2", "参数不能为空！");
		}

		List<SmsMobileWhitelist> list = new ArrayList<>();
		try {
			// 前台默认是多个手机号码换行添加
			String[] mobiles = white.getMobile().split("\n");
			SmsMobileWhitelist mwl;
			for (String mobile : mobiles) {
				if (StringUtils.isBlank(mobile)) {
					continue;
				}

				// 判断是否重复 重复则不保存
				int statCount = selectByUserCodeAndMobile(white.getUserCode(), mobile.trim());
				if (statCount > 0) {
					continue;
				}

				mwl = new SmsMobileWhitelist();
				mwl.setMobile(mobile.trim());
				mwl.setUserCode(white.getUserCode());

				list.add(mwl);
			}

			if (ListUtils.isNotEmpty(list)) {
				this.saveBatch(list);

				// 批量操作无误后添加至缓存REDIS
				for (SmsMobileWhitelist ml : list) {
					pushToRedis(ml);
				}
			}

			return response("success", "成功！");
		} catch (Exception e) {
			logger.info("添加白名单失败", e);
			return response("exption", "操作失败");
		}
	}

	@Override
	public List<SmsMobileWhitelist> selectByUserCode(String userCode) {
		return this.select(Wrappers.<SmsMobileWhitelist>lambdaQuery().eq(SmsMobileWhitelist::getUserCode, userCode));
	}

	/**
	 * 获取白名单手机号码KEY名称
	 *
	 * @param UserCode 用户ID
	 * @return key
	 */
	private String getKey(String UserCode) {
		return String.format("%s:%s", SmsRedisConstant.RED_MOBILE_WHITELIST, UserCode);
	}

	@Override
	public boolean reloadToRedis() {
		List<SmsMobileWhitelist> list = this.select();
		if (ListUtils.isEmpty(list)) {
			logger.info("数据库未检索到手机白名单，放弃填充REDIS");
			return true;
		}
		try {
			stringRedisTemplate.delete(stringRedisTemplate.keys(SmsRedisConstant.RED_MOBILE_WHITELIST + "*"));

			List<Object> con = stringRedisTemplate.execute((connection) -> {

				RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
				connection.openPipeline();
				for (SmsMobileWhitelist mwl : list) {
					byte[] key = serializer.serialize(getKey(mwl.getUserCode()));

					connection.sAdd(key, serializer.serialize(mwl.getMobile()));
				}

				return connection.closePipeline();

			}, false, true);

			return ListUtils.isNotEmpty(con);
		} catch (Exception e) {
			logger.warn("REDIS重载手机白名单数据失败", e);
			return false;
		}
	}

	/**
	 * 添加到REDIS 数据中
	 * 
	 * @param mwl 手机白名单数据
	 */
	private void pushToRedis(SmsMobileWhitelist mwl) {
		try {
			stringRedisTemplate.opsForSet().add(getKey(mwl.getUserCode()), mwl.getMobile());
		} catch (Exception e) {
			logger.error("REDIS加载手机白名单信息", e);
		}
	}

	@Override
	public boolean isMobileWhitelist(String userCode, String mobile) {
		if (StringUtils.isEmpty(userCode) || StringUtils.isEmpty(mobile)) {
			return false;
		}

		try {
			return stringRedisTemplate.opsForSet().isMember(getKey(userCode), mobile);
		} catch (Exception e) {
			logger.warn("redis 获取手机号码白名单失败，将从DB加载", e);
			return selectByUserCodeAndMobile(userCode, mobile) > 0;
		}
	}

	@Override
	public Set<String> getByUserCode(String userCode) {
		try {
			return stringRedisTemplate.opsForSet().members(getKey(userCode));
		} catch (Exception e) {
			logger.warn("redis 获取手机号码白名单集合失败，将从DB加载", e);
			List<String> list = smsMobileWhitelistMapper.selectDistinctMobilesByUserCode(userCode);
			if (ListUtils.isEmpty(list)) {
				return null;
			}

			return new HashSet<>(list);
		}
	}

	public int selectByUserCodeAndMobile(String userCode, String mobile) {
		QueryWrapper<SmsMobileWhitelist> queryWrapper = new QueryWrapper<SmsMobileWhitelist>();
		queryWrapper.eq("user_code", userCode);
		queryWrapper.eq("mobile", mobile);
		return count(queryWrapper);
	}

}