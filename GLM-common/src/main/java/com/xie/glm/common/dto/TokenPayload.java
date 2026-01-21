package com.xie.glm.common.dto;

import java.io.Serial;

/**
 * Token 载荷
 *
 * <p>Access Token 中包含的核心信息。
 * <p>详细权限信息（roles、permissions）从 Redis 缓存中获取。
 *
 * @author xie
 */
public record TokenPayload(
    /**
     * 用户 ID
     */
    Long userId,

    /**
     * 用户名
     */
    String username,

    /**
     * 部门 ID
     */
    Long deptId,

    /**
     * 数据权限范围
     * <ul>
     *   <li>1=全部数据权限</li>
     *   <li>2=自定义数据权限</li>
     *   <li>3=本部门数据权限</li>
     *   <li>4=本部门及以下数据权限</li>
     *   <li>5=仅本人数据权限</li>
     * </ul>
     */
    Integer dataScope
) implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
