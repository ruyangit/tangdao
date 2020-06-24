package com.tangdao.core.mybatis.data.privilege.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: DataPrivilegeParameter.java
 * @author: Naughty Guo
 * @date: Jun 10, 2016
 */
public class DataPrivilegeParameter {

	private String originalSql;
	private MDataPrivilege mDataPrivilege;
	private Set<String> legalObjects;
	private Map<String, List<String>> allJoins;
	private Map<String, Boolean> filterCategory;
	private Map<String, Object> privilegeData;

	public String getOriginalSql() {
		return originalSql;
	}

	public void setOriginalSql(String originalSql) {
		this.originalSql = originalSql;
	}

	public MDataPrivilege getMDataPrivilege() {
		return mDataPrivilege;
	}

	public void setMDataPrivilege(MDataPrivilege mDataPrivilege) {
		this.mDataPrivilege = mDataPrivilege;
	}

	public Set<String> getLegalObjects() {
		return legalObjects;
	}

	public void setLegalObjects(Set<String> legalObjects) {
		this.legalObjects = legalObjects;
	}

	public Map<String, List<String>> getAllJoins() {
		return allJoins;
	}

	public void setAllJoins(Map<String, List<String>> allJoins) {
		this.allJoins = allJoins;
	}

	public Map<String, Boolean> getFilterCategory() {
		return filterCategory;
	}

	public void setFilterCategory(Map<String, Boolean> filterCategory) {
		this.filterCategory = filterCategory;
	}

	public Map<String, Object> getPrivilegeData() {
		return privilegeData;
	}

	public void setPrivilegeData(Map<String, Object> privilegeData) {
		this.privilegeData = privilegeData;
	}
	
	/**
	 * check whether do condition.
	 * 
	 * @param mDataColumn
	 * @return boolean
	 * @author Naughty Guo 2016年7月11日
	 */
	@SuppressWarnings("unchecked")
	public boolean doConditionable(MDataColumn mDataColumn) {
		Map<String, Object> groupPrivilegeDatas = null;
		String categoryKey = mDataColumn.getCategoryKey();
		if (StringUtils.isBlank(categoryKey)) {
			return false;
		}
		if (null != filterCategory && filterCategory.containsKey(categoryKey) && !filterCategory.get(categoryKey)) {
			return false;
		}
		if (null != privilegeData) {
			if (privilegeData.containsKey(categoryKey) && null != privilegeData.get(categoryKey)) {
				return true;
			}
			for (Map.Entry<String, Object> entry : privilegeData.entrySet()) {
				if (entry.getValue() instanceof Collection<?>) {
					for (Object item : (Collection<?>) entry.getValue()) {
						groupPrivilegeDatas = (Map<String, Object>) item;
						if (null != groupPrivilegeDatas && groupPrivilegeDatas.containsKey(categoryKey) && 
								null != groupPrivilegeDatas.get(categoryKey)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * get privilege data.
	 * 
	 * @param mDataColumn
	 * @return Object
	 * @author Naughty Guo 2016年7月11日
	 */
	public Object getPrivilegeData(MDataColumn mDataColumn) {
		if (null != privilegeData && StringUtils.isNotBlank(mDataColumn.getCategoryKey()) && 
			privilegeData.containsKey(mDataColumn.getCategoryKey())) {
			return privilegeData.get(mDataColumn.getCategoryKey());
		}
		if (StringUtils.isNotBlank(mDataColumn.getValue())) {
			return mDataColumn.getValue();
		}
		return null;
	}
	
	/**
	 * copy instance without privilege data.
	 * 
	 * @return DataPrivilegeParameter
	 * @author Naughty Guo 2016年7月14日
	 */
	public DataPrivilegeParameter copyInstance() {
		DataPrivilegeParameter dataPrivilegeParameter = new DataPrivilegeParameter();
		dataPrivilegeParameter.setFilterCategory(filterCategory);
		dataPrivilegeParameter.setMDataPrivilege(mDataPrivilege);
		dataPrivilegeParameter.setOriginalSql(originalSql);
		return dataPrivilegeParameter;
	}
}
