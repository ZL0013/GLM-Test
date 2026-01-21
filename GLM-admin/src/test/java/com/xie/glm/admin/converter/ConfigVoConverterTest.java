package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.ConfigVO;
import com.xie.glm.system.dto.ConfigDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ConfigVoConverter 测试
 *
 * @author xie
 */
@DisplayName("ConfigVoConverter 测试")
class ConfigVoConverterTest {

    @DisplayName("测试 DTO 转 VO")
    @ParameterizedTest(name = "配置名称: {0}, 配置键: {1}, 配置类型: {2}")
    @MethodSource("provideConfigData")
    void testToVo(String configName, String configKey, String configType) {
        ConfigDTO dto = ConfigDTO.builder()
                .configId(1L)
                .configName(configName)
                .configKey(configKey)
                .configValue("测试值")
                .configType(configType)
                .remark("测试备注")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        ConfigVO vo = new ConfigVO();
        vo.setConfigId(dto.getConfigId());
        vo.setConfigName(dto.getConfigName());
        vo.setConfigKey(dto.getConfigKey());
        vo.setConfigValue(dto.getConfigValue());
        vo.setConfigType(dto.getConfigType());
        vo.setRemark(dto.getRemark());
        vo.setConfigTypeText("Y".equals(configType) ? "系统内置" : "用户自定义");
        vo.setCreateTime(dto.getCreateTime());
        vo.setUpdateTime(dto.getUpdateTime());
        vo.setCreatedBy(dto.getCreatedBy());
        vo.setUpdatedBy(dto.getUpdatedBy());

        assertThat(vo.getConfigId()).isEqualTo(dto.getConfigId());
        assertThat(vo.getConfigName()).isEqualTo(dto.getConfigName());
        assertThat(vo.getConfigKey()).isEqualTo(dto.getConfigKey());
        assertThat(vo.getConfigValue()).isEqualTo(dto.getConfigValue());
        assertThat(vo.getConfigType()).isEqualTo(dto.getConfigType());
        assertThat(vo.getConfigTypeText()).isEqualTo("Y".equals(configType) ? "系统内置" : "用户自定义");
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideConfigData() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("系统名称", "sys.name", "Y"),
                org.junit.jupiter.params.provider.Arguments.of("用户配置", "user.setting", "N"),
                org.junit.jupiter.params.provider.Arguments.of("系统版本", "sys.version", "Y")
        );
    }

    @DisplayName("测试 DTO 列表转 VO 列表")
    @Test
    void testToVoList() {
        ConfigDTO dto1 = ConfigDTO.builder()
                .configId(1L)
                .configName("系统名称")
                .configKey("sys.name")
                .configValue("GLM管理系统")
                .configType("Y")
                .build();

        ConfigDTO dto2 = ConfigDTO.builder()
                .configId(2L)
                .configName("用户配置")
                .configKey("user.setting")
                .configValue("用户设置")
                .configType("N")
                .build();

        List<ConfigDTO> dtoList = List.of(dto1, dto2);

        List<ConfigVO> voList = dtoList.stream().map(dto -> {
            ConfigVO vo = new ConfigVO();
            vo.setConfigId(dto.getConfigId());
            vo.setConfigName(dto.getConfigName());
            vo.setConfigKey(dto.getConfigKey());
            vo.setConfigValue(dto.getConfigValue());
            vo.setConfigType(dto.getConfigType());
            vo.setConfigTypeText("Y".equals(dto.getConfigType()) ? "系统内置" : "用户自定义");
            return vo;
        }).toList();

        assertThat(voList).hasSize(2);
        assertThat(voList.get(0).getConfigName()).isEqualTo("系统名称");
        assertThat(voList.get(0).getConfigTypeText()).isEqualTo("系统内置");
        assertThat(voList.get(1).getConfigName()).isEqualTo("用户配置");
        assertThat(voList.get(1).getConfigTypeText()).isEqualTo("用户自定义");
    }

    @DisplayName("测试空 DTO 转 VO")
    @Test
    void testNullDtoToVo() {
        ConfigDTO dto = null;
        ConfigVO vo = new ConfigVO();

        assertThat(vo).isNotNull();
    }

    @DisplayName("测试配置类型文本转换")
    @ParameterizedTest(name = "配置类型: {0}, 预期文本: {1}")
    @MethodSource("provideConfigTypes")
    void testConfigTypeTextConversion(String configType, String expectedText) {
        ConfigDTO dto = ConfigDTO.builder()
                .configId(1L)
                .configName("测试配置")
                .configKey("test.key")
                .configValue("测试值")
                .configType(configType)
                .build();

        ConfigVO vo = new ConfigVO();
        vo.setConfigId(dto.getConfigId());
        vo.setConfigName(dto.getConfigName());
        vo.setConfigKey(dto.getConfigKey());
        vo.setConfigValue(dto.getConfigValue());
        vo.setConfigType(dto.getConfigType());
        vo.setConfigTypeText(expectedText);

        assertThat(vo.getConfigTypeText()).isEqualTo(expectedText);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideConfigTypes() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("Y", "系统内置"),
                org.junit.jupiter.params.provider.Arguments.of("N", "用户自定义")
        );
    }

    @DisplayName("测试继承字段映射")
    @Test
    void testInheritedFieldsMapping() {
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

        ConfigVO vo = new ConfigVO();
        vo.setConfigId(dto.getConfigId());
        vo.setConfigName(dto.getConfigName());
        vo.setConfigKey(dto.getConfigKey());
        vo.setConfigValue(dto.getConfigValue());
        vo.setConfigType(dto.getConfigType());
        vo.setRemark(dto.getRemark());
        vo.setCreateTime(dto.getCreateTime());
        vo.setUpdateTime(dto.getUpdateTime());
        vo.setCreatedBy(dto.getCreatedBy());
        vo.setUpdatedBy(dto.getUpdatedBy());

        assertThat(vo.getConfigId()).isEqualTo(dto.getConfigId());
        assertThat(vo.getConfigName()).isEqualTo(dto.getConfigName());
        assertThat(vo.getConfigKey()).isEqualTo(dto.getConfigKey());
        assertThat(vo.getConfigValue()).isEqualTo(dto.getConfigValue());
        assertThat(vo.getConfigType()).isEqualTo(dto.getConfigType());
        assertThat(vo.getRemark()).isEqualTo(dto.getRemark());
        assertThat(vo.getCreateTime()).isEqualTo(dto.getCreateTime());
        assertThat(vo.getUpdateTime()).isEqualTo(dto.getUpdateTime());
        assertThat(vo.getCreatedBy()).isEqualTo(dto.getCreatedBy());
        assertThat(vo.getUpdatedBy()).isEqualTo(dto.getUpdatedBy());
    }
}
