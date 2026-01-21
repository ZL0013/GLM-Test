package com.xie.glm.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Cookie 配置属性
 *
 * <p>从 application.yml 中读取 Refresh Token Cookie 相关配置。
 *
 * @author xie
 */
@Data
@Component
@ConfigurationProperties(prefix = "cookie")
public class CookieProperties {

    /**
     * Cookie 名称
     * <p>默认为 refresh_token
     */
    private String name = "refresh_token";

    /**
     * Cookie 最大有效期（秒）
     * <p>默认 7 天（604800 秒），与 refreshTokenExpiration 一致
     */
    private long maxAge = 604800L;

    /**
     * Cookie 路径
     * <p>默认为根路径 /，全应用有效
     */
    private String path = "/";

    /**
     * 是否启用 HttpOnly
     * <p>默认为 true，防止 XSS 窃取
     */
    private boolean httpOnly = true;

    /**
     * 是否仅 HTTPS
     * <p>开发环境设置为 false，生产环境应设置为 true
     */
    private boolean secure = false;

    /**
     * SameSite 属性
     * <p>防止 CSRF 攻击
     * <p>开发环境使用 Lax，生产环境建议使用 Strict
     */
    private String sameSite = "Lax";
}
