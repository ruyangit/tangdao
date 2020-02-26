/**
 * 
 */
package com.tangdao.module.security.endpoint;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.module.security.utils.TokenUtils;

/**
 * <p>
 * TODO token create
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenControllerEndpoint extends AbstractEndpoint{
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtils tokenUtils;

	@RequestMapping(value = "/auth/token", method=RequestMethod.POST)
	public Map<String, Object> postAccessToken(@RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

		final String loginName = parameters.get("loginName");
		final String password = parameters.get("password");
		
		final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginName, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = tokenUtils.createJWT(authentication, false);
        
        // Return the token
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("access_token", token);
        result.put("expires_in", TokenUtils.ACCESS_TOKEN_VALIDITY_SECONDS);
        result.put("token_type", TokenUtils.TOKEN_TYPE_BEARER);
        return result;
	}
}
