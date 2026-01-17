package com.xie.glm.common.util;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * 日期工具类
 *
 * <p>基于 Java 8+ java.time API 实现，提供日期格式化、解析、时间戳转换、日期计算等功能。
 *
 * @author xie
 */
public final class DateUtil {

    /** 默认日期时间格式：yyyy-MM-dd HH:mm:ss */
    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /** 默认日期格式：yyyy-MM-dd */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /** 默认时间格式：HH:mm:ss */
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /** 系统默认时区 */
    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

    /**
     * 私有构造函数，防止实例化
     */
    private DateUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ==================== 日期格式化 ====================

    /**
     * 格式化日期时间为字符串
     *
     * @param dateTime 日期时间
     * @param pattern  格式化模式
     * @return 格式化后的字符串
     * @throws IllegalArgumentException 如果 dateTime 或 pattern 为 null
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }
        if (pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("pattern cannot be null or blank");
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化当前日期时间为字符串（使用默认格式）
     *
     * @return 格式化后的字符串，格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatNow() {
        return format(LocalDateTime.now(), DEFAULT_DATE_TIME_PATTERN);
    }

    // ==================== 日期解析 ====================

    /**
     * 解析日期字符串为 LocalDateTime
     *
     * <p>支持以下格式：
     * <ul>
     *   <li>完整日期时间：yyyy-MM-dd HH:mm:ss</li>
     *   <li>仅日期：yyyy-MM-dd（时间部分默认为 00:00:00）</li>
     *   <li>仅时间：HH:mm:ss（日期部分默认为 1970-01-01）</li>
     * </ul>
     *
     * @param dateStr 日期字符串
     * @param pattern 格式化模式
     * @return LocalDateTime 对象
     * @throws IllegalArgumentException 如果 dateStr 或 pattern 为 null
     * @throws DateTimeParseException   如果 dateStr 格式不匹配 pattern
     */
    public static LocalDateTime parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new IllegalArgumentException("dateStr cannot be null or blank");
        }
        if (pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("pattern cannot be null or blank");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // 根据格式判断使用何种解析方式
        if (pattern.equals("yyyy-MM-dd")) {
            // 仅日期，时间部分默认为 00:00:00
            return java.time.LocalDate.parse(dateStr, formatter).atStartOfDay();
        } else if (pattern.equals("HH:mm:ss")) {
            // 仅时间，日期部分默认为 1970-01-01
            return java.time.LocalTime.parse(dateStr, formatter).atDate(java.time.LocalDate.of(1970, 1, 1));
        } else {
            // 完整日期时间
            return LocalDateTime.parse(dateStr, formatter);
        }
    }

    // ==================== 时间戳转换 ====================

    /**
     * 将毫秒时间戳转换为 LocalDateTime
     *
     * @param timestamp 毫秒时间戳
     * @return LocalDateTime 对象
     */
    public static LocalDateTime fromTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), SYSTEM_ZONE);
    }

    /**
     * 将 LocalDateTime 转换为毫秒时间戳
     *
     * @param dateTime LocalDateTime 对象
     * @return 毫秒时间戳
     * @throws IllegalArgumentException 如果 dateTime 为 null
     */
    public static long toTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }
        return dateTime.atZone(SYSTEM_ZONE).toInstant().toEpochMilli();
    }

    /**
     * 获取当前时间的毫秒时间戳
     *
     * @return 毫秒时间戳
     */
    public static long currentTimestamp() {
        return System.currentTimeMillis();
    }

    // ==================== Date 与 LocalDateTime 转换 ====================

    /**
     * 将 java.util.Date 转换为 LocalDateTime
     *
     * @param date Date 对象
     * @return LocalDateTime 对象
     * @throws IllegalArgumentException 如果 date 为 null
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }
        return LocalDateTime.ofInstant(date.toInstant(), SYSTEM_ZONE);
    }

    /**
     * 将 LocalDateTime 转换为 java.util.Date
     *
     * @param dateTime LocalDateTime 对象
     * @return Date 对象
     * @throws IllegalArgumentException 如果 dateTime 为 null
     */
    public static Date fromLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }
        return Date.from(dateTime.atZone(SYSTEM_ZONE).toInstant());
    }

    // ==================== 日期计算 ====================

    /**
     * 日期加天数
     *
     * @param dateTime 日期时间
     * @param days     要增加的天数（可为负数）
     * @return 计算后的日期时间
     * @throws IllegalArgumentException 如果 dateTime 为 null
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, int days) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }
        return dateTime.plusDays(days);
    }

    /**
     * 日期加月数
     *
     * @param dateTime 日期时间
     * @param months   要增加的月数（可为负数）
     * @return 计算后的日期时间
     * @throws IllegalArgumentException 如果 dateTime 为 null
     * @throws DateTimeException       如果结果超出支持的范围
     */
    public static LocalDateTime plusMonths(LocalDateTime dateTime, int months) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }
        return dateTime.plusMonths(months);
    }

    /**
     * 日期加年数
     *
     * @param dateTime 日期时间
     * @param years    要增加的年数（可为负数）
     * @return 计算后的日期时间
     * @throws IllegalArgumentException 如果 dateTime 为 null
     * @throws DateTimeException       如果结果超出支持的范围
     */
    public static LocalDateTime plusYears(LocalDateTime dateTime, int years) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }
        return dateTime.plusYears(years);
    }

    // ==================== 日期比较 ====================

    /**
     * 判断 date1 是否在 date2 之后
     *
     * @param date1 日期时间1
     * @param date2 日期时间2
     * @return 如果 date1 在 date2 之后返回 true，否则返回 false
     * @throws IllegalArgumentException 如果 date1 或 date2 为 null
     */
    public static boolean isAfter(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("date1 and date2 cannot be null");
        }
        return date1.isAfter(date2);
    }

    /**
     * 判断 date1 是否在 date2 之前
     *
     * @param date1 日期时间1
     * @param date2 日期时间2
     * @return 如果 date1 在 date2 之前返回 true，否则返回 false
     * @throws IllegalArgumentException 如果 date1 或 date2 为 null
     */
    public static boolean isBefore(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("date1 and date2 cannot be null");
        }
        return date1.isBefore(date2);
    }

    /**
     * 判断两个日期是否相等
     *
     * @param date1 日期时间1
     * @param date2 日期时间2
     * @return 如果两个日期相等返回 true，否则返回 false
     * @throws IllegalArgumentException 如果 date1 或 date2 为 null
     */
    public static boolean isEqual(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("date1 and date2 cannot be null");
        }
        return date1.isEqual(date2);
    }
}
