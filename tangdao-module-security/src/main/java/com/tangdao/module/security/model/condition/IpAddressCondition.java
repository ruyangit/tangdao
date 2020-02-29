/**
 * 
 */
package com.tangdao.module.security.model.condition;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Boolean fullfills(String conditionKey, Request request) {
		// TODO Auto-generated method stub
		return this.entrySet().stream().anyMatch(item ->v(item.getValue(), request.getContext().get(item.getKey())));
	}
	
	private boolean v(Object q, Object q2) {
		
//		CollectionUtil.contains(, q2);
		
		System.out.println(q);
		System.out.println(q2);
		return false;
	}

}
