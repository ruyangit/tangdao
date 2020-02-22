/**
 * 
 */
package com.tangdao.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>
 * TODO Resource not found exception
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends SystemException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
