package com.xie.glm.system.dto.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LogQueryDTO 测试类
 *
 * @author xie
 */
@DisplayName("操作日志查询条件 DTO 测试")
class LogQueryDTOTest {

    @Nested
    @DisplayName("构造和默认值测试")
    class ConstructorTest {

        @Test
        @DisplayName("无参构造应该创建空对象")
        void testNoArgConstructor() {
            LogQueryDTO dto = new LogQueryDTO();

            assertNotNull(dto);
            assertNull(dto.getTitle());
            assertNull(dto.getBusinessType());
            assertNull(dto.getOperName());
            assertNull(dto.getStatus());
            assertNull(dto.getStartTime());
            assertNull(dto.getEndTime());
        }

        @Test
        @DisplayName("应该继承 PageQuery 的分页和排序参数")
        void testInheritPageQuery() {
            LogQueryDTO dto = new LogQueryDTO();

            dto.setPageNum(1);
            dto.setPageSize(10);
            dto.setOrderByColumn("oper_time");
            dto.setIsAsc("desc");

            assertEquals(1, dto.getPageNum());
            assertEquals(10, dto.getPageSize());
            assertEquals("oper_time", dto.getOrderByColumn());
            assertEquals("desc", dto.getIsAsc());
        }
    }

    @Nested
    @DisplayName("Setter 和 Getter 测试")
    class SetterGetterTest {

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.query.LogQueryDTOTest#provideTitleData")
        @DisplayName("应该正确设置和获取标题")
        void testTitleSetterGetter(String title) {
            LogQueryDTO dto = new LogQueryDTO();
            dto.setTitle(title);
            assertEquals(title, dto.getTitle());
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("应该支持 null 标题")
        void testNullTitle(String title) {
            LogQueryDTO dto = new LogQueryDTO();
            dto.setTitle(title);
            assertNull(dto.getTitle());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.query.LogQueryDTOTest#provideBusinessTypeData")
        @DisplayName("应该正确设置和获取业务类型")
        void testBusinessTypeSetterGetter(Integer businessType) {
            LogQueryDTO dto = new LogQueryDTO();
            dto.setBusinessType(businessType);
            assertEquals(businessType, dto.getBusinessType());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.query.LogQueryDTOTest#provideOperNameData")
        @DisplayName("应该正确设置和获取操作人")
        void testOperNameSetterGetter(String operName) {
            LogQueryDTO dto = new LogQueryDTO();
            dto.setOperName(operName);
            assertEquals(operName, dto.getOperName());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.query.LogQueryDTOTest#provideStatusData")
        @DisplayName("应该正确设置和获取状态")
        void testStatusSetterGetter(Integer status) {
            LogQueryDTO dto = new LogQueryDTO();
            dto.setStatus(status);
            assertEquals(status, dto.getStatus());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.query.LogQueryDTOTest#provideTimeRangeData")
        @DisplayName("应该正确设置和获取时间范围")
        void testTimeRangeSetterGetter(LocalDateTime startTime, LocalDateTime endTime) {
            LogQueryDTO dto = new LogQueryDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            assertEquals(startTime, dto.getStartTime());
            assertEquals(endTime, dto.getEndTime());
        }
    }

    @Nested
    @DisplayName("Equals 和 HashCode 测试")
    class EqualsHashCodeTest {

        @Test
        @DisplayName("相同的查询条件应该相等且有相同的 hashCode")
        void testEqualsHashCodeSameObject() {
            LogQueryDTO dto1 = new LogQueryDTO();
            dto1.setTitle("用户管理");
            dto1.setBusinessType(1);
            dto1.setOperName("admin");

            LogQueryDTO dto2 = new LogQueryDTO();
            dto2.setTitle("用户管理");
            dto2.setBusinessType(1);
            dto2.setOperName("admin");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("不同的查询条件不应该相等")
        void testEqualsDifferentObject() {
            LogQueryDTO dto1 = new LogQueryDTO();
            dto1.setTitle("用户管理");

            LogQueryDTO dto2 = new LogQueryDTO();
            dto2.setTitle("角色管理");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("继承的分页参数应该影响相等性")
        void testEqualsWithPageQuery() {
            LogQueryDTO dto1 = new LogQueryDTO();
            dto1.setTitle("用户管理");
            dto1.setPageNum(1);

            LogQueryDTO dto2 = new LogQueryDTO();
            dto2.setTitle("用户管理");
            dto2.setPageNum(2);

            assertNotEquals(dto1, dto2);
        }
    }

    // ==================== 测试数据提供方法 ====================

    private static Stream<String> provideTitleData() {
        return Stream.of(
            "用户管理",
            "角色管理",
            "菜单管理",
            "部门管理",
            ""
        );
    }

    private static Stream<Integer> provideBusinessTypeData() {
        return Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    private static Stream<String> provideOperNameData() {
        return Stream.of("admin", "user", "test", "");
    }

    private static Stream<Integer> provideStatusData() {
        return Stream.of(0, 1);
    }

    private static Stream<Arguments> provideTimeRangeData() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime tomorrow = now.plusDays(1);
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(now, null),
            Arguments.of(null, now),
            Arguments.of(yesterday, tomorrow)
        );
    }
}
