/**
 *
 */
package com.tangdao.core.constant;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月12日
 */
public class CommonContext {

	public static final String YES = "1";
	public static final String NO = "0";
	
	public enum UserSource {

		WEB("网页注册"),

		DEVELOPER("开发者平台");

		private String name;

		UserSource(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
