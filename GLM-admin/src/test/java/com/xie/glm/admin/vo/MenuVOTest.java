package com.xie.glm.admin.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * MenuVO 单元测试
 *
 * <p>测试菜单视图对象的创建和字段设置，包括树形结构的子菜单列表
 *
 * @author xie
 */
@DisplayName("MenuVO 单元测试")
class MenuVOTest {

    @ParameterizedTest
    @MethodSource("provideMenuVOData")
    @DisplayName("测试 MenuVO 创建和字段设置")
    void testMenuVOCreation(Long menuId, String menuName, Long parentId, Integer orderNum,
                            String path, String component, String query, String routeName,
                            Integer isFrame, Integer isCache, String menuType,
                            String visible, String status, String perms, String icon,
                            LocalDateTime createTime, LocalDateTime updateTime, List<MenuVO> children) {
        // When: 创建 MenuVO 对象
        MenuVO menuVO = MenuVO.builder()
                .menuId(menuId)
                .menuName(menuName)
                .parentId(parentId)
                .orderNum(orderNum)
                .path(path)
                .component(component)
                .query(query)
                .routeName(routeName)
                .isFrame(isFrame)
                .isCache(isCache)
                .menuType(menuType)
                .visible(visible)
                .status(status)
                .perms(perms)
                .icon(icon)
                .createTime(createTime)
                .updateTime(updateTime)
                .children(children)
                .build();

        // Then: 验证所有字段设置正确
        assertAll("MenuVO 字段验证",
                () -> assertThat(menuVO.getMenuId()).isEqualTo(menuId),
                () -> assertThat(menuVO.getMenuName()).isEqualTo(menuName),
                () -> assertThat(menuVO.getParentId()).isEqualTo(parentId),
                () -> assertThat(menuVO.getOrderNum()).isEqualTo(orderNum),
                () -> assertThat(menuVO.getPath()).isEqualTo(path),
                () -> assertThat(menuVO.getComponent()).isEqualTo(component),
                () -> assertThat(menuVO.getQuery()).isEqualTo(query),
                () -> assertThat(menuVO.getRouteName()).isEqualTo(routeName),
                () -> assertThat(menuVO.getIsFrame()).isEqualTo(isFrame),
                () -> assertThat(menuVO.getIsCache()).isEqualTo(isCache),
                () -> assertThat(menuVO.getMenuType()).isEqualTo(menuType),
                () -> assertThat(menuVO.getVisible()).isEqualTo(visible),
                () -> assertThat(menuVO.getStatus()).isEqualTo(status),
                () -> assertThat(menuVO.getPerms()).isEqualTo(perms),
                () -> assertThat(menuVO.getIcon()).isEqualTo(icon),
                () -> assertThat(menuVO.getCreateTime()).isEqualTo(createTime),
                () -> assertThat(menuVO.getUpdateTime()).isEqualTo(updateTime),
                () -> assertThat(menuVO.getChildren()).isEqualTo(children)
        );
    }

    @Test
    @DisplayName("测试 MenuVO 带空子菜单列表")
    void testMenuVOWithEmptyChildren() {
        // Given: 空子菜单列表
        List<MenuVO> children = List.of();

        // When: 创建 MenuVO
        MenuVO menuVO = MenuVO.builder()
                .menuId(1L)
                .menuName("系统管理")
                .children(children)
                .build();

        // Then: 验证 children 为空列表
        assertThat(menuVO.getChildren()).isEmpty();
    }

    @Test
    @DisplayName("测试 MenuVO 带子菜单列表")
    void testMenuVOWithChildren() {
        // Given: 子菜单列表
        List<MenuVO> children = List.of(
                MenuVO.builder().menuId(2L).menuName("用户管理").build(),
                MenuVO.builder().menuId(3L).menuName("角色管理").build(),
                MenuVO.builder().menuId(4L).menuName("菜单管理").build()
        );

        // When: 创建父菜单
        MenuVO menuVO = MenuVO.builder()
                .menuId(1L)
                .menuName("系统管理")
                .children(children)
                .build();

        // Then: 验证子菜单数量和名称
        assertAll("子菜单验证",
                () -> assertThat(menuVO.getChildren()).hasSize(3),
                () -> assertThat(menuVO.getChildren().get(0).getMenuName()).isEqualTo("用户管理"),
                () -> assertThat(menuVO.getChildren().get(1).getMenuName()).isEqualTo("角色管理"),
                () -> assertThat(menuVO.getChildren().get(2).getMenuName()).isEqualTo("菜单管理")
        );
    }

    @Test
    @DisplayName("测试 MenuVO 树形结构（多级菜单）")
    void testMenuVOTreeStructure() {
        // Given: 三级菜单结构
        MenuVO level3Menu1 = MenuVO.builder()
                .menuId(4L)
                .menuName("用户查询")
                .menuType("F")
                .build();

        MenuVO level3Menu2 = MenuVO.builder()
                .menuId(5L)
                .menuName("用户新增")
                .menuType("F")
                .build();

        MenuVO level2Menu1 = MenuVO.builder()
                .menuId(2L)
                .menuName("用户管理")
                .menuType("C")
                .children(List.of(level3Menu1, level3Menu2))
                .build();

        MenuVO level2Menu2 = MenuVO.builder()
                .menuId(3L)
                .menuName("角色管理")
                .menuType("C")
                .children(new ArrayList<>())
                .build();

        MenuVO level1Menu = MenuVO.builder()
                .menuId(1L)
                .menuName("系统管理")
                .menuType("M")
                .children(List.of(level2Menu1, level2Menu2))
                .build();

        // When & Then: 验证树形结构
        assertAll("树形结构验证",
                // 一级菜单
                () -> assertThat(level1Menu.getMenuId()).isEqualTo(1L),
                () -> assertThat(level1Menu.getMenuName()).isEqualTo("系统管理"),
                () -> assertThat(level1Menu.getMenuType()).isEqualTo("M"),
                () -> assertThat(level1Menu.getChildren()).hasSize(2),

                // 二级菜单 - 用户管理
                () -> assertThat(level2Menu1.getMenuId()).isEqualTo(2L),
                () -> assertThat(level2Menu1.getMenuName()).isEqualTo("用户管理"),
                () -> assertThat(level2Menu1.getMenuType()).isEqualTo("C"),
                () -> assertThat(level2Menu1.getChildren()).hasSize(2),

                // 二级菜单 - 角色管理
                () -> assertThat(level2Menu2.getMenuId()).isEqualTo(3L),
                () -> assertThat(level2Menu2.getMenuName()).isEqualTo("角色管理"),
                () -> assertThat(level2Menu2.getMenuType()).isEqualTo("C"),
                () -> assertThat(level2Menu2.getChildren()).isEmpty(),

                // 三级菜单
                () -> assertThat(level3Menu1.getMenuId()).isEqualTo(4L),
                () -> assertThat(level3Menu1.getMenuName()).isEqualTo("用户查询"),
                () -> assertThat(level3Menu1.getMenuType()).isEqualTo("F"),
                () -> assertThat(level3Menu2.getMenuId()).isEqualTo(5L),
                () -> assertThat(level3Menu2.getMenuName()).isEqualTo("用户新增")
        );
    }

    @Test
    @DisplayName("测试 MenuVO 菜单类型枚举值")
    void testMenuTypeEnum() {
        // Given & When & Then: 测试不同菜单类型
        assertAll("菜单类型枚举验证",
                () -> {
                    MenuVO menuM = MenuVO.builder()
                            .menuId(1L)
                            .menuName("系统管理")
                            .menuType("M")  // 目录
                            .build();
                    assertThat(menuM.getMenuType()).isEqualTo("M");
                },
                () -> {
                    MenuVO menuC = MenuVO.builder()
                            .menuId(2L)
                            .menuName("用户管理")
                            .menuType("C")  // 菜单
                            .build();
                    assertThat(menuC.getMenuType()).isEqualTo("C");
                },
                () -> {
                    MenuVO menuF = MenuVO.builder()
                            .menuId(3L)
                            .menuName("用户查询")
                            .menuType("F")  // 按钮
                            .build();
                    assertThat(menuF.getMenuType()).isEqualTo("F");
                }
        );
    }

    @Test
    @DisplayName("测试 MenuVO 状态字段")
    void testMenuStatusFields() {
        // Given & When & Then: 测试状态相关字段
        assertAll("状态字段验证",
                () -> {
                    MenuVO vo1 = MenuVO.builder()
                            .menuId(1L)
                            .visible("0")  // 显示
                            .status("0")   // 正常
                            .build();
                    assertAll("显示状态验证",
                            () -> assertThat(vo1.getVisible()).isEqualTo("0"),
                            () -> assertThat(vo1.getStatus()).isEqualTo("0")
                    );
                },
                () -> {
                    MenuVO vo2 = MenuVO.builder()
                            .menuId(2L)
                            .visible("1")  // 隐藏
                            .status("1")   // 停用
                            .build();
                    assertAll("隐藏状态验证",
                            () -> assertThat(vo2.getVisible()).isEqualTo("1"),
                            () -> assertThat(vo2.getStatus()).isEqualTo("1")
                    );
                }
        );
    }

    @Test
    @DisplayName("测试 MenuVO 布尔字段")
    void testMenuBooleanFields() {
        // Given & When & Then: 测试布尔相关字段
        assertAll("布尔字段验证",
                () -> {
                    MenuVO vo1 = MenuVO.builder()
                            .menuId(1L)
                            .isFrame(0)  // 不是外链
                            .isCache(0)  // 缓存
                            .build();
                    assertAll("非外链缓存验证",
                            () -> assertThat(vo1.getIsFrame()).isEqualTo(0),
                            () -> assertThat(vo1.getIsCache()).isEqualTo(0)
                    );
                },
                () -> {
                    MenuVO vo2 = MenuVO.builder()
                            .menuId(2L)
                            .isFrame(1)  // 是外链
                            .isCache(1)  // 不缓存
                            .build();
                    assertAll("外链不缓存验证",
                            () -> assertThat(vo2.getIsFrame()).isEqualTo(1),
                            () -> assertThat(vo2.getIsCache()).isEqualTo(1)
                    );
                }
        );
    }

    @Test
    @DisplayName("测试 MenuVO 路由相关字段")
    void testMenuRouteFields() {
        // Given: 路由相关字段
        // When: 创建 MenuVO
        MenuVO menuVO = MenuVO.builder()
                .menuId(1L)
                .menuName("用户管理")
                .path("/system/user")
                .component("system/user/index")
                .query("{}")
                .routeName("UserManage")
                .build();

        // Then: 验证路由字段
        assertAll("路由字段验证",
                () -> assertThat(menuVO.getPath()).isEqualTo("/system/user"),
                () -> assertThat(menuVO.getComponent()).isEqualTo("system/user/index"),
                () -> assertThat(menuVO.getQuery()).isEqualTo("{}"),
                () -> assertThat(menuVO.getRouteName()).isEqualTo("UserManage")
        );
    }

    @Test
    @DisplayName("测试 MenuVO null children")
    void testMenuVOWithNullChildren() {
        // Given: children 为 null
        List<MenuVO> children = null;

        // When: 创建 MenuVO
        MenuVO menuVO = MenuVO.builder()
                .menuId(1L)
                .menuName("系统管理")
                .children(children)
                .build();

        // Then: 验证 children 为 null
        assertThat(menuVO.getChildren()).isNull();
    }

    /**
     * 提供 MenuVO 测试数据
     */
    private static Stream<Arguments> provideMenuVOData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(
                        1L, "系统管理", 0L, 1,
                        "/system", null, null, "System",
                        0, 0, "M",
                        "0", "0", null, "system",
                        now, now, null
                ),
                Arguments.of(
                        2L, "用户管理", 1L, 1,
                        "/system/user", "system/user/index", null, "UserManage",
                        0, 0, "C",
                        "0", "0", "system:user:list", "user",
                        now, now, null
                ),
                Arguments.of(
                        3L, "用户查询", 2L, 1,
                        null, null, null, null,
                        null, null, "F",
                        "0", "0", "system:user:query", null,
                        null, null, null
                )
        );
    }
}
