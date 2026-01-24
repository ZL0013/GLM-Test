package com.xie.glm.framework.config;

import lombok.Data;
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
@Data
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
     * 允许的源模式
     * <p>支持精确匹配、通配符和模式（如 http://localhost:*）
     */
    private List<String> allowedOriginPatterns = Arrays.asList("*");

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
}
