package com.xie.glm.system.dto;

import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 通知 DTO 测试
 *
 * @author xie
 */
@DisplayName("通知 DTO 测试")
class NoticeDTOTest {

    @Nested
    @DisplayName("DTO 创建测试")
    class DtoCreationTest {

        @ParameterizedTest
        @MethodSource("provideNoticeData")
        @DisplayName("创建通知 DTO - 参数化测试")
        void testCreateNoticeDTO(String title, NoticeType type, String content, NoticeStatus status) {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeTitle(title);
            dto.setNoticeType(type);
            dto.setNoticeContent(content);
            dto.setStatus(status);

            assertNotNull(dto);
            assertEquals(title, dto.getNoticeTitle());
            assertEquals(type, dto.getNoticeType());
            assertEquals(content, dto.getNoticeContent());
            assertEquals(status, dto.getStatus());
        }

        private static Stream<Arguments> provideNoticeData() {
            return Stream.of(
                Arguments.of("系统维护通知", NoticeType.NOTICE, "系统将于今晚进行维护", NoticeStatus.NORMAL),
                Arguments.of("新功能发布", NoticeType.ANNOUNCEMENT, "新增用户管理功能", NoticeStatus.NORMAL),
                Arguments.of("过期通知", NoticeType.NOTICE, "此通知已关闭", NoticeStatus.CLOSED)
            );
        }

        @Test
        @DisplayName("创建通知 DTO - 包含所有字段")
        void testCreateFullNoticeDTO() {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeId(1L);
            dto.setNoticeTitle("完整通知");
            dto.setNoticeType(NoticeType.NOTICE);
            dto.setNoticeContent("完整内容");
            dto.setStatus(NoticeStatus.NORMAL);
            dto.setCreateBy("admin");
            dto.setCreateTime(LocalDateTime.now());
            dto.setUpdateBy("admin");
            dto.setUpdateTime(LocalDateTime.now());
            dto.setRemark("测试备注");

            assertEquals(1L, dto.getNoticeId());
            assertEquals("完整通知", dto.getNoticeTitle());
            assertEquals(NoticeType.NOTICE, dto.getNoticeType());
            assertEquals("admin", dto.getCreateBy());
            assertNotNull(dto.getCreateTime());
            assertEquals("测试备注", dto.getRemark());
        }
    }

    @Nested
    @DisplayName("DTO 字段验证测试")
    class FieldValidationTest {

        @Test
        @DisplayName("通知标题 - 允许空字符串")
        void testEmptyTitle() {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeTitle("");

            assertEquals("", dto.getNoticeTitle());
        }

        @Test
        @DisplayName("通知内容 - 允许空字符串")
        void testEmptyContent() {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeContent("");

            assertEquals("", dto.getNoticeContent());
        }

        @Test
        @DisplayName("通知类型 - 默认为通知")
        void testDefaultNoticeType() {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeType(NoticeType.NOTICE);

            assertEquals(NoticeType.NOTICE, dto.getNoticeType());
        }

        @Test
        @DisplayName("通知状态 - 默认为正常")
        void testDefaultNoticeStatus() {
            NoticeDTO dto = new NoticeDTO();
            dto.setStatus(NoticeStatus.NORMAL);

            assertEquals(NoticeStatus.NORMAL, dto.getStatus());
        }
    }

    @Nested
    @DisplayName("Lombok 注解测试")
    class LombokAnnotationTest {

        @Test
        @DisplayName("@Data 注解 - getter/setter 正常工作")
        void testDataAnnotation() {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeTitle("测试标题");
            dto.setNoticeContent("测试内容");

            assertEquals("测试标题", dto.getNoticeTitle());
            assertEquals("测试内容", dto.getNoticeContent());
        }
    }
}
