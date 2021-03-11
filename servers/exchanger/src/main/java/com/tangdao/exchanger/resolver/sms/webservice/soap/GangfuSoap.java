package com.tangdao.exchanger.resolver.sms.webservice.soap;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class GangfuSoap {

	/**
	 * 
	 * TODO 构建Webservice 报文信息
	 * 
	 * @param prams      参数
	 * @param callMethod 调用方法
	 * @return
	 */
	public static String soapXmlContent(Map<String, String> prams, String callMethod) {
		StringBuffer soapRequestData = new StringBuffer();
		soapRequestData.append(
				"<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.hydxqxt.dp.iac\">")
				.append("<soapenv:Header/>").append("<soapenv:Body>").append(String.format(
						"<ser:%s soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">", callMethod));

		Set<String> nameSet = prams.keySet();
		for (String name : nameSet) {
			if (StringUtils.isEmpty(name)) {
				continue;
			}

			soapRequestData.append(String.format("<%s xsi:type=\"xsd:string\">%s</%s>", name, prams.get(name), name));
		}
		soapRequestData.append(String.format("</ser:%s>", callMethod));
		soapRequestData.append("</soapenv:Body>");
		soapRequestData.append("</soapenv:Envelope>");

		return soapRequestData.toString();

	}

	/**
	 * 
	 * TODO 解析调用接口信息(去除SOAP一些描述信息，获取最终的节点信息, 如SUCCESS, FAIL等信息，状态码)
	 * 
	 * @param xmlResponse
	 * @return
	 */
	public static String analysisResult(String xmlResponse) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));

			Element root = doc.getDocumentElement();
//			NodeList envelope = root.getChildNodes();

			Element e = (Element) root.getLastChild();
			if (e == null) {
				return "解析数据异常";
			}

			if (StringUtils.isEmpty(e.getTextContent())) {
				return "解析数据为空";
			}

			// 回执信息包含SUCCESS:XXXX,需要去除前面的SUCCESS:
			return e.getTextContent().substring(e.getTextContent().indexOf(":") + 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "解析数据为空";
	}

	public static void main(String[] args) {
		String a = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:getStatusResponse soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://service.hydxqxt.dp.iac\"><getStatusReturn xsi:type=\"xsd:string\">SUCCESS:&lt;statusPacks&gt;&lt;statusPack&gt;&lt;id&gt;114690112183060909&lt;/id&gt;&lt;destaddr&gt;13385819856&lt;/destaddr&gt;&lt;status&gt;DELIVRD&lt;/status&gt;&lt;/statusPack&gt;&lt;/statusPacks&gt;</getStatusReturn></ns1:getStatusResponse></soapenv:Body></soapenv:Envelope>";
		a = GangfuSoap.analysisResult(a);
		System.out.println(a);
		System.out.println(a.substring(a.indexOf(":") + 1));
	}
}
