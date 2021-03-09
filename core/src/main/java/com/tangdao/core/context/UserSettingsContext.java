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
public class UserSettingsContext {

	public enum SmsReturnRule {
		NO(0, "不返回"), YES_WITH_AUTO(1, "失败自动返还"), YES_WITH_TIMEOUT(2, "超时未回执返还");

		private int value;
		private String title;

		private SmsReturnRule(int value, String title) {
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

	public enum MmsReturnRule {
		NO(0, "不返回"), YES_WITH_AUTO(1, "失败自动返还"), YES_WITH_TIMEOUT(2, "超时未回执返还");

		private int value;
		private String title;

		private MmsReturnRule(int value, String title) {
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

	public enum SmsMessagePass {
		NO(0, "不需要"), YES(1, "需要");

		private int value;
		private String title;

		private SmsMessagePass(int value, String title) {
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

	public enum SmsNeedTemplate {
		NO(0, "不需要"), YES(1, "需要");

		private int value;
		private String title;

		private SmsNeedTemplate(int value, String title) {
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

	public enum SmsSignatureSource {
		SELF_MANAGE(0, "自维护"), HSPAAS_AUTO_APPEND(1, "系统强制");

		private int value;
		private String title;

		private SmsSignatureSource(int value, String title) {
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

	public enum SmsPickupTemplate {
		NO(0, "不提取"), YES(1, "提取");

		private int value;
		private String title;

		private SmsPickupTemplate(int value, String title) {
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
