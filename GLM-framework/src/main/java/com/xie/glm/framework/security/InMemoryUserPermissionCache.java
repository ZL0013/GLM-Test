package com.xie.glm.framework.security;

import com.xie.glm.common.dto.UserPermissions;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 内存用户权限缓存实现（降级方案）
 *
 * <p>当 Redis 不可用时，使用本地内存缓存。
 * <p>特点：
 * <ul>
 *   <li>使用 ConcurrentHashMap 保证线程安全</li>
 *   <li>使用 ScheduledExecutorService 定期清理过期缓存</li>
 *   <li>单机模式，不支持分布式</li>
 * </ul>
 *
 * @author xie
 */
@Slf4j
@Component
@ConditionalOnMissingBean(name = "redisTemplate")
public class InMemoryUserPermissionCache implements UserPermissionCache {

    /**
     * 缓存条目
     *
     * @param permissions 用户权限
     * @param expireTime  过期时间（毫秒时间戳）
     */
    private record CacheEntry(UserPermissions permissions, long expireTime) {
        boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    /**
     * 本地缓存
     */
    private final ConcurrentHashMap<Long, CacheEntry> cache = new ConcurrentHashMap<>();

    /**
     * 定时清理线程池
     */
    private final ScheduledExecutorService cleanupExecutor;

    public InMemoryUserPermissionCache() {
        // 创建单线程定时任务，每 60 秒清理一次过期缓存
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "permission-cache-cleanup");
            thread.setDaemon(true);
            return thread;
        });
        this.cleanupExecutor.scheduleAtFixedRate(
                this::cleanupExpiredEntries,
                60, 60, TimeUnit.SECONDS
        );
        log.info("内存权限缓存已启动，降级模式");
    }

    @Override
    public void cachePermissions(Long userId, UserPermissions permissions, long ttlSeconds) {
        long expireTime = System.currentTimeMillis() + ttlSeconds * 1000;
        cache.put(userId, new CacheEntry(permissions, expireTime));
        log.debug("内存缓存用户权限: userId={}, ttl={}s", userId, ttlSeconds);
    }

    @Override
    public UserPermissions getPermissions(Long userId) {
        CacheEntry entry = cache.get(userId);
        if (entry == null) {
            log.debug("内存缓存未命中: userId={}", userId);
            return null;
        }
        if (entry.isExpired()) {
            cache.remove(userId);
            log.debug("内存缓存已过期: userId={}", userId);
            return null;
        }
        log.debug("内存缓存命中: userId={}", userId);
        return entry.permissions();
    }

    @Override
    public void evictPermissions(Long userId) {
        CacheEntry removed = cache.remove(userId);
        log.debug("清理内存缓存: userId={}, removed={}", userId, removed != null);
    }

    @Override
    public boolean exists(Long userId) {
        CacheEntry entry = cache.get(userId);
        if (entry == null) {
            return false;
        }
        if (entry.isExpired()) {
            cache.remove(userId);
            return false;
        }
        return true;
    }

    /**
     * 清理过期缓存条目
     */
    private void cleanupExpiredEntries() {
        try {
            int removed = 0;
            for (Map.Entry<Long, CacheEntry> entry : cache.entrySet()) {
                if (entry.getValue().isExpired()) {
                    cache.remove(entry.getKey());
                    removed++;
                }
            }
            if (removed > 0) {
                log.debug("清理过期缓存: removed={} 条", removed);
            }
        } catch (Exception e) {
            log.error("清理过期缓存失败", e);
        }
    }

    /**
     * 销毁时关闭线程池
     */
    @PreDestroy
    public void destroy() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("内存权限缓存已关闭");
    }
}
