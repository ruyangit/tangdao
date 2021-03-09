package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 下行短信提交Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_mt_message_submit")
public class MtMessageSubmit extends DataEntity<MtMessageSubmit> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userId; // 用户编码
	private Long sid; // 消息ID
	private String mobile; // 手机号码
	private String areaCode; // 省份代码
	private Integer cmcp; // 运营商
	private String content; // 短信内容
	private Integer fee; // 计费条数
	private String attach; // 自定义内容
	private String passageId; // 通道ID
	private Boolean needPush = false; // 是否需要推送，0：不需要，1：需要
	private String pushUrl; // 推送地址
	private String destnationNo; // 扩展号码
	private String msgId; // 调用接口回执ID，默认与SID一致

	@TableField(exist = false)
	private MtMessageDeliver messageDeliver;

	@TableField(exist = false)
	private MtMessagePush messagePush;

	@TableField(exist = false)
	private String passageName;

	@TableField(exist = false)
	private String appKey;
	// 回调网址
	@TableField(exist = false)
	private String callback;

	// 发送条数，伪列
	@TableField(exist = false)
	private Integer amount;

	// 针对提交失败补状态码
	@TableField(exist = false)
	private String pushErrorCode;

	// 用户信息
	@TableField(exist = false)
	private User user;

	public MtMessageSubmit() {
		super();
	}
}