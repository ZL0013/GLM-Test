package com.xie.glm.framework.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Knife4j 配置测试
 *
 * <p>测试 Knife4j API 文档配置，遵循 TDD 原则。
 *
 * @author xie
 */
@DisplayName("Knife4j 配置测试")
class Knife4jConfigTest {

    /**
     * 测试：创建 Knife4jConfig 实例
     */
    @Test
    @DisplayName("应该能够创建 Knife4jConfig 实例")
    void testCreateKnife4jConfig() {
        // When
        Knife4jConfig config = new Knife4jConfig();

        // Then
        assertNotNull(config, "Knife4jConfig 实例应该被创建");
    }

    /**
     * 测试：创建 customOpenAPI Bean
     */
    @Test
    @DisplayName("应该能够创建 OpenAPI Bean")
    void testCreateCustomOpenAPI() {
        // Given
        Knife4jConfig config = new Knife4jConfig();

        // When
        OpenAPI openAPI = config.customOpenAPI();

        // Then
        assertNotNull(openAPI, "OpenAPI Bean 应该被创建");
        assertNotNull(openAPI.getInfo(), "OpenAPI 应该包含 Info 信息");
        assertEquals("GLM-Test API 文档", openAPI.getInfo().getTitle(),
            "API 标题应该正确设置");
        assertEquals("0.0.1-SNAPSHOT", openAPI.getInfo().getVersion(),
            "API 版本应该正确设置");
    }

    /**
     * 测试：OpenAPI 应该包含 Contact 信息
     */
    @Test
    @DisplayName("OpenAPI 应该包含 Contact 信息")
    void testOpenAPIContainsContact() {
        // Given
        Knife4jConfig config = new Knife4jConfig();

        // When
        OpenAPI openAPI = config.customOpenAPI();

        // Then
        assertNotNull(openAPI.getInfo().getContact(), "OpenAPI 应该包含 Contact");
        assertEquals("xie", openAPI.getInfo().getContact().getName(),
            "联系人名称应该正确设置");
    }

    /**
     * 测试：OpenAPI 应该包含 License 信息
     */
    @Test
    @DisplayName("OpenAPI 应该包含 License 信息")
    void testOpenAPIContainsLicense() {
        // Given
        Knife4jConfig config = new Knife4jConfig();

        // When
        OpenAPI openAPI = config.customOpenAPI();

        // Then
        assertNotNull(openAPI.getInfo().getLicense(), "OpenAPI 应该包含 License");
        assertEquals("MIT License", openAPI.getInfo().getLicense().getName(),
            "许可证名称应该正确设置");
    }
}
