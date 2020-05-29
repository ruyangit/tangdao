/**
 *
 */
package com.tangdao.common;

import java.util.Collection;
import java.util.Map;

import com.tangdao.common.constant.IBaseEnum;
import com.tangdao.common.exception.BusinessException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public class Assert {

	protected Assert() {
        // to do noting
    }

    /**
     * 大于O
     */
    public static void gtZero(Integer num, IBaseEnum baseEnum) {
        if (num == null || num <= 0) {
            fail(baseEnum);
        }
    }

    /**
     * 大于等于O
     */
    public static void geZero(Integer num, IBaseEnum baseEnum) {
        if (num == null || num < 0) {
            fail(baseEnum);
        }
    }

    /**
     * num1大于num2
     */
    public static void gt(Integer num1, Integer num2, IBaseEnum baseEnum) {
        if (num1 <= num2) {
            fail(baseEnum);
        }
    }

    /**
     * num1大于等于num2
     */
    public static void ge(Integer num1, Integer num2, IBaseEnum baseEnum) {
        if (num1 < num2) {
            fail(baseEnum);
        }
    }

    /**
     * obj1 eq obj2
     */
    public static void eq(Object obj1, Object obj2, IBaseEnum baseEnum) {
        if (!obj1.equals(obj2)) {
            fail(baseEnum);
        }
    }

    public static void isTrue(boolean condition, IBaseEnum baseEnum) {
        if (!condition) {
            fail(baseEnum);
        }
    }

    public static void isFalse(boolean condition, IBaseEnum baseEnum) {
        if (condition) {
            fail(baseEnum);
        }
    }

    public static void isNull(IBaseEnum baseEnum, Object... conditions) {
        if (ObjectUtil.isNotNull(conditions)) {
            fail(baseEnum);
        }
    }

    public static void notNull(IBaseEnum baseEnum, Object... conditions) {
        if (ObjectUtil.isNull(conditions)) {
            fail(baseEnum);
        }
    }

    /**
     * 失败结果
     *
     * @param baseEnum 异常错误码
     */
    public static void fail(IBaseEnum baseEnum) {
        throw new BusinessException(baseEnum);
    }

    public static void fail(boolean condition, IBaseEnum baseEnum) {
        if (condition) {
            fail(baseEnum);
        }
    }

    public static void fail(String message) {
        throw new BusinessException(message);
    }

    public static void fail(boolean condition, String message) {
        if (condition) {
            fail(message);
        }
    }

    public static void notEmpty(Object[] array, IBaseEnum baseEnum) {
        if (ObjectUtil.isEmpty(array)) {
            fail(baseEnum);
        }
    }

    public static void noNullElements(Object[] array, IBaseEnum baseEnum) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    fail(baseEnum);
                }
            }
        }
    }

    public static void notEmpty(Collection<?> collection, IBaseEnum baseEnum) {
        if (CollUtil.isNotEmpty(collection)) {
            fail(baseEnum);
        }
    }

    public static void notEmpty(Map<?, ?> map, IBaseEnum baseEnum) {
        if (ObjectUtil.isEmpty(map)) {
            fail(baseEnum);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, IBaseEnum baseEnum) {
        notNull(baseEnum, type);
        if (!type.isInstance(obj)) {
            fail(baseEnum);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, IBaseEnum baseEnum) {
        notNull(baseEnum, superType);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            fail(baseEnum);
        }
    }
}
