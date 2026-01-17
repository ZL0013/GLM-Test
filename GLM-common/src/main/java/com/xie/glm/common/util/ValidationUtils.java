package com.xie.glm.common.util;

import com.xie.glm.common.validation.annotation.Phone;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

/**
 * 校验工具类
 *
 * <p>提供常用的校验工具方法。
 *
 * <p>设计原则：
 * <ul>
 *   <li>符合宪法第一条：简单性原则，只提供必要的工具方法</li>
 *   *   使用 Jakarta Validation 标准库</li>
 * </ul>
 *
 * @author xie
 */
public class ValidationUtils {

    private static final Validator VALIDATOR;

    static {
        // 初始化 Validator
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 私有构造方法，防止实例化
     */
    private ValidationUtils() {
    }

    /**
     * 校验手机号
     *
     * <p>校验手机号格式是否正确。
     *
     * @param phone 手机号
     * @return true-格式正确，false-格式错误
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }

        // 长度校验
        if (phone.length() != 11) {
            return false;
        }

        // 正则校验：1 开头 + 10 位数字
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return false;
        }

        // 号段校验
        String prefix = phone.substring(0, 3);
        String[] validPrefixes = {"13", "15", "17", "19"};
        boolean isValidPrefix = false;
        for (String validPrefix : validPrefixes) {
            if (prefix.equals(validPrefix)) {
                isValidPrefix = true;
                break;
            }
        }

        return isValidPrefix;
    }

    /**
     * 校验字符串是否为空
     *
     * @param str 待校验字符串
     * @return true-为空，false-非空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 校验字符串是否不为空
     *
     * @param str 待校验字符串
     * @return true-非空，false-空或null
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 手动触发校验
     *
     * <p>手动触发对象校验，用于需要手动校验的场景。
     *
     * @param object 待校验对象
     * <T> 对象类型
     * @return 校验结果
     */
    public static <T> ValidationResult validate(T object) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);

        if (violations.isEmpty()) {
            return ValidationResult.success();
        }

        String errorMessages = violations.stream()
            .map(ConstraintViolation::getMessage)
            .reduce((a, b) -> a + "; " + b)
            .orElse("校验失败");

        return ValidationResult.fail(errorMessages);
    }

    /**
     * 手动触发校验（支持分组）
     *
     * <p>手动触发对象校验，支持分组校验。
     *
     * @param object   待校验对象
     * @param groups  校验组
     * <T>       对象类型
     * @return 校验结果
     */
    public static <T> ValidationResult validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object, groups);

        if (violations.isEmpty()) {
            return ValidationResult.success();
        }

        String errorMessages = violations.stream()
            .map(ConstraintViolation::getMessage)
            .reduce((a, b) -> a + "; " + b)
            .orElse("校验失败");

        return ValidationResult.fail(errorMessages);
    }

    /**
     * 校验结果类（不可变 record）
     */
    public record ValidationResult(boolean valid, String errors) {
        /**
         * 创建成功结果
         */
        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        /**
         * 创建失败结果
         *
         * @param errors 错误信息
         * @return 失败结果
         */
        public static ValidationResult fail(String errors) {
            return new ValidationResult(false, errors);
        }

        /**
         * 判断是否成功
         *
         * @return true-成功，false-失败
         */
        public boolean isSuccess() {
            return valid;
        }
    }
}
