package com.chenxiong.szhn.exam.book.manage.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Controller 层统一返回结果
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
@ApiModel(description = "统一返回结果")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("提示信息")
    private String message;

    @ApiModelProperty("返回数据")
    private T data;

    private Result() {
    }

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ==================== 成功 ====================

    public static <T> Result<T> success() {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), message, data);
    }

    public static Result<Void> successMsg(String message) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), message, null);
    }

    // ==================== 失败 ====================

    public static <T> Result<T> fail(String message) {
        return new Result<>(ErrorCode.SYSTEM_ERROR.getCode(), message, null);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> Result<T> fail(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null);
    }

    // ==================== ServiceResult 转换 ====================

    public static <T> Result<T> from(ServiceResult<T> serviceResult) {
        if (serviceResult.isSuccess()) {
            return success(serviceResult.getData());
        }
        return fail(serviceResult.getCode(), serviceResult.getMessage());
    }

    // ==================== 判断 ====================

    public boolean isSuccess() {
        return code != null && code.equals(ErrorCode.SUCCESS.getCode());
    }
}
