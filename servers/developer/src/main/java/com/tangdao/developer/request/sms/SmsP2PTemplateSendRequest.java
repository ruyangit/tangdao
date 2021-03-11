package com.tangdao.developer.request.sms;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.tangdao.developer.annotation.ValidateField;
import com.tangdao.developer.request.AuthorizationRequest;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class SmsP2PTemplateSendRequest extends AuthorizationRequest {

	private static final long serialVersionUID = 1271682197751091183L;

	/**
	 * 模板内容信息，模板变量以 #code# 为例
	 */
	@ValidateField(value = "content", required = true, utf8 = true)
	private String content;

	/**
	 * 报文信息
	 */
	@ValidateField(value = "body", required = true, utf8 = true)
	private String body;

	/**
	 * 扩展码号
	 */
	@ValidateField(value = "extNumber", required = false, number = true)
	private String extNumber;

	/**
	 * 用于调用者自定义内容，主要为了实现调用侧的自行业务逻辑标识，如调用方自己的 会员标识，最终我方会原样返回
	 */
	@ValidateField(value = "attach", required = false)
	private String attach;

	/**
	 * 回调URL,用于调用方接口短信状态报告地址，目前只支持http地址
	 */
	@ValidateField(value = "callback", required = false)
	private String callback;

	private List<JSONObject> p2pBodies;

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getExtNumber() {
		return extNumber;
	}

	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<JSONObject> getP2pBodies() {
		return p2pBodies;
	}

	public void setP2pBodies(List<JSONObject> p2pBodies) {
		this.p2pBodies = p2pBodies;
	}

}
