/**
 * 
 */
package com.tangdao.module.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
@Service
public class AuthenticationService implements UserDetailsService {
	
//	private Logger log = LoggerFactory.getLogger(getClass());
	
//	@Autowired
//	private IUserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		final String tenantId = ServletUtils.getParameter("tenantId");
//		try {
//			UserVo userVo = userService.getUserVo(username, tenantId);
//			if(null == userVo) {
//				throw new UsernameNotFoundException("User with username " + username + " not founded");
//			}
//			return new UserPrincipal(userVo);
//		} catch (UsernameNotFoundException e) {
//			log.error("Error Username not found method loadUserByUsername in class AuthenticationService: ", e);
//		} catch (Exception e) {
//			log.error("Error method loadUserByUsername in class AuthenticationService: ", e);
//		}
		return null;
	}

}
