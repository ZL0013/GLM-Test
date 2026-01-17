package com.xie.glm.common.validation.validator;

import com.xie.glm.common.validation.annotation.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 手机号校验器
 *
 * <p>校验中国大陆手机号格式，支持以下运营商号段：
 * <ul>
 *   <li>中国移动：134-139、147、150-152、157-159、178、182-184、187-188、198</li>
 *   <li>中国联通：130-132、145、155-156、166、175-176、185-186</li>
 *   <li>中国电信：133、149、153、173-177、180-181、189、199</li>
 * </ul>
 *
 * @author xie
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    /**
     * 手机号正则表达式
     *
     * <p>规则：1开头，第二位为3-9，总共11位数字
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    private boolean required;

    @Override
    public void initialize(Phone constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null 值处理
        if (value == null) {
            return !required;
        }

        // 空字符串 "" 处理（非必填时通过）
        if (value.isEmpty()) {
            return !required;
        }

        // 纯空格字符串 trim 后为空，应该校验失败
        if (value.trim().isEmpty()) {
            return false;
        }

        // 校验手机号格式
        return PHONE_PATTERN.matcher(value).matches();
    }
}
