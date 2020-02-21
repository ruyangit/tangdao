/**
 * 
 */
package com.tangdao.framework.protocol;

import java.io.Serializable;

import com.tangdao.framework.constant.OpenApiCode;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月21日
 */

public class Result<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	编码
	 */
	private String code = OpenApiCode.SUCCESS;

	/**
	 * 	消息信息
	 */
	private String message;

	/**
	 *	 消息描述
	 */
	private String message_description;

	/**
	 *	 结果数据
	 */
	private T data;
	
	/**
	 *	是否成功
	 */
    private Boolean success = true;
    
	/**
	 * 
	 */
	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param message
	 * @param data
	 * @param success
	 */
	public Result(Boolean success, String message, T data) {
		super();
		this.message = message;
		this.data = data;
		this.success = success;
	}

	/**
	 * @param code
	 * @param message
	 * @param message_description
	 * @param data
	 * @param success
	 */
	public Result(String code, String message, String message_description, T data, Boolean success) {
		super();
		this.code = code;
		this.message = message;
		this.message_description = message_description;
		this.data = data;
		this.success = success;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message_description
	 */
	public String getMessage_description() {
		return message_description;
	}

	/**
	 * @param message_description the message_description to set
	 */
	public void setMessage_description(String message_description) {
		this.message_description = message_description;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return the success
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}
    
	/**
	 * 
	 * @param <T>
	 * @param success
	 * @param message
	 * @return
	 */
	public static <T> Result<T> render(Boolean success, String message){
		return new Result<T>(success, message, null);
	}
}
