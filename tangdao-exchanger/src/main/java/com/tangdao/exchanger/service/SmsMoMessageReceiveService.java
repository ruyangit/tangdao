package com.tangdao.exchanger.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.tangdao.common.constant.CommonContext.CallbackUrlType;
import org.tangdao.common.constant.RabbitConstant;
import org.tangdao.common.lang.StringUtils;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.constant.SmsRedisConstant;
import org.tangdao.modules.sms.constant.SmsSettingsContext.MobileBlacklistType;
import org.tangdao.modules.sms.mapper.SmsMoMessageReceiveMapper;
import org.tangdao.modules.sms.model.domain.SmsMoMessageReceive;
import org.tangdao.modules.sms.model.domain.SmsMobileBlacklist;
import org.tangdao.modules.sms.model.domain.SmsMtMessageSubmit;
import org.tangdao.modules.sys.constant.SettingsContext.SystemConfigType;
import org.tangdao.modules.sys.model.domain.PushConfig;
import org.tangdao.modules.sys.service.IPushConfigService;

import com.alibaba.fastjson.JSON;

/**
 * 上行消息回复ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMoMessageReceiveService extends CrudService<SmsMoMessageReceiveMapper, SmsMoMessageReceive>
		implements ISmsMoReceiveService {

	@Autowired
	private RabbitTemplate smsRabbitTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private IPushConfigService pushConfigService;
//    @Autowired
//    private ISmsPassageService         smsPassageService;

	@Autowired
	private ISmsMtSubmitService smsMtSubmitService;
//    @Autowired
//    private SmsMoMessagePushMapper     smsMoMessagePushMapper;
//    @Autowired
//    private SmsMoMessageReceiveMapper  moMessageReceiveMapper;
	@Autowired
	private ISmsMobileBlackListService smsMobileBlackListService;

	/**
	 * 恒生电子定制服务状态是否开启 0：未开启，1:已开启
	 */
	@Value("${custom.mo.hundsun.run:0}")
	private int hundsunRunStatus;

	/**
	 * 恒生电子用户Id
	 */
	@Value("${custom.mo.hundsun.userCode}")
	private String hundsunUserCode;

	/**
	 * 恒生电子定制短信上行码号
	 */
	@Value("${custom.mo.hundsun.destnationNo}")
	private String hundsunDestnationNo;

	/**
	 * 运行状态：运行中...
	 */
	private static final int RUNNING_STATUS = 1;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public int doFinishReceive(List<SmsMoMessageReceive> list) {
		int count = 0;
		try {
			for (SmsMoMessageReceive receive : list) {

				// // 31省测试上行自动触发下行功能（测试后上线去掉此逻辑） add by 20170709
				// if(StringUtils.isNotEmpty(receive.getDestnationNo()) &&
				// receive.getDestnationNo().startsWith("10691348")) {
				// logger.info("数据已发送短信 {}", JSON.toJSONString(receive));
				// MessageSendUtil.sms("http://api.hspaas.cn:8080/sms/send", "hsjXxJ2gO75iOK",
				// "f7c3ddd27a61fbe9612b2b9f1e6c8287",
				// receive.getMobile(), "【惠尔睿智】31省根据上行自动下行");
				// }

				// add by 20180730 如果属于恒生指定的码号上行，则按照固定恒生处理
				if (processIfHundsunMatched(receive)) {
					continue;
				}

				// 根据通道ID和消息ID
				SmsMtMessageSubmit submit = smsMtSubmitService.getByMoMapping(receive.getPassageId(),
						receive.getMsgId(), receive.getMobile(), receive.getDestnationNo());
				if (submit != null) {
					receive.setMsgId(submit.getMsgId());
					receive.setUserCode(submit.getUserCode());
					receive.setSid(submit.getSid() + "");

					// 如果上行回执内容为空，则置一个空格字符
					if (StringUtils.isEmpty(receive.getContent())) {
						receive.setContent(" ");
					}

					// 针对直连协议PassageId反补
					receive.setPassageId(submit.getPassageId());

					count++;

					// 判断是否包含退订关键词，如果包含直接加入黑名单
					joinBlacklistIfMatched(receive.getMobile(), receive.getContent(),
							String.format("SID:%d,MSG_ID:%s", submit.getSid(), submit.getMsgId()));

					PushConfig pushConfig = pushConfigService.getByUserCode(submit.getUserCode(),
							CallbackUrlType.SMS_MO.getCode());
					if (pushConfig == null || StringUtils.isEmpty(pushConfig.getUrl())) {
						receive.setNeedPush(false);
						continue;
					}

					receive.setNeedPush(true);
					receive.setPushUrl(pushConfig.getUrl());
					receive.setRetryTimes(pushConfig.getRetryTimes());

					sendToPushQueue(receive);
				}
			}

			// 插入DB
			super.saveBatch(list);

			return count;

		} catch (Exception e) {
			logger.error("处理待回执信息失败，失败信息：{}", JSON.toJSONString(list), e);
			return 0;
		}
	}

	/**
	 * 
	 * TODO 如果属于恒生上行码号，则按照恒生指定的方式执行
	 * 
	 * @param receive
	 * @return
	 */
	private boolean processIfHundsunMatched(SmsMoMessageReceive receive) {
		try {
			if (RUNNING_STATUS != hundsunRunStatus || receive == null || StringUtils.isEmpty(hundsunDestnationNo)
					|| !receive.getDestnationNo().startsWith(hundsunDestnationNo)) {
				return false;
			}

			receive.setUserCode(hundsunUserCode);
			PushConfig pushConfig = pushConfigService.getByUserCode(hundsunUserCode, CallbackUrlType.SMS_MO.getCode());
			if (pushConfig == null || StringUtils.isEmpty(pushConfig.getUrl())) {
				receive.setNeedPush(false);
				return true;
			}

			receive.setNeedPush(true);
			receive.setPushUrl(pushConfig.getUrl());
			receive.setRetryTimes(pushConfig.getRetryTimes());

			sendToPushQueue(receive);

			return true;
		} catch (Exception e) {
			logger.error("【恒生电子】处理上行信息失败，失败信息：{}", JSON.toJSONString(receive), e);
			return false;
		}
	}

	public String[] getBlacklistWords() {
		try {
			String value = stringRedisTemplate.opsForValue().get(SystemConfigType.WORDS_LIBRARY.name());
			if (StringUtils.isEmpty(value)) {
				return null;
			}

			return value.split(",");
		} catch (Exception e) {
			logger.error("查询黑名单词库失败", e);
			return null;
		}
	}

	/**
	 * TODO 根据上行回执短信内容判断是否需要加入黑名单
	 *
	 * @param mobile
	 * @param content
	 */
	private void joinBlacklistIfMatched(String mobile, String content, String remarks) {
		// 判断回复内容是否包含 黑名单词库内容，如果包括则直接加入黑名单
		String[] words = getBlacklistWords();
		if (words == null || words.length == 0) {
			words = BLACKLIST_WORDS;
		}

		boolean isContains = false;
		for (String wd : words) {
			if (StringUtils.isEmpty(wd)) {
				continue;
			}

			if (content.toUpperCase().contains(wd.toUpperCase())) {
				isContains = true;
				break;
			}
		}

		if (!isContains) {
			return;
		}

		SmsMobileBlacklist blacklist = new SmsMobileBlacklist();
		blacklist.setMobile(mobile);
		blacklist.setType(MobileBlacklistType.UNSUBSCRIBE.getCode());
		blacklist.setRemarks(remarks);

		smsMobileBlackListService.batchInsert(blacklist);
	}

	/**
	 * 默认黑名单词库（当用户上行回复下列词汇，会自动将回复的手机号码加入黑名单手机号码中）
	 */
	private static final String[] BLACKLIST_WORDS = { "TD", "T", "N" };

	@Override
	public boolean doReceiveToException(Object obj) {
		try {
			stringRedisTemplate.opsForList().rightPush(SmsRedisConstant.RED_MESSAGE_MO_RECEIPT_EXCEPTION_LIST,
					JSON.toJSONString(obj));
			return true;
		} catch (Exception e) {
			logger.error("上行回执错误信息失败 {}", JSON.toJSON(obj), e);
			return false;
		}
	}

	/**
	 * 
	 * TODO 发送上行推送数据至消息队列中
	 * 
	 * @param receive
	 */
	private void sendToPushQueue(SmsMoMessageReceive receive) {
		try {
			// 发送至待推送信息队列
			smsRabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE_SMS, RabbitConstant.MQ_SMS_MO_WAIT_PUSH, receive);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}