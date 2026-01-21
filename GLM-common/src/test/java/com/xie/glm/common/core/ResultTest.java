package com.xie.glm.common.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result 统一响应包装类测试
 * <p>
 * 测试原则：
 * 1. 参数化测试覆盖各种场景
 * 2. 测试 success() 和 fail() 方法的行为
 * 3. 验证 code、message、data 字段
 *
 * @author xie
 */
class ResultTest {

    /**
     * 测试无参数 success() 方法
     */
    @Test
    void testSuccessWithoutData() {
        Result<Void> result = Result.success();

        assertEquals(0, result.getCode());
        assertEquals("success", result.getMessage());
        assertNull(result.getData());
    }

    /**
     * 参数化测试：带数据的 success() 方法
     * <p>
     * 测试数据：
     * - null 数据
     * - 字符串数据
     * - 数字数据
     * - 对象数据
     */
    @ParameterizedTest
    @MethodSource("provideSuccessData")
    <T> void testSuccessWithData(T data, String expectedMessage) {
        Result<T> result = Result.success(data);

        assertEquals(0, result.getCode());
        assertEquals(expectedMessage, result.getMessage());
        assertEquals(data, result.getData());
    }

    /**
     * 参数化测试：fail(String) 方法
     * <p>
     * 测试数据：
     * - 简单错误消息
     * - 空错误消息
     * - 长错误消息
     */
    @ParameterizedTest
    @MethodSource("provideFailMessages")
    void testFailWithMessage(String message) {
        Result<Object> result = Result.fail(message);

        assertEquals(1, result.getCode());
        assertEquals(message, result.getMessage());
        assertNull(result.getData());
    }

    /**
     * 参数化测试：fail(int, String) 方法
     * <p>
     * 测试数据：
     * - 不同错误码和消息组合
     */
    @ParameterizedTest
    @MethodSource("provideFailCodes")
    void testFailWithCodeAndMessage(int code, String message) {
        Result<Object> result = Result.fail(code, message);

        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertNull(result.getData());
    }

    /**
     * 测试 Result 的 Getter/Setter 方法
     */
    @Test
    void testGettersAndSetters() {
        Result<String> result = new Result<>();
        result.setCode(100);
        result.setMessage("custom message");
        result.setData("test data");

        assertEquals(100, result.getCode());
        assertEquals("custom message", result.getMessage());
        assertEquals("test data", result.getData());
    }

    /**
     * 测试 Result 的构造方法
     */
    @Test
    void testConstructor() {
        Result<String> result = new Result<>(200, "created", "resource");

        assertEquals(200, result.getCode());
        assertEquals("created", result.getMessage());
        assertEquals("resource", result.getData());
    }

    // ==================== 测试数据提供方法 ====================

    /**
     * 提供 success(data) 测试数据
     */
    private static Stream<Arguments> provideSuccessData() {
        return Stream.of(
                Arguments.of(null, "success"),
                Arguments.of("test data", "success"),
                Arguments.of(123, "success"),
                Arguments.of(true, "success"),
                Arguments.of(new Object(), "success")
        );
    }

    /**
     * 提供 fail(message) 测试数据
     */
    private static Stream<Arguments> provideFailMessages() {
        return Stream.of(
                Arguments.of("error occurred"),
                Arguments.of(""),
                Arguments.of("a very long error message with many details about what went wrong in the system")
        );
    }

    /**
     * 提供 fail(code, message) 测试数据
     */
    private static Stream<Arguments> provideFailCodes() {
        return Stream.of(
                Arguments.of(1, "default error"),
                Arguments.of(10001, "user not found"),
                Arguments.of(20001, "database error"),
                Arguments.of(30001, "external service unavailable")
        );
    }
}
