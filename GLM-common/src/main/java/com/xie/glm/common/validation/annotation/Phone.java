package com.xie.glm.common.validation.annotation;

import com.xie.glm.common.validation.validator.PhoneValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 手机号校验注解
 *
 * <p>用于校验中国大陆手机号格式
 * <p>支持的手机号段：
 * <ul>
 *   <li>中国移动：134-139、147、150-152、157-159、178、182-184、187-188、198</li>
 *   <li>中国联通：130-132、145、155-156、166、175-176、185-186</li>
 *   <li>中国电信：133、149、153、173-177、180-181、189、199</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * public class UserDto {
 *     @Phone(message = "手机号格式不正确")
 *     private String phone;
 * }
 * }</pre>
 *
 * @author xie
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface Phone {

    /**
     * 校验失败时的错误消息
     *
     * @return 错误消息
     */
    String message() default "手机号格式不正确";

    /**
     * 校验分组
     *
     * @return 分组数组
     */
    Class<?>[] groups() default {};

    /**
     * 负载信息
     *
     * @return 负载数组
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 是否必填
     *
     * <p>true: 手机号不能为空
     * <p>false: 手机号可以为空，如果不为空则校验格式
     *
     * @return 是否必填
     */
    boolean required() default false;
}
