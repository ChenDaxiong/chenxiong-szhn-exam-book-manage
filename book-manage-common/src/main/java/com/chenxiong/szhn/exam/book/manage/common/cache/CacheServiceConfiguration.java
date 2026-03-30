package com.chenxiong.szhn.exam.book.manage.common.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * CacheService 配置项
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
@Configuration
public class CacheServiceConfiguration {

    @Value("${cache.type:caffeine}")
    private String cacheType;

    @Value("${cache.expire-seconds:300}")
    private long expireSeconds;

    @Value("${cache.local.max-size:1000}")
    private int localMaxSize;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public CacheService cacheService() {
        CacheService service;
        if ("redis".equalsIgnoreCase(cacheType)) {
            service = new RedisCacheService(stringRedisTemplate, objectMapper, expireSeconds);
        } else {
            service = new CaffeineCacheService(localMaxSize, expireSeconds, objectMapper);
        }
        return service;
    }
}
