package com.xie.glm.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import com.xie.glm.system.dto.NoticeDTO;
import com.xie.glm.system.dto.query.NoticeQueryDTO;
import com.xie.glm.admin.facade.NoticeFacade;
import com.xie.glm.admin.vo.NoticeVO;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 通知公告控制器测试
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("通知公告控制器测试")
class NoticeControllerTest {

    @Mock
    private NoticeFacade noticeFacade;

    @InjectMocks
    private NoticeController noticeController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

    private MockMvc mockMvc;

    private void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(noticeController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Nested
    @DisplayName("查询操作测试")
    class QueryOperationsTest {

        @Test
        @DisplayName("分页查询通知列表")
        void testListNotices() throws Exception {
            PageResult<NoticeVO> pageResult = new PageResult<>(List.of(), 0L);
            when(noticeFacade.listNotices(any(NoticeQueryDTO.class))).thenReturn(pageResult);

            setupMockMvc();

            mockMvc.perform(get("/api/system/notices")
                    .param("pageNum", "1")
                    .param("pageSize", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data.records").isArray());

            verify(noticeFacade).listNotices(any(NoticeQueryDTO.class));
        }

        @Test
        @DisplayName("根据ID查询通知详情")
        void testGetNoticeById() throws Exception {
            NoticeVO vo = new NoticeVO();
            vo.setNoticeId(1L);
            vo.setNoticeTitle("测试通知");
            when(noticeFacade.getNoticeById(1L)).thenReturn(vo);

            setupMockMvc();

            mockMvc.perform(get("/api/system/notices/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data.noticeId").value(1))
                    .andExpect(jsonPath("$.data.noticeTitle").value("测试通知"));

            verify(noticeFacade).getNoticeById(1L);
        }
    }

    @Nested
    @DisplayName("增删改操作测试")
    class ModifyOperationsTest {

        @Test
        @DisplayName("创建通知")
        void testCreateNotice() throws Exception {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeTitle("新通知");
            dto.setNoticeType(NoticeType.NOTICE);
            dto.setNoticeContent("通知内容");
            dto.setStatus(NoticeStatus.NORMAL);

            setupMockMvc();

            mockMvc.perform(post("/api/system/notices")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0));

            verify(noticeFacade).createNotice(any(NoticeDTO.class));
        }

        @Test
        @DisplayName("更新通知")
        void testUpdateNotice() throws Exception {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeId(1L);
            dto.setNoticeTitle("更新通知");
            dto.setNoticeType(NoticeType.NOTICE);
            dto.setNoticeContent("更新内容");
            dto.setStatus(NoticeStatus.NORMAL);

            setupMockMvc();

            mockMvc.perform(put("/api/system/notices/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0));

            verify(noticeFacade).updateNotice(any(NoticeDTO.class));
        }

        @Test
        @DisplayName("删除通知")
        void testDeleteNotice() throws Exception {
            setupMockMvc();

            mockMvc.perform(delete("/api/system/notices/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0));

            verify(noticeFacade).deleteNotice(1L);
        }
    }

    @Nested
    @DisplayName("参数校验测试")
    class ValidationTest {

        @Test
        @DisplayName("创建通知 - 标题为空")
        void testCreateNoticeWithEmptyTitle() throws Exception {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeTitle("");
            dto.setNoticeType(NoticeType.NOTICE);

            setupMockMvc();

            mockMvc.perform(post("/api/system/notices")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("创建通知 - 内容为空")
        void testCreateNoticeWithEmptyContent() throws Exception {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeTitle("标题");
            dto.setNoticeContent("");

            setupMockMvc();

            mockMvc.perform(post("/api/system/notices")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
        }
    }
}
