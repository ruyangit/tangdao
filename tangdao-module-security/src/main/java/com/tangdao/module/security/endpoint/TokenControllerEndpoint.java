/**
 * 
 */
package com.tangdao.module.security.endpoint;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.framework.model.UserInfo;
import com.tangdao.module.security.AuthenticationService;
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
	private AuthenticationService authenticationService;
	
	@Autowired
	private TokenUtils tokenUtils;

	/**
	 * Todo Get token
	 * @param user
	 * @return
	 * @throws HttpRequestMethodNotSupportedException
	 */
	@RequestMapping(value = "/login/token", method=RequestMethod.POST)
	public Map<String, Object> postAccessToken(@RequestBody Map<String, Object> params) throws HttpRequestMethodNotSupportedException {
		//授权认证
		final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(params.get("loginName"), params.get("password"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = tokenUtils.createJWT(authentication, false);
        
        // Return the token
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("access_token", token);
        result.put("expires_in", TokenUtils.ACCESS_TOKEN_VALIDITY_SECONDS);
        result.put("token_type", TokenUtils.TOKEN_TYPE_BEARER);
        result.put("user", authenticationService.getUserInfo());
        return result;
	}
	
	/**
	 * Todo Get User
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/{env}/user", method=RequestMethod.GET)
	public UserInfo userPrincipal(Principal principal) {
		return authenticationService.getUserInfo();
	}
}
