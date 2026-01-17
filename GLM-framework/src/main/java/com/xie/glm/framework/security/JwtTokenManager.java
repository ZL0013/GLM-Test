package com.xie.glm.framework.security;

/**
 * JWT Token 管理器
 *
 * <p>负责 JWT token 的生成、验证和解析。
 *
 * <p>注意：这是一个占位实现，完整实现将在 Phase 2.2 中完成。
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
}
