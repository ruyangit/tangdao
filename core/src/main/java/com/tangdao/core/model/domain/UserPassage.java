/**
 * 
 */
package com.tangdao.core.model.domain;

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
 * @author ruyangit@gmail.com
 * @since 2020年3月11日
 */
@Getter
@Setter
@TableName("paas_user_passage")
public class UserPassage extends DataEntity<UserPassage> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userId; // 用户编码
	private int type; // 类型 1-短信，2-流量，3-语音
	private String passageGroupId; // 业务通道组ID，如短信通道组ID，流量通道组ID

	public UserPassage() {
		super();
	}

	public UserPassage(String userId, Integer type, String passageGroupId) {
		this.userId = userId;
		this.type = type;
		this.passageGroupId = passageGroupId;
	}
}
