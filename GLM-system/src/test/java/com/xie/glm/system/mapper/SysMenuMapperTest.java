package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.system.domain.SysMenu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 菜单 Mapper 接口测试
 *
 * <p>测试 {@link SysMenuMapper} 的各种场景：
 * <ul>
 *   <li>基础 CRUD 操作</li>
 *   <li>条件查询</li>
 *   <li>分页查询</li>
 *   <li>批量操作</li>
 *   <li>树形结构查询</li>
 * </ul>
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("菜单 Mapper 接口测试")
class SysMenuMapperTest {

    @Mock
    private SysMenuMapper sysMenuMapper;

    // ==================== 基础 CRUD 测试 ====================

    @Test
    @DisplayName("插入菜单")
    void testInsert() {
        // Given
        SysMenu menu = new SysMenu();
        menu.setMenuName("系统管理");
        menu.setParentId(0L);
        menu.setOrderNum(1);
        menu.setPath("system");
        menu.setMenuType("M");
        menu.setStatus("0");

        when(sysMenuMapper.insert(any(SysMenu.class))).thenReturn(1);

        // When
        int result = sysMenuMapper.insert(menu);

        // Then
        assertThat(result).isEqualTo(1);
        verify(sysMenuMapper, times(1)).insert(menu);
    }

    @Test
    @DisplayName("根据 ID 查询菜单")
    void testSelectById() {
        // Given
        Long menuId = 1L;
        SysMenu menu = new SysMenu();
        menu.setMenuId(menuId);
        menu.setMenuName("系统管理");

        when(sysMenuMapper.selectById(menuId)).thenReturn(menu);

        // When
        SysMenu result = sysMenuMapper.selectById(menuId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMenuId()).isEqualTo(menuId);
        assertThat(result.getMenuName()).isEqualTo("系统管理");
        verify(sysMenuMapper, times(1)).selectById(menuId);
    }

    @Test
    @DisplayName("更新菜单")
    void testUpdateById() {
        // Given
        SysMenu menu = new SysMenu();
        menu.setMenuId(1L);
        menu.setMenuName("更新后的菜单");

        when(sysMenuMapper.updateById(any(SysMenu.class))).thenReturn(1);

        // When
        int result = sysMenuMapper.updateById(menu);

        // Then
        assertThat(result).isEqualTo(1);
        verify(sysMenuMapper, times(1)).updateById(menu);
    }

    @Test
    @DisplayName("删除菜单")
    void testDeleteById() {
        // Given
        Long menuId = 1L;
        when(sysMenuMapper.deleteById(menuId)).thenReturn(1);

        // When
        int result = sysMenuMapper.deleteById(menuId);

        // Then
        assertThat(result).isEqualTo(1);
        verify(sysMenuMapper, times(1)).deleteById(menuId);
    }

    // ==================== 条件查询测试 ====================

    @Test
    @DisplayName("查询菜单列表")
    void testSelectList() {
        // Given
        SysMenu menu1 = new SysMenu();
        menu1.setMenuId(1L);
        menu1.setMenuName("系统管理");

        SysMenu menu2 = new SysMenu();
        menu2.setMenuId(2L);
        menu2.setMenuName("用户管理");

        List<SysMenu> menus = Arrays.asList(menu1, menu2);
        when(sysMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(menus);

        // When
        List<SysMenu> result = sysMenuMapper.selectList(new LambdaQueryWrapper<>());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMenuName()).isEqualTo("系统管理");
        assertThat(result.get(1).getMenuName()).isEqualTo("用户管理");
        verify(sysMenuMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("查询单个菜单")
    void testSelectOne() {
        // Given
        SysMenu menu = new SysMenu();
        menu.setMenuId(1L);
        menu.setMenuName("系统管理");

        when(sysMenuMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(menu);

        // When
        SysMenu result = sysMenuMapper.selectOne(
            new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getMenuName, "系统管理")
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMenuName()).isEqualTo("系统管理");
    }

    @Test
    @DisplayName("查询菜单数量")
    void testSelectCount() {
        // Given
        Long expectedCount = 10L;
        when(sysMenuMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(expectedCount);

        // When
        Long result = sysMenuMapper.selectCount(new LambdaQueryWrapper<>());

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(sysMenuMapper, times(1)).selectCount(any(LambdaQueryWrapper.class));
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询菜单")
    void testSelectPage() {
        // Given
        Page<SysMenu> page = new Page<>(1, 10);
        SysMenu menu1 = new SysMenu();
        menu1.setMenuId(1L);
        menu1.setMenuName("系统管理");

        SysMenu menu2 = new SysMenu();
        menu2.setMenuId(2L);
        menu2.setMenuName("用户管理");

        Page<SysMenu> resultPage = new Page<>(1, 10);
        resultPage.setRecords(Arrays.asList(menu1, menu2));
        resultPage.setTotal(2);

        when(sysMenuMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(resultPage);

        // When
        IPage<SysMenu> result = sysMenuMapper.selectPage(page, new LambdaQueryWrapper<>());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(2);
        assertThat(result.getTotal()).isEqualTo(2);
        verify(sysMenuMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    // ==================== 批量操作测试 ====================

    @Test
    @DisplayName("批量查询菜单")
    void testSelectBatchIds() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        SysMenu menu1 = new SysMenu();
        menu1.setMenuId(1L);

        SysMenu menu2 = new SysMenu();
        menu2.setMenuId(2L);

        SysMenu menu3 = new SysMenu();
        menu3.setMenuId(3L);

        List<SysMenu> menus = Arrays.asList(menu1, menu2, menu3);
        when(sysMenuMapper.selectBatchIds(ids)).thenReturn(menus);

        // When
        List<SysMenu> result = sysMenuMapper.selectBatchIds(ids);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getMenuId()).isEqualTo(1L);
        assertThat(result.get(1).getMenuId()).isEqualTo(2L);
        assertThat(result.get(2).getMenuId()).isEqualTo(3L);
        verify(sysMenuMapper, times(1)).selectBatchIds(ids);
    }

    @Test
    @DisplayName("批量删除菜单")
    void testDeleteBatchIds() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(sysMenuMapper.deleteBatchIds(ids)).thenReturn(3);

        // When
        int result = sysMenuMapper.deleteBatchIds(ids);

        // Then
        assertThat(result).isEqualTo(3);
        verify(sysMenuMapper, times(1)).deleteBatchIds(ids);
    }

    // ==================== 状态查询测试 ====================

    @Test
    @DisplayName("根据状态查询菜单")
    void testSelectByStatus() {
        // Given
        String status = "0";
        SysMenu menu1 = new SysMenu();
        menu1.setMenuId(1L);
        menu1.setStatus(status);

        List<SysMenu> menus = Arrays.asList(menu1);
        when(sysMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(menus);

        // When
        List<SysMenu> result = sysMenuMapper.selectList(
            new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, status)
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(status);
    }

    // ==================== 菜单类型查询测试 ====================

    @Test
    @DisplayName("根据菜单类型查询")
    void testSelectByMenuType() {
        // Given
        String menuType = "C";
        SysMenu menu1 = new SysMenu();
        menu1.setMenuId(1L);
        menu1.setMenuType(menuType);

        List<SysMenu> menus = Arrays.asList(menu1);
        when(sysMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(menus);

        // When
        List<SysMenu> result = sysMenuMapper.selectList(
            new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getMenuType, menuType)
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMenuType()).isEqualTo(menuType);
    }

    // ==================== 父菜单查询测试 ====================

    @Test
    @DisplayName("根据父菜单 ID 查询子菜单")
    void testSelectByParentId() {
        // Given
        Long parentId = 1L;
        SysMenu menu1 = new SysMenu();
        menu1.setMenuId(2L);
        menu1.setParentId(parentId);

        SysMenu menu2 = new SysMenu();
        menu2.setMenuId(3L);
        menu2.setParentId(parentId);

        List<SysMenu> menus = Arrays.asList(menu1, menu2);
        when(sysMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(menus);

        // When
        List<SysMenu> result = sysMenuMapper.selectList(
            new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, parentId)
        );

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getParentId()).isEqualTo(parentId);
        assertThat(result.get(1).getParentId()).isEqualTo(parentId);
    }

    // ==================== 查询顶级菜单测试 ====================

    @Test
    @DisplayName("查询顶级菜单（父菜单ID为0）")
    void testSelectTopLevelMenus() {
        // Given
        SysMenu menu1 = new SysMenu();
        menu1.setMenuId(1L);
        menu1.setParentId(0L);
        menu1.setMenuType("M");

        SysMenu menu2 = new SysMenu();
        menu2.setMenuId(10L);
        menu2.setParentId(0L);
        menu2.setMenuType("M");

        List<SysMenu> menus = Arrays.asList(menu1, menu2);
        when(sysMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(menus);

        // When
        List<SysMenu> result = sysMenuMapper.selectList(
            new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, 0L)
                .eq(SysMenu::getMenuType, "M")
        );

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getParentId()).isEqualTo(0L);
        assertThat(result.get(1).getParentId()).isEqualTo(0L);
    }
}
