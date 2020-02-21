package com.tangdao.framework.protocol;

/**
 * @ClassName: IEnum.java
 * @Description: TODO(基础枚举接口)
 * @author ruyang
 * @date 2018年10月11日 下午4:32:48
 * 
 */
public interface IEnum<K, V> {

	/**
	 * 获取值
	 * @return
	 */
	K value();

	/**
	 * 获取描述
	 * @return
	 */
	V reasonPhrase();
}
