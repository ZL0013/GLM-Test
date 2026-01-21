package com.xie.glm.framework.web;

import com.xie.glm.common.core.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 响应包装处理器测试
 *
 * <p>测试 ResponseBodyAdvice 自动包装返回值为 Result 格式，遵循 TDD 原则。
 *
 * @author xie
 */
@DisplayName("响应包装处理器测试")
class ResponseAdviceTest {

    private ResponseAdvice responseAdvice;

    private ServerHttpRequest request;
    private ServerHttpResponse response;

    @BeforeEach
    void setUp() {
        responseAdvice = new ResponseAdvice();

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        request = new ServletServerHttpRequest(servletRequest);
        response = new ServletServerHttpResponse(servletResponse);
    }

    /**
     * 测试：supports 方法应该支持所有返回值类型
     */
    @Test
    @DisplayName("supports 方法应该支持所有返回值类型")
    void testSupports() {
        // Given
        MethodParameter methodParameter = mockMethodParameter(String.class);

        // When
        boolean supports = responseAdvice.supports(methodParameter, null);

        // Then
        assertTrue(supports, "ResponseAdvice 应该支持所有返回值类型");
    }

    /**
     * 测试：null 返回值应该被包装为成功响应
     */
    @Test
    @DisplayName("null 返回值应该被包装为成功响应")
    void testWrapNullResponse() {
        // Given
        Object body = null;
        MethodParameter returnType = mockMethodParameter(Void.class);

        // When
        Object result = responseAdvice.beforeBodyWrite(
            body, returnType, null, null, request, response
        );

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Result<?>);
        Result<?> resultObj = (Result<?>) result;
        assertEquals(0, resultObj.getCode());
        assertEquals("success", resultObj.getMessage());
    }

    /**
     * 测试：已经是 Result 类型的返回值不应该被重复包装
     */
    @Test
    @DisplayName("Result 类型不应该被重复包装")
    void testNotWrapResultType() {
        // Given
        Result<String> originalResult = Result.success("test data");
        MethodParameter returnType = mockMethodParameter(Result.class);

        // When
        Object result = responseAdvice.beforeBodyWrite(
            originalResult, returnType, null, null, request, response
        );

        // Then
        assertNotNull(result);
        assertEquals(originalResult, result, "Result 类型不应该被重新包装");
    }

    /**
     * 测试：String 类型返回值应该被包装为 Result
     */
    @Test
    @DisplayName("String 返回值应该被包装为 Result")
    void testWrapStringResponse() {
        // Given
        String body = "test message";
        MethodParameter returnType = mockMethodParameter(String.class);

        // When
        Object result = responseAdvice.beforeBodyWrite(
            body, returnType, null, null, request, response
        );

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Result<?>);
        Result<?> resultObj = (Result<?>) result;
        assertEquals(0, resultObj.getCode());
        assertEquals("success", resultObj.getMessage());
        assertEquals(body, resultObj.getData());
    }

    /**
     * 参数化测试：各种类型返回值应该被正确包装
     *
     * @param body         返回值
     * @param expectedData 预期的数据
     */
    @ParameterizedTest(name = "类型: {0}")
    @MethodSource("provideVariousResponseData")
    @DisplayName("各种类型返回值应该被正确包装 - 参数化测试")
    <T> void testWrapVariousResponses(T body, T expectedData) {
        // Given
        MethodParameter returnType = mockMethodParameter(body != null ? body.getClass() : Object.class);

        // When
        Object result = responseAdvice.beforeBodyWrite(
            body, returnType, null, null, request, response
        );

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Result<?>);
        Result<?> resultObj = (Result<?>) result;
        assertEquals(0, resultObj.getCode());
        assertEquals("success", resultObj.getMessage());
        assertEquals(expectedData, resultObj.getData());
    }

    /**
     * 测试：基本类型 Integer 应该被正确包装
     */
    @Test
    @DisplayName("Integer 返回值应该被正确包装")
    void testWrapIntegerResponse() {
        // Given
        Integer body = 12345;
        MethodParameter returnType = mockMethodParameter(Integer.class);

        // When
        Object result = responseAdvice.beforeBodyWrite(
            body, returnType, null, null, request, response
        );

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Result<?>);
        Result<?> resultObj = (Result<?>) result;
        assertEquals(0, resultObj.getCode());
        assertEquals("success", resultObj.getMessage());
        assertEquals(body, resultObj.getData());
    }

    /**
     * 测试：Boolean 类型应该被正确包装
     */
    @Test
    @DisplayName("Boolean 返回值应该被正确包装")
    void testWrapBooleanResponse() {
        // Given
        Boolean body = true;
        MethodParameter returnType = mockMethodParameter(Boolean.class);

        // When
        Object result = responseAdvice.beforeBodyWrite(
            body, returnType, null, null, request, response
        );

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Result<?>);
        Result<?> resultObj = (Result<?>) result;
        assertEquals(0, resultObj.getCode());
        assertEquals("success", resultObj.getMessage());
        assertEquals(body, resultObj.getData());
    }

    /**
     * 测试：自定义对象应该被正确包装
     */
    @Test
    @DisplayName("自定义对象返回值应该被正确包装")
    void testWrapCustomObjectResponse() {
        // Given
        TestUser body = new TestUser("张三", 25);
        MethodParameter returnType = mockMethodParameter(TestUser.class);

        // When
        Object result = responseAdvice.beforeBodyWrite(
            body, returnType, null, null, request, response
        );

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Result<?>);
        Result<?> resultObj = (Result<?>) result;
        assertEquals(0, resultObj.getCode());
        assertEquals("success", resultObj.getMessage());
        assertEquals(body, resultObj.getData());
    }

    // ==================== 测试辅助方法 ====================

    /**
     * 模拟 MethodParameter
     */
    private MethodParameter mockMethodParameter(Class<?> parameterType) {
        try {
            // 创建一个虚拟方法用于模拟 MethodParameter
            if (parameterType == String.class) {
                return new MethodParameter(TestController.class.getMethod("stringMethod"), -1);
            } else if (parameterType == Integer.class || parameterType == int.class) {
                return new MethodParameter(TestController.class.getMethod("intMethod"), -1);
            } else if (parameterType == Boolean.class || parameterType == boolean.class) {
                return new MethodParameter(TestController.class.getMethod("booleanMethod"), -1);
            } else if (parameterType == Result.class) {
                return new MethodParameter(TestController.class.getMethod("resultMethod"), -1);
            } else if (parameterType == Void.class || parameterType == void.class) {
                return new MethodParameter(TestController.class.getMethod("voidMethod"), -1);
            } else {
                return new MethodParameter(TestController.class.getMethod("objectMethod"), -1);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提供各种类型测试数据
     */
    private static Stream<Arguments> provideVariousResponseData() {
        return Stream.of(
            Arguments.of("字符串数据", "字符串数据"),
            Arguments.of(42, 42),
            Arguments.of(true, true),
            Arguments.of(3.14, 3.14),
            Arguments.of('A', 'A')
        );
    }

    // ==================== 测试辅助类 ====================

    /**
     * 测试用控制器
     */
    @SuppressWarnings("unused")
    static class TestController {

        public String stringMethod() {
            return "test";
        }

        public int intMethod() {
            return 0;
        }

        public boolean booleanMethod() {
            return false;
        }

        public Result<?> resultMethod() {
            return Result.success();
        }

        public void voidMethod() {
        }

        public Object objectMethod() {
            return new Object();
        }
    }

    /**
     * 测试用用户对象
     */
    record TestUser(String name, int age) {
    }
}
