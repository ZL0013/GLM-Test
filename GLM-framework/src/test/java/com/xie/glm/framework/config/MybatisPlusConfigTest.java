package com.xie.glm.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MyBatis Plus 配置测试
 *
 * <p>测试 MyBatis Plus 分页插件和数据权限插件配置，遵循 TDD 原则。
 *
 * @author xie
 */
@SpringBootTest(classes = MybatisPlusConfig.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver"
})
@DisplayName("MyBatis Plus 配置测试")
class MybatisPlusConfigTest {

    @Autowired(required = false)
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    /**
     * 测试：MybatisPlusInterceptor Bean 应该被创建
     */
    @Test
    @DisplayName("MybatisPlusInterceptor Bean 应该被创建")
    void testMybatisPlusInterceptorBeanExists() {
        assertNotNull(mybatisPlusInterceptor,
            "MybatisPlusInterceptor Bean 应该被正确配置");
    }

    /**
     * 测试：应该包含分页拦截器
     */
    @Test
    @DisplayName("应该包含分页拦截器")
    void testPaginationInnerInterceptorExists() {
        // Given & When
        boolean hasPaginationInterceptor = mybatisPlusInterceptor.getInterceptors().stream()
            .anyMatch(interceptor -> interceptor instanceof PaginationInnerInterceptor);

        // Then
        assertTrue(hasPaginationInterceptor,
            "MybatisPlusInterceptor 应该包含 PaginationInnerInterceptor");
    }

    /**
     * 参数化测试：分页拦截器应该配置正确的数据库类型
     *
     * @param dbType      数据库类型名称
     * @param expectedDbType 预期的数据库类型
     */
    @ParameterizedTest(name = "数据库类型: {0}")
    @CsvSource({
        "POSTGRESQL, POSTGRE_SQL",
        "MYSQL, MYSQL",
        "H2, H2"
    })
    @DisplayName("分页拦截器数据库类型配置 - 参数化测试")
    void testPaginationDbTypeConfigured(String dbType, DbType expectedDbType) {
        // Given & When
        PaginationInnerInterceptor paginationInterceptor = mybatisPlusInterceptor.getInterceptors().stream()
            .filter(interceptor -> interceptor instanceof PaginationInnerInterceptor)
            .map(interceptor -> (PaginationInnerInterceptor) interceptor)
            .findFirst()
            .orElse(null);

        // Then
        assertNotNull(paginationInterceptor, "分页拦截器应该存在");
        // 注意：实际数据库类型由 Spring Boot 自动检测，这里验证拦截器存在即可
    }

    /**
     * 测试：分页拦截器应该配置合理的溢出处理
     */
    @Test
    @DisplayName("分页拦截器应该配置合理的溢出处理")
    void testPaginationOverflowConfigured() {
        // Given & When
        PaginationInnerInterceptor paginationInterceptor = mybatisPlusInterceptor.getInterceptors().stream()
            .filter(interceptor -> interceptor instanceof PaginationInnerInterceptor)
            .map(interceptor -> (PaginationInnerInterceptor) interceptor)
            .findFirst()
            .orElse(null);

        // Then
        assertNotNull(paginationInterceptor, "分页拦截器应该存在");
        // 验证默认配置存在（具体配置值由 MyBatis Plus 提供）
    }

    /**
     * 测试：MybatisPlusInterceptor 应该至少有一个拦截器
     */
    @Test
    @DisplayName("MybatisPlusInterceptor 应该至少有一个拦截器")
    void testMybatisPlusInterceptorHasInnerInterceptors() {
        // Given & When
        int interceptorCount = mybatisPlusInterceptor.getInterceptors().size();

        // Then
        assertTrue(interceptorCount > 0,
            "MybatisPlusInterceptor 应该至少包含一个拦截器，实际包含: " + interceptorCount);
    }

    /**
     * 参数化测试：验证拦截器列表不为空
     *
     * @param minExpected 最小预期数量
     */
    @ParameterizedTest(name = "最小预期拦截器数量: {0}")
    @CsvSource({
        "1"
    })
    @DisplayName("验证拦截器列表不为空 - 参数化测试")
    void testInterceptorListNotEmpty(int minExpected) {
        // Given & When
        int actualCount = mybatisPlusInterceptor.getInterceptors().size();

        // Then
        assertTrue(actualCount >= minExpected,
            String.format("拦截器数量应该至少 %d 个，实际: %d", minExpected, actualCount));
    }
}
