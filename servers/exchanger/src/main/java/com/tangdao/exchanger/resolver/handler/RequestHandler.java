package com.tangdao.exchanger.resolver.handler;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.exception.DataEmptyException;
import com.tangdao.core.exception.DataParseException;
import com.tangdao.exchanger.template.vo.TParameter;

/**
 * 
 * <p>
 * TODO 参数请求解析器
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class RequestHandler {

	public static TParameter parse(String parameter) {
		
		validate(parameter);
		
		try {
			return JSON.parseObject(parameter, TParameter.class);
		} catch (Exception e) {
			throw new DataParseException(e);
		}
	}

	private static void validate(String parameter) {
		if (StringUtils.isEmpty(parameter)) {
            throw new DataEmptyException("参数数据为空");
        }
	}
		
}
