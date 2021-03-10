/**
 *
 */
package com.tangdao.core.model.vo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tangdao.core.model.domain.SmsPassageAccess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
@Getter
@Setter
public class SmsRoutePassage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;

	// 通道手机号码信息（手机号码以逗号分割）
	private Map<String, String> passageMobiles = new HashMap<>();
	// 未找到对应通道手机号码集合
	private Set<String> unkonwnMobiles = new HashSet<>();
	// 通道信息
	private Map<String, SmsPassageAccess> passaegAccesses = new HashMap<>();

	// 错误信息
	private String errorMessage;

	/**
	 * TODO 加入通道ID和手机号码对应关系(即同一个通道下面拥有手机号码集合信息)
	 * 
	 * @param cmcp      运营商
	 * @param passageId
	 * @param mobiles
	 */
	public void addPassageMobilesMapping(String passageId, String mobiles) {
		if (CollUtil.isEmpty(passageMobiles) || !passageMobiles.containsKey(passageId)) {
			passageMobiles.put(passageId, mobiles);
			return;
		}
		// 拼接手机号码
		passageMobiles.put(passageId, passageMobiles.get(passageId) + MobileCatagory.MOBILE_SPLIT_CHARCATOR + mobiles);
	}

	public void addUnknownMobiles(String mobile) {
		if (StrUtil.isEmpty(mobile)) {
			return;
		}
		this.unkonwnMobiles.addAll(Arrays.asList(mobile.split(MobileCatagory.MOBILE_SPLIT_CHARCATOR)));
	}

}
