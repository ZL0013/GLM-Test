package com.xie.glm.common.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StringUtil 字符串工具类测试
 * <p>
 * 测试字符串判空、脱敏、截取等工具方法
 *
 * @author xie
 */
class StringUtilTest {

    // ==================== isEmpty/isNotBlank 测试 ====================

    /**
     * 测试 isEmpty - null 和空字符串应返回 true
     */
    @ParameterizedTest
    @NullAndEmptySource
    void testIsEmpty(String input) {
        assertTrue(StringUtil.isEmpty(input), "isEmpty should return true for null or empty: " + input);
    }

    /**
     * 测试 isEmpty - 有内容字符串应返回 false
     */
    @ParameterizedTest
    @ValueSource(strings = {"a", "text", " ", "  ", "\t", "\n"})
    void testNotEmpty(String input) {
        assertFalse(StringUtil.isEmpty(input), "isEmpty should return false for: " + input);
    }

    /**
     * 测试 isBlank - null、空字符串、纯空白应返回 true
     */
    @ParameterizedTest
    @MethodSource("provideBlankStrings")
    void testIsBlank(String input) {
        assertTrue(StringUtil.isBlank(input), "isBlank should return true for: " + input);
    }

    /**
     * 测试 isBlank - 有非空白字符应返回 false
     */
    @ParameterizedTest
    @ValueSource(strings = {"a", "text", " a ", "text", "0", "false"})
    void testNotBlank(String input) {
        assertFalse(StringUtil.isBlank(input), "isBlank should return false for: " + input);
    }

    // ==================== defaultIfBlank 测试 ====================

    /**
     * 测试 defaultIfBlank
     */
    @ParameterizedTest
    @CsvSource({
        ", , default",
        "test, test, test",
        "'', default, default",
        "'  ', default, default"
    })
    void testDefaultIfBlank(String input, String expected, String defaultValue) {
        assertEquals(expected, StringUtil.defaultIfBlank(input, defaultValue));
    }

    // ==================== 脱敏测试 ====================

    /**
     * 测试手机号脱敏
     */
    @ParameterizedTest
    @CsvSource({
        "13812345678, 138****5678",
        "15912345678, 159****5678",
        "18012345678, 180****5678"
    })
    void testMaskPhone(String phone, String expected) {
        assertEquals(expected, StringUtil.maskPhone(phone));
    }

    /**
     * 测试手机号脱敏 - 无效格式
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "123", "12345678901234"})
    void testMaskPhoneInvalid(String phone) {
        // 无效格式应返回原值或空
        String result = StringUtil.maskPhone(phone);
        assertTrue(result.isEmpty() || result.equals(phone));
    }

    /**
     * 测试邮箱脱敏
     */
    @ParameterizedTest
    @CsvSource({
        "test@example.com, t***@example.com",
        "admin@test.com, a***@test.com",
        "user.name@company.com, u*******@company.com"
    })
    void testMaskEmail(String email, String expected) {
        assertEquals(expected, StringUtil.maskEmail(email));
    }

    /**
     * 测试邮箱脱敏 - 无效格式
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "invalid", "@example.com", "test@"})
    void testMaskEmailInvalid(String email) {
        String result = StringUtil.maskEmail(email);
        assertTrue(result.isEmpty() || result.equals(email));
    }

    /**
     * 测试身份证号脱敏
     */
    @ParameterizedTest
    @CsvSource({
        "110101199001011234, 110101********1234",
        "310101198512151234, 310101********1234",
        "440101200001011234, 440101********1234"
    })
    void testMaskIdCard(String idCard, String expected) {
        assertEquals(expected, StringUtil.maskIdCard(idCard));
    }

    /**
     * 测试身份证号脱敏 - 无效长度
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "123", "12345678901234567890"})
    void testMaskIdCardInvalid(String idCard) {
        String result = StringUtil.maskIdCard(idCard);
        assertTrue(result.isEmpty() || result.equals(idCard));
    }

    /**
     * 测试银行卡号脱敏
     */
    @ParameterizedTest
    @CsvSource({
        "6222021234567890123, 622202*******0123",
        "6228481234567890123, 622848*******0123"
    })
    void testMaskBankCard(String cardNo, String expected) {
        assertEquals(expected, StringUtil.maskBankCard(cardNo));
    }

    /**
     * 测试通用脱敏方法
     */
    @ParameterizedTest
    @MethodSource("provideMaskData")
    void testMask(String str, int prefixLen, int suffixLen, char maskChar, String expected) {
        assertEquals(expected, StringUtil.mask(str, prefixLen, suffixLen, maskChar));
    }

    // ==================== truncate 测试 ====================

    /**
     * 测试字符串截取
     */
    @ParameterizedTest
    @CsvSource({
        "hello, 10, hello",
        "hello world, 5, hello...",
        "test, 3, tes..."
    })
    void testTruncate(String str, int maxLength, String expected) {
        assertEquals(expected, StringUtil.truncate(str, maxLength, "..."));
    }

    /**
     * 测试截取 - null 输入
     */
    @Test
    void testTruncateNull() {
        assertEquals("", StringUtil.truncate(null, 10, "..."));
    }

    // ==================== equalsIgnoreCase 测试 ====================

    /**
     * 测试忽略大小写相等比较
     */
    @ParameterizedTest
    @CsvSource({
        "hello, HELLO, true",
        "Test, test, true",
        "abc, ABC, true",
        "hello, world, false",
        ", , true"
    })
    void testEqualsIgnoreCase(String str1, String str2, boolean expected) {
        assertEquals(expected, StringUtil.equalsIgnoreCase(str1, str2));
    }

    // ==================== isNumeric 测试 ====================

    /**
     * 测试数字字符串判断
     */
    @ParameterizedTest
    @ValueSource(strings = {"123", "0", "00123", "999999"})
    void testIsNumeric(String input) {
        assertTrue(StringUtil.isNumeric(input), "Should be numeric: " + input);
    }

    /**
     * 测试非数字字符串判断
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "abc", "12.34", "12a34", "-123", " 123 "})
    void testIsNotNumeric(String input) {
        assertFalse(StringUtil.isNumeric(input), "Should not be numeric: " + input);
    }

    // ==================== 下划线/驼峰转换测试 ====================

    /**
     * 测试下划线转驼峰
     */
    @ParameterizedTest
    @CsvSource({
        "user_name, userName",
        "first_name, firstName",
        "a_b_c, aBC",
        "user, user"
    })
    void testToCamelCase(String input, String expected) {
        assertEquals(expected, StringUtil.toCamelCase(input));
    }

    /**
     * 测试驼峰转下划线
     */
    @ParameterizedTest
    @CsvSource({
        "userName, user_name",
        "firstName, first_name",
        "ABC, a_b_c",
        "user, user"
    })
    void testToSnakeCase(String input, String expected) {
        assertEquals(expected, StringUtil.toSnakeCase(input));
    }

    // ==================== join 测试 ====================

    /**
     * 测试数组连接
     */
    @Test
    void testJoin() {
        assertEquals("a,b,c", StringUtil.join(new String[]{"a", "b", "c"}, ","));
        assertEquals("a-b-c", StringUtil.join(new String[]{"a", "b", "c"}, "-"));
        assertEquals("a", StringUtil.join(new String[]{"a"}, ","));
        assertEquals("", StringUtil.join(new String[]{}, ","));
    }

    /**
     * 测试 null 数组连接
     */
    @Test
    void testJoinNullArray() {
        assertEquals("", StringUtil.join(null, ","));
    }

    // ==================== 测试数据提供方法 ====================

    /**
     * 提供空白字符串
     */
    private static Stream<Arguments> provideBlankStrings() {
        return Stream.of(
            Arguments.of((String) null),
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("  "),
            Arguments.of("\t"),
            Arguments.of("\n"),
            Arguments.of("\r\n")
        );
    }

    /**
     * 提供脱敏测试数据
     */
    private static Stream<Arguments> provideMaskData() {
        return Stream.of(
            Arguments.of("hello", 1, 1, '*', "h***o"),
            Arguments.of("world", 2, 2, '*', "wo***d"),
            Arguments.of("test", 0, 0, '*', "****"),
            Arguments.of("abcd", 4, 0, '*', "abcd"),
            Arguments.of("abcd", 0, 4, '*', "abcd")
        );
    }
}
