package com.tangdao.core.model.domain.sms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 通道支持省份Entity
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_passage_area")
public class PassageArea extends DataEntity<PassageArea> {
	
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;
	private String passageId;		// 通道ID
	private String araCode;		// 省份代码
	
	public PassageArea() {
		super();
	}
	
	public PassageArea(String passageId,String araCode){
        this.passageId = passageId;
        this.araCode = araCode;
    }
	
}