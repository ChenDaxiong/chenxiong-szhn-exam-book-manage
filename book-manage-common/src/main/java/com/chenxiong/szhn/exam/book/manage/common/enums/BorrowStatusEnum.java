package com.chenxiong.szhn.exam.book.manage.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 借阅状态枚举
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Getter
@AllArgsConstructor
public enum BorrowStatusEnum {

    RETURNED(0, "已归还"),
    BORROWING(1, "借阅中");

    private final int code;
    private final String name;

}

