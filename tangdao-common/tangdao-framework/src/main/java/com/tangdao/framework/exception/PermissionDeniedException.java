/**
 * 
 */
package com.tangdao.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>
 * TODO Permission denied exception
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class PermissionDeniedException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    protected PermissionDeniedException() {
        super();
    }
}
