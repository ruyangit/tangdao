/**
 *
 */
package com.tangdao.core.context;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月24日
 */
public class UserContext {

	public enum Source {
		WEB_REGISTER(1, "页面注册"), BOSS_INPUT(2, "后台录入"), PARTNER_SEND(3, "第三方平台发送");

		private Source(int value, String title) {
			this.value = value;
			this.title = title;
		}

		private int value;
		private String title;

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}

	}

	public enum UserStatus {
		YES(0, "有效"), NO(1, "无效");

		private int value;

		private String title;

		private UserStatus(int value, String title) {
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

	public enum BalancePayType {
		PREPAY(1, "预付"), POSTPAY(2, "后付");

		private int value;
		private String title;

		private BalancePayType(int value, String title) {
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

	public enum BalanceStatus {
		AVAIABLE(0, "可用"), WARNING(1, "告警中");

		private int value;
		private String title;

		private BalanceStatus(int value, String title) {
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
}
