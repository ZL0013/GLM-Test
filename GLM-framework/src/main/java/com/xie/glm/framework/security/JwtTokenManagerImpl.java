package com.xie.glm.framework.security;

import org.springframework.stereotype.Component;

/**
 * JWT Token 管理器实现（占位）
 *
 * <p>这是一个占位实现，完整实现将在 Phase 2.2 中完成。
 *
 * @author xie
 */
@Component
public class JwtTokenManagerImpl implements JwtTokenManager {

    @Override
    public boolean validateAccessToken(String token) {
        // 占位实现
        return false;
    }

    @Override
    public String extractUsername(String token) {
        // 占位实现
        return null;
    }

    @Override
    public String generateAccessToken(String username) {
        // 占位实现
        return null;
    }

    @Override
    public String generateRefreshToken(String username) {
        // 占位实现
        return null;
    }

    @Override
    public boolean validateRefreshToken(String token) {
        // 占位实现
        return false;
    }

    @Override
    public String extractUsernameFromRefreshToken(String refreshToken) {
        // 占位实现
        return null;
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        // 占位实现
        return null;
    }
}
