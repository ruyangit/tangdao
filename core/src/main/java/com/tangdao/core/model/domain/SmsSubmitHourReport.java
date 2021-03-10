package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 提交报告（小时）Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_submit_hour_report")
public class SmsSubmitHourReport extends DataEntity<SmsSubmitHourReport> {

	private static final long serialVersionUID = 1L;

	private String userId; // 用户编码
	private String passageId; // 通道ID
	private String provinceCode; // 省份代码
	private String submitCount; // 提交数量
	private String billCount; // 计费数
	private String unknownCount; // 未知数量
	private String successCount; // 成功数量
	private String submitFailedCount; // 发送失败数量
	private String otherCount; // 其他数量
	private String bornHours; // 落地小时阀值
	private Long hourTime; // 当前小时毫秒数

	public SmsSubmitHourReport() {
		super();
	}

}