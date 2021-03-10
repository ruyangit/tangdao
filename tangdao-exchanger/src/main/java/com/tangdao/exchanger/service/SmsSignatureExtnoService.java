package com.tangdao.exchanger.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.dao.SmsSignatureExtnoMapper;
import com.tangdao.core.model.domain.SignatureExtno;
import com.tangdao.core.service.BaseService;
import com.tangdao.core.utils.PatternUtil;

import cn.hutool.core.collection.CollUtil;
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
		SignatureExtno td = super.getById(signatureExtNo.getId());
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

	public boolean delete(String id) {
		SignatureExtno signatureExtNo = super.getById(id);
		if (signatureExtNo == null) {
			logger.error("用户签名数据为空，删除失败， ID：{}", id);
			return false;
		}

		try {
			removeRedis(signatureExtNo.getUserId(), id);
		} catch (Exception e) {
			logger.error("移除REDIS用户签名失败， ID：{}", id, e);
		}

		return super.removeById(id);
	}

	public boolean reloadToRedis() {
		List<SignatureExtno> list = super.list();
		if (CollUtil.isEmpty(list)) {
			logger.warn("用户签名数据为空");
			return false;
		}

		try {
			stringRedisTemplate.delete(stringRedisTemplate.keys(SmsRedisConstant.RED_USER_SIGNATURE_EXT_NO + "*"));
		} catch (Exception e) {
			logger.error("移除REDIS 签名扩展号码失败", e);
		}

		for (SignatureExtno signatureExtNo : list) {
			pushToRedis(signatureExtNo);
		}

		return true;
	}

	private String getKey(String userId) {
		return String.format("%s:%s", SmsRedisConstant.RED_USER_SIGNATURE_EXT_NO, userId);
	}

	private void pushToRedis(SignatureExtno signatureExtNo) {
		if (signatureExtNo == null) {
			return;
		}

		try {
			stringRedisTemplate.opsForHash().put(getKey(signatureExtNo.getUserId()), signatureExtNo.getId().toString(),
					JSON.toJSONString(signatureExtNo,
							new SimplePropertyPreFilter("id", "userId", "signature", "extNumber")));
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
	private String getFromDb(String userId, String content) {
		List<SignatureExtno> list = super.list(
				Wrappers.<SignatureExtno>lambdaQuery().eq(SignatureExtno::getUserId, userId));
		if (CollUtil.isEmpty(list)) {
			return null;
		}

		for (SignatureExtno signatureExtNo : list) {
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
	private String getFromRedis(String userId, String content) {
		try {
			Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(getKey(userId));
			if (CollUtil.isEmpty(map)) {
				return null;
			}

			SignatureExtno signatureExtNo = null;
			for (Object key : map.keySet()) {
				Object obj = map.get(key);
				if (obj == null) {
					continue;
				}

				signatureExtNo = JSON.parseObject(obj.toString(), SignatureExtno.class);

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
	private static String getExtNumber(SignatureExtno signatureExtNo, String content) {
		if (signatureExtNo == null) {
			return null;
		}

		String signature = buildSignaturePattern(signatureExtNo.getSignature());
		if (StrUtil.isEmpty(signature)) {
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
		if (StrUtil.isEmpty(signature)) {
			return null;
		}

		return String.format("【%s】", signature);
	}

	private void removeRedis(String userId, String id) {
		try {
			stringRedisTemplate.opsForHash().delete(getKey(userId), id);
		} catch (Exception e) {
			logger.warn("REDIS 用户签名移除失败", e);
		}
	}

	public String getExtNumber(String userId, String content) {
		if (StrUtil.isEmpty(content) || (!PatternUtil.isContainsSignature(content)) || StrUtil.isEmpty(userId)) {
			return null;
		}

		try {
			return getFromRedis(userId, content);
		} catch (Exception e) {
			// 如果出错则由数据库补偿
			return getFromDb(userId, content);
		}
	}
}