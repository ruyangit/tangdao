/**
 *
 */
package com.tangdao.core.model.domain.sms;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tangdao.core.BaseModel;
import com.tangdao.core.DataEntity;
import com.tangdao.core.context.TaskContext.PacketsActionActor;
import com.tangdao.core.context.TaskContext.PacketsActionPosition;
import com.tangdao.core.context.TaskContext.TaskSubmitType;

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
@TableName(BaseModel.DB_PREFIX_ + "sms_mt_task")
public class MtTask extends DataEntity<MtTask> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;

	private String appId;

	private Long sid; // 消息ID
	private Integer appType; // 调用类型 1:融合WEB平台,2:开发者平台,3:运营支撑系统
	private String mobile; // 手机号
	private String content; // content
	private String extNumber; // 拓展号码
	private String attach; // 自定义内容
	private String callback; // 回调URL（选填）
	private Integer fee; // 计费条数
	private Integer returnFee; // 返还条数
	private String submitUrl; // 程序调用URL
	private String ip; // 提交IP
	// 提交类型：Enum@TaskSubmitType
	private Integer submitType = TaskSubmitType.BATCH_MESSAGE.getCode();; // 提交类型：0：批量短信，1：普通点对点，2：模板点对点
	private Integer processStatus; // 分包状态,0:正在分包，1：分包完成，待发送，2:分包异常，待处理，3:分包失败，终止
	private Integer approveStatus; // 0：待审核，1：自动通过，2：手动通过，3：审核未通过
	private String errorMobiles; // 错号手机号码
	private String repeatMobiles; // 重复手机号码
	private String blackMobiles; // 黑名单手机号码
	private String finalContent; // final_content

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date processTime; // 分包完成时间
	private String forceActions; // 异常分包情况下允许的操作，如000,010，第一位:未报备模板，第二位：敏感词，第三位：通道不可用
	private String messageTemplateId; // 短信模板ID
	private String forbiddenWords; // 敏感词

	public MtTask() {
		super();
	}

	@TableField(exist = false)
	private Integer totalFee;

	// 用户原提交手机号码，未进行黑名单等处理的号码
	@TableField(exist = false)
	private String originMobile;
	// 点对点短信用户原内容
	@TableField(exist = false)
	private String p2pBody;
	// 点对点短信分析后报文内容
	@TableField(exist = false)
	private List<JSONObject> p2pBodies;

	// 敏感词标签
	@TableField(exist = false)
	private List<ForbiddenWords> forbiddenWordLabels;

	@TableField(exist = false)
	private List<MtTaskPackets> packets;

	// 汇总错误信息
	private transient StringBuilder errorMessageReport = new StringBuilder();

	// 允许操作的强制动作，如敏感词报备，模板报备，切换通道
	private transient StringBuilder forceActionsReport = new StringBuilder("000");

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

	public String[] getMobiles() {
		if (StrUtil.isNotBlank(mobile)) {
			return mobile.split(",");
		}
		return null;
	}

	public String getFirstMobile() {
		String[] mobiles = getMobiles();
		if (mobiles != null && mobiles.length > 0) {
			return mobiles[0];
		}
		return mobile;
	}

	public String[] getShowErrorMobiles() {
		if (StrUtil.isNotBlank(errorMobiles)) {
			return errorMobiles.split(",");
		}
		return null;
	}

	public String getShowErrorFirstMobile() {
		String[] mobiles = getShowErrorMobiles();
		if (mobiles != null && mobiles.length > 0) {
			return mobiles[0];
		}
		return mobile;
	}

	public String[] getShowRepeatMobiles() {
		if (StrUtil.isNotBlank(repeatMobiles)) {
			return repeatMobiles.split(",");
		}
		return null;
	}

	public String getShowRepeatFirstMobiles() {
		String[] mobiles = getShowRepeatMobiles();
		if (mobiles != null && mobiles.length > 0) {
			return mobiles[0];
		}
		return mobile;
	}

}
