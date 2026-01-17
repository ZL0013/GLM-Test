package com.xie.glm.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 认证入口点
 *
 * <p>当用户未认证或令牌无效时，返回 401 响应。
 * <p>响应格式：{"code": 401, "message": "未认证或令牌已过期"}
 *
 * @author xie
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final int UNAUTHORIZED_CODE = 401;
    private static final String DEFAULT_MESSAGE = "未认证或令牌已过期";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CONTENT_TYPE);

        Map<String, Object> result = new HashMap<>();
        result.put("code", UNAUTHORIZED_CODE);
        result.put("message", DEFAULT_MESSAGE);

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
