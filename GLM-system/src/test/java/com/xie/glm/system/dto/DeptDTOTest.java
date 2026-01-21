package com.xie.glm.system.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 部门数据传输对象测试类
 *
 * <p>测试 {@link DeptDTO} 的基本功能，包括：
 * <ul>
 *   <li>Lombok 注解生成的 getter/setter</li>
 *   <li>Builder 模式</li>
 *   <li>序列化</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("部门 DTO 测试")
class DeptDTOTest {

    @Nested
    @DisplayName("基础功能测试")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("应该能够创建空的 DTO 对象")
        void shouldCreateEmptyDTO() {
            // When
            DeptDTO dto = new DeptDTO();

            // Then
            assertThat(dto).isNotNull();
        }

        @Test
        @DisplayName("应该能够使用 Builder 创建 DTO 对象")
        void shouldCreateDTOWithBuilder() {
            // When
            DeptDTO dto = DeptDTO.builder()
                    .deptId(1L)
                    .deptName("研发部")
                    .parentId(0L)
                    .orderNum(1)
                    .status("0")
                    .build();

            // Then
            assertThat(dto.getDeptId()).isEqualTo(1L);
            assertThat(dto.getDeptName()).isEqualTo("研发部");
            assertThat(dto.getParentId()).isEqualTo(0L);
            assertThat(dto.getOrderNum()).isEqualTo(1);
            assertThat(dto.getStatus()).isEqualTo("0");
        }

        @Test
        @DisplayName("应该能够设置和获取部门ID")
        void shouldSetAndGetDeptId() {
            // Given
            DeptDTO dto = new DeptDTO();

            // When
            dto.setDeptId(1L);

            // Then
            assertThat(dto.getDeptId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("应该能够设置和获取部门名称")
        void shouldSetAndGetDeptName() {
            // Given
            DeptDTO dto = new DeptDTO();

            // When
            dto.setDeptName("研发部");

            // Then
            assertThat(dto.getDeptName()).isEqualTo("研发部");
        }

        @Test
        @DisplayName("应该能够设置和获取父部门ID")
        void shouldSetAndGetParentId() {
            // Given
            DeptDTO dto = new DeptDTO();

            // When
            dto.setParentId(0L);

            // Then
            assertThat(dto.getParentId()).isEqualTo(0L);
        }

        @Test
        @DisplayName("应该能够设置和获取显示顺序")
        void shouldSetAndGetOrderNum() {
            // Given
            DeptDTO dto = new DeptDTO();

            // When
            dto.setOrderNum(1);

            // Then
            assertThat(dto.getOrderNum()).isEqualTo(1);
        }

        @Test
        @DisplayName("应该能够设置和获取负责人")
        void shouldSetAndGetLeader() {
            // Given
            DeptDTO dto = new DeptDTO();

            // When
            dto.setLeader("张三");

            // Then
            assertThat(dto.getLeader()).isEqualTo("张三");
        }

        @Test
        @DisplayName("应该能够设置和获取联系电话")
        void shouldSetAndGetPhone() {
            // Given
            DeptDTO dto = new DeptDTO();

            // When
            dto.setPhone("13800138000");

            // Then
            assertThat(dto.getPhone()).isEqualTo("13800138000");
        }

        @Test
        @DisplayName("应该能够设置和获取邮箱")
        void shouldSetAndGetEmail() {
            // Given
            DeptDTO dto = new DeptDTO();

            // When
            dto.setEmail("dept@example.com");

            // Then
            assertThat(dto.getEmail()).isEqualTo("dept@example.com");
        }

        @Test
        @DisplayName("应该能够设置和获取状态")
        void shouldSetAndGetStatus() {
            // Given
            DeptDTO dto = new DeptDTO();

            // When
            dto.setStatus("0");

            // Then
            assertThat(dto.getStatus()).isEqualTo("0");
        }

        @Test
        @DisplayName("应该能够设置和获取祖级列表")
        void shouldSetAndGetAncestors() {
            // Given
            DeptDTO dto = new DeptDTO();

            // When
            dto.setAncestors("0,100");

            // Then
            assertThat(dto.getAncestors()).isEqualTo("0,100");
        }
    }

    @Nested
    @DisplayName("时间字段测试")
    class TimeFieldTests {

        @Test
        @DisplayName("应该能够设置和获取创建时间")
        void shouldSetAndGetCreateTime() {
            // Given
            DeptDTO dto = new DeptDTO();
            LocalDateTime now = LocalDateTime.now();

            // When
            dto.setCreateTime(now);

            // Then
            assertThat(dto.getCreateTime()).isEqualTo(now);
        }

        @Test
        @DisplayName("应该能够设置和获取更新时间")
        void shouldSetAndGetUpdateTime() {
            // Given
            DeptDTO dto = new DeptDTO();
            LocalDateTime now = LocalDateTime.now();

            // When
            dto.setUpdateTime(now);

            // Then
            assertThat(dto.getUpdateTime()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("完整对象测试")
    class CompleteObjectTests {

        @Test
        @DisplayName("应该能够创建完整的 DTO 对象")
        void shouldCreateCompleteDTO() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            DeptDTO dto = DeptDTO.builder()
                    .deptId(1L)
                    .deptName("研发部")
                    .parentId(0L)
                    .orderNum(1)
                    .leader("张三")
                    .phone("13800138000")
                    .email("rd@example.com")
                    .status("0")
                    .ancestors("0")
                    .createTime(now)
                    .updateTime(now)
                    .build();

            // Then
            assertThat(dto.getDeptId()).isEqualTo(1L);
            assertThat(dto.getDeptName()).isEqualTo("研发部");
            assertThat(dto.getParentId()).isEqualTo(0L);
            assertThat(dto.getOrderNum()).isEqualTo(1);
            assertThat(dto.getLeader()).isEqualTo("张三");
            assertThat(dto.getPhone()).isEqualTo("13800138000");
            assertThat(dto.getEmail()).isEqualTo("rd@example.com");
            assertThat(dto.getStatus()).isEqualTo("0");
            assertThat(dto.getAncestors()).isEqualTo("0");
            assertThat(dto.getCreateTime()).isEqualTo(now);
            assertThat(dto.getUpdateTime()).isEqualTo(now);
        }

        @Test
        @DisplayName("应该能够使用全参构造函数")
        void shouldUseAllArgsConstructor() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            DeptDTO dto = new DeptDTO(
                    1L,
                    "研发部",
                    0L,
                    1,
                    "张三",
                    "13800138000",
                    "rd@example.com",
                    "0",
                    "0",
                    now,
                    now
            );

            // Then
            assertThat(dto.getDeptId()).isEqualTo(1L);
            assertThat(dto.getDeptName()).isEqualTo("研发部");
            assertThat(dto.getParentId()).isEqualTo(0L);
            assertThat(dto.getOrderNum()).isEqualTo(1);
            assertThat(dto.getLeader()).isEqualTo("张三");
            assertThat(dto.getPhone()).isEqualTo("13800138000");
            assertThat(dto.getEmail()).isEqualTo("rd@example.com");
            assertThat(dto.getStatus()).isEqualTo("0");
            assertThat(dto.getAncestors()).isEqualTo("0");
            assertThat(dto.getCreateTime()).isEqualTo(now);
            assertThat(dto.getUpdateTime()).isEqualTo(now);
        }
    }
}
