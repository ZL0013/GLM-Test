package com.xie.glm.admin.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.admin.converter.FileVoConverter;
import com.xie.glm.admin.vo.FileVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.dto.FileDTO;
import com.xie.glm.common.dto.query.FileQueryDTO;
import com.xie.glm.system.service.IFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 文件门面测试
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
class FileFacadeTest {

    @Mock
    private IFileService fileService;

    @Mock
    private FileVoConverter fileVoConverter;

    private FileFacade fileFacade;

    private FileVO fileVO;
    private FileDTO fileDTO;

    @BeforeEach
    void setUp() {
        fileFacade = new FileFacade(fileService, fileVoConverter);

        LocalDateTime now = LocalDateTime.now();

        fileDTO = new FileDTO(
            1L,
            "test.txt",
            "/uploads/test.txt",
            "text/plain",
            1024L,
            "abc123",
            now,
            now
        );

        fileVO = new FileVO(
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
    }

    @Test
    void testUploadFile() {
        MultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "content".getBytes()
        );

        when(fileService.uploadFile(any(MultipartFile.class))).thenReturn(fileDTO);
        when(fileVoConverter.toVo(any(FileDTO.class))).thenReturn(fileVO);

        FileVO result = fileFacade.uploadFile(file);

        assertThat(result).isNotNull();
        assertThat(result.fileName()).isEqualTo("test.txt");
        verify(fileService).uploadFile(any(MultipartFile.class));
    }

    @Test
    void testDownloadFile() {
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());

        when(fileService.downloadFile(1L)).thenReturn(inputStream);

        InputStream result = fileFacade.downloadFile(1L);

        assertThat(result).isNotNull();
        verify(fileService).downloadFile(1L);
    }

    @Test
    void testDeleteFile() {
        fileFacade.deleteFile(1L);
        verify(fileService).deleteFile(1L);
    }

    @Test
    void testDeleteFiles() {
        List<Long> fileIds = List.of(1L, 2L, 3L);
        when(fileService.deleteFiles(fileIds)).thenReturn(3);

        int count = fileFacade.deleteFiles(fileIds);

        assertThat(count).isEqualTo(3);
        verify(fileService).deleteFiles(fileIds);
    }

    @Test
    void testGetFileById() {
        when(fileService.getFileById(1L)).thenReturn(fileDTO);
        when(fileVoConverter.toVo(fileDTO)).thenReturn(fileVO);

        FileVO result = fileFacade.getFileById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        verify(fileService).getFileById(1L);
    }

    @Test
    void testGetFileByIdReturnsNullWhenNotFound() {
        when(fileService.getFileById(1L)).thenReturn(null);

        FileVO result = fileFacade.getFileById(1L);

        assertThat(result).isNull();
    }

    @Test
    void testListFiles() {
        Page<FileDTO> dtoPage = new Page<>(1, 10, 2);
        dtoPage.setRecords(List.of(fileDTO));

        when(fileService.listFiles(any(FileQueryDTO.class))).thenReturn(dtoPage);
        when(fileVoConverter.toVoList(any())).thenReturn(List.of(fileVO));

        FileQueryDTO queryDTO = FileQueryDTO.builder().build();
        PageResult<FileVO> result = fileFacade.listFiles(queryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(2);
    }

    @Test
    void testExists() {
        when(fileService.exists(1L)).thenReturn(true);

        boolean result = fileFacade.exists(1L);

        assertThat(result).isTrue();
    }
}
