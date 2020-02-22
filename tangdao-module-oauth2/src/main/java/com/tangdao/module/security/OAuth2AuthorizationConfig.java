/**
 * 
 */
package com.tangdao.module.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;

import com.tangdao.module.security.token.CustomTokenEnhancer;

/**
 * <p>
 * TODO 资源授权服务
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
@Configuration
public class OAuth2AuthorizationConfig {

	@Configuration
	@EnableAuthorizationServer
	public static class OauthAuthorizationServerConfigurerAdapter extends AuthorizationServerConfigurerAdapter {
		
		@Autowired
		private AuthenticationManager authenticationManager;

		@Autowired
	    private UserDetailsService userDetailsService;

		/**
		 * 
		 * @return
		 */
		public TokenEnhancer tokenEnhacer() {
			return new CustomTokenEnhancer();
		}
		
//		/**
//		 * 
//		 * @return
//		 */
//		@Bean
//	    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//	        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//	        jwtAccessTokenConverter.setSigningKey("maracuja");
//	        return jwtAccessTokenConverter;
//	    }
//		
//		/**
//		 * 
//		 * @return
//		 */
//		@Bean
//		public TokenStore tokenStore(){
//			return new JwtTokenStore(jwtAccessTokenConverter());
//		}
		
		@Autowired(required = false)
		private TokenStore inMemoryTokenStore;
		
		/**
		 * 
		 */
		@Override
	    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			String defaultPwd = "123456";
	        // @formatter:off
	        clients.inMemory()
	                .withClient("browser")
	                .secret(defaultPwd)
	                .accessTokenValiditySeconds(43200) //
	                .refreshTokenValiditySeconds(2592000) //
	                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
	                .scopes("ui")
	                .and();
	        // @formatter:on
	    }
		
		/**
		 * 
		 */
		@Override
	    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
	    	TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//			tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhacer(), jwtAccessTokenConverter()));
			tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhacer()));
	    	
	    	endpoints
//	    			.tokenStore(tokenStore())
	    			.tokenStore(inMemoryTokenStore)
	    			.tokenEnhancer(tokenEnhancerChain)
	    			.reuseRefreshTokens(false)
	                .userDetailsService(userDetailsService)
	                .authenticationManager(authenticationManager);
	    }
		
		/**
		 * 
		 */
		@Override
	    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
	        oauthServer
	                .tokenKeyAccess("permitAll()")
	                .checkTokenAccess("isAuthenticated()")
	                .passwordEncoder(passwordEncoder())
	                ;
	    }
		
		/**
		 * 
		 * @return
		 */
		private PasswordEncoder passwordEncoder() {
			return new PasswordEncoder() {
				@Override
				public boolean matches(CharSequence rawPassword, String encodedPassword) {
					return StringUtils.hasText(encodedPassword) ? rawPassword.equals(encodedPassword): true;
				}
				@Override
				public String encode(CharSequence rawPassword) {
					return rawPassword.toString();
				}
			};
		};

	}
	
	@Configuration
	@EnableResourceServer
	public static class OauthResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {
		
		/**
		 * 
		 */
		@Override
	    public void configure(ResourceServerSecurityConfigurer resources) {
	        resources.resourceId("api");
	    }

	    @Override
	    public void configure(HttpSecurity http) throws Exception {
	        http
	        	.cors()
	        	.and()
	    		.csrf().disable()
	            .authorizeRequests()
	            	.antMatchers(WebSecurityConfig.DOCS_INFRA_API).permitAll()
	            	.antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll()
	            .anyRequest().authenticated()
	            .and()
	            .sessionManagement()
	            	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//	            .and()
//	            .exceptionHandling()
//	            	.authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(new CustomAccessDeniedHandler())
	            ;
	    }
	    
	}
	
}
