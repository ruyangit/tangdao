/**
 * 
 */
package com.tangdao.module.security.model.condition;

import java.util.Set;

import javax.annotation.Nullable;

/**
 * <p>
 * TODO 抽象条件
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月27日
 */
public abstract class Condition {

	public static final String BEAN_SUFFIX = "Condition";

	public static final String IF_EXISTS_SUFFIX = "IfExists";

	public static final String FOR_ALL_VALUES_PREFIX = "ForAllValues:";

	public static final String FOR_ANY_VALUE_PREFIX = "ForAnyValue:";

	/**
	 * 没获取到上下文的值，也返回 true
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean evaluateIfExist(@Nullable final String key, final String value) {
		return key == null || evaluate(key, value);
	}

	/**
	 * 匹配一个 返回 true
	 * 
	 * @param keys
	 * @param values
	 * @return
	 */
	public boolean evaluateForAnyValue(final Set<String> keys, final Set<String> values, final boolean isExists) {
		boolean success = false;
		for (final String key : keys) {
			for (final String value : values) {
				success = isExists ? evaluateIfExist(key, value) : evaluate(key, value);
				if (success)
					break;
			}
			if (success)
				break;
		}
		return success;
	}

	/**
	 * 匹配所有 返回 true
	 * 
	 * @param keys
	 * @param values
	 * @return
	 */
	public boolean evaluateForAllValue(final Set<String> keys, final Set<String> values, final boolean isExists) {
		boolean success = true;
		for (final String key : keys) {
			for (final String value : values) {
				success = isExists ? evaluateIfExist(key, value) : evaluate(key, value);
				if (success)
					break;
			}
			if (!success)
				break;
		}
		return success;
	}

	/**
	 * 条件计算
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	abstract public boolean evaluate(@Nullable final String key, final String value);

}
