package com.xie.glm.framework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * CORS 配置类
 *
 * <p>配置跨域资源共享策略。
 *
 * @author xie
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final CorsProperties corsProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();

        // 根据配置决定是否启用 CORS
        if (!corsProperties.isEnabled()) {
            // 禁用 CORS
            return new UrlBasedCorsConfigurationSource();
        }

        // 配置允许的源
        configuration.setAllowedOriginPatterns(corsProperties.getAllowedOriginPatterns());

        // 配置允许的方法
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());

        // 配置允许的请求头
        configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());

        // 配置暴露的响应头
        configuration.setExposedHeaders(corsProperties.getExposedHeaders());

        // 配置是否允许携带凭证
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());

        // 配置预检请求的有效期
        configuration.setMaxAge(corsProperties.getMaxAge());

        // 注册路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
