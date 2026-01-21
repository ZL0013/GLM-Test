package com.xie.glm.common.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SecurityUtil 安全工具类测试
 * <p>
 * 测试原则：
 * 1. 测试获取当前用户信息的方法
 * 2. 测试在无认证环境下的默认行为
 * 3. 测试用户 ID 获取
 *
 * @author xie
 */
class SecurityUtilTest {

    /**
     * 测试获取当前用户名
     * <p>
     * 注意：在无 Spring Security 环境下，应返回 null
     */
    @Test
    void testGetCurrentUsername() {
        String username = SecurityUtil.getCurrentUsername();
        // 无认证环境下应返回 null
        assertNull(username);
    }

    /**
     * 测试获取当前用户 ID
     * <p>
     * 注意：在无 Spring Security 环境下，应返回 null
     */
    @Test
    void testGetCurrentUserId() {
        Long userId = SecurityUtil.getCurrentUserId();
        // 无认证环境下应返回 null
        assertNull(userId);
    }

    /**
     * 测试判断是否已认证
     */
    @Test
    void testIsAuthenticated() {
        boolean authenticated = SecurityUtil.isAuthenticated();
        // 无认证环境下应返回 false
        assertFalse(authenticated);
    }

    /**
     * 测试判断是否有指定权限
     */
    @ParameterizedTest
    @ValueSource(strings = {"system:user:list", "system:user:add", "admin"})
    void testHasAuthority(String authority) {
        boolean hasAuthority = SecurityUtil.hasAuthority(authority);
        // 无认证环境下应返回 false
        assertFalse(hasAuthority);
    }

    /**
     * 测试判断是否有任意一个权限
     */
    @Test
    void testHasAnyAuthority() {
        boolean result = SecurityUtil.hasAnyAuthority("system:user:list", "system:user:add");
        // 无认证环境下应返回 false
        assertFalse(result);
    }

    /**
     * 测试判断是否有所有权限
     */
    @Test
    void testHasAllAuthorities() {
        boolean result = SecurityUtil.hasAllAuthorities("system:user:list", "system:user:add");
        // 无认证环境下应返回 false（因为没有权限）
        assertFalse(result);
    }

    /**
     * 测试判断是否为管理员
     */
    @Test
    void testIsAdmin() {
        boolean admin = SecurityUtil.isAdmin();
        // 无认证环境下应返回 false
        assertFalse(admin);
    }

    /**
     * 测试空权限检查
     */
    @Test
    void testHasAuthorityWithNull() {
        boolean result = SecurityUtil.hasAuthority(null);
        assertFalse(result);
    }

    /**
     * 测试空权限数组检查
     */
    @Test
    void testHasAnyAuthorityWithEmptyArray() {
        boolean result = SecurityUtil.hasAnyAuthority();
        assertFalse(result);
    }

    /**
     * 测试获取当前用户详情
     */
    @Test
    void testGetCurrentUserDetails() {
        Object userDetails = SecurityUtil.getCurrentUserDetails();
        // 无认证环境下应返回 null
        assertNull(userDetails);
    }
}
