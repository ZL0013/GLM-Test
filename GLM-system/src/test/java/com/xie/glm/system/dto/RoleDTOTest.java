package com.xie.glm.system.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 角色数据传输对象测试
 *
 * <p>测试 {@link RoleDTO} 的各种场景：
 * <ul>
 *   <li>对象创建和初始化</li>
 *   <li>字段设置和获取</li>
 *   <li>菜单权限列表</li>
 *   <li>时间字段</li>
 *   <li>状态标志</li>
 * </ul>
 *
 * @author xie
 */
class RoleDTOTest {

    // ==================== 基础功能测试 ====================

    @Test
    void testRoleDTOCreation() {
        // Given & When
        RoleDTO dto = new RoleDTO();

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getRoleId()).isNull();
        assertThat(dto.getRoleName()).isNull();
        assertThat(dto.getRoleKey()).isNull();
        assertThat(dto.getRoleSort()).isNull();
        assertThat(dto.getStatus()).isNull();
    }

    @Test
    void testRoleDTOFieldSettersAndGetters() {
        // Given
        RoleDTO dto = new RoleDTO();
        Long roleId = 1L;
        String roleName = "管理员";
        String roleKey = "admin";
        Integer roleSort = 1;
        String dataScope = "1";
        String status = "0";
        String remark = "系统管理员";
        LocalDateTime createTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime updateTime = LocalDateTime.of(2024, 6, 1, 0, 0);
        List<Long> menuIds = List.of(1L, 2L, 3L);

        // When
        dto.setRoleId(roleId);
        dto.setRoleName(roleName);
        dto.setRoleKey(roleKey);
        dto.setRoleSort(roleSort);
        dto.setDataScope(dataScope);
        dto.setStatus(status);
        dto.setRemark(remark);
        dto.setCreateTime(createTime);
        dto.setUpdateTime(updateTime);
        dto.setMenuIds(menuIds);

        // Then
        assertAll("role dto fields",
            () -> assertThat(dto.getRoleId()).isEqualTo(roleId),
            () -> assertThat(dto.getRoleName()).isEqualTo(roleName),
            () -> assertThat(dto.getRoleKey()).isEqualTo(roleKey),
            () -> assertThat(dto.getRoleSort()).isEqualTo(roleSort),
            () -> assertThat(dto.getDataScope()).isEqualTo(dataScope),
            () -> assertThat(dto.getStatus()).isEqualTo(status),
            () -> assertThat(dto.getRemark()).isEqualTo(remark),
            () -> assertThat(dto.getCreateTime()).isEqualTo(createTime),
            () -> assertThat(dto.getUpdateTime()).isEqualTo(updateTime),
            () -> assertThat(dto.getMenuIds()).isEqualTo(menuIds)
        );
    }

    // ==================== 状态值测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0, 正常",
        "1, 停用"
    })
    void testStatusValues(String status, String description) {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setStatus(status);

        // Then
        assertThat(dto.getStatus()).isEqualTo(status);
        assertThat(status).isIn("0", "1");
    }

    @Test
    void testNullStatus() {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setStatus(null);

        // Then
        assertThat(dto.getStatus()).isNull();
    }

    // ==================== 数据范围测试 ====================

    @ParameterizedTest
    @MethodSource("provideDataScopeValues")
    void testDataScopeValues(String dataScope, String description) {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setDataScope(dataScope);

        // Then
        assertThat(dto.getDataScope()).isEqualTo(dataScope);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideDataScopeValues() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of("1", "全部数据权限"),
            org.junit.jupiter.params.provider.Arguments.of("2", "自定数据权限"),
            org.junit.jupiter.params.provider.Arguments.of("3", "本部门数据权限"),
            org.junit.jupiter.params.provider.Arguments.of("4", "本部门及以下数据权限"),
            org.junit.jupiter.params.provider.Arguments.of(null, "未设置")
        );
    }

    // ==================== 菜单权限测试 ====================

    @Test
    void testMenuIdsAssignment() {
        // Given
        List<Long> menuIds = List.of(1L, 2L, 3L, 4L, 5L);
        RoleDTO dto = new RoleDTO();

        // When
        dto.setMenuIds(menuIds);

        // Then
        assertThat(dto.getMenuIds()).hasSize(5);
        assertThat(dto.getMenuIds()).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void testEmptyMenuIds() {
        // Given
        RoleDTO dto = new RoleDTO();
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
        RoleDTO dto = new RoleDTO();

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
        "10, true",
        "100, true",
        "999, true"
    })
    void testRoleSortValues(Integer roleSort, boolean isValid) {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setRoleSort(roleSort);

        // Then
        assertThat(dto.getRoleSort()).isEqualTo(roleSort);
        assertThat(roleSort).isGreaterThanOrEqualTo(0);
    }

    // ==================== 树选择项关联测试 ====================

    @Test
    void testMenuCheckStrictly() {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setMenuCheckStrictly(true);

        // Then
        assertThat(dto.getMenuCheckStrictly()).isTrue();
    }

    @Test
    void testDeptCheckStrictly() {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setDeptCheckStrictly(false);

        // Then
        assertThat(dto.getDeptCheckStrictly()).isFalse();
    }

    @Test
    void testBothCheckStrictly() {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setMenuCheckStrictly(true);
        dto.setDeptCheckStrictly(true);

        // Then
        assertThat(dto.getMenuCheckStrictly()).isTrue();
        assertThat(dto.getDeptCheckStrictly()).isTrue();
    }

    @Test
    void testNullCheckStrictly() {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setMenuCheckStrictly(null);
        dto.setDeptCheckStrictly(null);

        // Then
        assertThat(dto.getMenuCheckStrictly()).isNull();
        assertThat(dto.getDeptCheckStrictly()).isNull();
    }

    // ==================== 时间字段测试 ====================

    @Test
    void testTimeFields() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        RoleDTO dto = new RoleDTO();

        // When
        dto.setCreateTime(yesterday);
        dto.setUpdateTime(now);

        // Then
        assertThat(dto.getCreateTime()).isBeforeOrEqualTo(dto.getUpdateTime());
        assertThat(dto.getCreateTime()).isEqualTo(yesterday);
        assertThat(dto.getUpdateTime()).isEqualTo(now);
    }

    @Test
    void testNullTimeFields() {
        // Given
        RoleDTO dto = new RoleDTO();

        // When & Then
        assertThat(dto.getCreateTime()).isNull();
        assertThat(dto.getUpdateTime()).isNull();
    }

    // ==================== 备注测试 ====================

    @Test
    void testRemark() {
        // Given
        RoleDTO dto = new RoleDTO();
        String remark = "这是一个测试角色";

        // When
        dto.setRemark(remark);

        // Then
        assertThat(dto.getRemark()).isEqualTo(remark);
    }

    @Test
    void testNullRemark() {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setRemark(null);

        // Then
        assertThat(dto.getRemark()).isNull();
    }

    // ==================== 完整对象测试 ====================

    @Test
    void testCompleteRoleDTO() {
        // Given
        List<Long> menuIds = List.of(1L, 2L, 3L, 100L, 200L, 300L);
        LocalDateTime createTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime updateTime = LocalDateTime.of(2024, 6, 30, 23, 59);

        // When
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(1L);
        dto.setRoleName("超级管理员");
        dto.setRoleKey("super_admin");
        dto.setRoleSort(0);
        dto.setDataScope("1");
        dto.setStatus("0");
        dto.setMenuCheckStrictly(true);
        dto.setDeptCheckStrictly(true);
        dto.setRemark("拥有所有权限的角色");
        dto.setCreateTime(createTime);
        dto.setUpdateTime(updateTime);
        dto.setMenuIds(menuIds);

        // Then
        assertAll("complete role dto",
            () -> assertThat(dto.getRoleId()).isEqualTo(1L),
            () -> assertThat(dto.getRoleName()).isEqualTo("超级管理员"),
            () -> assertThat(dto.getRoleKey()).isEqualTo("super_admin"),
            () -> assertThat(dto.getRoleSort()).isEqualTo(0),
            () -> assertThat(dto.getDataScope()).isEqualTo("1"),
            () -> assertThat(dto.getStatus()).isEqualTo("0"),
            () -> assertThat(dto.getMenuCheckStrictly()).isTrue(),
            () -> assertThat(dto.getDeptCheckStrictly()).isTrue(),
            () -> assertThat(dto.getRemark()).isEqualTo("拥有所有权限的角色"),
            () -> assertThat(dto.getCreateTime()).isEqualTo(createTime),
            () -> assertThat(dto.getUpdateTime()).isEqualTo(updateTime),
            () -> assertThat(dto.getMenuIds()).hasSize(6)
        );
    }

    // ==================== Builder 测试 ====================

    @Test
    void testBuilderPattern() {
        // Given & When
        RoleDTO dto = RoleDTO.builder()
            .roleId(1L)
            .roleName("测试角色")
            .roleKey("test")
            .roleSort(10)
            .status("0")
            .build();

        // Then
        assertAll("builder pattern",
            () -> assertThat(dto.getRoleId()).isEqualTo(1L),
            () -> assertThat(dto.getRoleName()).isEqualTo("测试角色"),
            () -> assertThat(dto.getRoleKey()).isEqualTo("test"),
            () -> assertThat(dto.getRoleSort()).isEqualTo(10),
            () -> assertThat(dto.getStatus()).isEqualTo("0")
        );
    }

    // ==================== 边界条件测试 ====================

    @Test
    void testMinimalRoleDTO() {
        // Given
        RoleDTO dto = new RoleDTO();

        // When
        dto.setRoleId(1L);
        dto.setRoleName("min");

        // Then
        assertThat(dto.getRoleId()).isEqualTo(1L);
        assertThat(dto.getRoleName()).isEqualTo("min");
    }
}
