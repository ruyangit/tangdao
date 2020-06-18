/**
 *
 */
package com.tangdao.web.security;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.tangdao.web.config.TangdaoProperties;
import com.tangdao.web.security.user.SecurityUser;
import com.tangdao.web.security.user.SecurityUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Component
public class JwtTokenManager {

	@Autowired
	private TangdaoProperties properties;

	private static final String AUTHORITIES_KEY = "auth";

	private static final String SECRET_KEY = "2020";

	private static final Long SECRET_EXPIRATION = 18000L;

	/**
	 * Create token
	 *
	 * @param authentication auth info
	 * @return token
	 */
	public String createToken(Authentication authentication) {

		long now = (new Date()).getTime();

		Date validity;
		validity = new Date(now + SECRET_EXPIRATION * 1000L);

		Claims claims = Jwts.claims().setSubject(authentication.getName());

		claims.put("userId", ((SecurityUserDetails) authentication.getPrincipal()).getSecurityUser().getId());

		return Jwts.builder().setClaims(claims).setExpiration(validity).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	/**
	 * Get auth Info
	 *
	 * @param token token
	 * @return auth info
	 */
	public Authentication getAuthentication(String token) {
		/**
		 * parse the payload of token
		 */
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

		List<GrantedAuthority> authorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));

//		User principal = new User(claims.getSubject(), "", authorities);
		SecurityUser principal = new SecurityUser();
		principal.setUsername(claims.getSubject());
		principal.setId((String)claims.get("userId"));
		principal.setToken(token);
		principal.setIsa(properties.isa(claims.getSubject()));

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	/**
	 * validate token
	 *
	 * @param token token
	 * @return whether valid
	 */
	public void validateToken(String token) {
		Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
	}
}
