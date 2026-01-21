package com.xie.glm.system.dto.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ConfigQueryDTO 测试
 *
 * @author xie
 */
@DisplayName("ConfigQueryDTO 测试")
class ConfigQueryDTOTest {

    @DisplayName("测试基本属性")
    @ParameterizedTest(name = "配置名称: {0}, 配置键: {1}, 配置类型: {2}")
    @CsvSource({
            "系统名称, sys.name, Y",
            "系统作者, sys.author, Y",
            ", , N"
    })
    void testBasicProperties(String configName, String configKey, String configType) {
        ConfigQueryDTO query = new ConfigQueryDTO();
        query.setConfigName(configName);
        query.setConfigKey(configKey);
        query.setConfigType(configType);

        assertThat(query.getConfigName()).isEqualTo(configName);
        assertThat(query.getConfigKey()).isEqualTo(configKey);
        assertThat(query.getConfigType()).isEqualTo(configType);
    }

    @DisplayName("测试分页参数继承")
    @Test
    void testPaginationInheritance() {
        ConfigQueryDTO query = new ConfigQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setOrderByColumn("config_id");
        query.setIsAsc("desc");

        assertThat(query.getPageNum()).isEqualTo(1);
        assertThat(query.getPageSize()).isEqualTo(10);
        assertThat(query.getOrderByColumn()).isEqualTo("config_id");
        assertThat(query.getIsAsc()).isEqualTo("desc");
    }

    @DisplayName("测试配置类型筛选")
    @ParameterizedTest(name = "配置类型: {0}, 预期结果: {1}")
    @MethodSource("provideConfigTypes")
    void testConfigTypeFilter(String configType, String description) {
        ConfigQueryDTO query = new ConfigQueryDTO();
        query.setConfigType(configType);

        assertThat(query.getConfigType()).isEqualTo(configType);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideConfigTypes() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("Y", "系统内置配置"),
                org.junit.jupiter.params.provider.Arguments.of("N", "用户自定义配置"),
                org.junit.jupiter.params.provider.Arguments.of("", "全部配置")
        );
    }

    @DisplayName("测试配置名称模糊搜索")
    @ParameterizedTest(name = "配置名称关键词: {0}")
    @CsvSource({
            "系统",
            "名称",
            "sys"
    })
    void testConfigNameSearch(String configName) {
        ConfigQueryDTO query = new ConfigQueryDTO();
        query.setConfigName(configName);

        assertThat(query.getConfigName()).isEqualTo(configName);
    }

    @DisplayName("测试配置键名模糊搜索")
    @ParameterizedTest(name = "配置键名关键词: {0}")
    @CsvSource({
            "sys.name",
            "sys.author",
            "sys.version"
    })
    void testConfigKeySearch(String configKey) {
        ConfigQueryDTO query = new ConfigQueryDTO();
        query.setConfigKey(configKey);

        assertThat(query.getConfigKey()).isEqualTo(configKey);
    }

    @DisplayName("测试组合查询条件")
    @Test
    void testCombinedQuery() {
        ConfigQueryDTO query = new ConfigQueryDTO();
        query.setConfigName("系统");
        query.setConfigKey("sys");
        query.setConfigType("Y");
        query.setPageNum(1);
        query.setPageSize(20);
        query.setOrderByColumn("create_time");
        query.setIsAsc("desc");

        assertThat(query.getConfigName()).isEqualTo("系统");
        assertThat(query.getConfigKey()).isEqualTo("sys");
        assertThat(query.getConfigType()).isEqualTo("Y");
        assertThat(query.getPageNum()).isEqualTo(1);
        assertThat(query.getPageSize()).isEqualTo(20);
    }

    @DisplayName("测试空值查询条件")
    @Test
    void testNullQueryConditions() {
        ConfigQueryDTO query = new ConfigQueryDTO();

        assertThat(query.getConfigName()).isNull();
        assertThat(query.getConfigKey()).isNull();
        assertThat(query.getConfigType()).isNull();
    }

    @DisplayName("测试 Lombok @Data 注解生成的方法")
    @Test
    void testDataAnnotation() {
        ConfigQueryDTO query = new ConfigQueryDTO();
        query.setConfigName("系统名称");
        query.setConfigKey("sys.name");
        query.setConfigType("Y");

        // 测试 toString
        String toString = query.toString();
        assertThat(toString).contains("系统名称", "sys.name");

        // 测试 equals 和 hashCode
        ConfigQueryDTO query2 = new ConfigQueryDTO();
        query2.setConfigName("系统名称");
        query2.setConfigKey("sys.name");
        query2.setConfigType("Y");

        assertThat(query).isEqualTo(query2);
        assertThat(query.hashCode()).isEqualTo(query2.hashCode());
    }
}
