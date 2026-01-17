package com.xie.glm.framework.web;

import com.xie.glm.common.core.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 响应包装处理器
 *
 * <p>使用 {@link ResponseBodyAdvice} 自动包装 Controller 返回值为统一的 {@link Result} 格式。
 *
 * <p>处理逻辑：
 * <ul>
 *   <li>如果返回值已经是 {@link Result} 类型，直接返回不重复包装</li>
 *   <li>如果返回值为 null，包装为成功响应（无数据）</li>
 *   <li>其他情况包装为成功响应（带数据）</li>
 * </ul>
 *
 * <p>设计原则：
 * <ul>
 *   <li>符合宪法第四条：全局响应包装，HTTP 状态码固定 200，业务状态通过 code 判断</li>
 *   <li>简单性：一个类处理所有响应包装，避免过度抽象</li>
 *   <li>明确性：自动包装，Controller 无需手动包装返回值</li>
 * </ul>
 *
 * @author xie
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要处理响应
     *
     * <p>本类支持所有返回值类型，始终返回 true。
     *
     * @param returnType    返回值类型
     * @param converterType 消息转换器类型
     * @return true，支持所有类型
     */
    @Override
    public boolean supports(MethodParameter returnType,
                           Class<? extends HttpMessageConverter<?>> converterType) {
        // 支持所有返回值类型
        return true;
    }

    /**
     * 在响应体写入前进行处理
     *
     * <p>将 Controller 返回值自动包装为 {@link Result} 格式。
     *
     * @param body                  原始返回值
     * @param returnType            返回值类型
     * @param selectedContentType   选择的内容类型
     * @param selectedConverterType 选择的转换器类型
     * @param request               HTTP 请求
     * @param response              HTTP 响应
     * @return 包装后的 Result 对象，如果已经是 Result 类型则直接返回
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 如果已经是 Result 类型，直接返回不重复包装
        if (body instanceof Result<?>) {
            return body;
        }

        // 包装为统一响应格式
        return Result.success(body);
    }
}
