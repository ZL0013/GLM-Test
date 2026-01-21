package com.xie.glm.framework.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * XSS 过滤器
 *
 * <p>在请求处理之前清理参数中的 XSS 内容。
 * <p>通过包装 HttpServletRequest 来实现参数的 XSS 清理。
 *
 * @author xie
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class XssFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 包装请求，清理参数
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(request);

        // 继续过滤器链
        filterChain.doFilter(xssRequest, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 对于文件上传请求，不进行 XSS 过滤
        String contentType = request.getContentType();
        return contentType != null && contentType.startsWith("multipart/");
    }
}
