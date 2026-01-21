package com.xie.glm.framework.security;

import com.xie.glm.common.dto.TokenPayload;
import com.xie.glm.common.dto.UserPermissions;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * JWT 认证过滤器测试
 *
 * <p>测试 JWT 认证过滤器的各种场景：
 * <ul>
 *   <li>没有提供 token - 应继续过滤器链</li>
 *   <li>无效的 token - 应继续过滤器链</li>
 *   <li>有效的 token - 缓存命中时应设置认证</li>
 *   <li>有效的 token - 缓存未命中时应降级到数据库查询</li>
 *   <li>旧格式 token - 向后兼容</li>
 * </ul>
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JWT 认证过滤器测试")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenManager jwtTokenManager;

    @Mock
    private UserPermissionCache permissionCache;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenManager, permissionCache, userDetailsService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("没有提供 token - 应继续过滤器链而不设置认证")
    void testDoFilterInternal_NoToken_ShouldContinueFilterChain() throws ServletException, IOException {
        // Given: 请求中没有 Authorization header
        request.setRequestURI("/api/test");

        // When: 执行过滤器
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then: 应该调用 filterChain.doFilter
        verify(filterChain).doFilter(request, response);

        // And: 不应该设置认证
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
            "SecurityContext 应该为空");

        // And: 不应该调用任何依赖
        verifyNoInteractions(jwtTokenManager, permissionCache, userDetailsService);
    }

    @Test
    @DisplayName("Authorization header 格式错误 - 应继续过滤器链")
    void testDoFilterInternal_InvalidAuthorizationFormat_ShouldContinueFilterChain()
            throws ServletException, IOException {
        // Given: Authorization header 格式错误
        request.addHeader("Authorization", "InvalidFormat token123");
        request.setRequestURI("/api/test");

        // When: 执行过滤器
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then: 应该调用 filterChain.doFilter
        verify(filterChain).doFilter(request, response);

        // And: 不应该设置认证
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
            "SecurityContext 应该为空");

        // And: 不应该调用任何依赖
        verifyNoInteractions(jwtTokenManager, permissionCache, userDetailsService);
    }

    @Test
    @DisplayName("Token 验证失败 - 应继续过滤器链")
    void testDoFilterInternal_InvalidToken_ShouldContinueFilterChain()
            throws ServletException, IOException {
        // Given: 有效的 Authorization header，但 token 无效
        request.addHeader("Authorization", "Bearer invalid-token");
        request.setRequestURI("/api/test");

        when(jwtTokenManager.validateAccessToken(anyString())).thenReturn(false);

        // When: 执行过滤器
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then: 应该调用 filterChain.doFilter
        verify(filterChain).doFilter(request, response);

        // And: 不应该设置认证
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
            "SecurityContext 应该为空");

        // And: 应该调用验证方法
        verify(jwtTokenManager).validateAccessToken("invalid-token");

        // And: 不应该调用其他方法
        verify(jwtTokenManager, never()).extractTokenPayload(anyString());
        verifyNoInteractions(permissionCache, userDetailsService);
    }

    @Test
    @DisplayName("有效的 token - 缓存命中时应设置认证到 SecurityContext")
    void testDoFilterInternal_ValidToken_CacheHit_ShouldSetAuthentication()
            throws ServletException, IOException {
        // Given: 有效的 token 且缓存命中
        String token = "valid-jwt-token";
        Long userId = 1L;
        String username = "admin";
        Long deptId = 100L;
        Integer dataScope = 3;

        TokenPayload payload = new TokenPayload(userId, username, deptId, dataScope);
        UserPermissions permissions = new UserPermissions(
            List.of("admin", "common"),
            List.of("system:user:list", "system:user:add"),
            true
        );

        request.addHeader("Authorization", "Bearer " + token);
        request.setRemoteAddr("192.168.1.100");

        when(jwtTokenManager.validateAccessToken(token)).thenReturn(true);
        when(jwtTokenManager.extractTokenPayload(token)).thenReturn(payload);
        when(permissionCache.getPermissions(userId)).thenReturn(permissions);

        // When: 执行过滤器
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then: 应该调用所有必要的方法
        verify(jwtTokenManager).validateAccessToken(token);
        verify(jwtTokenManager).extractTokenPayload(token);
        verify(permissionCache).getPermissions(userId);

        // And: 应该设置认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication, "Authentication 应该被设置");

        assertTrue(authentication.getPrincipal() instanceof CustomUserDetails,
            "Principal 应该是 CustomUserDetails");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        assertEquals(userId, userDetails.getUserId(), "userId 应该匹配");
        assertEquals(username, userDetails.getUsername(), "username 应该匹配");
        assertEquals(deptId, userDetails.getDeptId(), "deptId 应该匹配");
        assertEquals(dataScope, userDetails.getDataScope(), "dataScope 应该匹配");

        // And: 应该包含 authorities
        assertEquals(4, authentication.getAuthorities().size(),
            "应该包含角色和权限（2个角色前缀 + 2个权限）");

        // And: 应该包含 details
        assertNotNull(authentication.getDetails(),
            "Details 应该被设置");

        // And: 应该调用 filterChain.doFilter
        verify(filterChain).doFilter(request, response);

        // And: 不应该调用 userDetailsService（缓存命中）
        verifyNoInteractions(userDetailsService);
    }

    @Test
    @DisplayName("有效的 token - 缓存未命中时应降级到数据库查询")
    void testDoFilterInternal_ValidToken_CacheMiss_ShouldFallbackToDatabase()
            throws ServletException, IOException {
        // Given: 有效的 token 但缓存未命中
        String token = "valid-jwt-token";
        Long userId = 1L;
        String username = "admin";
        Long deptId = 100L;
        Integer dataScope = 3;

        TokenPayload payload = new TokenPayload(userId, username, deptId, dataScope);
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUserId(userId);
        userDetails.setUsername(username);
        setRolesViaReflection(userDetails, List.of("admin"));
        setPermissionsViaReflection(userDetails, List.of("system:user:list"));
        userDetails.setEnabled(true);

        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenManager.validateAccessToken(token)).thenReturn(true);
        when(jwtTokenManager.extractTokenPayload(token)).thenReturn(payload);
        when(permissionCache.getPermissions(userId)).thenReturn(null); // 缓存未命中
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // When: 执行过滤器
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then: 应该调用所有必要的方法
        verify(jwtTokenManager).validateAccessToken(token);
        verify(jwtTokenManager).extractTokenPayload(token);
        verify(permissionCache).getPermissions(userId);
        verify(userDetailsService).loadUserByUsername(username);

        // And: 应该设置认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication, "Authentication 应该被设置");

        // And: 应该调用 filterChain.doFilter
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("旧格式 token - 应向后兼容并设置认证")
    void testDoFilterInternal_LegacyToken_ShouldSetAuthentication()
            throws ServletException, IOException {
        // Given: 旧格式 token（extractTokenPayload 返回 null）
        String token = "legacy-jwt-token";
        String username = "admin";

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(username);
        setRolesViaReflection(userDetails, List.of("admin"));
        setPermissionsViaReflection(userDetails, List.of("system:user:list"));
        userDetails.setEnabled(true);

        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenManager.validateAccessToken(token)).thenReturn(true);
        when(jwtTokenManager.extractTokenPayload(token)).thenReturn(null); // 旧格式
        when(jwtTokenManager.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // When: 执行过滤器
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then: 应该调用旧格式的处理逻辑
        // validateAccessToken 被调用两次（主方法一次 + handleLegacyToken 一次）
        verify(jwtTokenManager, times(2)).validateAccessToken(token);
        verify(jwtTokenManager).extractTokenPayload(token);
        verify(jwtTokenManager).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(username);

        // And: 应该设置认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication, "Authentication 应该被设置");
        assertEquals(username, authentication.getName(), "username 应该匹配");

        // And: 应该调用 filterChain.doFilter
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("已认证用户 - 不重复设置认证")
    void testDoFilterInternal_AlreadyAuthenticated_ShouldNotResetAuthentication()
            throws ServletException, IOException {
        // Given: 用户已经认证
        String token = "valid-jwt-token";

        // 模拟已存在的认证
        Authentication existingAuth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        request.addHeader("Authorization", "Bearer " + token);

        // When: 执行过滤器
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then: 不应该加载新的用户详情
        verifyNoInteractions(userDetailsService, permissionCache);

        // And: 认证应该保持不变
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication, "Authentication 应该仍然存在");
        assertSame(existingAuth, authentication, "Authentication 应该是同一个实例");

        // And: 应该调用 filterChain.doFilter
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Token 验证通过但用户不存在 - 不设置认证")
    void testDoFilterInternal_ValidTokenButUserNotFound_ShouldNotSetAuthentication()
            throws ServletException, IOException {
        // Given: token 有效但用户不存在
        String token = "valid-jwt-token";
        Long userId = 1L;
        String username = "nonexistent";

        TokenPayload payload = new TokenPayload(userId, username, null, 5);

        request.addHeader("Authorization", "Bearer " + token);

        when(jwtTokenManager.validateAccessToken(token)).thenReturn(true);
        when(jwtTokenManager.extractTokenPayload(token)).thenReturn(payload);
        when(permissionCache.getPermissions(userId)).thenReturn(null);
        when(userDetailsService.loadUserByUsername(username))
            .thenThrow(new org.springframework.security.core.userdetails.UsernameNotFoundException(
                "用户不存在: " + username));

        // When: 执行过滤器
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then: 应该继续过滤器链
        verify(filterChain).doFilter(request, response);

        // And: 不应该设置认证
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
            "SecurityContext 应该为空");
    }

    // Helper methods to work with CustomUserDetails private fields

    private void setRolesViaReflection(CustomUserDetails userDetails, List<String> roles) {
        try {
            var field = CustomUserDetails.class.getDeclaredField("roles");
            field.setAccessible(true);
            field.set(userDetails, roles);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPermissionsViaReflection(CustomUserDetails userDetails, List<String> permissions) {
        try {
            var field = CustomUserDetails.class.getDeclaredField("permissions");
            field.setAccessible(true);
            field.set(userDetails, permissions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
