package com.xie.glm.system.dto.query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 角色查询条件 DTO 测试
 *
 * <p>测试 {@link RoleQueryDTO} 的各种场景：
 * <ul>
 *   <li>对象创建和初始化</li>
 *   <li>字段设置和获取</li>
 *   <li>分页参数继承</li>
 *   <li>查询条件组合</li>
 * </ul>
 *
 * @author xie
 */
class RoleQueryDTOTest {

    // ==================== 基础功能测试 ====================

    @Test
    void testRoleQueryDTOCreation() {
        // Given & When
        RoleQueryDTO query = new RoleQueryDTO();

        // Then
        assertThat(query).isNotNull();
        assertThat(query.getRoleName()).isNull();
        assertThat(query.getRoleKey()).isNull();
        assertThat(query.getStatus()).isNull();
        assertThat(query.getStartTime()).isNull();
        assertThat(query.getEndTime()).isNull();
    }

    @Test
    void testRoleQueryDTOFieldSettersAndGetters() {
        // Given
        RoleQueryDTO query = new RoleQueryDTO();
        String roleName = "admin";
        String roleKey = "admin";
        String status = "0";
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 31, 23, 59);

        // When
        query.setRoleName(roleName);
        query.setRoleKey(roleKey);
        query.setStatus(status);
        query.setStartTime(startTime);
        query.setEndTime(endTime);

        // Then
        assertThat(query.getRoleName()).isEqualTo(roleName);
        assertThat(query.getRoleKey()).isEqualTo(roleKey);
        assertThat(query.getStatus()).isEqualTo(status);
        assertThat(query.getStartTime()).isEqualTo(startTime);
        assertThat(query.getEndTime()).isEqualTo(endTime);
    }

    // ==================== 分页参数继承测试 ====================

    @ParameterizedTest
    @CsvSource({
        "1, 10, role_id, asc",
        "2, 20, role_sort, desc",
        "1, 50, create_time, asc"
    })
    void testPaginationParameters(int pageNum, int pageSize, String orderByColumn, String isAsc) {
        // Given & When
        RoleQueryDTO query = new RoleQueryDTO();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        query.setOrderByColumn(orderByColumn);
        query.setIsAsc(isAsc);

        // Then
        assertThat(query.getPageNum()).isEqualTo(pageNum);
        assertThat(query.getPageSize()).isEqualTo(pageSize);
        assertThat(query.getOrderByColumn()).isEqualTo(orderByColumn);
        assertThat(query.getIsAsc()).isEqualTo(isAsc);
    }

    // ==================== 查询条件组合测试 ====================

    @Test
    void testQueryConditionCombination() {
        // Given
        RoleQueryDTO query = new RoleQueryDTO();
        query.setRoleName("admin");
        query.setRoleKey("admin");
        query.setStatus("0");
        query.setPageNum(1);
        query.setPageSize(10);

        // When & Then
        assertAll("role query conditions",
            () -> assertThat(query.getRoleName()).isEqualTo("admin"),
            () -> assertThat(query.getRoleKey()).isEqualTo("admin"),
            () -> assertThat(query.getStatus()).isEqualTo("0"),
            () -> assertThat(query.getPageNum()).isEqualTo(1),
            () -> assertThat(query.getPageSize()).isEqualTo(10)
        );
    }

    // ==================== 参数化测试 ====================

    @ParameterizedTest
    @MethodSource("provideStatusValues")
    void testStatusValues(String status, boolean isValid) {
        // Given
        RoleQueryDTO query = new RoleQueryDTO();

        // When
        query.setStatus(status);

        // Then
        assertThat(query.getStatus()).isEqualTo(status);
        // 验证状态值只能是 0 或 1
        if (status != null) {
            assertThat(status).isIn("0", "1");
        }
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideStatusValues() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of("0", true),
            org.junit.jupiter.params.provider.Arguments.of("1", true),
            org.junit.jupiter.params.provider.Arguments.of(null, true)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "admin, admin",
        "user, user",
        "guest, guest"
    })
    void testRoleNameAndKey(String roleName, String roleKey) {
        // Given
        RoleQueryDTO query = new RoleQueryDTO();

        // When
        query.setRoleName(roleName);
        query.setRoleKey(roleKey);

        // Then
        assertThat(query.getRoleName()).isEqualTo(roleName);
        assertThat(query.getRoleKey()).isEqualTo(roleKey);
    }

    // ==================== 时间范围测试 ====================

    @Test
    void testTimeRangeQuery() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime tomorrow = now.plusDays(1);

        // When
        RoleQueryDTO query = new RoleQueryDTO();
        query.setStartTime(yesterday);
        query.setEndTime(tomorrow);

        // Then
        assertThat(query.getStartTime()).isBefore(query.getEndTime());
        assertThat(query.getStartTime()).isEqualTo(yesterday);
        assertThat(query.getEndTime()).isEqualTo(tomorrow);
    }

    @Test
    void testNullTimeRange() {
        // Given & When
        RoleQueryDTO query = new RoleQueryDTO();

        // Then
        assertThat(query.getStartTime()).isNull();
        assertThat(query.getEndTime()).isNull();
    }

    // ==================== 完整对象测试 ====================

    @Test
    void testCompleteRoleQueryDTO() {
        // Given
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 31, 23, 59);

        // When
        RoleQueryDTO query = new RoleQueryDTO();
        query.setRoleName("admin");
        query.setRoleKey("admin");
        query.setStatus("0");
        query.setStartTime(startTime);
        query.setEndTime(endTime);
        query.setPageNum(1);
        query.setPageSize(10);
        query.setOrderByColumn("role_sort");
        query.setIsAsc("asc");

        // Then
        assertAll("complete role query dto",
            () -> assertThat(query.getRoleName()).isEqualTo("admin"),
            () -> assertThat(query.getRoleKey()).isEqualTo("admin"),
            () -> assertThat(query.getStatus()).isEqualTo("0"),
            () -> assertThat(query.getStartTime()).isEqualTo(startTime),
            () -> assertThat(query.getEndTime()).isEqualTo(endTime),
            () -> assertThat(query.getPageNum()).isEqualTo(1),
            () -> assertThat(query.getPageSize()).isEqualTo(10),
            () -> assertThat(query.getOrderByColumn()).isEqualTo("role_sort"),
            () -> assertThat(query.getIsAsc()).isEqualTo("asc")
        );
    }
}
