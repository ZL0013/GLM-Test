package com.xie.glm.system.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OperLogDTO 测试类
 *
 * @author xie
 */
@DisplayName("操作日志 DTO 测试")
class OperLogDTOTest {

    @Nested
    @DisplayName("构造和默认值测试")
    class ConstructorTest {

        @Test
        @DisplayName("无参构造应该创建空对象")
        void testNoArgConstructor() {
            OperLogDTO dto = new OperLogDTO();

            assertNotNull(dto);
            assertNull(dto.getOperId());
            assertNull(dto.getTitle());
            assertNull(dto.getBusinessType());
            assertNull(dto.getBusinessTypeName());
            assertNull(dto.getMethod());
            assertNull(dto.getRequestMethod());
            assertNull(dto.getOperatorType());
            assertNull(dto.getOperatorTypeName());
            assertNull(dto.getOperName());
            assertNull(dto.getDeptName());
            assertNull(dto.getOperUrl());
            assertNull(dto.getOperIp());
            assertNull(dto.getOperLocation());
            assertNull(dto.getOperParam());
            assertNull(dto.getJsonResult());
            assertNull(dto.getStatus());
            assertNull(dto.getStatusName());
            assertNull(dto.getErrorMsg());
            assertNull(dto.getOperTime());
            assertNull(dto.getCostTime());
        }

        @Test
        @DisplayName("Builder 模式应该正确构造对象")
        void testBuilder() {
            LocalDateTime now = LocalDateTime.now();

            OperLogDTO dto = OperLogDTO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .businessTypeName("新增")
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
                    .operTime(now)
                    .costTime(100L)
                    .build();

            assertEquals(1L, dto.getOperId());
            assertEquals("用户管理", dto.getTitle());
            assertEquals(1, dto.getBusinessType());
            assertEquals("新增", dto.getBusinessTypeName());
            assertEquals("成功", dto.getStatusName());
        }
    }

    @Nested
    @DisplayName("Setter 和 Getter 测试")
    class SetterGetterTest {

        @ParameterizedTest
        @ValueSource(longs = {1L, 100L, Long.MAX_VALUE})
        @DisplayName("应该正确设置和获取操作日志ID")
        void testOperIdSetterGetter(Long operId) {
            OperLogDTO dto = new OperLogDTO();
            dto.setOperId(operId);
            assertEquals(operId, dto.getOperId());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.OperLogDTOTest#provideTitleData")
        @DisplayName("应该正确设置和获取标题")
        void testTitleSetterGetter(String title) {
            OperLogDTO dto = new OperLogDTO();
            dto.setTitle(title);
            assertEquals(title, dto.getTitle());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.OperLogDTOTest#provideBusinessTypeData")
        @DisplayName("应该正确设置和获取业务类型")
        void testBusinessTypeSetterGetter(BusinessTypeData data) {
            OperLogDTO dto = new OperLogDTO();
            dto.setBusinessType(data.code());
            dto.setBusinessTypeName(data.name());
            assertEquals(data.code(), dto.getBusinessType());
            assertEquals(data.name(), dto.getBusinessTypeName());
        }

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.OperLogDTOTest#provideStatusData")
        @DisplayName("应该正确设置和获取状态")
        void testStatusSetterGetter(StatusData data) {
            OperLogDTO dto = new OperLogDTO();
            dto.setStatus(data.code());
            dto.setStatusName(data.name());
            assertEquals(data.code(), dto.getStatus());
            assertEquals(data.name(), dto.getStatusName());
        }

        @ParameterizedTest
        @ValueSource(longs = {0L, 100L, 5000L, Long.MAX_VALUE})
        @DisplayName("应该正确设置和获取消耗时间")
        void testCostTimeSetterGetter(Long costTime) {
            OperLogDTO dto = new OperLogDTO();
            dto.setCostTime(costTime);
            assertEquals(costTime, dto.getCostTime());
        }
    }

    @Nested
    @DisplayName("Equals 和 HashCode 测试")
    class EqualsHashCodeTest {

        @Test
        @DisplayName("相同的 DTO 应该相等且有相同的 hashCode")
        void testEqualsHashCodeSameObject() {
            OperLogDTO dto1 = OperLogDTO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .build();

            OperLogDTO dto2 = OperLogDTO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .build();

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("不同的 DTO 不应该相等")
        void testEqualsDifferentObject() {
            OperLogDTO dto1 = OperLogDTO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .build();

            OperLogDTO dto2 = OperLogDTO.builder()
                    .operId(2L)
                    .title("角色管理")
                    .build();

            assertNotEquals(dto1, dto2);
        }
    }

    @Nested
    @DisplayName("业务类型枚举转换测试")
    class BusinessTypeConversionTest {

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.OperLogDTOTest#provideBusinessTypeData")
        @DisplayName("应该正确转换业务类型")
        void testBusinessTypeConversion(BusinessTypeData data) {
            OperLogDTO dto = new OperLogDTO();
            dto.setBusinessType(data.code());
            dto.setBusinessTypeName(data.name());

            assertEquals(data.code(), dto.getBusinessType());
            assertEquals(data.name(), dto.getBusinessTypeName());
        }
    }

    @Nested
    @DisplayName("操作状态转换测试")
    class StatusConversionTest {

        @ParameterizedTest
        @MethodSource("com.xie.glm.system.dto.OperLogDTOTest#provideStatusData")
        @DisplayName("应该正确转换操作状态")
        void testStatusConversion(StatusData data) {
            OperLogDTO dto = new OperLogDTO();
            dto.setStatus(data.code());
            dto.setStatusName(data.name());

            assertEquals(data.code(), dto.getStatus());
            assertEquals(data.name(), dto.getStatusName());
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
            new BusinessTypeData(0, "其它"),
            new BusinessTypeData(1, "新增"),
            new BusinessTypeData(2, "修改"),
            new BusinessTypeData(3, "删除"),
            new BusinessTypeData(4, "授权"),
            new BusinessTypeData(5, "导出"),
            new BusinessTypeData(6, "导入"),
            new BusinessTypeData(7, "强退"),
            new BusinessTypeData(8, "生成代码"),
            new BusinessTypeData(9, "清空数据")
        );
    }

    private static Stream<StatusData> provideStatusData() {
        return Stream.of(
            new StatusData(0, "成功"),
            new StatusData(1, "失败")
        );
    }

    // ==================== 测试数据记录 ====================

    record BusinessTypeData(Integer code, String name) {}
    record StatusData(Integer code, String name) {}
}
