package com.xie.glm.common.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.mock.web.MockMultipartFile;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 文件上传 DTO 测试
 *
 * @author xie
 */
class FileUploadDTOTest {

    @ParameterizedTest
    @NullSource
    void testConstructorWithNullFile(org.springframework.web.multipart.MultipartFile file) {
        assertThatThrownBy(() -> new FileUploadDTO(file))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("文件不能为空");
    }

    @ParameterizedTest
    @MethodSource("provideValidFiles")
    void testConstructorWithValidFile(MockMultipartFile file) {
        FileUploadDTO dto = new FileUploadDTO(file);

        assertThat(dto.file()).isEqualTo(file);
        assertThat(dto.originalFilename()).isEqualTo(file.getOriginalFilename());
        assertThat(dto.contentType()).isEqualTo(file.getContentType());
        assertThat(dto.size()).isEqualTo(file.getSize());
    }

    @ParameterizedTest
    @MethodSource("provideFileExtensions")
    void testGetFileExtension(String filename, String expectedExtension) {
        MockMultipartFile file = new MockMultipartFile("file", filename, "text/plain", "content".getBytes());
        FileUploadDTO dto = new FileUploadDTO(file);

        assertThat(dto.fileExtension()).isEqualTo(expectedExtension);
    }

    @ParameterizedTest
    @MethodSource("provideEmptyFiles")
    void testConstructorWithEmptyFile(MockMultipartFile file) {
        assertThatThrownBy(() -> new FileUploadDTO(file))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("文件不能为空");
    }

    @Test
    void testIsValidReturnsTrueForValidFile() {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "valid content".getBytes()
        );

        FileUploadDTO dto = new FileUploadDTO(file);

        assertThat(dto.isValid()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideImageFiles")
    void testIsImageFile(MockMultipartFile file, boolean expected) {
        FileUploadDTO dto = new FileUploadDTO(file);

        assertThat(dto.isImage()).isEqualTo(expected);
    }

    private static Stream<Arguments> provideValidFiles() {
        return Stream.of(
            Arguments.of(new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes())),
            Arguments.of(new MockMultipartFile("file", "document.pdf", "application/pdf", "pdf content".getBytes())),
            Arguments.of(new MockMultipartFile("file", "image.jpg", "image/jpeg", "jpg content".getBytes()))
        );
    }

    private static Stream<Arguments> provideFileExtensions() {
        return Stream.of(
            Arguments.of("test.txt", "txt"),
            Arguments.of("document.pdf", "pdf"),
            Arguments.of("archive.tar.gz", "gz"),
            Arguments.of("noextension", ""),
            Arguments.of("multiple.dots.name.txt", "txt")
        );
    }

    private static Stream<MockMultipartFile> provideEmptyFiles() {
        return Stream.of(
            new MockMultipartFile("file", "", "text/plain", "".getBytes()),
            new MockMultipartFile("file", "empty.txt", "text/plain", new byte[0])
        );
    }

    private static Stream<Arguments> provideImageFiles() {
        return Stream.of(
            Arguments.of(new MockMultipartFile("file", "image.jpg", "image/jpeg", "jpg".getBytes()), true),
            Arguments.of(new MockMultipartFile("file", "image.png", "image/png", "png".getBytes()), true),
            Arguments.of(new MockMultipartFile("file", "image.gif", "image/gif", "gif".getBytes()), true),
            Arguments.of(new MockMultipartFile("file", "document.pdf", "application/pdf", "pdf".getBytes()), false),
            Arguments.of(new MockMultipartFile("file", "text.txt", "text/plain", "text".getBytes()), false)
        );
    }
}
