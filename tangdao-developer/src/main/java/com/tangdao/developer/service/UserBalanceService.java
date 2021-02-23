/**
 *
 */
package com.tangdao.developer.service;

import org.springframework.stereotype.Service;

import com.tangdao.core.constant.CommonContext.PlatformType;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月23日
 */
@Service
public class UserBalanceService {

	public int calculateSmsAmount(String userCode, String content) {
		return 0;
	}

	public boolean isBalanceEnough(String userCode, PlatformType platformType, double totalFee) {
		return false;
	}
}
