package com.tangdao.exchanger.resolver.sms.http.huashi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tangdao.core.context.CommonContext.CMCP;
import com.tangdao.core.context.PassageContext.DeliverStatus;
import com.tangdao.core.model.domain.SmsMoMessageReceive;
import com.tangdao.core.model.domain.SmsMtMessageDeliver;
import com.tangdao.core.model.domain.SmsPassageParameter;
import com.tangdao.core.model.vo.ProviderSendVo;
import com.tangdao.exchanger.resolver.HttpClientManager;
import com.tangdao.exchanger.resolver.handler.RequestHandler;
import com.tangdao.exchanger.resolver.sms.http.AbstractPassageResolver;
import com.tangdao.exchanger.template.vo.TParameter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO 华时融合平台（目前主要是点集/华时公用）
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
@Component
public class HspaasPassageResolver extends AbstractPassageResolver {

	@Override
	public List<ProviderSendVo> send(SmsPassageParameter parameter, String mobile, String content,
			String extNumber) {

		try {
			TParameter tparameter = RequestHandler.parse(parameter.getParams());

			// 转换参数，并调用网关接口，接收返回结果
			String result = HttpClientManager.post(parameter.getUrl(),
					sendRequest(tparameter, mobile, content, extNumber));

			// 解析返回结果并返回
			return sendResponse(result, parameter.getSuccessCode());
		} catch (Exception e) {
			logger.error("华时/点集融合发送解析失败", e);
			throw new RuntimeException("解析失败");
		}
	}

	/**
	 * 
	 * TODO 发送短信组装请求信息
	 * 
	 * @param tparameter
	 * @param mobile
	 * @param content    短信内容
	 * @param extNumber  扩展号
	 * @return
	 */
	private static Map<String, Object> sendRequest(TParameter tparameter, String mobile, String content,
			String extNumber) {
		String timestamps = System.currentTimeMillis() + "";

		Map<String, Object> params = new HashMap<>();
		params.put("appkey", tparameter.getString("appkey"));
		params.put("appsecret", DigestUtils.md5Hex(tparameter.getString("appsecret") + mobile + timestamps));
		params.put("mobile", mobile);
		params.put("content", content);
		params.put("timestamp", timestamps);
		params.put("extNumber", extNumber == null ? "" : extNumber);
		params.put("attach", tparameter.getString("attach"));

		// 暂时不设置
//		params.put("callback", tparameter.getString("callback"));

		return params;
	}

	/**
	 * 
	 * TODO 解析发送返回值
	 * 
	 * @param result
	 * @param successCode
	 * @return
	 */
	private static List<ProviderSendVo> sendResponse(String result, String successCode) {
		if (StrUtil.isEmpty(result)) {
			return null;
		}

		successCode = StrUtil.isEmpty(successCode) ? COMMON_MT_STATUS_SUCCESS_CODE : successCode;

		JSONObject jsonObject = JSON.parseObject(result);
		if (jsonObject == null) {
			return null;
		}

		List<ProviderSendVo> list = new ArrayList<>();
		ProviderSendVo response = new ProviderSendVo();

		response.setStatusCode(jsonObject.getString("code"));
		response.setSid(jsonObject.getString("sid"));
		response.setSuccess(
				StrUtil.isNotEmpty(response.getStatusCode()) && successCode.equals(response.getStatusCode()));
		response.setRemarks(result);

		list.add(response);
		return list;
	}

	@Override
	public List<SmsMtMessageDeliver> mtDeliver(String report, String successCode) {
		try {
			logger.info("下行状态报告简码：{} =========={}", code(), report);

			JSONArray array = JSON.parseArray(report);
			if (CollUtil.isEmpty(array)) {
				return null;
			}

			List<SmsMtMessageDeliver> list = new ArrayList<>();
			SmsMtMessageDeliver response;
			for (Object object : array) {
				if (object == null) {
					continue;
				}

				JSONObject jsonobj = (JSONObject) object;
				response = new SmsMtMessageDeliver();
				response.setMsgId(jsonobj.getString("sid"));
				response.setMobile(jsonobj.getString("mobile"));
				response.setCmcp(CMCP.local(jsonobj.getString("mobile")).getCode());
				response.setStatusCode(jsonobj.getString("status"));
				response.setStatus((StrUtil.isNotEmpty(response.getStatusCode())
						&& response.getStatusCode().equalsIgnoreCase(successCode)
								? DeliverStatus.SUCCESS.getValue() + ""
								: DeliverStatus.FAILED.getValue() + ""));
				response.setDeliverTime(DateUtil.now());
				response.setCreateDate(new Date());
				response.setRemarks(jsonobj.toJSONString());

				list.add(response);
			}

			// 解析返回结果并返回
			return list;
		} catch (Exception e) {
			logger.error("华时/点集融合状态报告解析失败", e);
			throw new RuntimeException("解析失败");
		}
	}

	@Override
	public List<SmsMoMessageReceive> moReceive(String report, String passageId) {
		try {

			logger.info("上行报告简码：{} =========={}", code(), report);

			JSONObject jsonobj = JSONObject.parseObject(report);
			String msgId = jsonobj.getString("sid");
			String destId = jsonobj.getString("destnationNo");
			String mobile = jsonobj.getString("mobile");
			String content = jsonobj.getString("content");

			List<SmsMoMessageReceive> list = new ArrayList<>();

			SmsMoMessageReceive response = new SmsMoMessageReceive();
			response.setPassageId(passageId);
			response.setMsgId(msgId);
			response.setMobile(mobile);
			response.setContent(content);
			response.setDestnationNo(destId);
			response.setReceiveTime(DateUtil.now());
			response.setCreateDate(new Date());
			list.add(response);

			// 解析返回结果并返回
			return list;
		} catch (Exception e) {
			logger.error("华时/点集融合上行解析失败", e);
			throw new RuntimeException("解析失败");
		}
	}

	@Override
	public Double balance(TParameter tparameter, String url, String passageId) {
		return 0d;
	}

	@Override
	public String code() {
		return "hspaas";
	}
}
