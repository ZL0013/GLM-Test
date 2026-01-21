package com.xie.glm.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xie.glm.admin.facade.FileFacade;
import com.xie.glm.admin.vo.FileVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.dto.query.FileQueryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 文件控制器测试
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("文件控制器测试")
class FileControllerTest {

    @Mock
    private FileFacade fileFacade;

    @InjectMocks
    private FileController fileController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

    private MockMvc mockMvc;

    private void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(fileController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    @DisplayName("上传文件")
    void testUpload() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        FileVO fileVO = new FileVO(
            1L,
            "test.txt",
            "/uploads/test.txt",
            "text/plain",
            1024L,
            "abc123",
            "1.0 KB",
            now,
            now
        );

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "test content".getBytes()
        );

        when(fileFacade.uploadFile(any())).thenReturn(fileVO);

        setupMockMvc();

        mockMvc.perform(multipart("/api/system/files/upload")
                .file(file))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.fileName").value("test.txt"))
            .andExpect(jsonPath("$.data.fileSize").value(1024));
    }

    @Test
    @DisplayName("删除单个文件")
    void testDeleteFile() throws Exception {
        setupMockMvc();

        mockMvc.perform(delete("/api/system/files/1"))
            .andExpect(status().isOk());

        verify(fileFacade).deleteFile(1L);
    }

    @Test
    @DisplayName("批量删除文件")
    void testDeleteFiles() throws Exception {
        List<Long> fileIds = List.of(1L, 2L, 3L);
        when(fileFacade.deleteFiles(fileIds)).thenReturn(3);

        setupMockMvc();

        mockMvc.perform(delete("/api/system/files/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fileIds)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").value(3));
    }

    @Test
    @DisplayName("根据ID查询文件详情")
    void testGetById() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        FileVO fileVO = new FileVO(
            1L,
            "test.txt",
            "/uploads/test.txt",
            "text/plain",
            1024L,
            "abc123",
            "1.0 KB",
            now,
            now
        );

        when(fileFacade.getFileById(1L)).thenReturn(fileVO);

        setupMockMvc();

        mockMvc.perform(get("/api/system/files/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.fileName").value("test.txt"));
    }

    @Test
    @DisplayName("分页查询文件列表")
    void testListFiles() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        FileVO fileVO = new FileVO(
            1L,
            "test.txt",
            "/uploads/test.txt",
            "text/plain",
            1024L,
            "abc123",
            "1.0 KB",
            now,
            now
        );

        PageResult<FileVO> pageResult = new PageResult<>(List.of(fileVO), 1L);

        when(fileFacade.listFiles(any(FileQueryDTO.class))).thenReturn(pageResult);

        setupMockMvc();

        mockMvc.perform(get("/api/system/files")
                .param("pageNum", "1")
                .param("pageSize", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.records[0].fileName").value("test.txt"))
            .andExpect(jsonPath("$.data.total").value(1));
    }
}
