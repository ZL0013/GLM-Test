package com.xie.glm.admin.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 部门视图对象测试类
 *
 * <p>测试 {@link DeptVO} 的基本功能，包括：
 * <ul>
 *   <li>继承 DeptDTO 的所有字段</li>
 *   <li>子部门列表（children）</li>
 *   <li>Lombok 注解功能</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("部门视图对象测试")
class DeptVOTest {

    @Nested
    @DisplayName("基础功能测试")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("应该能够创建空的 VO 对象")
        void shouldCreateEmptyVO() {
            // When
            DeptVO vo = new DeptVO();

            // Then
            assertThat(vo).isNotNull();
        }

        @Test
        @DisplayName("应该能够使用 Builder 创建 VO 对象")
        void shouldCreateVOWithBuilder() {
            // When
            DeptVO vo = DeptVO.builder()
                    .deptId(1L)
                    .deptName("研发部")
                    .parentId(0L)
                    .orderNum(1)
                    .status("0")
                    .build();

            // Then
            assertThat(vo.getDeptId()).isEqualTo(1L);
            assertThat(vo.getDeptName()).isEqualTo("研发部");
            assertThat(vo.getParentId()).isEqualTo(0L);
            assertThat(vo.getOrderNum()).isEqualTo(1);
            assertThat(vo.getStatus()).isEqualTo("0");
        }

        @Test
        @DisplayName("应该能够设置和获取子部门列表")
        void shouldSetAndGetChildren() {
            // Given
            DeptVO vo = new DeptVO();
            DeptVO child1 = DeptVO.builder().deptId(2L).deptName("前端组").build();
            DeptVO child2 = DeptVO.builder().deptId(3L).deptName("后端组").build();

            // When
            vo.setChildren(Arrays.asList(child1, child2));

            // Then
            assertThat(vo.getChildren()).hasSize(2);
            assertThat(vo.getChildren().get(0).getDeptName()).isEqualTo("前端组");
            assertThat(vo.getChildren().get(1).getDeptName()).isEqualTo("后端组");
        }

        @Test
        @DisplayName("应该能够处理 null 子部门列表")
        void shouldHandleNullChildren() {
            // Given
            DeptVO vo = new DeptVO();

            // When
            vo.setChildren(null);

            // Then
            assertThat(vo.getChildren()).isNull();
        }
    }

    @Nested
    @DisplayName("继承功能测试")
    class InheritanceTests {

        @Test
        @DisplayName("应该继承 DeptDTO 的所有字段")
        void shouldInheritAllDTOFields() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            DeptVO vo = DeptVO.builder()
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
            assertThat(vo.getDeptId()).isEqualTo(1L);
            assertThat(vo.getDeptName()).isEqualTo("研发部");
            assertThat(vo.getParentId()).isEqualTo(0L);
            assertThat(vo.getOrderNum()).isEqualTo(1);
            assertThat(vo.getLeader()).isEqualTo("张三");
            assertThat(vo.getPhone()).isEqualTo("13800138000");
            assertThat(vo.getEmail()).isEqualTo("rd@example.com");
            assertThat(vo.getStatus()).isEqualTo("0");
            assertThat(vo.getAncestors()).isEqualTo("0");
            assertThat(vo.getCreateTime()).isEqualTo(now);
            assertThat(vo.getUpdateTime()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("树形结构测试")
    class TreeStructureTests {

        @Test
        @DisplayName("应该能够构建树形结构")
        void shouldBuildTreeStructure() {
            // Given - 创建根部门
            DeptVO root = DeptVO.builder()
                    .deptId(1L)
                    .deptName("研发部")
                    .parentId(0L)
                    .build();

            // Given - 创建一级子部门
            DeptVO child1 = DeptVO.builder()
                    .deptId(2L)
                    .deptName("前端组")
                    .parentId(1L)
                    .build();

            DeptVO child2 = DeptVO.builder()
                    .deptId(3L)
                    .deptName("后端组")
                    .parentId(1L)
                    .build();

            // Given - 创建二级子部门
            DeptVO grandChild = DeptVO.builder()
                    .deptId(4L)
                    .deptName("Java开发组")
                    .parentId(3L)
                    .build();

            // When - 构建树形结构
            child2.setChildren(Arrays.asList(grandChild));
            root.setChildren(Arrays.asList(child1, child2));

            // Then - 验证树形结构
            assertThat(root.getChildren()).hasSize(2);
            assertThat(root.getChildren().get(0).getDeptName()).isEqualTo("前端组");
            assertThat(root.getChildren().get(1).getDeptName()).isEqualTo("后端组");
            assertThat(root.getChildren().get(1).getChildren()).hasSize(1);
            assertThat(root.getChildren().get(1).getChildren().get(0).getDeptName()).isEqualTo("Java开发组");
        }

        @Test
        @DisplayName("应该能够表示叶子节点")
        void shouldRepresentLeafNode() {
            // Given
            DeptVO leaf = DeptVO.builder()
                    .deptId(1L)
                    .deptName("研发部")
                    .parentId(0L)
                    .build();

            // When
            leaf.setChildren(null); // 叶子节点没有子节点

            // Then
            assertThat(leaf.getChildren()).isNull();
        }

        @Test
        @DisplayName("应该能够表示空子部门列表")
        void shouldRepresentEmptyChildren() {
            // Given
            DeptVO vo = DeptVO.builder()
                    .deptId(1L)
                    .deptName("研发部")
                    .parentId(0L)
                    .build();

            // When
            vo.setChildren(Arrays.asList());

            // Then
            assertThat(vo.getChildren()).isEmpty();
        }
    }

    @Nested
    @DisplayName("完整对象测试")
    class CompleteObjectTests {

        @Test
        @DisplayName("应该能够创建完整的树形结构对象")
        void shouldCreateCompleteTreeObject() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When - 创建三级树形结构
            DeptVO root = DeptVO.builder()
                    .deptId(1L)
                    .deptName("公司")
                    .parentId(0L)
                    .orderNum(1)
                    .leader("总经理")
                    .phone("13800138000")
                    .email("company@example.com")
                    .status("0")
                    .ancestors("0")
                    .createTime(now)
                    .updateTime(now)
                    .build();

            DeptVO dept1 = DeptVO.builder()
                    .deptId(2L)
                    .deptName("研发部")
                    .parentId(1L)
                    .orderNum(1)
                    .leader("技术总监")
                    .status("0")
                    .ancestors("0,1")
                    .build();

            DeptVO team1 = DeptVO.builder()
                    .deptId(3L)
                    .deptName("前端组")
                    .parentId(2L)
                    .orderNum(1)
                    .status("0")
                    .ancestors("0,1,2")
                    .build();

            dept1.setChildren(Arrays.asList(team1));
            root.setChildren(Arrays.asList(dept1));

            // Then - 验证完整结构
            assertThat(root.getDeptName()).isEqualTo("公司");
            assertThat(root.getChildren()).hasSize(1);
            assertThat(root.getChildren().get(0).getDeptName()).isEqualTo("研发部");
            assertThat(root.getChildren().get(0).getChildren()).hasSize(1);
            assertThat(root.getChildren().get(0).getChildren().get(0).getDeptName()).isEqualTo("前端组");
        }
    }
}
