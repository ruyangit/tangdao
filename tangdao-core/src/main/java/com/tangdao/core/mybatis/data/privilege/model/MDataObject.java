package com.tangdao.core.mybatis.data.privilege.model;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: MDataObject.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataObject {

	private String name;
	private String alias;

	public String getName() {
		if (StringUtils.isNotBlank(name)) {
			name = name.toLowerCase().trim();
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		if (StringUtils.isNotBlank(alias)) {
			alias = alias.toLowerCase().trim();
		}
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getFullName() {
		return getName() + ":" + getAlias();
	}
}
