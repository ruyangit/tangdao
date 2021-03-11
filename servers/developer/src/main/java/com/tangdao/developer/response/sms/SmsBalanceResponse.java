package com.tangdao.developer.response.sms;

/**
 * 
 * <p>
 * TODO 短信余额回执信息
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class SmsBalanceResponse {

	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 余额
	 */
	private String balance;

	/**
	 * 付费方式: @Enum BalancePayType
	 */
	private String type;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SmsBalanceResponse() {
		super();
	}

	public SmsBalanceResponse(String code) {
		this.type = "0";
		this.balance = "0";
		this.code = code;
	}

	public SmsBalanceResponse(String code, int balance, int type) {
		super();
		this.code = code;
		this.balance = balance + "";
		this.type = type + "";
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
