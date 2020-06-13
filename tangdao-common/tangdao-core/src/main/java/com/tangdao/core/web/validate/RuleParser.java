/**
 *
 */
package com.tangdao.core.web.validate;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月4日
 */
public interface RuleParser {
	
	/**
	 * 验证字段内容
	 * @param value 校验值
	 * @param rvs 对比值数组
	 * @return 
	 * 
	 */
	Boolean validate(Object value, String... rvs);
}
