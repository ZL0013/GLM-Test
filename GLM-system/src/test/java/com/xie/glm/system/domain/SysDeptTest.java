package com.xie.glm.system.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 部门实体测试类
 *
 * <p>测试 {@link SysDept} 的基本功能，包括：
 * <ul>
 *   <li>Lombok 注解生成的 getter/setter</li>
 *   <li>MyBatis Plus 注解配置</li>
 *   <li>继承 {@link com.xie.glm.common.core.BaseEntity}</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("部门实体测试")
class SysDeptTest {

    @Nested
    @DisplayName("基础功能测试")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("应该能够创建部门实体")
        void shouldCreateDeptEntity() {
            // When
            SysDept dept = new SysDept();

            // Then
            assertThat(dept).isNotNull();
        }

        @Test
        @DisplayName("应该能够设置和获取部门ID")
        void shouldSetAndGetDeptId() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setDeptId(1L);

            // Then
            assertThat(dept.getDeptId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("应该能够设置和获取部门名称")
        void shouldSetAndGetDeptName() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setDeptName("研发部");

            // Then
            assertThat(dept.getDeptName()).isEqualTo("研发部");
        }

        @Test
        @DisplayName("应该能够设置和获取父部门ID")
        void shouldSetAndGetParentId() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setParentId(0L);

            // Then
            assertThat(dept.getParentId()).isEqualTo(0L);
        }

        @Test
        @DisplayName("应该能够设置和获取显示顺序")
        void shouldSetAndGetOrderNum() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setOrderNum(1);

            // Then
            assertThat(dept.getOrderNum()).isEqualTo(1);
        }

        @Test
        @DisplayName("应该能够设置和获取负责人")
        void shouldSetAndGetLeader() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setLeader("张三");

            // Then
            assertThat(dept.getLeader()).isEqualTo("张三");
        }

        @Test
        @DisplayName("应该能够设置和获取联系电话")
        void shouldSetAndGetPhone() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setPhone("13800138000");

            // Then
            assertThat(dept.getPhone()).isEqualTo("13800138000");
        }

        @Test
        @DisplayName("应该能够设置和获取邮箱")
        void shouldSetAndGetEmail() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setEmail("dept@example.com");

            // Then
            assertThat(dept.getEmail()).isEqualTo("dept@example.com");
        }

        @Test
        @DisplayName("应该能够设置和获取状态")
        void shouldSetAndGetStatus() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setStatus("0");

            // Then
            assertThat(dept.getStatus()).isEqualTo("0");
        }

        @Test
        @DisplayName("应该能够设置和获取祖级列表")
        void shouldSetAndGetAncestors() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setAncestors("0,100");

            // Then
            assertThat(dept.getAncestors()).isEqualTo("0,100");
        }
    }

    @Nested
    @DisplayName("继承功能测试")
    class InheritanceTests {

        @Test
        @DisplayName("应该能够设置和获取创建时间")
        void shouldSetAndGetCreateTime() {
            // Given
            SysDept dept = new SysDept();
            LocalDateTime now = LocalDateTime.now();

            // When
            dept.setCreateTime(now);

            // Then
            assertThat(dept.getCreateTime()).isEqualTo(now);
        }

        @Test
        @DisplayName("应该能够设置和获取更新时间")
        void shouldSetAndGetUpdateTime() {
            // Given
            SysDept dept = new SysDept();
            LocalDateTime now = LocalDateTime.now();

            // When
            dept.setUpdateTime(now);

            // Then
            assertThat(dept.getUpdateTime()).isEqualTo(now);
        }

        @Test
        @DisplayName("应该能够设置和获取创建者")
        void shouldSetAndGetCreatedBy() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setCreatedBy("admin");

            // Then
            assertThat(dept.getCreatedBy()).isEqualTo("admin");
        }

        @Test
        @DisplayName("应该能够设置和获取更新者")
        void shouldSetAndGetUpdatedBy() {
            // Given
            SysDept dept = new SysDept();

            // When
            dept.setUpdatedBy("admin");

            // Then
            assertThat(dept.getUpdatedBy()).isEqualTo("admin");
        }
    }

    @Nested
    @DisplayName("完整对象测试")
    class CompleteObjectTests {

        @Test
        @DisplayName("应该能够创建完整的部门对象")
        void shouldCreateCompleteDeptObject() {
            // Given
            SysDept dept = new SysDept();
            LocalDateTime now = LocalDateTime.now();

            // When
            dept.setDeptId(1L);
            dept.setDeptName("研发部");
            dept.setParentId(0L);
            dept.setOrderNum(1);
            dept.setLeader("张三");
            dept.setPhone("13800138000");
            dept.setEmail("rd@example.com");
            dept.setStatus("0");
            dept.setAncestors("0");
            dept.setCreateTime(now);
            dept.setUpdateTime(now);
            dept.setCreatedBy("admin");
            dept.setUpdatedBy("admin");

            // Then
            assertThat(dept.getDeptId()).isEqualTo(1L);
            assertThat(dept.getDeptName()).isEqualTo("研发部");
            assertThat(dept.getParentId()).isEqualTo(0L);
            assertThat(dept.getOrderNum()).isEqualTo(1);
            assertThat(dept.getLeader()).isEqualTo("张三");
            assertThat(dept.getPhone()).isEqualTo("13800138000");
            assertThat(dept.getEmail()).isEqualTo("rd@example.com");
            assertThat(dept.getStatus()).isEqualTo("0");
            assertThat(dept.getAncestors()).isEqualTo("0");
            assertThat(dept.getCreateTime()).isEqualTo(now);
            assertThat(dept.getUpdateTime()).isEqualTo(now);
            assertThat(dept.getCreatedBy()).isEqualTo("admin");
            assertThat(dept.getUpdatedBy()).isEqualTo("admin");
        }
    }
}
