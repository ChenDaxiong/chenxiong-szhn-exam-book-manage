package com.chenxiong.szhn.exam.book.manage.common.cache;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 统一缓存服务
 *
 * @author chenxiong
 * @date 2026/3/29
 */
public interface CacheService {

    /**
     * 写入缓存
     */
    <T> void put(String key, T value);

    /**
     * 写入缓存（指定过期时间，单位秒）
     */
    <T> void put(String key, T value, long expireSeconds);

    /**
     * 查询缓存
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 查询缓存
     */
    <T> T get(String key, TypeReference<T> typeRef);

    /**
     * 删除缓存
     */
    void evict(String key);

    /**
     * 按前缀批量删除缓存
     */
    void evictByPrefix(String prefix);
}
