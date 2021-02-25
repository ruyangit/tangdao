package com.tangdao.exchanger.resolver.sms.webservice.custom;


/**
 * 
  * TODO 港辅状态报告/上行报告扫描服务
  * 
  * @version V1.0   
  * @date 2016年7月19日 上午11:17:13
 */
public class GangfuWebserviceResolver {
	
//	@Value("${gangfu.userid}")
//	private String userid;
//	@Value("${gangfu.password}")
//	private String password;
//	
//	@Override
//	@Scheduled(fixedRate = 3000)
//	public void statusReport() {
//		try {
//			String response = super.post(Constants.GANGFU_URL, GangfuSoap.soapXmlContent(parameter(), "getStatus"),
//					MediaType.parse("text/xml; charset=utf-8"));
//			
//			// SUCCESS 为接口调用状态码， id 为子节点存在数据，不存在id 则不做解析
//			if (StringUtils.isEmpty(response) || !response.contains("SUCCESS") || !response.contains("id")) {
//				LogUtils.debug("港辅状态回执信息：{} ，不做推送处理: {}", response);
//				return;
//			}
//			
//			LogUtils.info("港辅状态信息：{}", response);
//			this.push(Constants.HUASHI_FROM_GANGFU_STATUS_REPORT_URL, GangfuSoap.analysisResult(response));
//		} catch (Exception e) {
//			LogUtils.error("港辅状态报告处理失败，失败信息为：{}", e.getMessage());
//		}
//	}
//	
//	private Map<String, String> parameter(){
//		Map<String, String> parameter = new HashMap<String, String>();
//		parameter.put("uid", userid);
//		parameter.put("pwd", password);
//		return parameter;
//	}

//	@Override
//	@Scheduled(fixedRate = 10000)
//	public void upstreamReport() {
//		try {
//			String response = super.post(Constants.GANGFU_URL, GangfuSoap.soapXmlContent(parameter(), "getMessage"),
//					MediaType.parse("text/xml; charset=utf-8"));
//			
//			// SUCCESS 为接口调用状态码， srcPhone 为子节点存在数据，不存在srcPhone 则不做解析
//			if (StringUtils.isEmpty(response) || !response.contains("SUCCESS") || !response.contains("srcPhone")) {
//				LogUtils.debug("港辅上行回执信息：{} ，不做推送处理", response);
//				return;
//			}
//			
//			LogUtils.info("港辅上行信息：{}", response);
//			this.push(Constants.HUASHI_FROM_GANGFU_UPSTREAM_REPORT_URL, GangfuSoap.analysisResult(response));
//		} catch (Exception e) {
//			LogUtils.error("港辅上行报告处理失败，失败信息为：{}", e.getMessage());
//		}
//
//	}
//
//	@Override
//	protected void push(String url, String content) {
//		try {
//			super.post(url, content);
//			LogUtils.info("推送URL：{}，已完成推送。", url);
//		} catch (Exception e) {
//			LogUtils.error("推送URL：{}，失败信息为：{}", url, e.getMessage());
//		}
//	}
	
	

}
