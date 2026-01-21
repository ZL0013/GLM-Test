package com.xie.glm.system.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MenuDTO 测试类
 *
 * <p>测试菜单数据传输对象的基本功能和字段设置。
 *
 * <p>测试场景：
 * <ul>
 *   <li>基本字段设置和获取</li>
 *   <li>菜单类型验证（M=目录，C=菜单，F=按钮）</li>
 *   <li>菜单状态验证（0=正常，1=停用）</li>
 *   <li>显示状态验证（0=显示，1=隐藏）</li>
 *   <li>外链标识验证（0=否，1=是）</li>
 *   <li>缓存标识验证（0=缓存，1=不缓存）</li>
 *   <li>父子菜单关系验证</li>
 *   <li>排序字段验证</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("菜单 DTO 测试")
class MenuDTOTest {

    // ==================== 基本功能测试 ====================

    @Test
    @DisplayName("创建空菜单 DTO")
    void testCreateEmptyMenuDTO() {
        // When
        MenuDTO dto = new MenuDTO();

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getMenuId()).isNull();
        assertThat(dto.getMenuName()).isNull();
        assertThat(dto.getMenuType()).isNull();
        assertThat(dto.getParentId()).isNull();
    }

    @Test
    @DisplayName("设置和获取菜单基本信息")
    void testSetAndGetBasicFields() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setMenuId(1L);
        dto.setMenuName("系统管理");
        dto.setParentId(0L);
        dto.setOrderNum(1);
        dto.setPath("system");
        dto.setComponent("system/index");
        dto.setRouteName("System");

        // Then
        assertThat(dto.getMenuId()).isEqualTo(1L);
        assertThat(dto.getMenuName()).isEqualTo("系统管理");
        assertThat(dto.getParentId()).isEqualTo(0L);
        assertThat(dto.getOrderNum()).isEqualTo(1);
        assertThat(dto.getPath()).isEqualTo("system");
        assertThat(dto.getComponent()).isEqualTo("system/index");
        assertThat(dto.getRouteName()).isEqualTo("System");
    }

    // ==================== 菜单类型测试 ====================

    @ParameterizedTest
    @MethodSource("provideMenuTypes")
    @DisplayName("菜单类型验证")
    void testMenuType(String menuType, String description) {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setMenuType(menuType);

        // Then
        assertThat(dto.getMenuType()).isEqualTo(menuType);
        assertThat(menuType).isIn("M", "C", "F");
    }

    private static Stream<Arguments> provideMenuTypes() {
        return Stream.of(
            Arguments.of("M", "目录"),
            Arguments.of("C", "菜单"),
            Arguments.of("F", "按钮")
        );
    }

    // ==================== 菜单状态测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0, 正常",
        "1, 停用"
    })
    @DisplayName("菜单状态验证")
    void testStatus(String status, String description) {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setStatus(status);

        // Then
        assertThat(dto.getStatus()).isEqualTo(status);
    }

    // ==================== 显示状态测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0, 显示",
        "1, 隐藏"
    })
    @DisplayName("显示状态验证")
    void testVisible(String visible, String description) {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setVisible(visible);

        // Then
        assertThat(dto.getVisible()).isEqualTo(visible);
    }

    // ==================== 外链标识测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0, 内部菜单",
        "1, 外链"
    })
    @DisplayName("外链标识验证")
    void testIsFrame(Integer isFrame, String description) {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setIsFrame(isFrame);

        // Then
        assertThat(dto.getIsFrame()).isEqualTo(isFrame);
    }

    // ==================== 缓存标识测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0, 缓存",
        "1, 不缓存"
    })
    @DisplayName("缓存标识验证")
    void testIsCache(Integer isCache, String description) {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setIsCache(isCache);

        // Then
        assertThat(dto.getIsCache()).isEqualTo(isCache);
    }

    // ==================== 权限标识测试 ====================

    @Test
    @DisplayName("权限标识设置")
    void testPerms() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setPerms("system:user:list");

        // Then
        assertThat(dto.getPerms()).isEqualTo("system:user:list");
    }

    @Test
    @DisplayName("权限标识为空")
    void testPermsEmpty() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setPerms(null);

        // Then
        assertThat(dto.getPerms()).isNull();
    }

    // ==================== 菜单图标测试 ====================

    @Test
    @DisplayName("菜单图标设置")
    void testIcon() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setIcon("system");

        // Then
        assertThat(dto.getIcon()).isEqualTo("system");
    }

    // ==================== 路由参数测试 ====================

    @Test
    @DisplayName("路由参数设置")
    void testQuery() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setQuery("{\"id\":1}");

        // Then
        assertThat(dto.getQuery()).isEqualTo("{\"id\":1}");
    }

    // ==================== 父子菜单关系测试 ====================

    @Test
    @DisplayName("顶级菜单（父菜单ID为0）")
    void testTopLevelMenu() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setParentId(0L);

        // Then
        assertThat(dto.getParentId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("子菜单（父菜单ID大于0）")
    void testSubMenu() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setParentId(1L);

        // Then
        assertThat(dto.getParentId()).isEqualTo(1L);
    }

    // ==================== 排序测试 ====================

    @ParameterizedTest
    @CsvSource({
        "1",
        "5",
        "10",
        "100"
    })
    @DisplayName("显示顺序设置")
    void testOrderNum(Integer orderNum) {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setOrderNum(orderNum);

        // Then
        assertThat(dto.getOrderNum()).isEqualTo(orderNum);
    }

    // ==================== 时间字段测试 ====================

    @Test
    @DisplayName("设置创建时间和更新时间")
    void testTimeFields() {
        // Given
        MenuDTO dto = new MenuDTO();
        LocalDateTime now = LocalDateTime.now();

        // When
        dto.setCreateTime(now);
        dto.setUpdateTime(now);

        // Then
        assertThat(dto.getCreateTime()).isEqualTo(now);
        assertThat(dto.getUpdateTime()).isEqualTo(now);
    }

    // ==================== 完整菜单对象测试 ====================

    @Test
    @DisplayName("创建完整目录菜单")
    void testCreateCompleteDirectoryMenu() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setMenuId(1L);
        dto.setMenuName("系统管理");
        dto.setParentId(0L);
        dto.setOrderNum(1);
        dto.setPath("system");
        dto.setMenuType("M");
        dto.setVisible("0");
        dto.setStatus("0");
        dto.setIsFrame(0);
        dto.setIsCache(0);
        dto.setIcon("system");
        dto.setCreateTime(LocalDateTime.now());

        // Then
        assertThat(dto.getMenuId()).isEqualTo(1L);
        assertThat(dto.getMenuName()).isEqualTo("系统管理");
        assertThat(dto.getMenuType()).isEqualTo("M");
        assertThat(dto.getParentId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("创建完整菜单项")
    void testCreateCompleteMenuItem() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setMenuId(2L);
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
        dto.setIsFrame(0);
        dto.setIsCache(0);
        dto.setIcon("user");
        dto.setCreateTime(LocalDateTime.now());

        // Then
        assertThat(dto.getMenuId()).isEqualTo(2L);
        assertThat(dto.getMenuName()).isEqualTo("用户管理");
        assertThat(dto.getMenuType()).isEqualTo("C");
        assertThat(dto.getPerms()).isEqualTo("system:user:list");
        assertThat(dto.getComponent()).isEqualTo("system/user/index");
    }

    @Test
    @DisplayName("创建完整按钮权限")
    void testCreateCompleteButtonPermission() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setMenuId(3L);
        dto.setMenuName("新增用户");
        dto.setParentId(2L);
        dto.setOrderNum(1);
        dto.setMenuType("F");
        dto.setPerms("system:user:add");
        dto.setVisible("0");
        dto.setStatus("0");
        dto.setCreateTime(LocalDateTime.now());

        // Then
        assertThat(dto.getMenuId()).isEqualTo(3L);
        assertThat(dto.getMenuName()).isEqualTo("新增用户");
        assertThat(dto.getMenuType()).isEqualTo("F");
        assertThat(dto.getPerms()).isEqualTo("system:user:add");
    }

    // ==================== 外链菜单测试 ====================

    @Test
    @DisplayName("创建外链菜单")
    void testCreateExternalLinkMenu() {
        // Given
        MenuDTO dto = new MenuDTO();

        // When
        dto.setMenuId(4L);
        dto.setMenuName("官方文档");
        dto.setParentId(0L);
        dto.setOrderNum(10);
        dto.setPath("https://docs.example.com");
        dto.setMenuType("C");
        dto.setIsFrame(1);  // 外链
        dto.setVisible("0");
        dto.setStatus("0");

        // Then
        assertThat(dto.getMenuId()).isEqualTo(4L);
        assertThat(dto.getMenuName()).isEqualTo("官方文档");
        assertThat(dto.getPath()).isEqualTo("https://docs.example.com");
        assertThat(dto.getIsFrame()).isEqualTo(1);
    }
}
