package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 通道消息模板Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_passage_message_template")
public class PassageMessageTemplate extends DataEntity<PassageMessageTemplate> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String templateId; // 通道模板ID（通道方提供）
	private String templateContent; // 模板内容
	private String regexValue; // 模板表达式
	private String paramNames; // 参数名称，多个以,分割(有序)
	private String passageId; // 通道ID

	private String status;

	public PassageMessageTemplate() {
		super();
	}
}