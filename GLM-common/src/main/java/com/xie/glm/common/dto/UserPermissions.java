package com.xie.glm.common.dto;

import java.io.Serial;
import java.util.List;

/**
 * 用户权限
 *
 * <p>存储在 Redis 缓存中的用户详细权限信息。
 * <p>Key 格式：{@code auth:user:{userId}}
 *
 * @author xie
 */
public record UserPermissions(
    /**
     * 角色列表（role_key 形式，如 "admin", "common"）
     */
    List<String> roles,

    /**
     * 权限标识列表（如 "system:user:list", "system:user:add"）
     */
    List<String> permissions,

    /**
     * 用户状态（true=正常，false=停用）
     */
    boolean enabled
) implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
