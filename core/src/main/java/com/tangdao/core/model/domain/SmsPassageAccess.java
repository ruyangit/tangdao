package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 通道资产Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_passage_access")
public class SmsPassageAccess extends DataEntity<SmsPassageAccess> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userId; // 编码
	private String groupId; // 通道组id
	private int routeType; // 路由类型
	private int cmcp; // 运营商
	private String areaCode; // 省份
	private String passageId; // 通道ID
	private String passageCode; // 通道代码
	private String protocol; // 协议类型
	private int callType; // 1-发送2-下行推送 3-下行自取 4-上行推送 5-上行自取
	private String url; // url
	private String paramsDefinition; // 定义，直接取模板里的值
	private String params; // 具体的参数值，取模板中的key作为KEY，如{＂username＂:＂test＂, ＂password＂:＂123456＂}
	private String resultFormat; // result_format
	private String successCode; // 成功码
	private String position; // 返回值的具体位置，json存储
	private int mobileSize; // 手机号码分包数
	private int packetsSize; // 1秒钟允许提交的网络包数量
	private int connectionSize; // 最大连接数
	private int readTimeout; // 超时时间（毫秒）
	private String accessCode; // 接入号码（10690...）
	private int extNumber; // 拓展号长度,0表示不允许拓展
	private int signMode; // 签名模式 0:不处理，1：自动前置，2：自动后置，3：自动去除签名
	private int smsTemplateParam; // 是否需要短信模板参数信息
	
	private String status;
	private String remarks;

	@TableField(exist = false)
	private String areaName;
	@TableField(exist = false)
	private String cmcpName;
	@TableField(exist = false)
	private String routeTypeText;

	public SmsPassageAccess() {
		super();
	}
}