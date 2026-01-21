package com.xie.glm.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色数据传输对象
 *
 * <p>用于服务层返回给前端展示的角色数据，包含角色基本信息和菜单权限列表。
 *
 * <p>字段说明：
 * <ul>
 *   <li>roleId：角色 ID</li>
 *   <li>roleName：角色名称</li>
 *   <li>roleKey：角色权限字符串</li>
 *   <li>roleSort：显示顺序</li>
 *   <li>dataScope：数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）</li>
 *   <li>status：角色状态（0=正常，1=停用）</li>
 *   <li>menuCheckStrictly：菜单树选择项是否关联显示</li>
 *   <li>deptCheckStrictly：部门树选择项是否关联显示</li>
 *   <li>remark：备注</li>
 *   <li>createTime：创建时间</li>
 *   <li>updateTime：更新时间</li>
 *   <li>menuIds：菜单权限 ID 列表</li>
 * </ul>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 角色基本信息 ====================

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    // ==================== 角色权限配置 ====================

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

    // ==================== 角色状态 ====================

    /**
     * 角色状态（0=正常，1=停用）
     */
    private String status;

    // ==================== 其他信息 ====================

    /**
     * 备注
     */
    private String remark;

    // ==================== 时间字段 ====================

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // ==================== 菜单权限列表 ====================

    /**
     * 菜单权限 ID 列表
     *
     * <p>该角色拥有的菜单权限 ID 列表，用于前端展示和权限控制
     */
    private List<Long> menuIds;
}
