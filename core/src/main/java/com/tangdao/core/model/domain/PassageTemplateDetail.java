package com.tangdao.core.model.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;
import com.tangdao.core.model.vo.ParseParamVo;
import com.tangdao.core.model.vo.RequestParamVo;

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

	public List<RequestParamVo> getRequestParams() {
		List<RequestParamVo> list = new ArrayList<RequestParamVo>();
		if (StringUtils.isNotBlank(params)) {
			list = JSON.parseArray(params, RequestParamVo.class);
		}
		return list;
	}

	public List<ParseParamVo> getParseParams() {
		List<ParseParamVo> list = new ArrayList<ParseParamVo>();
		if (StringUtils.isNotBlank(position)) {
			list = JSON.parseArray(position, ParseParamVo.class);
		}
		return list;
	}

}