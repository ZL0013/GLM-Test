package com.xie.glm.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性
 *
 * <p>从 application.yml 中读取 JWT 相关配置：
 * <ul>
 *   <li>secret: JWT 签名密钥</li>
 *   <li>access-token-expiration: Access Token 过期时间（毫秒）</li>
 *   <li>refresh-token-expiration: Refresh Token 过期时间（毫秒）</li>
 * </ul>
 *
 * @author xie
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT 签名密钥
     * <p>生产环境必须使用强密钥，建议使用环境变量注入
     */
    private String secret;

    /**
     * Access Token 过期时间（毫秒）
     * <p>默认 30 分钟
     */
    private Long accessTokenExpiration = 1800000L;

    /**
     * Refresh Token 过期时间（毫秒）
     * <p>默认 7 天
     */
    private Long refreshTokenExpiration = 604800000L;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(Long accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(Long refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}
