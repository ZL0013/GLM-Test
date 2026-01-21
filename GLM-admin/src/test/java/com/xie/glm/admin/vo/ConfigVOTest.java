package com.xie.glm.admin.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ConfigVO 测试
 *
 * @author xie
 */
@DisplayName("ConfigVO 测试")
class ConfigVOTest {

    @DisplayName("测试基本属性")
    @ParameterizedTest(name = "配置名称: {0}, 配置键: {1}, 配置值: {2}")
    @CsvSource({
            "系统名称, sys.name, GLM管理系统",
            "系统作者, sys.author, xie",
            "系统版本, sys.version, 1.0.0"
    })
    void testBasicProperties(String configName, String configKey, String configValue) {
        ConfigVO vo = new ConfigVO();
        vo.setConfigName(configName);
        vo.setConfigKey(configKey);
        vo.setConfigValue(configValue);

        assertThat(vo.getConfigName()).isEqualTo(configName);
        assertThat(vo.getConfigKey()).isEqualTo(configKey);
        assertThat(vo.getConfigValue()).isEqualTo(configValue);
    }

    @DisplayName("测试配置类型文本")
    @ParameterizedTest(name = "配置类型: {0}, 类型文本: {1}")
    @CsvSource({
            "Y, 系统内置",
            "N, 用户自定义"
    })
    void testConfigTypeText(String configType, String configTypeText) {
        ConfigVO vo = new ConfigVO();
        vo.setConfigType(configType);
        vo.setConfigTypeText(configTypeText);

        assertThat(vo.getConfigType()).isEqualTo(configType);
        assertThat(vo.getConfigTypeText()).isEqualTo(configTypeText);
    }

    @DisplayName("测试 Builder 模式")
    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();

        ConfigVO vo = ConfigVO.builder()
                .configId(1L)
                .configName("系统名称")
                .configKey("sys.name")
                .configValue("GLM管理系统")
                .configType("Y")
                .configTypeText("系统内置")
                .remark("系统名称配置")
                .createTime(now)
                .updateTime(now)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        assertThat(vo.getConfigId()).isEqualTo(1L);
        assertThat(vo.getConfigName()).isEqualTo("系统名称");
        assertThat(vo.getConfigKey()).isEqualTo("sys.name");
        assertThat(vo.getConfigValue()).isEqualTo("GLM管理系统");
        assertThat(vo.getConfigType()).isEqualTo("Y");
        assertThat(vo.getConfigTypeText()).isEqualTo("系统内置");
    }

    @DisplayName("测试完整 VO")
    @Test
    void testFullVO() {
        LocalDateTime now = LocalDateTime.now();

        ConfigVO vo = new ConfigVO();
        vo.setConfigId(1L);
        vo.setConfigName("系统名称");
        vo.setConfigKey("sys.name");
        vo.setConfigValue("GLM管理系统");
        vo.setConfigType("Y");
        vo.setConfigTypeText("系统内置");
        vo.setRemark("系统名称配置");
        vo.setCreateTime(now);
        vo.setUpdateTime(now);
        vo.setCreatedBy("admin");
        vo.setUpdatedBy("admin");

        assertThat(vo.getConfigId()).isEqualTo(1L);
        assertThat(vo.getConfigName()).isEqualTo("系统名称");
        assertThat(vo.getConfigKey()).isEqualTo("sys.name");
        assertThat(vo.getConfigValue()).isEqualTo("GLM管理系统");
        assertThat(vo.getConfigType()).isEqualTo("Y");
        assertThat(vo.getConfigTypeText()).isEqualTo("系统内置");
        assertThat(vo.getRemark()).isEqualTo("系统名称配置");
        assertThat(vo.getCreateTime()).isEqualTo(now);
        assertThat(vo.getUpdateTime()).isEqualTo(now);
        assertThat(vo.getCreatedBy()).isEqualTo("admin");
        assertThat(vo.getUpdatedBy()).isEqualTo("admin");
    }

    @DisplayName("测试 Lombok 生成的方法")
    @Test
    void testLombokMethods() {
        ConfigVO vo1 = new ConfigVO();
        vo1.setConfigId(1L);
        vo1.setConfigName("测试配置");

        // 测试 toString
        String toString = vo1.toString();
        assertThat(toString).contains("测试配置");

        // 测试 equals 和 hashCode
        ConfigVO vo2 = new ConfigVO();
        vo2.setConfigId(1L);
        vo2.setConfigName("测试配置");

        assertThat(vo1).isEqualTo(vo2);
        assertThat(vo1.hashCode()).isEqualTo(vo2.hashCode());
    }

    @DisplayName("测试继承 ConfigDTO 的字段")
    @Test
    void testInheritsFromConfigDTO() {
        ConfigVO vo = new ConfigVO();
        vo.setConfigId(1L);
        vo.setConfigName("系统名称");
        vo.setConfigKey("sys.name");
        vo.setConfigValue("GLM管理系统");
        vo.setConfigType("Y");
        vo.setRemark("系统名称配置");

        assertThat(vo.getConfigId()).isEqualTo(1L);
        assertThat(vo.getConfigName()).isEqualTo("系统名称");
        assertThat(vo.getConfigKey()).isEqualTo("sys.name");
        assertThat(vo.getConfigValue()).isEqualTo("GLM管理系统");
        assertThat(vo.getConfigType()).isEqualTo("Y");
        assertThat(vo.getRemark()).isEqualTo("系统名称配置");
    }
}
