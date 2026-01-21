package com.xie.glm.framework.web;

import com.xie.glm.common.util.XssCleaner;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * XSS 请求包装器
 *
 * <p>包装 HttpServletRequest，在获取参数时自动进行 XSS 清理。
 *
 * @author xie
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return XssCleaner.clean(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        return XssCleaner.clean(values);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return XssCleaner.clean(value);
    }
}
