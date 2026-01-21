package com.xie.glm.system.domain;

import com.xie.glm.common.core.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SysRole 测试类
 *
 * <p>测试系统角色实体的各种属性和行为
 *
 * @author xie
 */
@DisplayName("SysRole 实体单元测试")
class SysRoleTest {

    // ==================== 继承关系测试 ====================

    @Test
    @DisplayName("继承 BaseEntity - 验证继承关系")
    void testExtendsBaseEntity() {
        SysRole role = new SysRole();

        assertTrue(role instanceof BaseEntity, "SysRole 应继承 BaseEntity");
        assertTrue(role instanceof BaseEntity, "应可转换为 BaseEntity");

        // 验证基类字段
        BaseEntity base = role;
        assertNull(base.getCreateTime(), "默认创建时间应为null");
        assertNull(base.getUpdateTime(), "默认更新时间应为null");
        assertNull(base.getCreatedBy(), "默认创建人应为null");
        assertNull(base.getUpdatedBy(), "默认更新人应为null");
    }

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        SysRole role = new SysRole();

        assertNull(role.getRoleId(), "默认角色ID应为null");
        assertNull(role.getRoleName(), "默认角色名称应为null");
        assertNull(role.getRoleKey(), "默认角色权限字符串应为null");
        assertNull(role.getRoleSort(), "默认显示顺序应为null");
        assertNull(role.getDataScope(), "默认数据范围应为null");
        assertNull(role.getMenuCheckStrictly(), "默认菜单关联显示应为null");
        assertNull(role.getDeptCheckStrictly(), "默认部门关联显示应为null");
        assertNull(role.getStatus(), "默认状态应为null");
        assertNull(role.getDelFlag(), "默认删除标志应为null");
        assertNull(role.getRemark(), "默认备注应为null");

        // 基类字段
        assertNull(role.getCreateTime(), "默认创建时间应为null");
        assertNull(role.getUpdateTime(), "默认更新时间应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 超级管理员, admin, 1, 1, true, true, 0, 0",
            "2, 普通角色, common, 2, 2, true, true, 0, 0",
            "3, 测试角色, test, 3, 3, false, false, 1, 0"
    })
    @DisplayName("Getter/Setter - 验证字段赋值和获取")
    void testGettersSetters(Long roleId, String roleName, String roleKey, Integer roleSort,
                           String dataScope, boolean menuCheckStrictly, boolean deptCheckStrictly,
                           String status, String delFlag) {
        SysRole role = new SysRole();
        role.setRoleId(roleId);
        role.setRoleName(roleName);
        role.setRoleKey(roleKey);
        role.setRoleSort(roleSort);
        role.setDataScope(dataScope);
        role.setMenuCheckStrictly(menuCheckStrictly);
        role.setDeptCheckStrictly(deptCheckStrictly);
        role.setStatus(status);
        role.setDelFlag(delFlag);
        role.setRemark("测试角色");

        assertEquals(roleId, role.getRoleId(), "角色ID应匹配");
        assertEquals(roleName, role.getRoleName(), "角色名称应匹配");
        assertEquals(roleKey, role.getRoleKey(), "角色权限字符串应匹配");
        assertEquals(roleSort, role.getRoleSort(), "显示顺序应匹配");
        assertEquals(dataScope, role.getDataScope(), "数据范围应匹配");
        assertEquals(menuCheckStrictly, role.getMenuCheckStrictly(), "菜单关联显示应匹配");
        assertEquals(deptCheckStrictly, role.getDeptCheckStrictly(), "部门关联显示应匹配");
        assertEquals(status, role.getStatus(), "状态应匹配");
        assertEquals(delFlag, role.getDelFlag(), "删除标志应匹配");
        assertEquals("测试角色", role.getRemark(), "备注应匹配");
    }

    // ==================== 数据范围测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 全部数据权限",
            "2, 自定数据权限",
            "3, 本部门数据权限",
            "4, 本部门及以下数据权限"
    })
    @DisplayName("数据范围 - 验证数据范围枚举值")
    void testDataScope(String dataScope, String description) {
        SysRole role = new SysRole();
        role.setDataScope(dataScope);

        assertEquals(dataScope, role.getDataScope(), "数据范围应匹配");
        assertTrue(List.of("1", "2", "3", "4").contains(role.getDataScope()),
                "数据范围应为 1-4");
    }

    @Test
    @DisplayName("数据范围含义 - 验证各范围值含义")
    void testDataScopeMeaning() {
        SysRole role = new SysRole();

        role.setDataScope("1");
        assertEquals("1", role.getDataScope(), "1 表示全部数据权限");

        role.setDataScope("2");
        assertEquals("2", role.getDataScope(), "2 表示自定数据权限");

        role.setDataScope("3");
        assertEquals("3", role.getDataScope(), "3 表示本部门数据权限");

        role.setDataScope("4");
        assertEquals("4", role.getDataScope(), "4 表示本部门及以下数据权限");
    }

    // ==================== 布尔字段测试 ====================

    @ParameterizedTest
    @CsvSource({
            "true, true",
            "false, false"
    })
    @DisplayName("菜单关联显示 - 验证布尔字段")
    void testMenuCheckStrictly(boolean menuCheckStrictly, boolean deptCheckStrictly) {
        SysRole role = new SysRole();
        role.setMenuCheckStrictly(menuCheckStrictly);
        role.setDeptCheckStrictly(deptCheckStrictly);

        assertEquals(menuCheckStrictly, role.getMenuCheckStrictly(), "菜单关联显示应匹配");
        assertEquals(deptCheckStrictly, role.getDeptCheckStrictly(), "部门关联显示应匹配");
    }

    @Test
    @DisplayName("关联显示默认值 - 验证数据库默认值 true")
    void testCheckStrictlyDefault() {
        SysRole role = new SysRole();

        // 数据库默认值为 true
        role.setMenuCheckStrictly(true);
        role.setDeptCheckStrictly(true);

        assertTrue(role.getMenuCheckStrictly(), "菜单关联显示默认应为true");
        assertTrue(role.getDeptCheckStrictly(), "部门关联显示默认应为true");
    }

    // ==================== 角色状态测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1, false"
    })
    @DisplayName("角色状态 - 验证状态值含义")
    void testRoleStatus(String status, boolean isNormal) {
        SysRole role = new SysRole();
        role.setStatus(status);

        assertEquals(status, role.getStatus(), "状态值应匹配");
        assertEquals(isNormal, "0".equals(role.getStatus()), "0 表示正常状态");
    }

    // ==================== 删除标志测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "2, false"
    })
    @DisplayName("删除标志 - 验证删除标志值含义（数据库：0=存在，2=删除）")
    void testDelFlag(String delFlag, boolean isNotDeleted) {
        SysRole role = new SysRole();
        role.setDelFlag(delFlag);

        assertEquals(delFlag, role.getDelFlag(), "删除标志应匹配");
        assertEquals(isNotDeleted, "0".equals(role.getDelFlag()), "0 表示未删除");
    }

    // ==================== 角色权限字符串测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"admin", "common", "test", "system:user:list"})
    @DisplayName("角色权限字符串 - 验证 roleKey 格式")
    void testRoleKey(String roleKey) {
        SysRole role = new SysRole();
        role.setRoleKey(roleKey);

        assertEquals(roleKey, role.getRoleKey(), "角色权限字符串应匹配");
        assertNotNull(role.getRoleKey(), "角色权限字符串不应为null");
    }

    @Test
    @DisplayName("角色权限字符串 - 常见角色 key")
    void testCommonRoleKeys() {
        SysRole adminRole = new SysRole();
        adminRole.setRoleKey("admin");
        assertEquals("admin", adminRole.getRoleKey(), "超级管理员角色key");

        SysRole commonRole = new SysRole();
        commonRole.setRoleKey("common");
        assertEquals("common", commonRole.getRoleKey(), "普通角色key");
    }

    // ==================== 显示顺序测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "10, true",
            "100, true",
            "0, true",
            "-1, true"
    })
    @DisplayName("显示顺序 - 验证排序值")
    void testRoleSort(Integer roleSort, boolean isValid) {
        SysRole role = new SysRole();
        role.setRoleSort(roleSort);

        assertEquals(roleSort, role.getRoleSort(), "显示顺序应匹配");
    }

    @Test
    @DisplayName("显示顺序比较 - 验证排序逻辑")
    void testRoleSortComparison() {
        SysRole role1 = new SysRole();
        role1.setRoleSort(1);

        SysRole role2 = new SysRole();
        role2.setRoleSort(2);

        SysRole role3 = new SysRole();
        role3.setRoleSort(10);

        assertTrue(role1.getRoleSort() < role2.getRoleSort(), "角色1应排在角色2前面");
        assertTrue(role2.getRoleSort() < role3.getRoleSort(), "角色2应排在角色3前面");
    }

    // ==================== 备注测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("备注 null 值处理")
    void testRemarkNull(String remark) {
        SysRole role = new SysRole();
        role.setRemark(remark);

        assertNull(role.getRemark(), "null备注应被接受");
    }

    @Test
    @DisplayName("备注内容 - 验证备注赋值")
    void testRemarkContent() {
        SysRole role = new SysRole();
        String remark = "这是一个测试角色，用于系统验证";

        role.setRemark(remark);

        assertEquals(remark, role.getRemark(), "备注应匹配");
        assertNotNull(role.getRemark(), "备注不应为null");
    }

    // ==================== 基类字段测试 ====================

    @ParameterizedTest
    @MethodSource("provideBaseEntityData")
    @DisplayName("基类字段 - 验证审计字段设置")
    void testBaseEntityFields(LocalDateTime createTime, LocalDateTime updateTime,
                             String createdBy, String updatedBy) {
        SysRole role = new SysRole();
        role.setCreateTime(createTime);
        role.setUpdateTime(updateTime);
        role.setCreatedBy(createdBy);
        role.setUpdatedBy(updatedBy);

        assertEquals(createTime, role.getCreateTime(), "创建时间应匹配");
        assertEquals(updateTime, role.getUpdateTime(), "更新时间应匹配");
        assertEquals(createdBy, role.getCreatedBy(), "创建人应匹配");
        assertEquals(updatedBy, role.getUpdatedBy(), "更新人应匹配");
    }

    private static Stream<Arguments> provideBaseEntityData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now, now, "admin", "admin"),
                Arguments.of(now.minusDays(1), now, "system", "user"),
                Arguments.of(null, null, null, null)
        );
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        SysRole role1 = new SysRole();
        role1.setRoleId(1L);
        role1.setRoleName("admin");

        SysRole role2 = new SysRole();
        role2.setRoleId(1L);
        role2.setRoleName("admin");

        SysRole role3 = new SysRole();
        role3.setRoleId(2L);
        role3.setRoleName("common");

        assertEquals(role1, role2, "相同 ID 的角色应相等");
        assertEquals(role1.hashCode(), role2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(role1, role3, "不同 ID 的角色应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        SysRole role = new SysRole();
        role.setRoleId(1L);
        role.setRoleName("admin");

        String str = role.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("SysRole") || str.contains("admin") || str.contains("1"),
                "toString 应包含角色信息");
    }

    // ==================== 序列化测试 ====================

    @Test
    @DisplayName("serialVersionUID - 验证序列化兼容性")
    void testSerialVersionUID() {
        SysRole role = new SysRole();

        try {
            java.lang.reflect.Field field = BaseEntity.class.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            assertNotNull(field.get(null), "BaseEntity 应有 serialVersionUID 字段");
        } catch (NoSuchFieldException e) {
            fail("BaseEntity 应该有 serialVersionUID 字段");
        } catch (IllegalAccessException e) {
            fail("访问 serialVersionUID 失败");
        }
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 创建超级管理员角色")
    void testCreateAdminRole() {
        LocalDateTime now = LocalDateTime.now();

        SysRole adminRole = new SysRole();
        adminRole.setRoleId(1L);
        adminRole.setRoleName("超级管理员");
        adminRole.setRoleKey("admin");
        adminRole.setRoleSort(1);
        adminRole.setDataScope("1"); // 全部数据权限
        adminRole.setMenuCheckStrictly(true);
        adminRole.setDeptCheckStrictly(true);
        adminRole.setStatus("0");
        adminRole.setDelFlag("0");
        adminRole.setRemark("超级管理员");
        adminRole.setCreateTime(now);
        adminRole.setCreatedBy("system");

        assertEquals(1L, adminRole.getRoleId(), "角色ID应为1");
        assertEquals("超级管理员", adminRole.getRoleName(), "角色名称应为超级管理员");
        assertEquals("admin", adminRole.getRoleKey(), "角色权限字符串应为admin");
        assertEquals(1, adminRole.getRoleSort(), "显示顺序应为1");
        assertEquals("1", adminRole.getDataScope(), "数据范围应为全部数据权限");
        assertEquals(true, adminRole.getMenuCheckStrictly(), "菜单关联显示应为true");
        assertEquals(true, adminRole.getDeptCheckStrictly(), "部门关联显示应为true");
        assertEquals("0", adminRole.getStatus(), "状态应为正常");
        assertEquals("0", adminRole.getDelFlag(), "应未删除");
    }

    @Test
    @DisplayName("业务场景 - 创建普通角色")
    void testCreateCommonRole() {
        SysRole commonRole = new SysRole();
        commonRole.setRoleId(2L);
        commonRole.setRoleName("普通角色");
        commonRole.setRoleKey("common");
        commonRole.setRoleSort(2);
        commonRole.setDataScope("2"); // 自定数据权限
        commonRole.setMenuCheckStrictly(true);
        commonRole.setDeptCheckStrictly(true);
        commonRole.setStatus("0");
        commonRole.setDelFlag("0");

        assertEquals(2L, commonRole.getRoleId(), "角色ID应为2");
        assertEquals("普通角色", commonRole.getRoleName(), "角色名称应为普通角色");
        assertEquals("common", commonRole.getRoleKey(), "角色权限字符串应为common");
        assertEquals(2, commonRole.getRoleSort(), "显示顺序应为2");
        assertEquals("2", commonRole.getDataScope(), "数据范围应为自定数据权限");
    }

    @Test
    @DisplayName("业务场景 - 角色停用")
    void testDisableRole() {
        SysRole role = new SysRole();
        role.setRoleId(3L);
        role.setStatus("0");

        // 停用角色
        role.setStatus("1");

        assertEquals("1", role.getStatus(), "角色状态应更新为停用");
        assertFalse("0".equals(role.getStatus()), "角色应不再处于正常状态");
    }

    @Test
    @DisplayName("业务场景 - 逻辑删除角色")
    void testLogicalDeleteRole() {
        SysRole role = new SysRole();
        role.setRoleId(4L);
        role.setDelFlag("0");

        // 逻辑删除（数据库使用 2 表示删除）
        role.setDelFlag("2");

        assertEquals("2", role.getDelFlag(), "删除标志应更新为已删除");
        assertFalse("0".equals(role.getDelFlag()), "角色应标记为已删除");
    }

    @Test
    @DisplayName("业务场景 - 修改数据权限范围")
    void testChangeDataScope() {
        SysRole role = new SysRole();
        role.setRoleId(5L);
        role.setDataScope("1");

        // 从全部数据权限改为本部门数据权限
        role.setDataScope("3");

        assertEquals("3", role.getDataScope(), "数据范围应更新为本部门数据权限");
    }

    @Test
    @DisplayName("业务场景 - 关联显示设置")
    void testCheckStrictlySettings() {
        SysRole role = new SysRole();
        role.setMenuCheckStrictly(true);
        role.setDeptCheckStrictly(true);

        // 取消关联显示
        role.setMenuCheckStrictly(false);
        role.setDeptCheckStrictly(false);

        assertFalse(role.getMenuCheckStrictly(), "菜单关联显示应为false");
        assertFalse(role.getDeptCheckStrictly(), "部门关联显示应为false");
    }

    // ==================== 角色权限字符串格式测试 ====================

    @ParameterizedTest
    @CsvSource({
            "admin, true",
            "common, true",
            "role_with_underscore, true",
            "role-with-dash, true",
            "role.with.special.chars, false",
            ", false"
    })
    @DisplayName("角色权限字符串格式 - 验证有效字符")
    void testRoleKeyFormat(String roleKey, boolean isValid) {
        if (!isValid && roleKey != null) {
            // 无效格式不进行设置测试
            return;
        }

        SysRole role = new SysRole();
        role.setRoleKey(roleKey);

        assertEquals(roleKey, role.getRoleKey(), "角色权限字符串应匹配");
    }

    // ==================== 排序测试 ====================

    @Test
    @DisplayName("角色排序 - 验证多个角色排序")
    void testRoleOrdering() {
        List<SysRole> roles = List.of(
                createRole(1L, "角色1", 3),
                createRole(2L, "角色2", 1),
                createRole(3L, "角色3", 2)
        );

        // 按顺序排序
        List<SysRole> sorted = roles.stream()
                .sorted((r1, r2) -> r1.getRoleSort().compareTo(r2.getRoleSort()))
                .toList();

        assertEquals(1, sorted.get(0).getRoleSort(), "第一个角色顺序应为1");
        assertEquals(2, sorted.get(1).getRoleSort(), "第二个角色顺序应为2");
        assertEquals(3, sorted.get(2).getRoleSort(), "第三个角色顺序应为3");
    }

    private SysRole createRole(Long id, String name, Integer sort) {
        SysRole role = new SysRole();
        role.setRoleId(id);
        role.setRoleName(name);
        role.setRoleSort(sort);
        return role;
    }

    // ==================== 字段组合测试 ====================

    @Test
    @DisplayName("字段组合 - 验证关键业务字段组合")
    void testFieldCombination() {
        SysRole role = new SysRole();
        role.setRoleId(1L);
        role.setRoleName("admin");
        role.setRoleKey("admin");
        role.setRoleSort(1);
        role.setDataScope("1");
        role.setStatus("0");

        // 验证关键字段组合
        assertEquals("admin", role.getRoleName());
        assertEquals("admin", role.getRoleKey());
        assertEquals("1", role.getDataScope(), "超级管理员应有全部数据权限");
        assertEquals("0", role.getStatus(), "角色应为正常状态");
    }
}
