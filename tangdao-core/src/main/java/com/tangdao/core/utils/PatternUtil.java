/**
 *
 */
package com.tangdao.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月23日
 */
public class PatternUtil {

	// 短信签名内容表达式，通过扫描方式检索
	private static final String MESSAGE_CONTENT_SIGNATURE_REG = "(【)(.*?)(】)";

	/**
	 * EMAIL 表达式
	 */
	private static final Pattern PATTERN_EMAIL = Pattern.compile("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?");

	/**
	 * 传真/固定电话 表达式
	 */
	private static final Pattern PATTERN_FIXED_PHONE = Pattern.compile("[0]{1}[0-9]{2,3}-[0-9]{7,8}");

	/**
	 * 护照表达式
	 */
	private static final Pattern PATTERN_PASSPORT = Pattern
			.compile("^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+$");

	/**
	 * url 表达式
	 */
	private static final Pattern PATTERN_URL = Pattern
			.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$");

	/**
	 * 验证字符串是否是email
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		Matcher matcher = PATTERN_EMAIL.matcher(str);
		return matcher.matches();
	}

	/**
	 * 判断是否为固定电话号码
	 *
	 * @param str 固定电话号码
	 * @return
	 */
	public static boolean isFixedPhone(String str) {
		Matcher matcher = PATTERN_FIXED_PHONE.matcher(str);
		return matcher.matches();
	}

	/**
	 * 验证护照
	 * 
	 * @param str 护照号
	 * @return
	 */
	public static boolean isPassport(String str) {
		Matcher matcher = PATTERN_PASSPORT.matcher(str);
		return matcher.matches();
	}

	/**
	 * 验证是否是URL
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isUrl(String str) {
		Matcher matcher = PATTERN_URL.matcher(str);
		return matcher.matches();
	}

	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}

	/**
	 * 判断字符串是否是整数
	 */
	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是浮点数
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains(".")) {
				return true;
			}
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 
	 * TODO 判断数据是否符合表达式规则
	 * 
	 * @param reg
	 * @param value
	 * @return
	 */
	public static boolean isRight(String reg, String value) {
		try {
			if (StrUtil.isEmpty(value) || StrUtil.isEmpty(reg)) {
				return false;
			}

			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(value);
			if (matcher.matches()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * TODO 短信内容是否包含签名
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isContainsSignature(String content) {
		Pattern pattern = Pattern.compile(MESSAGE_CONTENT_SIGNATURE_REG);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			if (StrUtil.isNotEmpty(matcher.group(2))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * TODO 短信签名是否包含多个签名
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isMultiSignatures(String content) {
		Pattern pattern = Pattern.compile(MESSAGE_CONTENT_SIGNATURE_REG);
		Matcher matcher = pattern.matcher(content);

		int count = 0;
		if (matcher.find()) {
			count++;
		}
		return count > 1;
	}

	/**
	 * 
	 * TODO 报备签名和本次签名内容是否符合
	 * 
	 * @param content
	 * @param signature
	 * @return
	 */
	private static boolean isSignatureMatched(String content, String signature) {
		Pattern pattern = Pattern.compile(MESSAGE_CONTENT_SIGNATURE_REG);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			if (StrUtil.isNotEmpty(matcher.group(2)) && matcher.group(2).equals(signature)) {
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 
	 * TODO 签名是否有效
	 * 
	 * @param content
	 * @param signature
	 * @return
	 */
	public static boolean isSignatureAvaiable(String content, String signature) {
		if (!isContainsSignature(content)) {
			throw new RuntimeException("短信内容不包含签名内容");
		}

		if (isMultiSignatures(content)) {
			throw new RuntimeException("短信内容包含多个签名内容");
		}

		if (!isSignatureMatched(content, signature)) {
			throw new RuntimeException("短信报备签名和本次签名内容不一致");
		}

		return true;
	}
}
