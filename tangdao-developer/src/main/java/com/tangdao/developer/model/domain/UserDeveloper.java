/**
 *
 */
package com.tangdao.developer.model.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("tds_user_developer")
public class UserDeveloper implements Serializable {

	private static final long serialVersionUID = 1652989610979690232L;

	@TableId
	private String appKey;

	private String appSecret;

	private String salt;

	private String userCode;

	private String status;

}
