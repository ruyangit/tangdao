/**
 *
 */
package com.tangdao.core.model.domain.sms;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.BaseModel;
import com.tangdao.core.DataEntity;

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
@TableName(BaseModel.DB_PREFIX_ + "sms_api_failed_record")
public class ApiFailedRecord extends DataEntity<ApiFailedRecord> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String appId; // 用户编码
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

	private String remarks;
	
	private String status;

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
