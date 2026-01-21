package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.MenuVoConverter;
import com.xie.glm.admin.vo.MenuVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.dto.MenuDTO;
import com.xie.glm.system.dto.query.MenuQueryDTO;
import com.xie.glm.system.service.IMenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * MenuFacade 菜单门面测试类
 *
 * <p>测试菜单门面层，封装 Service 调用和 DTO → VO 转换。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MenuFacade 菜单门面单元测试")
class MenuFacadeTest {

    @Mock
    private IMenuService menuService;

    @Mock
    private MenuVoConverter voConverter;

    @InjectMocks
    private MenuFacade menuFacade;

    private MenuDTO testMenuDTO;
    private MenuVO testMenuVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testMenuDTO = createTestMenuDTO(1L, "系统管理", 0L, "M");
        testMenuVO = createTestMenuVO(1L, "系统管理", 0L, "M");
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询菜单列表 - 成功")
    void testListMenus_Success() {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setMenuName("系统");

        PageResult<MenuDTO> dtoPage = new PageResult<>(
            Arrays.asList(testMenuDTO),
            1L
        );

        when(menuService.listMenus(any(MenuQueryDTO.class)))
            .thenReturn(dtoPage);
        when(voConverter.toVoList(anyList()))
            .thenReturn(Arrays.asList(testMenuVO));

        // When
        PageResult<MenuVO> result = menuFacade.listMenus(query);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getRecords().get(0).getMenuName()).isEqualTo("系统管理");

        verify(menuService).listMenus(query);
        verify(voConverter).toVoList(Arrays.asList(testMenuDTO));
    }

    @ParameterizedTest
    @MethodSource("providePaginationParams")
    @DisplayName("分页查询菜单列表 - 参数化测试")
    void testListMenus_Parameterized(Integer pageNum, Integer pageSize) {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);

        PageResult<MenuDTO> dtoPage = new PageResult<>(Arrays.asList(), 0L);
        when(menuService.listMenus(any(MenuQueryDTO.class))).thenReturn(dtoPage);
        when(voConverter.toVoList(anyList())).thenReturn(Arrays.asList());

        // When
        PageResult<MenuVO> result = menuFacade.listMenus(query);

        // Then
        assertThat(result).isNotNull();
        verify(menuService).listMenus(query);
    }

    private static Stream<Arguments> providePaginationParams() {
        return Stream.of(
            Arguments.of(1, 10),
            Arguments.of(2, 20),
            Arguments.of(1, 50)
        );
    }

    // ==================== 查询所有菜单测试 ====================

    @Test
    @DisplayName("查询所有菜单列表 - 成功")
    void testListAllMenus_Success() {
        // Given
        List<MenuDTO> dtoList = Arrays.asList(
            testMenuDTO,
            createTestMenuDTO(2L, "用户管理", 1L, "C")
        );

        when(menuService.listAllMenus()).thenReturn(dtoList);
        when(voConverter.toVoList(dtoList)).thenReturn(Arrays.asList(
            testMenuVO,
            createTestMenuVO(2L, "用户管理", 1L, "C")
        ));

        // When
        List<MenuVO> result = menuFacade.listAllMenus();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        verify(menuService).listAllMenus();
        verify(voConverter).toVoList(dtoList);
    }

    // ==================== 构建菜单树测试 ====================

    @Test
    @DisplayName("构建菜单树 - 成功")
    void testBuildMenuTree_Success() {
        // Given - 构建层级菜单结构
        MenuDTO parentMenu = createTestMenuDTO(1L, "系统管理", 0L, "M");
        MenuDTO childMenu1 = createTestMenuDTO(2L, "用户管理", 1L, "C");
        MenuDTO childMenu2 = createTestMenuDTO(3L, "角色管理", 1L, "C");

        // Service 返回扁平列表
        List<MenuDTO> flatList = Arrays.asList(parentMenu, childMenu1, childMenu2);

        when(menuService.buildMenuTree()).thenReturn(flatList);

        // Mock 转换器返回对应的 VO
        MenuVO parentVO = createTestMenuVO(1L, "系统管理", 0L, "M");
        MenuVO childVO1 = createTestMenuVO(2L, "用户管理", 1L, "C");
        MenuVO childVO2 = createTestMenuVO(3L, "角色管理", 1L, "C");

        when(voConverter.toVoList(flatList)).thenReturn(Arrays.asList(parentVO, childVO1, childVO2));

        // When
        List<MenuVO> result = menuFacade.buildMenuTree();

        // Then - 返回树形结构，只有一个根节点
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMenuName()).isEqualTo("系统管理");
        // 子菜单应被正确设置
        assertThat(result.get(0).getChildren()).isNotNull();
        assertThat(result.get(0).getChildren()).hasSize(2);

        verify(menuService).buildMenuTree();
        verify(voConverter).toVoList(flatList);
    }

    @Test
    @DisplayName("构建菜单树 - 空列表")
    void testBuildMenuTree_Empty() {
        // Given
        List<MenuDTO> emptyList = Arrays.asList();
        when(menuService.buildMenuTree()).thenReturn(emptyList);
        when(voConverter.toVoList(emptyList)).thenReturn(Arrays.asList());

        // When
        List<MenuVO> result = menuFacade.buildMenuTree();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(menuService).buildMenuTree();
        verify(voConverter).toVoList(emptyList);
    }

    // ==================== 根据ID查询菜单测试 ====================

    @Test
    @DisplayName("根据ID查询菜单 - 成功")
    void testGetMenuById_Success() {
        // Given
        Long menuId = 1L;
        when(menuService.getMenuById(menuId)).thenReturn(testMenuDTO);
        when(voConverter.toVo(testMenuDTO)).thenReturn(testMenuVO);

        // When
        MenuVO result = menuFacade.getMenuById(menuId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMenuId()).isEqualTo(1L);
        assertThat(result.getMenuName()).isEqualTo("系统管理");

        verify(menuService).getMenuById(menuId);
        verify(voConverter).toVo(testMenuDTO);
    }

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "999, false"
    })
    @DisplayName("根据ID查询菜单 - 菜单不存在")
    void testGetMenuById_NotFound(Long menuId, boolean exists) {
        // Given
        if (exists) {
            when(menuService.getMenuById(menuId)).thenReturn(testMenuDTO);
            when(voConverter.toVo(testMenuDTO)).thenReturn(testMenuVO);
        } else {
            when(menuService.getMenuById(menuId))
                .thenThrow(new ServiceException("菜单不存在"));
        }

        // When & Then
        if (exists) {
            MenuVO result = menuFacade.getMenuById(menuId);
            assertThat(result).isNotNull();
        } else {
            assertThatThrownBy(() -> menuFacade.getMenuById(menuId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("菜单不存在");
        }
    }

    // ==================== 创建菜单测试 ====================

    @Test
    @DisplayName("创建菜单 - 成功")
    void testCreateMenu_Success() {
        // Given
        MenuDTO dto = createTestMenuDTO(null, "新菜单", 0L, "C");
        dto.setPath("/new");
        dto.setComponent("new/index");

        Long expectedMenuId = 100L;
        when(menuService.createMenu(dto)).thenReturn(expectedMenuId);

        // When
        menuFacade.createMenu(dto);

        // Then
        verify(menuService).createMenu(dto);
    }

    @Test
    @DisplayName("创建菜单 - 菜单名称已存在")
    void testCreateMenu_MenuNameExists() {
        // Given
        MenuDTO dto = createTestMenuDTO(null, "系统管理", 0L, "M");

        when(menuService.createMenu(dto))
            .thenThrow(new ServiceException("菜单名称已存在"));

        // When & Then
        assertThatThrownBy(() -> menuFacade.createMenu(dto))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("菜单名称已存在");

        verify(menuService).createMenu(dto);
    }

    // ==================== 更新菜单测试 ====================

    @Test
    @DisplayName("更新菜单 - 成功")
    void testUpdateMenu_Success() {
        // Given
        MenuDTO dto = createTestMenuDTO(1L, "更新后的菜单名", 0L, "M");
        dto.setPath("/updated");

        doNothing().when(menuService).updateMenu(any(MenuDTO.class));

        // When
        menuFacade.updateMenu(dto);

        // Then
        verify(menuService).updateMenu(dto);
    }

    @Test
    @DisplayName("更新菜单 - 菜单不存在")
    void testUpdateMenu_NotFound() {
        // Given
        MenuDTO dto = createTestMenuDTO(999L, "不存在的菜单", 0L, "C");

        doThrow(new ServiceException("菜单不存在"))
            .when(menuService).updateMenu(any(MenuDTO.class));

        // When & Then
        assertThatThrownBy(() -> menuFacade.updateMenu(dto))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("菜单不存在");

        verify(menuService).updateMenu(dto);
    }

    // ==================== 删除菜单测试 ====================

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 100L})
    @DisplayName("删除菜单 - 成功")
    void testDeleteMenu_Success(Long menuId) {
        // Given
        doNothing().when(menuService).deleteMenu(menuId);

        // When
        menuFacade.deleteMenu(menuId);

        // Then
        verify(menuService).deleteMenu(menuId);
    }

    @Test
    @DisplayName("删除菜单 - 存在子菜单")
    void testDeleteMenu_HasChildren() {
        // Given
        Long menuId = 1L;
        doThrow(new ServiceException("菜单存在子菜单，不允许删除"))
            .when(menuService).deleteMenu(menuId);

        // When & Then
        assertThatThrownBy(() -> menuFacade.deleteMenu(menuId))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("存在子菜单");

        verify(menuService).deleteMenu(menuId);
    }

    // ==================== 唯一性检查测试 ====================

    @ParameterizedTest
    @CsvSource({
            "新菜单, 0, true",
            "系统管理, 0, false",
            "用户管理, 1, true",
            "用户管理, 0, false"
    })
    @DisplayName("检查菜单名称唯一性")
    void testCheckMenuNameUnique(String menuName, Long parentId, boolean expected) {
        // Given
        when(menuService.checkMenuNameUnique(menuName, parentId)).thenReturn(expected);

        // When
        boolean result = menuFacade.checkMenuNameUnique(menuName, parentId);

        // Then
        assertThat(result).isEqualTo(expected);
        verify(menuService).checkMenuNameUnique(menuName, parentId);
    }

    // ==================== 辅助方法 ====================

    private MenuDTO createTestMenuDTO(Long menuId, String menuName, Long parentId, String menuType) {
        return MenuDTO.builder()
            .menuId(menuId)
            .menuName(menuName)
            .parentId(parentId)
            .orderNum(1)
            .path("/system")
            .component("system/index")
            .query(null)
            .routeName("System")
            .isFrame(0)
            .isCache(1)
            .menuType(menuType)
            .visible("0")
            .status("0")
            .perms("system:user:list")
            .icon("system")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
    }

    private MenuVO createTestMenuVO(Long menuId, String menuName, Long parentId, String menuType) {
        return MenuVO.builder()
            .menuId(menuId)
            .menuName(menuName)
            .parentId(parentId)
            .orderNum(1)
            .path("/system")
            .component("system/index")
            .query(null)
            .routeName("System")
            .isFrame(0)
            .isCache(1)
            .menuType(menuType)
            .visible("0")
            .status("0")
            .perms("system:user:list")
            .icon("system")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
    }
}
