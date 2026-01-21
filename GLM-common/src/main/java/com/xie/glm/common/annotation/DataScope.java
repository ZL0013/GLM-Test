package com.xie.glm.common.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * <p>
 * 用于标记需要进行数据权限过滤的方法，配合 MyBatis Plus 数据权限插件使用。
 * </p>
 *
 * @author xie
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 表别名，用于 SQL 中指定表的别名
     * <p>
     * 例如：如果 SQL 中使用了 "SELECT * FROM sys_user u WHERE ...",
     * 则 alias 应设置为 "u"
     * </p>
     *
     * @return 表别名
     */
    String alias() default "";

    /**
     * 权限字段名，默认为 dept_id
     * <p>
     * 该字段用于数据权限过滤，通常为部门ID字段。
     * 可根据实际业务需求设置为其他字段，如 create_dept_id
     * </p>
     *
     * @return 权限字段名
     */
    String column() default "dept_id";
}
