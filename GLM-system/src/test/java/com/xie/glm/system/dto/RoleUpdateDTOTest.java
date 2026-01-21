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
 * 更新角色 DTO 测试
 *
 * <p>测试 {@link RoleUpdateDTO} 的各种场景：
 * <ul>
 *   <li>对象创建和初始化</li>
 *   <li>必填字段验证（roleId）</li>
 *   <li>可选字段更新</li>
 *   <li>菜单权限关联更新</li>
 *   <li>部分更新支持</li>
 * </ul>
 *
 * @author xie
 */
class RoleUpdateDTOTest {

    // ==================== 基础功能测试 ====================

    @Test
    void testRoleUpdateDTOCreation() {
        // Given & When
        RoleUpdateDTO dto = new RoleUpdateDTO();

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getRoleId()).isNull();
        assertThat(dto.getRoleName()).isNull();
        assertThat(dto.getRoleKey()).isNull();
        assertThat(dto.getMenuIds()).isNull();
    }

    @Test
    void testRoleUpdateDTOFieldSettersAndGetters() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        Long roleId = 1L;
        String roleName = "更新后的角色";
        String roleKey = "updated";
        Integer roleSort = 10;
        String status = "1";
        String remark = "更新后的备注";
        List<Long> menuIds = List.of(1L, 2L);

        // When
        dto.setRoleId(roleId);
        dto.setRoleName(roleName);
        dto.setRoleKey(roleKey);
        dto.setRoleSort(roleSort);
        dto.setStatus(status);
        dto.setRemark(remark);
        dto.setMenuIds(menuIds);

        // Then
        assertThat(dto.getRoleId()).isEqualTo(roleId);
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
    @ValueSource(longs = {1L, 100L, 999L})
    void testRoleIdValidation(Long roleId) {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(roleId);

        // When & Then
        // roleId 是必填字段
        assertThat(dto.getRoleId()).isEqualTo(roleId);
        if (roleId == null) {
            // 实际验证由 Jakarta Validation 注解完成
            // 这里只测试字段设置
        }
    }

    // ==================== 可选字段更新测试 ====================

    @Test
    void testPartialUpdateOnlyRoleName() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        dto.setRoleName("仅更新角色名");

        // When & Then
        assertThat(dto.getRoleId()).isEqualTo(1L);
        assertThat(dto.getRoleName()).isEqualTo("仅更新角色名");
        assertThat(dto.getRoleKey()).isNull();
        assertThat(dto.getRoleSort()).isNull();
        assertThat(dto.getStatus()).isNull();
    }

    @Test
    void testPartialUpdateOnlyStatus() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        dto.setStatus("1");

        // When & Then
        assertThat(dto.getRoleId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo("1");
        assertThat(dto.getRoleName()).isNull();
        assertThat(dto.getRoleKey()).isNull();
    }

    @Test
    void testUpdateAllFields() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        List<Long> menuIds = List.of(1L, 2L, 3L);

        // When
        dto.setRoleId(1L);
        dto.setRoleName("完整更新");
        dto.setRoleKey("full");
        dto.setRoleSort(100);
        dto.setDataScope("2");
        dto.setStatus("0");
        dto.setMenuCheckStrictly(true);
        dto.setDeptCheckStrictly(false);
        dto.setRemark("完整更新所有字段");
        dto.setMenuIds(menuIds);

        // Then
        assertAll("update all fields",
            () -> assertThat(dto.getRoleId()).isEqualTo(1L),
            () -> assertThat(dto.getRoleName()).isEqualTo("完整更新"),
            () -> assertThat(dto.getRoleKey()).isEqualTo("full"),
            () -> assertThat(dto.getRoleSort()).isEqualTo(100),
            () -> assertThat(dto.getDataScope()).isEqualTo("2"),
            () -> assertThat(dto.getStatus()).isEqualTo("0"),
            () -> assertThat(dto.getMenuCheckStrictly()).isTrue(),
            () -> assertThat(dto.getDeptCheckStrictly()).isFalse(),
            () -> assertThat(dto.getRemark()).isEqualTo("完整更新所有字段"),
            () -> assertThat(dto.getMenuIds()).isEqualTo(menuIds)
        );
    }

    // ==================== 状态值测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0, true",
        "1, true"
    })
    void testStatusValues(String status, boolean isValid) {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        dto.setStatus(status);

        // When & Then
        assertThat(dto.getStatus()).isEqualTo(status);
        if (status != null) {
            assertThat(status).isIn("0", "1");
        }
    }

    // ==================== 数据范围测试 ====================

    @ParameterizedTest
    @MethodSource("provideDataScopeValues")
    void testDataScopeValues(String dataScope, boolean isValid) {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        dto.setDataScope(dataScope);

        // When & Then
        assertThat(dto.getDataScope()).isEqualTo(dataScope);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideDataScopeValues() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of("1", true),
            org.junit.jupiter.params.provider.Arguments.of("2", true),
            org.junit.jupiter.params.provider.Arguments.of("3", true),
            org.junit.jupiter.params.provider.Arguments.of("4", true),
            org.junit.jupiter.params.provider.Arguments.of(null, true)
        );
    }

    // ==================== 菜单权限更新测试 ====================

    @Test
    void testUpdateMenuIds() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        List<Long> menuIds = List.of(1L, 2L, 3L, 4L, 5L);

        // When
        dto.setMenuIds(menuIds);

        // Then
        assertThat(dto.getMenuIds()).hasSize(5);
        assertThat(dto.getMenuIds()).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void testClearMenuIds() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        dto.setMenuIds(List.of(1L, 2L, 3L));

        // When
        dto.setMenuIds(List.of());

        // Then
        assertThat(dto.getMenuIds()).isNotNull();
        assertThat(dto.getMenuIds()).isEmpty();
    }

    @Test
    void testNullMenuIdsNoChange() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);

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
        "50, true",
        "100, true",
        "999, true"
    })
    void testRoleSortValues(Integer roleSort, boolean isValid) {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        dto.setRoleSort(roleSort);

        // When & Then
        assertThat(dto.getRoleSort()).isEqualTo(roleSort);
    }

    // ==================== 树选择项关联测试 ====================

    @Test
    void testUpdateMenuCheckStrictly() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);

        // When
        dto.setMenuCheckStrictly(false);

        // Then
        assertThat(dto.getMenuCheckStrictly()).isFalse();
    }

    @Test
    void testUpdateDeptCheckStrictly() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);

        // When
        dto.setDeptCheckStrictly(true);

        // Then
        assertThat(dto.getDeptCheckStrictly()).isTrue();
    }

    @Test
    void testUpdateBothCheckStrictly() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);

        // When
        dto.setMenuCheckStrictly(false);
        dto.setDeptCheckStrictly(false);

        // Then
        assertThat(dto.getMenuCheckStrictly()).isFalse();
        assertThat(dto.getDeptCheckStrictly()).isFalse();
    }

    // ==================== 备注测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {
        "更新后的备注",
        "Updated remark",
        "这是一个测试",
        ""
    })
    void testRemarkValues(String remark) {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);

        // When
        dto.setRemark(remark);

        // Then
        assertThat(dto.getRemark()).isEqualTo(remark);
    }

    // ==================== 边界条件测试 ====================

    @Test
    void testUpdateOnlyRoleId() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();

        // When
        dto.setRoleId(1L);

        // Then
        assertThat(dto.getRoleId()).isEqualTo(1L);
        assertThat(dto.getRoleName()).isNull();
        assertThat(dto.getRoleKey()).isNull();
        assertThat(dto.getRoleSort()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getDataScope()).isNull();
        assertThat(dto.getRemark()).isNull();
        assertThat(dto.getMenuIds()).isNull();
    }

    @Test
    void testMinimalUpdate() {
        // Given
        RoleUpdateDTO dto = new RoleUpdateDTO();

        // When
        dto.setRoleId(1L);
        dto.setRoleName("最小更新");

        // Then
        assertThat(dto.getRoleId()).isEqualTo(1L);
        assertThat(dto.getRoleName()).isEqualTo("最小更新");
    }

    // ==================== 完整更新场景测试 ====================

    @Test
    void testCompleteUpdateScenario() {
        // Given
        List<Long> newMenuIds = List.of(10L, 20L, 30L, 40L, 50L, 100L);

        // When
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        dto.setRoleName("超级管理员");
        dto.setRoleKey("super_admin");
        dto.setRoleSort(0);
        dto.setDataScope("1");
        dto.setStatus("0");
        dto.setMenuCheckStrictly(true);
        dto.setDeptCheckStrictly(true);
        dto.setRemark("更新为超级管理员角色");
        dto.setMenuIds(newMenuIds);

        // Then
        assertAll("complete update scenario",
            () -> assertThat(dto.getRoleId()).isEqualTo(1L),
            () -> assertThat(dto.getRoleName()).isEqualTo("超级管理员"),
            () -> assertThat(dto.getRoleKey()).isEqualTo("super_admin"),
            () -> assertThat(dto.getRoleSort()).isEqualTo(0),
            () -> assertThat(dto.getDataScope()).isEqualTo("1"),
            () -> assertThat(dto.getStatus()).isEqualTo("0"),
            () -> assertThat(dto.getMenuCheckStrictly()).isTrue(),
            () -> assertThat(dto.getDeptCheckStrictly()).isTrue(),
            () -> assertThat(dto.getRemark()).isEqualTo("更新为超级管理员角色"),
            () -> assertThat(dto.getMenuIds()).hasSize(6)
        );
    }
}
