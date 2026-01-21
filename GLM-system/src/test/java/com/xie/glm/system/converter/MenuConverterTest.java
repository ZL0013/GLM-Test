package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysMenu;
import com.xie.glm.system.dto.MenuDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MenuConverter 菜单转换器测试类
 *
 * <p>测试 MapStruct 生成的菜单对象转换功能
 *
 * <p>测试原则：
 * <ul>
 *   <li>使用 MapStruct 生成的实现进行测试</li>
 *   <li>验证 Entity ↔ DTO 双向转换</li>
 *   <li>测试列表转换</li>
 *   <li>验证字段映射的完整性</li>
 * </ul>
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MenuConverter 菜单转换器集成测试")
class MenuConverterTest {

    // MapStruct 生成的实现类需要通过 Spring 注入或手动创建
    // 这里我们使用 Mappers.getMapper 方法来获取实例
    private final MenuConverter menuConverter = org.mapstruct.factory.Mappers.getMapper(MenuConverter.class);

    // ==================== Entity → DTO 转换测试 ====================

    @Test
    @DisplayName("Entity → DTO - 验证基本转换")
    void testToDto() {
        // 准备测试数据
        SysMenu entity = createTestMenu(1L, "系统管理", "M", 0L);

        // 执行转换
        MenuDTO dto = menuConverter.toDto(entity);

        // 验证结果
        assertNotNull(dto, "DTO不应为null");
        assertEquals(entity.getMenuId(), dto.getMenuId(), "菜单ID应匹配");
        assertEquals(entity.getMenuName(), dto.getMenuName(), "菜单名称应匹配");
        assertEquals(entity.getParentId(), dto.getParentId(), "父菜单ID应匹配");
        assertEquals(entity.getOrderNum(), dto.getOrderNum(), "排序应匹配");
        assertEquals(entity.getPath(), dto.getPath(), "路由地址应匹配");
        assertEquals(entity.getComponent(), dto.getComponent(), "组件路径应匹配");
        assertEquals(entity.getQuery(), dto.getQuery(), "路由参数应匹配");
        assertEquals(entity.getRouteName(), dto.getRouteName(), "路由名称应匹配");
        assertEquals(entity.getIsFrame(), dto.getIsFrame(), "外链标识应匹配");
        assertEquals(entity.getIsCache(), dto.getIsCache(), "缓存标识应匹配");
        assertEquals(entity.getMenuType(), dto.getMenuType(), "菜单类型应匹配");
        assertEquals(entity.getVisible(), dto.getVisible(), "显示状态应匹配");
        assertEquals(entity.getStatus(), dto.getStatus(), "菜单状态应匹配");
        assertEquals(entity.getPerms(), dto.getPerms(), "权限标识应匹配");
        assertEquals(entity.getIcon(), dto.getIcon(), "菜单图标应匹配");
        assertEquals(entity.getCreateTime(), dto.getCreateTime(), "创建时间应匹配");
        assertEquals(entity.getUpdateTime(), dto.getUpdateTime(), "更新时间应匹配");
    }

    @Test
    @DisplayName("Entity → DTO - 验证 null 值处理")
    void testToDtoWithNullValues() {
        SysMenu entity = new SysMenu();
        // 所有字段保持默认值 null

        MenuDTO dto = menuConverter.toDto(entity);

        assertNotNull(dto, "DTO不应为null");
        assertNull(dto.getMenuId(), "null字段应保持null");
        assertNull(dto.getMenuName(), "null字段应保持null");
        assertNull(dto.getParentId(), "null字段应保持null");
    }

    @ParameterizedTest
    @MethodSource("provideMenuEntities")
    @DisplayName("Entity → DTO - 参数化测试")
    void testToDtoParameterized(SysMenu entity) {
        MenuDTO dto = menuConverter.toDto(entity);

        assertNotNull(dto, "DTO不应为null");
        assertEquals(entity.getMenuId(), dto.getMenuId());
        assertEquals(entity.getMenuName(), dto.getMenuName());
        assertEquals(entity.getMenuType(), dto.getMenuType());
    }

    private static Stream<SysMenu> provideMenuEntities() {
        return Stream.of(
            createTestMenu(1L, "系统管理", "M", 0L),
            createTestMenu(2L, "用户管理", "C", 1L),
            createTestMenu(3L, "新增用户", "F", 2L)
        );
    }

    // ==================== DTO → Entity 转换测试 ====================

    @Test
    @DisplayName("DTO → Entity - 验证基本转换")
    void testToEntity() {
        MenuDTO dto = new MenuDTO();
        dto.setMenuId(1L);
        dto.setMenuName("系统管理");
        dto.setParentId(0L);
        dto.setOrderNum(1);
        dto.setPath("system");
        dto.setComponent("system/index");
        dto.setRouteName("System");
        dto.setIsFrame(0);
        dto.setIsCache(0);
        dto.setMenuType("M");
        dto.setVisible("0");
        dto.setStatus("0");
        dto.setPerms("system:user:list");
        dto.setIcon("system");

        SysMenu entity = menuConverter.toEntity(dto);

        assertNotNull(entity, "Entity不应为null");
        assertEquals(dto.getMenuId(), entity.getMenuId(), "菜单ID应匹配");
        assertEquals(dto.getMenuName(), entity.getMenuName(), "菜单名称应匹配");
        assertEquals(dto.getParentId(), entity.getParentId(), "父菜单ID应匹配");
        assertEquals(dto.getOrderNum(), entity.getOrderNum(), "排序应匹配");
        assertEquals(dto.getPath(), entity.getPath(), "路由地址应匹配");
        assertEquals(dto.getComponent(), entity.getComponent(), "组件路径应匹配");
        assertEquals(dto.getQuery(), entity.getQuery(), "路由参数应匹配");
        assertEquals(dto.getRouteName(), entity.getRouteName(), "路由名称应匹配");
        assertEquals(dto.getIsFrame(), entity.getIsFrame(), "外链标识应匹配");
        assertEquals(dto.getIsCache(), entity.getIsCache(), "缓存标识应匹配");
        assertEquals(dto.getMenuType(), entity.getMenuType(), "菜单类型应匹配");
        assertEquals(dto.getVisible(), entity.getVisible(), "显示状态应匹配");
        assertEquals(dto.getStatus(), entity.getStatus(), "菜单状态应匹配");
        assertEquals(dto.getPerms(), entity.getPerms(), "权限标识应匹配");
        assertEquals(dto.getIcon(), entity.getIcon(), "菜单图标应匹配");
    }

    // ==================== 列表转换测试 ====================

    @Test
    @DisplayName("Entity List → DTO List - 验证列表转换")
    void testToDtoList() {
        List<SysMenu> entities = Arrays.asList(
            createTestMenu(1L, "系统管理", "M", 0L),
            createTestMenu(2L, "用户管理", "C", 1L),
            createTestMenu(3L, "角色管理", "C", 1L)
        );

        List<MenuDTO> dtos = menuConverter.toDtoList(entities);

        assertNotNull(dtos, "DTO列表不应为null");
        assertEquals(entities.size(), dtos.size(), "列表大小应匹配");

        for (int i = 0; i < entities.size(); i++) {
            assertEquals(entities.get(i).getMenuId(), dtos.get(i).getMenuId(),
                    "第" + i + "个元素的 menuId 应匹配");
            assertEquals(entities.get(i).getMenuName(), dtos.get(i).getMenuName(),
                    "第" + i + "个元素的 menuName 应匹配");
        }
    }

    @Test
    @DisplayName("Entity List → DTO List - 验证空列表转换")
    void testToDtoListWithEmpty() {
        List<SysMenu> entities = List.of();

        List<MenuDTO> dtos = menuConverter.toDtoList(entities);

        assertNotNull(dtos, "DTO列表不应为null");
        assertTrue(dtos.isEmpty(), "DTO列表应为空");
    }

    @Test
    @DisplayName("Entity List → DTO List - 验证 null 列表转换")
    void testToDtoListWithNull() {
        List<MenuDTO> dtos = menuConverter.toDtoList(null);

        // MapStruct 生成的实现通常返回 null 或空列表
        // 这里验证不会抛出异常
        if (dtos != null) {
            assertTrue(dtos.isEmpty(), "null输入应返回空列表或null");
        }
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 菜单列表查询转换")
    void testScenario_MenuListQuery() {
        // 模拟数据库查询结果
        List<SysMenu> dbResult = Arrays.asList(
            createTestMenu(1L, "系统管理", "M", 0L),
            createTestMenu(2L, "用户管理", "C", 1L),
            createTestMenu(3L, "角色管理", "C", 1L)
        );

        // 转换为 DTO 返回给前端
        List<MenuDTO> dtoList = menuConverter.toDtoList(dbResult);

        assertEquals(3, dtoList.size(), "应返回3个菜单");

        // 验证第一个菜单
        assertEquals(1L, dtoList.get(0).getMenuId());
        assertEquals("系统管理", dtoList.get(0).getMenuName());
        assertEquals("M", dtoList.get(0).getMenuType());
    }

    @Test
    @DisplayName("业务场景 - 创建目录菜单转换")
    void testScenario_CreateDirectoryMenu() {
        MenuDTO dto = new MenuDTO();
        dto.setMenuName("系统管理");
        dto.setParentId(0L);
        dto.setOrderNum(1);
        dto.setPath("system");
        dto.setMenuType("M");
        dto.setVisible("0");
        dto.setStatus("0");
        dto.setIcon("system");

        // 转换为 Entity 准备插入数据库
        SysMenu entity = menuConverter.toEntity(dto);

        assertEquals("系统管理", entity.getMenuName());
        assertEquals(0L, entity.getParentId());
        assertEquals("M", entity.getMenuType());
    }

    @Test
    @DisplayName("业务场景 - 创建菜单项转换")
    void testScenario_CreateMenuItem() {
        MenuDTO dto = new MenuDTO();
        dto.setMenuName("用户管理");
        dto.setParentId(1L);
        dto.setOrderNum(1);
        dto.setPath("user");
        dto.setComponent("system/user/index");
        dto.setRouteName("User");
        dto.setMenuType("C");
        dto.setPerms("system:user:list");
        dto.setVisible("0");
        dto.setStatus("0");
        dto.setIcon("user");

        // 转换为 Entity 准备插入数据库
        SysMenu entity = menuConverter.toEntity(dto);

        assertEquals("用户管理", entity.getMenuName());
        assertEquals(1L, entity.getParentId());
        assertEquals("C", entity.getMenuType());
        assertEquals("system/user/index", entity.getComponent());
        assertEquals("system:user:list", entity.getPerms());
    }

    @Test
    @DisplayName("业务场景 - 创建按钮权限转换")
    void testScenario_CreateButtonPermission() {
        MenuDTO dto = new MenuDTO();
        dto.setMenuName("新增用户");
        dto.setParentId(2L);
        dto.setOrderNum(1);
        dto.setMenuType("F");
        dto.setPerms("system:user:add");
        dto.setVisible("0");
        dto.setStatus("0");

        // 转换为 Entity 准备插入数据库
        SysMenu entity = menuConverter.toEntity(dto);

        assertEquals("新增用户", entity.getMenuName());
        assertEquals(2L, entity.getParentId());
        assertEquals("F", entity.getMenuType());
        assertEquals("system:user:add", entity.getPerms());
    }

    // ==================== 辅助方法 ====================

    /**
     * 创建测试用的菜单实体
     */
    private static SysMenu createTestMenu(Long menuId, String menuName, String menuType, Long parentId) {
        SysMenu entity = new SysMenu();
        entity.setMenuId(menuId);
        entity.setMenuName(menuName);
        entity.setParentId(parentId);
        entity.setOrderNum(1);
        entity.setPath("path");
        entity.setComponent("component");
        entity.setQuery(null);
        entity.setRouteName("RouteName");
        entity.setIsFrame(0);
        entity.setIsCache(0);
        entity.setMenuType(menuType);
        entity.setVisible("0");
        entity.setStatus("0");
        entity.setPerms("perms");
        entity.setIcon("icon");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return entity;
    }
}
