package com.xie.glm.system.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SysOperLog 实体类测试
 *
 * @author xie
 */
@DisplayName("操作日志实体测试")
class SysOperLogTest {

    @Nested
    @DisplayName("实体构造测试")
    class ConstructorTest {

        @Test
        @DisplayName("无参构造应该创建空对象")
        void testNoArgConstructor() {
            SysOperLog log = new SysOperLog();

            assertNotNull(log);
            assertNull(log.getOperId());
            assertNull(log.getTitle());
            assertNull(log.getBusinessType());
            assertNull(log.getMethod());
            assertNull(log.getRequestMethod());
            assertNull(log.getOperatorType());
            assertNull(log.getOperName());
            assertNull(log.getDeptName());
            assertNull(log.getOperUrl());
            assertNull(log.getOperIp());
            assertNull(log.getOperLocation());
            assertNull(log.getOperParam());
            assertNull(log.getJsonResult());
            assertNull(log.getStatus());
            assertNull(log.getErrorMsg());
            assertNull(log.getOperTime());
            assertNull(log.getCostTime());
        }

        @Test
        @DisplayName("Builder 模式应该正确构造对象")
        void testBuilder() {
            LocalDateTime now = LocalDateTime.now();

            SysOperLog log = SysOperLog.builder()
                    .title("用户管理")
                    .businessType(1)
                    .method("com.xie.glm.admin.controller.UserController.list()")
                    .requestMethod("GET")
                    .operatorType(1)
                    .operName("admin")
                    .deptName("技术部")
                    .operUrl("/api/system/users")
                    .operIp("127.0.0.1")
                    .operLocation("本地")
                    .operParam("{\"pageNum\":1,\"pageSize\":10}")
                    .jsonResult("{\"code\":0,\"message\":\"success\"}")
                    .status(0)
                    .operTime(now)
                    .costTime(100L)
                    .build();

            assertEquals("用户管理", log.getTitle());
            assertEquals(1, log.getBusinessType());
            assertEquals("com.xie.glm.admin.controller.UserController.list()", log.getMethod());
            assertEquals("GET", log.getRequestMethod());
            assertEquals(1, log.getOperatorType());
            assertEquals("admin", log.getOperName());
            assertEquals("技术部", log.getDeptName());
            assertEquals("/api/system/users", log.getOperUrl());
            assertEquals("127.0.0.1", log.getOperIp());
            assertEquals("本地", log.getOperLocation());
            assertEquals("{\"pageNum\":1,\"pageSize\":10}", log.getOperParam());
            assertEquals("{\"code\":0,\"message\":\"success\"}", log.getJsonResult());
            assertEquals(0, log.getStatus());
            assertEquals(now, log.getOperTime());
            assertEquals(100L, log.getCostTime());
        }
    }

    @Nested
    @DisplayName("Setter 和 Getter 测试")
    class SetterGetterTest {

        @Test
        @DisplayName("应该正确设置和获取属性值")
        void testSetterGetter() {
            SysOperLog log = new SysOperLog();
            LocalDateTime now = LocalDateTime.now();

            log.setOperId(1L);
            log.setTitle("用户管理");
            log.setBusinessType(1);
            log.setMethod("com.xie.glm.admin.controller.UserController.list()");
            log.setRequestMethod("GET");
            log.setOperatorType(1);
            log.setOperName("admin");
            log.setDeptName("技术部");
            log.setOperUrl("/api/system/users");
            log.setOperIp("127.0.0.1");
            log.setOperLocation("本地");
            log.setOperParam("{\"pageNum\":1,\"pageSize\":10}");
            log.setJsonResult("{\"code\":0,\"message\":\"success\"}");
            log.setStatus(0);
            log.setErrorMsg(null);
            log.setOperTime(now);
            log.setCostTime(100L);

            assertEquals(1L, log.getOperId());
            assertEquals("用户管理", log.getTitle());
            assertEquals(1, log.getBusinessType());
            assertEquals("com.xie.glm.admin.controller.UserController.list()", log.getMethod());
            assertEquals("GET", log.getRequestMethod());
            assertEquals(1, log.getOperatorType());
            assertEquals("admin", log.getOperName());
            assertEquals("技术部", log.getDeptName());
            assertEquals("/api/system/users", log.getOperUrl());
            assertEquals("127.0.0.1", log.getOperIp());
            assertEquals("本地", log.getOperLocation());
            assertEquals("{\"pageNum\":1,\"pageSize\":10}", log.getOperParam());
            assertEquals("{\"code\":0,\"message\":\"success\"}", log.getJsonResult());
            assertEquals(0, log.getStatus());
            assertNull(log.getErrorMsg());
            assertEquals(now, log.getOperTime());
            assertEquals(100L, log.getCostTime());
        }
    }

    @Nested
    @DisplayName("Equals 和 HashCode 测试")
    class EqualsHashCodeTest {

        @Test
        @DisplayName("相同的对象应该相等且有相同的 hashCode")
        void testEqualsHashCodeSameObject() {
            SysOperLog log1 = new SysOperLog();
            log1.setOperId(1L);
            log1.setTitle("用户管理");

            SysOperLog log2 = new SysOperLog();
            log2.setOperId(1L);
            log2.setTitle("用户管理");

            assertEquals(log1, log2);
            assertEquals(log1.hashCode(), log2.hashCode());
        }

        @Test
        @DisplayName("不同的对象不应该相等")
        void testEqualsDifferentObject() {
            SysOperLog log1 = new SysOperLog();
            log1.setOperId(1L);

            SysOperLog log2 = new SysOperLog();
            log2.setOperId(2L);

            assertNotEquals(log1, log2);
        }
    }

    @Nested
    @DisplayName("业务类型枚举测试")
    class BusinessTypeTest {

        @Test
        @DisplayName("业务类型应该正确对应")
        void testBusinessTypes() {
            assertEquals(0, SysOperLog.BusinessType.OTHER.getCode());
            assertEquals("其它", SysOperLog.BusinessType.OTHER.getName());

            assertEquals(1, SysOperLog.BusinessType.INSERT.getCode());
            assertEquals("新增", SysOperLog.BusinessType.INSERT.getName());

            assertEquals(2, SysOperLog.BusinessType.UPDATE.getCode());
            assertEquals("修改", SysOperLog.BusinessType.UPDATE.getName());

            assertEquals(3, SysOperLog.BusinessType.DELETE.getCode());
            assertEquals("删除", SysOperLog.BusinessType.DELETE.getName());

            assertEquals(4, SysOperLog.BusinessType.GRANT.getCode());
            assertEquals("授权", SysOperLog.BusinessType.GRANT.getName());

            assertEquals(5, SysOperLog.BusinessType.EXPORT.getCode());
            assertEquals("导出", SysOperLog.BusinessType.EXPORT.getName());

            assertEquals(6, SysOperLog.BusinessType.IMPORT.getCode());
            assertEquals("导入", SysOperLog.BusinessType.IMPORT.getName());

            assertEquals(7, SysOperLog.BusinessType.FORCE.getCode());
            assertEquals("强退", SysOperLog.BusinessType.FORCE.getName());

            assertEquals(8, SysOperLog.BusinessType.GENCODE.getCode());
            assertEquals("生成代码", SysOperLog.BusinessType.GENCODE.getName());

            assertEquals(9, SysOperLog.BusinessType.CLEAN.getCode());
            assertEquals("清空数据", SysOperLog.BusinessType.CLEAN.getName());
        }
    }

    @Nested
    @DisplayName("操作人类别枚举测试")
    class OperatorTypeTest {

        @Test
        @DisplayName("操作人类别应该正确对应")
        void testOperatorTypes() {
            assertEquals(0, SysOperLog.OperatorType.OTHER.getCode());
            assertEquals("其它", SysOperLog.OperatorType.OTHER.getName());

            assertEquals(1, SysOperLog.OperatorType.BACKEND.getCode());
            assertEquals("后台用户", SysOperLog.OperatorType.BACKEND.getName());

            assertEquals(2, SysOperLog.OperatorType.MOBILE.getCode());
            assertEquals("手机端用户", SysOperLog.OperatorType.MOBILE.getName());
        }
    }

    @Nested
    @DisplayName("状态枚举测试")
    class StatusTest {

        @Test
        @DisplayName("状态应该正确对应")
        void testStatus() {
            assertEquals(0, SysOperLog.Status.SUCCESS.getCode());
            assertEquals("成功", SysOperLog.Status.SUCCESS.getName());

            assertEquals(1, SysOperLog.Status.FAIL.getCode());
            assertEquals("失败", SysOperLog.Status.FAIL.getName());
        }
    }
}
