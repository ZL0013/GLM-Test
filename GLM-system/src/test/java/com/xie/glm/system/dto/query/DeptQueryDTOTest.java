package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 部门查询条件 DTO 测试类
 *
 * <p>测试 {@link DeptQueryDTO} 的基本功能，包括：
 * <ul>
 *   <li>继承 {@link PageQuery} 获得分页和排序能力</li>
 *   <li>部门查询条件：部门名称、状态、负责人</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("部门查询条件 DTO 测试")
class DeptQueryDTOTest {

    @Nested
    @DisplayName("基础功能测试")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("应该能够创建空的查询条件对象")
        void shouldCreateEmptyQuery() {
            // When
            DeptQueryDTO query = new DeptQueryDTO();

            // Then
            assertThat(query).isNotNull();
            assertThat(query.getDeptName()).isNull();
            assertThat(query.getStatus()).isNull();
            assertThat(query.getLeader()).isNull();
        }

        @Test
        @DisplayName("应该能够继承分页参数")
        void shouldInheritPagination() {
            // Given
            DeptQueryDTO query = new DeptQueryDTO();

            // When
            query.setPageNum(2);
            query.setPageSize(20);

            // Then
            assertThat(query.getPageNum()).isEqualTo(2);
            assertThat(query.getPageSize()).isEqualTo(20);
        }

        @Test
        @DisplayName("应该能够继承排序参数")
        void shouldInheritSorting() {
            // Given
            DeptQueryDTO query = new DeptQueryDTO();

            // When
            query.setOrderByColumn("order_num");
            query.setIsAsc("asc");

            // Then
            assertThat(query.getOrderByColumn()).isEqualTo("order_num");
            assertThat(query.getIsAsc()).isEqualTo("asc");
        }
    }

    @Nested
    @DisplayName("查询条件测试")
    class QueryConditionTests {

        @Test
        @DisplayName("应该能够设置部门名称查询条件")
        void shouldSetDeptNameCondition() {
            // Given
            DeptQueryDTO query = new DeptQueryDTO();

            // When
            query.setDeptName("研发部");

            // Then
            assertThat(query.getDeptName()).isEqualTo("研发部");
        }

        @Test
        @DisplayName("应该能够设置状态查询条件")
        void shouldSetStatusCondition() {
            // Given
            DeptQueryDTO query = new DeptQueryDTO();

            // When
            query.setStatus("0");

            // Then
            assertThat(query.getStatus()).isEqualTo("0");
        }

        @Test
        @DisplayName("应该能够设置负责人查询条件")
        void shouldSetLeaderCondition() {
            // Given
            DeptQueryDTO query = new DeptQueryDTO();

            // When
            query.setLeader("张三");

            // Then
            assertThat(query.getLeader()).isEqualTo("张三");
        }

        @Test
        @DisplayName("应该能够设置多个查询条件")
        void shouldSetMultipleConditions() {
            // Given
            DeptQueryDTO query = new DeptQueryDTO();

            // When
            query.setDeptName("研发");
            query.setStatus("0");
            query.setLeader("张三");
            query.setPageNum(1);
            query.setPageSize(10);

            // Then
            assertThat(query.getDeptName()).isEqualTo("研发");
            assertThat(query.getStatus()).isEqualTo("0");
            assertThat(query.getLeader()).isEqualTo("张三");
            assertThat(query.getPageNum()).isEqualTo(1);
            assertThat(query.getPageSize()).isEqualTo(10);
        }
    }
}
