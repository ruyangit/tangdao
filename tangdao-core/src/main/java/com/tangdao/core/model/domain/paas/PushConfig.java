package com.tangdao.core.model.domain.paas;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("paas_push_config")
public class PushConfig extends DataEntity<PushConfig> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userId; // 用户编码
	private String url; // 状态报告地址/上行地址
	private int type; // 类型 1:短信状态报告,2:短信上行回执报告,3:流量充值报告,4:语音发送报告
	private int retryTimes; // 重推次数

	private String status;
	private String remarks;

	public PushConfig() {
		super();
	}

}