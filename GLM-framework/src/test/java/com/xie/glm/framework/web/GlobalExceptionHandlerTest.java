package com.xie.glm.framework.web;

import com.xie.glm.common.core.Result;
import com.xie.glm.common.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 全局异常处理器测试
 *
 * <p>测试各种异常场景的统一处理，遵循 TDD 原则。
 *
 * @author xie
 */
@DisplayName("全局异常处理器测试")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * 参数化测试：业务异常处理
     *
     * @param message      异常消息
     * @param expectedCode 预期错误码
     */
    @ParameterizedTest(name = "消息: {0}, 预期码: {1}")
    @CsvSource({
        "用户不存在, 1",
        "密码错误, 1",
        "角色不存在, 1",
        "参数不能为空, 1"
    })
    @DisplayName("业务异常处理 - 参数化测试")
    void testHandleServiceException(String message, Integer expectedCode) {
        // Given
        ServiceException exception = new ServiceException(message);

        // When
        Result<Object> result = handler.handleServiceException(exception);

        // Then
        assertNotNull(result);
        assertEquals(expectedCode, result.getCode());
        assertEquals(message, result.getMessage());
    }

    /**
     * 测试：参数校验异常处理
     */
    @Test
    @DisplayName("参数校验异常处理")
    void testHandleValidationException() {
        // Given
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(null, "testObject");
        bindingResult.addError(new FieldError("testObject", "fieldName", "默认错误消息"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // When
        Result<Object> result = handler.handleValidationException(exception);

        // Then
        assertNotNull(result);
        assertEquals(15003, result.getCode());
    }

    /**
     * 参数化测试：参数类型不匹配异常处理
     *
     * @param paramName    参数名
     * @param expectedCode 预期错误码
     */
    @ParameterizedTest(name = "参数: {0}, 预期码: {1}")
    @CsvSource({
        "userId, 15003",
        "deptId, 15003",
        "roleId, 15003"
    })
    @DisplayName("参数类型不匹配异常处理 - 参数化测试")
    void testHandleTypeMismatchException(String paramName, Integer expectedCode) {
        // Given
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
            null, null, paramName, null, null
        );

        // When
        Result<Object> result = handler.handleTypeMismatchException(exception);

        // Then
        assertNotNull(result);
        assertEquals(expectedCode, result.getCode());
    }

    /**
     * 参数化测试：运行时异常处理
     *
     * @param exception     异常实例
     * @param expectedCode  预期错误码
     */
    @ParameterizedTest(name = "运行时异常 -> 预期码: {1}")
    @MethodSource("provideRuntimeExceptionData")
    @DisplayName("运行时异常处理 - 参数化测试")
    void testHandleRuntimeException(RuntimeException exception, Integer expectedCode) {
        // When
        Result<Object> result = handler.handleRuntimeException(exception);

        // Then
        assertNotNull(result);
        assertEquals(expectedCode, result.getCode());
    }

    /**
     * 参数化测试：通用异常处理
     *
     * @param exception     异常实例
     * @param expectedCode  预期错误码
     */
    @ParameterizedTest(name = "通用异常 -> 预期码: {1}")
    @MethodSource("provideExceptionData")
    @DisplayName("通用异常处理 - 参数化测试")
    void testHandleException(Exception exception, Integer expectedCode) {
        // When
        Result<Object> result = handler.handleException(exception);

        // Then
        assertNotNull(result);
        assertEquals(expectedCode, result.getCode());
    }

    // ==================== 测试数据提供方法 ====================

    /**
     * 提供运行时异常测试数据
     */
    private static Stream<Arguments> provideRuntimeExceptionData() {
        return Stream.of(
            Arguments.of(new NullPointerException("对象为空"), 10000),
            Arguments.of(new IllegalArgumentException("参数不合法"), 10000),
            Arguments.of(new IllegalStateException("状态不正确"), 10000),
            Arguments.of(new UnsupportedOperationException("不支持的操作"), 10000)
        );
    }

    /**
     * 提供通用异常测试数据
     */
    private static Stream<Arguments> provideExceptionData() {
        return Stream.of(
            Arguments.of(new SQLException("数据库连接失败"), 10000),
            Arguments.of(new java.io.IOException("文件读取失败"), 10000),
            Arguments.of(new ClassNotFoundException("类未找到"), 10000)
        );
    }
}
