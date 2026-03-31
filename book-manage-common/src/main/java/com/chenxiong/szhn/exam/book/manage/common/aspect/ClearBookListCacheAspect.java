package com.chenxiong.szhn.exam.book.manage.common.aspect;

import com.chenxiong.szhn.exam.book.manage.common.annotation.ClearBookListCache;
import com.chenxiong.szhn.exam.book.manage.common.cache.CacheService;
import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 清除图书列表缓存切面
 *
 * @author chenxiong
 * @date 2026/3/31
 */
@Slf4j
@Aspect
@Component
public class ClearBookListCacheAspect {

    @Resource
    private CacheService cacheService;

    @Around("@annotation(clearBookListCache)")
    public Object around(ProceedingJoinPoint joinPoint, ClearBookListCache clearBookListCache) throws Throwable {
        Object result = joinPoint.proceed();
        try {
            /**
             * todo chenxiong
             * 这里缓存失效是临时方案，基于当前5分钟缓存失效，key数量不多的情况
             * 长期方案考虑缓存当前所有图书列表查询key，再全部失效
             */
            cacheService.evictByPrefix(SystemConstant.CACHE_KEY_BOOK_LIST);
            return result;
        } catch (Throwable e) {
            // 清楚缓存失败不影响业务，让其自动过期
            log.error("Clear book list cache exception");
        }
        return result;
    }
}
