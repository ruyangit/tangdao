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
 * @author ruyangit@gmail.com
 * @since 2020年3月12日
 */
public class SmsSettingsContext {

	public enum ForbiddenWordsSwitch {

		OPEN("0", "开启"), CLOSE("1", "关闭");

		private String value;
		private String title;

		private ForbiddenWordsSwitch(String value, String title) {
			this.value = value;
			this.title = title;
		}

		public static ForbiddenWordsSwitch parse(String value) {
			if (StrUtil.isEmpty(value)) {
				{
					return null;
				}
			}

			for (ForbiddenWordsSwitch as : ForbiddenWordsSwitch.values()) {
				if (value.equals(as.getValue())) {
					{
						return as;
					}
				}
			}
			return null;
		}

		/**
		 * 
		 * TODO 判断敏感词开关是否打开（默认情况均为打开）
		 * 
		 * @param value
		 * @return
		 */
		public static boolean isOpen(String value) {
			if (StrUtil.isEmpty(value)) {
				{
					return true;
				}
			}

			try {
				ForbiddenWordsSwitch fws = parse(value);
				if (fws == null || ForbiddenWordsSwitch.OPEN == fws) {
					{
						return true;
					}
				}

				return false;
			} catch (Exception e) {
				// 数据转换异常
				return true;
			}

		}

		public String getValue() {
			return value;
		}

		public String getTitle() {
			return title;
		}
	}

	public enum MobileBlacklistType {

		NORMAL(0, "一般黑名单"), COMPLAINT(1, "投诉黑名单"), UNSUBSCRIBE(2, "退订黑名单"), OTHER(10, "其他黑名单");

		MobileBlacklistType(int code, String title) {
			this.code = code;
			this.title = title;
		}

		public static String parse(int code) {
			for (MobileBlacklistType mbt : MobileBlacklistType.values()) {
				if (mbt.code == code) {
					{
						return mbt.getTitle();
					}
				}
			}
			return MobileBlacklistType.NORMAL.getTitle();
		}

		private int code;
		private String title;

		public int getCode() {
			return code;
		}

		public String getTitle() {
			return title;
		}
	}
}
