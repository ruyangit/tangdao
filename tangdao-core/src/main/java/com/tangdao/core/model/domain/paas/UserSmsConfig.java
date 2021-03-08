/**
 *
 */
package com.tangdao.core.model.domain.paas;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

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
@TableName("paas_user_sms_config")
public class UserSmsConfig extends DataEntity<UserSmsConfig> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userId;

	private Integer smsWords; // 每条计费字数

	private String smsReturnRule; // 短信返还规则，0:不返还，1：失败自动返还，2：超时未回执返还；

	private Long smsTimeout; // 短信超时时间（毫秒）

	private String messagePass; // 短信内容免审核，1：需要审核，0：不需要

	private String needTemplate; // 是否需要报备模板，1：需要，0：不需要

	private String autoTemplate; // 自动提取短信模板,0-不提取，1-提取

	private Integer signatureSource; // 签名途径，0：自维护，1：系统强制

	private String signatureContent; // 签名内容

	private String extNumber; // 扩展号码

	private Integer submitInterval; // 短信提交时间间隔（同一号码）

	private Integer limitTimes; // 短信每天提交次数上限（同一号码）
	
	private String status;
	
	private String remarks;

}
