package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 优先级词库配置Entity
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_priority_words")
public class PriorityWords extends DataEntity<PriorityWords> {
	
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;
	
	private String userId;		// 用户编码
	private String content;		// 内容
	private String priority;		// 优先级
	
	public PriorityWords() {
		super();
	}
}