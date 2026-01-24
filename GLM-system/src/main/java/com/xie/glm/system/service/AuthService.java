package com.xie.glm.system.service;

import com.xie.glm.common.dto.LoginRequest;
import com.xie.glm.common.dto.LoginResponse;
import com.xie.glm.common.dto.TokenPayload;
import com.xie.glm.common.dto.UserPermissions;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.framework.security.CustomUserDetails;
import com.xie.glm.framework.security.JwtTokenManager;
import com.xie.glm.framework.security.UserPermissionCache;
import com.xie.glm.system.domain.SysTokenRegistry;
import com.xie.glm.system.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;

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
    private final ITokenRegistryService tokenRegistryService;

    /**
     * 用户登录
     *
     * <p>认证流程：
     * <ol>
     *   <li>使用 Spring Security 进行认证</li>
     *   <li>从认证结果中获取 CustomUserDetails</li>
     *   <li>构造 TokenPayload（包含 userId、username、deptId、dataScope）</li>
     *   <li>生成 Access Token 和 Refresh Token</li>
     *   <li>注册 Token 对到 Token 注册表</li>
     *   <li>缓存用户权限到 Redis（TTL 与 Access Token 相同）</li>
     *   <li>返回登录响应</li>
     * </ol>
     *
     * @param request 登录请求
     * @return 登录响应，Refresh Token 通过 HttpOnly Cookie 返回
     */
    @Transactional(rollbackFor = Exception.class)
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

        // 5. 计算 Token 过期时间
        LocalDateTime accessExprAt = calculateAccessExprAt();
        LocalDateTime refreshExprAt = calculateRefreshExprAt();

        // 6. 注册 Token 对到 Token 注册表
        String accessTokenId = jwtTokenManager.extractAccessTokenId(accessToken);
        String refreshTokenHash = hashRefreshToken(refreshToken);
        String deviceFingerprint = request.deviceFingerprint() != null ? request.deviceFingerprint() : "unknown";

        SysTokenRegistry registry = new SysTokenRegistry();
        registry.setAccessTokenId(accessTokenId);
        registry.setRefreshTokenHash(refreshTokenHash);
        registry.setUserId(userDetails.getUserId());
        registry.setUsername(userDetails.getUsername());
        registry.setDeviceFingerprint(deviceFingerprint);
        registry.setAccessExprAt(accessExprAt);
        registry.setRefreshExprAt(refreshExprAt);

        tokenRegistryService.registerToken(registry);
        log.debug("Token 对已注册: accessTokenId={}, userId={}, device={}", accessTokenId, userDetails.getUserId(), deviceFingerprint);

        // 7. 缓存用户权限到 Redis（TTL = 30分钟 = 1800秒）
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

        // 8. 返回登录响应（Refresh Token 通过 HttpOnly Cookie 返回）
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
     * <p>使用过期的 Access Token 刷新令牌：
     * <ol>
     *   <li>从过期的 Access Token 中提取 accessTokenId（验证签名但不验证过期时间）</li>
     *   <li>通过 accessTokenId 查询 Token 注册表</li>
     *   <li>验证 Refresh Token 是否有效（未撤销且未过期）</li>
     *   <li>生成新的 Access Token</li>
     *   <li>更新 Token 注册表</li>
     *   <li>更新缓存</li>
     * </ol>
     *
     * @param expiredAccessToken 过期的 Access Token
     * @return 新的登录响应，仅包含 Access Token
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse refreshToken(String expiredAccessToken) {
        // 1. 从过期的 Access Token 中提取 accessTokenId（验证签名，但不验证过期时间）
        String accessTokenId = jwtTokenManager.extractAccessTokenIdFromExpiredToken(expiredAccessToken);
        if (accessTokenId == null) {
            throw new ServiceException(BusinessStatus.ACCESS_TOKEN_INVALID);
        }

        // 2. 查询 Token 注册表
        SysTokenRegistry registry = tokenRegistryService.getByAccessTokenId(accessTokenId);
        if (registry == null) {
            throw new ServiceException(BusinessStatus.TOKEN_NOT_FOUND);
        }

        // 3. 验证 Refresh Token 是否过期
        if (registry.getRefreshExprAt().isBefore(LocalDateTime.now())) {
            throw new ServiceException(BusinessStatus.TOKEN_EXPIRED);
        }

        // 4. 重新加载用户详情
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(registry.getUsername());

        // 5. 构造 TokenPayload
        TokenPayload payload = new TokenPayload(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getDeptId(),
                userDetails.getDataScope()
        );

        // 6. 生成新的 Access Token
        String newAccessToken = jwtTokenManager.generateAccessToken(payload);
        String newAccessTokenId = jwtTokenManager.extractAccessTokenId(newAccessToken);
        LocalDateTime newAccessExprAt = calculateAccessExprAt();

        // 7. 更新 Token 注册表
        tokenRegistryService.refreshTokenByAccessTokenId(accessTokenId, newAccessTokenId, newAccessExprAt);
        log.debug("Token 已刷新: oldAccessTokenId={}, newAccessTokenId={}, userId={}",
                accessTokenId, newAccessTokenId, userDetails.getUserId());

        // 8. 更新缓存
        UserPermissions permissions = new UserPermissions(
                userDetails.getRoles(),
                userDetails.getPermissions(),
                userDetails.isEnabled()
        );
        permissionCache.cachePermissions(userDetails.getUserId(), permissions, 1800L);

        return LoginResponse.of(newAccessToken);
    }

    /**
     * 用户登出
     *
     * <p>撤销当前 Access Token 对应的 Token 对。
     *
     * @param accessToken Access Token
     */
    @Transactional(rollbackFor = Exception.class)
    public void logout(String accessToken) {
        String accessTokenId = jwtTokenManager.extractAccessTokenId(accessToken);
        if (accessTokenId != null) {
            tokenRegistryService.revokeToken(accessTokenId, "用户登出");
            log.debug("Token 已撤销: accessTokenId={}", accessTokenId);
        }

        // 清除用户权限缓存
        String username = jwtTokenManager.extractUsername(accessToken);
        if (username != null) {
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
            permissionCache.evictPermissions(userDetails.getUserId());
            log.debug("用户权限缓存已清除: userId={}", userDetails.getUserId());
        }
    }

    /**
     * 强制用户下线（撤销所有 Token）
     *
     * @param userId 用户 ID
     * @param reason 撤销原因
     * @return 撤销的 Token 数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int forceLogoutUser(Long userId, String reason) {
        int count = tokenRegistryService.revokeAllUserTokens(userId, reason);

        // 清除用户权限缓存
        permissionCache.evictPermissions(userId);
        log.info("用户已强制下线: userId={}, 撤销Token数量={}", userId, count);

        return count;
    }

    /**
     * 强制设备下线（撤销指定设备的 Token）
     *
     * @param userId 用户 ID
     * @param deviceFingerprint 设备指纹
     * @param reason 撤销原因
     * @return 撤销的 Token 数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int forceLogoutDevice(Long userId, String deviceFingerprint, String reason) {
        int count = tokenRegistryService.revokeDeviceTokens(userId, deviceFingerprint, reason);
        log.info("设备已强制下线: userId={}, device={}, 撤销Token数量={}", userId, deviceFingerprint, count);
        return count;
    }

    // ========== 辅助方法 ==========

    /**
     * 计算 Access Token 过期时间
     *
     * @return 过期时间（当前时间 + 30分钟）
     */
    private LocalDateTime calculateAccessExprAt() {
        return LocalDateTime.now().plusMinutes(30);
    }

    /**
     * 计算 Refresh Token 过期时间
     *
     * @return 过期时间（当前时间 + 7天）
     */
    private LocalDateTime calculateRefreshExprAt() {
        return LocalDateTime.now().plusDays(7);
    }

    /**
     * 计算 Refresh Token 的 SHA-256 哈希值
     *
     * @param refreshToken Refresh Token
     * @return SHA-256 哈希值（十六进制字符串）
     */
    private String hashRefreshToken(String refreshToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new ServiceException("计算 Refresh Token 哈希值失败", e);
        }
    }
}
