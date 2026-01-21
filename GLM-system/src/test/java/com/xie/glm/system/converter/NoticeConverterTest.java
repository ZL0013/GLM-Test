package com.xie.glm.system.converter;

import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import com.xie.glm.system.domain.SysNotice;
import com.xie.glm.system.dto.NoticeDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 通知转换器测试
 *
 * @author xie
 */
@DisplayName("通知转换器测试")
class NoticeConverterTest {

    private final NoticeConverter noticeConverter = Mappers.getMapper(NoticeConverter.class);

    @Nested
    @DisplayName("Entity 转 DTO 测试")
    class EntityToDtoTest {

        @ParameterizedTest
        @MethodSource("provideNoticeData")
        @DisplayName("Entity 转 DTO - 参数化测试")
        void testToDto(String title, NoticeType type, String content, NoticeStatus status) {
            SysNotice entity = new SysNotice();
            entity.setNoticeId(1L);
            entity.setNoticeTitle(title);
            entity.setNoticeType(type);
            entity.setNoticeContent(content);
            entity.setStatus(status);
            entity.setCreatedBy("admin");
            entity.setCreateTime(LocalDateTime.now());

            NoticeDTO dto = noticeConverter.toDto(entity);

            assertNotNull(dto);
            assertEquals(entity.getNoticeId(), dto.getNoticeId());
            assertEquals(entity.getNoticeTitle(), dto.getNoticeTitle());
            assertEquals(entity.getNoticeType(), dto.getNoticeType());
            assertEquals(entity.getNoticeContent(), dto.getNoticeContent());
            assertEquals(entity.getStatus(), dto.getStatus());
            assertEquals(entity.getCreatedBy(), dto.getCreateBy());
            assertNotNull(dto.getCreateTime());
        }

        private static Stream<Arguments> provideNoticeData() {
            return Stream.of(
                Arguments.of("系统维护通知", NoticeType.NOTICE, "系统将于今晚进行维护", NoticeStatus.NORMAL),
                Arguments.of("新功能发布", NoticeType.ANNOUNCEMENT, "新增用户管理功能", NoticeStatus.NORMAL),
                Arguments.of("过期通知", NoticeType.NOTICE, "此通知已关闭", NoticeStatus.CLOSED)
            );
        }

        @Test
        @DisplayName("Entity 转 DTO - null 值处理")
        void testToDtoWithNull() {
            SysNotice entity = new SysNotice();

            NoticeDTO dto = noticeConverter.toDto(entity);

            assertNotNull(dto);
        }
    }

    @Nested
    @DisplayName("DTO 转 Entity 测试")
    class DtoToEntityTest {

        @ParameterizedTest
        @MethodSource("provideNoticeData")
        @DisplayName("DTO 转 Entity - 参数化测试")
        void testToEntity(String title, NoticeType type, String content, NoticeStatus status) {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeTitle(title);
            dto.setNoticeType(type);
            dto.setNoticeContent(content);
            dto.setStatus(status);

            SysNotice entity = noticeConverter.toEntity(dto);

            assertNotNull(entity);
            assertEquals(dto.getNoticeTitle(), entity.getNoticeTitle());
            assertEquals(dto.getNoticeType(), entity.getNoticeType());
            assertEquals(dto.getNoticeContent(), entity.getNoticeContent());
            assertEquals(dto.getStatus(), entity.getStatus());
        }

        private static Stream<Arguments> provideNoticeData() {
            return Stream.of(
                Arguments.of("系统维护通知", NoticeType.NOTICE, "系统将于今晚进行维护", NoticeStatus.NORMAL),
                Arguments.of("新功能发布", NoticeType.ANNOUNCEMENT, "新增用户管理功能", NoticeStatus.NORMAL),
                Arguments.of("过期通知", NoticeType.NOTICE, "此通知已关闭", NoticeStatus.CLOSED)
            );
        }
    }

    @Nested
    @DisplayName("列表转换测试")
    class ListConversionTest {

        @Test
        @DisplayName("Entity 列表转 DTO 列表")
        void testToDtoList() {
            List<SysNotice> entities = List.of(
                createNotice(1L, "通知1", NoticeType.NOTICE, NoticeStatus.NORMAL),
                createNotice(2L, "通知2", NoticeType.ANNOUNCEMENT, NoticeStatus.NORMAL)
            );

            List<NoticeDTO> dtos = noticeConverter.toDtoList(entities);

            assertNotNull(dtos);
            assertEquals(2, dtos.size());
            assertEquals("通知1", dtos.get(0).getNoticeTitle());
            assertEquals("通知2", dtos.get(1).getNoticeTitle());
        }

        @Test
        @DisplayName("空列表转换")
        void testEmptyList() {
            List<NoticeDTO> dtos = noticeConverter.toDtoList(List.of());

            assertNotNull(dtos);
            assertTrue(dtos.isEmpty());
        }
    }

    private SysNotice createNotice(Long id, String title, NoticeType type, NoticeStatus status) {
        SysNotice notice = new SysNotice();
        notice.setNoticeId(id);
        notice.setNoticeTitle(title);
        notice.setNoticeType(type);
        notice.setNoticeContent("内容");
        notice.setStatus(status);
        return notice;
    }
}
