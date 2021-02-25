package com.tangdao.exchanger.resolver.sms.http.zhangs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.tangdao.common.constant.CommonContext.CMCP;
import org.tangdao.common.lang.DateUtils;
import org.tangdao.modules.exchanger.model.response.ProviderSendResponse;
import org.tangdao.modules.exchanger.resolver.HttpClientManager;
import org.tangdao.modules.exchanger.resolver.handler.RequestHandler;
import org.tangdao.modules.exchanger.resolver.sms.http.AbstractPassageResolver;
import org.tangdao.modules.exchanger.template.vo.TParameter;
import org.tangdao.modules.sms.model.domain.SmsMoMessageReceive;
import org.tangdao.modules.sms.model.domain.SmsMtMessageDeliver;
import org.tangdao.modules.sms.model.domain.SmsPassageParameter;
import org.tangdao.modules.sys.constant.PassageContext.DeliverStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
  * TODO 华时融合平台（目前主要是点集/华时公用）
  *
  * @version V1.0.0   
  * @date 2017年7月11日 下午10:17:54
 */
@Component
public class ZhangsPassageResolver extends AbstractPassageResolver{
	
	@Override
	public List<ProviderSendResponse> send(SmsPassageParameter parameter,String mobile, String content,
			String extNumber) {
		
		try {
			TParameter tparameter = RequestHandler.parse(parameter.getParams());
			
			// 转换参数，并调用网关接口，接收返回结果
			String result = HttpClientManager.post(parameter.getUrl(), sendRequest(tparameter, mobile, content, extNumber));
			
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
	   * @param tparameter
	   * @param mobile
	   * @param content
	   * 		短信内容
	   * @param extNumber
	   * 		扩展号
	   * @return
	 */
	private static Map<String, Object> sendRequest(TParameter tparameter, String mobile, String content, String extNumber) {
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
	private static List<ProviderSendResponse> sendResponse(String result, String successCode) {
		if (StringUtils.isEmpty(result)) {
            return null;
        }
		
		successCode = StringUtils.isEmpty(successCode) ? COMMON_MT_STATUS_SUCCESS_CODE : successCode;
		
		JSONObject jsonObject = JSON.parseObject(result);
		if(jsonObject == null) {
            return null;
        }
		
		List<ProviderSendResponse> list = new ArrayList<>();
		ProviderSendResponse response = new ProviderSendResponse();
		
		response.setStatusCode(jsonObject.getString("code"));
		response.setSid(jsonObject.getString("sid"));
		response.setSuccess(StringUtils.isNotEmpty(response.getStatusCode()) && successCode.equals(response.getStatusCode()));
		response.setRemark(result);
		
		list.add(response);
		return list;
	}

	@Override
	public List<SmsMtMessageDeliver> mtDeliver(String report, String successCode) {
		try {
			logger.info("下行状态报告简码：{} =========={}", code(), report);
			
			JSONArray array = JSON.parseArray(report);
			if(CollectionUtils.isEmpty(array)) {
                return null;
            }
			
			List<SmsMtMessageDeliver> list = new ArrayList<>();
			SmsMtMessageDeliver response;
			for(Object object : array) {
				if(object == null) {
                    continue;
                }
				
				JSONObject jsonobj = (JSONObject)object;
				response = new SmsMtMessageDeliver();
				response.setMsgId(jsonobj.getString("sid"));
				response.setMobile(jsonobj.getString("mobile"));
				response.setCmcp(CMCP.local(jsonobj.getString("mobile")).getCode());
				response.setStatusCode(jsonobj.getString("status"));
				response.setStatus((StringUtils.isNotEmpty(response.getStatusCode()) 
						&& response.getStatusCode().equalsIgnoreCase(successCode)
						? DeliverStatus.SUCCESS.getValue() : DeliverStatus.FAILED.getValue()));
				response.setDeliverTime(DateUtils.getDateTime());
				response.setCreateTime(new Date());
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
			response.setReceiveTime(DateUtils.getDateTime());
			response.setCreateTime(new Date());
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
		return "zhangs";
	}
}
