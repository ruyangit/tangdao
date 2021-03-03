package com.tangdao.exchanger.template.handler;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.exception.DataEmptyException;
import com.tangdao.core.exception.DataParseException;
import com.tangdao.exchanger.template.vo.TParameter;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * TODO 参数请求解析器
 * 
 * @author ruyang
 * @version V1.0
 * @date 2016年9月28日 下午2:56:23
 */
public class RequestTemplateHandler {

	public static TParameter parse(String parameter) {
		validate(parameter);
		try {
			return JSON.parseObject(parameter, TParameter.class);
		} catch (Exception e) {
			throw new DataParseException(e);
		}
	}

	private static void validate(String parameter) {
		if (StrUtil.isEmpty(parameter)) {
			throw new DataEmptyException("参数数据为空");
		}
	}

}
