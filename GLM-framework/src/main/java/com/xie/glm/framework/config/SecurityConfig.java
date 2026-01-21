package com.xie.glm.framework.config;

import com.xie.glm.framework.security.CustomAccessDeniedHandler;
import com.xie.glm.framework.security.JwtAuthenticationEntryPoint;
import com.xie.glm.framework.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Spring Security 6 配置类
 *
 * <p>配置内容包括：
 * <ul>
 *   <li>无状态会话管理（JWT）</li>
 *   <li>BCrypt 密码加密</li>
 *   <li>公开接口白名单</li>
 *   <li>方法级权限校验</li>
 *   <li>异常处理（401 未认证 / 403 无权限）</li>
 * </ul>
 *
 * @author xie
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * 配置 SecurityFilterChain
     *
     * <p>Spring Security 6 使用 SecurityFilterChain Bean 替代了 WebSecurityConfigurerAdapter
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（使用 JWT 无需 CSRF）
                .csrf(AbstractHttpConfigurer::disable)

                // 配置会话管理为无状态
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 配置授权规则（Spring Security 6 新风格）
                .authorizeHttpRequests(auth -> auth
                        // 公开接口：登录、刷新令牌
                        .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()

                        // Knife4j 文档
                        .requestMatchers(
                                "/doc.html",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 健康检查
                        .requestMatchers("/actuator/health").permitAll()

                        // 其他所有请求需要认证
                        .anyRequest().authenticated()
                )

                // 配置 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // 配置异常处理
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // 添加 JWT 认证过滤器（在 UsernamePasswordAuthenticationFilter 之前）
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * BCrypt 密码编码器
     *
     * <p>默认强度为 10（4-31），越高越安全但越慢
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     *
     * <p>用于登录时进行用户认证
     *
     * @param config 认证配置
     * @return AuthenticationManager
     * @throws Exception 配置异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
