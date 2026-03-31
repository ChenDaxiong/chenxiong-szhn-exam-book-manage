package com.chenxiong.szhn.exam.book.manage.common.cache;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存实现
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
public class RedisCacheService implements CacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final long defaultExpireSeconds;

    public RedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper, long defaultExpireSeconds) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.defaultExpireSeconds = defaultExpireSeconds;
    }

    @Override
    public <T> void put(String key, T value) {
        put(key, value, defaultExpireSeconds);
    }

    @Override
    public <T> void put(String key, T value, long expireSeconds) {
        String json = serialize(value);
        if (json != null) {
            redisTemplate.opsForValue().set(key, json, expireSeconds, TimeUnit.SECONDS);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            return null;
        }
        return deserialize(json, clazz);
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeRef) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            return null;
        }
        return deserialize(json, typeRef);
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void evictByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private <T> String serialize(T value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("RedisCacheService serialize error,value:{}", value.toString(),e);
            return null;
        }
    }

    private <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("RedisCacheService deserialize error,value:{}",json, e);
            return null;
        }
    }

    private <T> T deserialize(String json, TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            log.error("RedisCacheService deserialize error,value:{}",json, e);
            return null;
        }
    }

    @Override
    public <T> boolean putIfAbsent(String key, T value, long expireSeconds) {
        String json = serialize(value);
        if (json == null) {
            return false;
        }
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, json, expireSeconds, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

}
