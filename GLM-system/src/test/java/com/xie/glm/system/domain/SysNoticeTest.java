package com.xie.glm.system.domain;

import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 通知公告实体测试
 *
 * @author xie
 */
@DisplayName("通知公告实体测试")
class SysNoticeTest {

    @Nested
    @DisplayName("实体创建测试")
    class EntityCreationTest {

        @Test
        @DisplayName("创建通知实体 - 成功")
        void testCreateNotice() {
            SysNotice notice = new SysNotice();
            notice.setNoticeTitle("系统维护通知");
            notice.setNoticeType(NoticeType.NOTICE);
            notice.setNoticeContent("系统将于今晚进行维护");
            notice.setStatus(NoticeStatus.NORMAL);

            assertNotNull(notice);
            assertEquals("系统维护通知", notice.getNoticeTitle());
            assertEquals(NoticeType.NOTICE, notice.getNoticeType());
            assertEquals("系统将于今晚进行维护", notice.getNoticeContent());
            assertEquals(NoticeStatus.NORMAL, notice.getStatus());
        }

        @Test
        @DisplayName("创建通知实体 - 公告类型")
        void testCreateAnnouncement() {
            SysNotice notice = new SysNotice();
            notice.setNoticeTitle("新功能发布");
            notice.setNoticeType(NoticeType.ANNOUNCEMENT);
            notice.setStatus(NoticeStatus.NORMAL);

            assertEquals(NoticeType.ANNOUNCEMENT, notice.getNoticeType());
        }

        @Test
        @DisplayName("创建通知实体 - 已关闭状态")
        void testCreateClosedNotice() {
            SysNotice notice = new SysNotice();
            notice.setNoticeTitle("过期通知");
            notice.setStatus(NoticeStatus.CLOSED);

            assertEquals(NoticeStatus.CLOSED, notice.getStatus());
        }
    }

    @Nested
    @DisplayName("Lombok 注解测试")
    class LombokAnnotationTest {

        @Test
        @DisplayName("@Data 注解 - getter/setter 正常工作")
        void testDataAnnotation() {
            SysNotice notice = new SysNotice();
            notice.setNoticeTitle("测试标题");
            notice.setNoticeContent("测试内容");

            assertEquals("测试标题", notice.getNoticeTitle());
            assertEquals("测试内容", notice.getNoticeContent());
        }

        @Test
        @DisplayName("@EqualsAndHashCode(callSuper = true) - 继承父类字段")
        void testEqualsAndHashCode() {
            SysNotice notice1 = new SysNotice();
            notice1.setNoticeId(1L);
            notice1.setNoticeTitle("标题");

            SysNotice notice2 = new SysNotice();
            notice2.setNoticeId(1L);
            notice2.setNoticeTitle("标题");

            assertEquals(notice1, notice2);
            assertEquals(notice1.hashCode(), notice2.hashCode());
        }
    }

    @Nested
    @DisplayName("枚举显示名称测试")
    class EnumDisplayNameTest {

        @Test
        @DisplayName("NoticeType 枚举 - 获取显示名称")
        void testNoticeTypeDisplayName() {
            assertEquals("通知", NoticeType.NOTICE.getDisplayName());
            assertEquals("公告", NoticeType.ANNOUNCEMENT.getDisplayName());
        }

        @Test
        @DisplayName("NoticeStatus 枚举 - 获取显示名称")
        void testNoticeStatusDisplayName() {
            assertEquals("正常", NoticeStatus.NORMAL.getDisplayName());
            assertEquals("关闭", NoticeStatus.CLOSED.getDisplayName());
        }
    }
}
