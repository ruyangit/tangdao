package com.tangdao.exchanger.template.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tangdao.core.context.CommonContext.CMCP;
import com.tangdao.core.context.ParameterContext;
import com.tangdao.core.context.PassageContext.DeliverStatus;
import com.tangdao.core.model.domain.sms.MtMessageDeliver;
import com.tangdao.core.model.domain.sms.PassageAccess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO 通道状态报告回执公共解析
 * </p>
 *
 * @author ruyang
 * @since 2021年3月3日
 */
public class DeliverTemplateHandler {

	private static Logger logger = LoggerFactory.getLogger(DeliverTemplateHandler.class);

	public static List<MtMessageDeliver> translate(PassageAccess access, JSONObject report) {

		List<MtMessageDeliver> list = new ArrayList<>();
		try {
//			JSONObject paramDefination = JSON.parseObject(access.getParams());
//			if (!paramDefination.keySet().contains(TPosition.MSGID_NODE_NAME)
//					|| !paramDefination.keySet().contains(TPosition.MOBILE_NODE_NAME)
//					|| !paramDefination.keySet().contains(TPosition.STATUS_CODE_NODE_NAME)) {
//
//				logger.error("数据节点配置有误 : {}", paramDefination.toJSONString());
//				throw new RuntimeException("数据节点配置异常");
//			}

			String rport = report.getString(ParameterContext.PARAMETER_NAME_IN_STREAM);
			if (StrUtil.isEmpty(rport)) {
				return null;
			}
			List<JSONObject> array = JSON.parseObject(rport, new TypeReference<List<JSONObject>>() {
			});
			if (CollUtil.isEmpty(array)) {
				return null;
			}

			MtMessageDeliver response;
			for (JSONObject prameterReport : array) {

				// 消息ID
				String msgId = prameterReport.getString("Msg_Id");
				// 手机号码
				String mobile = prameterReport.getString("Mobile");
				// 状态码
				String statusCode = prameterReport.getString("Status");

				response = new MtMessageDeliver();

				response.setMsgId(msgId);
				response.setMobile(mobile);
				response.setCmcp(CMCP.local(mobile).getCode());
				response.setStatusCode(statusCode);
				response.setStatus(
						(StrUtil.isNotEmpty(statusCode) && statusCode.equalsIgnoreCase(access.getSuccessCode())
								? DeliverStatus.SUCCESS.getValue() + ""
								: DeliverStatus.FAILED.getValue() + ""));
				response.setDeliverTime(DateUtil.now());
				response.setCreateDate(new Date());
				response.setRemarks(prameterReport.toJSONString());

				list.add(response);
			}

			return list;
		} catch (Exception e) {
			logger.error("解析回执数据失败", e);
			throw new RuntimeException("解析回执数据失败");
		}
	}

}
