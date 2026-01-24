package com.xie.glm.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xie.glm.common.core.Result;
import com.xie.glm.common.enums.BusinessStatus;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * JWT 认证入口点
 *
 * <p>当用户未认证或令牌无效时，返回统一响应格式。
 * <p>响应格式：HTTP 200, {"code": 10001, "message": "未认证或令牌已过期", "data": null}
 *
 * @author xie
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(CONTENT_TYPE);
        Cookie[] cookie = request.getCookies();
        for (Cookie cookie1 : cookie) {
            log.info("request.getCookies(): {}", cookie1.getName());
            log.info("request.getCookies(): {}", cookie1.getValue());
        }
        Result<Void> result = Result.fail(BusinessStatus.UNAUTHORIZED.getCode(), BusinessStatus.UNAUTHORIZED.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
