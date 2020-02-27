/**
 * 
 */
package com.tangdao.module.security.evaluate;

import java.util.List;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月27日
 */
public interface Evaluate {

	Boolean evaluateNotPrincipal(List<String> expressions, String reference);
	
	Boolean evaluatePrincipal(List<String> expressions, String reference);
	
	Boolean evaluateAction(List<String> expressions, String reference);
	
	Boolean evaluateResource(List<String> expressions, String reference);
	
	Boolean evaluateCondition(List<String> expressions, String reference);
}
