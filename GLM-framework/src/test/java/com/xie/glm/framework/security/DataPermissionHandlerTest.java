package com.xie.glm.framework.security;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据权限处理器测试
 *
 * <p>测试数据权限处理器在各种场景下的 SQL 条件构造行为。
 * <p>测试场景包括：
 * <ul>
 *   <li>管理员用户：不添加任何数据权限条件</li>
 *   <li>普通用户：根据数据权限范围添加相应的 SQL 条件</li>
 *   <li>未认证用户：不添加任何数据权限条件</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("数据权限处理器测试")
class DataPermissionHandlerTest {

    /**
     * 测试：管理员用户不应添加数据权限条件
     */
    @Test
    @DisplayName("管理员用户应返回null（不添加权限条件）")
    void testAdminUser_ReturnsNull() {
        // Given: 设置管理员用户上下文
        CustomUserDetails adminDetails = createAdminUser();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                adminDetails, null, adminDetails.getAuthorities()));
        SecurityContextHolder.setContext(context);

        // When: 获取数据权限SQL条件
        DataPermissionHandler handler = new DataPermissionHandlerImpl();
        Expression whereExpression = handler.getSqlSegment(null, null);

        // Then: 管理员应返回null，表示不添加任何权限条件
        assertNull(whereExpression, "管理员用户应返回null，不添加数据权限条件");

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    /**
     * 测试：未认证用户不应添加数据权限条件
     */
    @Test
    @DisplayName("未认证用户应返回null")
    void testUnauthenticatedUser_ReturnsNull() {
        // Given: 清空安全上下文（未认证状态）
        SecurityContextHolder.clearContext();

        // When: 获取数据权限SQL条件
        DataPermissionHandler handler = new DataPermissionHandlerImpl();
        Expression whereExpression = handler.getSqlSegment(null, null);

        // Then: 未认证用户应返回null
        assertNull(whereExpression, "未认证用户应返回null");
    }

    /**
     * 测试：普通用户（仅本部门数据权限）
     */
    @ParameterizedTest
    @CsvSource({
            "100,       本部门数据",
            "101,       本部门数据"
    })
    @DisplayName("普通用户（本部门数据）应返回正确的SQL条件")
    void testNormalUser_DeptOnly_ReturnsCorrectSql(Long deptId, String description) {
        // Given: 设置普通用户上下文（本部门数据权限）
        CustomUserDetails userDetails = createUser(1L, "user1", deptId,
                DataScopeConstants.DATA_SCOPE_DEPT, /* 数据权限：本部门 */
                null);  // 无自定义部门列表
        setupSecurityContext(userDetails);

        // When: 获取数据权限SQL条件
        DataPermissionHandler handler = new DataPermissionHandlerImpl();
        Expression whereExpression = handler.getSqlSegment(null, null);

        // Then: 应返回EqualsTo表达式
        assertInstanceOf(EqualsTo.class, whereExpression,
                "本部门数据权限应返回EqualsTo表达式");

        EqualsTo equalsTo = (EqualsTo) whereExpression;
        Column column = (Column) equalsTo.getLeftExpression();
        assertEquals("dept_id", column.getColumnName(), "字段名应为dept_id");

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    /**
     * 测试：普通用户（本部门及以下数据权限）
     */
    @Test
    @DisplayName("普通用户（本部门及以下）应返回正确的SQL条件")
    void testNormalUser_DeptAndChildren_ReturnsCorrectSql() {
        // Given: 设置普通用户上下文（本部门及以下数据权限）
        CustomUserDetails userDetails = createUser(1L, "user1", 100L,
                DataScopeConstants.DATA_SCOPE_DEPT_AND_CHILD, /* 数据权限：本部门及以下 */
                List.of(100L, 101L, 102L));  // 部门列表
        setupSecurityContext(userDetails);

        // When: 获取数据权限SQL条件
        DataPermissionHandler handler = new DataPermissionHandlerImpl();
        Expression whereExpression = handler.getSqlSegment(null, null);

        // Then: 应返回In表达式
        assertInstanceOf(InExpression.class, whereExpression,
                "本部门及以下数据权限应返回In表达式");

        InExpression inExpression = (InExpression) whereExpression;
        RowConstructor rightExpression = (RowConstructor) inExpression.getRightExpression();
        List<Expression> expressions = rightExpression.getExpressions();
        assertEquals(3, expressions.size(), "应包含3个部门ID");

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    /**
     * 测试：普通用户（仅本人数据权限）
     */
    @Test
    @DisplayName("普通用户（仅本人数据）应返回正确的SQL条件")
    void testNormalUser_SelfOnly_ReturnsCorrectSql() {
        // Given: 设置普通用户上下文（仅本人数据权限）
        CustomUserDetails userDetails = createUser(1L, "user1", 100L,
                DataScopeConstants.DATA_SCOPE_SELF, /* 数据权限：仅本人 */
                null);
        setupSecurityContext(userDetails);

        // When: 获取数据权限SQL条件
        DataPermissionHandler handler = new DataPermissionHandlerImpl();
        Expression whereExpression = handler.getSqlSegment(null, null);

        // Then: 应返回EqualsTo表达式（user_id = 当前用户ID）
        assertInstanceOf(EqualsTo.class, whereExpression,
                "仅本人数据权限应返回EqualsTo表达式");

        EqualsTo equalsTo = (EqualsTo) whereExpression;
        Column column = (Column) equalsTo.getLeftExpression();
        assertEquals("user_id", column.getColumnName(),
                "字段名应为user_id");

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    /**
     * 测试：普通用户（自定义部门数据权限）
     */
    @ParameterizedTest
    @MethodSource("provideCustomDeptData")
    @DisplayName("普通用户（自定义部门）应返回正确的SQL条件")
    void testNormalUser_CustomDepts_ReturnsCorrectSql(List<Long> deptIds, int expectedCount) {
        // Given: 设置普通用户上下文（自定义部门数据权限）
        CustomUserDetails userDetails = createUser(1L, "user1", 100L,
                DataScopeConstants.DATA_SCOPE_CUSTOM, /* 数据权限：自定义 */
                deptIds);
        setupSecurityContext(userDetails);

        // When: 获取数据权限SQL条件
        DataPermissionHandler handler = new DataPermissionHandlerImpl();
        Expression whereExpression = handler.getSqlSegment(null, null);

        // Then: 应返回In表达式，包含指定的部门ID
        assertInstanceOf(InExpression.class, whereExpression,
                "自定义部门数据权限应返回In表达式");

        InExpression inExpression = (InExpression) whereExpression;
        RowConstructor rightExpression = (RowConstructor) inExpression.getRightExpression();
        List<Expression> expressions = rightExpression.getExpressions();
        assertEquals(expectedCount, expressions.size(),
                "应包含" + expectedCount + "个部门ID");

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    /**
     * 测试：已存在WHERE条件时，应使用AND连接
     */
    @Test
    @DisplayName("已存在WHERE条件时，应使用AND连接新条件")
    void testExistingWhereClause_ShouldUseAnd() {
        // Given: 设置普通用户上下文
        CustomUserDetails userDetails = createUser(1L, "user1", 100L,
                DataScopeConstants.DATA_SCOPE_DEPT, null);
        setupSecurityContext(userDetails);

        // 模拟已存在的WHERE条件
        EqualsTo existingCondition = new EqualsTo();
        existingCondition.setLeftExpression(new Column("status"));
        existingCondition.setRightExpression(new LongValue(1));

        // When: 获取数据权限SQL条件
        DataPermissionHandler handler = new DataPermissionHandlerImpl();
        Expression whereExpression = handler.getSqlSegment(existingCondition, null);

        // Then: 应返回AndExpression，将两个条件用AND连接
        assertInstanceOf(AndExpression.class, whereExpression,
                "应使用AND连接已存在的WHERE条件和新条件");

        AndExpression andExpression = (AndExpression) whereExpression;
        assertSame(existingCondition, andExpression.getLeftExpression(),
                "左侧应为已存在的条件");

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    /**
     * 测试：全部数据权限不应添加条件
     */
    @Test
    @DisplayName("全部数据权限应返回null")
    void testAllDataScope_ReturnsNull() {
        // Given: 设置用户上下文（全部数据权限）
        CustomUserDetails userDetails = createUser(1L, "user1", 100L,
                DataScopeConstants.DATA_SCOPE_ALL, null);
        setupSecurityContext(userDetails);

        // When: 获取数据权限SQL条件
        DataPermissionHandler handler = new DataPermissionHandlerImpl();
        Expression whereExpression = handler.getSqlSegment(null, null);

        // Then: 应返回null，不添加任何条件
        assertNull(whereExpression, "全部数据权限应返回null");

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    // ==================== 辅助方法 ====================

    /**
     * 提供自定义部门数据测试参数
     */
    private static Stream<Arguments> provideCustomDeptData() {
        return Stream.of(
                Arguments.of(List.of(100L), 1),
                Arguments.of(List.of(100L, 101L), 2),
                Arguments.of(List.of(100L, 101L, 102L, 103L), 4)
        );
    }

    /**
     * 创建管理员用户
     */
    private CustomUserDetails createAdminUser() {
        return new CustomUserDetails(1L, "admin", "password",
                true, List.of("admin"), List.of("*:*:*"));
    }

    /**
     * 创建普通用户
     *
     * @param userId     用户ID
     * @param username   用户名
     * @param deptId     部门ID
     * @param dataScope  数据权限范围
     * @param deptIds    自定义部门ID列表
     */
    private CustomUserDetails createUser(Long userId, String username, Long deptId,
                                        int dataScope, List<Long> deptIds) {
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUserId(userId);
        userDetails.setUsername(username);
        userDetails.setPassword("password");
        userDetails.setEnabled(true);
        userDetails.setDeptId(deptId);
        userDetails.setDataScope(dataScope);
        userDetails.setDataScopeDeptIds(deptIds);
        userDetails.setRoles(List.of("common"));
        userDetails.setPermissions(List.of("system:user:list"));
        return userDetails;
    }

    /**
     * 设置安全上下文
     */
    private void setupSecurityContext(CustomUserDetails userDetails) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(context);
    }
}
