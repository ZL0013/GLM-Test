package com.xie.glm.common.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PasswordValidator 密码校验工具测试
 * <p>
 * 密码规则：
 * <ul>
 *   <li>长度：8-32 位</li>
 *   <li>必须包含：大小写字母 + 数字 + 特殊字符</li>
 *   <li>不能包含用户名</li>
 *   <li>不能包含空格</li>
 *   <li>特殊字符：!@#$%^&*()_+-=[]{}|;:,.<>?</li>
 * </ul>
 *
 * @author xie
 */
class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    /**
     * 测试有效密码
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Abc123!@",        // 最小长度，包含所有要素
            "Admin@2025",       // 10位，包含所有要素
            "Test#1234567890",  // 16位，包含所有要素
            "aB1!abcdefg",      // 包含字母、数字、特殊字符
            "P@ssw0rd"          // 经典强密码
    })
    void testValidPassword(String password) {
        ValidationResult result = validator.validate(password, "testuser");
        assertTrue(result.valid(), "Password should be valid: " + password);
        assertTrue(result.errors().isEmpty(), "Should have no errors: " + password);
    }

    /**
     * 测试无效密码 - 长度不足
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Ab1!",            // 少于 8 位
            "A1!a",            // 4位
            ""                 // 空字符串
    })
    void testPasswordTooShort(String password) {
        ValidationResult result = validator.validate(password, "testuser");
        assertFalse(result.valid());
        assertFalse(result.errors().isEmpty());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("8-32")));
    }

    /**
     * 测试无效密码 - 长度过长
     */
    @Test
    void testPasswordTooLong() {
        String password = "Abc123!@" + "x".repeat(50); // 超过 32 位
        ValidationResult result = validator.validate(password, "testuser");
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("8-32")));
    }

    /**
     * 测试无效密码 - 缺少大写字母
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "abc123!@",        // 全小写
            "12345678!a",      // 全小写+数字
    })
    void testMissingUpperCase(String password) {
        ValidationResult result = validator.validate(password, "testuser");
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("大写字母")));
    }

    /**
     * 测试无效密码 - 缺少小写字母
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "ABC123!@",        // 全大写
            "12345678!A",      // 全大写+数字
    })
    void testMissingLowerCase(String password) {
        ValidationResult result = validator.validate(password, "testuser");
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("小写字母")));
    }

    /**
     * 测试无效密码 - 缺少数字
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Abcdefg!",        // 无数字
            "Admin!Password",  // 无数字
    })
    void testMissingDigit(String password) {
        ValidationResult result = validator.validate(password, "testuser");
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("数字")));
    }

    /**
     * 测试无效密码 - 缺少特殊字符
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Abc12345",        // 无特殊字符
            "Admin2025",       // 无特殊字符
    })
    void testMissingSpecialChar(String password) {
        ValidationResult result = validator.validate(password, "testuser");
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("特殊字符")));
    }

    /**
     * 测试无效密码 - 包含用户名
     */
    @ParameterizedTest
    @MethodSource("providePasswordsWithUsername")
    void testContainsUsername(String password, String username) {
        ValidationResult result = validator.validate(password, username);
        assertFalse(result.valid(), "Password should not contain username");
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("用户名")));
    }

    /**
     * 测试无效密码 - 包含空格
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Abc 123!@",       // 中间有空格
            " Abc123!@",       // 开头有空格
            "Abc123!@ ",       // 结尾有空格
            "Abc  123 !@",     // 多个空格
    })
    void testContainsWhitespace(String password) {
        ValidationResult result = validator.validate(password, "testuser");
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("空格")));
    }

    /**
     * 测试边界条件 - 正好 8 位
     */
    @Test
    void testExactMinimumLength() {
        String password = "Aa1!aaaa"; // 正好 8 位，包含所有要素
        ValidationResult result = validator.validate(password, "testuser");
        assertTrue(result.valid());
    }

    /**
     * 测试边界条件 - 正好 32 位
     */
    @Test
    void testExactMaximumLength() {
        String password = "Aa1!aaaaAa1!aaaaAa1!aaaaAa1!"; // 正好 32 位
        ValidationResult result = validator.validate(password, "testuser");
        assertTrue(result.valid());
    }

    /**
     * 测试 null 用户名
     */
    @Test
    void testNullUsername() {
        ValidationResult result = validator.validate("Abc123!@", null);
        // null 用户名应该跳过用户名检查
        assertTrue(result.valid());
    }

    /**
     * 测试多个错误同时存在
     */
    @Test
    void testMultipleErrors() {
        ValidationResult result = validator.validate("abc", "testuser");
        // 太短、缺少大写、缺少特殊字符
        assertFalse(result.valid());
        assertTrue(result.errors().size() >= 2);
    }

    // ==================== 测试数据提供方法 ====================

    /**
     * 提供包含用户名的密码
     */
    private static Stream<Arguments> providePasswordsWithUsername() {
        return Stream.of(
            Arguments.of("Test123!@", "Test"),      // 开头包含用户名
            Arguments.of("123Test!@", "Test"),      // 中间包含用户名
            Arguments.of("123!@Test", "Test"),      // 结尾包含用户名
            Arguments.of("test123!@", "test"),      // 小写用户名
            Arguments.of("ADMIN123!@", "admin")     // 大小写不敏感
        );
    }
}
