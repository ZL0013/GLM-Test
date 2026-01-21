package com.xie.glm.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xie.glm.admin.facade.ConfigFacade;
import com.xie.glm.admin.vo.ConfigVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.core.Result;
import com.xie.glm.system.dto.ConfigDTO;
import com.xie.glm.system.dto.query.ConfigQueryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ConfigController 测试
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("参数配置管理控制器测试")
class ConfigControllerTest {

    @Mock
    private ConfigFacade configFacade;

    @InjectMocks
    private ConfigController configController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

    private MockMvc mockMvc;

    private void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(configController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Nested
    @DisplayName("查询操作测试")
    class QueryOperationsTest {

        @Test
        @DisplayName("分页查询配置列表")
        void testListConfigs() throws Exception {
            ConfigVO vo1 = new ConfigVO();
            vo1.setConfigId(1L);
            vo1.setConfigName("系统名称");
            vo1.setConfigKey("sys.name");
            vo1.setConfigValue("GLM管理系统");
            vo1.setConfigType("Y");
            vo1.setConfigTypeText("系统内置");
            vo1.setCreateTime(LocalDateTime.now());

            ConfigVO vo2 = new ConfigVO();
            vo2.setConfigId(2L);
            vo2.setConfigName("用户配置");
            vo2.setConfigKey("user.setting");
            vo2.setConfigValue("用户设置");
            vo2.setConfigType("N");
            vo2.setConfigTypeText("用户自定义");
            vo2.setCreateTime(LocalDateTime.now());

            PageResult<ConfigVO> pageResult = new PageResult<>(Arrays.asList(vo1, vo2), 2L);

            when(configFacade.listConfigs(any(ConfigQueryDTO.class))).thenReturn(pageResult);

            setupMockMvc();

            mockMvc.perform(get("/api/system/configs")
                            .param("pageNum", "1")
                            .param("pageSize", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.records").isArray())
                    .andExpect(jsonPath("$.records.length()").value(2))
                    .andExpect(jsonPath("$.total").value(2));

            verify(configFacade).listConfigs(any(ConfigQueryDTO.class));
        }

        @Test
        @DisplayName("查询配置详情")
        void testGetConfigDetail() throws Exception {
            Long configId = 1L;
            ConfigVO vo = new ConfigVO();
            vo.setConfigId(configId);
            vo.setConfigName("系统名称");
            vo.setConfigKey("sys.name");
            vo.setConfigValue("GLM管理系统");
            vo.setConfigType("Y");
            vo.setConfigTypeText("系统内置");
            vo.setRemark("系统名称配置");
            vo.setCreateTime(LocalDateTime.now());

            when(configFacade.getConfigById(configId)).thenReturn(vo);

            setupMockMvc();

            mockMvc.perform(get("/api/system/configs/{id}", configId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.configId").value(configId))
                    .andExpect(jsonPath("$.configName").value("系统名称"))
                    .andExpect(jsonPath("$.configKey").value("sys.name"))
                    .andExpect(jsonPath("$.configTypeText").value("系统内置"));

            verify(configFacade).getConfigById(configId);
        }

        @Test
        @DisplayName("检查配置键名唯一性")
        void testCheckConfigKeyUnique() throws Exception {
            String configKey = "sys.name";

            when(configFacade.checkConfigKeyUnique(configKey)).thenReturn(true);

            setupMockMvc();

            mockMvc.perform(get("/api/system/configs/check/key")
                            .param("configKey", configKey))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));

            verify(configFacade).checkConfigKeyUnique(configKey);
        }

        @Test
        @DisplayName("根据配置键查询配置值")
        void testGetConfigValueByKey() throws Exception {
            String configKey = "sys.name";
            String expectedValue = "GLM管理系统";

            when(configFacade.getConfigValueByKey(configKey)).thenReturn(expectedValue);

            setupMockMvc();

            mockMvc.perform(get("/api/system/configs/config")
                            .param("configKey", configKey))
                    .andExpect(status().isOk())
                    .andExpect(content().string("\"" + expectedValue + "\""));

            verify(configFacade).getConfigValueByKey(configKey);
        }
    }

    @Nested
    @DisplayName("增删改操作测试")
    class ModifyOperationsTest {

        @Test
        @DisplayName("创建配置")
        void testCreateConfig() throws Exception {
            ConfigDTO dto = ConfigDTO.builder()
                    .configName("系统名称")
                    .configKey("sys.name")
                    .configValue("GLM管理系统")
                    .configType("Y")
                    .remark("系统名称配置")
                    .build();

            Long expectedId = 1L;

            when(configFacade.createConfig(any(ConfigDTO.class))).thenReturn(expectedId);

            setupMockMvc();

            mockMvc.perform(post("/api/system/configs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedId.toString()));

            verify(configFacade).createConfig(any(ConfigDTO.class));
        }

        @Test
        @DisplayName("更新配置")
        void testUpdateConfig() throws Exception {
            Long configId = 1L;
            ConfigDTO dto = ConfigDTO.builder()
                    .configName("系统名称（更新）")
                    .configValue("GLM管理系统 v2.0")
                    .remark("系统名称配置")
                    .build();

            setupMockMvc();

            mockMvc.perform(put("/api/system/configs/{id}", configId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());

            verify(configFacade).updateConfig(any(ConfigDTO.class));
        }

        @Test
        @DisplayName("删除配置")
        void testDeleteConfig() throws Exception {
            Long configId = 1L;

            setupMockMvc();

            mockMvc.perform(delete("/api/system/configs/{id}", configId))
                    .andExpect(status().isOk());

            verify(configFacade).deleteConfig(configId);
        }

        @Test
        @DisplayName("批量删除配置")
        void testDeleteConfigs() throws Exception {
            setupMockMvc();

            mockMvc.perform(delete("/api/system/configs")
                            .param("ids", "1,2,3"))
                    .andExpect(status().isOk());

            verify(configFacade).deleteConfigs(any(Long[].class));
        }
    }
}
