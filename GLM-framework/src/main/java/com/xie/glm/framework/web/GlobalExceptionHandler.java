package com.xie.glm.framework.web;

import com.xie.glm.common.core.Result;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * <p>使用 {@link RestControllerAdvice} + {@link ExceptionHandler} 统一处理所有异常。
 *
 * <p>异常处理原则：
 * <ul>
 *   <li>业务异常：直接返回业务错误码和消息</li>
 *   <li>参数校验异常：返回字段校验错误信息</li>
 *   <li>运行时异常：记录堆栈，返回系统错误</li>
 *   <li>通用异常：记录堆栈，返回系统错误</li>
 * </ul>
 *
 * <p>设计原则：
 * <ul>
 *   <li>符合宪法第五条：全局异常处理</li>
 *   <li>符合宪法第三条：异常链传递</li>
 *   <li>符合宪法第四条：HTTP 状态码固定 200 OK，业务状态通过 code 判断</li>
 *   <li>简单性：一个类处理所有异常，直接返回 Result，避免过度包装</li>
 * </ul>
 *
 * @author xie
 */
@Hidden
@ResponseStatus(HttpStatus.OK)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     *
     * <p>业务异常继承自 {@link ServiceException}，包含业务错误码和消息。
     *
     * @param exception 业务异常
     * @return Result 响应结果，code 为业务错误码
     */
    @ExceptionHandler(ServiceException.class)
    public Result<Object> handleServiceException(ServiceException exception) {
        log.warn("业务异常: code={}, message={}", exception.getCode(), exception.getMessage());

        return Result.fail(exception.getCode(), exception.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * <p>触发条件：使用 {@link org.springframework.validation.annotation.Validated} + {@link jakarta.validation.Valid}
     * 注解进行参数校验失败时触发。
     *
     * @param exception 参数校验异常
     * @return Result 响应结果，code 为参数错误码
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("; "));

        log.warn("参数校验异常: {}", errorMessage);

        return Result.fail(BusinessStatus.PARAM_NULL.getCode(), errorMessage);
    }

    /**
     * 处理绑定异常
     *
     * <p>触发条件：表单提交绑定失败时触发。
     *
     * @param exception 绑定异常
     * @return Result 响应结果，code 为参数错误码
     */
    @ExceptionHandler(BindException.class)
    public Result<Object> handleBindException(BindException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("; "));

        log.warn("绑定异常: {}", errorMessage);

        return Result.fail(BusinessStatus.PARAM_NULL.getCode(), errorMessage);
    }

    /**
     * 处理参数类型不匹配异常
     *
     * <p>触发条件：URL 参数类型转换失败时触发，例如 {@code /users?userId=abc}（userId 应为 Long）。
     *
     * @param exception 参数类型不匹配异常
     * @return Result 响应结果，code 为参数错误码
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String paramName = exception.getName();
        String paramValue = exception.getValue() != null ? exception.getValue().toString() : "null";

        log.warn("参数类型不匹配: 参数名={}, 参数值={}", paramName, paramValue);

        return Result.fail(BusinessStatus.PARAM_NULL.getCode(),
            "参数 '" + paramName + "' 类型不正确，值为: " + paramValue);
    }

    /**
     * 处理运行时异常
     *
     * <p>触发条件：未捕获的运行时异常，例如 NullPointerException、IllegalArgumentException 等。
     *
     * <p>符合宪法第三条：异常链传递，记录完整堆栈。
     *
     * @param exception 运行时异常
     * @return Result 响应结果，code 为系统错误码
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Object> handleRuntimeException(RuntimeException exception) {
        log.error("运行时异常: {}", exception.getMessage(), exception);

        return Result.fail(BusinessStatus.SYSTEM_ERROR.getCode(),
            "系统异常: " + exception.getMessage());
    }

    /**
     * 处理通用异常
     *
     * <p>触发条件：所有未被上述方法处理的异常，例如 SQLException、IOException 等。
     *
     * <p>符合宪法第三条：异常链传递，记录完整堆栈。
     *
     * @param exception 通用异常
     * @return Result 响应结果，code 为系统错误码
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception exception) {
        log.error("系统异常: {}", exception.getMessage(), exception);

        return Result.fail(BusinessStatus.SYSTEM_ERROR.getCode(),
            "系统异常: " + exception.getMessage());
    }
}
