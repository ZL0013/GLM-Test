package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.MenuFacade;
import com.xie.glm.admin.vo.MenuVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.dto.MenuDTO;
import com.xie.glm.system.dto.query.MenuQueryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 菜单控制器测试
 *
 * <p>采用 TDD 方式开发，使用 Mockito 进行单元测试。
 *
 * <p>测试原则：
 * <ul>
 *   <li>Red-Green-Refactor 循环</li>
 *   <li>使用 @ParameterizedTest 进行参数化测试</li>
 *   <li>使用嵌套测试类 (@Nested) 组织相关测试</li>
 * </ul>
 *
 * <p>注意：Controller 直接返回数据对象，ResponseAdvice 会自动包装为 Result 格式。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("菜单控制器测试")
class MenuControllerTest {

    @Mock
    private MenuFacade menuFacade;

    @InjectMocks
    private MenuController menuController;

    // ==================== 菜单查询接口测试 ====================

    @Nested
    @DisplayName("菜单查询接口测试")
    class QueryTests {

        @Test
        @DisplayName("分页查询菜单列表 - 成功")
        void testListMenus_Success() {
            // Given
            MenuQueryDTO query = new MenuQueryDTO();
            query.setMenuName("系统管理");

            PageResult<MenuVO> expectedPage = new PageResult<>(
                Arrays.asList(createMockMenuVO()),
                10L
            );
            when(menuFacade.listMenus(query)).thenReturn(expectedPage);

            // When
            PageResult<MenuVO> result = menuController.list(query);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotal()).isEqualTo(10L);
            assertThat(result.getRecords()).hasSize(1);

            verify(menuFacade).listMenus(query);
        }

        @Test
        @DisplayName("分页查询菜单列表 - 空结果")
        void testListMenus_EmptyResult() {
            // Given
            MenuQueryDTO query = new MenuQueryDTO();
            PageResult<MenuVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(menuFacade.listMenus(query)).thenReturn(expectedPage);

            // When
            PageResult<MenuVO> result = menuController.list(query);

            // Then
            assertThat(result.getRecords()).isEmpty();
            assertThat(result.getTotal()).isEqualTo(0L);
        }

        @ParameterizedTest
        @MethodSource("provideQueryConditions")
        @DisplayName("分页查询菜单列表 - 带条件查询")
        void testListMenus_WithConditions(String menuName, String menuType, String status) {
            // Given
            MenuQueryDTO query = new MenuQueryDTO();
            query.setMenuName(menuName);
            query.setMenuType(menuType);
            query.setStatus(status);

            PageResult<MenuVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(menuFacade.listMenus(query)).thenReturn(expectedPage);

            // When
            PageResult<MenuVO> result = menuController.list(query);

            // Then
            assertThat(result).isNotNull();
            verify(menuFacade).listMenus(query);
        }

        private static List<Arguments> provideQueryConditions() {
            return List.of(
                Arguments.of("系统管理", "M", "0"),
                Arguments.of("用户管理", "C", "0"),
                Arguments.of("", "F", "1")
            );
        }

        @Test
        @DisplayName("构建菜单树 - 成功")
        void testTree_Success() {
            // Given
            List<MenuVO> expectedTree = Arrays.asList(
                createMockMenuVOWithChildren(),
                createMockMenuVO()
            );
            when(menuFacade.buildMenuTree()).thenReturn(expectedTree);

            // When
            List<MenuVO> result = menuController.tree();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);

            verify(menuFacade).buildMenuTree();
        }

        @Test
        @DisplayName("查询所有菜单列表 - 成功")
        void testListAll_Success() {
            // Given
            List<MenuVO> expectedList = Arrays.asList(
                createMockMenuVO(),
                createMockMenuVO()
            );
            when(menuFacade.listAllMenus()).thenReturn(expectedList);

            // When
            List<MenuVO> result = menuController.listAll();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);

            verify(menuFacade).listAllMenus();
        }
    }

    // ==================== 菜单详情接口测试 ====================

    @Nested
    @DisplayName("菜单详情接口测试")
    class DetailTests {

        @Test
        @DisplayName("查询菜单详情 - 成功")
        void testGetDetail_Success() {
            // Given
            Long menuId = 1L;
            MenuVO expectedMenu = createMockMenuVO();
            when(menuFacade.getMenuById(menuId)).thenReturn(expectedMenu);

            // When
            MenuVO result = menuController.getDetail(menuId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMenuId()).isEqualTo(menuId);

            verify(menuFacade).getMenuById(menuId);
        }

        @Test
        @DisplayName("查询菜单详情 - 菜单不存在")
        void testGetDetail_MenuNotFound() {
            // Given
            Long menuId = 999L;
            when(menuFacade.getMenuById(menuId))
                .thenThrow(new ServiceException("菜单不存在"));

            // When & Then
            assertThatThrownBy(() -> menuController.getDetail(menuId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("菜单不存在");
        }
    }

    // ==================== 菜单创建接口测试 ====================

    @Nested
    @DisplayName("菜单创建接口测试")
    class CreateTests {

        @Test
        @DisplayName("创建菜单 - 成功")
        void testCreate_Success() {
            // Given
            MenuDTO dto = createMockMenuDTO();
            Long expectedMenuId = 1L;
            when(menuFacade.createMenu(dto)).thenReturn(expectedMenuId);

            // When
            Long result = menuController.create(dto);

            // Then
            assertThat(result).isEqualTo(expectedMenuId);

            verify(menuFacade).createMenu(dto);
        }

        @ParameterizedTest
        @CsvSource({
            "系统管理, M, 0",
            "用户管理, C, 1",
            "用户新增, F, 2"
        })
        @DisplayName("创建菜单 - 不同菜单类型")
        void testCreate_DifferentMenuTypes(String menuName, String menuType, Long parentId) {
            // Given
            MenuDTO dto = createMockMenuDTO();
            dto.setMenuName(menuName);
            dto.setMenuType(menuType);
            dto.setParentId(parentId);

            Long expectedMenuId = 1L;
            when(menuFacade.createMenu(dto)).thenReturn(expectedMenuId);

            // When
            Long result = menuController.create(dto);

            // Then
            assertThat(result).isEqualTo(expectedMenuId);

            verify(menuFacade).createMenu(dto);
        }

        @Test
        @DisplayName("创建菜单 - 菜单名称重复")
        void testCreate_MenuNameDuplicate() {
            // Given
            MenuDTO dto = createMockMenuDTO();
            when(menuFacade.createMenu(dto))
                .thenThrow(new ServiceException("菜单名称已存在"));

            // When & Then
            assertThatThrownBy(() -> menuController.create(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("菜单名称已存在");
        }
    }

    // ==================== 菜单更新接口测试 ====================

    @Nested
    @DisplayName("菜单更新接口测试")
    class UpdateTests {

        @Test
        @DisplayName("更新菜单 - 成功")
        void testUpdate_Success() {
            // Given
            Long menuId = 1L;
            MenuDTO dto = createMockMenuDTO();
            dto.setMenuId(menuId);

            // When
            menuController.update(dto);

            // Then
            verify(menuFacade).updateMenu(dto);
        }

        @Test
        @DisplayName("更新菜单 - 菜单不存在")
        void testUpdate_MenuNotFound() {
            // Given
            Long menuId = 999L;
            MenuDTO dto = createMockMenuDTO();
            dto.setMenuId(menuId);

            doThrow(new ServiceException("菜单不存在"))
                .when(menuFacade).updateMenu(any(MenuDTO.class));

            // When & Then
            assertThatThrownBy(() -> menuController.update(dto))
                .isInstanceOf(ServiceException.class);
        }
    }

    // ==================== 菜单删除接口测试 ====================

    @Nested
    @DisplayName("菜单删除接口测试")
    class DeleteTests {

        @ParameterizedTest
        @CsvSource({"1", "2", "100"})
        @DisplayName("删除菜单 - 成功")
        void testDelete_Success(Long menuId) {
            // Given

            // When
            menuController.delete(menuId);

            // Then
            verify(menuFacade).deleteMenu(menuId);
        }

        @Test
        @DisplayName("删除菜单 - 菜单不存在")
        void testDelete_MenuNotFound() {
            // Given
            Long menuId = 999L;
            doThrow(new ServiceException("菜单不存在"))
                .when(menuFacade).deleteMenu(menuId);

            // When & Then
            assertThatThrownBy(() -> menuController.delete(menuId))
                .isInstanceOf(ServiceException.class);
        }

        @Test
        @DisplayName("删除菜单 - 存在子菜单")
        void testDelete_HasChildren() {
            // Given
            Long menuId = 1L;
            doThrow(new ServiceException("菜单存在子菜单，不允许删除"))
                .when(menuFacade).deleteMenu(menuId);

            // When & Then
            assertThatThrownBy(() -> menuController.delete(menuId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("存在子菜单");
        }
    }

    // ==================== 菜单名称唯一性校验接口测试 ====================

    @Nested
    @DisplayName("菜单名称唯一性校验接口测试")
    class UniqueTests {

        @ParameterizedTest
        @CsvSource({
            "用户管理, 0, true",
            "角色管理, 1, true",
            "系统管理, 0, false"
        })
        @DisplayName("检查菜单名称唯一性")
        void testCheckMenuNameUnique(String menuName, Long parentId, boolean expected) {
            // Given
            when(menuFacade.checkMenuNameUnique(menuName, parentId)).thenReturn(expected);

            // When
            boolean result = menuController.checkMenuNameUnique(menuName, parentId);

            // Then
            assertThat(result).isEqualTo(expected);

            verify(menuFacade).checkMenuNameUnique(menuName, parentId);
        }
    }

    // ==================== Mock 辅助方法 ====================

    private MenuVO createMockMenuVO() {
        return MenuVO.builder()
            .menuId(1L)
            .menuName("系统管理")
            .parentId(0L)
            .orderNum(1)
            .path("/system")
            .component(null)
            .query(null)
            .routeName(null)
            .isFrame(0)
            .isCache(0)
            .menuType("M")
            .visible("0")
            .status("0")
            .perms(null)
            .icon("system")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
    }

    private MenuVO createMockMenuVOWithChildren() {
        MenuVO parent = createMockMenuVO();
        parent.setMenuId(1L);
        parent.setMenuName("系统管理");

        MenuVO child1 = MenuVO.builder()
            .menuId(2L)
            .menuName("用户管理")
            .parentId(1L)
            .orderNum(1)
            .path("/system/user")
            .component("system/user/index")
            .query(null)
            .routeName("User")
            .isFrame(0)
            .isCache(0)
            .menuType("C")
            .visible("0")
            .status("0")
            .perms("system:user:list")
            .icon("user")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();

        MenuVO child2 = MenuVO.builder()
            .menuId(3L)
            .menuName("角色管理")
            .parentId(1L)
            .orderNum(2)
            .path("/system/role")
            .component("system/role/index")
            .query(null)
            .routeName("Role")
            .isFrame(0)
            .isCache(0)
            .menuType("C")
            .visible("0")
            .status("0")
            .perms("system:role:list")
            .icon("peoples")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();

        parent.setChildren(Arrays.asList(child1, child2));
        return parent;
    }

    private MenuDTO createMockMenuDTO() {
        MenuDTO dto = new MenuDTO();
        dto.setMenuName("测试菜单");
        dto.setParentId(0L);
        dto.setOrderNum(1);
        dto.setPath("/test");
        dto.setComponent("test/index");
        dto.setMenuType("C");
        dto.setVisible("0");
        dto.setStatus("0");
        dto.setPerms("test:list");
        dto.setIcon("test");
        return dto;
    }
}
