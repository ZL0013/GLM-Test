package com.xie.glm.common.enums;

import java.io.Serial;
import java.util.Arrays;

/**
 * 业务状态码枚举
 *
 * <p>业务错误码采用三段式枚举：`系统级别(1位) + 模块(2位) + 错误类型(2位)`
 *
 * <p>状态码分段：
 * <ul>
 *   <li>0：成功</li>
 *   <li>1：通用失败</li>
   *   <li>10xxx：系统模块</li>
   <li>11xxx：用户模块</li>
   *   <li>12xxx：角色模块</li>
   *   <li>13xxx：菜单模块</li>
   <li>14xxx：部门模块</li>
   *   <li>15xxx：工具类模块</li>
 * </ul>
 *
 * @author xie
 */
public enum BusinessStatus {

    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 失败
     */
    ERROR(1, "失败"),

    // ==================== 系统模块 (10xxx) ====================

    /**
     * 系统异常
     */
    SYSTEM_ERROR(10000, "系统异常"),

    // ==================== 用户模块 (11xxx) ====================

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(11001, "用户不存在"),

    /**
     * 密码错误
     */
    USER_PASSWORD_ERROR(11002, "密码错误"),

    /**
     * 账号已禁用
     */
    USER_ACCOUNT_DISABLED(11003, "账号已禁用"),

    // ==================== 角色模块 (12xxx) ====================

    /**
     * 角色不存在
     */
    ROLE_NOT_FOUND(12001, "角色不存在"),

    /**
     * 角色名称已存在
     */
    ROLE_NAME_DUPLICATE(12002, "角色名称已存在"),

    // ==================== 菜单模块 (13xxx) ====================

    /**
     * 菜单不存在
     */
    MENU_NOT_FOUND(13001, "菜单不存在"),

    /**
     * 菜单存在子菜单，不允许删除
     */
    MENU_HAS_CHILD(13002, "菜单存在子菜单，不允许删除"),

    // ==================== 部门模块 (14xxx) ====================

    /**
     * 部门不存在
     */
    DEPT_NOT_FOUND(14001, "部门不存在"),

    /**
     * 部门存在子部门，不允许删除
     */
    DEPT_HAS_CHILD(14002, "部门存在子部门，不允许删除"),

    /**
     * 部门存在用户，不允许删除
     */
    DEPT_HAS_USER(14003, "部门存在用户，不允许删除"),

    // ==================== 工具类模块 (15xxx) ====================

    /**
     * 文件参数为空
     */
    FILE_PARAM_NULL(15001, "文件参数不能为空"),

    /**
     * 文件不存在
     */
    FILE_NOT_FOUND(15002, "文件不存在"),

    /**
     * 参数不能为空
     */
    PARAM_NULL(15003, "参数不能为空"),

    /**
     * Excel 导出失败
     */
    EXCEL_EXPORT_FAILED(15004, "Excel 导出失败"),

    /**
     * Excel 导入失败
     */
    EXCEL_IMPORT_FAILED(15005, "Excel 导入失败");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态描述
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    状态码
     * @param message 状态描述
     */
    BusinessStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据 code 获取枚举
     *
     * @param code 状态码
     * @return 对应的枚举值，不存在则返回 null
     */
    public static BusinessStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(status -> status.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    /**
     * 判断是否成功
     *
     * @return true=成功, false=失败
     */
    public boolean isSuccess() {
        return SUCCESS == this;
    }

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取状态描述
     *
     * @return 状态描述
     */
    public String getMessage() {
        return message;
    }
}
