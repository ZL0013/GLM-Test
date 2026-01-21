package com.xie.glm.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * CORS 配置属性
 *
 * <p>从 application.yml 中读取 CORS 相关配置。
 *
 * @author xie
 */
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    /**
     * 是否启用 CORS
     * <p>默认启用，生产环境可通过环境变量控制
     */
    private boolean enabled = true;

    /**
     * 允许的源
     * <p>默认允许所有源，生产环境应该配置具体的域名
     */
    private List<String> allowedOrigins = Arrays.asList("*");

    /**
     * 允许的 HTTP 方法
     */
    private List<String> allowedMethods = Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");

    /**
     * 允许的请求头
     */
    private List<String> allowedHeaders = Arrays.asList("*");

    /**
     * 允许的响应头
     */
    private List<String> exposedHeaders = Arrays.asList("Authorization", "Content-Type");

    /**
     * 是否允许携带凭证
     * <p>默认为 true，以支持 HttpOnly Cookie（Refresh Token）
     */
    private boolean allowCredentials = true;

    /**
     * 预检请求的有效期（秒）
     */
    private long maxAge = 3600L;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public List<String> getExposedHeaders() {
        return exposedHeaders;
    }

    public void setExposedHeaders(List<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }
}
