/**
 *
 */
package com.tangdao.core.model.domain;

import java.beans.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.context.TaskContext.PacketsActionActor;
import com.tangdao.core.context.TaskContext.PacketsActionPosition;
import com.tangdao.core.model.DataEntity;

import cn.hutool.core.util.StrUtil;
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
@TableName("sms_mt_task_packets")
public class MtTaskPackets extends DataEntity<MtTaskPackets> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private Long sid; // 消息ID
	private String mobile; // 手机号码（批量时为多个）
	private Integer cmcp; // 运营商
	private String content; // content
	private Integer mobileSize; // 号码总个数
	private String messageTemplateId; // 短信模板ID
	private String passageId; // 通道ID
	private String finalPassageId; // 最终使用的通道ID
	private String passageProtocol; // 协议类型
	private String passageUrl; // 通道URL
	private String passageParameter; // 参数信息
	private String resultFormat; // 结果模板
	private String successCode; // 成功码
	private String position; // 定位
	private String priority; // 优先级
	private String forceActions; // 异常分包情况下允许的操作，如000,010，第一位:未报备模板，第二位：敏感词，第三位：通道不可用
	private Integer retryTimes; // 调用上家重试次数
	private String areaCode; // 省份代码

	@TableField(exist = false)
	private String appId;

	@TableField(exist = false)
	private String passageName;

	// 省份名称
	@TableField(exist = false)
	private String areaName;

	@TableField(exist = false)
	private String callback;

	// 用户自定义内容
	@TableField(exist = false)
	private String attach;

	// 用户传入的扩展号码
	@TableField(exist = false)
	private String extNumber;

	// add by 20170621 短信模板扩展号码
	@TableField(exist = false)
	private String templateExtNumber;

	@TableField(exist = false)
	private int fee = 1;

	// 单条计费（主要针对点对点和模板点对点）
	@TableField(exist = false)
	private int singleFee = 1;

	// 通道代码
	@TableField(exist = false)
	private String passageCode;
	// 通道流速
	@TableField(exist = false)
	private Integer passageSpeed;

	// 通道签名模式
	@TableField(exist = false)
	private Integer passageSignMode;

	public char[] getActions() {
		if (StrUtil.isNotBlank(forceActions)) {
			return forceActions.toCharArray();
		}
		return null;
	}

	/**
	 * 
	 * TODO 是否模板存在问题
	 * 
	 * @return
	 */
	public boolean isTemplateError() {
		char[] actions = getActions();
		if (actions != null && actions.length > 2) {
			return actions[PacketsActionPosition.SMS_TEMPLATE_MISSED.getPosition()] == PacketsActionActor.BROKEN
					.getActor();
		}
		return false;
	}

	/**
	 * 
	 * TODO 是否包含敏感词
	 * 
	 * @return
	 */
	public boolean isWordError() {
		char[] actions = getActions();
		if (actions != null && actions.length > 2) {
			return actions[PacketsActionPosition.FOBIDDEN_WORDS.getPosition()] == PacketsActionActor.BROKEN.getActor();
		}
		return false;
	}

	/**
	 * 
	 * TODO 是否无可用通道
	 * 
	 * @return
	 */
	public boolean isPassageError() {
		char[] actions = getActions();
		if (actions != null && actions.length > 2) {
			return actions[PacketsActionPosition.PASSAGE_NOT_AVAIABLE.getPosition()] == PacketsActionActor.BROKEN
					.getActor();
		}
		return false;
	}

	public MtTaskPackets() {
		super();
	}

	@Transient
	public String[] getMobiles() {
		if (StrUtil.isNotBlank(mobile)) {
			return mobile.split(",");
		}
		return null;
	}

	@Transient
	public String getFirstMobile() {
		String[] mobiles = getMobiles();
		if (mobiles != null && mobiles.length > 0) {
			return mobiles[0];
		}
		return mobile;
	}
}
