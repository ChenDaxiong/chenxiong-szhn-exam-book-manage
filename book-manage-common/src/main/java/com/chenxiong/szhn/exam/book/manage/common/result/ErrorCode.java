package com.chenxiong.szhn.exam.book.manage.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局错误码枚举
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 成功
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统内部错误"),

    // 客户端异常
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),

    // 业务异常
    BUSINESS_ERROR(1000, "业务异常"),
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_EXISTS(1002, "用户名已存在"),
    BOOK_NOT_FOUND(1004, "图书不存在"),
    BOOK_EXISTS(1005, "图书名称已存在"),
    CATEGORY_NOT_FOUND(1006, "分类不存在"),
    CATEGORY_HAS_CHILDREN(1007, "分类下存在子分类"),
    LOGIN_FAILED(1008, "用户名或密码错误"),
    ACCOUNT_DISABLED(1009, "账号已被禁用"),

    BOOK_ALREADY_BORROWED(1010, "您已借阅该图书，请勿重复借阅"),
    BOOK_NOT_BORROWED(1011, "未找到借阅记录"),
    BOOK_OUT_OF_STOCK(1012, "图书库存不足"),
    BORROW_RATE_LIMITED(1013, "操作过于频繁，请稍后再试"),
    BORROW_CONCURRENT_CONFLICT(1014, "操作冲突，请重试");

    private final Integer code;
    private final String message;
}
