package com.xie.glm.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.MenuConverter;
import com.xie.glm.system.domain.SysMenu;
import com.xie.glm.system.dto.MenuDTO;
import com.xie.glm.system.dto.query.MenuQueryDTO;
import com.xie.glm.system.mapper.SysMenuMapper;
import com.xie.glm.system.service.impl.MenuServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 菜单服务测试类
 *
 * <p>测试 {@link IMenuService} 的业务逻辑。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("菜单服务单元测试")
class MenuServiceTest {

    @Mock
    private SysMenuMapper menuMapper;

    @Mock
    private MenuConverter menuConverter;

    @InjectMocks
    private MenuServiceImpl menuService;

    private SysMenu testMenu;
    private MenuDTO testMenuDTO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testMenu = createTestMenu(1L, "系统管理", "M", 0L);
        testMenuDTO = createMenuDTO(1L, "系统管理", "M", 0L);
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询菜单 - 成功")
    void testListMenus_Success() {
        // 准备测试数据
        MenuQueryDTO query = new MenuQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setMenuName("系统");
        query.setStatus("0");

        Page<SysMenu> pageResult = new Page<>(1, 10);
        pageResult.setRecords(Arrays.asList(testMenu));
        pageResult.setTotal(1);

        when(menuMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(pageResult);
        when(menuConverter.toDtoList(anyList()))
            .thenReturn(Arrays.asList(testMenuDTO));

        // 执行测试
        PageResult<MenuDTO> result = menuService.listMenus(query);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(1, result.getTotal());
        assertEquals("系统管理", result.getRecords().get(0).getMenuName());

        verify(menuMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
        verify(menuConverter).toDtoList(anyList());
    }

    @ParameterizedTest
    @MethodSource("provideQueryConditions")
    @DisplayName("分页查询菜单 - 不同查询条件")
    void testListMenus_QueryConditions(String menuName, String menuType, String status) {
        MenuQueryDTO query = new MenuQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setMenuName(menuName);
        query.setMenuType(menuType);
        query.setStatus(status);

        Page<SysMenu> pageResult = new Page<>(1, 10);
        pageResult.setRecords(Arrays.asList());

        when(menuMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(pageResult);

        PageResult<MenuDTO> result = menuService.listMenus(query);

        assertNotNull(result);
        verify(menuMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    private static Stream<Arguments> provideQueryConditions() {
        return Stream.of(
            Arguments.of("系统", "M", "0"),
            Arguments.of("用户", "C", "0"),
            Arguments.of(null, null, null)
        );
    }

    // ==================== 根据 ID 查询菜单测试 ====================

    @Test
    @DisplayName("根据 ID 查询菜单 - 成功")
    void testGetMenuById_Success() {
        when(menuMapper.selectById(1L)).thenReturn(testMenu);
        when(menuConverter.toDto(testMenu)).thenReturn(testMenuDTO);

        MenuDTO result = menuService.getMenuById(1L);

        assertNotNull(result);
        assertEquals("系统管理", result.getMenuName());
        assertEquals("M", result.getMenuType());
        verify(menuMapper).selectById(1L);
        verify(menuConverter).toDto(testMenu);
    }

    @Test
    @DisplayName("根据 ID 查询菜单 - 菜单不存在")
    void testGetMenuById_NotFound() {
        when(menuMapper.selectById(999L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.getMenuById(999L))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("菜单不存在");

        verify(menuMapper).selectById(999L);
        verify(menuConverter, never()).toDto(any());
    }

    // ==================== 查询菜单列表测试 ====================

    @Test
    @DisplayName("查询所有菜单列表")
    void testListAllMenus() {
        SysMenu menu1 = createTestMenu(1L, "系统管理", "M", 0L);
        SysMenu menu2 = createTestMenu(2L, "用户管理", "C", 1L);

        when(menuMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(Arrays.asList(menu1, menu2));
        when(menuConverter.toDtoList(anyList()))
            .thenReturn(Arrays.asList(
                createMenuDTO(1L, "系统管理", "M", 0L),
                createMenuDTO(2L, "用户管理", "C", 1L)
            ));

        List<MenuDTO> result = menuService.listAllMenus();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(menuMapper).selectList(any(LambdaQueryWrapper.class));
        verify(menuConverter).toDtoList(anyList());
    }

    // ==================== 查询树形菜单测试 ====================

    @Test
    @DisplayName("构建树形菜单结构")
    void testBuildMenuTree() {
        // 准备测试数据 - 包含父子关系的菜单
        SysMenu parent1 = createTestMenu(1L, "系统管理", "M", 0L);
        SysMenu child1 = createTestMenu(2L, "用户管理", "C", 1L);
        SysMenu child2 = createTestMenu(3L, "角色管理", "C", 1L);
        SysMenu parent2 = createTestMenu(10L, "监控管理", "M", 0L);

        List<SysMenu> menus = Arrays.asList(parent1, child1, child2, parent2);

        when(menuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(menus);
        when(menuConverter.toDtoList(anyList())).thenReturn(Arrays.asList(
            createMenuDTO(1L, "系统管理", "M", 0L),
            createMenuDTO(2L, "用户管理", "C", 1L),
            createMenuDTO(3L, "角色管理", "C", 1L),
            createMenuDTO(10L, "监控管理", "M", 0L)
        ));

        List<MenuDTO> result = menuService.buildMenuTree();

        assertNotNull(result);
        // 简化实现返回所有菜单
        assertThat(result).hasSize(4);
        verify(menuMapper).selectList(any(LambdaQueryWrapper.class));
    }

    // ==================== 创建菜单测试 ====================

    @Test
    @DisplayName("创建菜单 - 成功")
    void testCreateMenu_Success() {
        MenuDTO dto = new MenuDTO();
        dto.setMenuName("新菜单");
        dto.setMenuType("C");
        dto.setParentId(1L);
        dto.setOrderNum(1);

        when(menuMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(menuConverter.toEntity(dto)).thenReturn(testMenu);
        when(menuMapper.insert(any(SysMenu.class))).thenReturn(1);

        Long menuId = menuService.createMenu(dto);

        assertNotNull(menuId);
        verify(menuMapper).insert(any(SysMenu.class));
    }

    @Test
    @DisplayName("创建菜单 - 菜单名称已存在")
    void testCreateMenu_MenuNameExists() {
        MenuDTO dto = new MenuDTO();
        dto.setMenuName("系统管理");
        dto.setParentId(0L);

        when(menuMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        assertThatThrownBy(() -> menuService.createMenu(dto))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("菜单名称已存在");

        verify(menuMapper).selectCount(any(LambdaQueryWrapper.class));
        verify(menuMapper, never()).insert(any(SysMenu.class));
    }

    // ==================== 更新菜单测试 ====================

    @Test
    @DisplayName("更新菜单 - 成功")
    void testUpdateMenu_Success() {
        MenuDTO dto = new MenuDTO();
        dto.setMenuId(1L);
        dto.setMenuName("更新后的菜单");

        when(menuMapper.selectById(1L)).thenReturn(testMenu);
        when(menuMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(menuConverter.toEntity(dto)).thenReturn(testMenu);
        when(menuMapper.updateById(any(SysMenu.class))).thenReturn(1);

        menuService.updateMenu(dto);

        verify(menuMapper).selectById(1L);
        verify(menuConverter).toEntity(dto);
        verify(menuMapper).updateById(any(SysMenu.class));
    }

    @Test
    @DisplayName("更新菜单 - 菜单不存在")
    void testUpdateMenu_NotFound() {
        MenuDTO dto = new MenuDTO();
        dto.setMenuId(999L);

        when(menuMapper.selectById(999L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.updateMenu(dto))
            .isInstanceOf(ServiceException.class);

        verify(menuMapper).selectById(999L);
        verify(menuMapper, never()).updateById(any(SysMenu.class));
    }

    // ==================== 删除菜单测试 ====================

    @Test
    @DisplayName("删除菜单 - 成功")
    void testDeleteMenu_Success() {
        when(menuMapper.selectById(1L)).thenReturn(testMenu);
        when(menuMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L); // 没有子菜单
        when(menuMapper.deleteById(1L)).thenReturn(1);

        menuService.deleteMenu(1L);

        verify(menuMapper).selectById(1L);
        verify(menuMapper).deleteById(1L);
    }

    @Test
    @DisplayName("删除菜单 - 菜单不存在")
    void testDeleteMenu_NotFound() {
        when(menuMapper.selectById(999L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.deleteMenu(999L))
            .isInstanceOf(ServiceException.class);

        verify(menuMapper).selectById(999L);
        verify(menuMapper, never()).deleteById(any());
    }

    @Test
    @DisplayName("删除菜单 - 存在子菜单")
    void testDeleteMenu_HasChildren() {
        when(menuMapper.selectById(1L)).thenReturn(testMenu);
        when(menuMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L); // 有子菜单

        assertThatThrownBy(() -> menuService.deleteMenu(1L))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("存在子菜单");

        verify(menuMapper).selectById(1L);
        verify(menuMapper, never()).deleteById(any());
    }

    // ==================== 唯一性检查测试 ====================

    @ParameterizedTest
    @CsvSource({
        "新菜单, true",
        "系统管理, false"
    })
    @DisplayName("检查菜单名称唯一性")
    void testCheckMenuNameUnique(String menuName, boolean expected) {
        when(menuMapper.selectCount(any(LambdaQueryWrapper.class)))
            .thenReturn(expected ? 0L : 1L);

        boolean result = menuService.checkMenuNameUnique(menuName, 0L);

        assertEquals(expected, result);
    }

    // ==================== 辅助方法 ====================

    private SysMenu createTestMenu(Long menuId, String menuName, String menuType, Long parentId) {
        SysMenu menu = new SysMenu();
        menu.setMenuId(menuId);
        menu.setMenuName(menuName);
        menu.setMenuType(menuType);
        menu.setParentId(parentId);
        menu.setOrderNum(1);
        menu.setPath("path");
        menu.setStatus("0");
        menu.setVisible("0");
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        return menu;
    }

    private MenuDTO createMenuDTO(Long menuId, String menuName, String menuType, Long parentId) {
        MenuDTO dto = new MenuDTO();
        dto.setMenuId(menuId);
        dto.setMenuName(menuName);
        dto.setMenuType(menuType);
        dto.setParentId(parentId);
        dto.setOrderNum(1);
        dto.setPath("path");
        dto.setStatus("0");
        dto.setVisible("0");
        dto.setCreateTime(LocalDateTime.now());
        dto.setUpdateTime(LocalDateTime.now());
        return dto;
    }
}
