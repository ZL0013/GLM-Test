package com.xie.glm.framework.security;

import com.xie.glm.common.dto.TokenPayload;
import com.xie.glm.common.dto.UserPermissions;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * JWT 认证过滤器
 *
 * <p>该过滤器在每个请求执行一次，负责：
 * <ul>
 *   <li>从请求头中提取 JWT token</li>
 *   <li>验证 token 有效性并解析 TokenPayload</li>
 *   <li>从 Redis 缓存中获取用户详细权限</li>
 *   <li>缓存未命中时降级到 UserDetailsService 查询，并异步回填缓存</li>
 *   <li>将认证信息设置到 SecurityContext</li>
 * </ul>
 *
 * @author xie
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final UserPermissionCache permissionCache;
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

        // 2. 如果存在 token 且当前未认证，则进行认证处理
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // 3. 验证 token 并解析 TokenPayload
                if (!jwtTokenManager.validateAccessToken(token)) {
                    log.debug("Token 无效或已过期");
                    filterChain.doFilter(request, response);
                    return;
                }

                TokenPayload payload = jwtTokenManager.extractTokenPayload(token);
                if (payload == null) {
                    // 可能是旧格式 token，尝试使用旧方式处理
                    handleLegacyToken(token, request);
                    filterChain.doFilter(request, response);
                    return;
                }

                // 4. 从 Redis 缓存获取用户权限
                UserPermissions permissions = permissionCache.getPermissions(payload.userId());

                CustomUserDetails userDetails;
                if (permissions != null) {
                    // 5. 缓存命中：直接构造 CustomUserDetails
                    log.debug("权限缓存命中: userId={}", payload.userId());
                    userDetails = buildCustomUserDetails(payload, permissions);
                } else {
                    // 6. 缓存未命中：降级到 UserDetailsService 查询
                    log.debug("权限缓存未命中，降级到数据库查询: userId={}", payload.userId());
                    userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(payload.username());

                    // 7. 异步回填缓存
                    asyncFillCache(userDetails);
                }

                // 8. 创建认证对象
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                // 9. 设置认证详情（包含远程地址等信息）
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 10. 将认证设置到 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("设置用户认证成功: userId={}, username={}",
                    userDetails.getUserId(), userDetails.getUsername());
            } catch (Exception e) {
                log.error("JWT 认证失败", e);
                // 不抛出异常，继续过滤器链
            }
        }

        // 11. 继续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 处理旧格式 token（向后兼容）
     *
     * <p>旧格式 token 只包含 username，不包含 userId 等字段。
     *
     * @param token    JWT token
     * @param request  HTTP 请求
     */
    private void handleLegacyToken(String token, HttpServletRequest request) {
        String username = jwtTokenManager.extractUsername(token);
        if (username != null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtTokenManager.validateAccessToken(token)) {
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("旧格式 token 认证成功: username={}", username);
                }
            } catch (Exception e) {
                log.debug("旧格式 token 认证失败: username={}", username, e);
            }
        }
    }

    /**
     * 使用 TokenPayload 和缓存的权限构造 CustomUserDetails
     *
     * @param payload    Token 载荷
     * @param permissions 用户权限
     * @return CustomUserDetails
     */
    private CustomUserDetails buildCustomUserDetails(TokenPayload payload, UserPermissions permissions) {
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUserId(payload.userId());
        userDetails.setUsername(payload.username());
        userDetails.setDeptId(payload.deptId());
        userDetails.setDataScope(payload.dataScope());
        userDetails.setEnabled(permissions.enabled());
        userDetails.setRoles(permissions.roles());
        userDetails.setPermissions(permissions.permissions());
        return userDetails;
    }

    /**
     * 异步回填缓存
     *
     * <p>当缓存未命中时，在数据库查询后异步将权限写入缓存。
     *
     * @param userDetails 用户详情
     */
    private void asyncFillCache(CustomUserDetails userDetails) {
        CompletableFuture.runAsync(() -> {
            try {
                UserPermissions permissions = new UserPermissions(
                    userDetails.getRoles() != null ? userDetails.getRoles() : List.of(),
                    userDetails.getPermissions() != null ? userDetails.getPermissions() : List.of(),
                    userDetails.isEnabled()
                );
                permissionCache.cachePermissions(userDetails.getUserId(), permissions, 1800L);
                log.debug("异步回填缓存成功: userId={}", userDetails.getUserId());
            } catch (Exception e) {
                log.error("异步回填缓存失败: userId={}", userDetails.getUserId(), e);
            }
        });
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
