package com.xie.glm.system.dto.query;

import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 通知查询条件 DTO 测试
 *
 * @author xie
 */
@DisplayName("通知查询条件 DTO 测试")
class NoticeQueryDTOTest {

    @Nested
    @DisplayName("DTO 创建测试")
    class DtoCreationTest {

        @Test
        @DisplayName("创建查询条件 - 所有字段为空")
        void testCreateEmptyQuery() {
            NoticeQueryDTO query = new NoticeQueryDTO();

            assertNotNull(query);
            assertNull(query.getNoticeTitle());
            assertNull(query.getNoticeType());
            assertNull(query.getStatus());
        }

        @Test
        @DisplayName("创建查询条件 - 按标题查询")
        void testCreateQueryByTitle() {
            NoticeQueryDTO query = new NoticeQueryDTO();
            query.setNoticeTitle("系统维护");

            assertEquals("系统维护", query.getNoticeTitle());
        }

        @Test
        @DisplayName("创建查询条件 - 按类型查询")
        void testCreateQueryByType() {
            NoticeQueryDTO query = new NoticeQueryDTO();
            query.setNoticeType(NoticeType.NOTICE);

            assertEquals(NoticeType.NOTICE, query.getNoticeType());
        }

        @Test
        @DisplayName("创建查询条件 - 按状态查询")
        void testCreateQueryByStatus() {
            NoticeQueryDTO query = new NoticeQueryDTO();
            query.setStatus(NoticeStatus.NORMAL);

            assertEquals(NoticeStatus.NORMAL, query.getStatus());
        }

        @Test
        @DisplayName("创建查询条件 - 组合查询")
        void testCreateCombinedQuery() {
            NoticeQueryDTO query = new NoticeQueryDTO();
            query.setNoticeTitle("系统");
            query.setNoticeType(NoticeType.NOTICE);
            query.setStatus(NoticeStatus.NORMAL);

            assertEquals("系统", query.getNoticeTitle());
            assertEquals(NoticeType.NOTICE, query.getNoticeType());
            assertEquals(NoticeStatus.NORMAL, query.getStatus());
        }
    }

    @Nested
    @DisplayName("分页查询测试")
    class PaginationTest {

        @Test
        @DisplayName("分页参数 - 默认值")
        void testDefaultPagination() {
            NoticeQueryDTO query = new NoticeQueryDTO();

            // 继承自 PageQuery，应该有默认分页参数
            assertNotNull(query);
        }

        @Test
        @DisplayName("分页参数 - 自定义值")
        void testCustomPagination() {
            NoticeQueryDTO query = new NoticeQueryDTO();
            query.setPageNum(2);
            query.setPageSize(20);

            assertEquals(2, query.getPageNum());
            assertEquals(20, query.getPageSize());
        }
    }

    @Nested
    @DisplayName("Lombok 注解测试")
    class LombokAnnotationTest {

        @Test
        @DisplayName("@Data 注解 - getter/setter 正常工作")
        void testDataAnnotation() {
            NoticeQueryDTO query = new NoticeQueryDTO();
            query.setNoticeTitle("测试标题");

            assertEquals("测试标题", query.getNoticeTitle());
        }
    }
}
