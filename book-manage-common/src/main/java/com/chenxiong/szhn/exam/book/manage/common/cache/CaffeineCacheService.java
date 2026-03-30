package com.chenxiong.szhn.exam.book.manage.common.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存实现
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
public class CaffeineCacheService implements CacheService {

    private final Cache<String, String> cache;
    private final ObjectMapper objectMapper;

    public CaffeineCacheService(int maxSize, long expireSeconds, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public <T> void put(String key, T value) {
        String json = serialize(value);
        if (json != null) {
            cache.put(key, json);
        }
    }

    @Override
    public <T> void put(String key, T value, long expireSeconds) {
        put(key, value);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String json = cache.getIfPresent(key);
        if (json == null) {
            return null;
        }
        return deserialize(json, clazz);
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeRef) {
        String json = cache.getIfPresent(key);
        if (json == null) {
            return null;
        }
        return deserialize(json, typeRef);
    }

    @Override
    public void evict(String key) {
        cache.invalidate(key);
    }

    @Override
    public void evictByPrefix(String prefix) {
        cache.asMap().keySet().removeIf(k -> k.startsWith(prefix));
    }

    private <T> String serialize(T value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("CaffeineCacheService serialize error", e);
            return null;
        }
    }

    private <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("CaffeineCacheService deserialize error", e);
            return null;
        }
    }

    private <T> T deserialize(String json, TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            log.error("CaffeineCacheService deserialize error", e);
            return null;
        }
    }
}
