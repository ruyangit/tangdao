/**
 * 
 */
package com.tangdao.framework.constant;

import com.tangdao.framework.protocol.IEnum;

/**
 * <p>
 * TODO 对外常量编码
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月21日
 */
public class OpenApiCode {
    
	/**
	 * 
	 * <p>
	 * TODO 结果状态
	 * </p>
	 *
	 * @author ruyangit@gmail.com
	 * @since 2020年2月23日
	 */
	public enum ResultStatus implements IEnum<String, String> {

		TOKEN_EXPIRED("5002", "认证已过期，请重新登录！"), 
		TOKEN_PARSE_ERROR("5003", "解析失败，请尝试重新登录！"), 
		TOKEN_OUT_OF_CTRL("5004", "当前用户已在别处登录，请尝试更改密码或重新登录！"), 
		TOKEN_KICKOUT_SELF("5005", "无法手动踢出自己，请尝试退出登录操作！");
		
		private final String code;
		private final String message;

		ResultStatus(String code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public String value() {
			return this.code;
		}

		@Override
		public String reasonPhrase() {
			// TODO Auto-generated method stub
			return this.message;
		}

		/**
		 * Return a string representation of this status code.
		 */
		@Override
	    public String toString() {
	        return String.format(getClass().getSimpleName() + "{code=%s, message=%s} ", value(), reasonPhrase());
	    }
	}
}
