package com.xie.glm.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xie.glm.common.core.Result;
import com.xie.glm.common.enums.BusinessStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权限访问处理器
 *
 * <p>当用户已认证但权限不足时，返回统一响应格式。
 * <p>响应格式：HTTP 200, {"code": 10002, "message": "无权访问", "data": null}
 *
 * @author xie
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(CONTENT_TYPE);

        Result<Void> result = Result.fail(BusinessStatus.FORBIDDEN.getCode(), BusinessStatus.FORBIDDEN.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
