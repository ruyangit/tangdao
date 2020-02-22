/**
 * 
 */
package com.tangdao.module.security.token;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.tangdao.module.security.service.UserPrincipal;

/**
 * <p>
 * TODO 扩展 TokenEnhancer
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		// TODO Auto-generated method stub
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		
		Map<String, Object> addInfo = new LinkedHashMap<String, Object>();
		addInfo.put("login_name", principal.getUsername());
		addInfo.put("tenant_id", principal.getTenantId());
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
		return accessToken;
	}

}
