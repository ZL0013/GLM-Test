package com.xie.glm.admin.converter;

import com.xie.glm.admin.GlmApplication;
import com.xie.glm.admin.vo.NoticeVO;
import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import com.xie.glm.system.dto.NoticeDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 通知 VO 转换器测试
 *
 * @author xie
 */
@SpringBootTest(classes = GlmApplication.class)
@DisplayName("通知 VO 转换器测试")
class NoticeVoConverterTest {

    @Autowired
    private NoticeVoConverter noticeVoConverter;

    @Nested
    @DisplayName("DTO 转 VO 测试")
    class DtoToVoTest {

        @ParameterizedTest
        @MethodSource("provideNoticeData")
        @DisplayName("DTO 转 VO - 参数化测试")
        void testToVo(String title, NoticeType type, String content, NoticeStatus status, String typeName, String statusName) {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeId(1L);
            dto.setNoticeTitle(title);
            dto.setNoticeType(type);
            dto.setNoticeContent(content);
            dto.setStatus(status);
            dto.setCreateBy("admin");
            dto.setCreateTime(LocalDateTime.now());

            NoticeVO vo = noticeVoConverter.toVo(dto);

            assertNotNull(vo);
            assertEquals(dto.getNoticeId(), vo.getNoticeId());
            assertEquals(dto.getNoticeTitle(), vo.getNoticeTitle());
            assertEquals(typeName, vo.getNoticeTypeName());
            assertEquals(dto.getNoticeContent(), vo.getNoticeContent());
            assertEquals(statusName, vo.getStatusName());
            assertEquals(dto.getCreateBy(), vo.getCreateBy());
            assertNotNull(vo.getCreateTime());
        }

        private static Stream<Arguments> provideNoticeData() {
            return Stream.of(
                Arguments.of("系统维护通知", NoticeType.NOTICE, "系统将于今晚进行维护", NoticeStatus.NORMAL, "通知", "正常"),
                Arguments.of("新功能发布", NoticeType.ANNOUNCEMENT, "新增用户管理功能", NoticeStatus.NORMAL, "公告", "正常"),
                Arguments.of("过期通知", NoticeType.NOTICE, "此通知已关闭", NoticeStatus.CLOSED, "通知", "关闭")
            );
        }

        @Test
        @DisplayName("DTO 转 VO - null 值处理")
        void testToVoWithNull() {
            NoticeDTO dto = new NoticeDTO();

            NoticeVO vo = noticeVoConverter.toVo(dto);

            assertNotNull(vo);
        }
    }

    @Nested
    @DisplayName("列表转换测试")
    class ListConversionTest {

        @Test
        @DisplayName("DTO 列表转 VO 列表")
        void testToVoList() {
            List<NoticeDTO> dtos = List.of(
                createDto(1L, "通知1", NoticeType.NOTICE, NoticeStatus.NORMAL),
                createDto(2L, "通知2", NoticeType.ANNOUNCEMENT, NoticeStatus.NORMAL)
            );

            List<NoticeVO> vos = noticeVoConverter.toVoList(dtos);

            assertNotNull(vos);
            assertEquals(2, vos.size());
            assertEquals("通知1", vos.get(0).getNoticeTitle());
            assertEquals("通知", vos.get(0).getNoticeTypeName());
            assertEquals("正常", vos.get(0).getStatusName());
            assertEquals("通知2", vos.get(1).getNoticeTitle());
            assertEquals("公告", vos.get(1).getNoticeTypeName());
        }

        @Test
        @DisplayName("空列表转换")
        void testEmptyList() {
            List<NoticeVO> vos = noticeVoConverter.toVoList(List.of());

            assertNotNull(vos);
            assertTrue(vos.isEmpty());
        }
    }

    private NoticeDTO createDto(Long id, String title, NoticeType type, NoticeStatus status) {
        NoticeDTO dto = new NoticeDTO();
        dto.setNoticeId(id);
        dto.setNoticeTitle(title);
        dto.setNoticeType(type);
        dto.setNoticeContent("内容");
        dto.setStatus(status);
        return dto;
    }
}
