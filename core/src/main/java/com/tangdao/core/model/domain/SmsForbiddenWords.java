/**
 *
 */
package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月24日
 */
@Getter
@Setter
@TableName("sms_forbidden_words")
public class SmsForbiddenWords extends DataEntity<SmsForbiddenWords> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String word; // 敏感词
	private int level; // 告警级别，区分颜色
	private String label; // 标签

	public SmsForbiddenWords() {
		super();
	}
}
