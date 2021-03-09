package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 下行短信处理失败Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_mt_process_failed")
public class MtProcessFailed extends DataEntity<MtProcessFailed> {

	private static final long serialVersionUID = 1L;

	private Long sid; // sid
	private String appType; // 调用类型 1:融合WEB平台,2:开发者平台,3:运营支撑系统
	private String ip; // 发送IP
	private String userId; // 用户编码
	private String mobile; // 手机号
	private String cmcp; // 运营商，参见cmcp枚举
	private String templateId; // 模板编号
	private String content; // 短信内容
	private String fee; // 计费条数
	private String extNumber; // 拓展号码
	private String attach; // 自定义内容
	private String callback; // 回调URL（选填）
	private String lastestStatus; // 上一条状态

	public MtProcessFailed() {
		super();
	}
}