package com.tangdao.core.model.domain.paas;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;
import com.tangdao.core.model.vo.TemplateDetail;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月9日
 */
@Getter
@Setter
@TableName("paas_passage_template")
public class PassageTemplate extends DataEntity<PassageTemplate> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String name; // 模板名称
	private String protocol; // 协议
	private String passageType; // 1-短信通道模板 2-流量通道模板 3-语音通道模板

	public PassageTemplate() {
		super();
	}

	@TableField(exist = false)
	private List<String> callType;

	@TableField(exist = false)
	private List<String> url;

	@TableField(exist = false)
	private List<String> successCode;

	@TableField(exist = false)
	private List<List<TemplateDetail>> templateDetail;

}