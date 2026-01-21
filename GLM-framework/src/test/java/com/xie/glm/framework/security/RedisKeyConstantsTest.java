package com.xie.glm.framework.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis Key 常量测试
 *
 * <p>测试 Redis Key 常量的格式和生成方法：
 * <ul>
 *   <li>用户权限缓存 key 格式</li>
 *   <li>Token 黑名单 key 格式</li>
 *   <li>静态方法生成 key</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("Redis Key 常量测试")
class RedisKeyConstantsTest {

    @Test
    @DisplayName("AUTH_USER_PREFIX - 应该是正确的格式")
    void testAuthUserPrefix_ShouldBeCorrectFormat() {
        // Given & Then
        assertEquals("auth:user:", RedisKeyConstants.AUTH_USER_PREFIX,
            "AUTH_USER_PREFIX 应该是 'auth:user:'");
    }

    @Test
    @DisplayName("AUTH_BLACKLIST_PREFIX - 应该是正确的格式")
    void testAuthBlacklistPrefix_ShouldBeCorrectFormat() {
        // Given & Then
        assertEquals("auth:blacklist:", RedisKeyConstants.AUTH_BLACKLIST_PREFIX,
            "AUTH_BLACKLIST_PREFIX 应该是 'auth:blacklist:'");
    }

    @ParameterizedTest
    @CsvSource({
        "1, auth:user:1",
        "100, auth:user:100",
        "999, auth:user:999"
    })
    @DisplayName("userPermissionKey - 应生成正确的 key")
    void testUserPermissionKey_ShouldGenerateCorrectKey(Long userId, String expectedKey) {
        // When
        String actualKey = RedisKeyConstants.userPermissionKey(userId);

        // Then
        assertEquals(expectedKey, actualKey, "生成的 key 应该匹配");
    }

    @Test
    @DisplayName("userPermissionKey - null userId 应生成 'auth:user:null'")
    void testUserPermissionKey_NullUserId_ShouldGenerateKeyWithNull() {
        // When & Then
        // String concatenation doesn't throw NPE for null values
        // "auth:user:" + null = "auth:user:null"
        assertEquals("auth:user:null", RedisKeyConstants.userPermissionKey(null),
            "null userId 应该生成 'auth:user:null'");
    }

    @Test
    @DisplayName("tokenBlacklistKey - 应生成正确的 key")
    void testTokenBlacklistKey_ShouldGenerateCorrectKey() {
        // Given
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.abc123";

        // When
        String key = RedisKeyConstants.tokenBlacklistKey(token);

        // Then
        assertEquals("auth:blacklist:" + token, key,
            "黑名单 key 应该以 'auth:blacklist:' 开头并包含完整 token");
    }

    @Test
    @DisplayName("tokenBlacklistKey - 空字符串 token")
    void testTokenBlacklistKey_EmptyToken_ShouldGenerateKey() {
        // Given
        String token = "";

        // When
        String key = RedisKeyConstants.tokenBlacklistKey(token);

        // Then
        assertEquals("auth:blacklist:", key,
            "空 token 应该生成只有前缀的 key");
    }

    @Test
    @DisplayName("多个不同的 userId - 应生成不同的 key")
    void testMultipleUserIds_ShouldGenerateDifferentKeys() {
        // Given
        Long userId1 = 1L;
        Long userId2 = 100L;
        Long userId3 = 999L;

        // When
        String key1 = RedisKeyConstants.userPermissionKey(userId1);
        String key2 = RedisKeyConstants.userPermissionKey(userId2);
        String key3 = RedisKeyConstants.userPermissionKey(userId3);

        // Then
        assertNotEquals(key1, key2, "不同的 userId 应该生成不同的 key");
        assertNotEquals(key2, key3, "不同的 userId 应该生成不同的 key");
        assertNotEquals(key1, key3, "不同的 userId 应该生成不同的 key");
    }

    @Test
    @DisplayName("用户权限 key 和黑名单 key - 应该有不同的前缀")
    void testUserPermissionKeyAndBlacklistKey_ShouldHaveDifferentPrefixes() {
        // Given
        Long userId = 1L;
        String token = "some-token";

        // When
        String permissionKey = RedisKeyConstants.userPermissionKey(userId);
        String blacklistKey = RedisKeyConstants.tokenBlacklistKey(token);

        // Then
        assertNotEquals(permissionKey, blacklistKey,
            "权限 key 和黑名单 key 应该不同");
        assertTrue(permissionKey.startsWith(RedisKeyConstants.AUTH_USER_PREFIX),
            "权限 key 应该以 AUTH_USER_PREFIX 开头");
        assertTrue(blacklistKey.startsWith(RedisKeyConstants.AUTH_BLACKLIST_PREFIX),
            "黑名单 key 应该以 AUTH_BLACKLIST_PREFIX 开头");
    }

    @Test
    @DisplayName("key 格式一致性 - 应该都使用冒号分隔")
    void testKeyFormatConsistency_ShouldUseColonSeparator() {
        // Given
        Long userId = 1L;
        String token = "test-token";

        // When
        String permissionKey = RedisKeyConstants.userPermissionKey(userId);
        String blacklistKey = RedisKeyConstants.tokenBlacklistKey(token);

        // Then: 验证 key 格式使用冒号分隔
        assertTrue(permissionKey.contains(":"), "权限 key 应该包含冒号");
        assertTrue(blacklistKey.contains(":"), "黑名单 key 应该包含冒号");

        // 验证不包含空格或其他特殊字符
        assertFalse(permissionKey.contains(" "), "权限 key 不应该包含空格");
        assertFalse(blacklistKey.contains(" "), "黑名单 key 不应该包含空格");
    }
}
