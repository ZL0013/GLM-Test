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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT 认证入口点测试类
 *
 * <p>测试未认证请求的统一处理，返回统一响应格式。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtAuthenticationEntryPointTest {

    private JwtAuthenticationEntryPoint entryPoint;

    @Mock
    private AuthenticationException authException;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        entryPoint = new JwtAuthenticationEntryPoint();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @ParameterizedTest
    @MethodSource("provideAuthenticationExceptions")
    void testCommence_Returns200(AuthenticationException exception, String description) throws IOException {
        // When
        entryPoint.commence(request, response, exception);

        // Then
        assertEquals(200, response.getStatus());
        assertEquals("application/json;charset=UTF-8", response.getContentType());

        String responseContent = response.getContentAsString(StandardCharsets.UTF_8);
        assertTrue(responseContent.contains("\"code\":" + BusinessStatus.UNAUTHORIZED.getCode()));
        assertTrue(responseContent.contains("\"message\""));
        assertTrue(responseContent.contains("未认证"));
        assertTrue(responseContent.contains("\"data\":null"));
    }

    @Test
    void testCommence_WithNullException() throws IOException {
        // When
        entryPoint.commence(request, response, null);

        // Then
        assertEquals(200, response.getStatus());
        assertEquals("application/json;charset=UTF-8", response.getContentType());

        String responseContent = response.getContentAsString(StandardCharsets.UTF_8);
        assertTrue(responseContent.contains("\"code\":" + BusinessStatus.UNAUTHORIZED.getCode()));
        assertTrue(responseContent.contains("\"data\":null"));
    }

    @Test
    void testCommence_ResponseStructure() throws IOException {
        // When
        entryPoint.commence(request, response, authException);

        // Then - 验证完整的 JSON 结构
        String responseContent = response.getContentAsString(StandardCharsets.UTF_8);
        assertTrue(responseContent.startsWith("{"));
        assertTrue(responseContent.endsWith("}"));
        assertTrue(responseContent.contains("\"code\":" + BusinessStatus.UNAUTHORIZED.getCode()));
        assertTrue(responseContent.contains("\"message\":"));
        assertTrue(responseContent.contains("\"data\":null"));
    }

    private static Stream<Arguments> provideAuthenticationExceptions() {
        return Stream.of(
            Arguments.of(new BadCredentialsException("Bad credentials"), "Bad credentials"),
            Arguments.of(new AuthenticationException("Token expired") {}, "Token expired"),
            Arguments.of(new AuthenticationException("Invalid token") {}, "Invalid token"),
            Arguments.of(new AuthenticationException("") {}, "Empty message")
        );
    }
}
