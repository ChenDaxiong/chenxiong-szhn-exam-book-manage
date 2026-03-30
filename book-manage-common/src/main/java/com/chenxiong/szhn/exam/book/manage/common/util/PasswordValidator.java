package com.chenxiong.szhn.exam.book.manage.common.util;

import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import com.chenxiong.szhn.exam.book.manage.common.exception.BusinessException;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;

/**
 * 密码强度校验工具类
 *
 * @author chenxiong
 * @date 2026/3/29
 */
public final class PasswordValidator {

    private PasswordValidator() {
    }

    public static void validate(String password) {

        ThrowUtil.throwIf(password == null || password.length() < SystemConstant.PASSWORD_MIN_LENGTH
                , ErrorCode.PARAM_ERROR, "密码长度不能少于" + SystemConstant.PASSWORD_MIN_LENGTH + "个字符");

        ThrowUtil.throwIf(password.length() > SystemConstant.PASSWORD_MAX_LENGTH
                , ErrorCode.PARAM_ERROR, "密码长度不能超过" + SystemConstant.PASSWORD_MAX_LENGTH + "个字符");
        // todo chenxiong 后续可以补充一些密码强度校验
    }
}
