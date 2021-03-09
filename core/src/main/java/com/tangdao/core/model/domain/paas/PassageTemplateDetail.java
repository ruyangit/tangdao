package com.tangdao.core.model.domain.paas;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;
import com.tangdao.core.model.dto.ParseParamDto;
import com.tangdao.core.model.dto.RequestParamDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("paas_passage_template_detail")
public class PassageTemplateDetail extends DataEntity<PassageTemplateDetail> {

	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;

	private String templateId; // template_id
	private String callType; // 1-发送 2-状态回执推送 3-状态回执自取 4-上行推送 5-上行自取
	private String url; // url
	private String params; // 参数
	private String position; // 具体值的位置，json存储
	private String resultFormat; // 结果格式
	private String successCode; // 成功码标记

	public PassageTemplateDetail() {
		super();
	}

	public String getShowResultFormat() {
		if (StringUtils.isNotBlank(resultFormat)) {
			resultFormat = resultFormat.replaceAll("\"", "&quot;");
			resultFormat = resultFormat.replaceAll("'", "&#39;");
			resultFormat = resultFormat.replaceAll("<", "&lt;");
			resultFormat = resultFormat.replaceAll(">", "&gt;");
			return resultFormat;
		}
		return null;
	}

	public List<RequestParamDto> getRequestParams() {
		List<RequestParamDto> list = new ArrayList<RequestParamDto>();
		if (StringUtils.isNotBlank(params)) {
			list = JSON.parseArray(params, RequestParamDto.class);
		}
		return list;
	}

	public List<ParseParamDto> getParseParams() {
		List<ParseParamDto> list = new ArrayList<ParseParamDto>();
		if (StringUtils.isNotBlank(position)) {
			list = JSON.parseArray(position, ParseParamDto.class);
		}
		return list;
	}

}