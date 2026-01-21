package com.xie.glm.common.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserPermissions 测试
 *
 * <p>测试 UserPermissions record 的各种场景：
 * <ul>
 *   <li>完整参数构造</li>
 *   <li>空列表处理</li>
 *   <li>equals 和 hashCode</li>
 *   <li>序列化</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("UserPermissions 测试")
class UserPermissionsTest {

    @Test
    @DisplayName("完整参数构造 - 应正确创建 UserPermissions")
    void testFullConstructor_ShouldCreateUserPermissions() {
        // Given
        List<String> roles = List.of("admin", "common");
        List<String> permissions = List.of("system:user:list", "system:user:add");
        boolean enabled = true;

        // When
        UserPermissions userPermissions = new UserPermissions(roles, permissions, enabled);

        // Then
        assertEquals(roles, userPermissions.roles(), "roles 应该匹配");
        assertEquals(permissions, userPermissions.permissions(), "permissions 应该匹配");
        assertEquals(enabled, userPermissions.enabled(), "enabled 应该匹配");
    }

    @Test
    @DisplayName("空列表 - 应允许")
    void testEmptyLists_ShouldBeAllowed() {
        // When
        UserPermissions userPermissions = new UserPermissions(List.of(), List.of(), true);

        // Then
        assertTrue(userPermissions.roles().isEmpty(), "roles 应该为空");
        assertTrue(userPermissions.permissions().isEmpty(), "permissions 应该为空");
        assertTrue(userPermissions.enabled(), "enabled 应该为 true");
    }

    @Test
    @DisplayName("null 列表 - record 允许但业务上应避免")
    void testNullLists_RecordAllowsButBusinessShouldAvoid() {
        // When
        UserPermissions userPermissions = new UserPermissions(null, null, false);

        // Then
        assertNull(userPermissions.roles(), "roles 应该为 null");
        assertNull(userPermissions.permissions(), "permissions 应该为 null");
        assertFalse(userPermissions.enabled(), "enabled 应该为 false");
    }

    @Test
    @DisplayName("equals - 相同值应返回 true")
    void testEquals_SameValues_ShouldReturnTrue() {
        // Given
        List<String> roles = List.of("admin", "common");
        List<String> permissions = List.of("system:user:list");
        UserPermissions permissions1 = new UserPermissions(roles, permissions, true);
        UserPermissions permissions2 = new UserPermissions(roles, permissions, true);

        // Then
        assertEquals(permissions1, permissions2, "相同值的 UserPermissions 应该相等");
        assertEquals(permissions1.hashCode(), permissions2.hashCode(), "hashCode 应该相同");
    }

    @Test
    @DisplayName("equals - 不同值应返回 false")
    void testEquals_DifferentValues_ShouldReturnFalse() {
        // Given
        UserPermissions permissions1 = new UserPermissions(
            List.of("admin"),
            List.of("system:user:list"),
            true
        );
        UserPermissions permissions2 = new UserPermissions(
            List.of("common"),
            List.of("system:user:add"),
            false
        );

        // Then
        assertNotEquals(permissions1, permissions2, "不同值的 UserPermissions 应该不相等");
    }

    @Test
    @DisplayName("toString - 应包含所有字段")
    void testToString_ShouldContainAllFields() {
        // Given
        UserPermissions permissions = new UserPermissions(
            List.of("admin"),
            List.of("system:user:list"),
            true
        );

        // When
        String result = permissions.toString();

        // Then
        assertTrue(result.contains("roles"), "toString 应包含 roles");
        assertTrue(result.contains("permissions"), "toString 应包含 permissions");
        assertTrue(result.contains("enabled"), "toString 应包含 enabled");
    }

    @Test
    @DisplayName("实现 Serializable - 应可序列化")
    void testSerializable_ShouldImplementSerializable() {
        // Given
        UserPermissions permissions = new UserPermissions(
            List.of("admin"),
            List.of("system:user:list"),
            true
        );

        // Then
        assertInstanceOf(java.io.Serializable.class, permissions,
            "UserPermissions 应该实现 Serializable");
    }

    @Test
    @DisplayName("不可变性 - record 自动提供")
    void testImmutability_RecordProvides() {
        // Given
        List<String> roles = List.of("admin");
        List<String> permissions = List.of("system:user:list");
        UserPermissions userPermissions = new UserPermissions(roles, permissions, true);

        // Then: record 不提供 setter，所有字段都是 final
        // 尝试修改列表不会影响 record 本身（因为 record 存储的是引用）
        assertEquals(roles, userPermissions.roles());
        assertEquals(permissions, userPermissions.permissions());
    }
}
