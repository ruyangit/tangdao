package com.tangdao.exchanger.resolver.sms.sgip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
import com.huawei.insa2.comm.sgip.message.SGIPSubmitMessage;
import com.huawei.insa2.comm.sgip.message.SGIPSubmitRepMessage;
import com.tangdao.core.constant.RabbitConstant;
import com.tangdao.core.context.CommonContext.CMCP;
import com.tangdao.core.context.PassageContext.DeliverStatus;
import com.tangdao.core.context.TaskContext.MessageSubmitStatus;
import com.tangdao.core.model.domain.sms.MoMessageReceive;
import com.tangdao.core.model.domain.sms.MtMessageDeliver;
import com.tangdao.core.model.domain.sms.PassageParameter;
import com.tangdao.exchanger.resolver.sms.AbstractSmsProxySender;
import com.tangdao.exchanger.resolver.sms.cmpp.constant.CmppConstant;
import com.tangdao.exchanger.resolver.sms.sgip.constant.SgipConstant;
import com.tangdao.exchanger.template.handler.RequestTemplateHandler;
import com.tangdao.exchanger.template.response.ProviderSendResponse;
import com.tangdao.exchanger.template.vo.TParameter;
import com.tangdao.exchanger.utils.MobileNumberCatagoryUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

@Service
public class SgipProxySender extends AbstractSmsProxySender {

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
            int yushu = messageUCS2Len % (SgipConstant.MAX_MESSAGE_UCS2_LENGTH - 6);
            int add = 0;
            if (yushu > 0) {
                add = 1;
            }
            int messageUCS2Count = messageUCS2Len / (SgipConstant.MAX_MESSAGE_UCS2_LENGTH - 6) + add;// 长短信分为多少条发送

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
                    msgContent = byteAdd(tp_udhiHead, messageUCS2, i * (SgipConstant.MAX_MESSAGE_UCS2_LENGTH - 6),
                                         (i + 1) * (SgipConstant.MAX_MESSAGE_UCS2_LENGTH - 6));
                    list.add(msgContent);
                } else {
                    msgContent = byteAdd(tp_udhiHead, messageUCS2, i * (SgipConstant.MAX_MESSAGE_UCS2_LENGTH - 6),
                                         messageUCS2Len);
                    list.add(msgContent);
                }
                // logger.debug("msgContent  "+new
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
     * @param mobile 手机号码（支持多个）
     * @param content 短信内容
     * @return
     */
    public List<ProviderSendResponse> send(PassageParameter parameter, String extNumber, String mobile,
                                           String content) {
        try {
            TParameter tparameter = RequestTemplateHandler.parse(parameter.getParams());
            if (CollUtil.isEmpty(tparameter)) {
                throw new RuntimeException("SGIP 参数信息为空");
            }

            SgipManageProxy sgipManageProxy = (SgipManageProxy)getSmManageProxy(parameter);
            if (sgipManageProxy == null) {
                logger.error("SGIP代理获取失败，手机号码：{}， 短信内容：{}，扩展号码：{}", mobile, content, extNumber);
                return null;
            }

            // 接入号码（如果扩展号码不为空，则加入扩展号码）
            String srcTerminalId = tparameter.getString("src_terminal_id")
                                   + (StringUtils.isEmpty(extNumber) ? "" : extNumber);

            // 获取发送回执信息
            SGIPSubmitRepMessage submitRepMsg = getSGIPSubmitResponseMessage(tparameter.getString("service_code"),
                                                                             tparameter.getString("spid"),
                                                                             StringUtils.isEmpty(tparameter.getString("msg_fmt")) ? CmppConstant.MSG_FMT_GBK : Integer.parseInt(tparameter.getString("msg_fmt")),
                                                                             StringUtils.isEmpty(tparameter.getString("mobile")) ? "000" : tparameter.getString("mobile"),
                                                                             srcTerminalId, mobile, content, "",
                                                                             sgipManageProxy);

            if (submitRepMsg == null) {
                logger.error("SGIPSubmitRepMessage 网关提交信息为空");
                smsProxyService.plusSendErrorTimes(parameter.getPassageId());
                return null;
            }

            // 发送完短信设置上次发送时间
            SgipConstant.SGIP_RECONNECT_TIMEMILLS.put(parameter.getPassageId(), System.currentTimeMillis());

            List<ProviderSendResponse> list = new ArrayList<>();
            ProviderSendResponse response = new ProviderSendResponse();
            if (submitRepMsg.getResult() == MessageSubmitStatus.SUCCESS.getCode()) {
                // 发送成功清空
            	smsProxyService.clearSendErrorTimes(parameter.getPassageId());

                response.setMobile(mobile);
                response.setStatusCode(submitRepMsg.getResult() + "");
                response.setSid(submitRepMsg.getSubmitSequenceNumber());
                response.setSuccess(true);
                response.setRemarks(String.format("{msgId:%s, sequenceId:%d, commandId:%d}", response.getSid(),
                                                 submitRepMsg.getSequenceId(), submitRepMsg.getCommandId()));

                list.add(response);

            } else {
                response.setMobile(mobile);
                response.setStatusCode(submitRepMsg.getResult() + "");
                response.setSid("" + submitRepMsg.getSubmitSequenceNumber());
                response.setSuccess(false);
                response.setRemarks(String.format("{result:%d, sequenceId:%d, commandId:%d}", submitRepMsg.getResult(),
                                                 submitRepMsg.getSequenceId(), submitRepMsg.getCommandId()));

                list.add(response);
            }

            return list;
        } catch (Exception e) {
            // 累加发送错误次数
        	smsProxyService.plusSendErrorTimes(parameter.getPassageId());

            logger.error("SGIP发送失败", e);
            throw new RuntimeException("SGIP发送失败");
        }
    }

    // 手机号码前缀
    private static final String MOBILE_PREFIX_NUMBER = "86";

    /**
     * TODO 组装待提交短信CMPP结构体数据
     * 
     * @param serviceType 业务代码
     * @param spid 企业代码
     * @param chargeNumber 计费号码
     * @param msgFmt 消息编码方式（默认GBK）
     * @param srcTerminalId 接入号（包含用于配置的扩展号码）
     * @param mobile 接收短信手机号码
     * @param content 短信内容
     * @param reserve 扩展项，用于自定义内容
     * @return
     * @throws IOException
     */
    private SGIPSubmitRepMessage getSGIPSubmitResponseMessage(String serviceType, String spid, int msgFmt,
                                                              String chargeNumber, String srcTerminalId, String mobile,
                                                              String content, String reserve,
                                                              SgipManageProxy sgipManageProxy) throws IOException {

        // 存活有效期（6小时）
        Date validTime = new Date(System.currentTimeMillis() + (long) 21600000);
        // 定时发送时间
        Date atTime = null;

        int tpUdhi = SgipConstant.TP_UDHI;
        // 全都改成以 UTF 16 格式提交
        List<byte[]> contentList = getLongByte(content);
        if (contentList.size() > 1) {
            // 长短信格式发送 增加协议头 改变编码方式
            tpUdhi = 1;
            msgFmt = SgipConstant.MSG_FMT_UCS2;
        }

        String[] mobiles = mobile.split(MobileNumberCatagoryUtil.DATA_SPLIT_CHARCATOR);
        for (int i = 0; i < mobiles.length; i++) {
            mobiles[i] = MOBILE_PREFIX_NUMBER + mobiles[i];
        }

        SGIPSubmitMessage submitMsg = null;
        SGIPSubmitRepMessage submitRepMsg = null;
        for (int index = 1; index <= contentList.size(); index++) {
            submitMsg = getSGIPSubmitMessage(serviceType, tpUdhi, msgFmt, spid, validTime, atTime, chargeNumber,
                                             srcTerminalId, mobiles, contentList.get(index - 1), reserve);

            SGIPSubmitRepMessage repMsg = (SGIPSubmitRepMessage) sgipManageProxy.send(submitMsg);
            if (index == 1) {
                // 以长短信 拆分发送时，目前状态报告的 msgid 是 拆分后第一条的 msgid
                submitRepMsg = repMsg;
            } else if (submitRepMsg == null) {
                submitRepMsg = repMsg;
            }

            if (submitRepMsg == null) {
                logger.error(" SgipSubmitRepMessage null, submitMsg : {}", submitMsg);
            } else if (submitRepMsg.getResult() != 0) {
                logger.error(" submitRepMsg result :{}, index : {}, content: {}, mobile : {}",
                             submitRepMsg.getResult(), index, content, mobile);
            }
        }

        return submitRepMsg;
    }

    /**
     * TODO 组装CMPP提交信息
     * 
     * @param serviceType 业务代码
     * @param tpUdhi GSM协议类型
     * @param msgFmt 消息格式
     * @param spid
     * @param validTime
     * @param atTime
     * @param srcTerminalId 源终端MSISDN号码（包含扩展号码）
     * @param mobiles 手机号码
     * @param msgContent 短信内容
     * @param reserve 保留
     * @return
     */
    private SGIPSubmitMessage getSGIPSubmitMessage(String serviceType, int tpUdhi, int msgFmt, String spid,
                                                   Date validTime, Date atTime, String chargeNumber,
                                                   String srcTerminalId, String[] mobiles, byte[] msgContent,
                                                   String reserve) {

        return new SGIPSubmitMessage(srcTerminalId, chargeNumber, // 付费号码 string
                                     mobiles, // 接收该短消息的手机号，最多100个号码 string[]
                                     spid, // 企业代码，取值范围为0～99999 string
                                     serviceType, // 业务代码，由SP定义 stirng
                                     SgipConstant.FEE_TYPE, // 计费类型 int
                                     SgipConstant.FEE_VALUE, // 该条短消息的收费值 stirng
                                     SgipConstant.FEE_VALUE, // 赠送用户的话费 string
                                     SgipConstant.FEE_CODE, // 代收费标志0：应收1：实收 int
                                     0, // 引起MT消息的原因 int
                                     0, // 优先级0～9从低 到高，默认为0 int
                                     validTime, // 短消息寿命的终止时间 date
                                     atTime, // 短消息定时发送的时间 date
                                     1, // 状态报告标记 int
                                     SgipConstant.TP_PID, // GSM协议类型 int
                                     tpUdhi, // GSM协议类型 int
                                     msgFmt, // 短消息的编码格式 int
                                     0, // 信息类型 int
                                     msgContent.length, // 短消息内容长度 int
                                     msgContent, // 短消息的内容 btye[]
                                     reserve // 保留，扩展用 string
        );

    }

    /**
     * TODO SGIP 下行状态报文
     * 
     * @param report
     * @return
     */
    public void mtDeliver(SGIPReportMessage report) {
        if (report == null) {
            return;
        }

        try {
            logger.info("SGIP状态报告数据: {}", report);

            String msgid = "" + report.getSubmitSequenceNumber();
            int state = report.getState();
            if (state == 1) {
                logger.warn("下行状态回执失败，msgId -->" + msgid + " number" + report.getUserNumber() + " state -->"
                            + report.getErrorCode());
                // 等待发送状态直接返回不保存
                return;
            }

            // 发送时手机号码拼接86，回执需去掉86前缀
            String mobile = report.getUserNumber();
            if (mobile != null && mobile.startsWith("86")) {
                mobile = mobile.substring(2);
            }

            List<MtMessageDeliver> list = new ArrayList<>();

            MtMessageDeliver response = new MtMessageDeliver();
            response.setMsgId(msgid);
            response.setMobile(mobile);
            response.setCmcp(CMCP.local(response.getMobile()).getCode());
            response.setStatusCode(state == 0 ? SgipConstant.COMMON_MT_STATUS_SUCCESS_CODE : report.getErrorCode() + "");
            response.setStatus(state == 0 ? DeliverStatus.SUCCESS.getValue()+"" : DeliverStatus.FAILED.getValue()+"");
            response.setDeliverTime(DateUtil.now());
            response.setCreateDate(new Date());
            response.setRemarks(String.format("msg_id:%s,code:%d", report.getSubmitSequenceNumber() + "",
                                             report.getErrorCode()));

            list.add(response);

            if (CollUtil.isNotEmpty(list)) {
                // 发送异步消息
                rabbitTemplate.convertAndSend(RabbitConstant.MQ_SMS_MT_WAIT_RECEIPT, list);
            }

            // 解析返回结果并返回
        } catch (Exception e) {
            logger.error("SGIP状态回执解析失败：{}", JSON.toJSONString(report), e);
            throw new RuntimeException("SGIP状态回执解析失败");
        }
    }

    /**
     * TODO SGIP上行报文回执
     * 
     * @param report
     */
    public void moReceive(SGIPDeliverMessage report) {
        if (report == null) {
            return;
        }

        SGIPDeliverMessage deliverMsg = (SGIPDeliverMessage) report;
        try {

            logger.info("SGIP上行报告数据: {}", report);

            List<MoMessageReceive> list = new ArrayList<>();

            // 发送时手机号码拼接86，回执需去掉86前缀
            String mobile = report.getUserNumber();
            if (mobile != null && mobile.startsWith("86")) {
                mobile = mobile.substring(2);
            }

            MoMessageReceive response = new MoMessageReceive();
            response.setPassageId(null);
            response.setMsgId(deliverMsg.getSPNumber());
            response.setMobile(mobile);
            response.setDestnationNo(deliverMsg.getSPNumber());
            response.setReceiveTime(DateUtil.now());
            response.setCreateDate(new Date());
            // 编号方式
            if (SgipConstant.MSG_FMT_UCS2 == report.getMsgFmt()) {
                response.setContent(new String(report.getMsgContent(), "UTF-16"));
            } else {
                response.setContent(new String(report.getMsgContent(), "GBK"));
            }

            list.add(response);

            if (CollUtil.isNotEmpty(list)) {
                rabbitTemplate.convertAndSend(RabbitConstant.MQ_SMS_MO_RECEIVE, list);
            }

        } catch (Exception e) {
            logger.error("SGIP上行解析失败 {}", JSON.toJSONString(report), e);
            throw new RuntimeException("SGIP上行解析失败");
        }
    }

}
