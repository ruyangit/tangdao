package com.tangdao.exchanger.resolver.sms.smgp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.huawei.insa2.comm.smgp.message.SMGPDeliverMessage;
import com.huawei.insa2.comm.smgp.message.SMGPSubmitMessage;
import com.huawei.insa2.comm.smgp.message.SMGPSubmitRespMessage;
import com.tangdao.core.constant.RabbitConstant;
import com.tangdao.core.context.CommonContext.CMCP;
import com.tangdao.core.context.PassageContext.DeliverStatus;
import com.tangdao.core.context.TaskContext.MessageSubmitStatus;
import com.tangdao.core.model.domain.sms.MoMessageReceive;
import com.tangdao.core.model.domain.sms.MtMessageDeliver;
import com.tangdao.core.model.domain.sms.PassageParameter;
import com.tangdao.exchanger.model.response.ProviderSendResponse;
import com.tangdao.exchanger.resolver.sms.AbstractSmsProxySender;
import com.tangdao.exchanger.resolver.sms.cmpp.constant.CmppConstant;
import com.tangdao.exchanger.resolver.sms.sgip.constant.SgipConstant;
import com.tangdao.exchanger.resolver.sms.smgp.constant.SmgpConstant;
import com.tangdao.exchanger.template.handler.RequestTemplateHandler;
import com.tangdao.exchanger.template.vo.TParameter;
import com.tangdao.exchanger.utils.MobileNumberCatagoryUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

@Service
public class SmgpProxySender extends AbstractSmsProxySender {

	/**
	 * 对于Proxy分发短信的接收
	 * 
	 * @param msg
	 */
	public void doProcessDeliverMessage(SMGPDeliverMessage msg) {
		if (msg == null) {
			return;
		}

		if (msg.getIsReport() == 0) {
			// 上行报告
			moReceive(msg);
		} else {
			// 短信状态回执
			mtDeliver(msg);
		}
	}

	private final AtomicInteger LONG_MESSGE_CONTENT_COUNTER = new AtomicInteger(1);

	/**
	 * 短信内容转换为 字节数。普通短信 GBK编码。长短信 UCS2编码
	 * 
	 * @param message
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private List<byte[]> getLongByte(String message) throws UnsupportedEncodingException {
		List<byte[]> list = new ArrayList<byte[]>();
		byte[] messageUCS2 = message.getBytes("UnicodeBigUnmarked");
		// int messageGBKLen = messageGBK.length;// 短信长度
		// 超过70字符以长短信发送
		if (message.length() > 70) {
			// if (messageGBKLen > maxMessageGBKLen) {
			// 如果字节数大于 GBK，则使用UCS2编码以长短信发送
			// byte[] messageUCS2 = message.getBytes("UnicodeBigUnmarked");
			int messageUCS2Len = messageUCS2.length;
			int yushu = messageUCS2Len % (SmgpConstant.MAX_MESSAGE_UCS2_LENGTH - 6);
			int add = 0;
			if (yushu > 0) {
				add = 1;
			}
			int messageUCS2Count = messageUCS2Len / (SmgpConstant.MAX_MESSAGE_UCS2_LENGTH - 6) + add;// 长短信分为多少条发送

			byte[] tp_udhiHead = new byte[6];
			tp_udhiHead[0] = 0x05;
			tp_udhiHead[1] = 0x00;
			tp_udhiHead[2] = 0x03;
			tp_udhiHead[3] = (byte) LONG_MESSGE_CONTENT_COUNTER.get();// 0x0A
			tp_udhiHead[4] = (byte) messageUCS2Count;
			tp_udhiHead[5] = 0x01;// 默认为第一条
			if (LONG_MESSGE_CONTENT_COUNTER.incrementAndGet() == 256) {
				LONG_MESSGE_CONTENT_COUNTER.set(1);
			}

			for (int i = 0; i < messageUCS2Count; i++) {
				tp_udhiHead[5] = (byte) (i + 1);
				byte[] msgContent;
				if (i != messageUCS2Count - 1) {// 不为最后一条
					msgContent = byteAdd(tp_udhiHead, messageUCS2, i * (SmgpConstant.MAX_MESSAGE_UCS2_LENGTH - 6),
							(i + 1) * (SmgpConstant.MAX_MESSAGE_UCS2_LENGTH - 6));
					list.add(msgContent);
				} else {
					msgContent = byteAdd(tp_udhiHead, messageUCS2, i * (SmgpConstant.MAX_MESSAGE_UCS2_LENGTH - 6),
							messageUCS2Len);
					list.add(msgContent);
				}
				// logger.debug("msgContent "+new
				// String(msgContent,"UnicodeBigUnmarked"));
			}
		} else {
			list.add(message.getBytes("GBK"));
		}
		return list;
	}

	private static byte[] byteAdd(byte[] tpUdhiHead, byte[] messageUCS2, int i, int j) {
		byte[] msgb = new byte[j - i + 6];
		System.arraycopy(tpUdhiHead, 0, msgb, 0, 6);
		System.arraycopy(messageUCS2, i, msgb, 6, j - i);
		return msgb;
	}

	// @PreDestroy
	// public void terminate() {
	// System.out.println("SMC下发终断消息");
	// cmppProxyReceiver.close();
	// cmppProxyReceiver = null;
	// }

	/**
	 * TODO 发送短信接口
	 * 
	 * @param parameter 通道参数信息
	 * @param extNumber 客户扩展号码
	 * @param mobile    手机号码（支持多个）
	 * @param content   短信内容
	 * @return
	 */
	public List<ProviderSendResponse> send(PassageParameter parameter, String extNumber, String mobile,
			String content) {
		try {
			TParameter tparameter = RequestTemplateHandler.parse(parameter.getParams());
			if (CollUtil.isEmpty(tparameter)) {
				throw new RuntimeException("SMGP 参数信息为空");
			}

			SmgpManageProxy smgpManageProxy = (SmgpManageProxy) getSmManageProxy(parameter);
			if (smgpManageProxy == null) {
				logger.error("SMGP 代理获取失败，手机号码：{}， 短信内容：{}，扩展号码：{}", mobile, content, extNumber);

				return null;
			}

			// 接入号码（如果扩展号码不为空，则加入扩展号码）
			String srcTerminalId = tparameter.getString("src_terminal_id")
					+ (StrUtil.isEmpty(extNumber) ? "" : extNumber);

			// 获取发送回执信息
			SMGPSubmitRespMessage submitRepMsg = getSMGPSubmitResponseMessage(tparameter.getString("spid"),
					StrUtil.isEmpty(tparameter.getString("msg_fmt")) ? CmppConstant.MSG_FMT_GBK
							: Integer.parseInt(tparameter.getString("msg_fmt")),
							StrUtil.isEmpty(tparameter.getString("mobile")) ? "" : tparameter.getString("mobile"),
					srcTerminalId, mobile, content, "", smgpManageProxy);

			List<ProviderSendResponse> list = new ArrayList<>();
			ProviderSendResponse response = new ProviderSendResponse();
			if (submitRepMsg == null) {
				logger.error("SMGPSubmitRespMessage 网关提交信息为空");
				smsProxyService.plusSendErrorTimes(parameter.getPassageId());
				return null;
			}

			if (submitRepMsg.getStatus() == MessageSubmitStatus.SUCCESS.getCode()) {
				// 发送成功清空
				smsProxyService.clearSendErrorTimes(parameter.getPassageId());

				response.setMobile(mobile);
				response.setStatusCode(submitRepMsg.getStatus() + "");
				response.setSid(submitRepMsg.bytesToHexString(submitRepMsg.getMsgId()));
				response.setSuccess(true);
				response.setRemarks(
						String.format("{msgId:%s, sequenceId:%d}", response.getSid(), submitRepMsg.getSequenceId()));

				list.add(response);
			} else {
				response.setMobile(mobile);
				response.setStatusCode(submitRepMsg.getStatus() + "");
				response.setSid(submitRepMsg.bytesToHexString(submitRepMsg.getMsgId()));
				response.setSuccess(false);
				response.setRemarks(String.format("{status:%d, sequenceId:%d}", submitRepMsg.getStatus(),
						submitRepMsg.getSequenceId()));

				list.add(response);
			}

			return list;
		} catch (Exception e) {
			// 累加发送错误次数
			smsProxyService.plusSendErrorTimes(parameter.getPassageId());

			logger.error("SMGP发送失败", e);
			throw new RuntimeException("SMGP发送失败");
		}
	}

	/**
	 * TODO 组装待提交短信SMGP结构体数据
	 * 
	 * @param spid          企业代码
	 * @param msgFmt        消息编码方式（默认GBK）
	 * @param chargeNumber  计费手机号码
	 * @param srcTerminalId 接入号（包含用于配置的扩展号码）
	 * @param mobile        接收短信手机号码
	 * @param content       短信内容
	 * @param reserve       扩展项，用于自定义内容
	 * @return
	 * @throws IOException
	 */
	private SMGPSubmitRespMessage getSMGPSubmitResponseMessage(String spid, int msgFmt, String chargeNumber,
			String srcTerminalId, String mobile, String content, String reserve, SmgpManageProxy smgpManageProxy)
			throws IOException {

		// 存活有效期（2天）
		Date validTime = new Date(System.currentTimeMillis() + (long) 0xa4cb800);
		// 定时发送时间
		Date atTime = null;// new Date(System.currentTimeMillis() + (long)//
							// 0xa4cb800); //new Date();

		int tpUdhi = SmgpConstant.TP_UDHI;
		// 全都改成以 UTF 16 格式提交
		List<byte[]> contentList = getLongByte(content);
		if (contentList.size() > 1) {
			// 长短信格式发送 增加协议头 改变编码方式
			tpUdhi = 1;
			msgFmt = SmgpConstant.MSG_FMT_UCS2;
		}

		SMGPSubmitMessage submitMsg = null;
		SMGPSubmitRespMessage submitRepMsg = null;
		for (int index = 1; index <= contentList.size(); index++) {
			submitMsg = getSMGPSubmitMessage(tpUdhi, msgFmt, spid, validTime, atTime, chargeNumber, srcTerminalId,
					mobile.split(MobileNumberCatagoryUtil.DATA_SPLIT_CHARCATOR), contentList.get(index - 1), reserve);

			SMGPSubmitRespMessage repMsg = (SMGPSubmitRespMessage) smgpManageProxy.send(submitMsg);
			if (index == 1) {
				// 以长短信 拆分发送时，目前状态报告的 msgid 是 拆分后第一条的 msgid
				submitRepMsg = repMsg;
			} else if (submitRepMsg == null) {
				submitRepMsg = repMsg;
			}

			if (submitRepMsg == null) {
				logger.error(" SmgpSubmitRepMessage null, submitMsg : {}", submitMsg);
			} else if (submitRepMsg.getStatus() != 0) {
				logger.error(" submitRepMsg result :{}, index : {}, content: {}, mobile : {}", submitRepMsg.getStatus(),
						index, content, mobile);
			}

		}

		return submitRepMsg;
	}

	/**
	 * TODO 组装SMGP提交信息
	 * 
	 * @param tpUdhi
	 * @param msgFmt
	 * @param spid
	 * @param validTime
	 * @param atTime
	 * @param chargeNumber  扣费号码
	 * @param srcTerminalId
	 * @param mobiles
	 * @param msgContent
	 * @param reserve
	 * @return
	 */
	private SMGPSubmitMessage getSMGPSubmitMessage(int tpUdhi, int msgFmt, String spid, Date validTime, Date atTime,
			String chargeNumber, String srcTerminalId, String[] mobiles, byte[] msgContent, String reserve) {

		return new SMGPSubmitMessage(SmgpConstant.MSG_TYPE, SmgpConstant.NEED_REPORT, SmgpConstant.PRIORITY, spid,
				SmgpConstant.FEE_TYPE, SmgpConstant.FEE_CODE, SmgpConstant.FIXED_FEE, msgFmt, validTime, atTime,
				srcTerminalId, chargeNumber, mobiles, msgContent, tpUdhi, reserve);

	}

	/**
	 * TODO SMGP 下行状态报文
	 * 
	 * @param report
	 * @return
	 */
	private void mtDeliver(SMGPDeliverMessage report) {
		if (report == null) {
			return;
		}

		try {
			logger.info("SMGP状态报告数据: {}", report);

			// 发送时手机号码拼接86，回执需去掉86前缀
			String mobile = report.getSrcTermID();
			if (mobile != null && mobile.startsWith("86")) {
				mobile = mobile.substring(2);
			}

			List<MtMessageDeliver> list = new ArrayList<>();

			MtMessageDeliver response = new MtMessageDeliver();
			response.setMsgId(report.bytesToHexString(report.getReplyMsgId()));
			response.setMobile(mobile);
			response.setCmcp(CMCP.local(response.getMobile()).getCode());
			response.setStatus((StrUtil.isNotEmpty(report.getErrorCode())
					&& SmgpConstant.SMGP_MT_STATUS_SUCCESS_CODE.equalsIgnoreCase(report.getErrorCode())
							? DeliverStatus.SUCCESS.getValue()+""
							: DeliverStatus.FAILED.getValue()+""));

			// edit by zhengying 20171208 电信成功码转义
			response.setStatusCode(DeliverStatus.SUCCESS.getValue()+"" == response.getStatus()
					? SmgpConstant.COMMON_MT_STATUS_SUCCESS_CODE
					: String.format("%s:%s", report.getStat(), report.getErrorCode()));

			response.setDeliverTime(DateUtil.now());
			response.setCreateDate(new Date());
			response.setRemarks(
					String.format("DestnationId:%s,RecvTime:%s", report.getDestTermID(), report.getRecvTime()));

			list.add(response);

			if (CollUtil.isNotEmpty(list)) {
				// 发送异步消息
				rabbitTemplate.convertAndSend(RabbitConstant.MQ_SMS_MT_WAIT_RECEIPT, list);
			}

			// 解析返回结果并返回
		} catch (Exception e) {
			logger.error("SMGP状态回执解析失败：{}", JSON.toJSONString(report), e);
			throw new RuntimeException("SMGP状态回执解析失败");
		}
	}

	/**
	 * TODO SMGP上行报文回执
	 * 
	 * @param report
	 */
	public void moReceive(SMGPDeliverMessage report) {
		if (report == null) {
			return;
		}

		try {

			logger.info("SMGP上行报告数据: {}", report);

			List<MoMessageReceive> list = new ArrayList<>();

			// 发送时手机号码拼接86，回执需去掉86前缀
			String mobile = report.getSrcTermID();
			if (mobile != null && mobile.startsWith("86")) {
				mobile = mobile.substring(2);
			}

			MoMessageReceive response = new MoMessageReceive();
			response.setPassageId(null);
			response.setMsgId(report.bytesToHexString(report.getMsgId()));
			response.setMobile(mobile);
			response.setDestnationNo(report.getDestTermID());
			response.setReceiveTime(DateUtil.now());
			response.setCreateDate(new Date());
			// 编号方式
			if (SgipConstant.MSG_FMT_UCS2 == report.getMsgFormat()) {
				response.setContent(new String(report.getMsgContent(), "UTF-16"));
			} else {
				response.setContent(new String(report.getMsgContent(), "GBK"));
			}

			list.add(response);

			if (CollUtil.isNotEmpty(list)) {
				rabbitTemplate.convertAndSend(RabbitConstant.MQ_SMS_MO_RECEIVE, list);
			}

		} catch (Exception e) {
			logger.error("SMGP上行解析失败 {}", JSON.toJSONString(report), e);
			throw new RuntimeException("SMGP上行解析失败");
		}
	}
}
