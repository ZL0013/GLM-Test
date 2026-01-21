package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.MenuVO;
import com.xie.glm.system.dto.MenuDTO;
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
 * MenuVoConverter 单元测试
 *
 * <p>测试 MenuDTO 到 MenuVO 的转换
 *
 * @author xie
 */
@DisplayName("MenuVoConverter 单元测试")
class MenuVoConverterTest {

    /**
     * 获取 MenuVoConverter 实例
     * MapStruct 会在 target/generated-sources/annotations 下生成实现类
     */
    private MenuVoConverter getMenuVoConverter() {
        return new MenuVoConverterImpl();
    }

    @ParameterizedTest
    @MethodSource("provideMenuDTOData")
    @DisplayName("测试 DTO 转 VO")
    void testToVo(Long menuId, String menuName, Long parentId, Integer orderNum,
                  String path, String component, String query, String routeName,
                  Integer isFrame, Integer isCache, String menuType,
                  String visible, String status, String perms, String icon) {
        // Given: 准备 MenuDTO 数据
        MenuDTO menuDTO = MenuDTO.builder()
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
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // When: 执行转换
        MenuVO menuVO = getMenuVoConverter().toVo(menuDTO);

        // Then: 验证基本字段转换正确
        assertAll("MenuVO 基本字段验证",
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
                () -> assertThat(menuVO.getIcon()).isEqualTo(icon)
        );
    }

    @Test
    @DisplayName("测试 DTO 列表转 VO 列表")
    void testToVoList() {
        // Given: 准备 MenuDTO 列表
        List<MenuDTO> menuDTOs = List.of(
                MenuDTO.builder()
                        .menuId(1L)
                        .menuName("系统管理")
                        .menuType("M")
                        .build(),
                MenuDTO.builder()
                        .menuId(2L)
                        .menuName("用户管理")
                        .menuType("C")
                        .build(),
                MenuDTO.builder()
                        .menuId(3L)
                        .menuName("角色管理")
                        .menuType("C")
                        .build()
        );

        // When: 执行转换
        List<MenuVO> menuVOs = getMenuVoConverter().toVoList(menuDTOs);

        // Then: 验证转换结果
        assertAll("VO 列表验证",
                () -> assertThat(menuVOs).hasSize(3),
                () -> assertThat(menuVOs.get(0).getMenuId()).isEqualTo(1L),
                () -> assertThat(menuVOs.get(0).getMenuName()).isEqualTo("系统管理"),
                () -> assertThat(menuVOs.get(1).getMenuId()).isEqualTo(2L),
                () -> assertThat(menuVOs.get(1).getMenuName()).isEqualTo("用户管理"),
                () -> assertThat(menuVOs.get(2).getMenuId()).isEqualTo(3L),
                () -> assertThat(menuVOs.get(2).getMenuName()).isEqualTo("角色管理")
        );
    }

    @Test
    @DisplayName("测试 null 转换")
    void testNullConversion() {
        // Given: null DTO
        MenuDTO menuDTO = null;

        // When: 执行转换
        MenuVO menuVO = getMenuVoConverter().toVo(menuDTO);

        // Then: 验证返回 null
        assertThat(menuVO).isNull();
    }

    @Test
    @DisplayName("测试 null 列表转换")
    void testNullListConversion() {
        // Given: null DTO 列表
        List<MenuDTO> menuDTOs = null;

        // When: 执行转换
        List<MenuVO> menuVOs = getMenuVoConverter().toVoList(menuDTOs);

        // Then: 验证返回 null
        assertThat(menuVOs).isNull();
    }

    @Test
    @DisplayName("测试空列表转换")
    void testEmptyListConversion() {
        // Given: 空 DTO 列表
        List<MenuDTO> menuDTOs = List.of();

        // When: 执行转换
        List<MenuVO> menuVOs = getMenuVoConverter().toVoList(menuDTOs);

        // Then: 验证返回空列表
        assertThat(menuVOs).isEmpty();
    }

    @Test
    @DisplayName("测试 children 字段初始为 null")
    void testChildrenFieldInitialization() {
        // Given: MenuDTO
        MenuDTO menuDTO = MenuDTO.builder()
                .menuId(1L)
                .menuName("系统管理")
                .build();

        // When: 执行转换
        MenuVO menuVO = getMenuVoConverter().toVo(menuDTO);

        // Then: 验证 children 初始为 null（需要手动构建树形结构）
        assertThat(menuVO.getChildren()).isNull();
    }

    @Test
    @DisplayName("测试不同菜单类型转换")
    void testMenuTypeConversion() {
        // Given & When & Then: 测试不同菜单类型
        assertAll("菜单类型转换验证",
                () -> {
                    MenuDTO dtoM = MenuDTO.builder()
                            .menuId(1L)
                            .menuName("系统管理")
                            .menuType("M")
                            .build();
                    MenuVO voM = getMenuVoConverter().toVo(dtoM);
                    assertThat(voM.getMenuType()).isEqualTo("M");
                },
                () -> {
                    MenuDTO dtoC = MenuDTO.builder()
                            .menuId(2L)
                            .menuName("用户管理")
                            .menuType("C")
                            .build();
                    MenuVO voC = getMenuVoConverter().toVo(dtoC);
                    assertThat(voC.getMenuType()).isEqualTo("C");
                },
                () -> {
                    MenuDTO dtoF = MenuDTO.builder()
                            .menuId(3L)
                            .menuName("用户查询")
                            .menuType("F")
                            .build();
                    MenuVO voF = getMenuVoConverter().toVo(dtoF);
                    assertThat(voF.getMenuType()).isEqualTo("F");
                }
        );
    }

    @Test
    @DisplayName("测试菜单层级结构转换（parentId）")
    void testMenuHierarchyConversion() {
        // Given: 三级菜单结构
        MenuDTO parentMenu = MenuDTO.builder()
                .menuId(1L)
                .menuName("系统管理")
                .parentId(0L)
                .menuType("M")
                .build();

        MenuDTO childMenu = MenuDTO.builder()
                .menuId(2L)
                .menuName("用户管理")
                .parentId(1L)
                .menuType("C")
                .build();

        MenuDTO grandchildMenu = MenuDTO.builder()
                .menuId(3L)
                .menuName("用户查询")
                .parentId(2L)
                .menuType("F")
                .build();

        // When: 执行转换
        MenuVO parentVO = getMenuVoConverter().toVo(parentMenu);
        MenuVO childVO = getMenuVoConverter().toVo(childMenu);
        MenuVO grandchildVO = getMenuVoConverter().toVo(grandchildMenu);

        // Then: 验证层级关系正确转换
        assertAll("层级关系验证",
                () -> assertThat(parentVO.getParentId()).isEqualTo(0L),
                () -> assertThat(childVO.getParentId()).isEqualTo(1L),
                () -> assertThat(grandchildVO.getParentId()).isEqualTo(2L)
        );
    }

    @Test
    @DisplayName("测试路由字段转换")
    void testRouteFieldsConversion() {
        // Given: 包含路由字段的 MenuDTO
        MenuDTO menuDTO = MenuDTO.builder()
                .menuId(1L)
                .menuName("用户管理")
                .path("/system/user")
                .component("system/user/index")
                .query("{}")
                .routeName("UserManage")
                .build();

        // When: 执行转换
        MenuVO menuVO = getMenuVoConverter().toVo(menuDTO);

        // Then: 验证路由字段正确转换
        assertAll("路由字段验证",
                () -> assertThat(menuVO.getPath()).isEqualTo("/system/user"),
                () -> assertThat(menuVO.getComponent()).isEqualTo("system/user/index"),
                () -> assertThat(menuVO.getQuery()).isEqualTo("{}"),
                () -> assertThat(menuVO.getRouteName()).isEqualTo("UserManage")
        );
    }

    @Test
    @DisplayName("测试状态字段转换")
    void testStatusFieldsConversion() {
        // Given: 包含状态字段的 MenuDTO
        MenuDTO menuDTO = MenuDTO.builder()
                .menuId(1L)
                .menuName("用户管理")
                .visible("0")
                .status("0")
                .isFrame(0)
                .isCache(0)
                .build();

        // When: 执行转换
        MenuVO menuVO = getMenuVoConverter().toVo(menuDTO);

        // Then: 验证状态字段正确转换
        assertAll("状态字段验证",
                () -> assertThat(menuVO.getVisible()).isEqualTo("0"),
                () -> assertThat(menuVO.getStatus()).isEqualTo("0"),
                () -> assertThat(menuVO.getIsFrame()).isEqualTo(0),
                () -> assertThat(menuVO.getIsCache()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("测试权限标识转换")
    void testPermissionsConversion() {
        // Given: 包含权限标识的 MenuDTO
        MenuDTO menuDTO = MenuDTO.builder()
                .menuId(1L)
                .menuName("用户查询")
                .perms("system:user:query")
                .build();

        // When: 执行转换
        MenuVO menuVO = getMenuVoConverter().toVo(menuDTO);

        // Then: 验证权限标识正确转换
        assertThat(menuVO.getPerms()).isEqualTo("system:user:query");
    }

    @Test
    @DisplayName("测试显示顺序转换")
    void testOrderNumConversion() {
        // Given: 不同显示顺序的 MenuDTO 列表
        List<MenuDTO> menuDTOs = List.of(
                MenuDTO.builder().menuId(1L).menuName("菜单3").orderNum(3).build(),
                MenuDTO.builder().menuId(2L).menuName("菜单1").orderNum(1).build(),
                MenuDTO.builder().menuId(3L).menuName("菜单2").orderNum(2).build()
        );

        // When: 执行转换
        List<MenuVO> menuVOs = getMenuVoConverter().toVoList(menuDTOs);

        // Then: 验证显示顺序保持一致（排序由业务层负责）
        assertAll("显示顺序验证",
                () -> assertThat(menuVOs.get(0).getOrderNum()).isEqualTo(3),
                () -> assertThat(menuVOs.get(1).getOrderNum()).isEqualTo(1),
                () -> assertThat(menuVOs.get(2).getOrderNum()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("测试图标字段转换")
    void testIconConversion() {
        // Given: 包含图标的 MenuDTO
        MenuDTO menuDTO = MenuDTO.builder()
                .menuId(1L)
                .menuName("用户管理")
                .icon("user")
                .build();

        // When: 执行转换
        MenuVO menuVO = getMenuVoConverter().toVo(menuDTO);

        // Then: 验证图标正确转换
        assertThat(menuVO.getIcon()).isEqualTo("user");
    }

    /**
     * 提供 MenuDTO 测试数据
     */
    private static Stream<Arguments> provideMenuDTOData() {
        return Stream.of(
                Arguments.of(
                        1L, "系统管理", 0L, 1,
                        "/system", null, null, "System",
                        0, 0, "M",
                        "0", "0", null, "system"
                ),
                Arguments.of(
                        2L, "用户管理", 1L, 1,
                        "/system/user", "system/user/index", null, "UserManage",
                        0, 0, "C",
                        "0", "0", "system:user:list", "user"
                ),
                Arguments.of(
                        3L, "用户查询", 2L, 1,
                        null, null, null, null,
                        null, null, "F",
                        "0", "0", "system:user:query", null
                )
        );
    }
}
