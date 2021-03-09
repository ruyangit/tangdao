/**
 *
 */
package com.tangdao.developer.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.tangdao.core.constant.UserBalanceConstant;
import com.tangdao.core.context.CommonContext.PlatformType;
import com.tangdao.core.context.UserContext.BalancePayType;
import com.tangdao.core.model.domain.UserBalance;
import com.tangdao.core.service.BaseService;
import com.tangdao.developer.dao.UserBalanceMapper;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月23日
 */
@Service
public class UserBalanceService extends BaseService<UserBalanceMapper, UserBalance> {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserSmsConfigService userSmsConfigService;

	/**
	 * 
	 * TODO 获取用户余额
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public UserBalance getByUserId(String userId, int type) {
		QueryWrapper<UserBalance> queryWrapper = new QueryWrapper<UserBalance>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("type", type);
		return this.getOne(queryWrapper);
	}

	/**
	 * 
	 * TODO 根据用户传递的短信内容计算短信计费数
	 * 
	 * @param userCode
	 * @param content
	 * @return
	 */
	public int calculateSmsAmount(String appId, String content) {
		if (StrUtil.isEmpty(appId) || StrUtil.isEmpty(content)) {
			log.error("用户 :{} 短信报文为空，无法计算计费条数", appId);
			return UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE;
		}
		// 获取短信单条计费字数
		int wordsPerNum = this.userSmsConfigService.getSingleChars(appId);
		return calculateGroupSizeByContent(wordsPerNum, content);
	}

	/**
	 * 
	 * TODO 用户余额是否足够
	 * 
	 * @param userCode
	 * @param platformType
	 * @param totalFee
	 * @return
	 */
	public boolean isBalanceEnough(String userId, PlatformType platformType, double fee) {
		UserBalance userBalance = getByUserId(userId, platformType.getCode());
		if (userBalance == null) {
			log.error("用户：{} ，平台类型：{} 余额数据异常，请检修", userId, platformType);
			return false;
		}
		// 如果用户付费类型为后付费则不判断 余额是否不足
		if (BalancePayType.POSTPAY.getValue() == userBalance.getPayType()) {
			log.info("用户：{} ，平台类型：{} 付费类型为后付费，不检验可用余额", userId, platformType);
			return true;
		}
		//
		if (userBalance.getBalance() < fee) {
			log.warn("用户额度不足：用户：{} 平台类型：{} 可用余额：{} 本次计费：{}，", userId, platformType, userBalance.getBalance(), fee);
			return false;
		}

		return true;
	}
	
	/**
	 * 
	 * TODO
	 * @param userCode
	 * @param amount
	 * @param platformType
	 * @param remark
	 * @return
	 */
	public boolean deductBalance(String userId, int amount, int platformType, String remarks) {
        try {
            UserBalance userBalance = getByUserId(userId, platformType);
            userBalance.setBalance(userBalance.getBalance() + amount);
            userBalance.setUserId(userId);
            if (StrUtil.isNotEmpty(remarks)) {
                userBalance.setRemarks(remarks);
            }
            userBalance.setUpdateBy(userId);
            userBalance.setUpdateDate(new Date());
            return SqlHelper.retBool(this.getBaseMapper().updateById(userBalance));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * TODO 根据短信内容和每条计费字数 计算总费用
	 * 
	 * @param wordsPerNum 每条计费字数
	 * @param content     短信内容
	 * @return
	 */
	private int calculateGroupSizeByContent(int wordsPerNum, String content) {
		if (StrUtil.isEmpty(content)) {
			return UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE;
		}
		// 按照70个字为单位，减去设置的第一条短信计费字数，得出实际签名内容长度
		int realTotalWords = UserBalanceConstant.WORDS_SIZE_PER_NUM - wordsPerNum + content.length();
		if (realTotalWords <= UserBalanceConstant.WORDS_SIZE_PER_NUM) {
			// 如果减除的字数后 小于等于0，则按照1条计费
			return UserBalanceConstant.CONTENT_SINGLE_FEE;
		}
		// 长短信计费按照67字计费
		return realTotalWords % UserBalanceConstant.LONG_TEXT_MESSAGE_WORDS_SIZE_PER_NUM == 0
				? realTotalWords / UserBalanceConstant.LONG_TEXT_MESSAGE_WORDS_SIZE_PER_NUM
				: realTotalWords / UserBalanceConstant.LONG_TEXT_MESSAGE_WORDS_SIZE_PER_NUM + 1;
	}
}
