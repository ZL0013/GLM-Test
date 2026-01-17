package com.xie.glm.framework.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器
 *
 * <p>该过滤器在每个请求执行一次，负责：
 * <ul>
 *   <li>从请求头中提取 JWT token</li>
 *   <li>验证 token 有效性</li>
 *   <li>从 token 中提取用户名并加载用户详情</li>
 *   <li>将认证信息设置到 SecurityContext</li>
 * </ul>
 *
 * <p>注意：
 * <ul>
 *   <li>继承 {@link OncePerRequestFilter} 确保每个请求只执行一次</li>
 *   <li>如果 token 无效或不存在，不会阻塞请求，而是继续过滤器链</li>
 *   <li>如果用户已经认证，不会重复加载用户详情</li>
 * </ul>
 *
 * @author xie
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final UserDetailsService userDetailsService;

    /**
     * 从请求头中提取并验证 JWT token，如果有效则设置认证
     *
     * @param request     HTTP 请求
     * @param response    HTTP 响应
     * @param filterChain 过滤器链
     * @throws ServletException 如果发生 Servlet 异常
     * @throws IOException      如果发生 I/O 异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 从请求头中提取 token
        String token = extractToken(request);

        // 2. 如果存在 token 且有效，则设置认证
        if (token != null && jwtTokenManager.validateAccessToken(token)) {
            String username = jwtTokenManager.extractUsername(token);

            // 3. 如果用户名存在且当前未认证，则加载用户详情并设置认证
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // 4. 验证 token 仍然有效（双重验证）
                    if (jwtTokenManager.validateAccessToken(token)) {
                        // 5. 创建认证对象
                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                            );

                        // 6. 设置认证详情（包含远程地址等信息）
                        authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        // 7. 将认证设置到 SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        log.debug("设置用户认证成功: {}", username);
                    }
                } catch (Exception e) {
                    log.error("加载用户详情失败: {}", username, e);
                    // 不抛出异常，继续过滤器链，让后续处理器处理
                }
            }
        }

        // 8. 继续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中提取 JWT token
     *
     * <p>token 应该在 {@code Authorization} 请求头中，格式为：
     * {@code Bearer <token>}
     *
     * @param request HTTP 请求
     * @return JWT token，如果不存在或格式错误则返回 null
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 移除 "Bearer " 前缀
        }

        return null;
    }
}
