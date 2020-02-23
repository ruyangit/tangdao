/**
 * 
 */
package com.tangdao.module.security.endpoint;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.framework.protocol.Result;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */

@RestController
@RequestMapping(value = "/api/{env}", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenEndpoint extends AbstractEndpoint{

	@RequestMapping(value = "/auth/token", method=RequestMethod.POST)
	public Result postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

		if (!(principal instanceof Authentication)) {
			throw new InsufficientAuthenticationException(
					"There is no client authentication. Try adding an appropriate authentication filter.");
		}
		
		
		return null;
	}
}
