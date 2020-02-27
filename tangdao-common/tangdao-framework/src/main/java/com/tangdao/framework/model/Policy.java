/**
 * 
 */
package com.tangdao.framework.model;

import java.util.List;

/**
 * <p>
 * TODO 策略管理
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月26日
 */
public class Policy {

	private String policyName;
	
	private String policyCode;
	
	private String policyType;
	
	public List<Assertion> assertions;

	/**
	 * @return the policyName
	 */
	public String getPolicyName() {
		return policyName;
	}

	/**
	 * @param policyName the policyName to set
	 */
	public Policy setPolicyName(String policyName) {
		this.policyName = policyName;
		return this;
	}

	/**
	 * @return the policyCode
	 */
	public String getPolicyCode() {
		return policyCode;
	}

	/**
	 * @param policyCode the policyCode to set
	 */
	public Policy setPolicyCode(String policyCode) {
		this.policyCode = policyCode;
		return this;
	}

	/**
	 * @return the policyType
	 */
	public String getPolicyType() {
		return policyType;
	}

	/**
	 * @param policyType the policyType to set
	 */
	public Policy setPolicyType(String policyType) {
		this.policyType = policyType;
		return this;
	}

	/**
	 * @return the assertions
	 */
	public List<Assertion> getAssertions() {
		return assertions;
	}

	/**
	 * @param assertions the assertions to set
	 */
	public Policy setAssertions(List<Assertion> assertions) {
		this.assertions = assertions;
		return this;
	}
	
}
