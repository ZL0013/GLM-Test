package com.xie.glm.framework.security;

import com.xie.glm.common.dto.TokenPayload;

/**
 * JWT Token 管理器
 *
 * <p>负责 JWT token 的生成、验证和解析。
 *
 * @author xie
 */
public interface JwtTokenManager {

    /**
     * 验证 Access Token 是否有效
     *
     * @param token JWT token
     * @return 如果 token 有效返回 true，否则返回 false
     */
    boolean validateAccessToken(String token);

    /**
     * 从 token 中提取用户名
     *
     * @param token JWT token
     * @return 用户名，如果 token 无效则返回 null
     */
    String extractUsername(String token);

    /**
     * 生成 Access Token
     *
     * @param username 用户名
     * @return JWT token
     */
    String generateAccessToken(String username);

    /**
     * 生成 Refresh Token
     *
     * @param username 用户名
     * @return JWT refresh token
     */
    String generateRefreshToken(String username);

    /**
     * 验证 Refresh Token 是否有效
     *
     * @param token Refresh token
     * @return 如果 token 有效返回 true，否则返回 false
     */
    boolean validateRefreshToken(String token);

    /**
     * 从 refresh token 中提取用户名
     *
     * @param refreshToken Refresh token
     * @return 用户名
     */
    String extractUsernameFromRefreshToken(String refreshToken);

    /**
     * 刷新 token
     *
     * @param refreshToken 旧的 refresh token
     * @return 新的 access token
     */
    String refreshAccessToken(String refreshToken);

    // ========== 新增：TokenPayload 支持 ==========

    /**
     * 使用 TokenPayload 生成 Access Token
     *
     * <p>Access Token 包含核心信息（userId、username、deptId、dataScope）。
     * <p>详细权限（roles、permissions）从 Redis 缓存中获取。
     *
     * @param payload Token 载荷
     * @return JWT access token
     */
    String generateAccessToken(TokenPayload payload);

    /**
     * 从 token 中提取 TokenPayload
     *
     * @param token JWT token
     * @return Token 载荷，如果 token 无效则返回 null
     */
    TokenPayload extractTokenPayload(String token);
}
