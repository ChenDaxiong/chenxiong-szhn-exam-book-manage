package com.chenxiong.szhn.exam.book.manage.common.exception;

import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = ErrorCode.BUSINESS_ERROR.getCode();
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
