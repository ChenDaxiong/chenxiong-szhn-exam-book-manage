package com.chenxiong.szhn.exam.book.manage.common.util;

import com.chenxiong.szhn.exam.book.manage.common.exception.BusinessException;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;

/**
 * 异常抛出工具类,提高代码简洁性和可读性
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public final class ThrowUtil {

    private ThrowUtil() {
    }

    public static void throwIf(boolean condition, ErrorCode errorCode) {
        if (condition) {
            throw new BusinessException(errorCode);
        }
    }

    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        if (condition) {
            throw new BusinessException(errorCode, message);
        }
    }

    public static void throwIfNull(Object obj, ErrorCode errorCode) {
        if (obj == null) {
            throw new BusinessException(errorCode);
        }
    }

    public static void throwIfNull(Object obj, ErrorCode errorCode, String message) {
        if (obj == null) {
            throw new BusinessException(errorCode, message);
        }
    }
}
