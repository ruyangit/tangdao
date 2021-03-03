package com.tangdao.core.model.domain.passage;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.BaseModel;
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
@TableName(BaseModel.DB_PREFIX_ + "sms_passage_area")
public class SmsPassageArea extends DataEntity<SmsPassageArea> {
	
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;
	private String passageId;		// 通道ID
	private String araCode;		// 省份代码
	
	public SmsPassageArea() {
		super();
	}
	
	public SmsPassageArea(String passageId,String araCode){
        this.passageId = passageId;
        this.araCode = araCode;
    }
	
}