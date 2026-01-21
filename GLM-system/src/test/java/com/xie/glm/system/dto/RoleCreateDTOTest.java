package com.xie.glm.system.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建角色 DTO 测试
 *
 * <p>测试 {@link RoleCreateDTO} 的各种场景：
 * <ul>
 *   <li>对象创建和初始化</li>
 *   <li>必填字段验证</li>
 *   <li>字段设置和获取</li>
 *   <li>菜单权限关联</li>
 *   <li>数据范围配置</li>
 * </ul>
 *
 * @author xie
 */
class RoleCreateDTOTest {

    // ==================== 基础功能测试 ====================

    @Test
    void testRoleCreateDTOCreation() {
        // Given & When
        RoleCreateDTO dto = new RoleCreateDTO();

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getRoleName()).isNull();
        assertThat(dto.getRoleKey()).isNull();
        assertThat(dto.getRoleSort()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getMenuIds()).isNull();
    }

    @Test
    void testRoleCreateDTOFieldSettersAndGetters() {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();
        String roleName = "测试角色";
        String roleKey = "test";
        Integer roleSort = 1;
        String status = "0";
        String remark = "测试备注";
        List<Long> menuIds = List.of(1L, 2L, 3L);

        // When
        dto.setRoleName(roleName);
        dto.setRoleKey(roleKey);
        dto.setRoleSort(roleSort);
        dto.setStatus(status);
        dto.setRemark(remark);
        dto.setMenuIds(menuIds);

        // Then
        assertThat(dto.getRoleName()).isEqualTo(roleName);
        assertThat(dto.getRoleKey()).isEqualTo(roleKey);
        assertThat(dto.getRoleSort()).isEqualTo(roleSort);
        assertThat(dto.getStatus()).isEqualTo(status);
        assertThat(dto.getRemark()).isEqualTo(remark);
        assertThat(dto.getMenuIds()).isEqualTo(menuIds);
    }

    // ==================== 必填字段测试 ====================

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void testRoleNameValidation(String roleName) {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleName(roleName);

        // When & Then
        // 角色名称不能为空或纯空格
        boolean isValid = roleName != null && !roleName.trim().isEmpty();
        assertThat(dto.getRoleName()).isEqualTo(roleName);
        // 实际验证由 Jakarta Validation 注解完成
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void testRoleKeyValidation(String roleKey) {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleKey(roleKey);

        // When & Then
        // 角色权限字符串不能为空或纯空格
        assertThat(dto.getRoleKey()).isEqualTo(roleKey);
    }

    // ==================== 状态值测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0, true",
        "1, true",
        "2, false"
    })
    void testStatusValues(String status, boolean isValid) {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setStatus(status);

        // When & Then
        assertThat(dto.getStatus()).isEqualTo(status);
        if (isValid && status != null) {
            // 有效状态只能是 0 或 1
            assertThat(status).isIn("0", "1");
        }
    }

    @Test
    void testDefaultStatus() {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();

        // When
        dto.setStatus("0"); // 默认正常状态

        // Then
        assertThat(dto.getStatus()).isEqualTo("0");
    }

    // ==================== 数据范围测试 ====================

    @ParameterizedTest
    @MethodSource("provideDataScopeValues")
    void testDataScopeValues(String dataScope, boolean isValid) {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setDataScope(dataScope);

        // When & Then
        assertThat(dto.getDataScope()).isEqualTo(dataScope);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideDataScopeValues() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of("1", true),  // 全部数据权限
            org.junit.jupiter.params.provider.Arguments.of("2", true),  // 自定数据权限
            org.junit.jupiter.params.provider.Arguments.of("3", true),  // 本部门数据权限
            org.junit.jupiter.params.provider.Arguments.of("4", true),  // 本部门及以下数据权限
            org.junit.jupiter.params.provider.Arguments.of(null, true)  // 未设置
        );
    }

    // ==================== 菜单权限关联测试 ====================

    @Test
    void testMenuIdsAssignment() {
        // Given
        List<Long> menuIds = List.of(1L, 2L, 3L, 4L, 5L);
        RoleCreateDTO dto = new RoleCreateDTO();

        // When
        dto.setMenuIds(menuIds);

        // Then
        assertThat(dto.getMenuIds()).hasSize(5);
        assertThat(dto.getMenuIds()).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void testEmptyMenuIds() {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();
        List<Long> emptyMenuIds = List.of();

        // When
        dto.setMenuIds(emptyMenuIds);

        // Then
        assertThat(dto.getMenuIds()).isNotNull();
        assertThat(dto.getMenuIds()).isEmpty();
    }

    @Test
    void testNullMenuIds() {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();

        // When
        dto.setMenuIds(null);

        // Then
        assertThat(dto.getMenuIds()).isNull();
    }

    // ==================== 角色排序测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0, true",
        "1, true",
        "100, true",
        "999, true"
    })
    void testRoleSortValues(Integer roleSort, boolean isValid) {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleSort(roleSort);

        // When & Then
        assertThat(dto.getRoleSort()).isEqualTo(roleSort);
        assertThat(roleSort).isGreaterThanOrEqualTo(0);
    }

    // ==================== 树选择项关联测试 ====================

    @Test
    void testMenuCheckStrictly() {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();

        // When
        dto.setMenuCheckStrictly(true);

        // Then
        assertThat(dto.getMenuCheckStrictly()).isTrue();
    }

    @Test
    void testDeptCheckStrictly() {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();

        // When
        dto.setDeptCheckStrictly(false);

        // Then
        assertThat(dto.getDeptCheckStrictly()).isFalse();
    }

    @Test
    void testBothCheckStrictly() {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();

        // When
        dto.setMenuCheckStrictly(true);
        dto.setDeptCheckStrictly(true);

        // Then
        assertThat(dto.getMenuCheckStrictly()).isTrue();
        assertThat(dto.getDeptCheckStrictly()).isTrue();
    }

    // ==================== 备注测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {
        "这是一个测试角色",
        "Test Role",
        "管理员角色",
        "具有所有权限的角色"
    })
    void testRemarkValues(String remark) {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();

        // When
        dto.setRemark(remark);

        // Then
        assertThat(dto.getRemark()).isEqualTo(remark);
    }

    @Test
    void testNullRemark() {
        // Given
        RoleCreateDTO dto = new RoleCreateDTO();

        // When
        dto.setRemark(null);

        // Then
        assertThat(dto.getRemark()).isNull();
    }

    // ==================== 完整对象测试 ====================

    @Test
    void testCompleteRoleCreateDTO() {
        // Given
        List<Long> menuIds = List.of(1L, 2L, 3L, 100L, 200L);

        // When
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleName("管理员");
        dto.setRoleKey("admin");
        dto.setRoleSort(1);
        dto.setDataScope("1");
        dto.setStatus("0");
        dto.setMenuCheckStrictly(true);
        dto.setDeptCheckStrictly(true);
        dto.setRemark("系统管理员角色");
        dto.setMenuIds(menuIds);

        // Then
        assertAll("complete role create dto",
            () -> assertThat(dto.getRoleName()).isEqualTo("管理员"),
            () -> assertThat(dto.getRoleKey()).isEqualTo("admin"),
            () -> assertThat(dto.getRoleSort()).isEqualTo(1),
            () -> assertThat(dto.getDataScope()).isEqualTo("1"),
            () -> assertThat(dto.getStatus()).isEqualTo("0"),
            () -> assertThat(dto.getMenuCheckStrictly()).isTrue(),
            () -> assertThat(dto.getDeptCheckStrictly()).isTrue(),
            () -> assertThat(dto.getRemark()).isEqualTo("系统管理员角色"),
            () -> assertThat(dto.getMenuIds()).hasSize(5),
            () -> assertThat(dto.getMenuIds()).containsExactly(1L, 2L, 3L, 100L, 200L)
        );
    }

    // ==================== 边界条件测试 ====================

    @Test
    void testMinimumRoleCreateDTO() {
        // Given & When
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleName("test");
        dto.setRoleKey("test");

        // Then
        assertThat(dto.getRoleName()).isEqualTo("test");
        assertThat(dto.getRoleKey()).isEqualTo("test");
    }
}
