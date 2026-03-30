package com.chenxiong.szhn.exam.book.manage.common.session;

import com.chenxiong.szhn.exam.book.manage.common.login.LoginUser;

/**
 * Session 存储接口
 *
 * @author chenxiong
 * @date 2026/3/29
 */
public interface SessionService {

    /**
     * 存储登录用户session
     *
     * @param sessionId
     * @param loginUser
     */
    void set(String sessionId, LoginUser loginUser);

    /**
     * 获取登录用户
     *
     * @param sessionId
     * @return 登录用户，不存在返回 null
     */
    LoginUser get(String sessionId);

    /**
     * 删除session
     *
     * @param sessionId
     */
    void remove(String sessionId);

    /**
     * 重刷session有效期
     *
     * @param sessionId
     */
    void refresh(String sessionId);
}
