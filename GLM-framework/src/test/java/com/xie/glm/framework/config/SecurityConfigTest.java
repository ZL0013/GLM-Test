package com.xie.glm.framework.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Security 6 配置测试类
 *
 * <p>使用单元测试验证 Security 配置
 *
 * @author xie
 */
@DisplayName("Spring Security 6 配置测试")
class SecurityConfigTest {

    @Test
    @DisplayName("验证 SecurityConfig 类存在 passwordEncoder 方法")
    void testPasswordEncoderMethodExists() throws NoSuchMethodException {
        Method method = SecurityConfig.class.getMethod("passwordEncoder");
        assertNotNull(method, "passwordEncoder 方法应该存在");
        assertTrue(method.isAnnotationPresent(org.springframework.context.annotation.Bean.class),
                "passwordEncoder 方法应该有 @Bean 注解");
    }

    @Test
    @DisplayName("验证 SecurityConfig 类存在 securityFilterChain 方法")
    void testSecurityFilterChainMethodExists() throws NoSuchMethodException {
        Method method = SecurityConfig.class.getMethod("securityFilterChain", HttpSecurity.class);
        assertNotNull(method, "securityFilterChain 方法应该存在");
        assertTrue(method.isAnnotationPresent(org.springframework.context.annotation.Bean.class),
                "securityFilterChain 方法应该有 @Bean 注解");
    }

    @Test
    @DisplayName("验证 SecurityConfig 类存在 authenticationManager 方法")
    void testAuthenticationManagerMethodExists() throws NoSuchMethodException {
        Method method = SecurityConfig.class.getMethod("authenticationManager", AuthenticationConfiguration.class);
        assertNotNull(method, "authenticationManager 方法应该存在");
        assertTrue(method.isAnnotationPresent(org.springframework.context.annotation.Bean.class),
                "authenticationManager 方法应该有 @Bean 注解");
    }

    @Test
    @DisplayName("验证 SecurityConfig 类有正确的注解")
    void testSecurityConfigAnnotations() {
        assertTrue(SecurityConfig.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class),
                "SecurityConfig 应该有 @Configuration 注解");
        assertTrue(SecurityConfig.class.isAnnotationPresent(org.springframework.security.config.annotation.web.configuration.EnableWebSecurity.class),
                "SecurityConfig 应该有 @EnableWebSecurity 注解");
        assertTrue(SecurityConfig.class.isAnnotationPresent(org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity.class),
                "SecurityConfig 应该有 @EnableMethodSecurity 注解");
    }

    @Test
    @DisplayName("验证 BCrypt PasswordEncoder 功能")
    void testBCryptPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // BCrypt 加密后的密码格式为 $2a$ 或 $2b$ 开头
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(
                encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$"),
                "BCrypt 加密后的密码应该以 $2a$ 或 $2b$ 开头，实际为: " + encodedPassword
        );

        // 验证相同明文加密后结果不同（加盐）
        String encodedPassword2 = passwordEncoder.encode(rawPassword);
        assertNotEquals(
                encodedPassword,
                encodedPassword2,
                "BCrypt 每次加密结果应该不同（随机盐）"
        );

        // 验证密码匹配
        assertTrue(
                passwordEncoder.matches(rawPassword, encodedPassword),
                "密码匹配验证应该通过"
        );

        assertFalse(
                passwordEncoder.matches("wrongPassword", encodedPassword),
                "错误密码匹配验证应该失败"
        );
    }

    @Test
    @DisplayName("验证 BCrypt 密码强度默认为 10")
    void testBCryptStrength() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // BCrypt 默认强度（rounds）为 10
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // BCrypt $2a$10$ 表示强度为 10（默认值）
        // 格式: $2a$[rounds]$[salt][hash]
        assertTrue(
                encodedPassword.matches("^\\$2[ab]\\$\\d+\\$.{53}$"),
                "BCrypt 密码格式应该正确，实际为: " + encodedPassword
        );

        // 验证强度为 10
        String[] parts = encodedPassword.split("\\$");
        assertEquals(10, Integer.parseInt(parts[2]),
                "BCrypt 默认强度应该为 10，实际为: " + parts[2]);
    }

    @Test
    @DisplayName("验证 BCrypt 不同强度的密码编码器")
    void testBCryptStrengthVariants() {
        // 测试不同强度的 BCrypt
        PasswordEncoder weakEncoder = new BCryptPasswordEncoder(4);
        PasswordEncoder normalEncoder = new BCryptPasswordEncoder(10);
        PasswordEncoder strongEncoder = new BCryptPasswordEncoder(12);

        String rawPassword = "testPassword";
        String weakEncoded = weakEncoder.encode(rawPassword);
        String normalEncoded = normalEncoder.encode(rawPassword);
        String strongEncoded = strongEncoder.encode(rawPassword);

        // 验证不同强度的密码编码器都能正确工作
        assertTrue(weakEncoder.matches(rawPassword, weakEncoded),
                "弱强度 BCrypt 应该能正确验证密码");
        assertTrue(normalEncoder.matches(rawPassword, normalEncoded),
                "正常强度 BCrypt 应该能正确验证密码");
        assertTrue(strongEncoder.matches(rawPassword, strongEncoded),
                "强强度 BCrypt 应该能正确验证密码");

        // 验证不同编码器生成的密码格式不同（强度不同）
        String[] weakParts = weakEncoded.split("\\$");
        String[] normalParts = normalEncoded.split("\\$");
        String[] strongParts = strongEncoded.split("\\$");

        assertEquals(4, Integer.parseInt(weakParts[2]), "弱强度应该为 4");
        assertEquals(10, Integer.parseInt(normalParts[2]), "正常强度应该为 10");
        assertEquals(12, Integer.parseInt(strongParts[2]), "强强度应该为 12");
    }
}
