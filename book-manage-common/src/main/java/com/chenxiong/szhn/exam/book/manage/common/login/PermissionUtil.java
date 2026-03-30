package com.chenxiong.szhn.exam.book.manage.common.login;

import com.chenxiong.szhn.exam.book.manage.common.enums.UserRoleEnum;
import com.chenxiong.szhn.exam.book.manage.common.exception.BusinessException;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import com.chenxiong.szhn.exam.book.manage.common.util.ThrowUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 权限校验工具类
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public final class PermissionUtil {

    private PermissionUtil() {
    }

    /**
     * 校验当前用户是否已登
     */
    public static LoginUser checkLogin() {
        LoginUser loginUser = LoginContextHolder.getLoginUser();
        ThrowUtil.throwIf(loginUser == null, ErrorCode.UNAUTHORIZED);
        return loginUser;
    }

    /**
     * 校验当前用户是否为管理员
     */
    public static LoginUser checkAdmin() {
        LoginUser loginUser = checkLogin();
        String roleCode = loginUser.getRoleCode();
        ThrowUtil.throwIf(!UserRoleEnum.ADMIN.getCode().equals(roleCode)
                && !UserRoleEnum.SUPER_ADMIN.getCode().equals(roleCode), ErrorCode.FORBIDDEN);

        return loginUser;
    }

    /**
     * 校验当前用户是否为超级管理员
     */
    public static LoginUser checkSuperAdmin() {
        LoginUser loginUser = checkLogin();
        ThrowUtil.throwIf(!UserRoleEnum.SUPER_ADMIN.getCode().equals(loginUser.getRoleCode()), ErrorCode.FORBIDDEN);
        return loginUser;
    }
}
