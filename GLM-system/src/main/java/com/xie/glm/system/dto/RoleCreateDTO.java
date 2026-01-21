package com.xie.glm.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建角色 DTO
 *
 * <p>用于创建角色的数据传输对象，包含角色基本信息和菜单权限关联数据。
 *
 * <p>字段说明：
 * <ul>
 *   <li>roleName：角色名称（必填）</li>
 *   <li>roleKey：角色权限字符串（必填）</li>
 *   <li>roleSort：显示顺序（必填）</li>
 *   <li>dataScope：数据范围（选填）</li>
 *   <li>status：角色状态（选填，默认为正常）</li>
 *   <li>menuCheckStrictly：菜单树选择项是否关联显示（选填）</li>
 *   <li>deptCheckStrictly：部门树选择项是否关联显示（选填）</li>
 *   <li>remark：备注（选填）</li>
 *   <li>menuIds：菜单权限 ID 列表（选填，用于关联角色菜单权限）</li>
 * </ul>
 *
 * @author xie
 */
@Data
public class RoleCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 角色基本信息（必填） ====================

    /**
     * 角色名称（必填）
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 30, message = "角色名称长度不能超过 30 个字符")
    private String roleName;

    /**
     * 角色权限字符串（必填）
     *
     * <p>例如：admin, user, guest 等
     */
    @NotBlank(message = "角色权限字符串不能为空")
    @Size(max = 100, message = "角色权限字符串长度不能超过 100 个字符")
    @Pattern(regexp = "^[a-zA-Z0-9_:]+$", message = "角色权限字符串只能包含字母、数字、下划线和冒号")
    private String roleKey;

    /**
     * 显示顺序（必填）
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;

    // ==================== 角色权限配置（选填） ====================

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示
     */
    private Boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示
     */
    private Boolean deptCheckStrictly;

    // ==================== 角色状态（选填） ====================

    /**
     * 角色状态（0=正常，1=停用）
     */
    private String status;

    // ==================== 其他信息（选填） ====================

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过 500 个字符")
    private String remark;

    // ==================== 关联信息 ====================

    /**
     * 菜单权限 ID 列表
     *
     * <p>用于创建角色时关联菜单权限，一个角色可以关联多个菜单
     */
    private List<Long> menuIds;
}
