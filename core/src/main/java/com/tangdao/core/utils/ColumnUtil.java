/**
 * 
 */
package com.tangdao.core.utils;

import java.util.Map;

import org.springframework.core.annotation.AnnotationAttributes;

import com.tangdao.core.annotation.TableScannerRegistrar;

import cn.hutool.core.bean.BeanUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2021年4月10日
 */
public class ColumnUtil {

	public static AnnotationAttributes[] getColumns(Class<?> clazz) {
		Map<String, Object> tsreg = TableScannerRegistrar.getTable(clazz);
		Object obj = null;
		if ((obj = tsreg.get("columns")) != null) {
			return (AnnotationAttributes[]) obj;
		}
		return null;
	}

	public static String getPKAttrName(Class<?> clazz) {
		AnnotationAttributes[] aas = ColumnUtil.getColumns(clazz);
		for (AnnotationAttributes annotationAttributes : aas) {
			if (annotationAttributes.getBoolean("isPK")) {
				return annotationAttributes.getString("attrName");
			}
		}
		return null;
	}

	public static Object getPKAttrVal(Object bean) {
		AnnotationAttributes[] aas = ColumnUtil.getColumns(bean.getClass());
		for (AnnotationAttributes annotationAttributes : aas) {
			if (annotationAttributes.getBoolean("isPK")) {
				return BeanUtil.getFieldValue(bean, annotationAttributes.getString("attrName"));
			}
		}
		return null;
	}

	public static String getTreeNameAttrName(Class<?> clazz) {
		AnnotationAttributes[] aas = ColumnUtil.getColumns(clazz);
		for (AnnotationAttributes annotationAttributes : aas) {
			if (annotationAttributes.getBoolean("isTreeName")) {
				return annotationAttributes.getString("attrName");
			}
		}
		return null;
	}

	public static Object getTreeNameAttrVal(Object bean) {
		AnnotationAttributes[] aas = ColumnUtil.getColumns(bean.getClass());
		for (AnnotationAttributes annotationAttributes : aas) {
			if (annotationAttributes.getBoolean("isTreeName")) {
				return BeanUtil.getFieldValue(bean, annotationAttributes.getString("attrName"));
			}
		}
		return null;
	}
}
