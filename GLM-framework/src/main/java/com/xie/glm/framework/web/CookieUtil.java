package com.xie.glm.framework.web;

import com.xie.glm.framework.config.CookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Cookie 工具类
 *
 * <p>提供 HttpOnly Cookie 的创建、删除和读取功能。
 *
 * @author xie
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final CookieProperties cookieProperties;

    /**
     * 添加 HttpOnly Cookie 到响应
     *
     * <p>用于设置 Refresh Token Cookie，包含安全属性：
     * <ul>
     *   <li>HttpOnly: true（防止 XSS 窃取）</li>
     *   <li>Secure: 根据配置（生产环境建议 true）</li>
     *   <li>SameSite: 根据配置（防止 CSRF）</li>
     *   <li>Path: /（全应用有效）</li>
     *   <li>Max-Age: 7 天（与 refreshTokenExpiration 一致）</li>
     * </ul>
     *
     * @param response HTTP 响应
     * @param value    Cookie 值
     */
    public void addHttpOnlyCookie(HttpServletResponse response, String value) {
        Cookie cookie = new Cookie(cookieProperties.getName(), value);
        cookie.setHttpOnly(cookieProperties.isHttpOnly());
        cookie.setSecure(cookieProperties.isSecure());
        cookie.setPath(cookieProperties.getPath());
        cookie.setMaxAge((int) cookieProperties.getMaxAge());
        cookie.setAttribute("SameSite", cookieProperties.getSameSite());
        response.addCookie(cookie);
        log.debug("已添加 HttpOnly Cookie: name={}, maxAge={}秒", cookieProperties.getName(), cookieProperties.getMaxAge());
    }

    /**
     * 清除 Cookie
     *
     * <p>通过设置 Max-Age 为 0 来删除 Cookie。
     *
     * @param response HTTP 响应
     */
    public void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieProperties.getName(), null);
        cookie.setPath(cookieProperties.getPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        log.debug("已清除 Cookie: name={}", cookieProperties.getName());
    }

    /**
     * 从请求中获取 Cookie 值
     *
     * @param request HTTP 请求
     * @return Cookie 值，如果不存在则返回 null
     */
    public String getCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieProperties.getName().equals(cookie.getName())) {
                log.debug("获取到 Cookie: name={}", cookieProperties.getName());
                return cookie.getValue();
            }
        }
        log.debug("未找到 Cookie: name={}", cookieProperties.getName());
        return null;
    }

    /**
     * 检查请求中是否存在指定的 Cookie
     *
     * @param request HTTP 请求
     * @return 如果 Cookie 存在则返回 true，否则返回 false
     */
    public boolean hasCookie(HttpServletRequest request) {
        return getCookieValue(request) != null;
    }
}
