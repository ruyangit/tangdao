/**
 *
 */
package com.tangdao.developer.model.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月23日
 */
@Getter
@Setter
@TableName("td_sms_api_failed_record")
public class SmsApiFailedRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userCode; // 用户编码
	private Integer appType; // 调用类型
	private String submitType; // 请求类型
	private String appKey; // 接口账号
	private String appSecret; // 接口密码
	private String mobile; // 手机号
	private String timestamps; // 提交时间戳
	private String content; // content
	private String extNumber; // 拓展号码
	private String attach; // 自定义内容
	private String callback; // 回调URL
	private String submitUrl; // 程序调用URL
	private String ip; // 提交IP
	private String code; // 错误码

	private String remark;

	@TableField(fill = FieldFill.INSERT)
	protected Date createDate; // 新增时间

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

	public Date getSubmitTime() {
		if (StrUtil.isNotBlank(timestamps)) {
			long t = Long.valueOf(timestamps);
			return new Date(t);
		}
		return null;
	}
}