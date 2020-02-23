/**
 * 
 */
package com.tangdao.module.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tangdao.framework.exception.ServiceException;
import com.tangdao.framework.protocol.IEnum;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class SecurityException extends ServiceException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param status
	 */
	public SecurityException(IEnum<?, ?> status) {
		super(status.toString());
	}
}
