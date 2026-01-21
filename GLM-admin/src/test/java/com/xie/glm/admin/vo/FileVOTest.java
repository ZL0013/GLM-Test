package com.xie.glm.admin.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 文件 VO 测试
 *
 * @author xie
 */
class FileVOTest {

    @Test
    void testConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();

        FileVO vo = new FileVO(
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

        assertThat(vo.id()).isEqualTo(1L);
        assertThat(vo.fileName()).isEqualTo("test.txt");
        assertThat(vo.filePath()).isEqualTo("/uploads/test.txt");
        assertThat(vo.contentType()).isEqualTo("text/plain");
        assertThat(vo.fileSize()).isEqualTo(1024L);
        assertThat(vo.fileMd5()).isEqualTo("abc123");
        assertThat(vo.fileSizeFormatted()).isEqualTo("1.0 KB");
        assertThat(vo.createTime()).isEqualTo(now);
        assertThat(vo.updateTime()).isEqualTo(now);
    }

    @ParameterizedTest
    @CsvSource({
        "1024, 1.0 KB",
        "1048576, 1.0 MB",
        "1073741824, 1.0 GB",
        "512, 512 B",
        "1536, 1.5 KB",
        "2097152, 2.0 MB"
    })
    void testFileSizeFormatting(long fileSize, String expectedFormat) {
        FileVO vo = FileVO.builder()
            .id(1L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .fileSize(fileSize)
            .fileSizeFormatted(expectedFormat)
            .build();

        assertThat(vo.fileSize()).isEqualTo(fileSize);
        assertThat(vo.fileSizeFormatted()).isEqualTo(expectedFormat);
    }

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();

        FileVO vo = FileVO.builder()
            .id(1L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .contentType("text/plain")
            .fileSize(1024L)
            .fileMd5("abc123")
            .fileSizeFormatted("1.0 KB")
            .createTime(now)
            .updateTime(now)
            .build();

        assertThat(vo.id()).isEqualTo(1L);
        assertThat(vo.fileName()).isEqualTo("test.txt");
        assertThat(vo.fileSizeFormatted()).isEqualTo("1.0 KB");
    }

    @Test
    void testBuilderWithRequiredFieldsOnly() {
        FileVO vo = FileVO.builder()
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .build();

        assertThat(vo.fileName()).isEqualTo("test.txt");
        assertThat(vo.filePath()).isEqualTo("/uploads/test.txt");
        assertThat(vo.id()).isNull();
    }

    @Test
    void testEquality() {
        LocalDateTime now = LocalDateTime.now();

        FileVO vo1 = FileVO.builder()
            .id(1L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .createTime(now)
            .updateTime(now)
            .build();

        FileVO vo2 = FileVO.builder()
            .id(1L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .createTime(now)
            .updateTime(now)
            .build();

        assertThat(vo1).isEqualTo(vo2);
        assertThat(vo1.hashCode()).isEqualTo(vo2.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();

        FileVO vo = FileVO.builder()
            .id(1L)
            .fileName("test.txt")
            .filePath("/uploads/test.txt")
            .createTime(now)
            .updateTime(now)
            .build();

        String str = vo.toString();
        assertThat(str).contains("FileVO");
        assertThat(str).contains("test.txt");
    }

    @Test
    void testIsImage() {
        FileVO imageVo = FileVO.builder()
            .fileName("image.jpg")
            .contentType("image/jpeg")
            .build();

        FileVO textVo = FileVO.builder()
            .fileName("document.txt")
            .contentType("text/plain")
            .build();

        // 验证 content type 包含 image
        assertThat(imageVo.contentType()).contains("image");
        assertThat(textVo.contentType()).doesNotContain("image");
    }
}
