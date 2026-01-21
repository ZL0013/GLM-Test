package com.xie.glm.admin.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 *
 * <p>提供用户认证相关的 REST API：
 * <ul>
 *   <li>登录：POST /api/auth/login</li>
 *   <li>刷新令牌：POST /api/auth/refresh</li>
 *   <li>登出：POST /api/auth/logout</li>
 * </ul>
 *
 * <p>认证流程：
 * <ol>
 *   <li>用户使用用户名和密码登录</li>
 *   <li>认证成功后返回 Access Token（响应体）和 Refresh Token（HttpOnly Cookie）</li>
 *   <li>Access Token 用于后续 API 访问认证</li>
 *   <li>Refresh Token 存储在 HttpOnly Cookie 中，用于获取新的 Access Token</li>
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
    private final CookieUtil cookieUtil;

    /**
     * 用户登录
     *
     * <p>使用用户名和密码进行认证，成功后：
     * <ul>
     *   <li>响应体返回 Access Token</li>
     *   <li>HttpOnly Cookie 设置 Refresh Token</li>
     * </ul>
     *
     * @param request  登录请求（包含用户名和密码）
     * @param response HTTP 响应（用于设置 Cookie）
     * @return 登录响应（包含 Access Token），由 ResponseAdvice 自动包装
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码登录，Access Token 返回在响应体，Refresh Token 设置在 HttpOnly Cookie")
    public LoginResponse login(@Valid @RequestBody LoginRequest request,
                               HttpServletResponse response) {
        // 获取 Access Token
        String accessToken = authService.loginForAccessToken(request);
        // 生成 Refresh Token
        String refreshToken = authService.generateRefreshToken(request.username());
        // 设置 Refresh Token 到 HttpOnly Cookie
        cookieUtil.addHttpOnlyCookie(response, refreshToken);
        return LoginResponse.of(accessToken);
    }

    /**
     * 刷新令牌
     *
     * <p>从 HttpOnly Cookie 中读取 Refresh Token，返回新的 Access Token。
     *
     * @param request HTTP 请求（从 Cookie 中读取 Refresh Token）
     * @return 刷新令牌响应（包含新的 Access Token），由 ResponseAdvice 自动包装
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "从 HttpOnly Cookie 中读取 Refresh Token，返回新的 Access Token")
    public LoginResponse refreshToken(HttpServletRequest request) {
        // 从 Cookie 中获取 Refresh Token
        String refreshToken = cookieUtil.getCookieValue(request);
        if (refreshToken == null) {
            throw new ServiceException(BusinessStatus.UNAUTHORIZED);
        }

        // 验证 Refresh Token
        if (!jwtTokenManager.validateRefreshToken(refreshToken)) {
            throw new ServiceException(BusinessStatus.UNAUTHORIZED);
        }

        // 从 Refresh Token 中提取用户名
        String username = jwtTokenManager.extractUsernameFromRefreshToken(refreshToken);
        if (username == null) {
            throw new ServiceException(BusinessStatus.UNAUTHORIZED);
        }

        // 生成新的 Access Token
        String newAccessToken = jwtTokenManager.generateAccessToken(username);
        return LoginResponse.of(newAccessToken);
    }

    /**
     * 用户登出
     *
     * <p>清除 HttpOnly Cookie 中的 Refresh Token。
     *
     * @param response HTTP 响应（用于清除 Cookie）
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "清除 HttpOnly Cookie 中的 Refresh Token")
    public void logout(HttpServletResponse response) {
        // 清除 Refresh Token Cookie
        cookieUtil.clearCookie(response);
    }
}
