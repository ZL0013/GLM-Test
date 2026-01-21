package com.xie.glm.system.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ConfigDTO 测试
 *
 * @author xie
 */
@DisplayName("ConfigDTO 测试")
class ConfigDTOTest {

    @DisplayName("测试基本属性")
    @ParameterizedTest(name = "配置名称: {0}, 配置键: {1}, 配置值: {2}")
    @CsvSource({
            "系统名称, sys.name, GLM管理系统",
            "系统作者, sys.author, xie",
            "系统版本, sys.version, 1.0.0"
    })
    void testBasicProperties(String configName, String configKey, String configValue) {
        ConfigDTO dto = new ConfigDTO();
        dto.setConfigName(configName);
        dto.setConfigKey(configKey);
        dto.setConfigValue(configValue);

        assertThat(dto.getConfigName()).isEqualTo(configName);
        assertThat(dto.getConfigKey()).isEqualTo(configKey);
        assertThat(dto.getConfigValue()).isEqualTo(configValue);
    }

    @DisplayName("测试配置ID")
    @ParameterizedTest(name = "配置ID: {0}")
    @CsvSource({
            "1",
            "100",
            "1000"
    })
    void testConfigId(Long configId) {
        ConfigDTO dto = new ConfigDTO();
        dto.setConfigId(configId);

        assertThat(dto.getConfigId()).isEqualTo(configId);
    }

    @DisplayName("测试配置类型")
    @ParameterizedTest(name = "配置类型: {0}")
    @CsvSource({
            "Y",
            "N"
    })
    void testConfigType(String configType) {
        ConfigDTO dto = new ConfigDTO();
        dto.setConfigType(configType);

        assertThat(dto.getConfigType()).isEqualTo(configType);
    }

    @DisplayName("测试备注")
    @ParameterizedTest(name = "备注: {0}")
    @org.junit.jupiter.params.provider.ValueSource(strings = {
            "系统名称配置",
            "作者信息配置",
            ""
    })
    void testRemark(String remark) {
        ConfigDTO dto = new ConfigDTO();
        dto.setRemark(remark);

        assertThat(dto.getRemark()).isEqualTo(remark);
    }

    @DisplayName("测试时间字段")
    @Test
    void testTimeFields() {
        ConfigDTO dto = new ConfigDTO();
        LocalDateTime now = LocalDateTime.now();
        dto.setCreateTime(now);
        dto.setUpdateTime(now);

        assertThat(dto.getCreateTime()).isEqualTo(now);
        assertThat(dto.getUpdateTime()).isEqualTo(now);
    }

    @DisplayName("测试审计字段")
    @ParameterizedTest(name = "创建人: {0}, 更新人: {1}")
    @CsvSource({
            "admin, admin",
            "user1, user2",
            "system, admin"
    })
    void testAuditFields(String createdBy, String updatedBy) {
        ConfigDTO dto = new ConfigDTO();
        dto.setCreatedBy(createdBy);
        dto.setUpdatedBy(updatedBy);

        assertThat(dto.getCreatedBy()).isEqualTo(createdBy);
        assertThat(dto.getUpdatedBy()).isEqualTo(updatedBy);
    }

    @DisplayName("测试是否系统内置配置")
    @ParameterizedTest(name = "配置类型: {0}, 预期: {1}")
    @CsvSource({
            "Y, true",
            "N, false"
    })
    void testIsSystemBuiltIn(String configType, boolean expected) {
        ConfigDTO dto = new ConfigDTO();
        dto.setConfigType(configType);

        boolean isSystemBuiltIn = "Y".equals(dto.getConfigType());
        assertThat(isSystemBuiltIn).isEqualTo(expected);
    }

    @DisplayName("测试 Builder 模式")
    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();

        ConfigDTO dto = ConfigDTO.builder()
                .configId(1L)
                .configName("系统名称")
                .configKey("sys.name")
                .configValue("GLM管理系统")
                .configType("Y")
                .remark("系统名称配置")
                .createTime(now)
                .updateTime(now)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        assertThat(dto.getConfigId()).isEqualTo(1L);
        assertThat(dto.getConfigName()).isEqualTo("系统名称");
        assertThat(dto.getConfigKey()).isEqualTo("sys.name");
        assertThat(dto.getConfigValue()).isEqualTo("GLM管理系统");
        assertThat(dto.getConfigType()).isEqualTo("Y");
    }

    @DisplayName("测试完整DTO")
    @Test
    void testFullDTO() {
        LocalDateTime now = LocalDateTime.now();

        ConfigDTO dto = new ConfigDTO();
        dto.setConfigId(1L);
        dto.setConfigName("系统名称");
        dto.setConfigKey("sys.name");
        dto.setConfigValue("GLM管理系统");
        dto.setConfigType("Y");
        dto.setRemark("系统名称配置");
        dto.setCreateTime(now);
        dto.setUpdateTime(now);
        dto.setCreatedBy("admin");
        dto.setUpdatedBy("admin");

        assertThat(dto.getConfigId()).isEqualTo(1L);
        assertThat(dto.getConfigName()).isEqualTo("系统名称");
        assertThat(dto.getConfigKey()).isEqualTo("sys.name");
        assertThat(dto.getConfigValue()).isEqualTo("GLM管理系统");
        assertThat(dto.getConfigType()).isEqualTo("Y");
        assertThat(dto.getRemark()).isEqualTo("系统名称配置");
        assertThat(dto.getCreateTime()).isEqualTo(now);
        assertThat(dto.getUpdateTime()).isEqualTo(now);
        assertThat(dto.getCreatedBy()).isEqualTo("admin");
        assertThat(dto.getUpdatedBy()).isEqualTo("admin");
    }

    @DisplayName("测试 Lombok 生成的方法")
    @Test
    void testLombokMethods() {
        ConfigDTO dto1 = new ConfigDTO();
        dto1.setConfigId(1L);
        dto1.setConfigName("测试配置");

        // 测试 toString
        String toString = dto1.toString();
        assertThat(toString).contains("测试配置");

        // 测试 equals 和 hashCode
        ConfigDTO dto2 = new ConfigDTO();
        dto2.setConfigId(1L);
        dto2.setConfigName("测试配置");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }
}
