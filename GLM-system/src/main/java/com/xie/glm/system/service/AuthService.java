package com.xie.glm.system.service;

import com.xie.glm.common.dto.LoginRequest;
import com.xie.glm.common.dto.LoginResponse;
import com.xie.glm.common.dto.TokenPayload;
import com.xie.glm.common.dto.UserPermissions;
import com.xie.glm.framework.security.CustomUserDetails;
import com.xie.glm.framework.security.JwtTokenManager;
import com.xie.glm.framework.security.UserPermissionCache;
import com.xie.glm.system.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 认证服务
 *
 * <p>处理用户登录认证流程：
 * <ul>
 *   <li>使用 Spring Security AuthenticationManager 进行认证</li>
 *   <li>生成包含核心信息的 TokenPayload</li>
 *   <li>生成 Access Token 和 Refresh Token</li>
 *   <li>缓存用户权限到 Redis</li>
 * </ul>
 *
 * @author xie
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;
    private final UserPermissionCache permissionCache;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 用户登录
     *
     * <p>认证流程：
     * <ol>
     *   <li>使用 Spring Security 进行认证</li>
     *   <li>从认证结果中获取 CustomUserDetails</li>
     *   <li>构造 TokenPayload（包含 userId、username、deptId、dataScope）</li>
     *   <li>生成 Access Token 和 Refresh Token</li>
     *   <li>缓存用户权限到 Redis（TTL 与 Access Token 相同）</li>
     *   <li>返回登录响应</li>
     * </ol>
     *
     * @param request 登录请求
     * @return 登录响应，Refresh Token 通过 HttpOnly Cookie 返回
     */
    public LoginResponse login(LoginRequest request) {
        // 1. Spring Security 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. 获取 CustomUserDetails
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("用户登录成功: userId={}, username={}", userDetails.getUserId(), userDetails.getUsername());

        // 3. 构造 TokenPayload
        TokenPayload payload = new TokenPayload(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getDeptId(),
                userDetails.getDataScope()
        );

        // 4. 生成双 Token
        String accessToken = jwtTokenManager.generateAccessToken(payload);
        String refreshToken = jwtTokenManager.generateRefreshToken(userDetails.getUsername());

        // 5. 缓存用户权限到 Redis（TTL = 30分钟 = 1800秒）
        UserPermissions permissions = new UserPermissions(
                userDetails.getRoles(),
                userDetails.getPermissions(),
                userDetails.isEnabled()
        );
        permissionCache.cachePermissions(userDetails.getUserId(), permissions, 1800L);

        log.debug("用户权限已缓存: userId={}, roles={}, permissions={}",
                userDetails.getUserId(),
                permissions.roles().size(),
                permissions.permissions().size());

        // 6. 返回登录响应（Refresh Token 通过 HttpOnly Cookie 返回）
        return LoginResponse.of(accessToken);
    }

    /**
     * 用户登录（仅获取 Access Token）
     *
     * <p>用于 AuthController 中登录后设置 Cookie 的场景。
     *
     * @param request 登录请求
     * @return Access Token
     */
    public String loginForAccessToken(LoginRequest request) {
        // 1. Spring Security 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. 获取 CustomUserDetails
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("用户登录成功: userId={}, username={}", userDetails.getUserId(), userDetails.getUsername());

        // 3. 构造 TokenPayload
        TokenPayload payload = new TokenPayload(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getDeptId(),
                userDetails.getDataScope()
        );

        // 4. 生成 Access Token
        String accessToken = jwtTokenManager.generateAccessToken(payload);

        // 5. 缓存用户权限到 Redis（TTL = 30分钟 = 1800秒）
        UserPermissions permissions = new UserPermissions(
                userDetails.getRoles(),
                userDetails.getPermissions(),
                userDetails.isEnabled()
        );
        permissionCache.cachePermissions(userDetails.getUserId(), permissions, 1800L);

        log.debug("用户权限已缓存: userId={}, roles={}, permissions={}",
                userDetails.getUserId(),
                permissions.roles().size(),
                permissions.permissions().size());

        return accessToken;
    }

    /**
     * 生成 Refresh Token
     *
     * <p>用于登录后生成 Refresh Token 并设置到 HttpOnly Cookie。
     *
     * @param username 用户名
     * @return Refresh Token
     */
    public String generateRefreshToken(String username) {
        return jwtTokenManager.generateRefreshToken(username);
    }

    /**
     * 刷新 Access Token
     *
     * @param refreshToken Refresh Token
     * @return 新的登录响应，仅包含 Access Token
     */
    public LoginResponse refreshToken(String refreshToken) {
        // 验证 Refresh Token
        if (!jwtTokenManager.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token 无效或已过期", null);
        }

        // 提取用户名并重新加载用户详情
        String username = jwtTokenManager.extractUsernameFromRefreshToken(refreshToken);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        // 构造 TokenPayload
        TokenPayload payload = new TokenPayload(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getDeptId(),
                userDetails.getDataScope()
        );

        // 生成新的 Access Token（Refresh Token 保持不变，直到过期）
        String newAccessToken = jwtTokenManager.generateAccessToken(payload);

        // 更新缓存
        UserPermissions permissions = new UserPermissions(
                userDetails.getRoles(),
                userDetails.getPermissions(),
                userDetails.isEnabled()
        );
        permissionCache.cachePermissions(userDetails.getUserId(), permissions, 1800L);

        return LoginResponse.of(newAccessToken);
    }
}
