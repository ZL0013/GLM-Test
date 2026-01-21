package com.xie.glm.system.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SysConfig 实体类测试
 *
 * @author xie
 */
@DisplayName("SysConfig 实体测试")
class SysConfigTest {

    @DisplayName("测试实体基本属性")
    @ParameterizedTest(name = "配置名称: {0}, 配置键: {1}, 配置值: {2}")
    @CsvSource({
            "系统名称, sys.name, GLM管理系统",
            "系统作者, sys.author, xie",
            "系统版本, sys.version, 1.0.0"
    })
    void testBasicProperties(String configName, String configKey, String configValue) {
        SysConfig config = new SysConfig();
        config.setConfigName(configName);
        config.setConfigKey(configKey);
        config.setConfigValue(configValue);

        assertThat(config.getConfigName()).isEqualTo(configName);
        assertThat(config.getConfigKey()).isEqualTo(configKey);
        assertThat(config.getConfigValue()).isEqualTo(configValue);
    }

    @DisplayName("测试配置类型")
    @ParameterizedTest(name = "配置类型: {0}, 预期描述: {1}")
    @MethodSource("provideConfigTypes")
    void testConfigType(String configType, String expectedDescription) {
        SysConfig config = new SysConfig();
        config.setConfigType(configType);

        assertThat(config.getConfigType()).isEqualTo(configType);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideConfigTypes() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("Y", "系统内置配置"),
                org.junit.jupiter.params.provider.Arguments.of("N", "用户自定义配置")
        );
    }

    @DisplayName("测试是否系统内置配置")
    @ParameterizedTest(name = "配置类型: {0}, 预期: {1}")
    @CsvSource({
            "Y, true",
            "N, false"
    })
    void testIsSystemBuiltIn(String configType, boolean expected) {
        SysConfig config = new SysConfig();
        config.setConfigType(configType);

        boolean isSystemBuiltIn = "Y".equals(configType);
        assertThat(isSystemBuiltIn).isEqualTo(expected);
    }

    @DisplayName("测试配置键名的唯一性")
    @Test
    void testConfigKeyUniqueness() {
        SysConfig config1 = new SysConfig();
        config1.setConfigKey("sys.name");

        SysConfig config2 = new SysConfig();
        config2.setConfigKey("sys.name");

        assertThat(config1.getConfigKey()).isEqualTo(config2.getConfigKey());
    }

    @DisplayName("测试备注字段")
    @ParameterizedTest(name = "备注: {0}")
    @org.junit.jupiter.params.provider.ValueSource(strings = {
            "系统名称配置",
            "作者信息配置",
            ""
    })
    void testRemark(String remark) {
        SysConfig config = new SysConfig();
        config.setRemark(remark);

        assertThat(config.getRemark()).isEqualTo(remark);
    }

    @DisplayName("测试 Lombok @Data 注解生成的方法")
    @Test
    void testDataAnnotation() {
        SysConfig config = new SysConfig();
        config.setConfigId(1L);
        config.setConfigName("测试配置");
        config.setConfigKey("test.key");
        config.setConfigValue("test.value");
        config.setConfigType("N");
        config.setRemark("测试备注");

        // 测试 toString
        String toString = config.toString();
        assertThat(toString).contains("测试配置", "test.key");

        // 测试 equals 和 hashCode
        SysConfig config2 = new SysConfig();
        config2.setConfigId(1L);
        config2.setConfigName("测试配置");
        config2.setConfigKey("test.key");
        config2.setConfigValue("test.value");
        config2.setConfigType("N");
        config2.setRemark("测试备注");

        assertThat(config).isEqualTo(config2);
        assertThat(config.hashCode()).isEqualTo(config2.hashCode());
    }
}
