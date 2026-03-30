package com.chenxiong.szhn.exam.book.manage.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Getter
@AllArgsConstructor
public enum OperationType {

    INSERT("新增", "insert"),
    UPDATE("修改", "update"),
    DELETE("删除", "delete"),
    SELECT("查询", "select"),
    LOGIN("登录", "login"),
    LOGOUT("登出", "logout"),
    OTHER("其他", "other");

    private final String name;
    private final String code;
}
