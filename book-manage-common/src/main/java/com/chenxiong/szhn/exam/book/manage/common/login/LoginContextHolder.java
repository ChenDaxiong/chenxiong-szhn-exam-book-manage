package com.chenxiong.szhn.exam.book.manage.common.login;

/**
 * 登录用户上下文Holder，基于ThreadLocal
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public final class LoginContextHolder {

    private static final ThreadLocal<LoginUser> CONTEXT = new ThreadLocal<>();

    private LoginContextHolder() {
    }

    /**
     * 设置当前登录用户
     */
    public static void setLoginUser(LoginUser loginUser) {
        CONTEXT.set(loginUser);
    }

    /**
     * 获取当前登录用户
     */
    public static LoginUser getLoginUser() {
        return CONTEXT.get();
    }

    /**
     * 获取当前登录用户名
     */
    public static String getUsername() {
        LoginUser user = CONTEXT.get();
        return user != null ? user.getUsername() : null;
    }

    /**
     * 清除当前线程的登录用户信息
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
