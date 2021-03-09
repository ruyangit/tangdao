package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 通道组内容Entity
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Getter
@Setter
@TableName("sms_passage_group_detail")
public class PassageGroupDetail extends DataEntity<PassageGroupDetail> {

	private static final long serialVersionUID = 1L;

	private static final String SPLIT_TAG = "#passage_split#";

	@TableId
	private String id;

	private String groupId; // 通道组id
	private String passageId; // 通道id
	private String areaCode; // 通道代码
	private int routeType; // 路由类型，0默认路由，1验证码路由,2即时通知路由，3批量通知路由，4高风险投诉路由
	private String priority; // 优先级
	private int cmcp; // 运营商1-移动 2-联通 3-电信

	@TableField(exist = false)
	private Passage smsPassage;

	public PassageGroupDetail() {
		super();
	}

	public PassageGroupDetail(String formData) {
		// passageId + split_tag + passageName + split_tag + provinceCode + split_tag +
		// cmcp + split_tag + routeType;
		String[] datas = formData.split(SPLIT_TAG);
		this.passageId = datas[0];
		this.areaCode = datas[2];
		this.cmcp = Integer.parseInt(datas[3]);
		this.routeType = Integer.parseInt(datas[4]);
	}

	public String disponsePassageToSplitStr() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(passageId);
		buffer.append(SPLIT_TAG);
		buffer.append(smsPassage.getName());
		if (smsPassage.getType() == 1) {
			buffer.append(" [独]");
		}
		if (smsPassage.getCmcp() == 4) {
			buffer.append(" [全]");
		}

		buffer.append(SPLIT_TAG);
		buffer.append(areaCode);
		buffer.append(SPLIT_TAG);
		buffer.append(cmcp);
		buffer.append(SPLIT_TAG);
		return buffer.toString();
	}

}