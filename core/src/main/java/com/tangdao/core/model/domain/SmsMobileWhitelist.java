package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 手机白名单信息表Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_mobile_whitelist")
public class SmsMobileWhitelist extends DataEntity<SmsMobileWhitelist> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String mobile; // mobile
	private String userId; // 用户编码

	public SmsMobileWhitelist() {
		super();
	}

}