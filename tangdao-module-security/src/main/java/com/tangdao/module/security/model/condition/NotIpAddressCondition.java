/**
 * 
 */
package com.tangdao.module.security.model.condition;

import org.springframework.stereotype.Component;

import com.tangdao.module.security.key.Cidr;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月29日
 */
@Component
public class NotIpAddressCondition extends Condition {

	@Override
	public boolean evaluate(String key, String value) {
		// TODO Auto-generated method stub
		try {
			return key == null || !Cidr.valueOf(value).matchIp(key);
		} catch (RuntimeException e) {
			return false;
		}
	}

}
