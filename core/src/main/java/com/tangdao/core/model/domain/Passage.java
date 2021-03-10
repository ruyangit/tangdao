package com.tangdao.core.model.domain;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月3日
 */
@Getter
@Setter
@TableName("sms_passage")
public class Passage extends DataEntity<Passage> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String name; // 通道名称
	private String code; // 通道字母编码
	private int cmcp; // 运营商
	private int wordNumber; // 计费字数
	private String priority; // 优先级
	private String paasTemplateId; // 通道模板
	private int type; // 通道类型 0 公共通道 1 独立通道
	private String exclusiveUserId; // 独立通道时绑定的用户
	private int signMode; // 签名模式 0:不处理，1：自动前置，2：自动后置，3：自动去除签名
	private String accessCode; // 10690接入号，需与params表接入号一致
	private String account; // 接入账号(上家提供的用户账号)
	private int payType; // 付费方式(1预付2后付)
	private String balance; // 剩余条数
	private int mobileSize; // 手机号码分包数
	private int packetsSize; // 1秒钟允许提交的网络包数量
	private int connectionSize; // 最大连接数
	private int readTimeout; // 超时时间（毫秒）
	private int extNumber; // 拓展号长度,0表示不允许拓展
	private String bornTerm; // 统计落地时限（小时）
	private int smsTemplateParam; // 是否需要短信模板参数信息
	
	private String status;
	
	private String remarks;

	/**
	 * 参数集合信息
	 */
	@TableField(exist = false)
	private List<PassageParameter> parameterList = new ArrayList<>();

	/**
	 * 省份集合信息
	 */
	@TableField(exist = false)
	private List<PassageArea> areaList = new ArrayList<>();

	public Passage() {
		super();
	}

}