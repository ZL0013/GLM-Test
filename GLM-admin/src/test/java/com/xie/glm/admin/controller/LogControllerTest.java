package com.xie.glm.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xie.glm.admin.facade.OperLogFacade;
import com.xie.glm.admin.vo.OperLogVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.query.LogQueryDTO;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * LogController 测试类
 *
 * <p>测试日志管理控制器的 REST API。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("日志管理控制器测试")
class LogControllerTest {

    @Mock
    private OperLogFacade operLogFacade;

    @InjectMocks
    private LogController logController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

    private MockMvc mockMvc;

    private void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(logController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Nested
    @DisplayName("分页查询操作日志列表")
    class ListOperLogsTest {

        @Test
        @DisplayName("应该成功分页查询操作日志列表")
        void testListOperLogsSuccess() throws Exception {
            OperLogVO vo1 = OperLogVO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .businessTypeName("新增")
                    .businessTypeText("新增")
                    .status(0)
                    .statusName("成功")
                    .statusText("成功")
                    .operName("admin")
                    .operTime(LocalDateTime.now())
                    .costTime(100L)
                    .build();

            OperLogVO vo2 = OperLogVO.builder()
                    .operId(2L)
                    .title("角色管理")
                    .businessType(2)
                    .businessTypeName("修改")
                    .businessTypeText("修改")
                    .status(0)
                    .statusName("成功")
                    .statusText("成功")
                    .operName("admin")
                    .operTime(LocalDateTime.now())
                    .costTime(200L)
                    .build();

            PageResult<OperLogVO> pageResult = new PageResult<>(Arrays.asList(vo1, vo2), 2L);

            when(operLogFacade.listOperLogs(any(LogQueryDTO.class))).thenReturn(pageResult);

            setupMockMvc();

            mockMvc.perform(get("/api/system/oper-logs")
                    .param("pageNum", "1")
                    .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records.length()").value(2))
                .andExpect(jsonPath("$.total").value(2));

            verify(operLogFacade, times(1)).listOperLogs(any(LogQueryDTO.class));
        }

        @Test
        @DisplayName("查询结果为空时应该返回空列表")
        void testListOperLogsEmpty() throws Exception {
            PageResult<OperLogVO> pageResult = new PageResult<>(Collections.emptyList(), 0L);

            when(operLogFacade.listOperLogs(any(LogQueryDTO.class))).thenReturn(pageResult);

            setupMockMvc();

            mockMvc.perform(get("/api/system/oper-logs")
                    .param("pageNum", "1")
                    .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records.length()").value(0))
                .andExpect(jsonPath("$.total").value(0));

            verify(operLogFacade, times(1)).listOperLogs(any(LogQueryDTO.class));
        }
    }

    @Nested
    @DisplayName("根据 ID 查询操作日志详情")
    class GetOperLogByIdTest {

        @Test
        @DisplayName("应该成功查询操作日志详情")
        void testGetOperLogByIdSuccess() throws Exception {
            Long operId = 1L;

            OperLogVO vo = OperLogVO.builder()
                    .operId(operId)
                    .title("用户管理")
                    .businessType(1)
                    .businessTypeName("新增")
                    .businessTypeText("新增")
                    .status(0)
                    .statusName("成功")
                    .statusText("成功")
                    .operName("admin")
                    .operTime(LocalDateTime.now())
                    .costTime(100L)
                    .build();

            when(operLogFacade.getOperLogById(operId)).thenReturn(vo);

            setupMockMvc();

            mockMvc.perform(get("/api/system/oper-logs/{id}", operId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operId").value(operId))
                .andExpect(jsonPath("$.title").value("用户管理"))
                .andExpect(jsonPath("$.businessTypeText").value("新增"))
                .andExpect(jsonPath("$.statusText").value("成功"));

            verify(operLogFacade, times(1)).getOperLogById(operId);
        }

        @Test
        @DisplayName("查询不存在的操作日志应该返回 null")
        void testGetOperLogByIdNotFound() throws Exception {
            Long operId = 999L;
            when(operLogFacade.getOperLogById(operId)).thenReturn(null);

            setupMockMvc();

            mockMvc.perform(get("/api/system/oper-logs/{id}", operId))
                .andExpect(status().isOk());

            verify(operLogFacade, times(1)).getOperLogById(operId);
        }
    }

    @Nested
    @DisplayName("删除操作日志")
    class DeleteOperLogTest {

        @Test
        @DisplayName("应该成功删除单条操作日志")
        void testDeleteOperLogSuccess() throws Exception {
            Long operId = 1L;
            doNothing().when(operLogFacade).deleteOperLog(operId);

            setupMockMvc();

            mockMvc.perform(delete("/api/system/oper-logs/{id}", operId))
                .andExpect(status().isOk());

            verify(operLogFacade, times(1)).deleteOperLog(operId);
        }

        @Test
        @DisplayName("应该成功批量删除操作日志")
        void testDeleteOperLogsSuccess() throws Exception {
            Long[] operIds = {1L, 2L, 3L};
            doNothing().when(operLogFacade).deleteOperLogs(operIds);

            setupMockMvc();

            mockMvc.perform(delete("/api/system/oper-logs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(operIds)))
                .andExpect(status().isOk());

            verify(operLogFacade, times(1)).deleteOperLogs(operIds);
        }
    }

    @Nested
    @DisplayName("清空操作日志")
    class CleanOperLogsTest {

        @Test
        @DisplayName("应该成功清空所有操作日志")
        void testCleanOperLogsSuccess() throws Exception {
            doNothing().when(operLogFacade).cleanOperLogs();

            setupMockMvc();

            mockMvc.perform(delete("/api/system/oper-logs/clean"))
                .andExpect(status().isOk());

            verify(operLogFacade, times(1)).cleanOperLogs();
        }
    }
}
