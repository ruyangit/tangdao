package com.tangdao.exchanger.model.response;

import java.io.Serializable;

/**
 * 
 * <p>
 * TODO 调用厂商模板报备接口返回信息
 * </p>
 *
 * @author ruyang
 * @since 2021年3月3日
 */
public class ProviderModelResponse implements Serializable {

	private static final long serialVersionUID = -3483094301780957549L;

	/**
	 * 是否成功
	 */
	private boolean succeess;

	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 描述
	 */
	private String msg;

	/**
	 * 模板ID
	 */
	private String modelId;

	public boolean isSucceess() {
		return succeess;
	}

	public void setSucceess(boolean succeess) {
		this.succeess = succeess;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

}
