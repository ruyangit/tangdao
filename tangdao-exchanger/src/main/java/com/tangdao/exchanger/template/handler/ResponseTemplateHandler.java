package com.tangdao.exchanger.template.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tangdao.core.constant.ExchangerConstant;
import com.tangdao.core.exception.DataEmptyException;
import com.tangdao.core.exception.DataParseException;
import com.tangdao.exchanger.response.ProviderSendResponse;
import com.tangdao.exchanger.template.TPosition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO 调用接口响应解析器
 * </p>
 *
 * @author ruyang
 * @since 2021年3月3日
 */
public class ResponseTemplateHandler {

	/**
	 * 
	 * TODO 解析
	 * 
	 * @param result   返回结果
	 * @param format   结果模板
	 * @param position 模板值定位
	 * @return
	 */
	public static List<ProviderSendResponse> parse(String result, String format, String position, String successCode) {

		// 暂定写死
		List<ProviderSendResponse> list = new ArrayList<>();
		ProviderSendResponse response = null;
		List<Map<String, Object>> array = JSON.parseObject(result, new TypeReference<List<Map<String, Object>>>() {
		});
		if (array == null || array.size() == 0) {
			return null;
		}

		for (Map<String, Object> ojb : array) {
			response = new ProviderSendResponse();
			response.setMobile(ojb.get("Mobile").toString());
			response.setStatusCode(ojb.get("Rspcode") == null ? null : ojb.get("Rspcode").toString());
			response.setSid(ojb.get("Msg_Id").toString());
			response.setSuccess(ResponseTemplateHandler.isSuccess(response.getStatusCode(), successCode));

			list.add(response);
		}

		return list;

//		validate(result, format, position);
//
//		return parseResult(result, format, position, successCode);
	}

	/**
	 * 
	 * TODO 校验
	 * 
	 * @param result
	 * @param format
	 * @param position
	 */
	static void validate(String result, String format, String position) {
		if (StrUtil.isEmpty(result)) {
			throw new DataEmptyException("结果数据为空");
		}

		if (StrUtil.isEmpty(format)) {
			throw new DataEmptyException("格式化模板数据为空");
		}

		if (StrUtil.isEmpty(position)) {
			throw new DataEmptyException("格式化模板定位数据为空");
		}
	}

	static List<ProviderSendResponse> parseResult(String result, String format, String position, String successCode) {
		try {
			TPosition tpositon = getPosition(position);

			// 手机号码定位
			Integer mobilePosition = tpositon.getPosition(TPosition.MOBILE_NODE_NAME);
			// 状态码定位
			Integer statusCodePosition = tpositon.getPosition(TPosition.STATUS_CODE_NODE_NAME);
			// 任务ID定位
			Integer sidPosition = tpositon.getPosition(TPosition.SID_NODE_NAME);

			List<ProviderSendResponse> list = new ArrayList<>();
			ProviderSendResponse response = null;

			Pattern pattern = Pattern.compile(format);
			Matcher matcher = pattern.matcher(result);
			while (matcher.find()) {
				response = new ProviderSendResponse();
				response.setMobile(
						mobilePosition == null ? null : getRespVal(matcher.group(mobilePosition.intValue())));
				response.setStatusCode(
						statusCodePosition == null ? null : getRespVal(matcher.group(statusCodePosition.intValue())));
				response.setSid(sidPosition == null ? null : getRespVal(matcher.group(sidPosition.intValue())));
				response.setSuccess(isSuccess(response.getStatusCode(), successCode));

				list.add(response);
			}

			return list;
		} catch (Exception e) {
			throw new DataParseException(e);
		}

	}

	private static TPosition getPosition(String position) {
		try {
			TPosition tposition = JSON.parseObject(position, TPosition.class);
			if (CollUtil.isEmpty(tposition)) {
				throw new DataEmptyException(position);
			}
			return tposition;
		} catch (Exception e) {
			throw new DataParseException(e);
		}
	}

	/**
	 * 
	 * TODO 获取返回值信息
	 * 
	 * @param o
	 * @return
	 */
	private static String getRespVal(String o) {
		if (StrUtil.isEmpty(o)) {
			return null;
		}
		return o.replaceAll("\"", "");
	}

	/**
	 * 
	 * TODO 判断状态码是否成功
	 * 
	 * @param statusCode
	 * @param temlateSuccessCode 模板成功状态码
	 * @return
	 */
	public static boolean isSuccess(String statusCode, String temlateSuccessCode) {
		if (StrUtil.isEmpty(statusCode)) {
			return false;
		}

		if (StrUtil.isNotEmpty(temlateSuccessCode)) {
			return temlateSuccessCode.equalsIgnoreCase(statusCode);
		}

		// 如果没有配置模板状态码，则解析常量中所有成功状态码是否包含（后期改至REDIS）
		List<String> list = Arrays.asList(ExchangerConstant.SUCCESS_CODE_ARRAY);
		return list.contains(statusCode.trim().toLowerCase());
	}

	public static void main(String[] args) {

		String result = "[{\"Rspcode\":0,\"Msg_Id\":\"28244007242246\",\"Mobile\":\"18368031231\"}]";
		String format = "(\"Rspcode\":)(.*?)(,)(\"Msg_Id\":\")(.*?)(\",)(\"Mobile\":\")(.*?)(\")";
		String position = "{\"mobile\":\"8\",\"statusCode\":\"2\",\"sid\":\"5\"}";
		String successCode = "0";

		List<ProviderSendResponse> list = parse(result, format, position, successCode);
		for (ProviderSendResponse p : list) {
			System.out.println(p.getMobile());
			System.out.println(p.getMobile());
		}

//		String str1 = "<?xml version='1.0' encoding='utf-8' ?><returnsms><statusbox><mobile>15821917717</mobile>"
//				+ "<taskid>1212</taskid><status>10</status><receivetime>2011-12-02 22:12:11</receivetime>"
//				+ "<errorcode>MK:0011</errorcode></statusbox><statusbox><mobile>15821917717</mobile>"
//				+ "<taskid>1212</taskid><status>20</status><receivetime>2011-12-02 22:12:11</receivetime>"
//				+ "<errorcode>DELIVRD</errorcode></statusbox></returnsms>";
//
//		String regex1 = "(<mobile>)(.*?)(</mobile>)(<taskid>)(.*?)(</taskid>)(<status>)(.*?)(</status>)";
//		Pattern pattern = Pattern.compile(regex1);
//		Matcher matcher = pattern.matcher(str1);
//		while (matcher.find()) {
//			System.out.println("手机号：" + matcher.group(2));
//			System.out.println("任务ID：" + matcher.group(5));
//			System.out.println("响应状态：" + matcher.group(8));
//			System.out.println("---------------------");
//		}

////		String str2 = "{\"Rets\": [\"{\"Rspcode\":0,\"Msg_Id\":\"114445276129660989\",\"Mobile\":\"18600000000\"},{\"Rspcode\":0,\"Msg_Id\":\"114445276129660991\",\"Mobile\":\"13910101010\"}]}";
//		String str2 = "[{\"Rspcode\":0,\"Msg_Id\":\"27513552514463\",\"Mobile\":\"18368031231\"}]";
//		
////		String str2 = "[{\"Rspcode\":0,\"Msg_Id\":\"114445276129660989\",\"Mobile\":\"18600000000\"}]";
//		System.out.println(str2);
//		String regex2 = "(\"Rspcode\":)(.*?)(,)(\"Msg_Id\":\")(.*?)(\",)(\"Mobile\":\")(.*?)(\")";
//		
//		Pattern pattern2 = Pattern.compile(regex2);
//		Matcher matcher2 = pattern2.matcher(str2);
//		System.out.println(matcher2.groupCount());
//		while (matcher2.find()) {
//			System.out.println("手机号：" + matcher2.group(2));
//			System.out.println("任务ID：" + matcher2.group(5));
//			System.out.println("响应状态：" + matcher2.group(8));
//			System.out.println("---------------------");
//		}

//		String str3 = "{\"Rets\":[{\"Rspcode\":1,\"Msg_Id\":\"\",\"Mobile\":\"\",\"Fee\":0}]}";
//		System.out.println(str3);
//		String regex3 = "(\"Rspcode\":)(.*?)(,)(\"Msg_Id\":\")(.*?)(\",)(\"Mobile\":\")(.*?)(\")";
//		Pattern pattern3 = Pattern.compile(regex3);
//		Matcher matcher3 = pattern3.matcher(str3);
//		System.out.println(matcher3.groupCount());
//		while (matcher3.find()) {
//			System.out.println("手机号：" + matcher3.group(2));
//			System.out.println("任务ID：" + matcher3.group(5));
//			System.out.println("响应状态：" + matcher3.group(8));
//			System.out.println("---------------------");
//		}
	}
}
