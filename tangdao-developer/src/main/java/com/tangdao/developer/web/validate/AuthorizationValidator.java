/**
 *
 */
package com.tangdao.developer.web.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tangdao.core.BaseModel;
import com.tangdao.core.constant.CommonApiCode;
import com.tangdao.developer.exception.ValidateException;
import com.tangdao.developer.model.constant.PassportConstant;
import com.tangdao.developer.model.domain.UserDeveloper;
import com.tangdao.developer.model.dto.AuthorizationDTO;
import com.tangdao.developer.service.IHostWhitelistService;
import com.tangdao.developer.service.IUserDeveloperService;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
@Component
public class AuthorizationValidator {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserDeveloperService userDeveloperService;

	@Autowired
	private IHostWhitelistService hostWhitelistService;

	/**
	 * TODO 校验开发者身份的有效性（接口账号，签名，状态信息）
	 * 
	 * @param baseDTO
	 * @param ip
	 * @param mobile 手机号码信息（基础校验此值为空）
	 * 
	 * @throws ValidateException
	 */
	public String checkIdentityValidity(AuthorizationDTO authorizationDTO, String ip, String mobile) throws ValidateException {
		// 判断开发者是否存在
		UserDeveloper developer = userDeveloperService.getByAppkey(authorizationDTO.getAppkey());
		if (developer == null) {
			throw new ValidateException(CommonApiCode.DEV7100106);
		}

		String signature = signature(developer.getAppSecret(), mobile, authorizationDTO.getTimestamp());
		// 判断用户签名信息是否正确
		if (StrUtil.isEmpty(signature) || !signature.equals(authorizationDTO.getAppsecret())) {
			throw new ValidateException(CommonApiCode.DEV7100108);
		}

		// 账号冻结
		if (!BaseModel.STATUS_NORMAL.equals(developer.getStatus())) {
			throw new ValidateException(CommonApiCode.DEV7100107);
		}

		// 服务器IP未报备
		if (!hostWhitelistService.ipAllowedPass(developer.getUserCode(), ip)) {
			throw new ValidateException(CommonApiCode.DEV7100105);
		}

		return developer.getUserCode();
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
	public void validateTimestampExpired(String timestamp) throws ValidateException {
		try {
			boolean isSuccess = System.currentTimeMillis()
					- Long.valueOf(timestamp) <= PassportConstant.DEFAULT_EXPIRE_TIMESTAMP_MILLISECOND;
			if (isSuccess) {
				return;
			}
			throw new ValidateException(CommonApiCode.DEV7100104);
		} catch (Exception e) {
			logger.error("时间戳验证异常，{}", timestamp, e);
			throw new ValidateException(CommonApiCode.DEV7100104);
		}
	}
}
