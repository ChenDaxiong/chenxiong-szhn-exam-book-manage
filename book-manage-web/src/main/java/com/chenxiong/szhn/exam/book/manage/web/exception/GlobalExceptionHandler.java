package com.chenxiong.szhn.exam.book.manage.web.exception;

import com.chenxiong.szhn.exam.book.manage.common.exception.BusinessException;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import com.chenxiong.szhn.exam.book.manage.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("BusinessException code：{}，message：{}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("SYSTEM_ERROR", e);
        return Result.fail(ErrorCode.SYSTEM_ERROR, "系统内部错误，请联系管理员");
    }
}
