package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.FileVO;
import com.xie.glm.common.dto.FileDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 文件 VO 转换器测试
 *
 * @author xie
 */
class FileVoConverterTest {

    private final FileVoConverter fileVoConverter = Mappers.getMapper(FileVoConverter.class);

    @Test
    void testToVo() {
        LocalDateTime now = LocalDateTime.now();

        FileDTO dto = new FileDTO(
            1L,
            "test.txt",
            "/uploads/test.txt",
            "text/plain",
            1024L,
            "abc123",
            now,
            now
        );

        FileVO vo = fileVoConverter.toVo(dto);

        assertThat(vo).isNotNull();
        assertThat(vo.id()).isEqualTo(1L);
        assertThat(vo.fileName()).isEqualTo("test.txt");
        assertThat(vo.filePath()).isEqualTo("/uploads/test.txt");
        assertThat(vo.contentType()).isEqualTo("text/plain");
        assertThat(vo.fileSize()).isEqualTo(1024L);
        assertThat(vo.fileMd5()).isEqualTo("abc123");
        assertThat(vo.fileSizeFormatted()).isEqualTo("1.0 KB");
    }

    @Test
    void testToVoWithNullFileSize() {
        FileDTO dto = new FileDTO(
            1L,
            "test.txt",
            "/uploads/test.txt",
            "text/plain",
            null,
            "abc123",
            null,
            null
        );

        FileVO vo = fileVoConverter.toVo(dto);

        assertThat(vo.fileSizeFormatted()).isEqualTo("0 B");
    }

    @Test
    void testToVoList() {
        LocalDateTime now = LocalDateTime.now();

        FileDTO dto1 = new FileDTO(
            1L,
            "file1.txt",
            "/uploads/file1.txt",
            "text/plain",
            512L,
            "md5_1",
            now,
            now
        );

        FileDTO dto2 = new FileDTO(
            2L,
            "file2.txt",
            "/uploads/file2.txt",
            "text/plain",
            2048L,
            "md5_2",
            now,
            now
        );

        List<FileVO> vos = fileVoConverter.toVoList(List.of(dto1, dto2));

        assertThat(vos).hasSize(2);
        assertThat(vos.get(0).id()).isEqualTo(1L);
        assertThat(vos.get(0).fileSize()).isEqualTo(512L);
        assertThat(vos.get(0).fileSizeFormatted()).isEqualTo("512.0 B");
        assertThat(vos.get(1).id()).isEqualTo(2L);
        assertThat(vos.get(1).fileSize()).isEqualTo(2048L);
        assertThat(vos.get(1).fileSizeFormatted()).isEqualTo("2.0 KB");
    }

    @Test
    void testToVoListWithNull() {
        List<FileVO> vos = fileVoConverter.toVoList(null);
        assertThat(vos).isNull();
    }

    @Test
    void testToVoListWithEmpty() {
        List<FileVO> vos = fileVoConverter.toVoList(List.of());
        assertThat(vos).isNotNull();
        assertThat(vos).isEmpty();
    }

    @Test
    void testFormatFileSize() {
        assertThat(fileVoConverter.formatFileSize(1024L)).isEqualTo("1.0 KB");
        assertThat(fileVoConverter.formatFileSize(1048576L)).isEqualTo("1.0 MB");
        assertThat(fileVoConverter.formatFileSize(1073741824L)).isEqualTo("1.0 GB");
        assertThat(fileVoConverter.formatFileSize(512L)).isEqualTo("512.0 B");
        assertThat(fileVoConverter.formatFileSize(null)).isEqualTo("0 B");
    }
}
