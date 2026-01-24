package com.xie.glm.admin.controller;

import com.xie.glm.admin.dto.RefreshTokenRequest;
import com.xie.glm.common.dto.LoginRequest;
import com.xie.glm.common.dto.LoginResponse;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.framework.security.JwtTokenManager;
import com.xie.glm.framework.web.CookieUtil;
import com.xie.glm.system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 *
 * <p>提供用户认证相关的 REST API：
 * <ul>
 *   <li>登录：POST /api/auth/login</li>
 *   <li>刷新令牌：POST /api/auth/refresh</li>
 *   <li>登出：POST /api/auth/logout</li>
 *   <li>检查Token：POST /api/auth/check</li>
 *   <li>强制用户下线：POST /api/auth/force-logout/user</li>
 *   <li>强制设备下线：POST /api/auth/force-logout/device</li>
 * </ul>
 *
 * <p>认证流程：
 * <ol>
 *   <li>用户使用用户名和密码登录</li>
 *   <li>认证成功后返回 Access Token（响应体）</li>
 *   <li>Access Token 用于后续 API 访问认证</li>
 *   <li>Token 刷新时，前端携带过期的 Access Token 到刷新接口</li>
 *   <li>Token 信息存储在 Token 注册表中，支持撤销和刷新</li>
 * </ol>
 *
 * <p>注意：返回值由 ResponseAdvice 自动包装为 {@link com.xie.glm.common.core.Result} 格式
 *
 * @author xie
 */
@Tag(name = "认证管理", description = "用户登录、刷新令牌、登出接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;

    /**
     * 用户登录
     *
     * <p>使用用户名和密码进行认证，成功后：
     * <ul>
     *   <li>响应体返回 Access Token</li>
     *   <li>HttpOnly Cookie 设置 Refresh Token</li>
     *   <li>Token 对信息注册到 Token 注册表</li>
     * </ul>
     *
     * @param request  登录请求（包含用户名和密码）
     * @return 登录响应（包含 Access Token），由 ResponseAdvice 自动包装
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码登录，Access Token 返回在响应体，Refresh Token 设置在 HttpOnly Cookie")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        // 当前简化实现：只返回 Access Token
        return authService.login(request);
    }

    @PostMapping("/check")
    @Operation(summary = "检查Token是否有效", description = "检查Token是否有效，返回Boolean值")
    public Boolean checkToken(HttpServletRequest request) {
        // 从请求头中获取 Token
        String token = request.getHeader("Authorization");
        if (token == null) {
            return false;
        }
        // 验证 Token
        return jwtTokenManager.validateAccessToken(token);
    }

    /**
     * 刷新令牌
     *
     * <p>接收前端携带的过期 Access Token，返回新的 Access Token。
     *
     * @param request 刷新令牌请求（包含过期的 Access Token）
     * @return 刷新令牌响应（包含新的 Access Token），由 ResponseAdvice 自动包装
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "使用过期的 Access Token 刷新，返回新的 Access Token")
    public LoginResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        // 调用 AuthService 刷新 Token（内部会更新 Token 注册表）
        return authService.refreshToken(request.accessToken());
    }

    /**
     * 用户登出
     *
     * <p>撤销当前 Access Token。
     *
     * @param request HTTP 请求（从 Header 中获取 Access Token）
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "撤销当前 Access Token")
    public void logout(HttpServletRequest request) {
        // 从 Header 中获取 Access Token
        String token = extractBearerToken(request);
        if (token != null) {
            authService.logout(token);
        }
    }

    /**
     * 强制用户下线
     *
     * <p>撤销指定用户的所有 Token，用于管理员强制用户下线。
     *
     * @param userId 用户 ID
     * @param reason 下线原因（可选）
     * @return 撤销的 Token 数量，由 ResponseAdvice 自动包装
     */
    @PostMapping("/force-logout/user")
    @Operation(summary = "强制用户下线", description = "撤销指定用户的所有 Token，强制用户下线")
    @PreAuthorize("hasAuthority('system:auth:forceLogout')")
    public int forceLogoutUser(@RequestParam Long userId,
                                @RequestParam(defaultValue = "管理员强制下线") String reason) {
        return authService.forceLogoutUser(userId, reason);
    }

    /**
     * 强制设备下线
     *
     * <p>撤销指定用户在指定设备上的所有 Token。
     *
     * @param userId 用户 ID
     * @param deviceFingerprint 设备指纹
     * @param reason 下线原因（可选）
     * @return 撤销的 Token 数量，由 ResponseAdvice 自动包装
     */
    @PostMapping("/force-logout/device")
    @Operation(summary = "强制设备下线", description = "撤销指定用户在指定设备上的所有 Token")
    @PreAuthorize("hasAuthority('system:auth:forceLogout')")
    public int forceLogoutDevice(@RequestParam Long userId,
                                  @RequestParam String deviceFingerprint,
                                  @RequestParam(defaultValue = "管理员强制设备下线") String reason) {
        return authService.forceLogoutDevice(userId, deviceFingerprint, reason);
    }

    /**
     * 从请求头中提取 Bearer Token
     *
     * @param request HTTP 请求
     * @return Token 字符串，如果不存在返回 null
     */
    private String extractBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
