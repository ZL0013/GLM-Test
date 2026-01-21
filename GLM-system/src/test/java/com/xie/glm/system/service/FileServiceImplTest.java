package com.xie.glm.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.dto.FileDTO;
import com.xie.glm.common.dto.FileUploadDTO;
import com.xie.glm.common.dto.query.FileQueryDTO;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.FileConverter;
import com.xie.glm.system.domain.SysFile;
import com.xie.glm.system.mapper.SysFileMapper;
import com.xie.glm.system.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 文件服务测试
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private SysFileMapper fileMapper;

    @Mock
    private FileConverter fileConverter;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileService, "uploadPath", "test-uploads");
    }

    @Test
    void testUploadFile() {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "Hello, World!".getBytes()
        );

        SysFile savedEntity = new SysFile();
        savedEntity.setFileId(1L);
        savedEntity.setFileName("test.txt");
        savedEntity.setFilePath("2024/01/15/abc123_test.txt");
        savedEntity.setFileSize(13L);

        when(fileMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(fileConverter.toDto(any(SysFile.class))).thenReturn(new FileDTO(
            1L, "test.txt", "2024/01/15/abc123_test.txt", "text/plain", 13L, "abc123", null, null
        ));

        FileUploadDTO uploadDTO = new FileUploadDTO(file);
        FileDTO result = fileService.uploadFile(uploadDTO);

        assertThat(result).isNotNull();
        assertThat(result.fileName()).isEqualTo("test.txt");
        assertThat(result.fileSize()).isEqualTo(13L);
    }

    @Test
    void testUploadFileViaMultipartFile() {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "document.pdf",
            "application/pdf",
            "PDF content".getBytes()
        );

        when(fileMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(fileConverter.toDto(any(SysFile.class))).thenReturn(new FileDTO(
            1L, "document.pdf", "/uploads/document.pdf", "application/pdf", 11L, "md5", null, null
        ));

        FileDTO result = fileService.uploadFile(file);

        assertThat(result).isNotNull();
        assertThat(result.fileName()).isEqualTo("document.pdf");
        assertThat(result.contentType()).isEqualTo("application/pdf");
    }

    @Test
    void testUploadExistingFileReturnsCached() {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "content".getBytes()
        );

        SysFile existingFile = new SysFile();
        existingFile.setFileId(1L);
        existingFile.setFileName("test.txt");

        when(fileMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existingFile);
        when(fileConverter.toDto(existingFile)).thenReturn(new FileDTO(
            1L, "test.txt", "/uploads/test.txt", "text/plain", 7L, "md5", null, null
        ));

        FileDTO result = fileService.uploadFile(new FileUploadDTO(file));

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void testDownloadFile() {
        when(fileMapper.selectById(999999L)).thenReturn(null);

        // 测试不存在的文件
        assertThatThrownBy(() -> fileService.downloadFile(999999L))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("文件不存在");
    }

    @Test
    void testDeleteFile() {
        SysFile sysFile = new SysFile();
        sysFile.setFileId(1L);
        sysFile.setFileName("test.txt");

        when(fileMapper.selectById(1L)).thenReturn(sysFile);
        when(fileMapper.deleteById(1L)).thenReturn(1);

        fileService.deleteFile(1L);

        verify(fileMapper).deleteById(1L);
    }

    @Test
    void testDeleteNonExistentFile() {
        when(fileMapper.selectById(999999L)).thenReturn(null);

        assertThatThrownBy(() -> fileService.deleteFile(999999L))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("文件不存在");
    }

    @Test
    void testDeleteFiles() {
        List<Long> fileIds = List.of(1L, 2L, 3L);

        when(fileMapper.selectById(1L)).thenReturn(new SysFile());
        when(fileMapper.selectById(2L)).thenReturn(new SysFile());
        when(fileMapper.selectById(3L)).thenReturn(new SysFile());
        when(fileMapper.deleteById(1L)).thenReturn(1);
        when(fileMapper.deleteById(2L)).thenReturn(1);
        when(fileMapper.deleteById(3L)).thenReturn(1);

        int deletedCount = fileService.deleteFiles(fileIds);

        assertThat(deletedCount).isEqualTo(3);
    }

    @Test
    void testGetFileById() {
        LocalDateTime now = LocalDateTime.now();

        SysFile sysFile = new SysFile();
        sysFile.setFileId(1L);
        sysFile.setFileName("test.txt");
        sysFile.setFilePath("/uploads/test.txt");
        sysFile.setCreateTime(now);

        when(fileMapper.selectById(1L)).thenReturn(sysFile);
        when(fileConverter.toDto(sysFile)).thenReturn(new FileDTO(
            1L, "test.txt", "/uploads/test.txt", "text/plain", 1024L, "abc123", now, now
        ));

        FileDTO result = fileService.getFileById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.fileName()).isEqualTo("test.txt");
    }

    @Test
    void testGetFileByIdReturnsNullForNonExistent() {
        when(fileMapper.selectById(999999L)).thenReturn(null);

        FileDTO result = fileService.getFileById(999999L);
        assertThat(result).isNull();
    }

    @Test
    void testListFiles() {
        Page<SysFile> mockPage = new Page<>(1, 10, 2);
        SysFile file1 = new SysFile();
        file1.setFileId(1L);
        file1.setFileName("file1.txt");

        SysFile file2 = new SysFile();
        file2.setFileId(2L);
        file2.setFileName("file2.txt");

        mockPage.setRecords(List.of(file1, file2));

        when(fileMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(mockPage);
        when(fileConverter.toDtoList(any())).thenReturn(List.of(
            new FileDTO(1L, "file1.txt", "/uploads/file1.txt", "text/plain", 1L, "md5_1", null, null),
            new FileDTO(2L, "file2.txt", "/uploads/file2.txt", "text/plain", 1L, "md5_2", null, null)
        ));

        FileQueryDTO queryDTO = FileQueryDTO.builder()
            .pageNum(1)
            .pageSize(10)
            .build();

        Page<FileDTO> result = fileService.listFiles(queryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(2);
        assertThat(result.getTotal()).isEqualTo(2);
    }

    @Test
    void testListFilesWithFileNameFilter() {
        Page<SysFile> mockPage = new Page<>(1, 10, 1);
        SysFile sysFile = new SysFile();
        sysFile.setFileId(1L);
        sysFile.setFileName("filter-test.txt");

        mockPage.setRecords(List.of(sysFile));

        when(fileMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(mockPage);
        when(fileConverter.toDtoList(any())).thenReturn(List.of(
            new FileDTO(1L, "filter-test.txt", "/uploads/filter-test.txt", "text/plain", 1L, "md5", null, null)
        ));

        FileQueryDTO queryDTO = FileQueryDTO.builder()
            .fileName("filter")
            .pageNum(1)
            .pageSize(10)
            .build();

        Page<FileDTO> result = fileService.listFiles(queryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getRecords()).allMatch(dto -> dto.fileName().contains("filter"));
    }

    @Test
    void testExists() {
        SysFile sysFile = new SysFile();
        sysFile.setFileId(1L);

        when(fileMapper.selectById(1L)).thenReturn(sysFile);
        when(fileMapper.selectById(999999L)).thenReturn(null);

        assertThat(fileService.exists(1L)).isTrue();
        assertThat(fileService.exists(999999L)).isFalse();
    }
}
