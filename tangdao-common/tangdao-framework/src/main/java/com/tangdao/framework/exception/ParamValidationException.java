/**
 * 
 */
package com.tangdao.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParamValidationException extends SystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ParamValidationException(String message) {
        super(message);
    }

}
