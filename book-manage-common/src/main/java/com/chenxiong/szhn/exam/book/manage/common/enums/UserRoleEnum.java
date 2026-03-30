package com.chenxiong.szhn.exam.book.manage.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Getter
@AllArgsConstructor
public enum UserRoleEnum {

    SUPER_ADMIN("SUPER_ADMIN", "超级管理员"),
    ADMIN("ADMIN", "管理员"),
    USER("USER", "普通用户");

    private final String code;
    private final String name;
}
