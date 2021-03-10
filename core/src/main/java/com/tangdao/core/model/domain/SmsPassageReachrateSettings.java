package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 通道重连配置Entity
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_passage_reachrate_settings")
public class SmsPassageReachrateSettings extends DataEntity<SmsPassageReachrateSettings> {
	
	private static final long serialVersionUID = 1L;
	
	private Long passageId;		// 短信通道ID
	private Long interval;		// 轮询间隔（单位秒）
	private Long startTime;		// 数据源时间（开始执行时间点，单位秒）
	private Long timeLength;		// 数据源时长
	private String countPoint;		// 计数阀值，起始点
	private String rateThreshold;		// 到达率，整数，最大100
	private String mobile;		// 通知手机号码
	
	public SmsPassageReachrateSettings() {
		super();
	}
}