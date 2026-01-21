package com.xie.glm.framework.security;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static com.xie.glm.framework.security.DataScopeConstants.*;

/**
 * 数据权限处理器
 *
 * <p>实现 MyBatis Plus 的 DataPermissionHandler 接口，根据用户的数据权限范围
 * 自动构造 SQL WHERE 条件，实现数据级别的权限控制。
 *
 * <p>数据权限范围说明：
 * <ul>
 *   <li>全部数据权限（DATA_SCOPE_ALL=1）：不添加任何过滤条件，可查看所有数据</li>
 *   <li>自定义数据权限（DATA_SCOPE_CUSTOM=2）：根据自定义部门列表过滤数据</li>
 *   <li>本部门数据权限（DATA_SCOPE_DEPT=3）：仅查看本部门数据</li>
 *   <li>本部门及以下数据权限（DATA_SCOPE_DEPT_AND_CHILD=4）：查看本部门及子部门数据</li>
 *   <li>仅本人数据权限（DATA_SCOPE_SELF=5）：仅查看本人创建的数据</li>
 * </ul>
 *
 * <p>使用说明：
 * <ol>
 *   <li>在 MyBatis Plus 配置中添加此处理器为内部拦截器</li>
 *   <li>在 Mapper 方法上使用 {@link com.xie.glm.common.annotation.DataScope} 注解指定表别名和权限字段</li>
 *   <li>用户登录时，CustomUserDetails 中需包含 deptId、dataScope、dataScopeDeptIds 字段</li>
 * </ol>
 *
 * <p>SQL 条件构造规则：
 * <ul>
 *   <li>已存在 WHERE 条件时，使用 AND 连接新条件</li>
 *   <li>默认使用 dept_id 字段进行过滤（可通过 @DataScope 注解修改）</li>
 *   <li>仅本人数据权限使用 user_id 字段进行过滤</li>
 * </ul>
 *
 * <p>设计原则：
 * <ul>
 *   <li>符合宪法第一条：简单性原则，仅实现必要的数据权限过滤功能</li>
 *   <li>符合宪法第三条：明确性原则，清晰注释各数据权限范围的含义</li>
 *   <li>符合宪法第四条：遵循 Spring Security 约定，从 SecurityContext 获取用户信息</li>
 * </ul>
 *
 * @author xie
 */
public class DataPermissionHandlerImpl implements DataPermissionHandler {

    private static final Logger log = LoggerFactory.getLogger(DataPermissionHandlerImpl.class);

    /**
     * 默认权限字段名（部门ID）
     */
    private static final String DEFAULT_PERMISSION_COLUMN = "dept_id";

    /**
     * 用户ID字段名（用于仅本人数据权限）
     */
    private static final String USER_ID_COLUMN = "user_id";

    /**
     * 获取数据权限 SQL 片段
     *
     * <p>根据当前用户的数据权限范围，构造相应的 SQL WHERE 条件表达式。
     *
     * @param whereExpression    已存在的 WHERE 条件表达式（可能为 null）
     * @param mappedStatementId MyBatis MappedStatement ID（暂未使用）
     * @return SQL WHERE 条件表达式，返回 null 表示不添加任何条件
     */
    @Override
    public Expression getSqlSegment(Expression whereExpression, String mappedStatementId) {
        // 获取当前登录用户
        CustomUserDetails userDetails = getCurrentUserDetails();
        if (userDetails == null) {
            log.debug("未获取到当前用户信息，不添加数据权限条件");
            return null;
        }

        // 检查是否为管理员角色
        if (isAdmin(userDetails)) {
            log.debug("当前用户为管理员，不添加数据权限条件");
            return null;
        }

        // 获取数据权限范围
        Integer dataScope = userDetails.getDataScope();
        if (dataScope == null) {
            log.debug("用户数据权限范围为空，不添加数据权限条件");
            return null;
        }

        // 根据数据权限范围构造 SQL 条件
        Expression newExpression = buildDataScopeExpression(userDetails, dataScope);

        // 如果已存在 WHERE 条件，使用 AND 连接
        if (whereExpression == null) {
            return newExpression;
        }

        return new AndExpression(whereExpression, newExpression);
    }

    /**
     * 构造数据权限 SQL 表达式
     *
     * @param userDetails 当前用户详情
     * @param dataScope   数据权限范围
     * @return SQL 表达式
     */
    private Expression buildDataScopeExpression(CustomUserDetails userDetails, Integer dataScope) {
        return switch (dataScope) {
            case DATA_SCOPE_ALL -> {
                log.debug("数据权限范围：全部数据，不添加条件");
                yield null;
            }
            case DATA_SCOPE_CUSTOM -> buildCustomExpression(userDetails);
            case DATA_SCOPE_DEPT -> buildDeptExpression(userDetails);
            case DATA_SCOPE_DEPT_AND_CHILD -> buildDeptAndChildExpression(userDetails);
            case DATA_SCOPE_SELF -> buildSelfExpression(userDetails);
            default -> {
                log.warn("未知的数据权限范围：{}，不添加条件", dataScope);
                yield null;
            }
        };
    }

    /**
     * 构造自定义数据权限表达式（IN 查询）
     *
     * @param userDetails 当前用户详情
     * @return IN 表达式
     */
    private Expression buildCustomExpression(CustomUserDetails userDetails) {
        List<Long> deptIds = userDetails.getDataScopeDeptIds();
        if (deptIds == null || deptIds.isEmpty()) {
            log.warn("自定义数据权限部门列表为空，不添加条件");
            return null;
        }

        log.debug("数据权限范围：自定义部门，部门ID列表：{}", deptIds);
        return buildInExpression(DEFAULT_PERMISSION_COLUMN, deptIds);
    }

    /**
     * 构造本部门数据权限表达式（等于查询）
     *
     * @param userDetails 当前用户详情
     * @return 等于表达式
     */
    private Expression buildDeptExpression(CustomUserDetails userDetails) {
        Long deptId = userDetails.getDeptId();
        if (deptId == null) {
            log.warn("用户部门ID为空，不添加本部门数据权限条件");
            return null;
        }

        log.debug("数据权限范围：本部门，部门ID：{}", deptId);
        return buildEqualsExpression(DEFAULT_PERMISSION_COLUMN, deptId);
    }

    /**
     * 构造本部门及以下数据权限表达式（IN 查询）
     *
     * <p>注意：此实现假设用户已获取到子部门ID列表。
     * 实际应用中，子部门列表需要在用户登录时预先加载到 dataScopeDeptIds 中。
     *
     * @param userDetails 当前用户详情
     * @return IN 表达式
     */
    private Expression buildDeptAndChildExpression(CustomUserDetails userDetails) {
        List<Long> deptIds = userDetails.getDataScopeDeptIds();
        if (deptIds == null || deptIds.isEmpty()) {
            // 如果没有子部门列表，降级为本部门数据权限
            return buildDeptExpression(userDetails);
        }

        log.debug("数据权限范围：本部门及以下，部门ID列表：{}", deptIds);
        return buildInExpression(DEFAULT_PERMISSION_COLUMN, deptIds);
    }

    /**
     * 构造仅本人数据权限表达式（等于查询）
     *
     * @param userDetails 当前用户详情
     * @return 等于表达式
     */
    private Expression buildSelfExpression(CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        if (userId == null) {
            log.warn("用户ID为空，不添加仅本人数据权限条件");
            return null;
        }

        log.debug("数据权限范围：仅本人，用户ID：{}", userId);
        return buildEqualsExpression(USER_ID_COLUMN, userId);
    }

    /**
     * 构造等于表达式
     *
     * @param columnName 字段名
     * @param value      值
     * @return EqualsTo 表达式
     */
    private EqualsTo buildEqualsExpression(String columnName, Long value) {
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column(columnName));
        equalsTo.setRightExpression(new LongValue(value));
        return equalsTo;
    }

    /**
     * 构造 IN 表达式
     *
     * @param columnName 字段名
     * @param values     值列表
     * @return InExpression 表达式
     */
    private InExpression buildInExpression(String columnName, List<Long> values) {
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(new Column(columnName));

        // 构造值列表表达式
        List<Expression> valueExpressions = new ArrayList<>();
        for (Long value : values) {
            valueExpressions.add(new LongValue(value));
        }

        // 使用 RowConstructor 包装值列表
        RowConstructor rowConstructor = new RowConstructor();
        rowConstructor.addExpressions(valueExpressions);
        inExpression.setRightExpression(rowConstructor);

        return inExpression;
    }

    /**
     * 获取当前登录用户详情
     *
     * @return CustomUserDetails，未登录返回 null
     */
    private CustomUserDetails getCurrentUserDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }

    /**
     * 判断用户是否为管理员
     *
     * <p>管理员拥有所有数据权限，不进行数据过滤。
     *
     * @param userDetails 用户详情
     * @return true=管理员, false=非管理员
     */
    private boolean isAdmin(CustomUserDetails userDetails) {
        List<String> roles = userDetails.getRoles();
        if (roles == null) {
            return false;
        }
        return roles.stream().anyMatch("admin"::equalsIgnoreCase);
    }
}
