package com.xie.glm.system.dto.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MenuQueryDTO 测试类
 *
 * <p>测试菜单查询条件 DTO 的基本功能和边界条件。
 *
 * <p>测试场景：
 * <ul>
 *   <li>基本字段设置和获取</li>
 *   <li>分页参数设置（继承自 PageQuery）</li>
 *   <li>排序参数设置</li>
 *   <li>菜单名称模糊搜索条件</li>
 *   <li>菜单类型筛选条件</li>
 *   <li>菜单状态筛选条件</li>
 *   <li>显示状态筛选条件</li>
 *   <li>父菜单ID筛选条件</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("菜单查询条件 DTO 测试")
class MenuQueryDTOTest {

    // ==================== 基本功能测试 ====================

    @Test
    @DisplayName("创建空查询条件对象")
    void testCreateEmptyQuery() {
        // When
        MenuQueryDTO query = new MenuQueryDTO();

        // Then
        assertThat(query).isNotNull();
        assertThat(query.getMenuName()).isNull();
        assertThat(query.getMenuType()).isNull();
        assertThat(query.getStatus()).isNull();
        assertThat(query.getVisible()).isNull();
        assertThat(query.getParentId()).isNull();
    }

    @ParameterizedTest
    @CsvSource({
        "系统管理, M, 0, 0",
        "用户管理, C, 1, 0",
        "新增, F, 0, 1"
    })
    @DisplayName("设置和获取查询条件")
    void testSetAndGetQueryConditions(String menuName, String menuType, String status, String visible) {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setMenuName(menuName);
        query.setMenuType(menuType);
        query.setStatus(status);
        query.setVisible(visible);
        query.setParentId(0L);

        // Then
        assertThat(query.getMenuName()).isEqualTo(menuName);
        assertThat(query.getMenuType()).isEqualTo(menuType);
        assertThat(query.getStatus()).isEqualTo(status);
        assertThat(query.getVisible()).isEqualTo(visible);
        assertThat(query.getParentId()).isEqualTo(0L);
    }

    // ==================== 分页参数测试（继承自 PageQuery）====================

    @ParameterizedTest
    @CsvSource({
        "1, 10",
        "2, 20",
        "5, 50"
    })
    @DisplayName("设置分页参数")
    void testSetPagination(Integer pageNum, Integer pageSize) {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);

        // Then
        assertThat(query.getPageNum()).isEqualTo(pageNum);
        assertThat(query.getPageSize()).isEqualTo(pageSize);
    }

    // ==================== 排序参数测试 ====================

    @ParameterizedTest
    @CsvSource({
        "order_num, asc",
        "menu_id, desc",
        "create_time, asc"
    })
    @DisplayName("设置排序参数")
    void testSetSorting(String orderByColumn, String isAsc) {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setOrderByColumn(orderByColumn);
        query.setIsAsc(isAsc);

        // Then
        assertThat(query.getOrderByColumn()).isEqualTo(orderByColumn);
        assertThat(query.getIsAsc()).isEqualTo(isAsc);
    }

    // ==================== 菜单类型筛选测试 ====================

    @ParameterizedTest
    @MethodSource("provideMenuTypes")
    @DisplayName("菜单类型筛选条件")
    void testMenuTypeFilter(String menuType, boolean isValid) {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setMenuType(menuType);

        // Then
        assertThat(query.getMenuType()).isEqualTo(menuType);
        if (isValid) {
            assertThat(menuType).isIn("M", "C", "F");
        }
    }

    private static Stream<Arguments> provideMenuTypes() {
        return Stream.of(
            Arguments.of("M", true),   // 目录
            Arguments.of("C", true),   // 菜单
            Arguments.of("F", true),   // 按钮
            Arguments.of("X", false)   // 无效类型
        );
    }

    // ==================== 状态筛选测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0",  // 正常
        "1"   // 停用
    })
    @DisplayName("菜单状态筛选条件")
    void testStatusFilter(String status) {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setStatus(status);

        // Then
        assertThat(query.getStatus()).isEqualTo(status);
    }

    // ==================== 显示状态筛选测试 ====================

    @ParameterizedTest
    @CsvSource({
        "0",  // 显示
        "1"   // 隐藏
    })
    @DisplayName("显示状态筛选条件")
    void testVisibleFilter(String visible) {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setVisible(visible);

        // Then
        assertThat(query.getVisible()).isEqualTo(visible);
    }

    // ==================== 父菜单筛选测试 ====================

    @Test
    @DisplayName("筛选顶级菜单（父菜单ID为0）")
    void testFilterTopLevelMenu() {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setParentId(0L);

        // Then
        assertThat(query.getParentId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("筛选子菜单（父菜单ID大于0）")
    void testFilterSubMenu() {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setParentId(1L);

        // Then
        assertThat(query.getParentId()).isEqualTo(1L);
    }

    // ==================== 组合查询条件测试 ====================

    @Test
    @DisplayName("组合查询条件：菜单名称 + 菜单类型 + 状态")
    void testCombinedQueryConditions() {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setMenuName("用户");
        query.setMenuType("C");
        query.setStatus("0");
        query.setPageNum(1);
        query.setPageSize(10);
        query.setOrderByColumn("order_num");
        query.setIsAsc("asc");

        // Then
        assertThat(query.getMenuName()).isEqualTo("用户");
        assertThat(query.getMenuType()).isEqualTo("C");
        assertThat(query.getStatus()).isEqualTo("0");
        assertThat(query.getPageNum()).isEqualTo(1);
        assertThat(query.getPageSize()).isEqualTo(10);
        assertThat(query.getOrderByColumn()).isEqualTo("order_num");
        assertThat(query.getIsAsc()).isEqualTo("asc");
    }

    // ==================== 边界条件测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("空值查询条件")
    void testNullQueryConditions(String menuName) {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setMenuName(menuName);

        // Then
        assertThat(query.getMenuName()).isNull();
    }

    @Test
    @DisplayName("空字符串菜单名称")
    void testEmptyMenuName() {
        // Given
        MenuQueryDTO query = new MenuQueryDTO();

        // When
        query.setMenuName("");

        // Then
        assertThat(query.getMenuName()).isEmpty();
    }
}
