package com.chenxiong.szhn.exam.book.manage.common.util;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 密码加密工具类,基于 BCrypt 算法
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * 加密密码
     */
    public static String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    /**
     * 校验密码
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
