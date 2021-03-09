package com.tangdao.core.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;
import com.tangdao.core.model.dto.ParseParamDto;
import com.tangdao.core.model.dto.RequestParamDto;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月3日
 */
@Getter
@Setter
@TableName("sms_passage_parameter")
public class PassageParameter extends DataEntity<PassageParameter> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String passageId; // 通道ID
	private String protocol; // 协议类型
	private int callType; // 1-发送 2-状态回执推送 3-状态回执自取 4-上行推送 5-上行自取 6-余额查询
	private String url; // url
	private String paramsDefinition; // 定义，直接取模板里的值
	private String params; // 具体的参数值，取模板中的key作为KEY，如{＂username＂:＂test＂, ＂password＂:＂123456＂}
	private String resultFormat; // result_format
	private String successCode; // success_code
	private String position; // 返回值的具体位置，json存储

	public PassageParameter() {
		super();
	}

	// 通道代码（伪列）
	@TableField(exist = false)
	private String passageCode;
	// 限流速度
	@TableField(exist = false)
	private Integer packetsSize;
	// 第一条计费字数（针对一客一签有意义）
	@TableField(exist = false)
	private Integer feeByWords;
	// 通道方短信模板ID（提前报备）
	@TableField(exist = false)
	private String smsTemplateId;
	// 变量参数，专指用于类似点对点短信数组/或者JSON变量传递 add by zhengying 20170825
	@TableField(exist = false)
	private String[] variableParamNames;

	@TableField(exist = false)
	private String[] variableParamValues;

	// 最大连接数
	@TableField(exist = false)
	private Integer connectionSize;
	// 读取数据流超时时间（针对已经和目标服务器建立连接，对方处理时间过慢，相应超时时间）
	@TableField(exist = false)
	private Integer readTimeout;

	public void setProtocol(String protocol) {
		this.protocol = protocol == null ? null : protocol.trim();
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public void setParamsDefinition(String paramsDefinition) {
		this.paramsDefinition = paramsDefinition == null ? null : paramsDefinition.trim();
	}

	public void setParams(String params) {
		this.params = params == null ? null : params.trim();
	}

	public void setResultFormat(String resultFormat) {
		this.resultFormat = resultFormat == null ? null : resultFormat.trim();
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode == null ? null : successCode.trim();
	}

	public List<RequestParamDto> getRequestParams() {
		List<RequestParamDto> list = new ArrayList<RequestParamDto>();
		if (StrUtil.isNotBlank(paramsDefinition)) {
			list = JSON.parseArray(paramsDefinition, RequestParamDto.class);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ParseParamDto> getParseParams() {
		List<ParseParamDto> list = new ArrayList<ParseParamDto>();
		if (StrUtil.isNotBlank(position)) {
			Map<String, String> map = JSON.parseObject(position, Map.class);
			for (Map.Entry<String, String> m : map.entrySet()) {
				ParseParamDto dto = new ParseParamDto();
				dto.setPosition(m.getValue());
				dto.setParseName(m.getKey());
				dto.setShowName("");
			}
		}
		return list;
	}

	public String getShowResultFormat() {
		if (StrUtil.isNotBlank(resultFormat)) {
			resultFormat = resultFormat.replaceAll("\"", "&quot;");
			resultFormat = resultFormat.replaceAll("'", "&#39;");
			resultFormat = resultFormat.replaceAll("<", "&lt;");
			resultFormat = resultFormat.replaceAll(">", "&gt;");
			return resultFormat;
		}
		return null;
	}

	public String[] getVariableParamNames() {
		return variableParamNames;
	}

	public void setVariableParamNames(String[] variableParamNames) {
		this.variableParamNames = variableParamNames;
	}

	public String[] getVariableParamValues() {
		return variableParamValues;
	}

	public void setVariableParamValues(String[] variableParamValues) {
		this.variableParamValues = variableParamValues;
	}
}