package com.xie.glm.common.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 文件 DTO 测试
 *
 * @author xie
 */
class FileDTOTest {

    @Test
    void testConstructorAndGetters() {
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

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.fileName()).isEqualTo("test.txt");
        assertThat(dto.filePath()).isEqualTo("/uploads/test.txt");
        assertThat(dto.contentType()).isEqualTo("text/plain");
        assertThat(dto.fileSize()).isEqualTo(1024L);
        assertThat(dto.fileMd5()).isEqualTo("abc123");
        assertThat(dto.createTime()).isEqualTo(now);
        assertThat(dto.updateTime()).isEqualTo(now);
    }

    @ParameterizedTest
    @CsvSource({
        "test.txt, text/plain, 1024",
        "document.pdf, application/pdf, 2048000",
        "image.jpg, image/jpeg, 512000"
    })
    void testBuilder(String fileName, String contentType, long fileSize) {
        LocalDateTime now = LocalDateTime.now();

        FileDTO dto = FileDTO.builder()
            .id(1L)
            .fileName(fileName)
            .filePath("/uploads/" + fileName)
            .contentType(contentType)
            .fileSize(fileSize)
            .fileMd5("md5hash")
            .createTime(now)
            .updateTime(now)
            .build();

        assertThat(dto.fileName()).isEqualTo(fileName);
        assertThat(dto.contentType()).isEqualTo(contentType);
        assertThat(dto.fileSize()).isEqualTo(fileSize);
    }

    @Test
    void testBuilderWithRequiredFieldsOnly() {
        FileDTO dto = FileDTO.builder()
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .build();

        assertThat(dto.fileName()).isEqualTo("test.txt");
        assertThat(dto.filePath()).isEqualTo("/uploads/test.txt");
        assertThat(dto.id()).isNull();
        assertThat(dto.contentType()).isNull();
    }

    @Test
    void testEquality() {
        LocalDateTime now = LocalDateTime.now();

        FileDTO dto1 = FileDTO.builder()
            .id(1L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .createTime(now)
            .updateTime(now)
            .build();

        FileDTO dto2 = FileDTO.builder()
            .id(1L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .createTime(now)
            .updateTime(now)
            .build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testInequality() {
        FileDTO dto1 = FileDTO.builder()
            .id(1L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .build();

        FileDTO dto2 = FileDTO.builder()
            .id(2L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .build();

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();

        FileDTO dto = FileDTO.builder()
            .id(1L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .createTime(now)
            .updateTime(now)
            .build();

        String str = dto.toString();
        assertThat(str).contains("FileDTO");
        assertThat(str).contains("test.txt");
    }

    @ParameterizedTest
    @NullSource
    void testWithNullValues(LocalDateTime nullTime) {
        FileDTO dto = FileDTO.builder()
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .createTime(nullTime)
            .updateTime(nullTime)
            .build();

        assertThat(dto.fileName()).isEqualTo("test.txt");
        assertThat(dto.createTime()).isNull();
        assertThat(dto.updateTime()).isNull();
    }

    @Test
    void testFileSizeFormatting() {
        FileDTO dto = FileDTO.builder()
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .fileSize(1024L)
            .build();

        assertThat(dto.fileSize()).isEqualTo(1024L);
    }
}
