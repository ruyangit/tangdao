package com.tangdao.developer.validator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.constant.PassportConstant;
import com.tangdao.core.context.UserContext.UserStatus;
import com.tangdao.core.model.domain.UserDeveloper;
import com.tangdao.core.service.HostWhitelistService;
import com.tangdao.core.service.UserDeveloperService;
import com.tangdao.developer.exception.ValidateException;
import com.tangdao.developer.request.AuthorizationRequest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
@Component
public class AuthorizationValidator extends Validator {

	@Autowired
	private UserDeveloperService userDeveloperService;

	@Autowired
	private HostWhitelistService hostWhiteListService;

	/**
	 * TODO 基础授权校验
	 * 
	 * @param paramMap
	 * @param ip
	 * @return
	 * @throws ValidateException
	 */
	public AuthorizationRequest validate(Map<String, String[]> paramMap, String ip) throws ValidateException {
		return validate(paramMap, ip, null);
	}

	/**
	 * TODO 用户参数完整性校验
	 * 
	 * @param paramMap
	 * @param ip
	 * @param mobile
	 * @return
	 * @throws ValidateException
	 */
	public AuthorizationRequest validate(Map<String, String[]> paramMap, String ip, String mobile)
			throws ValidateException {
		AuthorizationRequest passportModel = new AuthorizationRequest();
		validateAndParseFields(passportModel, paramMap);

		// 校验时间戳是否失效
		validateTimestampExpired(passportModel.getTimestamp());

		// 校验开发者身份的有效性
		checkIdentityValidity(passportModel, ip, mobile);

		return passportModel;
	}

	/**
	 * TODO 校验开发者身份的有效性（接口账号，签名，状态信息）
	 * 
	 * @param model
	 * @param ip
	 * @param mobile 手机号码信息（基础校验此值为空）
	 * 
	 * @throws ValidateException
	 */
	private void checkIdentityValidity(AuthorizationRequest model, String ip, String mobile) throws ValidateException {

		// 判断开发者是否存在
		UserDeveloper developer = userDeveloperService.getByAppkey(model.getAppkey());
		if (developer == null) {
			throw new ValidateException(CommonApiCode.COMMON_APPKEY_INVALID);
		}

		String signature = signature(developer.getAppSecret(), mobile, model.getTimestamp());
		// 判断用户签名信息是否正确
		if (StringUtils.isEmpty(signature) || !signature.equals(model.getAppsecret())) {
			throw new ValidateException(CommonApiCode.COMMON_AUTHENTICATION_FAILED);
		}

		// 账号冻结
		if (UserStatus.YES.getValue() != Integer.parseInt(developer.getStatus())) {
			throw new ValidateException(CommonApiCode.COMMON_APPKEY_NOT_AVAIABLE);
		}

		// a.服务器IP未报备
		if (!hostWhiteListService.ipAllowedPass(developer.getUserId(), ip)) {
			throw new ValidateException(CommonApiCode.COMMON_REQUEST_IP_INVALID);
		}

		model.setUserId(developer.getUserId());

		model.setIp(ip);
	}

	/**
	 * TODO 还原签名数据，包含传递的手机号码
	 * 
	 * @param appSecret
	 * @param mobile
	 * @param timestamp
	 * @return
	 */
	public String signature(String appSecret, String mobile, String timestamp) {
		if (StrUtil.isEmpty(mobile)) {
			return SecureUtil.md5(appSecret + timestamp);
		}
		return SecureUtil.md5(appSecret + mobile + timestamp);
	}


	/**
	 * TODO 判断用户时间戳是否过期
	 * 
	 * @param timestamp
	 * @return
	 */
	private void validateTimestampExpired(String timestamp) throws ValidateException {
		try {
			boolean isSuccess = System.currentTimeMillis()
					- Long.valueOf(timestamp) <= PassportConstant.DEFAULT_EXPIRE_TIMESTAMP_MILLISECOND;
			if (isSuccess) {
				return;
			}

			throw new ValidateException(CommonApiCode.COMMON_REQUEST_TIMESTAMPS_EXPIRED);
		} catch (Exception e) {
			logger.error("时间戳验证异常，{}", timestamp, e);
			throw new ValidateException(CommonApiCode.COMMON_REQUEST_TIMESTAMPS_EXPIRED);
		}
	}
}
