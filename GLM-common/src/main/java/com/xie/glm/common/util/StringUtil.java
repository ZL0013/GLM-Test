package com.xie.glm.common.util;

import java.util.regex.Pattern;

/**
 * 字符串工具类
 * <p>
 * 提供字符串判空、脱敏、截取、转换等常用工具方法。
 *
 * @author xie
 */
public final class StringUtil {

    /**
     * 数字正则表达式
     */
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");

    /**
     * 私有构造函数，防止实例化
     */
    private StringUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ==================== 判空方法 ====================

    /**
     * 判断字符串是否为空（null 或 ""）
     *
     * @param str 字符串
     * @return true 如果字符串为 null 或空字符串
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否为空白（null、空字符串或仅包含空白字符）
     *
     * @param str 字符串
     * @return true 如果字符串为 null、空字符串或仅包含空白字符
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 返回字符串的默认值，如果字符串为空白（不包括 null）
     *
     * @param str           字符串
     * @param defaultValue  默认值
     * @return null 如果输入为 null，否则返回字符串本身或默认值
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        if (str == null) {
            return null;
        }
        return str.trim().isEmpty() ? defaultValue : str;
    }

    // ==================== 脱敏方法 ====================

    /**
     * 手机号脱敏（保留前3位和后4位）
     * <p>
     * 例如：13812345678 → 138****5678
     *
     * @param phone 手机号
     * @return 脱敏后的手机号，无效格式返回空字符串
     */
    public static String maskPhone(String phone) {
        if (!isValidPhone(phone)) {
            return "";
        }
        return mask(phone, 3, 4, '*');
    }

    /**
     * 邮箱脱敏（保留首字符和域名，中间部分用星号替换）
     * <p>
     * 例如：test@example.com → t***@example.com
     *
     * @param email 邮箱
     * @return 脱敏后的邮箱，无效格式返回空字符串
     */
    public static String maskEmail(String email) {
        if (isBlank(email) || !email.contains("@")) {
            return "";
        }
        int atIndex = email.indexOf('@');
        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        if (localPart.isEmpty() || domain.isEmpty() || domain.equals("@")) {
            return "";
        }
        // 星号数量 = max(3, local part长度 - 2)
        int starsCount = Math.max(3, localPart.length() - 2);
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < starsCount; i++) {
            stars.append('*');
        }
        return localPart.charAt(0) + stars.toString() + domain;
    }

    /**
     * 身份证号脱敏（保留前6位和后4位）
     * <p>
     * 例如：110101199001011234 → 110101********1234
     *
     * @param idCard 身份证号
     * @return 脱敏后的身份证号，无效长度返回空字符串
     */
    public static String maskIdCard(String idCard) {
        if (isEmpty(idCard) || idCard.length() != 15 && idCard.length() != 18) {
            return "";
        }
        return mask(idCard, 6, 4, '*');
    }

    /**
     * 银行卡号脱敏（保留前6位和后4位，中间固定7个星号）
     * <p>
     * 例如：6222021234567890123 → 622202*******0123
     *
     * @param cardNo 银行卡号
     * @return 脱敏后的银行卡号
     */
    public static String maskBankCard(String cardNo) {
        if (isEmpty(cardNo) || cardNo.length() < 10) {
            return "";
        }
        String prefix = cardNo.substring(0, 6);
        String suffix = cardNo.substring(cardNo.length() - 4);
        return prefix + "*******" + suffix;
    }

    /**
     * 通用脱敏方法
     * <p>
     * 保留前缀和后缀，中间用指定字符替换（至少3个字符）
     *
     * @param str       字符串
     * @param prefixLen 保留前缀长度
     * @param suffixLen 保留后缀长度
     * @param maskChar  脱敏字符
     * @return 脱敏后的字符串
     */
    public static String mask(String str, int prefixLen, int suffixLen, char maskChar) {
        if (isEmpty(str)) {
            return "";
        }

        int len = str.length();
        int minMaskLen = 3;

        // 如果前缀+后缀长度超过或等于字符串长度，返回原字符串
        if (prefixLen + suffixLen >= len) {
            return str;
        }

        // 计算原始中间部分的长度
        int originalMiddleLen = len - prefixLen - suffixLen;

        // 计算实际mask长度（至少3个）
        int maskLen = Math.max(minMaskLen, originalMiddleLen);

        // 计算实际后缀长度（当mask扩展时减少后缀）
        int actualSuffixLen = Math.max(0, len - prefixLen - maskLen);
        // 当原始中间部分需要扩展时，尽量保留至少1个字符的后缀
        if (actualSuffixLen == 0 && originalMiddleLen < minMaskLen && len > prefixLen + minMaskLen - 1) {
            actualSuffixLen = 1;
        }

        String prefix = prefixLen > 0 ? str.substring(0, prefixLen) : "";
        String suffix = actualSuffixLen > 0 ? str.substring(len - actualSuffixLen) : "";

        StringBuilder mask = new StringBuilder(maskLen);
        for (int i = 0; i < maskLen; i++) {
            mask.append(maskChar);
        }

        return prefix + mask + suffix;
    }

    // ==================== 截取方法 ====================

    /**
     * 截取字符串，超过最大长度时截取到maxLength并添加后缀
     *
     * @param str       字符串
     * @param maxLength 截取位置
     * @param suffix    超长时的后缀
     * @return 截取后的字符串
     */
    public static String truncate(String str, int maxLength, String suffix) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        if (suffix == null) {
            suffix = "";
        }
        // 截取到 maxLength，然后添加后缀
        int truncLength = Math.min(maxLength, str.length());
        if (truncLength < 0) {
            truncLength = 0;
        }
        return str.substring(0, truncLength) + suffix;
    }

    // ==================== 比较方法 ====================

    /**
     * 忽略大小写判断两个字符串是否相等
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true 如果两个字符串忽略大小写后相等
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    // ==================== 判断方法 ====================

    /**
     * 判断字符串是否为纯数字
     *
     * @param str 字符串
     * @return true 如果字符串仅包含数字字符
     */
    public static boolean isNumeric(String str) {
        return !isEmpty(str) && NUMERIC_PATTERN.matcher(str).matches();
    }

    /**
     * 判断手机号格式是否有效
     *
     * @param phone 手机号
     * @return true 如果手机号格式有效
     */
    private static boolean isValidPhone(String phone) {
        return !isEmpty(phone) && phone.length() == 11 && phone.startsWith("1")
            && NUMERIC_PATTERN.matcher(phone).matches();
    }

    // ==================== 转换方法 ====================

    /**
     * 下划线转驼峰命名
     * <p>
     * 例如：user_name → userName
     *
     * @param str 下划线命名字符串
     * @return 驼峰命名字符串
     */
    public static String toCamelCase(String str) {
        if (isEmpty(str)) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }

    /**
     * 驼峰转下划线命名
     * <p>
     * 例如：userName → user_name
     *
     * @param str 驼峰命名字符串
     * @return 下划线命名字符串
     */
    public static String toSnakeCase(String str) {
        if (isEmpty(str)) {
            return str;
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                // 如果不是第一个字符，添加下划线
                if (result.length() > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    // ==================== 连接方法 ====================

    /**
     * 使用指定分隔符连接字符串数组
     *
     * @param array     字符串数组
     * @param separator 分隔符
     * @return 连接后的字符串
     */
    public static String join(String[] array, String separator) {
        if (array == null || array.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                result.append(separator);
            }
            if (array[i] != null) {
                result.append(array[i]);
            }
        }

        return result.toString();
    }
}
