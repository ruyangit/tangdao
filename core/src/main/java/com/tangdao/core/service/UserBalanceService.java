package com.tangdao.core.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.UserBalanceConstant;
import com.tangdao.core.context.CommonContext.PlatformType;
import com.tangdao.core.context.CommonContext.Status;
import com.tangdao.core.context.PayContext.PaySource;
import com.tangdao.core.context.PayContext.PayType;
import com.tangdao.core.context.UserContext.BalancePayType;
import com.tangdao.core.context.UserContext.BalanceStatus;
import com.tangdao.core.dao.UserBalanceMapper;
import com.tangdao.core.exception.DataEmptyException;
import com.tangdao.core.model.domain.UserBalance;
import com.tangdao.core.model.domain.UserBalanceLog;
import com.tangdao.core.model.vo.P2pBalance;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class UserBalanceService extends BaseService<UserBalanceMapper, UserBalance> {

	/**
	 * 点对点模板参数
	 */
	private static final Pattern PATTERN_P2P_ARGS = Pattern.compile("#args#");

	@Autowired
	private UserSmsConfigService userSmsConfigService;

	@Autowired
	private UserBalanceLogService userBalanceLogService;

	public List<UserBalance> findByUserId(String userId) {
		// TODO Auto-generated method stub
		return this.list(Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getUserId, userId));
	}

	public UserBalance getByUserId(String userId, PlatformType type) {
		// TODO Auto-generated method stub
		if (StrUtil.isEmpty(userId)) {
			return null;
		}
		return getByUserId(userId, type.getCode());
	}

	public UserBalance getByUserId(String userId, int type) {
		// TODO Auto-generated method stub
		if (StrUtil.isEmpty(userId)) {
			return null;
		}

		return this.getOne(
				Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getUserId, userId).eq(UserBalance::getType, type));
	}

	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public boolean saveBalance(UserBalance balance) {
		try {
			String id = balance.getId();
			balance.setPayType(balance.getPayType() == null ? BalancePayType.PREPAY.getValue() : balance.getPayType());
			if (this.saveOrUpdate(balance) && StrUtil.isEmpty(id)) {
				UserBalanceLog log = new UserBalanceLog();
				log.setBalance(balance.getBalance());
				log.setPayType(balance.getPayType());
				log.setUserId(balance.getUserId());
				log.setPlatformType(balance.getType());
				log.setPaySource(balance.getPaySource().getValue());

				return userBalanceLogService.save(log);
			}

			return false;
		} catch (Exception e) {
			log.error("保存用户 [" + balance.getUserId() + "]失败", e);
			throw new RuntimeException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean updateBalance(String userId, int amount, int platformType, PaySource paySource, PayType payType,
			Double price, Double totalPrice, String remarks, boolean isNotice) {
		try {
			UserBalance userBalance = getByUserId(userId, platformType);
			userBalance.setBalance(userBalance.getBalance() + amount);
			userBalance.setUserId(userId);
			userBalance.setRemarks(remarks);

			// 冲扣值后统一将告警状态设置为 “正常” add by 20170827
			userBalance.setStatus(BalanceStatus.AVAIABLE.getValue() + "");
			boolean result = super.updateById(userBalance);
			if (result) {
				UserBalanceLog log = new UserBalanceLog();
				log.setBalance(Double.valueOf(amount));
				log.setPaySource(paySource.getValue());
				log.setPayType(payType == null ? null : payType.getValue());
				log.setUserId(userBalance.getUserId());
				log.setPlatformType(userBalance.getType());
				log.setPrice(price);
				log.setTotalPrice(totalPrice);
				log.setRemarks(remarks);

				if (isNotice) {
					// LS
//                    notificationMessageService.save(userId,
//                                                    NotificationMessageTemplateType.USER_BALACE_CHANGE,
//                                                    String.format(NotificationMessageTemplateType.USER_BALACE_CHANGE.getContent(),
//                                                                  PlatformType.parse(platformType).getName(),
//                                                                  amount));
				}

				return userBalanceLogService.save(log);
			}

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public boolean deductBalance(String userId, int amount, int type, String remarks) {
		try {
			UserBalance userBalance = getByUserId(userId, type);
			userBalance.setBalance(userBalance.getBalance() + amount);
			userBalance.setUserId(userId);
			if (StrUtil.isNotEmpty(remarks)) {
				userBalance.setRemarks(remarks);
			}

			UpdateWrapper<UserBalance> updateWrapper = new UpdateWrapper<UserBalance>();
			updateWrapper.eq("user_id", userId);
			updateWrapper.eq("type", type);
			return this.update(userBalance, updateWrapper);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean exchange(String userId, String fromuserId, int type, int amount) {
		if (StrUtil.isEmpty(userId)) {
			throw new DataEmptyException("用户ID为空");
		}

		validate(fromuserId, type, amount);

		try {
			boolean isOk = updateBalance(userId, amount, type, PaySource.USER_ACCOUNT_EXCHANGE,
					PayType.HSUSER_EXCHANGE, null, null, "余额转赠", true);
			if (isOk) {
				return updateBalance(fromuserId, -amount, type, PaySource.USER_ACCOUNT_EXCHANGE,
						PayType.HSUSER_EXCHANGE, null, null, "余额转赠", true);
			}
			return false;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void validate(String fromUserId, int type, int balance) {
		if (StrUtil.isEmpty(fromUserId)) {
			throw new DataEmptyException("转存人ID为空");
		}

		if (balance == 0d) {
			throw new DataEmptyException("转存额度为0");
		}

		UserBalance userBalance = getByUserId(fromUserId, type);
		if (userBalance == null) {
			throw new DataEmptyException("用户平台额度数据为空");
		}

		if (userBalance.getBalance() < balance) {
			throw new DataEmptyException(String.format("用户平台额度不足，当前余额 : %d ", balance));
		}

	}

	public boolean isBalanceEnough(String userId, PlatformType type, Double fee) {
		UserBalance userBalance = getByUserId(userId, type);
		if (userBalance == null) {
			logger.error("用户：{} ，平台类型：{} 余额数据异常，请检修", userId, type);
			return false;
		}

		// 如果用户付费类型为后付费则不判断 余额是否不足
		if (BalancePayType.POSTPAY.getValue() == userBalance.getPayType()) {
			logger.info("用户：{} ，平台类型：{} 付费类型为后付费，不检验可用余额", userId, type);
			return true;
		}

		if (userBalance.getBalance() < fee) {
			logger.warn("用户额度不足：用户ID：{} 平台类型：{} 可用余额：{} 本次计费：{}，", userId, type, userBalance.getBalance(), fee);
			return false;
		}

		return true;
	}

	public boolean updateBalanceWarning(UserBalance userBalance) {
		if (userBalance == null) {
			return false;
		}
		return this.getBaseMapper().updateWarning(userBalance) > 0;
	}

	public List<UserBalance> findAvaibleUserBalace() {
		// TODO Auto-generated method stub
		return this.list(Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getStatus, Status.NORMAL));
	}

	public int calculateSmsAmount(String userId, String content) {
		if (StrUtil.isEmpty(content)) {
			logger.error("userId :{} 短信报文为空，无法计算计费条数", userId);
			return UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE;
		}

		// 获取短信单条计费字数
		int wordsPerNum = userSmsConfigService.getSingleChars(userId);

		return calculateGroupSizeByContent(wordsPerNum, content);
	}

	public P2pBalance calculateP2pSmsAmount(String userId, List<JSONObject> p2pBodies) {
		if (CollUtil.isEmpty(p2pBodies)) {
			logger.error("userId :{} 点对点短信报文为空，无法计算计费条数", userId);
			return new P2pBalance(UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE, null);
		}

		// 总费用
		int smsTotalNum = 0;
		int wordsPerNum = userSmsConfigService.getSingleChars(userId);
		for (JSONObject obj : p2pBodies) {
			int num = calculateGroupSizeByContent(wordsPerNum, obj.getString("content"));
			obj.put("fee", num);
			smsTotalNum += num;
		}

		return new P2pBalance(smsTotalNum, p2pBodies);
	}

	public P2pBalance calculateP2ptSmsAmount(String userId, String content, List<JSONObject> p2pBody) {
		if (CollUtil.isEmpty(p2pBody) || StrUtil.isEmpty(content)) {
			logger.error("userId :{} 模板点对点短信内容或报文为空，无法计算计费条数", userId);
			return new P2pBalance(UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE, null);
		}

		// 总费用
		int smsTotalNum = 0;

		int wordsPerNum = userSmsConfigService.getSingleChars(userId);
		String finalContent;
		for (JSONObject p2b : p2pBody) {
			finalContent = translateP2pArgs(content, p2b.getObject("args", Object[].class));

			int num = calculateGroupSizeByContent(wordsPerNum, finalContent);
			p2b.put("content", finalContent);
			p2b.put("fee", num);
			smsTotalNum += num;
		}

		return new P2pBalance(smsTotalNum, p2pBody);
	}

	/**
	 * TODO 替换模板点对点短信内容
	 * 
	 * @param content 短信内容
	 * @param args    参数信息
	 * @return
	 */
	private static String translateP2pArgs(String content, Object[] args) {
		Matcher matcher = PATTERN_P2P_ARGS.matcher(content);

		StringBuffer finalContent = new StringBuffer();
		int index = 0;
		while (matcher.find()) {
			if (index >= args.length) {
				break;
			}

			matcher.appendReplacement(finalContent, args[index].toString());
			index++;
		}

		matcher.appendTail(finalContent);// 添加尾巴
		return finalContent.toString();
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