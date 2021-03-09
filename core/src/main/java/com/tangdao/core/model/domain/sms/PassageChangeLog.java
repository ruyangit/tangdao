package com.tangdao.core.model.domain.sms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 通道变更日志Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_passage_change_log")
public class PassageChangeLog extends DataEntity<PassageChangeLog> {

	private static final long serialVersionUID = 1L;

	private String passageId; // 通道ID
	private String oldPassageId; // 原通道ID
	private String groupId; // 通道组id
	private String userId; // 用户编码
	private String routeType; // 路由类型
	private String cmcp; // 运营商
	private String operateType; // 操作方式

	public PassageChangeLog() {
		super();
	}
}