/**
 *
 */
package com.tangdao.developer.web.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangdao.core.constant.CommonApiCode;
import com.tangdao.developer.exception.ValidateException;
import com.tangdao.developer.model.constant.PassportConstant;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
public class AuthorizationValidate {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	 /**
     * TODO 判断用户时间戳是否过期
     * 
     * @param timestamp
     * @return
     */
    private void validateTimestampExpired(String timestamp) throws ValidateException {
        try {
            boolean isSuccess = System.currentTimeMillis() - Long.valueOf(timestamp) <= PassportConstant.DEFAULT_EXPIRE_TIMESTAMP_MILLISECOND;
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
