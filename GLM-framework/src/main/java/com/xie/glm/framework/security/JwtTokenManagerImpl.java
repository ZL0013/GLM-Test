package com.xie.glm.framework.security;

import com.xie.glm.common.dto.TokenPayload;
import com.xie.glm.framework.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * JWT Token 管理器实现
 *
 * <p>基于 JJWT 库实现 JWT token 的生成、验证和解析。
 * <p>支持双 token 机制：
 * <ul>
 *   <li>Access Token：短期有效，用于 API 访问</li>
 *   <li>Refresh Token：长期有效，用于刷新 Access Token</li>
 * </ul>
 *
 * @author xie
 */
@Component
@RequiredArgsConstructor
public class JwtTokenManagerImpl implements JwtTokenManager {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenManagerImpl.class);

    private final JwtProperties jwtProperties;

    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_TOKEN_TYPE = "tokenType";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    // ========== 新增：TokenPayload 相关常量 ==========
    private static final String CLAIM_KEY_USER_ID = "userId";
    private static final String CLAIM_KEY_DEPT_ID = "deptId";
    private static final String CLAIM_KEY_DATA_SCOPE = "dataScope";
    private static final String CLAIM_KEY_ACCESS_TOKEN_ID = "accessTokenId";

    /**
     * 获取签名密钥
     *
     * @return SecretKey
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public boolean validateAccessToken(String token) {
        return validateToken(token, TOKEN_TYPE_ACCESS);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).get(CLAIM_KEY_USERNAME, String.class);
    }

    @Override
    public String generateAccessToken(String username) {
        return generateToken(username, TOKEN_TYPE_ACCESS, jwtProperties.getAccessTokenExpiration());
    }

    @Override
    public String generateRefreshToken(String username) {
        return generateToken(username, TOKEN_TYPE_REFRESH, jwtProperties.getRefreshTokenExpiration());
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return validateToken(token, TOKEN_TYPE_REFRESH);
    }

    @Override
    public String extractUsernameFromRefreshToken(String refreshToken) {
        return extractClaims(refreshToken).get(CLAIM_KEY_USERNAME, String.class);
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new JwtException("Refresh token 无效或已过期");
        }
        String username = extractUsernameFromRefreshToken(refreshToken);
        return generateAccessToken(username);
    }

    /**
     * 生成 JWT token
     *
     * @param username 用户名
     * @param tokenType token 类型（access 或 refresh）
     * @param expiration 过期时间（毫秒）
     * @return JWT token
     */
    private String generateToken(String username, String tokenType, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claim(CLAIM_KEY_USERNAME, username)
                .claim(CLAIM_KEY_TOKEN_TYPE, tokenType)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 验证 token 是否有效
     *
     * @param token JWT token
     * @param expectedTokenType 期望的 token 类型
     * @return 如果 token 有效返回 true，否则返回 false
     */
    private boolean validateToken(String token, String expectedTokenType) {
        try {
            Claims claims = extractClaims(token);
            String tokenType = claims.get(CLAIM_KEY_TOKEN_TYPE, String.class);
            return expectedTokenType.equals(tokenType);
        } catch (JwtException e) {
            log.debug("JWT token 验证失败: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT token 解析异常", e);
            return false;
        }
    }

    /**
     * 从 token 中提取 Claims
     *
     * @param token JWT token
     * @return Claims
     * @throws JwtException 如果 token 无效
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ========== 新增：TokenPayload 相关方法实现 ==========

    @Override
    public String generateAccessToken(TokenPayload payload) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());
        String accessTokenId = generateAccessTokenId();

        return Jwts.builder()
                .claim(CLAIM_KEY_ACCESS_TOKEN_ID, accessTokenId)
                .claim(CLAIM_KEY_USER_ID, payload.userId())
                .claim(CLAIM_KEY_USERNAME, payload.username())
                .claim(CLAIM_KEY_DEPT_ID, payload.deptId())
                .claim(CLAIM_KEY_DATA_SCOPE, payload.dataScope())
                .claim(CLAIM_KEY_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public String extractAccessTokenId(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.get(CLAIM_KEY_ACCESS_TOKEN_ID, String.class);
        } catch (JwtException e) {
            log.debug("解析 accessTokenId 失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String generateAccessTokenId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public TokenPayload extractTokenPayload(String token) {
        try {
            Claims claims = extractClaims(token);

            // 验证 token 类型
            String tokenType = claims.get(CLAIM_KEY_TOKEN_TYPE, String.class);
            if (!TOKEN_TYPE_ACCESS.equals(tokenType)) {
                log.warn("Token 类型错误，期望: {}, 实际: {}", TOKEN_TYPE_ACCESS, tokenType);
                return null;
            }

            // 提取字段
            Long userId = claims.get(CLAIM_KEY_USER_ID, Long.class);
            String username = claims.get(CLAIM_KEY_USERNAME, String.class);
            Long deptId = claims.get(CLAIM_KEY_DEPT_ID, Long.class);
            Integer dataScope = claims.get(CLAIM_KEY_DATA_SCOPE, Integer.class);

            // 兼容旧格式 token（如果没有 userId 字段，返回 null）
            if (userId == null) {
                log.debug("Token 中缺少 userId 字段，可能是旧格式 token");
                return null;
            }

            return new TokenPayload(userId, username, deptId, dataScope);
        } catch (JwtException e) {
            log.debug("解析 TokenPayload 失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String extractAccessTokenIdFromExpiredToken(String expiredAccessToken) {
        try {
            // 使用允许过期时间的解析器（仅验证签名，不验证过期时间）
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(expiredAccessToken)
                    .getPayload();

            // 验证 token 类型必须是 access
            String tokenType = claims.get(CLAIM_KEY_TOKEN_TYPE, String.class);
            if (!TOKEN_TYPE_ACCESS.equals(tokenType)) {
                log.debug("Token 类型错误，期望: {}, 实际: {}", TOKEN_TYPE_ACCESS, tokenType);
                return null;
            }

            return claims.get(CLAIM_KEY_ACCESS_TOKEN_ID, String.class);
        } catch (JwtException e) {
            log.debug("解析过期的 Access Token 失败: {}", e.getMessage());
            return null;
        }
    }
}
