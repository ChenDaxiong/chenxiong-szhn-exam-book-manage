package com.chenxiong.szhn.exam.book.manage.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * Service 层统一返回结果
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
public class ServiceResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;

    private T data;

    private Integer code;

    private String message;

    private ServiceResult() {
    }

    public static <T> ServiceResult<T> success(T data) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setSuccess(true);
        result.setData(data);
        result.setCode(ErrorCode.SUCCESS.getCode());
        result.setMessage(ErrorCode.SUCCESS.getMessage());
        return result;
    }

    public static ServiceResult<Void> success() {
        ServiceResult<Void> result = new ServiceResult<>();
        result.setSuccess(true);
        result.setCode(ErrorCode.SUCCESS.getCode());
        result.setMessage(ErrorCode.SUCCESS.getMessage());
        return result;
    }

    public static ServiceResult<Void> successMsg(String message) {
        ServiceResult<Void> result = new ServiceResult<>();
        result.setSuccess(true);
        result.setCode(ErrorCode.SUCCESS.getCode());
        result.setMessage(message);
        return result;
    }

    public static <T> ServiceResult<T> fail(ErrorCode errorCode) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setSuccess(false);
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());
        return result;
    }

    public static <T> ServiceResult<T> fail(ErrorCode errorCode, String message) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setSuccess(false);
        result.setCode(errorCode.getCode());
        result.setMessage(message);
        return result;
    }

    public static <T> ServiceResult<T> fail(Integer code, String message) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
