package com.xie.glm.framework.security;

import com.xie.glm.common.dto.UserPermissions;

/**
 * 用户权限缓存服务
 *
 * <p>定义用户权限缓存的统一接口。
 * <p>支持多种实现：
 * <ul>
 *   <li>Redis 实现（推荐）</li>
 *   <li>内存实现（降级方案）</li>
 * </ul>
 *
 * @author xie
 */
public interface UserPermissionCache {

    /**
     * 缓存用户权限
     *
     * @param userId      用户 ID
     * @param permissions 用户权限
     * @param ttlSeconds  过期时间（秒）
     */
    void cachePermissions(Long userId, UserPermissions permissions, long ttlSeconds);

    /**
     * 获取用户权限
     *
     * @param userId 用户 ID
     * @return 用户权限，如果不存在或已过期返回 null
     */
    UserPermissions getPermissions(Long userId);

    /**
     * 移除用户权限缓存
     *
     * <p>用于权限变更时主动清理缓存。
     *
     * @param userId 用户 ID
     */
    void evictPermissions(Long userId);

    /**
     * 检查用户权限缓存是否存在
     *
     * @param userId 用户 ID
     * @return 如果缓存存在返回 true，否则返回 false
     */
    boolean exists(Long userId);
}
