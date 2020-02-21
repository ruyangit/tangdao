/**
 * 
 */
package com.tangdao.framework.model;

import org.springframework.lang.Nullable;

import com.tangdao.common.protocol.IEnum;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月21日
 */
public enum DataStatus implements IEnum<String, String> {

	NORMAL("0", "正常"), DELETE("1", "删除"), DISABLE("2", "停用"), FREEZE("3", "冻结"), DRAFT("9", "草稿");
	
	private final String value;

	private final String reasonPhrase;

	DataStatus(String value, String reasonPhrase) {
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
	public static DataStatus resolve(String value) {
		for (DataStatus status : values()) {
			if (status.value == value) {
				return status;
			}
		}
		return null;
	}
}
