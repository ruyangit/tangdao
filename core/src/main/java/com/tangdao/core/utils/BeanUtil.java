/**
 *
 */
package com.tangdao.core.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.tangdao.core.annotation.TreeName;
import com.tangdao.core.config.Global;
import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.exception.BusinessException;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月9日
 */
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {

	/**
	 * 实体类主键
	 */
	private static Map<String, String> PK_NID_ENTITY_CACHE = new ConcurrentHashMap<>();

	/**
	 * 获取Entity主键
	 * 
	 * @return
	 */
	public static String getPrimaryKey(Class<?> clazz) {
		if (PK_NID_ENTITY_CACHE.size() > 1000) {
			PK_NID_ENTITY_CACHE.clear();
		}
		if (!PK_NID_ENTITY_CACHE.containsKey(clazz.getName())) {
			String pk = Global.FieldName.id.name();
			List<Field> fields = ReflectionKit.getFieldList(clazz);
			if (CollUtil.isNotEmpty(fields)) {
				for (Field field : fields) {
					TableId tableId = field.getAnnotation(TableId.class);
					if (tableId == null) {
						continue;
					}
					TableField tableField = field.getAnnotation(TableField.class);
					if (tableField != null && tableField.exist() == false) {
						continue;
					}
					pk = field.getName();
					break;
				}
			}
			PK_NID_ENTITY_CACHE.put(clazz.getName(), pk);
		}
		return PK_NID_ENTITY_CACHE.get(clazz.getName());
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getTreeNameKey(Class<?> clazz) {
		List<Field> fields = ReflectionKit.getFieldList(clazz);
		if (CollUtil.isNotEmpty(fields)) {
			for (Field field : fields) {
				TreeName tableId = field.getAnnotation(TreeName.class);
				if (tableId != null) {
					return field.getName();
				}
			}
		}
		throw new BusinessException(CommonApiCode.COMMON_ANNOTATION_EXCEPTION, "请配置 @TreeName 属性");
	}
}
