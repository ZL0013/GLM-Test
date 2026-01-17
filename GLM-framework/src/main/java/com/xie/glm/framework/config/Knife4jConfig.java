package com.xie.glm.framework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j API 文档配置类
 *
 * <p>基于 OpenAPI 3.0 规范，提供 RESTful API 文档展示。
 *
 * <p>功能说明：
 * <ul>
 *   <li>自动扫描所有 @RestController 接口</li>
 *   <li>在线调试 API 接口</li>
   *   <li>展示接口参数、返回值、示例</li>
 * </ul>
 *
 * <p>设计原则：
 * <ul>
 *   <li>符合宪法第一条：简单性原则，仅配置必要功能</li>
   <li>符合宪法第四条：遵循 Spring Boot 自动配置约定</li>
 * </ul>
 *
 * <p>配置开关：通过 {@code knife4j.enable=true} 启用，默认关闭
 *
 * <p>访问地址：
 * <ul>
 *   <li>文档页：http://localhost:8080/doc.html</li>
   * <li>Swagger JSON：http://localhost:8080/v3/api-docs</li>
 * </ul>
 *
 * @author xie
 */
@Configuration
@ConditionalOnProperty(name = "knife4j.enable", havingValue = "true", matchIfMissing = false)
public class Knife4jConfig {

    /**
     * 配置 OpenAPI 信息
     *
     * <p>包含 API 标题、版本、描述、联系人等信息。
     *
     * @return OpenAPI 信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GLM-Test API 文档")
                        .version("0.0.1-SNAPSHOT")
                        .description("GLM-Test 快速开发框架 API 文档")
                        .contact(new Contact()
                                .name("xie")
                                .email("xie@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
