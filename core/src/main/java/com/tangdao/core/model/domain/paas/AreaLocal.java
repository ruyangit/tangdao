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
 * @author ruyangit@gmail.com
 * @since 2020年3月11日
 */
@Getter
@Setter
@TableName("paas_area_local")
public class AreaLocal extends DataEntity<AreaLocal> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String areaCode; // 归属地代码
	private String numberArea; // 号段
	private int cmcp; // 运营商

	public AreaLocal() {
		super();
	}

	public AreaLocal(String areaCode, int cmcp) {
		super();
		this.areaCode = areaCode;
		this.cmcp = cmcp;
	}
}
