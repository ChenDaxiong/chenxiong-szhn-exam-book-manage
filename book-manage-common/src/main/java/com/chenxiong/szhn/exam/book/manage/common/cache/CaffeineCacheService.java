package com.chenxiong.szhn.exam.book.manage.common.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
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

    private static class ValueWrapper {
        final String json;
        final long expireNanos;

        ValueWrapper(String json, long expireNanos) {
            this.json = json;
            this.expireNanos = expireNanos;
        }
    }

    private final Cache<String, ValueWrapper> cache;
    private final ObjectMapper objectMapper;
    private final long defaultExpireSeconds;


    public CaffeineCacheService(int maxSize, long expireSeconds, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.defaultExpireSeconds = expireSeconds;
        this.cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfter(new Expiry<String, ValueWrapper>() {
                    @Override
                    public long expireAfterCreate(String key, ValueWrapper value, long currentTime) {
                        return value.expireNanos;
                    }

                    @Override
                    public long expireAfterUpdate(String key, ValueWrapper value,
                                                  long currentTime, long currentDuration) {
                        return value.expireNanos;
                    }

                    @Override
                    public long expireAfterRead(String key, ValueWrapper value,
                                                long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }

    @Override
    public <T> void put(String key, T value) {
        put(key, value, defaultExpireSeconds);
    }

    @Override
    public <T> void put(String key, T value, long expireSeconds) {
        String json = serialize(value);
        if (json != null) {
            cache.put(key, new ValueWrapper(json, TimeUnit.SECONDS.toNanos(expireSeconds)));
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        ValueWrapper wrapper = cache.getIfPresent(key);
        if (wrapper == null) {
            return null;
        }
        return deserialize(wrapper.json, clazz);
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeRef) {
        ValueWrapper wrapper = cache.getIfPresent(key);
        if (wrapper == null) {
            return null;
        }
        return deserialize(wrapper.json, typeRef);
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

    @Override
    public <T> boolean putIfAbsent(String key, T value, long expireSeconds) {
        String json = serialize(value);
        if (json == null) {
            return false;
        }
        long nanos = TimeUnit.SECONDS.toNanos(expireSeconds);
        // 原子操作
        ValueWrapper existing = cache.asMap().putIfAbsent(key, new ValueWrapper(json, nanos));
        return existing == null;
    }
}