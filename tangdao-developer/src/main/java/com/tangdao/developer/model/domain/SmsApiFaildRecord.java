/**
 *
 */
package com.tangdao.developer.model.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("td_sms_api_faild_record")
public class SmsApiFaildRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
