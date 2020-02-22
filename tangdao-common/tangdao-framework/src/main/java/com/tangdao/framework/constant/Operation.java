/**
 * 
 */
package com.tangdao.framework.constant;

import org.springframework.lang.Nullable;

import com.tangdao.framework.protocol.IEnum;

/**
 * 
 * <p>
 * TODO 响应消息
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
public enum Operation implements IEnum<String, String>{
	
	ADD("add", "新增"), EDIT("edit", "编辑"), AUTH("auth", "授权"), DEL("del", "删除");
	
	private final String value;
	private final String reasonPhrase;

	private Operation(String value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	@Override
	public String value() {
		return this.value;
	}

	@Override
	public String reasonPhrase() {
		// TODO Auto-generated method stub
		return this.reasonPhrase;
	}

	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return this.value;
	}

	/**
	 * Resolve the given status code to an {@code DataStatus}, if possible.
	 * @param value the HTTP status code (potentially non-standard)
	 * @return the corresponding {@code ResultStatus}, or {@code null} if not found
	 * @since 5.0
	 */
	@Nullable
	public static Operation resolve(String value) {
		for (Operation status : values()) {
			if (status.value == value) {
				return status;
			}
		}
		return null;
	}
}
