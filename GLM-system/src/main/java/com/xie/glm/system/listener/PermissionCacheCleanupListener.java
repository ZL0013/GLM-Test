package com.xie.glm.system.listener;

import com.xie.glm.framework.security.UserPermissionCache;
import com.xie.glm.system.event.PermissionChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 权限缓存清理监听器
 *
 * <p>监听 {@link PermissionChangedEvent} 事件，异步清理用户权限缓存。
 * <p>使用 {@link TransactionalEventListener} 确保在事务提交后才清理缓存，
 * 避免事务回滚时缓存不一致。
 *
 * @author xie
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionCacheCleanupListener {

    private final UserPermissionCache permissionCache;

    /**
     * 处理权限变更事件
     *
     * <p>在事务提交后异步清理用户权限缓存。
     *
     * @param event 权限变更事件
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePermissionChanged(PermissionChangedEvent event) {
        try {
            permissionCache.evictPermissions(event.userId());
            log.info("用户权限缓存已清理: userId={}, event={}", event.userId(), event);
        } catch (Exception e) {
            log.error("清理用户权限缓存失败: userId={}", event.userId(), e);
        }
    }
}
