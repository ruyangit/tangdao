/**
 *
 */
package com.tangdao.common;

import java.util.Collection;
import java.util.Map;

import com.tangdao.common.constant.ErrorCode;
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
    public static void gtZero(Integer num, ErrorCode errorCode) {
        if (num == null || num <= 0) {
            fail(errorCode);
        }
    }

    /**
     * 大于等于O
     */
    public static void geZero(Integer num, ErrorCode errorCode) {
        if (num == null || num < 0) {
            fail(errorCode);
        }
    }

    /**
     * num1大于num2
     */
    public static void gt(Integer num1, Integer num2, ErrorCode errorCode) {
        if (num1 <= num2) {
            fail(errorCode);
        }
    }

    /**
     * num1大于等于num2
     */
    public static void ge(Integer num1, Integer num2, ErrorCode errorCode) {
        if (num1 < num2) {
            fail(errorCode);
        }
    }

    /**
     * obj1 eq obj2
     */
    public static void eq(Object obj1, Object obj2, ErrorCode errorCode) {
        if (!obj1.equals(obj2)) {
            fail(errorCode);
        }
    }

    public static void isTrue(boolean condition, ErrorCode errorCode) {
        if (!condition) {
            fail(errorCode);
        }
    }

    public static void isFalse(boolean condition, ErrorCode errorCode) {
        if (condition) {
            fail(errorCode);
        }
    }

    public static void isNull(ErrorCode errorCode, Object... conditions) {
        if (ObjectUtil.isNotNull(conditions)) {
            fail(errorCode);
        }
    }

    public static void notNull(ErrorCode errorCode, Object... conditions) {
        if (ObjectUtil.isNull(conditions)) {
            fail(errorCode);
        }
    }

    /**
     * 失败结果
     *
     * @param errorCode 异常错误码
     */
    public static void fail(ErrorCode errorCode) {
        throw new BusinessException(errorCode);
    }

    public static void fail(boolean condition, ErrorCode errorCode) {
        if (condition) {
            fail(errorCode);
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

    public static void notEmpty(Object[] array, ErrorCode errorCode) {
        if (ObjectUtil.isEmpty(array)) {
            fail(errorCode);
        }
    }

    public static void noNullElements(Object[] array, ErrorCode errorCode) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    fail(errorCode);
                }
            }
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorCode errorCode) {
        if (CollUtil.isNotEmpty(collection)) {
            fail(errorCode);
        }
    }

    public static void notEmpty(Map<?, ?> map, ErrorCode errorCode) {
        if (ObjectUtil.isEmpty(map)) {
            fail(errorCode);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, ErrorCode errorCode) {
        notNull(errorCode, type);
        if (!type.isInstance(obj)) {
            fail(errorCode);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, ErrorCode errorCode) {
        notNull(errorCode, superType);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            fail(errorCode);
        }
    }
}
