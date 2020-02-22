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
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SystemException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }
}
