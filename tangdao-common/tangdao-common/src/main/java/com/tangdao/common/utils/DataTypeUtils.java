/**
 * 
 */
package com.tangdao.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tangdao.common.mapper.JsonMapper;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月28日
 */
public class DataTypeUtils {

	/**
     * 判断是否是数字类型的
     * @param cs CharSequence
     * @return boolean
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (StringUtils.isBlank(cs)) {
            return true;
        }
        Pattern pattern = Pattern.compile("[-+]?[0-9]+(\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(cs);
        return isNum.matches();
    }

    public static boolean isBoolean(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.equals("true") || str.equals("false");
    }

    /**
     * 判断字符串是否是指定的时间格式
     * @param str str
     * @param format 格式
     * @return boolean
     */
    public static boolean isDateTime(final String str, final String format) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (StringUtils.isBlank(format)) {
            throw new NullPointerException("format不能为空");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    public static boolean isArray(final String str) {
    	if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
        	JsonMapper.fromJson(str, List.class);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是Array<Number>
     * @param str str
     * @return boolean
     */
    public static boolean isArrayNumber(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
        	List<String> list = JsonMapper.fromJson(str, List.class);
        	return list.stream().anyMatch(s ->isNumeric(s));
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是Array<Boolean>
     * @param str str
     * @return boolean
     */
    public static boolean isArrayBoolean(final String str) {
    	if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
        	List<String> list = JsonMapper.fromJson(str, List.class);
        	return list.stream().anyMatch(s ->isBoolean(s));
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是Array<DateTime>
     * @param str str
     * @param format format
     * @return boolean
     */
    public static boolean isArrayDateTime(final String str, final String format) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (StringUtils.isBlank(format)) {
            throw new NullPointerException("format不能为空");
        }
        try {
        	List<String> list = JsonMapper.fromJson(str, List.class);
        	return list.stream().anyMatch(s ->isDateTime(s, format));
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是时间戳（秒）
     * @param str str
     * @return boolean
     */
    public static boolean isTimestampSec(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        // 秒 长度是十位
        if (StringUtils.isNumeric(str)) {
            return str.length() == 10;
        }
        return false;
    }

    /**
     * 判断字符串是否是时间戳（毫秒）
     * @param str str
     * @return boolean
     */
    public static boolean isTimestampMill(final String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        // 毫秒 长度是13位
        if (StringUtils.isNumeric(str)) {
            return str.length() == 13;
        }
        return false;
    }
}
