package com.tangdao.developer;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;

public class SmsTest {
	public static void main(String[] args) {
		String timestamps = System.currentTimeMillis() + "";

		String url = "http://localhost:4002/sms/send";
		String appkey = "a";
		String appsecret = "b";
		String mobile = "15821554550,13262669725,17709898766,15182939999";
		String content = "【天机科技】您的验证码为8428。会员理财，试试，就可以成为会员。";
		String extNumber = null;
		String attach = "";
		Map<String, Object> params = new HashMap<>();
		params.put("appkey", appkey);
		params.put("appsecret", SecureUtil.md5(appsecret + mobile + timestamps));
		params.put("mobile", mobile);
		params.put("content", content);
		params.put("timestamp", timestamps);
		params.put("extNumber", extNumber == null ? "" : extNumber);
		params.put("attach", attach);
		
		String ret = HttpUtil.post(url, params);
		System.out.println("ret:" + ret);
	}
}
