package com.tangdao.exchanger.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.tangdao.common.collect.ListUtils;
import org.tangdao.common.collect.MapUtils;
import org.tangdao.common.lang.StringUtils;
import org.tangdao.common.service.CrudService;
import org.tangdao.common.utils.PatternUtils;
import org.tangdao.modules.sms.constant.SmsRedisConstant;
import org.tangdao.modules.sms.mapper.SmsSignatureExtnoMapper;
import org.tangdao.modules.sms.model.domain.SmsSignatureExtno;
import org.tangdao.modules.sms.service.ISmsSignatureExtnoService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.model.domain.sms.SignatureExtno;
import com.tangdao.core.service.BaseService;

import cn.hutool.core.util.StrUtil;

/**
 * 签名扩展ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsSignatureExtnoService extends BaseService<SmsSignatureExtnoMapper, SignatureExtno> {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public boolean save(SignatureExtno signatureExtNo) {
		if (StrUtil.isEmpty(signatureExtNo.getSignature()) || StrUtil.isEmpty(signatureExtNo.getExtNumber())) {
			logger.error("签名或扩展号码为空");
			return false;
		}

		boolean result = super.save(signatureExtNo);
		if (!result) {
			return false;
		}

		pushToRedis(signatureExtNo);

		return true;
	}

	public boolean update(SignatureExtno signatureExtNo) {
		SignatureExtno td = get(signatureExtNo.getId());
		if (td == null) {
			logger.error("签名数据为空, id:{}", signatureExtNo.getId());
			return false;
		}

		boolean result = super.updateById(signatureExtNo);
		if (result) {
			pushToRedis(signatureExtNo);
		}

		return result;
	}

	@Override
	public boolean delete(String id) {
		SmsSignatureExtno signatureExtNo = get(id);
		if (signatureExtNo == null) {
			logger.error("用户签名数据为空，删除失败， ID：{}", id);
			return false;
		}

		try {
			removeRedis(signatureExtNo.getUserCode(), id);
		} catch (Exception e) {
			logger.error("移除REDIS用户签名失败， ID：{}", id, e);
		}

		return super.deleteById(id);
	}

	@Override
	public boolean reloadToRedis() {
		List<SmsSignatureExtno> list = super.select();
		if (ListUtils.isEmpty(list)) {
			logger.warn("用户签名数据为空");
			return false;
		}

		try {
			stringRedisTemplate.delete(stringRedisTemplate.keys(SmsRedisConstant.RED_USER_SIGNATURE_EXT_NO + "*"));
		} catch (Exception e) {
			logger.error("移除REDIS 签名扩展号码失败", e);
		}

		for (SmsSignatureExtno signatureExtNo : list) {
			pushToRedis(signatureExtNo);
		}

		return true;
	}

	private String getKey(String userCode) {
		return String.format("%s:%s", SmsRedisConstant.RED_USER_SIGNATURE_EXT_NO, userCode);
	}

	private void pushToRedis(SmsSignatureExtno signatureExtNo) {
		if (signatureExtNo == null) {
			return;
		}

		try {
			stringRedisTemplate.opsForHash().put(getKey(signatureExtNo.getUserCode()),
					signatureExtNo.getId().toString(), JSON.toJSONString(signatureExtNo,
							new SimplePropertyPreFilter("id", "userCode", "signature", "extNumber")));
		} catch (Exception e) {
			logger.error("签名扩展号加载到REDIS失败", e);
		}
	}

	/**
	 * 
	 * TODO 数据库查询用户签名扩展号码信息
	 * 
	 * @param userId
	 * @param content
	 * @return
	 */
	private String getFromDb(String userCode, String content) {
		List<SmsSignatureExtno> list = super.select(
				Wrappers.<SmsSignatureExtno>lambdaQuery().eq(SmsSignatureExtno::getUserCode, userCode));
		if (ListUtils.isEmpty(list)) {
			return null;
		}

		for (SmsSignatureExtno signatureExtNo : list) {
			// 根据内容匹配用户的扩展号码
			String extNumber = getExtNumber(signatureExtNo, content);
			if (extNumber == null) {
				continue;
			}

			return extNumber;
		}

		return null;
	}

	/**
	 * 
	 * TODO 查询REDIS
	 * 
	 * @param userId
	 */
	private String getFromRedis(String userCode, String content) {
		try {
			Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(getKey(userCode));
			if (MapUtils.isEmpty(map)) {
				return null;
			}

			SmsSignatureExtno signatureExtNo = null;
			for (Object key : map.keySet()) {
				Object obj = map.get(key);
				if (obj == null) {
					continue;
				}

				signatureExtNo = JSON.parseObject(obj.toString(), SmsSignatureExtno.class);

				// 根据内容匹配用户的扩展号码
				String extNumber = getExtNumber(signatureExtNo, content);
				if (extNumber == null) {
					continue;
				}

				return extNumber;
			}

			return null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * TODO 获取用户扩展号码
	 * 
	 * @param signatureExtNo
	 * @param content
	 * @return
	 */
	private static String getExtNumber(SmsSignatureExtno signatureExtNo, String content) {
		if (signatureExtNo == null) {
			return null;
		}

		String signature = buildSignaturePattern(signatureExtNo.getSignature());
		if (StringUtils.isEmpty(signature)) {
			return null;
		}

		if (content.startsWith(signature) || content.endsWith(signature)) {
			return signatureExtNo.getExtNumber();
		}

		return null;
	}

	/**
	 * 
	 * TODO 签名组装
	 * 
	 * @param signature
	 * @return
	 */
	private static String buildSignaturePattern(String signature) {
		if (StringUtils.isEmpty(signature)) {
			return null;
		}

		return String.format("【%s】", signature);
	}

	private void removeRedis(String userCode, String id) {
		try {
			stringRedisTemplate.opsForHash().delete(getKey(userCode), id);
		} catch (Exception e) {
			logger.warn("REDIS 用户签名移除失败", e);
		}
	}

	@Override
	public String getExtNumber(String userCode, String content) {
		if (StringUtils.isEmpty(content) || (!PatternUtils.isContainsSignature(content))
				|| StringUtils.isEmpty(userCode)) {
			return null;
		}

		try {
			return getFromRedis(userCode, content);
		} catch (Exception e) {
			// 如果出错则由数据库补偿
			return getFromDb(userCode, content);
		}
	}
}