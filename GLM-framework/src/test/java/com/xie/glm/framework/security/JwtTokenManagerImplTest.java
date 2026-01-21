package com.xie.glm.framework.security;

import com.xie.glm.common.dto.TokenPayload;
import com.xie.glm.framework.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT Token 管理器实现测试（TokenPayload 支持）
 *
 * <p>测试新增的 TokenPayload 相关方法：
 * <ul>
 *   <li>generateAccessToken(TokenPayload) - 生成包含完整信息的 token</li>
 *   <li>extractTokenPayload(String) - 从 token 解析 TokenPayload</li>
 *   <li>向后兼容性 - 旧格式 token 仍可正常工作</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("JWT Token 管理器实现测试（TokenPayload）")
class JwtTokenManagerImplTest {

    private JwtTokenManagerImpl jwtTokenManager;
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("4085eeeb53ab89f4e36790184e17e10e4b5c0202935e5d1d7d8f3c1a6b2e9d7f");
        jwtProperties.setAccessTokenExpiration(1800000L); // 30分钟
        jwtProperties.setRefreshTokenExpiration(604800000L); // 7天

        jwtTokenManager = new JwtTokenManagerImpl(jwtProperties);
    }

    @Test
    @DisplayName("使用 TokenPayload 生成 Access Token - 应成功")
    void testGenerateAccessToken_WithTokenPayload_ShouldSucceed() {
        // Given
        TokenPayload payload = new TokenPayload(1L, "admin", 100L, 3);

        // When
        String token = jwtTokenManager.generateAccessToken(payload);

        // Then
        assertNotNull(token, "Token 不应该为 null");
        assertFalse(token.isEmpty(), "Token 不应该为空");
        // JWT token 格式：header.payload.signature
        assertTrue(token.contains("."), "Token 应该包含点号分隔符");
    }

    @Test
    @DisplayName("从 Token 解析 TokenPayload - 应正确解析")
    void testExtractTokenPayload_ValidToken_ShouldExtractCorrectly() {
        // Given
        TokenPayload originalPayload = new TokenPayload(1L, "admin", 100L, 3);
        String token = jwtTokenManager.generateAccessToken(originalPayload);

        // When
        TokenPayload extractedPayload = jwtTokenManager.extractTokenPayload(token);

        // Then
        assertNotNull(extractedPayload, "解析的 payload 不应该为 null");
        assertEquals(originalPayload.userId(), extractedPayload.userId(), "userId 应该匹配");
        assertEquals(originalPayload.username(), extractedPayload.username(), "username 应该匹配");
        assertEquals(originalPayload.deptId(), extractedPayload.deptId(), "deptId 应该匹配");
        assertEquals(originalPayload.dataScope(), extractedPayload.dataScope(), "dataScope 应该匹配");
    }

    @ParameterizedTest
    @CsvSource({
        "1, admin, 100, 1",
        "2, user1, 101, 2",
        "3, user2, 102, 3",
        "4, user3, 103, 4",
        "5, user4, 104, 5"
    })
    @DisplayName("参数化测试 - 不同数据权限范围的 Token 生成和解析")
    void testTokenPayloadWithDifferentDataScopes(Long userId, String username, Long deptId, Integer dataScope) {
        // Given
        TokenPayload payload = new TokenPayload(userId, username, deptId, dataScope);

        // When
        String token = jwtTokenManager.generateAccessToken(payload);
        TokenPayload extractedPayload = jwtTokenManager.extractTokenPayload(token);

        // Then
        assertNotNull(extractedPayload, "解析的 payload 不应该为 null");
        assertEquals(userId, extractedPayload.userId());
        assertEquals(username, extractedPayload.username());
        assertEquals(deptId, extractedPayload.deptId());
        assertEquals(dataScope, extractedPayload.dataScope());
    }

    @Test
    @DisplayName("null deptId - 应正常生成和解析")
    void testTokenPayloadWithNullDeptId_ShouldWork() {
        // Given
        TokenPayload payload = new TokenPayload(1L, "admin", null, 5);

        // When
        String token = jwtTokenManager.generateAccessToken(payload);
        TokenPayload extractedPayload = jwtTokenManager.extractTokenPayload(token);

        // Then
        assertNotNull(extractedPayload, "解析的 payload 不应该为 null");
        assertEquals(1L, extractedPayload.userId());
        assertEquals("admin", extractedPayload.username());
        assertNull(extractedPayload.deptId(), "deptId 应该为 null");
        assertEquals(5, extractedPayload.dataScope());
    }

    @Test
    @DisplayName("从旧格式 token 解析 TokenPayload - 应返回 null")
    void testExtractTokenPayload_LegacyToken_ShouldReturnNull() {
        // Given: 生成旧格式 token（只有 username）
        String legacyToken = jwtTokenManager.generateAccessToken("admin");

        // When
        TokenPayload payload = jwtTokenManager.extractTokenPayload(legacyToken);

        // Then
        assertNull(payload, "旧格式 token 应该返回 null");
    }

    @Test
    @DisplayName("Refresh token 解析 TokenPayload - 应返回 null")
    void testExtractTokenPayload_RefreshToken_ShouldReturnNull() {
        // Given
        String refreshToken = jwtTokenManager.generateRefreshToken("admin");

        // When
        TokenPayload payload = jwtTokenManager.extractTokenPayload(refreshToken);

        // Then
        assertNull(payload, "Refresh token 解析 TokenPayload 应该返回 null");
    }

    @Test
    @DisplayName("无效 token - extractTokenPayload 应返回 null")
    void testExtractTokenPayload_InvalidToken_ShouldReturnNull() {
        // Given
        String invalidToken = "invalid.token.string";

        // When
        TokenPayload payload = jwtTokenManager.extractTokenPayload(invalidToken);

        // Then
        assertNull(payload, "无效 token 应该返回 null");
    }

    @Test
    @DisplayName("向后兼容 - 旧格式 Access Token 仍可验证")
    void testBackwardCompatibility_LegacyAccessToken_ShouldBeValid() {
        // Given: 生成旧格式 token
        String username = "admin";
        String legacyToken = jwtTokenManager.generateAccessToken(username);

        // When & Then: 旧 token 应该仍可验证
        assertTrue(jwtTokenManager.validateAccessToken(legacyToken),
            "旧格式 Access Token 应该有效");

        // And: 应该能提取用户名
        assertEquals(username, jwtTokenManager.extractUsername(legacyToken),
            "应该能从旧 token 提取用户名");
    }

    @Test
    @DisplayName("新格式 token - validateAccessToken 应该返回 true")
    void testValidateAccessToken_NewFormatToken_ShouldReturnTrue() {
        // Given
        TokenPayload payload = new TokenPayload(1L, "admin", 100L, 3);
        String token = jwtTokenManager.generateAccessToken(payload);

        // When & Then
        assertTrue(jwtTokenManager.validateAccessToken(token),
            "新格式 Access Token 应该有效");
    }

    @Test
    @DisplayName("Token 包含完整的声明 - userId、username、deptId、dataScope、tokenType")
    void testTokenClaims_ShouldContainAllRequiredFields() {
        // Given
        TokenPayload payload = new TokenPayload(1L, "admin", 100L, 3);
        String token = jwtTokenManager.generateAccessToken(payload);

        // When
        TokenPayload extractedPayload = jwtTokenManager.extractTokenPayload(token);

        // Then: 验证所有必需的字段都存在
        assertNotNull(extractedPayload, "应该能解析 payload");
        assertNotNull(extractedPayload.userId(), "userId 不应该为 null");
        assertNotNull(extractedPayload.username(), "username 不应该为 null");
        // deptId 可以为 null
        assertNotNull(extractedPayload.dataScope(), "dataScope 不应该为 null");
    }

    @Test
    @DisplayName("Token 过期测试")
    void testTokenExpiration() {
        // Given: 创建一个极短过期时间的配置
        JwtProperties shortLivedProperties = new JwtProperties();
        shortLivedProperties.setSecret(jwtProperties.getSecret());
        shortLivedProperties.setAccessTokenExpiration(1L); // 1毫秒
        shortLivedProperties.setRefreshTokenExpiration(jwtProperties.getRefreshTokenExpiration());

        JwtTokenManagerImpl shortLivedManager = new JwtTokenManagerImpl(shortLivedProperties);
        TokenPayload payload = new TokenPayload(1L, "admin", 100L, 3);
        String token = shortLivedManager.generateAccessToken(payload);

        // When: 等待 token 过期
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Then: token 应该无效
        assertFalse(shortLivedManager.validateAccessToken(token),
            "过期的 token 应该无效");
    }

    @Test
    @DisplayName("不同用户生成不同的 token")
    void testDifferentUsersGenerateDifferentTokens() {
        // Given
        TokenPayload payload1 = new TokenPayload(1L, "admin", 100L, 3);
        TokenPayload payload2 = new TokenPayload(2L, "user", 101L, 4);

        // When
        String token1 = jwtTokenManager.generateAccessToken(payload1);
        String token2 = jwtTokenManager.generateAccessToken(payload2);

        // Then
        assertNotEquals(token1, token2, "不同用户应该生成不同的 token");
    }
}
