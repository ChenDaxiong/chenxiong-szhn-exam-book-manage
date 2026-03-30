package com.chenxiong.szhn.exam.book.manage.common.session;

import com.chenxiong.szhn.exam.book.manage.common.cache.CacheService;
import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于 CacheService 的 Session 存储实现
 * 底层缓存为本地的Caffeine 或 Redis
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
public class DefaultSessionService implements SessionService {

    private final CacheService cacheService;
    private final long timeoutSeconds;

    public DefaultSessionService(CacheService cacheService, long timeoutSeconds) {
        this.cacheService = cacheService;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public void set(String sessionId, LoginUser loginUser) {
        cacheService.put(buildKey(sessionId), loginUser, timeoutSeconds);
    }

    @Override
    public LoginUser get(String sessionId) {
        return cacheService.get(buildKey(sessionId), LoginUser.class);
    }

    @Override
    public void remove(String sessionId) {
        cacheService.evict(buildKey(sessionId));
    }

    @Override
    public void refresh(String sessionId) {
        LoginUser user = get(sessionId);
        if (user != null) {
            set(sessionId, user);
        }
    }

    private String buildKey(String sessionId) {
        return SystemConstant.SESSION_REDIS_KEY_PREFIX + sessionId;
    }
}
