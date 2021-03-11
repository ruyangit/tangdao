package com.tangdao.developer.validator;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.constant.PassportConstant;
import com.tangdao.developer.annotation.ValidateField;
import com.tangdao.developer.exception.ValidateException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO 基础验证
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class Validator {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * TODO 验证参数信息并赋值给object
	 * 
	 * @param obj
	 * @param paramMap
	 * @throws ValidateException
	 */
	protected void validateAndParseFields(Object obj, Map<String, String[]> paramMap) throws ValidateException {
		if (CollUtil.isEmpty(paramMap)) {
			logger.error("对接数据为空");
			throw new ValidateException(CommonApiCode.COMMON_REQUEST_EXCEPTION);
		}
		// 解析注解信息
		Map<String, String> param = parseAnotation(obj.getClass(), paramMap);
		try {
			org.apache.commons.beanutils.BeanUtils.populate(obj, param);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("接收类型 : {}， 参数：{} 转换model异常", obj.getClass().getName(), JSON.toJSONString(paramMap), e);
		}
	}

	/**
	 * TODO 获取需要解析的直接列（包含父类）
	 * 
	 * @param clazz
	 * @return
	 */
	private Field[] getClassFiled(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();

		Field[] parentFields = clazz.getSuperclass().getDeclaredFields();

		Field[] finalFields = new Field[fields.length + parentFields.length];

		System.arraycopy(fields, 0, finalFields, 0, fields.length);
		System.arraycopy(parentFields, 0, finalFields, fields.length, parentFields.length);

		return finalFields;
	}

	/**
	 * TODO 解析需要验证的注解列信息
	 * 
	 * @param clazz
	 * @param paramMap
	 * @return
	 * @throws ValidateException
	 */
	private Map<String, String> parseAnotation(Class<?> clazz, Map<String, String[]> paramMap)
			throws ValidateException {
		Field[] fields = getClassFiled(clazz);
		if (fields == null || fields.length == 0) {
			return null;
		}

		Map<String, String> param = new HashMap<>();
		Set<String> keys = paramMap.keySet();
		for (Field field : fields) {
			ValidateField vf = field.getAnnotation(ValidateField.class);
			if (vf == null) {
				continue;
			}

			// 参数中不包含必要的参数，抛错误码
			if (vf.required() && (!keys.contains(vf.value()) || StrUtil.isEmpty(paramMap.get(vf.value())[0]))) {
				throw new ValidateException(CommonApiCode.COMMON_REQUEST_EXCEPTION);
			}

			// 如果参数要求是UT`F-8编码，需要验证
			if (vf.utf8() && !isUtf8(paramMap.get(vf.value())[0])) {
				throw new ValidateException(CommonApiCode.COMMON_REQUEST_ENCODING_ERROR);
			}

			// 数字暂不校验
			// if (vf.number())
			// continue;

			if (paramMap.get(vf.value()) == null) {
				continue;
			}

			param.put(vf.value(), paramMap.get(vf.value())[0]);
		}

		return param;
	}

	/**
	 * TODO 是否为UTF-8编码
	 * 
	 * @param value
	 * @return
	 */
	private boolean isUtf8(String value) {
		try {
			value.getBytes("utf-8");
			return true;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	protected void isBeyondMobileSize(int mobiles) throws ValidateException {
		if (mobiles > PassportConstant.MAX_REQUEST_MOBILE_SIZE) {
			throw new ValidateException(CommonApiCode.COMMON_BEYOND_MOBILE_THRESHOLD);
		}
	}

}
