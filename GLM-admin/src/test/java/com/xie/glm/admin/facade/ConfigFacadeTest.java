package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.ConfigVoConverter;
import com.xie.glm.admin.vo.ConfigVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.ConfigDTO;
import com.xie.glm.system.dto.query.ConfigQueryDTO;
import com.xie.glm.system.service.IConfigService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ConfigFacade 测试
 *
 * @author xie
 */
@DisplayName("ConfigFacade 测试")
@ExtendWith(MockitoExtension.class)
class ConfigFacadeTest {

    @Mock
    private IConfigService configService;

    @Mock
    private ConfigVoConverter voConverter;

    @InjectMocks
    private ConfigFacade configFacade;

    @DisplayName("测试分页查询配置列表")
    @Test
    void testListConfigs() {
        // 准备测试数据
        ConfigQueryDTO query = new ConfigQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);

        ConfigDTO dto1 = ConfigDTO.builder()
                .configId(1L)
                .configName("系统名称")
                .configKey("sys.name")
                .configValue("GLM管理系统")
                .configType("Y")
                .createTime(LocalDateTime.now())
                .build();

        ConfigDTO dto2 = ConfigDTO.builder()
                .configId(2L)
                .configName("用户配置")
                .configKey("user.setting")
                .configValue("用户设置")
                .configType("N")
                .createTime(LocalDateTime.now())
                .build();

        ConfigVO vo1 = new ConfigVO();
        vo1.setConfigId(1L);
        vo1.setConfigName("系统名称");
        vo1.setConfigKey("sys.name");
        vo1.setConfigValue("GLM管理系统");
        vo1.setConfigType("Y");

        ConfigVO vo2 = new ConfigVO();
        vo2.setConfigId(2L);
        vo2.setConfigName("用户配置");
        vo2.setConfigKey("user.setting");
        vo2.setConfigValue("用户设置");
        vo2.setConfigType("N");

        PageResult<ConfigDTO> dtoPage = new PageResult<>(Arrays.asList(dto1, dto2), 2L);

        // Mock 行为
        when(configService.listConfigs(any(ConfigQueryDTO.class))).thenReturn(dtoPage);
        when(voConverter.toVoList(Arrays.asList(dto1, dto2))).thenReturn(Arrays.asList(vo1, vo2));

        // 执行测试
        PageResult<ConfigVO> result = configFacade.listConfigs(query);

        // 验证结果
        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(2L);
        assertThat(result.getRecords()).hasSize(2);

        // 验证 configTypeText 被正确设置
        assertThat(result.getRecords().get(0).getConfigTypeText()).isEqualTo("系统内置");
        assertThat(result.getRecords().get(1).getConfigTypeText()).isEqualTo("用户自定义");

        // 验证方法调用
        verify(configService).listConfigs(query);
    }

    @DisplayName("测试根据 ID 查询配置详情")
    @Test
    void testGetConfigById() {
        // 准备测试数据
        Long configId = 1L;
        ConfigDTO dto = ConfigDTO.builder()
                .configId(configId)
                .configName("系统名称")
                .configKey("sys.name")
                .configValue("GLM管理系统")
                .configType("Y")
                .remark("系统名称配置")
                .createTime(LocalDateTime.now())
                .build();

        ConfigVO vo = new ConfigVO();
        vo.setConfigId(configId);
        vo.setConfigName("系统名称");
        vo.setConfigKey("sys.name");
        vo.setConfigValue("GLM管理系统");
        vo.setConfigType("Y");

        // Mock 行为
        when(configService.getConfigById(configId)).thenReturn(dto);
        when(voConverter.toVo(dto)).thenReturn(vo);

        // 执行测试
        ConfigVO result = configFacade.getConfigById(configId);

        // 验证结果
        assertThat(result).isNotNull();
        assertThat(result.getConfigId()).isEqualTo(configId);
        assertThat(result.getConfigName()).isEqualTo("系统名称");
        assertThat(result.getConfigTypeText()).isEqualTo("系统内置");

        // 验证方法调用
        verify(configService).getConfigById(configId);
    }

    @DisplayName("测试创建配置")
    @Test
    void testCreateConfig() {
        // 准备测试数据
        ConfigDTO dto = ConfigDTO.builder()
                .configName("系统名称")
                .configKey("sys.name")
                .configValue("GLM管理系统")
                .configType("Y")
                .remark("系统名称配置")
                .build();

        Long expectedId = 1L;

        // Mock 行为
        when(configService.createConfig(any(ConfigDTO.class))).thenReturn(expectedId);

        // 执行测试
        Long result = configFacade.createConfig(dto);

        // 验证结果
        assertThat(result).isEqualTo(expectedId);

        // 验证方法调用
        verify(configService).createConfig(dto);
    }

    @DisplayName("测试更新配置")
    @Test
    void testUpdateConfig() {
        // 准备测试数据
        ConfigDTO dto = ConfigDTO.builder()
                .configId(1L)
                .configName("系统名称（更新）")
                .configValue("GLM管理系统 v2.0")
                .remark("系统名称配置")
                .build();

        // 执行测试
        configFacade.updateConfig(dto);

        // 验证方法调用
        verify(configService).updateConfig(dto);
    }

    @DisplayName("测试删除配置")
    @Test
    void testDeleteConfig() {
        // 准备测试数据
        Long configId = 1L;

        // 执行测试
        configFacade.deleteConfig(configId);

        // 验证方法调用
        verify(configService).deleteConfig(configId);
    }

    @DisplayName("测试批量删除配置")
    @Test
    void testDeleteConfigs() {
        // 准备测试数据
        Long[] configIds = {1L, 2L, 3L};

        // 执行测试
        configFacade.deleteConfigs(configIds);

        // 验证方法调用
        verify(configService).deleteConfigs(configIds);
    }

    @DisplayName("测试检查配置键名唯一性")
    @Test
    void testCheckConfigKeyUnique() {
        // 准备测试数据
        String configKey = "sys.name";

        // Mock 行为
        when(configService.checkConfigKeyUnique(configKey)).thenReturn(true);

        // 执行测试
        boolean result = configFacade.checkConfigKeyUnique(configKey);

        // 验证结果
        assertThat(result).isTrue();

        // 验证方法调用
        verify(configService).checkConfigKeyUnique(configKey);
    }

    @DisplayName("测试根据配置键查询配置值")
    @Test
    void testGetConfigValueByKey() {
        // 准备测试数据
        String configKey = "sys.name";
        String expectedValue = "GLM管理系统";

        // Mock 行为
        when(configService.getConfigValueByKey(configKey)).thenReturn(expectedValue);

        // 执行测试
        String result = configFacade.getConfigValueByKey(configKey);

        // 验证结果
        assertThat(result).isEqualTo(expectedValue);

        // 验证方法调用
        verify(configService).getConfigValueByKey(configKey);
    }
}
