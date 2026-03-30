package com.chenxiong.szhn.exam.book.manage.common.session;

import com.chenxiong.szhn.exam.book.manage.common.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SessionService配置
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
@Configuration
public class SessionServiceConfiguration {

    @Value("${session.timeout-seconds:7200}")
    private long timeoutSeconds;

    @Bean
    public SessionService sessionService(CacheService cacheService) {
        return new DefaultSessionService(cacheService, timeoutSeconds);
    }
}
