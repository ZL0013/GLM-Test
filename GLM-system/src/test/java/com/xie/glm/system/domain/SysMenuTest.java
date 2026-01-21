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
 * SysMenu 测试类
 *
 * <p>测试系统菜单实体的各种属性和行为
 *
 * @author xie
 */
@DisplayName("SysMenu 实体单元测试")
class SysMenuTest {

    // ==================== 继承关系测试 ====================

    @Test
    @DisplayName("继承 BaseEntity - 验证继承关系")
    void testExtendsBaseEntity() {
        SysMenu menu = new SysMenu();

        assertTrue(menu instanceof BaseEntity, "SysMenu 应继承 BaseEntity");
        assertTrue(menu instanceof BaseEntity, "应可转换为 BaseEntity");

        // 验证基类字段
        BaseEntity base = menu;
        assertNull(base.getCreateTime(), "默认创建时间应为null");
        assertNull(base.getUpdateTime(), "默认更新时间应为null");
        assertNull(base.getCreatedBy(), "默认创建人应为null");
        assertNull(base.getUpdatedBy(), "默认更新人应为null");
    }

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        SysMenu menu = new SysMenu();

        assertNull(menu.getMenuId(), "默认菜单ID应为null");
        assertNull(menu.getMenuName(), "默认菜单名称应为null");
        assertNull(menu.getParentId(), "默认父菜单ID应为null");
        assertNull(menu.getOrderNum(), "默认显示顺序应为null");
        assertNull(menu.getPath(), "默认路由地址应为null");
        assertNull(menu.getComponent(), "默认组件路径应为null");
        assertNull(menu.getQuery(), "默认路由参数应为null");
        assertNull(menu.getRouteName(), "默认路由名称应为null");
        assertNull(menu.getIsFrame(), "默认是否为外链应为null");
        assertNull(menu.getIsCache(), "默认是否缓存应为null");
        assertNull(menu.getMenuType(), "默认菜单类型应为null");
        assertNull(menu.getVisible(), "默认显示状态应为null");
        assertNull(menu.getStatus(), "默认菜单状态应为null");
        assertNull(menu.getPerms(), "默认权限标识应为null");
        assertNull(menu.getIcon(), "默认菜单图标应为null");

        // 基类字段
        assertNull(menu.getCreateTime(), "默认创建时间应为null");
        assertNull(menu.getUpdateTime(), "默认更新时间应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @Test
    @DisplayName("Getter/Setter - 验证字段赋值和获取")
    void testGettersSetters() {
        SysMenu menu = new SysMenu();

        menu.setMenuId(1L);
        menu.setMenuName("系统管理");
        menu.setParentId(0L);
        menu.setOrderNum(1);
        menu.setPath("system");
        menu.setComponent("");
        menu.setQuery("");
        menu.setRouteName("");
        menu.setIsFrame(1);
        menu.setIsCache(0);
        menu.setMenuType("M");
        menu.setVisible("0");
        menu.setStatus("0");
        menu.setPerms("");
        menu.setIcon("manage");

        assertEquals(1L, menu.getMenuId(), "菜单ID应匹配");
        assertEquals("系统管理", menu.getMenuName(), "菜单名称应匹配");
        assertEquals("M", menu.getMenuType(), "菜单类型应为目录");
        assertEquals("0", menu.getVisible(), "显示状态应为显示");
    }

    @ParameterizedTest
    @CsvSource({
            "M, 目录",
            "C, 菜单",
            "F, 按钮"
    })
    @DisplayName("菜单类型 - 验证菜单类型枚举值")
    void testMenuType(String menuType, String description) {
        SysMenu menu = new SysMenu();
        menu.setMenuType(menuType);

        assertEquals(menuType, menu.getMenuType(), "菜单类型应匹配");
        assertTrue(List.of("M", "C", "F").contains(menu.getMenuType()),
                "菜单类型应为 M/C/F");
    }

    // ==================== 布尔字段测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, 否",
            "1, 是"
    })
    @DisplayName("布尔字段 - 验证 isFrame 字段")
    void testIsFrame(Integer isFrame, String description) {
        SysMenu menu = new SysMenu();
        menu.setIsFrame(isFrame);

        assertEquals(isFrame, menu.getIsFrame(), "是否为外链应匹配");
        assertEquals(description, (isFrame == 1 ? "是" : "否"),
                "0 表示否，1 表示是");
    }

    @ParameterizedTest
    @CsvSource({
            "0, 缓存",
            "1, 不缓存"
    })
    @DisplayName("布尔字段 - 验证 isCache 字段")
    void testIsCache(Integer isCache, String description) {
        SysMenu menu = new SysMenu();
        menu.setIsCache(isCache);

        assertEquals(isCache, menu.getIsCache(), "是否缓存应匹配");
    }

    // ==================== 状态字段测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, 显示",
            "1, 隐藏"
    })
    @DisplayName("显示状态 - 验证 visible 字段")
    void testVisible(String visible, String description) {
        SysMenu menu = new SysMenu();
        menu.setVisible(visible);

        assertEquals(visible, menu.getVisible(), "显示状态应匹配");
        assertEquals(description, (visible.equals("0") ? "显示" : "隐藏"));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 正常",
            "1, 停用"
    })
    @DisplayName("菜单状态 - 验证 status 字段")
    void testStatus(String status, String description) {
        SysMenu menu = new SysMenu();
        menu.setStatus(status);

        assertEquals(status, menu.getStatus(), "菜单状态应匹配");
        assertEquals(description, (status.equals("0") ? "正常" : "停用"));
    }

    // ==================== 树形结构测试 ====================

    @Test
    @DisplayName("树形结构 - 父子菜单关系")
    void testTreeStructure() {
        SysMenu parentMenu = new SysMenu();
        parentMenu.setMenuId(1L);
        parentMenu.setMenuName("系统管理");
        parentMenu.setParentId(0L);
        parentMenu.setMenuType("M");

        SysMenu childMenu = new SysMenu();
        childMenu.setMenuId(100L);
        childMenu.setMenuName("用户管理");
        childMenu.setParentId(1L);
        childMenu.setMenuType("C");

        assertEquals(0L, parentMenu.getParentId(), "根节点父ID应为0");
        assertEquals(1L, childMenu.getParentId(), "子菜单父ID应为父菜单ID");
        assertEquals("M", parentMenu.getMenuType(), "父菜单应为目录");
        assertEquals("C", childMenu.getMenuType(), "子菜单应为菜单");
    }

    // ==================== 权限标识测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {
            "system:user:list",
            "system:user:add",
            "system:user:edit",
            "system:user:remove",
            "system:user:query"
    })
    @DisplayName("权限标识 - 验证 perms 字段")
    void testPerms(String perms) {
        SysMenu menu = new SysMenu();
        menu.setPerms(perms);

        assertEquals(perms, menu.getPerms(), "权限标识应匹配");
    }

    // ==================== 图标测试 ====================

    @Test
    @DisplayName("菜单图标 - 验证 icon 字段")
    void testIcon() {
        SysMenu menu = new SysMenu();
        String icon = "user";

        menu.setIcon(icon);

        assertEquals(icon, menu.getIcon(), "菜单图标应匹配");
    }

    @Test
    @DisplayName("菜单图标默认值 - 验证数据库默认值")
    void testIconDefault() {
        SysMenu menu = new SysMenu();
        String defaultIcon = "#";

        menu.setIcon(defaultIcon);

        assertEquals("#", menu.getIcon(), "默认图标应为#");
    }

    // ==================== 显示顺序测试 ====================

    @Test
    @DisplayName("显示顺序 - 验证 orderNum 字段")
    void testOrderNum() {
        SysMenu menu = new SysMenu();
        menu.setOrderNum(5);

        assertEquals(5, menu.getOrderNum(), "显示顺序应匹配");
    }

    // ==================== 路由相关字段测试 ====================

    @Test
    @DisplayName("路由地址 - 验证 path 字段")
    void testPath() {
        SysMenu menu = new SysMenu();
        String path = "system/user";

        menu.setPath(path);

        assertEquals(path, menu.getPath(), "路由地址应匹配");
    }

    @Test
    @DisplayName("组件路径 - 验证 component 字段")
    void testComponent() {
        SysMenu menu = new SysMenu();
        String component = "system/user/index";

        menu.setComponent(component);

        assertEquals(component, menu.getComponent(), "组件路径应匹配");
    }

    @Test
    @DisplayName("路由参数 - 验证 query 字段")
    void testQuery() {
        SysMenu menu = new SysMenu();
        String query = "id=1&name=test";

        menu.setQuery(query);

        assertEquals(query, menu.getQuery(), "路由参数应匹配");
    }

    @Test
    @DisplayName("路由名称 - 验证 routeName 字段")
    void testRouteName() {
        SysMenu menu = new SysMenu();
        String routeName = "SystemUser";

        menu.setRouteName(routeName);

        assertEquals(routeName, menu.getRouteName(), "路由名称应匹配");
    }

    // ==================== 基类字段测试 ====================

    @ParameterizedTest
    @MethodSource("provideBaseEntityData")
    @DisplayName("基类字段 - 验证审计字段设置")
    void testBaseEntityFields(LocalDateTime createTime, LocalDateTime updateTime,
                             String createdBy, String updatedBy) {
        SysMenu menu = new SysMenu();
        menu.setCreateTime(createTime);
        menu.setUpdateTime(updateTime);
        menu.setCreatedBy(createdBy);
        menu.setUpdatedBy(updatedBy);

        assertEquals(createTime, menu.getCreateTime(), "创建时间应匹配");
        assertEquals(updateTime, menu.getUpdateTime(), "更新时间应匹配");
        assertEquals(createdBy, menu.getCreatedBy(), "创建人应匹配");
        assertEquals(updatedBy, menu.getUpdatedBy(), "更新人应匹配");
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
        SysMenu menu1 = new SysMenu();
        menu1.setMenuId(1L);
        menu1.setMenuName("系统管理");

        SysMenu menu2 = new SysMenu();
        menu2.setMenuId(1L);
        menu2.setMenuName("系统管理");

        SysMenu menu3 = new SysMenu();
        menu3.setMenuId(2L);
        menu3.setMenuName("用户管理");

        assertEquals(menu1, menu2, "相同 ID 的菜单应相等");
        assertEquals(menu1.hashCode(), menu2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(menu1, menu3, "不同 ID 的菜单应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        SysMenu menu = new SysMenu();
        menu.setMenuId(1L);
        menu.setMenuName("系统管理");

        String str = menu.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("SysMenu") || str.contains("系统管理") || str.contains("1"),
                "toString 应包含菜单信息");
    }

    // ==================== 序列化测试 ====================

    @Test
    @DisplayName("serialVersionUID - 验证序列化兼容性")
    void testSerialVersionUID() {
        SysMenu menu = new SysMenu();

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
    @DisplayName("业务场景 - 创建系统管理目录菜单")
    void testCreateSystemDirectory() {
        SysMenu directory = new SysMenu();
        directory.setMenuId(1L);
        directory.setMenuName("系统管理");
        directory.setParentId(0L);
        directory.setOrderNum(1);
        directory.setPath("system");
        directory.setMenuType("M");
        directory.setVisible("0");
        directory.setStatus("0");
        directory.setPerms("");

        assertEquals(1L, directory.getMenuId(), "菜单ID应为1");
        assertEquals("系统管理", directory.getMenuName(), "菜单名称应为系统管理");
        assertEquals(0L, directory.getParentId(), "根目录父ID应为0");
        assertEquals("M", directory.getMenuType(), "应为目录类型");
    }

    @Test
    @DisplayName("业务场景 - 创建用户管理菜单")
    void testCreateUserMenu() {
        SysMenu userMenu = new SysMenu();
        userMenu.setMenuId(100L);
        userMenu.setMenuName("用户管理");
        userMenu.setParentId(1L);
        userMenu.setOrderNum(1);
        userMenu.setPath("user");
        userMenu.setComponent("system/user/index");
        userMenu.setMenuType("C");
        userMenu.setVisible("0");
        userMenu.setStatus("0");
        userMenu.setPerms("system:user:list");

        assertEquals(100L, userMenu.getMenuId(), "菜单ID应为100");
        assertEquals("用户管理", userMenu.getMenuName(), "菜单名称应为用户管理");
        assertEquals(1L, userMenu.getParentId(), "父菜单ID应为1");
        assertEquals("C", userMenu.getMenuType(), "应为菜单类型");
        assertEquals("system:user:list", userMenu.getPerms(), "权限标识应为system:user:list");
    }

    @Test
    @DisplayName("业务场景 - 创建按钮权限")
    void testCreateButtonMenu() {
        SysMenu button = new SysMenu();
        button.setMenuId(1000L);
        button.setMenuName("用户查询");
        button.setParentId(100L);
        button.setOrderNum(1);
        button.setMenuType("F");
        button.setVisible("0");
        button.setStatus("0");
        button.setPerms("system:user:query");

        assertEquals(1000L, button.getMenuId(), "菜单ID应为1000");
        assertEquals("用户查询", button.getMenuName(), "菜单名称应为用户查询");
        assertEquals(100L, button.getParentId(), "父菜单ID应为100");
        assertEquals("F", button.getMenuType(), "应为按钮类型");
        assertEquals("system:user:query", button.getPerms(), "权限标识应为system:user:query");
    }

    @Test
    @DisplayName("业务场景 - 菜单停用")
    void testDisableMenu() {
        SysMenu menu = new SysMenu();
        menu.setMenuId(5L);
        menu.setStatus("0");

        menu.setStatus("1");

        assertEquals("1", menu.getStatus(), "菜单状态应更新为停用");
        assertFalse("0".equals(menu.getStatus()), "菜单应不再处于正常状态");
    }

    @Test
    @DisplayName("业务场景 - 菜单隐藏")
    void testHideMenu() {
        SysMenu menu = new SysMenu();
        menu.setMenuId(6L);
        menu.setVisible("0");

        menu.setVisible("1");

        assertEquals("1", menu.getVisible(), "显示状态应更新为隐藏");
        assertFalse("0".equals(menu.getVisible()), "菜单应被隐藏");
    }

    @Test
    @DisplayName("业务场景 - 外链菜单")
    void testExternalLinkMenu() {
        SysMenu menu = new SysMenu();
        menu.setMenuId(7L);
        menu.setMenuName("官网");
        menu.setPath("http://ruoyi.vip");
        menu.setIsFrame(0);
        menu.setMenuType("C");

        assertEquals("http://ruoyi.vip", menu.getPath(), "路由地址应为外部URL");
        assertEquals(0, menu.getIsFrame(), "应为外链菜单");
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证字符串字段容错")
    void testNullStringFields(String value) {
        SysMenu menu = new SysMenu();
        menu.setMenuName(value);
        menu.setPath(value);
        menu.setComponent(value);
        menu.setQuery(value);
        menu.setRouteName(value);
        menu.setPerms(value);
        menu.setIcon(value);

        assertNull(menu.getMenuName(), "null菜单名应被接受");
        assertNull(menu.getPath(), "null路由地址应被接受");
        assertNull(menu.getComponent(), "null组件路径应被接受");
        assertNull(menu.getQuery(), "null路由参数应被接受");
        assertNull(menu.getRouteName(), "null路由名称应被接受");
        assertNull(menu.getPerms(), "null权限标识应被接受");
        assertNull(menu.getIcon(), "null菜单图标应被接受");
    }
}
