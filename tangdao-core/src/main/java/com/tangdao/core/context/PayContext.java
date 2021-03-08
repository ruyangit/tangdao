/**
 *
 */
package com.tangdao.core.context;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
public class PayContext {

	// 支付宝成功码
	public static final String ALIPAY_SUCCESS_CODE = "success";
	public static final String ALIPAY_FAIL_CODE = "fail";

	public enum PaySource {
		TRADE_ORDER_PAY(1, "订单充值"), USER_ACCOUNT_EXCHANGE(2, "账户余额划拨"), BOSS_INPUT(3, "支撑系统录入"),
		CONSUMER_EXPENDITURE(4, "消费");

		private int value;
		private String title;

		private PaySource(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}
	}

	public enum PayType {
		SYSTEM(0, "系统返充"), HSUSER_EXCHANGE(1, "站内账户转账"), OFFLINE_TRANSFER(2, "线下转账"), ALIPAY(3, "支付宝"),
		WXPAY(4, "微信支付");

		private int value;
		private String title;

		private PayType(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}
	}

	/**
	 * 
	 * TODO 支付服务提供商
	 *
	 * @author zhengying
	 * @version V1.0.0
	 * @date 2016年9月17日 下午7:47:50
	 */
	public enum PayProvider {
		UNION_PAY(1, "银联支付"), ALIPAY(2, "支付宝"), WXPAY(3, "微信支付"),;

		private int value;
		private String title;

		private PayProvider(int value, String title) {
			this.value = value;
			this.title = title;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}

		public static boolean isAlipay(String code) {
			if (StrUtil.isEmpty(code)) {
				{
					return false;
				}
			}
			return PayProvider.ALIPAY.name().equalsIgnoreCase(code);
		}
	}
}
