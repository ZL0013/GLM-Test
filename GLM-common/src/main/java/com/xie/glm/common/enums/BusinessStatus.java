package com.xie.glm.common.enums;

import lombok.Getter;
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
@Getter
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

    /**
     * 未认证或令牌已过期
     */
    UNAUTHORIZED(10001, "未认证或令牌已过期"),

    /**
     * 无权访问
     */
    FORBIDDEN(10002, "无权访问"),

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

    /**
     * 用户名已存在
     */
    USER_NAME_DUPLICATE(11004, "用户名已存在"),

    /**
     * 邮箱已被使用
     */
    USER_EMAIL_DUPLICATE(11005, "邮箱已被使用"),

    /**
     * 手机号已被使用
     */
    USER_PHONE_DUPLICATE(11006, "手机号已被使用"),

    /**
     * 旧密码错误
     */
    USER_OLD_PASSWORD_ERROR(11007, "旧密码错误"),

    // ==================== 角色模块 (12xxx) ====================

    /**
     * 角色不存在
     */
    ROLE_NOT_FOUND(12001, "角色不存在"),

    /**
     * 角色名称已存在
     */
    ROLE_NAME_DUPLICATE(12002, "角色名称已存在"),

    /**
     * 角色权限字符串已存在
     */
    ROLE_KEY_DUPLICATE(12003, "角色权限字符串已存在"),

    // ==================== 菜单模块 (13xxx) ====================

    /**
     * 菜单不存在
     */
    MENU_NOT_FOUND(13001, "菜单不存在"),

    /**
     * 菜单存在子菜单，不允许删除
     */
    MENU_HAS_CHILD(13002, "菜单存在子菜单，不允许删除"),

    /**
     * 菜单名称已存在
     */
    MENU_NAME_DUPLICATE(13003, "菜单名称已存在"),

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

    /**
     * 部门名称已存在
     */
    DEPT_NAME_DUPLICATE(14004, "部门名称已存在"),

    // ==================== 字典模块 (16xxx) ====================

    /**
     * 字典类型已存在
     */
    DICT_TYPE_DUPLICATE(16001, "字典类型已存在"),

    // ==================== 配置模块 (17xxx) ====================

    /**
     * 配置键名已存在
     */
    CONFIG_KEY_DUPLICATE(17001, "配置键名已存在"),

    /**
     * 系统内置配置不允许修改配置键名
     */
    CONFIG_KEY_READONLY(17002, "系统内置配置不允许修改配置键名"),

    /**
     * 系统内置配置不允许删除
     */
    CONFIG_READONLY(17003, "系统内置配置不允许删除"),

    /**
     * 配置ID不能为空
     */
    CONFIG_ID_NULL(17004, "配置ID不能为空"),

    // ==================== 定时任务模块 (18xxx) ====================

    /**
     * 任务名称已存在
     */
    JOB_NAME_DUPLICATE(18001, "任务名称已存在"),

    /**
     * Cron 表达式不能为空
     */
    JOB_CRON_NULL(18002, "Cron 表达式不能为空"),

    /**
     * 调用目标不能为空
     */
    JOB_TARGET_NULL(18003, "调用目标不能为空"),

    /**
     * 任务 ID 不能为空
     */
    JOB_ID_NULL(18004, "任务 ID 不能为空"),

    /**
     * 任务已暂停，无法执行
     */
    JOB_PAUSED(18005, "任务已暂停，无法执行"),

    /**
     * 任务执行功能待实现
     */
    JOB_NOT_IMPLEMENTED(18006, "任务执行功能待实现"),

    // ==================== 通知公告模块 (19xxx) ====================

    /**
     * 通知公告不存在
     */
    NOTICE_NOT_FOUND(19001, "通知公告不存在"),

    // ==================== 操作日志模块 (20xxx) ====================

    /**
     * 操作日志不存在
     */
    OPER_LOG_NOT_FOUND(20001, "操作日志不存在"),

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
}
