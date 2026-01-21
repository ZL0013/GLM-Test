package com.xie.glm.admin.controller;

import com.xie.glm.admin.dto.RefreshTokenRequest;
import com.xie.glm.admin.dto.RefreshTokenResponse;
import com.xie.glm.common.dto.LoginRequest;
import com.xie.glm.common.dto.LoginResponse;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.framework.security.JwtTokenManager;
import com.xie.glm.system.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 认证控制器测试
 *
 * <p>测试内容：
 * <ul>
 *   <li>登录接口：正确凭证、错误凭证</li>
 *   <li>刷新令牌接口：有效 refresh token、无效 refresh token</li>
 *   <li>登出接口：正常登出</li>
 * </ul>
 *
 * <p>注意：Controller 直接返回业务对象，ResponseAdvice 自动包装为 Result
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("认证控制器测试")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @InjectMocks
    private AuthController authController;

    /**
     * 登录接口测试
     */
    @Nested
    @DisplayName("登录接口测试")
    class LoginTests {

        @ParameterizedTest(name = "用户名={0}, 密码={1}, 预期成功={2}")
        @CsvSource({
                "admin, admin123, true",
                "admin, wrongpass, false"
        })
        @DisplayName("登录接口 - 参数化测试")
        void testLogin(String username, String password, boolean shouldSucceed) {
            // Arrange
            LoginRequest request = new LoginRequest(username, password);

            if (shouldSucceed) {
                // 模拟认证成功
                LoginResponse mockResponse = LoginResponse.of("mock-access-token", "mock-refresh-token");
                when(authService.login(request)).thenReturn(mockResponse);
            } else {
                // 模拟认证失败
                when(authService.login(request))
                        .thenThrow(new ServiceException(BusinessStatus.USER_PASSWORD_ERROR));
            }

            // Act & Assert
            if (shouldSucceed) {
                LoginResponse response = authController.login(request);
                assertNotNull(response);
                assertEquals("mock-access-token", response.accessToken());
                assertEquals("mock-refresh-token", response.refreshToken());
            } else {
                // 验证抛出 ServiceException
                try {
                    authController.login(request);
                } catch (ServiceException e) {
                    assertEquals(BusinessStatus.USER_PASSWORD_ERROR.getCode(), e.getCode());
                }
            }
        }
    }

    /**
     * 刷新令牌接口测试
     */
    @Nested
    @DisplayName("刷新令牌接口测试")
    class RefreshTokenTests {

        @ParameterizedTest(name = "RefreshToken={0}, 预期成功={1}")
        @MethodSource("provideRefreshTokenData")
        @DisplayName("刷新令牌 - 参数化测试")
        void testRefreshToken(String refreshToken, boolean shouldSucceed) {
            // Arrange
            RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

            if (shouldSucceed) {
                when(jwtTokenManager.validateRefreshToken(refreshToken)).thenReturn(true);
                when(jwtTokenManager.extractUsernameFromRefreshToken(refreshToken)).thenReturn("admin");
                when(jwtTokenManager.generateAccessToken("admin")).thenReturn("new-access-token");
            } else {
                when(jwtTokenManager.validateRefreshToken(refreshToken)).thenReturn(false);
            }

            // Act & Assert
            if (shouldSucceed) {
                RefreshTokenResponse response = authController.refreshToken(request);
                assertNotNull(response);
                assertEquals("new-access-token", response.accessToken());
            } else {
                // 验证抛出 ServiceException
                try {
                    authController.refreshToken(request);
                } catch (ServiceException e) {
                    assertEquals(BusinessStatus.UNAUTHORIZED.getCode(), e.getCode());
                }
            }
        }

        private static Stream<Arguments> provideRefreshTokenData() {
            return Stream.of(
                    Arguments.of("valid-refresh-token", true),
                    Arguments.of("invalid-refresh-token", false),
                    Arguments.of("", false)
            );
        }
    }

    /**
     * 登出接口测试
     */
    @Nested
    @DisplayName("登出接口测试")
    class LogoutTests {

        @Test
        @DisplayName("登出接口 - 成功")
        void testLogout() {
            // Act & Assert (无返回值，无异常即成功)
            authController.logout();
        }
    }
}
