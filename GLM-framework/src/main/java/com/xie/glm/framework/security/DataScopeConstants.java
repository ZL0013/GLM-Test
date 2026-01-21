package com.xie.glm.framework.security;

/**
 * 数据权限常量
 *
 * <p>定义数据权限范围的常量值。
 *
 * @author xie
 */
public final class DataScopeConstants {

    /**
     * 私有构造方法，防止实例化
     */
    private DataScopeConstants() {
        // 工具类，不允许实例化
    }

    /**
     * 全部数据权限
     */
    public static final int DATA_SCOPE_ALL = 1;

    /**
     * 自定义数据权限
     */
    public static final int DATA_SCOPE_CUSTOM = 2;

    /**
     * 本部门数据权限
     */
    public static final int DATA_SCOPE_DEPT = 3;

    /**
     * 本部门及以下数据权限
     */
    public static final int DATA_SCOPE_DEPT_AND_CHILD = 4;

    /**
     * 仅本人数据权限
     */
    public static final int DATA_SCOPE_SELF = 5;
}
