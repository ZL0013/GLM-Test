package com.xie.glm.admin.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OperLogVO 测试类
 *
 * @author xie
 */
@DisplayName("操作日志视图对象测试")
class OperLogVOTest {

    @Nested
    @DisplayName("构造和默认值测试")
    class ConstructorTest {

        @Test
        @DisplayName("无参构造应该创建空对象")
        void testNoArgConstructor() {
            OperLogVO vo = new OperLogVO();

            assertNotNull(vo);
            assertNull(vo.getOperId());
            assertNull(vo.getTitle());
            assertNull(vo.getBusinessType());
            assertNull(vo.getBusinessTypeName());
            assertNull(vo.getMethod());
            assertNull(vo.getRequestMethod());
            assertNull(vo.getOperatorType());
            assertNull(vo.getOperatorTypeName());
            assertNull(vo.getOperName());
            assertNull(vo.getDeptName());
            assertNull(vo.getOperUrl());
            assertNull(vo.getOperIp());
            assertNull(vo.getOperLocation());
            assertNull(vo.getOperParam());
            assertNull(vo.getJsonResult());
            assertNull(vo.getStatus());
            assertNull(vo.getStatusName());
            assertNull(vo.getErrorMsg());
            assertNull(vo.getOperTime());
            assertNull(vo.getCostTime());
            assertNull(vo.getStatusText());
            assertNull(vo.getBusinessTypeText());
        }

        @Test
        @DisplayName("Builder 模式应该正确构造对象")
        void testBuilder() {
            LocalDateTime now = LocalDateTime.now();

            OperLogVO vo = OperLogVO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .businessTypeName("新增")
                    .businessTypeText("新增")
                    .method("com.xie.glm.admin.controller.UserController.add()")
                    .requestMethod("POST")
                    .operatorType(1)
                    .operatorTypeName("后台用户")
                    .operName("admin")
                    .deptName("技术部")
                    .operUrl("/api/system/users")
                    .operIp("127.0.0.1")
                    .operLocation("本地")
                    .operParam("{\"userName\":\"test\"}")
                    .jsonResult("{\"code\":0}")
                    .status(0)
                    .statusName("成功")
                    .statusText("成功")
                    .operTime(now)
                    .costTime(100L)
                    .build();

            assertEquals(1L, vo.getOperId());
            assertEquals("用户管理", vo.getTitle());
            assertEquals(1, vo.getBusinessType());
            assertEquals("新增", vo.getBusinessTypeName());
            assertEquals("新增", vo.getBusinessTypeText());
            assertEquals("成功", vo.getStatusName());
            assertEquals("成功", vo.getStatusText());
        }
    }

    @Nested
    @DisplayName("Setter 和 Getter 测试")
    class SetterGetterTest {

        @ParameterizedTest
        @ValueSource(longs = {1L, 100L, Long.MAX_VALUE})
        @DisplayName("应该正确设置和获取操作日志ID")
        void testOperIdSetterGetter(Long operId) {
            OperLogVO vo = new OperLogVO();
            vo.setOperId(operId);
            assertEquals(operId, vo.getOperId());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.admin.vo.OperLogVOTest#provideTitleData")
        @DisplayName("应该正确设置和获取标题")
        void testTitleSetterGetter(String title) {
            OperLogVO vo = new OperLogVO();
            vo.setTitle(title);
            assertEquals(title, vo.getTitle());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.admin.vo.OperLogVOTest#provideBusinessTypeData")
        @DisplayName("应该正确设置和获取业务类型")
        void testBusinessTypeSetterGetter(BusinessTypeData data) {
            OperLogVO vo = new OperLogVO();
            vo.setBusinessType(data.code());
            vo.setBusinessTypeName(data.name());
            vo.setBusinessTypeText(data.text());
            assertEquals(data.code(), vo.getBusinessType());
            assertEquals(data.name(), vo.getBusinessTypeName());
            assertEquals(data.text(), vo.getBusinessTypeText());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.admin.vo.OperLogVOTest#provideStatusData")
        @DisplayName("应该正确设置和获取状态")
        void testStatusSetterGetter(StatusData data) {
            OperLogVO vo = new OperLogVO();
            vo.setStatus(data.code());
            vo.setStatusName(data.name());
            vo.setStatusText(data.text());
            assertEquals(data.code(), vo.getStatus());
            assertEquals(data.name(), vo.getStatusName());
            assertEquals(data.text(), vo.getStatusText());
        }

        @ParameterizedTest
        @ValueSource(longs = {0L, 100L, 5000L, Long.MAX_VALUE})
        @DisplayName("应该正确设置和获取消耗时间")
        void testCostTimeSetterGetter(Long costTime) {
            OperLogVO vo = new OperLogVO();
            vo.setCostTime(costTime);
            assertEquals(costTime, vo.getCostTime());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.admin.vo.OperLogVOTest#provideStatusTextData")
        @DisplayName("应该正确设置和获取状态文本")
        void testStatusTextSetterGetter(String statusText) {
            OperLogVO vo = new OperLogVO();
            vo.setStatusText(statusText);
            assertEquals(statusText, vo.getStatusText());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.admin.vo.OperLogVOTest#provideBusinessTypeTextData")
        @DisplayName("应该正确设置和获取业务类型文本")
        void testBusinessTypeTextSetterGetter(String businessTypeText) {
            OperLogVO vo = new OperLogVO();
            vo.setBusinessTypeText(businessTypeText);
            assertEquals(businessTypeText, vo.getBusinessTypeText());
        }
    }

    @Nested
    @DisplayName("Equals 和 HashCode 测试")
    class EqualsHashCodeTest {

        @Test
        @DisplayName("相同的 VO 应该相等且有相同的 hashCode")
        void testEqualsHashCodeSameObject() {
            OperLogVO vo1 = OperLogVO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .build();

            OperLogVO vo2 = OperLogVO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .build();

            assertEquals(vo1, vo2);
            assertEquals(vo1.hashCode(), vo2.hashCode());
        }

        @Test
        @DisplayName("不同的 VO 不应该相等")
        void testEqualsDifferentObject() {
            OperLogVO vo1 = OperLogVO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .build();

            OperLogVO vo2 = OperLogVO.builder()
                    .operId(2L)
                    .title("角色管理")
                    .build();

            assertNotEquals(vo1, vo2);
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

    private static Stream<BusinessTypeData> provideBusinessTypeData() {
        return Stream.of(
            new BusinessTypeData(0, "其它", "其它"),
            new BusinessTypeData(1, "新增", "新增"),
            new BusinessTypeData(2, "修改", "修改"),
            new BusinessTypeData(3, "删除", "删除"),
            new BusinessTypeData(4, "授权", "授权"),
            new BusinessTypeData(5, "导出", "导出"),
            new BusinessTypeData(6, "导入", "导入"),
            new BusinessTypeData(7, "强退", "强退"),
            new BusinessTypeData(8, "生成代码", "生成代码"),
            new BusinessTypeData(9, "清空数据", "清空数据")
        );
    }

    private static Stream<StatusData> provideStatusData() {
        return Stream.of(
            new StatusData(0, "成功", "成功"),
            new StatusData(1, "失败", "失败")
        );
    }

    private static Stream<String> provideStatusTextData() {
        return Stream.of("成功", "失败", "");
    }

    private static Stream<String> provideBusinessTypeTextData() {
        return Stream.of("新增", "修改", "删除", "其它", "");
    }

    // ==================== 测试数据记录 ====================

    record BusinessTypeData(Integer code, String name, String text) {}
    record StatusData(Integer code, String name, String text) {}
}
