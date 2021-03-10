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
 * @since 2021年2月23日
 */
public class PassageContext {

	/**
	 * 异常通道ID
	 */
	public static final String EXCEPTION_PASSAGE_ID = "0";

	/**
	 * 异常（根据ID找不到通道等情况）显示的通道名称
	 */
	public static final String EXCEPTION_PASSAGE_NAME = "N/A";

	public enum RouteType {
		DEFAULT(0, "默认路由"), VALIDATE_CODE(1, "验证码路由"), QUIK_NOTIFICATION(2, "即时通知路由"), BATCH_NOTIFICATION(3, "批量通知路由"),
		HIGH_DANGER(4, "高风险投诉路由");

		private int value;

		private String name;

		RouteType(int value, String name) {
			this.name = name;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}

		public static RouteType parse(int value) {
			for (RouteType type : RouteType.values()) {
				if (type.getValue() == value) {
					return type;
				}
			}
			return RouteType.DEFAULT;
		}
	}

	public enum PassageStatus {
		ACTIVE(0, "有效"), HANGUP(1, "暂停使用"), SERVER_EXCEPTION(2, "服务器异常停用");

		private int value;
		private String title;

		PassageStatus(int value, String title) {
			this.title = title;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}

		public static RouteType getByValue(int value) {
			for (RouteType type : RouteType.values()) {
				if (type.getValue() == value) {
					return type;
				}
			}
			return RouteType.DEFAULT;
		}
	}

	public enum ResultCode {
		ZERO(0, "失败"), ONE(1, "成功"), TWO(2, "重复"), THREE(3, "记录重复并且状态为停用");

		private int value;

		private String title;

		ResultCode(int value, String title) {
			this.title = title;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}
	}

	public enum DeliverStatus {
		SUCCESS(0, "成功"), FAILED(1, "失败");

		private int value;
		private String title;

		DeliverStatus(int value, String title) {
			this.title = title;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}

	}

	public enum PushStatus {
		SUCCESS(0, "成功"), FAILED(1, "失败"),;

		private int value;
		private String title;

		PushStatus(int value, String title) {
			this.title = title;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}
	}

	public enum PassageSignMode {
		IGNORED(0, "不处理"), SIGNATURE_AUTO_PREPOSITION(1, "签名自动前置"), SIGNATURE_AUTO_POSTPOSITION(2, "签名自动后置"),
		REMOVE_SIGNATURE(3, "自动去掉签名");

		private int value;
		private String title;

		PassageSignMode(int value, String title) {
			this.title = title;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}
	}

	public enum PassageMessageTemplateStatus {
		AVAIABLE(0, "使用中"), DISABLED(1, "停用");

		private int value;
		private String title;

		PassageMessageTemplateStatus(int value, String title) {
			this.title = title;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}

	}

	public enum PassageSmsTemplateParam {
		NO(0, "不需要"), YES(1, "需要");

		private int value;
		private String title;

		PassageSmsTemplateParam(int value, String title) {
			this.title = title;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}

	}
	
	public enum PassageTemplateType {
        SMS(1, "短信模板"), FS(2, "流量模板"), VS(3, "语音模板"), MMS(4, "彩信模板");

        private int    value;

        private String name;

        private PassageTemplateType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum PassageTemplateDetailType {
        DETAIL_1(1, "发送模板"), DETAIL_2(2, "状态回执推送"), DETAIL_3(3, "状态回执自取"), DETAIL_4(4, "上行推送"), DETAIL_5(5, "上行自取"),
        DETAIL_6(6, "模板报备"), DETAIL_7(7, "模板报备结果推送"), DETAIL_8(8, "模板报备结果自取");

        private int    value;
        private String name;

        private PassageTemplateDetailType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static PassageTemplateDetailType getByValue(int value) {
            for (PassageTemplateDetailType t : PassageTemplateDetailType.values()) {
                if (t.getValue() == value) {
                    return t;
                }
            }
            return null;
        }

    }
}
