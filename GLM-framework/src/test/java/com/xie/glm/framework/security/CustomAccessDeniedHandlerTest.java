package com.xie.glm.framework.security;

import com.xie.glm.common.enums.BusinessStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 无权限访问处理器测试类
 *
 * <p>测试已认证但权限不足用户的统一处理，返回统一响应格式。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CustomAccessDeniedHandlerTest {

    private CustomAccessDeniedHandler accessDeniedHandler;

    @Mock
    private AccessDeniedException accessDeniedException;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        accessDeniedHandler = new CustomAccessDeniedHandler();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @ParameterizedTest
    @MethodSource("provideAccessDeniedExceptions")
    void testHandle_Returns200(AccessDeniedException exception, String description) throws IOException {
        // When
        accessDeniedHandler.handle(request, response, exception);

        // Then
        assertResponseStructure(200);
        assertTrue(response.getContentAsString(StandardCharsets.UTF_8).contains("无权访问"));
    }

    @Test
    void testHandle_WithNullException() throws IOException {
        // When
        accessDeniedHandler.handle(request, response, null);

        // Then
        assertEquals(200, response.getStatus());
        assertEquals("application/json;charset=UTF-8", response.getContentType());

        String responseContent = response.getContentAsString(StandardCharsets.UTF_8);
        assertTrue(responseContent.contains("\"code\":" + BusinessStatus.FORBIDDEN.getCode()));
        assertTrue(responseContent.contains("\"data\":null"));
    }

    @Test
    void testHandle_ResponseStructure() throws IOException {
        // When
        accessDeniedHandler.handle(request, response, accessDeniedException);

        // Then - 验证完整的 JSON 结构
        String responseContent = response.getContentAsString(StandardCharsets.UTF_8);
        assertTrue(responseContent.startsWith("{"), "Response should start with '{'");
        assertTrue(responseContent.endsWith("}"), "Response should end with '}'");
        assertTrue(responseContent.contains("\"code\":" + BusinessStatus.FORBIDDEN.getCode()), "Response should contain code:10002");
        assertTrue(responseContent.contains("\"message\":"), "Response should contain message field");
        assertTrue(responseContent.contains("无权访问"), "Response should contain '无权访问'");
        assertTrue(responseContent.contains("\"data\":null"), "Response should contain data:null");
    }

    private void assertResponseStructure(int expectedStatus) throws IOException {
        assertEquals(expectedStatus, response.getStatus());
        assertEquals("application/json;charset=UTF-8", response.getContentType());

        String responseContent = response.getContentAsString(StandardCharsets.UTF_8);
        assertTrue(responseContent.contains("\"code\":"), "Response should contain 'code' field");
        assertTrue(responseContent.contains("\"message\":"), "Response should contain 'message' field");
        assertTrue(responseContent.contains("\"data\":null"), "Response should contain 'data:null' field");
    }

    private static Stream<Arguments> provideAccessDeniedExceptions() {
        return Stream.of(
            Arguments.of(new AccessDeniedException("Access denied"), "Standard access denied"),
            Arguments.of(new AccessDeniedException("insufficient authorities"), "Insufficient authorities"),
            Arguments.of(new AccessDeniedException("Role required: ADMIN"), "Missing role"),
            Arguments.of(new AccessDeniedException(""), "Empty message")
        );
    }
}
