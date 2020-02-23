/**
 * 
 */
package com.tangdao.module.security.endpoint;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */

@RestController
public class CheckTokenEndpoint extends AbstractEndpoint{

	@RequestMapping(value = "/auth/check_token")
	@ResponseBody
	public Map<String, ?> checkToken(@RequestParam("token") String value) {
		return null;
	}
}
