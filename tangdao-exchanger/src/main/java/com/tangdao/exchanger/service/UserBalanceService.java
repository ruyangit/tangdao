package com.tangdao.exchanger.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.model.domain.paas.UserBalance;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.UserBalanceMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserBalanceService extends BaseService<UserBalanceMapper, UserBalance>{

	/**
	 * 点对点模板参数
	 */
	private static final Pattern PATTERN_P2P_ARGS = Pattern.compile("#args#");

	@Autowired
	private UserSmsConfigService userSmsConfigService;

	@Autowired
	private UserBalanceLogService userBalanceLogService;

	
	public List<UserBalance> findByUserCode(String userCode) {
		// TODO Auto-generated method stub
		return this.select(Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getUserCode, userCode));
	}

	
	public UserBalance getByUserCode(String userCode, PlatformType type) {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(userCode)) {
			return null;
		}

		return getByUserCode(userCode, type.getCode());
	}

	
	public UserBalance getByUserCode(String userCode, int type) {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(userCode)) {
			return null;
		}

		return this.getOne(Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getUserCode, userCode)
				.eq(UserBalance::getType, type));
	}

	
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public boolean saveBalance(UserBalance balance) {
		try {
			String id = balance.getId();
			balance.setPayType(balance.getPayType() == null ? BalancePayType.PREPAY.getValue() : balance.getPayType());
			if (this.saveOrUpdate(balance)&&StringUtils.isEmpty(id)) {
				UserBalanceLog log = new UserBalanceLog();
				log.setBalance(balance.getBalance());
				log.setPayType(balance.getPayType());
				log.setUserCode(balance.getUserCode());
				log.setPlatformType(balance.getType());
				log.setPaySource(balance.getPaySource().getValue());

				return userBalanceLogService.save(log);
			}

			return false;
		} catch (Exception e) {
			log.error("保存用户 [" + balance.getUserCode() + "]失败", e);
			throw new RuntimeException(e);
		}
	}

	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean updateBalance(String userCode, int amount, int platformType, PaySource paySource, PayType payType,
			Double price, Double totalPrice, String remarks, boolean isNotice) {
		try {
			UserBalance userBalance = getByUserCode(userCode, platformType);
			userBalance.setBalance(userBalance.getBalance() + amount);
			userBalance.setUserCode(userCode);
			userBalance.setRemarks(remarks);

			// 冲扣值后统一将告警状态设置为 “正常” add by 20170827
			userBalance.setStatus(BalanceStatus.AVAIABLE.getValue());
			boolean result = super.updateById(userBalance);
			if (result) {
				UserBalanceLog log = new UserBalanceLog();
				log.setBalance(Double.valueOf(amount));
				log.setPaySource(paySource.getValue());
				log.setPayType(payType == null ? null : payType.getValue());
				log.setUserCode(userBalance.getUserCode());
				log.setPlatformType(userBalance.getType());
				log.setPrice(price);
				log.setTotalPrice(totalPrice);
				log.setRemarks(remarks);

				if (isNotice) {
					// LS
//                    notificationMessageService.save(userCode,
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

	
	public boolean deductBalance(String userCode, int amount, int type, String remarks) {
		try {
			UserBalance userBalance = getByUserCode(userCode, type);
			userBalance.setBalance(userBalance.getBalance() + amount);
			userBalance.setUserCode(userCode);
			if (StringUtils.isNotEmpty(remarks)) {
				userBalance.setRemarks(remarks);
			}

			UpdateWrapper<UserBalance> updateWrapper = new UpdateWrapper<UserBalance>();
			updateWrapper.eq("user_code", userCode);
			updateWrapper.eq("type", type);
			return this.update(userBalance, updateWrapper);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean exchange(String userCode, String fromUserCode, int type, int amount) {
		if (StringUtils.isEmpty(userCode)) {
			throw new ExchangeException("用户ID为空");
		}

		validate(fromUserCode, type, amount);

		try {
			boolean isOk = updateBalance(userCode, amount, type, PaySource.USER_ACCOUNT_EXCHANGE,
					PayType.HSUSER_EXCHANGE, null, null, "余额转赠", true);
			if (isOk) {
				return updateBalance(fromUserCode, -amount, type, PaySource.USER_ACCOUNT_EXCHANGE,
						PayType.HSUSER_EXCHANGE, null, null, "余额转赠", true);
			}
			return false;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void validate(String fromUserCode, int type, int balance) {
		if (StringUtils.isEmpty(fromUserCode)) {
			throw new ExchangeException("转存人ID为空");
		}

		if (balance == 0d) {
			throw new ExchangeException("转存额度为0");
		}

		UserBalance userBalance = getByUserCode(fromUserCode, type);
		if (userBalance == null) {
			throw new ExchangeException("用户平台额度数据为空");
		}

		if (userBalance.getBalance() < balance) {
			throw new ExchangeException(String.format("用户平台额度不足，当前余额 : %d ", balance));
		}

	}

	
	public boolean isBalanceEnough(String userCode, PlatformType type, Double fee) {
		UserBalance userBalance = getByUserCode(userCode, type);
		if (userBalance == null) {
			log.error("用户：{} ，平台类型：{} 余额数据异常，请检修", userCode, type);
			return false;
		}

		// 如果用户付费类型为后付费则不判断 余额是否不足
		if (BalancePayType.POSTPAY.getValue() == userBalance.getPayType()) {
			log.info("用户：{} ，平台类型：{} 付费类型为后付费，不检验可用余额", userCode, type);
			return true;
		}

		if (userBalance.getBalance() < fee) {
			log.warn("用户额度不足：用户ID：{} 平台类型：{} 可用余额：{} 本次计费：{}，", userCode, type, userBalance.getBalance(), fee);
			return false;
		}

		return true;
	}

	
	public UserBalance getById(int id) {
		// TODO Auto-generated method stub
		return this.get(id);
	}

	
	public boolean updateBalanceWarning(UserBalance userBalance) {
		if (userBalance == null) {
			return false;
		}
		return this.getBaseMapper().updateWarning(userBalance) > 0;
	}

	
	public List<UserBalance> findAvaibleUserBalace() {
		// TODO Auto-generated method stub
		return this.select(Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getStatus, UserBalance.STATUS_NORMAL));
	}

	
	public int calculateSmsAmount(String userCode, String content) {
		if (StringUtils.isEmpty(content)) {
			log.error("userCode :{} 短信报文为空，无法计算计费条数", userCode);
			return UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE;
		}

		// 获取短信单条计费字数
		int wordsPerNum = userSmsConfigService.getSingleChars(userCode);

		return calculateGroupSizeByContent(wordsPerNum, content);
	}

	
	public P2pBalanceResponse calculateP2pSmsAmount(String userCode, List<JSONObject> p2pBodies) {
		if (CollectionUtils.isEmpty(p2pBodies)) {
			log.error("userCode :{} 点对点短信报文为空，无法计算计费条数", userCode);
			return new P2pBalanceResponse(UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE, null);
		}

		// 总费用
		int smsTotalNum = 0;
		int wordsPerNum = userSmsConfigService.getSingleChars(userCode);
		for (JSONObject obj : p2pBodies) {
			int num = calculateGroupSizeByContent(wordsPerNum, obj.getString("content"));
			obj.put("fee", num);
			smsTotalNum += num;
		}

		return new P2pBalanceResponse(smsTotalNum, p2pBodies);
	}

	
	public P2pBalanceResponse calculateP2ptSmsAmount(String userCode, String content, List<JSONObject> p2pBody) {
		if (CollectionUtils.isEmpty(p2pBody) || StringUtils.isEmpty(content)) {
			log.error("userCode :{} 模板点对点短信内容或报文为空，无法计算计费条数", userCode);
			return new P2pBalanceResponse(UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE, null);
		}

		// 总费用
		int smsTotalNum = 0;

		int wordsPerNum = userSmsConfigService.getSingleChars(userCode);
		String finalContent;
		for (JSONObject p2b : p2pBody) {
			finalContent = translateP2pArgs(content, p2b.getObject("args", Object[].class));

			int num = calculateGroupSizeByContent(wordsPerNum, finalContent);
			p2b.put("content", finalContent);
			p2b.put("fee", num);
			smsTotalNum += num;
		}

		return new P2pBalanceResponse(smsTotalNum, p2pBody);
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
		if (StringUtils.isEmpty(content)) {
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
