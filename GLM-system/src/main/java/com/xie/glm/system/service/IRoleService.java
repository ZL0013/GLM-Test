package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.RoleCreateDTO;
import com.xie.glm.system.dto.RoleDTO;
import com.xie.glm.system.dto.RoleUpdateDTO;
import com.xie.glm.system.dto.query.RoleQueryDTO;

/**
 * 角色服务接口
 *
 * <p>定义角色管理的业务逻辑方法，包括角色的增删改查、权限分配等功能。
 *
 * <p>方法说明：
 * <ul>
 *   <li>分页查询角色列表</li>
 *   <li>根据 ID 查询角色详情</li>
 *   <li>创建角色（含菜单权限关联）</li>
 *   <li>更新角色信息（含菜单权限关联）</li>
 *   <li>删除角色</li>
 *   <li>批量删除角色</li>
 *   <li>更新角色状态</li>
 *   <li>分配菜单权限</li>
 *   <li>数据权限范围设置</li>
 * </ul>
 *
 * @author xie
 */
public interface IRoleService {

    /**
     * 分页查询角色列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<RoleDTO> listRoles(RoleQueryDTO query);

    /**
     * 根据 ID 查询角色详情
     *
     * @param roleId 角色 ID
     * @return 角色 DTO，如果不存在返回 null
     */
    RoleDTO getRoleById(Long roleId);

    /**
     * 根据角色名称查询角色
     *
     * @param roleName 角色名称
     * @return 角色 DTO，如果不存在返回 null
     */
    RoleDTO getRoleByName(String roleName);

    /**
     * 根据角色权限字符串查询角色
     *
     * @param roleKey 角色权限字符串
     * @return 角色 DTO，如果不存在返回 null
     */
    RoleDTO getRoleByKey(String roleKey);

    /**
     * 创建角色
     *
     * <p>创建角色时：
     * <ul>
     *   <li>会检查角色名称和权限字符串的唯一性</li>
     *   <li>会关联指定的菜单权限</li>
     *   <li>会设置默认状态为正常</li>
     * </ul>
     *
     * @param dto 创建角色 DTO
     * @return 创建的角色 ID
     */
    Long createRole(RoleCreateDTO dto);

    /**
     * 更新角色信息
     *
     * <p>更新角色时：
     * <ul>
     *   <li>仅更新 DTO 中非 null 的字段</li>
     *   <li>会更新菜单权限关联（如果提供）</li>
     *   <li>不允许修改角色 ID</li>
     * </ul>
     *
     * @param dto 更新角色 DTO
     */
    void updateRole(RoleUpdateDTO dto);

    /**
     * 删除角色
     *
     * <p>逻辑删除，将 del_flag 设置为 "2"
     *
     * @param roleId 角色 ID
     */
    void deleteRole(Long roleId);

    /**
     * 批量删除角色
     *
     * <p>逻辑删除，将 del_flag 设置为 "2"
     *
     * @param roleIds 角色 ID 列表
     */
    void deleteRoles(Long[] roleIds);

    /**
     * 更新角色状态
     *
     * <p>启用或停用角色
     *
     * @param roleId 角色 ID
     * @param status 状态（0=正常，1=停用）
     */
    void updateStatus(Long roleId, String status);

    /**
     * 分配菜单权限
     *
     * <p>为角色分配菜单权限，会完全替换现有的菜单权限
     *
     * @param roleId 角色 ID
     * @param menuIds 菜单权限 ID 列表
     */
    void assignMenus(Long roleId, Long[] menuIds);

    /**
     * 检查角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkRoleNameUnique(String roleName);

    /**
     * 检查角色权限字符串是否唯一
     *
     * @param roleKey 角色权限字符串
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkRoleKeyUnique(String roleKey);
}
