package com.xie.glm.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 日期工具类测试
 *
 * @author xie
 */
@DisplayName("日期工具类测试")
class DateUtilTest {

    // ==================== 日期格式化测试 ====================

    @ParameterizedTest
    @CsvSource({
        "2024-01-15 10:30:45, yyyy-MM-dd HH:mm:ss",
        "2024-12-31 23:59:59, yyyy-MM-dd HH:mm:ss"
    })
    @DisplayName("格式化日期 - 完整日期时间")
    void testFormat(String dateStr, String pattern) {
        LocalDateTime dateTime = DateUtil.parse(dateStr, pattern);
        String result = DateUtil.format(dateTime, pattern);
        assertEquals(dateStr, result);
    }

    @Test
    @DisplayName("格式化日期 - 仅日期")
    void testFormatDateOnly() {
        LocalDateTime dateTime = DateUtil.parse("2024-01-15", "yyyy-MM-dd");
        // 格式化回去会包含默认时间 00:00:00
        String result = DateUtil.format(dateTime, "yyyy-MM-dd HH:mm:ss");
        assertEquals("2024-01-15 00:00:00", result);
    }

    @Test
    @DisplayName("格式化日期 - 仅时间")
    void testFormatTimeOnly() {
        LocalDateTime dateTime = DateUtil.parse("10:30:45", "HH:mm:ss");
        // 格式化回去会包含默认日期 1970-01-01
        String result = DateUtil.format(dateTime, "yyyy-MM-dd HH:mm:ss");
        assertEquals("1970-01-01 10:30:45", result);
    }

    @Test
    @DisplayName("格式化当前日期 - 默认格式")
    void testFormatNow() {
        String result = DateUtil.formatNow();
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    // ==================== 日期解析测试 ====================

    @ParameterizedTest
    @CsvSource({
        "2024-01-15 10:30:45, yyyy-MM-dd HH:mm:ss, true",
        "2024-01-15, yyyy-MM-dd, true",
        "10:30:45, HH:mm:ss, true",
        "invalid, yyyy-MM-dd, false"
    })
    @DisplayName("解析日期字符串")
    void testParse(String dateStr, String pattern, boolean shouldSucceed) {
        if (shouldSucceed) {
            LocalDateTime result = DateUtil.parse(dateStr, pattern);
            assertNotNull(result);

            // 对于仅日期或仅时间的格式，不进行往返验证
            if (pattern.equals("yyyy-MM-dd") || pattern.equals("HH:mm:ss")) {
                // 验证解析成功即可
                assertNotNull(result);
            } else {
                // 完整日期时间格式进行往返验证
                assertEquals(dateStr, DateUtil.format(result, pattern));
            }
        } else {
            assertThrows(DateTimeParseException.class, () -> DateUtil.parse(dateStr, pattern));
        }
    }

    // ==================== 时间戳转换测试 ====================

    @Test
    @DisplayName("时间戳转日期时间 - 圆满测试")
    void testTimestampConversion() {
        // 创建一个本地时间
        LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 45);

        // 转换为时间戳
        long timestamp = DateUtil.toTimestamp(localDateTime);

        // 再转换回本地时间
        LocalDateTime result = DateUtil.fromTimestamp(timestamp);

        // 验证往返转换的一致性
        assertEquals(localDateTime, result);
    }

    @Test
    @DisplayName("当前时间戳")
    void testCurrentTimestamp() {
        long timestamp = DateUtil.currentTimestamp();
        assertTrue(timestamp > 0);
        // 验证时间戳在合理范围内（2024年左右）
        assertTrue(timestamp > 1700000000000L);
    }

    // ==================== Date 与 LocalDateTime 转换测试 ====================

    @Test
    @DisplayName("Date 与 LocalDateTime 互相转换 - 圆满测试")
    void testDateLocalDateTimeConversion() {
        // 创建一个本地时间
        LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 45);

        // 转换为 Date
        Date date = DateUtil.fromLocalDateTime(localDateTime);

        // 再转换回 LocalDateTime
        LocalDateTime result = DateUtil.toLocalDateTime(date);

        // 验证往返转换的一致性
        assertEquals(localDateTime, result);
    }

    @Test
    @DisplayName("Date 转 LocalDateTime - null 处理")
    void testToLocalDateTimeWithNull() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.toLocalDateTime(null));
    }

    @Test
    @DisplayName("LocalDateTime 转 Date - null 处理")
    void testFromLocalDateTimeWithNull() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.fromLocalDateTime(null));
    }

    // ==================== 日期计算测试 ====================

    @Test
    @DisplayName("日期加天数")
    void testPlusDays() {
        LocalDateTime base = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        LocalDateTime result = DateUtil.plusDays(base, 5);
        assertEquals(LocalDateTime.of(2024, 1, 20, 10, 30, 45), result);

        result = DateUtil.plusDays(base, -5);
        assertEquals(LocalDateTime.of(2024, 1, 10, 10, 30, 45), result);
    }

    @Test
    @DisplayName("日期加月数")
    void testPlusMonths() {
        LocalDateTime base = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        LocalDateTime result = DateUtil.plusMonths(base, 2);
        assertEquals(LocalDateTime.of(2024, 3, 15, 10, 30, 45), result);

        result = DateUtil.plusMonths(base, -1);
        assertEquals(LocalDateTime.of(2023, 12, 15, 10, 30, 45), result);
    }

    @Test
    @DisplayName("日期加年数")
    void testPlusYears() {
        LocalDateTime base = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        LocalDateTime result = DateUtil.plusYears(base, 1);
        assertEquals(LocalDateTime.of(2025, 1, 15, 10, 30, 45), result);

        result = DateUtil.plusYears(base, -1);
        assertEquals(LocalDateTime.of(2023, 1, 15, 10, 30, 45), result);
    }

    @Test
    @DisplayName("日期计算 - null 处理")
    void testPlusDaysWithNull() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.plusDays(null, 5));
    }

    // ==================== 日期比较测试 ====================

    @Test
    @DisplayName("日期比较 - 是否在之后")
    void testIsAfter() {
        LocalDateTime date1 = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        LocalDateTime date2 = LocalDateTime.of(2024, 1, 20, 10, 30, 45);

        assertTrue(DateUtil.isAfter(date2, date1));
        assertFalse(DateUtil.isAfter(date1, date2));
    }

    @Test
    @DisplayName("日期比较 - 是否在之前")
    void testIsBefore() {
        LocalDateTime date1 = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        LocalDateTime date2 = LocalDateTime.of(2024, 1, 20, 10, 30, 45);

        assertTrue(DateUtil.isBefore(date1, date2));
        assertFalse(DateUtil.isBefore(date2, date1));
    }

    @Test
    @DisplayName("日期比较 - 是否相等")
    void testIsEqual() {
        LocalDateTime date1 = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        LocalDateTime date2 = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        LocalDateTime date3 = LocalDateTime.of(2024, 1, 20, 10, 30, 45);

        assertTrue(DateUtil.isEqual(date1, date2));
        assertFalse(DateUtil.isEqual(date1, date3));
    }

    @Test
    @DisplayName("日期比较 - null 处理")
    void testIsAfterWithNull() {
        LocalDateTime date1 = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        assertThrows(IllegalArgumentException.class, () -> DateUtil.isAfter(null, date1));
        assertThrows(IllegalArgumentException.class, () -> DateUtil.isAfter(date1, null));
    }

    // ==================== 常用日期格式常量测试 ====================

    @Test
    @DisplayName("测试默认日期格式常量")
    void testDefaultPatterns() {
        assertNotNull(DateUtil.DEFAULT_DATE_TIME_PATTERN);
        assertNotNull(DateUtil.DEFAULT_DATE_PATTERN);
        assertNotNull(DateUtil.DEFAULT_TIME_PATTERN);
        assertEquals("yyyy-MM-dd HH:mm:ss", DateUtil.DEFAULT_DATE_TIME_PATTERN);
        assertEquals("yyyy-MM-dd", DateUtil.DEFAULT_DATE_PATTERN);
        assertEquals("HH:mm:ss", DateUtil.DEFAULT_TIME_PATTERN);
    }

    // ==================== 空值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("空值处理 - format")
    void testFormatWithNull(LocalDateTime dateTime) {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.format(dateTime, "yyyy-MM-dd"));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("空值处理 - parse")
    void testParseWithNull(String dateStr) {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.parse(dateStr, "yyyy-MM-dd"));
    }

    @Test
    @DisplayName("空值处理 - toTimestamp")
    void testToTimestampWithNull() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.toTimestamp(null));
    }

    @Test
    @DisplayName("空值处理 - fromTimestamp")
    void testFromTimestampWithNullTimestamp() {
        // 测试正常情况（时间戳为0是有效的，对应1970-01-01）
        LocalDateTime result = DateUtil.fromTimestamp(0L);
        assertNotNull(result);
    }

    // ==================== 参数化测试 - 日期计算 ====================

    @ParameterizedTest
    @MethodSource("provideDateCalculationData")
    @DisplayName("日期计算 - 参数化测试")
    void testDateCalculations(LocalDateTime base, int days, int months, int years, LocalDateTime expectedDays, LocalDateTime expectedMonths, LocalDateTime expectedYears) {
        assertEquals(expectedDays, DateUtil.plusDays(base, days));
        assertEquals(expectedMonths, DateUtil.plusMonths(base, months));
        assertEquals(expectedYears, DateUtil.plusYears(base, years));
    }

    private static Stream<Arguments> provideDateCalculationData() {
        LocalDateTime base = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        return Stream.of(
            Arguments.of(
                base,
                5, 2, 1,
                LocalDateTime.of(2024, 1, 20, 10, 30, 45),
                LocalDateTime.of(2024, 3, 15, 10, 30, 45),
                LocalDateTime.of(2025, 1, 15, 10, 30, 45)
            ),
            Arguments.of(
                base,
                -5, -1, -1,
                LocalDateTime.of(2024, 1, 10, 10, 30, 45),
                LocalDateTime.of(2023, 12, 15, 10, 30, 45),
                LocalDateTime.of(2023, 1, 15, 10, 30, 45)
            )
        );
    }
}
