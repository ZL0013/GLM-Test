package com.xie.glm.admin.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * RoleVO 单元测试
 *
 * <p>测试角色视图对象的创建和字段设置
 *
 * @author xie
 */
@DisplayName("RoleVO 单元测试")
class RoleVOTest {

    @ParameterizedTest
    @MethodSource("provideRoleVOData")
    @DisplayName("测试 RoleVO 创建和字段设置")
    void testRoleVOCreation(Long roleId, String roleName, String roleKey, Integer roleSort,
                            String dataScope, Boolean menuCheckStrictly, Boolean deptCheckStrictly,
                            String status, String remark, LocalDateTime createTime,
                            LocalDateTime updateTime, List<Long> menuIds, String statusText) {
        // When: 创建 RoleVO 对象
        RoleVO roleVO = RoleVO.builder()
                .roleId(roleId)
                .roleName(roleName)
                .roleKey(roleKey)
                .roleSort(roleSort)
                .dataScope(dataScope)
                .menuCheckStrictly(menuCheckStrictly)
                .deptCheckStrictly(deptCheckStrictly)
                .status(status)
                .remark(remark)
                .createTime(createTime)
                .updateTime(updateTime)
                .menuIds(menuIds)
                .statusText(statusText)
                .build();

        // Then: 验证所有字段设置正确
        assertAll("RoleVO 字段验证",
                () -> assertThat(roleVO.getRoleId()).isEqualTo(roleId),
                () -> assertThat(roleVO.getRoleName()).isEqualTo(roleName),
                () -> assertThat(roleVO.getRoleKey()).isEqualTo(roleKey),
                () -> assertThat(roleVO.getRoleSort()).isEqualTo(roleSort),
                () -> assertThat(roleVO.getDataScope()).isEqualTo(dataScope),
                () -> assertThat(roleVO.getMenuCheckStrictly()).isEqualTo(menuCheckStrictly),
                () -> assertThat(roleVO.getDeptCheckStrictly()).isEqualTo(deptCheckStrictly),
                () -> assertThat(roleVO.getStatus()).isEqualTo(status),
                () -> assertThat(roleVO.getRemark()).isEqualTo(remark),
                () -> assertThat(roleVO.getCreateTime()).isEqualTo(createTime),
                () -> assertThat(roleVO.getUpdateTime()).isEqualTo(updateTime),
                () -> assertThat(roleVO.getMenuIds()).isEqualTo(menuIds),
                () -> assertThat(roleVO.getStatusText()).isEqualTo(statusText)
        );
    }

    @Test
    @DisplayName("测试 RoleVO 带 null 菜单 ID 列表")
    void testRoleVOWithNullMenuIds() {
        // Given: 菜单 ID 列表为 null
        List<Long> menuIds = null;

        // When: 创建 RoleVO
        RoleVO roleVO = RoleVO.builder()
                .roleId(1L)
                .roleName("超级管理员")
                .menuIds(menuIds)
                .build();

        // Then: 验证 menuIds 为 null
        assertThat(roleVO.getMenuIds()).isNull();
    }

    @Test
    @DisplayName("测试 RoleVO 带空菜单 ID 列表")
    void testRoleVOWithEmptyMenuIds() {
        // Given: 空菜单 ID 列表
        List<Long> menuIds = List.of();

        // When: 创建 RoleVO
        RoleVO roleVO = RoleVO.builder()
                .roleId(1L)
                .roleName("超级管理员")
                .menuIds(menuIds)
                .build();

        // Then: 验证 menuIds 为空列表
        assertThat(roleVO.getMenuIds()).isEmpty();
    }

    @Test
    @DisplayName("测试 RoleVO 状态文本映射")
    void testStatusTextMapping() {
        // Given & When & Then: 测试不同状态码映射
        assertAll("状态文本映射",
                () -> {
                    RoleVO vo1 = RoleVO.builder()
                            .status("0")
                            .statusText("正常")
                            .build();
                    assertThat(vo1.getStatusText()).isEqualTo("正常");
                },
                () -> {
                    RoleVO vo2 = RoleVO.builder()
                            .status("1")
                            .statusText("停用")
                            .build();
                    assertThat(vo2.getStatusText()).isEqualTo("停用");
                }
        );
    }

    @Test
    @DisplayName("测试 RoleVO 数据范围文本映射")
    void testDataScopeTextMapping() {
        // Given & When & Then: 测试不同数据范围映射
        assertAll("数据范围文本映射",
                () -> {
                    RoleVO vo1 = RoleVO.builder()
                            .dataScope("1")
                            .dataScopeText("全部数据权限")
                            .build();
                    assertThat(vo1.getDataScopeText()).isEqualTo("全部数据权限");
                },
                () -> {
                    RoleVO vo2 = RoleVO.builder()
                            .dataScope("2")
                            .dataScopeText("自定数据权限")
                            .build();
                    assertThat(vo2.getDataScopeText()).isEqualTo("自定数据权限");
                },
                () -> {
                    RoleVO vo3 = RoleVO.builder()
                            .dataScope("3")
                            .dataScopeText("本部门数据权限")
                            .build();
                    assertThat(vo3.getDataScopeText()).isEqualTo("本部门数据权限");
                },
                () -> {
                    RoleVO vo4 = RoleVO.builder()
                            .dataScope("4")
                            .dataScopeText("本部门及以下数据权限")
                            .build();
                    assertThat(vo4.getDataScopeText()).isEqualTo("本部门及以下数据权限");
                }
        );
    }

    @Test
    @DisplayName("测试 RoleVO 带所有布尔字段")
    void testRoleVOWithAllBooleanFields() {
        // Given: 所有布尔字段为 true
        // When: 创建 RoleVO
        RoleVO roleVO = RoleVO.builder()
                .roleId(1L)
                .roleName("超级管理员")
                .menuCheckStrictly(true)
                .deptCheckStrictly(true)
                .build();

        // Then: 验证布尔字段
        assertAll("布尔字段验证",
                () -> assertThat(roleVO.getMenuCheckStrictly()).isTrue(),
                () -> assertThat(roleVO.getDeptCheckStrictly()).isTrue()
        );
    }

    /**
     * 提供 RoleVO 测试数据
     */
    private static Stream<Arguments> provideRoleVOData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(
                        1L, "超级管理员", "admin", 1,
                        "1", true, true,
                        "0", "超级管理员拥有所有权限", now, now,
                        List.of(1L, 2L, 3L, 4L, 5L), "正常"
                ),
                Arguments.of(
                        2L, "普通角色", "common", 2,
                        "2", false, false,
                        "0", "普通角色", now, now,
                        List.of(1L, 2L), "正常"
                ),
                Arguments.of(
                        3L, "测试角色", "test", 3,
                        "3", null, null,
                        "1", "测试角色备注", null, null,
                        null, "停用"
                )
        );
    }
}
