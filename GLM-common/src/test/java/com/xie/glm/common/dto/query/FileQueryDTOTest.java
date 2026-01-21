package com.xie.glm.common.dto.query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 文件查询 DTO 测试
 *
 * @author xie
 */
class FileQueryDTOTest {

    @Test
    void testDefaultConstructor() {
        FileQueryDTO dto = new FileQueryDTO();

        assertThat(dto.fileName()).isNull();
        assertThat(dto.startDate()).isNull();
        assertThat(dto.endDate()).isNull();
        assertThat(dto.minSize()).isNull();
        assertThat(dto.maxSize()).isNull();
        assertThat(dto.pageNum()).isEqualTo(1);
        assertThat(dto.pageSize()).isEqualTo(10);
    }

    @ParameterizedTest
    @CsvSource({
        "test.txt, 1, 10",
        "document.pdf, 2, 20",
        ", 1, 10"
    })
    void testBuilder(String fileName, int pageNum, int pageSize) {
        FileQueryDTO dto = FileQueryDTO.builder()
            .fileName(fileName)
            .pageNum(pageNum)
            .pageSize(pageSize)
            .build();

        assertThat(dto.fileName()).isEqualTo(fileName);
        assertThat(dto.pageNum()).isEqualTo(pageNum);
        assertThat(dto.pageSize()).isEqualTo(pageSize);
    }

    @Test
    void testBuilderWithAllFields() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        FileQueryDTO dto = FileQueryDTO.builder()
            .fileName("test")
            .startDate(start)
            .endDate(end)
            .minSize(1024L)
            .maxSize(1024000L)
            .pageNum(1)
            .pageSize(20)
            .build();

        assertThat(dto.fileName()).isEqualTo("test");
        assertThat(dto.startDate()).isEqualTo(start);
        assertThat(dto.endDate()).isEqualTo(end);
        assertThat(dto.minSize()).isEqualTo(1024L);
        assertThat(dto.maxSize()).isEqualTo(1024000L);
        assertThat(dto.pageNum()).isEqualTo(1);
        assertThat(dto.pageSize()).isEqualTo(20);
    }

    @ParameterizedTest
    @NullSource
    void testFileNameWithNull(String fileName) {
        FileQueryDTO dto = FileQueryDTO.builder()
            .fileName(fileName)
            .build();

        assertThat(dto.fileName()).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "test file", "文件名.txt"})
    void testFileNameWithVariousValues(String fileName) {
        FileQueryDTO dto = FileQueryDTO.builder()
            .fileName(fileName)
            .build();

        assertThat(dto.fileName()).isEqualTo(fileName);
    }

    @Test
    void testToPage() {
        FileQueryDTO dto = FileQueryDTO.builder()
            .pageNum(2)
            .pageSize(20)
            .build();

        assertThat(dto.<String>toPage()).isNotNull();
        assertThat(dto.<String>toPage().getCurrent()).isEqualTo(2L);
        assertThat(dto.<String>toPage().getSize()).isEqualTo(20L);
    }

    @Test
    void testEquality() {
        FileQueryDTO dto1 = FileQueryDTO.builder()
            .fileName("test.txt")
            .pageNum(1)
            .pageSize(10)
            .build();

        FileQueryDTO dto2 = FileQueryDTO.builder()
            .fileName("test.txt")
            .pageNum(1)
            .pageSize(10)
            .build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }
}
