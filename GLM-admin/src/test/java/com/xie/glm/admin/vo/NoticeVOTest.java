package com.xie.glm.admin.vo;

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
 * 通知 VO 测试
 *
 * @author xie
 */
@DisplayName("通知 VO 测试")
class NoticeVOTest {

    @Nested
    @DisplayName("VO 创建测试")
    class VoCreationTest {

        @ParameterizedTest
        @MethodSource("provideNoticeData")
        @DisplayName("创建通知 VO - 参数化测试")
        void testCreateNoticeVO(String title, String typeName, String content, String statusName) {
            NoticeVO vo = new NoticeVO();
            vo.setNoticeTitle(title);
            vo.setNoticeTypeName(typeName);
            vo.setNoticeContent(content);
            vo.setStatusName(statusName);

            assertNotNull(vo);
            assertEquals(title, vo.getNoticeTitle());
            assertEquals(typeName, vo.getNoticeTypeName());
            assertEquals(content, vo.getNoticeContent());
            assertEquals(statusName, vo.getStatusName());
        }

        private static Stream<Arguments> provideNoticeData() {
            return Stream.of(
                Arguments.of("系统维护通知", "通知", "系统将于今晚进行维护", "正常"),
                Arguments.of("新功能发布", "公告", "新增用户管理功能", "正常"),
                Arguments.of("过期通知", "通知", "此通知已关闭", "关闭")
            );
        }

        @Test
        @DisplayName("创建通知 VO - 包含所有字段")
        void testCreateFullNoticeVO() {
            NoticeVO vo = new NoticeVO();
            vo.setNoticeId(1L);
            vo.setNoticeTitle("完整通知");
            vo.setNoticeTypeName("通知");
            vo.setNoticeContent("完整内容");
            vo.setStatusName("正常");
            vo.setCreateBy("admin");
            vo.setCreateTime(LocalDateTime.now());
            vo.setUpdateTime(LocalDateTime.now());
            vo.setRemark("测试备注");

            assertEquals(1L, vo.getNoticeId());
            assertEquals("完整通知", vo.getNoticeTitle());
            assertEquals("通知", vo.getNoticeTypeName());
            assertEquals("正常", vo.getStatusName());
            assertEquals("admin", vo.getCreateBy());
            assertNotNull(vo.getCreateTime());
            assertEquals("测试备注", vo.getRemark());
        }
    }

    @Nested
    @DisplayName("VO 字段验证测试")
    class FieldValidationTest {

        @Test
        @DisplayName("通知类型名称 - 通知")
        void testNoticeTypeName() {
            NoticeVO vo = new NoticeVO();
            vo.setNoticeTypeName("通知");

            assertEquals("通知", vo.getNoticeTypeName());
        }

        @Test
        @DisplayName("通知类型名称 - 公告")
        void testAnnouncementTypeName() {
            NoticeVO vo = new NoticeVO();
            vo.setNoticeTypeName("公告");

            assertEquals("公告", vo.getNoticeTypeName());
        }

        @Test
        @DisplayName("通知状态名称 - 正常")
        void testNormalStatusName() {
            NoticeVO vo = new NoticeVO();
            vo.setStatusName("正常");

            assertEquals("正常", vo.getStatusName());
        }

        @Test
        @DisplayName("通知状态名称 - 关闭")
        void testClosedStatusName() {
            NoticeVO vo = new NoticeVO();
            vo.setStatusName("关闭");

            assertEquals("关闭", vo.getStatusName());
        }
    }

    @Nested
    @DisplayName("Lombok 注解测试")
    class LombokAnnotationTest {

        @Test
        @DisplayName("@Data 注解 - getter/setter 正常工作")
        void testDataAnnotation() {
            NoticeVO vo = new NoticeVO();
            vo.setNoticeTitle("测试标题");
            vo.setNoticeContent("测试内容");

            assertEquals("测试标题", vo.getNoticeTitle());
            assertEquals("测试内容", vo.getNoticeContent());
        }
    }
}
