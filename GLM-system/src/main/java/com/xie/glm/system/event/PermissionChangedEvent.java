package com.xie.glm.system.event;

import org.springframework.context.ApplicationEvent;

/**
 * 权限变更事件
 *
 * <p>当用户权限变更时发布此事件，触发缓存清理。
 * <p>触发场景：
 * <ul>
 *   <li>用户角色分配变更</li>
 *   <li>角色权限配置变更</li>
 *   <li>用户状态变更（启用/停用）</li>
 *   <li>部门变更（影响数据权限）</li>
 * </ul>
 *
 * @author xie
 */
public class PermissionChangedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    private final Long userId;

    /**
     * 创建权限变更事件
     *
     * @param source 事件源（通常是发布事件的 Service）
     * @param userId 用户 ID
     */
    public PermissionChangedEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }

    /**
     * 获取用户 ID
     *
     * @return 用户 ID
     */
    public Long userId() {
        return userId;
    }

    @Override
    public String toString() {
        return "PermissionChangedEvent{" +
                "userId=" + userId +
                ", source=" + getSource() +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
