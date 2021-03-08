package com.tangdao.core.model.domain.sms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 手机黑名单信息表Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_mobile_blacklist")
public class MobileBlacklist extends DataEntity<MobileBlacklist> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String mobile; // 手机号码
	private int type; // 类型

	public MobileBlacklist() {
		super();
	}
}