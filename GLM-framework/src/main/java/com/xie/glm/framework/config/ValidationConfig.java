package com.xie.glm.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

/**
 * Jakarta Validation 配置类
 *
 * <p>配置 Jakarta Bean Validation（Jakarta Validation），实现参数自动校验。
 *
 * <p>功能说明：
 * <ul>
 *   <li>自动校验 @Valid 注解的参数</li>
 *   <li>支持分组校验（Group Sequence）</li>
   *   *   在 Spring Boot 3.x 中，推荐使用 {@code @Validated} 和 {@code @Valid} 注解</li>
 * </ul>
 *
 * <p>设计原则：
 * <ul>
 *   <li>符合宪法第一条：简单性原则，仅配置必要功能</li>
   *   <li>符合宪法第四条：遵循 Spring Boot 自动配置约定</li>
   *   <li>默认启用，无需额外配置</li>
 * </ul>
 *
 * <p>配置说明：
 * <ul>
 *   *   Spring Boot 3.x 默认已包含 ValidationAutoConfiguration</li>
   *   *   自定义 {@link MethodValidationPostProcessor} 以支持更灵活的校验策略</li>
 *   *   使用 {@link OptionalValidatorFactoryBean} 提供 JSR-303 Validator</li>
 * </ul>
 *
 * @author xie
 */
@Configuration
public class ValidationConfig {

    /**
     * 配置 MethodValidationPostProcessor
     *
     * <p>启用方法参数校验，支持分组校验和自定义校验器。
     *
     * @return MethodValidationPostProcessor Bean
     */
    @Bean
    @ConditionalOnMissingBean(MethodValidationPostProcessor.class)
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * 配置 OptionalValidatorFactoryBean
     *
     * <p>提供 JSR-303 ValidatorFactory。
     *
     * @return OptionalValidatorFactoryBean Bean
     */
    @Bean
    @ConditionalOnMissingBean(OptionalValidatorFactoryBean.class)
    public OptionalValidatorFactoryBean optionalValidatorFactoryBean() {
        return new OptionalValidatorFactoryBean();
    }
}
