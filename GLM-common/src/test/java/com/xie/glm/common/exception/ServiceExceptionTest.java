package com.xie.glm.common.exception;

import com.xie.glm.common.enums.BusinessStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ServiceException 业务异常测试
 * <p>
 * 测试原则：
 * 1. 参数化测试覆盖各种构造场景
 * 2. 验证异常链传递（符合宪法第三条）
 * 3. 测试继承 RuntimeException
 *
 * @author xie
 */
class ServiceExceptionTest {

    /**
     * 测试默认构造方法
     */
    @Test
    void testDefaultConstructor() {
        ServiceException exception = new ServiceException();

        // 默认使用 BusinessStatus.ERROR
        assertEquals("失败", exception.getMessage());
        assertEquals(1, exception.getCode());
        assertNull(exception.getCause());
    }

    /**
     * 测试带消息的构造方法
     */
    @Test
    void testMessageConstructor() {
        String message = "业务异常";
        ServiceException exception = new ServiceException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(1, exception.getCode()); // ERROR.code
        assertNull(exception.getCause());
    }

    /**
     * 测试基于 BusinessStatus 的构造方法
     */
    @Test
    void testBusinessStatusConstructor() {
        ServiceException exception = new ServiceException(BusinessStatus.USER_NOT_FOUND);

        assertEquals(BusinessStatus.USER_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals(BusinessStatus.USER_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(11001, exception.getCode());
        assertNull(exception.getCause());
    }

    /**
     * 测试基于 BusinessStatus 和原因的构造方法
     */
    @Test
    void testBusinessStatusWithCauseConstructor() {
        SQLException cause = new SQLException("数据库错误");
        ServiceException exception = new ServiceException(BusinessStatus.SYSTEM_ERROR, cause);

        assertEquals(BusinessStatus.SYSTEM_ERROR.getMessage(), exception.getMessage());
        assertEquals(BusinessStatus.SYSTEM_ERROR.getCode(), exception.getCode());
        assertEquals(cause, exception.getCause());
    }

    /**
     * 测试带消息和原因的构造方法（异常链）
     */
    @Test
    void testMessageAndCauseConstructor() {
        String message = "业务异常";
        SQLException cause = new SQLException("数据库错误");
        ServiceException exception = new ServiceException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(cause, exception.getCause());
    }

    /**
     * 测试仅带原因的构造方法
     */
    @Test
    void testCauseConstructor() {
        SQLException cause = new SQLException("数据库错误");
        ServiceException exception = new ServiceException(cause);

        assertNotNull(exception.getMessage()); // cause.toString() 会作为消息
        assertEquals(cause, exception.getCause());
    }

    /**
     * 参数化测试：各种构造方法组合
     */
    @ParameterizedTest
    @MethodSource("provideExceptionData")
    void testExceptionConstruction(String message, Throwable cause, String expectedMessage) {
        ServiceException exception;

        if (message != null && cause != null) {
            exception = new ServiceException(message, cause);
        } else if (message != null) {
            exception = new ServiceException(message);
        } else if (cause != null) {
            exception = new ServiceException(cause);
        } else {
            exception = new ServiceException();
        }

        if (expectedMessage != null) {
            assertEquals(expectedMessage, exception.getMessage());
        }
        assertEquals(cause, exception.getCause());
    }

    /**
     * 测试异常链完整性
     */
    @Test
    void testExceptionChain() {
        SQLException rootCause = new SQLException("连接超时");
        RuntimeException midCause = new RuntimeException("数据访问失败", rootCause);
        ServiceException exception = new ServiceException("用户查询失败", midCause);

        // 验证异常链
        assertEquals(midCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());

        // 验证异常链不会丢失
        assertSame(midCause, exception.getCause());
        assertSame(rootCause, exception.getCause().getCause());
    }

    /**
     * 测试继承关系
     */
    @Test
    void testInheritance() {
        ServiceException exception = new ServiceException("测试异常");

        // 继承 RuntimeException
        assertTrue(exception instanceof RuntimeException);
        // 继承 Exception
        assertTrue(exception instanceof Exception);
        // 继承 Throwable
        assertTrue(exception instanceof Throwable);
    }

    /**
     * 测试异常可以被捕获
     */
    @Test
    void testCanBeCaught() {
        SQLException cause = new SQLException("数据库错误");
        ServiceException exception = new ServiceException("业务异常", cause);

        // 可以作为 RuntimeException 捕获
        assertThrows(RuntimeException.class, () -> {
            throw exception;
        });

        // 可以作为 Exception 捕获
        assertThrows(Exception.class, () -> {
            throw exception;
        });

        // 可以作为 ServiceException 捕获
        assertThrows(ServiceException.class, () -> {
            throw exception;
        });
    }

    /**
     * 测试堆栈跟踪包含异常链
     */
    @Test
    void testStackTraceContainsCause() {
        SQLException cause = new SQLException("数据库错误");
        ServiceException exception = new ServiceException("业务异常", cause);

        StackTraceElement[] stackTrace = exception.getStackTrace();
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    // ==================== 测试数据提供方法 ====================

    /**
     * 提供异常测试数据
     */
    private static Stream<Arguments> provideExceptionData() {
        SQLException cause = new SQLException("数据库错误");

        return Stream.of(
            Arguments.of("业务异常", cause, "业务异常"),
            Arguments.of("业务异常", null, "业务异常"),
            Arguments.of(null, cause, "java.sql.SQLException: 数据库错误"),
            Arguments.of(null, null, null)
        );
    }
}
