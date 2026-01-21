package com.xie.glm.framework.security;

import com.xie.glm.common.dto.UserPermissions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis 用户权限缓存实现
 *
 * <p>使用 Redis 存储用户权限信息，支持分布式环境。
 * <p>仅在 RedisTemplate Bean 存在时启用。
 *
 * @author xie
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(RedisTemplate.class)
public class RedisUserPermissionCache implements UserPermissionCache {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void cachePermissions(Long userId, UserPermissions permissions, long ttlSeconds) {
        String key = RedisKeyConstants.userPermissionKey(userId);
        try {
            redisTemplate.opsForValue().set(key, permissions, ttlSeconds, TimeUnit.SECONDS);
            log.debug("缓存用户权限成功: userId={}, ttl={}s", userId, ttlSeconds);
        } catch (Exception e) {
            log.error("缓存用户权限失败: userId={}", userId, e);
            throw new RuntimeException("缓存用户权限失败", e);
        }
    }

    @Override
    public UserPermissions getPermissions(Long userId) {
        String key = RedisKeyConstants.userPermissionKey(userId);
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value instanceof UserPermissions) {
                log.debug("命中用户权限缓存: userId={}", userId);
                return (UserPermissions) value;
            }
            log.debug("用户权限缓存不存在: userId={}", userId);
            return null;
        } catch (Exception e) {
            log.error("获取用户权限缓存失败: userId={}", userId, e);
            return null;
        }
    }

    @Override
    public void evictPermissions(Long userId) {
        String key = RedisKeyConstants.userPermissionKey(userId);
        try {
            Boolean deleted = redisTemplate.delete(key);
            log.debug("清理用户权限缓存: userId={}, deleted={}", userId, deleted);
        } catch (Exception e) {
            log.error("清理用户权限缓存失败: userId={}", userId, e);
        }
    }

    @Override
    public boolean exists(Long userId) {
        String key = RedisKeyConstants.userPermissionKey(userId);
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            log.error("检查用户权限缓存是否存在失败: userId={}", userId, e);
            return false;
        }
    }
}
