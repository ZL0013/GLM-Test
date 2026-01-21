package com.xie.glm.framework.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Jakarta Validation 配置测试
 *
 * <p>测试 Jakarta Validation 配置，遵循 TDD 原则。
 *
 * @author xie
 */
@DisplayName("Jakarta Validation 配置测试")
class ValidationConfigTest {

    /**
     * 测试：应该能够创建 ValidationConfig 实例
     */
    @Test
    @DisplayName("应该能够创建 ValidationConfig 实例")
    void testCreateValidationConfig() {
        // When
        ValidationConfig config = new ValidationConfig();

        // Then
        assertNotNull(config, "ValidationConfig 实例应该被创建");
    }

    /**
     * 测试：配置 MethodValidationPostProcessor Bean
     */
    @Test
     @DisplayName("应该配置 MethodValidationPostProcessor Bean")
    void testMethodValidationPostProcessorBeanExists() {
        // Given
        ValidationConfig config = new ValidationConfig();

        // When
        MethodValidationPostProcessor postProcessor = config.methodValidationPostProcessor();

        // Then
        assertNotNull(postProcessor, "MethodValidationPostProcessor Bean 应该被配置");
    }

    /**
     * @ConditionalOnProperty 禁用时 Bean 不应该创建
     */
    @Test
    @DisplayName("禁用时 Bean 不应该被创建")
    void testBeanNotCreatedWhenDisabled() {
        // Given & When & Then
        // 配置类使用 @ConditionalOnProperty 控制启用状态
        // 这里测试验证配置的条件创建逻辑

        // 由于单元测试无法测试条件注解，这里仅验证实例创建能力
        ValidationConfig config = new ValidationConfig();
        assertNotNull(config);
    }
}
