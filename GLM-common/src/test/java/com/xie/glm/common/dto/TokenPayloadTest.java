package com.xie.glm.common.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TokenPayload 测试
 *
 * <p>测试 TokenPayload record 的各种场景：
 * <ul>
 *   <li>完整参数构造</li>
 *   <li>null 值处理</li>
 *   <li>equals 和 hashCode</li>
 *   <li>序列化</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("TokenPayload 测试")
class TokenPayloadTest {

    @Test
    @DisplayName("完整参数构造 - 应正确创建 TokenPayload")
    void testFullConstructor_ShouldCreateTokenPayload() {
        // Given
        Long userId = 1L;
        String username = "admin";
        Long deptId = 100L;
        Integer dataScope = 3;

        // When
        TokenPayload payload = new TokenPayload(userId, username, deptId, dataScope);

        // Then
        assertEquals(userId, payload.userId(), "userId 应该匹配");
        assertEquals(username, payload.username(), "username 应该匹配");
        assertEquals(deptId, payload.deptId(), "deptId 应该匹配");
        assertEquals(dataScope, payload.dataScope(), "dataScope 应该匹配");
    }

    @ParameterizedTest
    @CsvSource({
        "1, admin, 100, 1",
        "2, user1, 101, 2",
        "3, user2, 102, 3",
        "4, user3, 103, 4",
        "5, user4, 104, 5"
    })
    @DisplayName("参数化测试 - 不同数据权限范围")
    void testDifferentDataScopes(Long userId, String username, Long deptId, Integer dataScope) {
        // When
        TokenPayload payload = new TokenPayload(userId, username, deptId, dataScope);

        // Then
        assertEquals(userId, payload.userId());
        assertEquals(username, payload.username());
        assertEquals(deptId, payload.deptId());
        assertEquals(dataScope, payload.dataScope());
    }

    @Test
    @DisplayName("null deptId - 应允许（用户可能没有部门）")
    void testNullDeptId_ShouldBeAllowed() {
        // When
        TokenPayload payload = new TokenPayload(1L, "admin", null, 5);

        // Then
        assertNull(payload.deptId(), "deptId 应该为 null");
        assertEquals(1L, payload.userId());
        assertEquals("admin", payload.username());
        assertEquals(5, payload.dataScope());
    }

    @Test
    @DisplayName("equals - 相同值应返回 true")
    void testEquals_SameValues_ShouldReturnTrue() {
        // Given
        TokenPayload payload1 = new TokenPayload(1L, "admin", 100L, 3);
        TokenPayload payload2 = new TokenPayload(1L, "admin", 100L, 3);

        // Then
        assertEquals(payload1, payload2, "相同值的 TokenPayload 应该相等");
        assertEquals(payload1.hashCode(), payload2.hashCode(), "hashCode 应该相同");
    }

    @Test
    @DisplayName("equals - 不同值应返回 false")
    void testEquals_DifferentValues_ShouldReturnFalse() {
        // Given
        TokenPayload payload1 = new TokenPayload(1L, "admin", 100L, 3);
        TokenPayload payload2 = new TokenPayload(2L, "user", 101L, 4);

        // Then
        assertNotEquals(payload1, payload2, "不同值的 TokenPayload 应该不相等");
    }

    @Test
    @DisplayName("toString - 应包含所有字段")
    void testToString_ShouldContainAllFields() {
        // Given
        TokenPayload payload = new TokenPayload(1L, "admin", 100L, 3);

        // When
        String result = payload.toString();

        // Then
        assertTrue(result.contains("userId=1"), "toString 应包含 userId");
        assertTrue(result.contains("username=admin"), "toString 应包含 username");
        assertTrue(result.contains("deptId=100"), "toString 应包含 deptId");
        assertTrue(result.contains("dataScope=3"), "toString 应包含 dataScope");
    }

    @Test
    @DisplayName("实现 Serializable - 应可序列化")
    void testSerializable_ShouldImplementSerializable() {
        // Given
        TokenPayload payload = new TokenPayload(1L, "admin", 100L, 3);

        // Then
        assertInstanceOf(java.io.Serializable.class, payload,
            "TokenPayload 应该实现 Serializable");
    }

    @ParameterizedTest
    @DisplayName("数据权限范围边界值测试")
    @MethodSource("provideDataScopeBoundaryValues")
    void testDataScopeBoundaryValues(Integer dataScope, boolean expectedValid) {
        // When
        TokenPayload payload = new TokenPayload(1L, "admin", 100L, dataScope);

        // Then
        assertEquals(dataScope, payload.dataScope());

        // 验证数据权限范围在 1-5 之间（业务规则）
        if (expectedValid) {
            assertTrue(payload.dataScope() >= 1 && payload.dataScope() <= 5,
                "dataScope 应该在 1-5 之间");
        }
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideDataScopeBoundaryValues() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(1, true),
            org.junit.jupiter.params.provider.Arguments.of(2, true),
            org.junit.jupiter.params.provider.Arguments.of(3, true),
            org.junit.jupiter.params.provider.Arguments.of(4, true),
            org.junit.jupiter.params.provider.Arguments.of(5, true),
            org.junit.jupiter.params.provider.Arguments.of(0, false),
            org.junit.jupiter.params.provider.Arguments.of(6, false),
            org.junit.jupiter.params.provider.Arguments.of(-1, false)
        );
    }
}
