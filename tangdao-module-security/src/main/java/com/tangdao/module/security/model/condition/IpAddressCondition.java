/**
 * 
 */
package com.tangdao.module.security.model.condition;

import org.springframework.stereotype.Component;

import com.tangdao.module.security.model.Request;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月27日
 */
@Component("IpAddress")
public class IpAddressCondition extends Condition {

	@Override
	public Boolean fullfills(String conditionKey, Request request) {
		// TODO Auto-generated method stub
		System.out.println("IpAddressCondition");
		System.out.println(this);
		System.out.println(request.getContext().get("req"));
		return false;
	}

}
