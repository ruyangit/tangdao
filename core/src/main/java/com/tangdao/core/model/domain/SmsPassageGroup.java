package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 通道组Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_passage_group")
public class SmsPassageGroup extends DataEntity<SmsPassageGroup> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String passageGroupName; // 通道组名称

	public SmsPassageGroup() {
		super();
	}
}