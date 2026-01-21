package com.xie.glm.framework.security;

/**
 * Redis Key 常量
 *
 * <p>定义系统中使用的 Redis Key 格式常量。
 *
 * @author xie
 */
public final class RedisKeyConstants {

    private RedisKeyConstants() {
        // 工具类，禁止实例化
    }

    /**
     * 用户权限缓存 Key 前缀
     * <p>格式：{@code auth:user:{userId}}
     * <p>示例：{@code auth:user:1}
     */
    public static final String AUTH_USER_PREFIX = "auth:user:";

    /**
     * Token 黑名单 Key 前缀（可选）
     * <p>格式：{@code auth:blacklist:{token}}
     * <p>用于主动失效 token
     */
    public static final String AUTH_BLACKLIST_PREFIX = "auth:blacklist:";

    /**
     * 生成用户权限缓存 Key
     *
     * @param userId 用户 ID
     * @return Redis Key
     */
    public static String userPermissionKey(Long userId) {
        return AUTH_USER_PREFIX + userId;
    }

    /**
     * 生成 Token 黑名单 Key
     *
     * @param token JWT token（使用完整的 token 字符串，实际可以使用 token 的 hash 值缩短）
     * @return Redis Key
     */
    public static String tokenBlacklistKey(String token) {
        return AUTH_BLACKLIST_PREFIX + token;
    }
}
