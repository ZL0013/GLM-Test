package com.xie.glm.system.converter;

import com.xie.glm.common.dto.FileDTO;
import com.xie.glm.system.domain.SysFile;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 文件转换器测试
 *
 * @author xie
 */
class FileConverterTest {

    private final FileConverter fileConverter = Mappers.getMapper(FileConverter.class);

    @Test
    void testToDto() {
        LocalDateTime now = LocalDateTime.now();

        SysFile entity = new SysFile();
        entity.setFileId(1L);
        entity.setFileName("test.txt");
        entity.setFilePath("/uploads/test.txt");
        entity.setContentType("text/plain");
        entity.setFileSize(1024L);
        entity.setFileMd5("abc123");
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        FileDTO dto = fileConverter.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.fileName()).isEqualTo("test.txt");
        assertThat(dto.filePath()).isEqualTo("/uploads/test.txt");
        assertThat(dto.contentType()).isEqualTo("text/plain");
        assertThat(dto.fileSize()).isEqualTo(1024L);
        assertThat(dto.fileMd5()).isEqualTo("abc123");
    }

    @Test
    void testToEntity() {
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

        SysFile entity = fileConverter.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getFileId()).isEqualTo(1L);
        assertThat(entity.getFileName()).isEqualTo("test.txt");
        assertThat(entity.getFilePath()).isEqualTo("/uploads/test.txt");
        assertThat(entity.getContentType()).isEqualTo("text/plain");
        assertThat(entity.getFileSize()).isEqualTo(1024L);
        assertThat(entity.getFileMd5()).isEqualTo("abc123");
    }

    @Test
    void testToDtoList() {
        LocalDateTime now = LocalDateTime.now();

        SysFile entity1 = new SysFile();
        entity1.setFileId(1L);
        entity1.setFileName("file1.txt");
        entity1.setFilePath("/uploads/file1.txt");
        entity1.setContentType("text/plain");
        entity1.setFileSize(1024L);
        entity1.setCreateTime(now);
        entity1.setUpdateTime(now);

        SysFile entity2 = new SysFile();
        entity2.setFileId(2L);
        entity2.setFileName("file2.txt");
        entity2.setFilePath("/uploads/file2.txt");
        entity2.setContentType("text/plain");
        entity2.setFileSize(2048L);
        entity2.setCreateTime(now);
        entity2.setUpdateTime(now);

        List<FileDTO> dtos = fileConverter.toDtoList(List.of(entity1, entity2));

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).id()).isEqualTo(1L);
        assertThat(dtos.get(0).fileName()).isEqualTo("file1.txt");
        assertThat(dtos.get(1).id()).isEqualTo(2L);
        assertThat(dtos.get(1).fileName()).isEqualTo("file2.txt");
    }

    @Test
    void testToDtoWithNull() {
        FileDTO dto = fileConverter.toDto(null);
        assertThat(dto).isNull();
    }

    @Test
    void testToEntityWithNull() {
        SysFile entity = fileConverter.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    void testToDtoListWithNull() {
        List<FileDTO> dtos = fileConverter.toDtoList(null);
        assertThat(dtos).isNull();
    }

    @Test
    void testToDtoListWithEmpty() {
        List<FileDTO> dtos = fileConverter.toDtoList(List.of());
        assertThat(dtos).isNotNull();
        assertThat(dtos).isEmpty();
    }
}
