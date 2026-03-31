package com.chenxiong.szhn.exam.book.manage.common.helper;

import com.chenxiong.szhn.exam.book.manage.common.cache.CacheService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 限流帮助类
 *
 * @author chenxiong
 * @date 2026/3/31
 */
@Component
public class RateLimitHelper {
    // 默认的值
    private final static String DEFAULT_VALUE = "1";

    @Resource
    private CacheService cacheService;

    /**
     * 简单限流
     *
     * @param key
     * @param seconds
     * @return
     */
    public boolean tryLimit(String key, long seconds) {
        if (null != cacheService.get(key, String.class)) {
            return false;
        }
        // 设置默认的值1
        cacheService.put(key, DEFAULT_VALUE, seconds);
        return true;
    }
}
