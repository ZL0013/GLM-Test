package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.RoleFacade;
import com.xie.glm.admin.vo.RoleVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.RoleCreateDTO;
import com.xie.glm.system.dto.RoleUpdateDTO;
import com.xie.glm.system.dto.query.RoleQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理控制器
 *
 * <p>提供角色 CRUD 操作的 REST API。
 *
 * <p>接口列表：
 * <ul>
 *   <li>GET /api/system/roles - 分页查询角色列表</li>
 *   <li>GET /api/system/roles/{id} - 查询角色详情</li>
 *   <li>POST /api/system/roles - 创建角色</li>
 *   <li>PUT /api/system/roles/{id} - 更新角色</li>
 *   <li>DELETE /api/system/roles/{id} - 删除角色</li>
 *   <li>DELETE /api/system/roles - 批量删除角色</li>
 *   <li>PUT /api/system/roles/{id}/status - 修改角色状态</li>
 *   <li>PUT /api/system/roles/{id}/menus - 分配菜单权限</li>
 * </ul>
 *
 * <p>权限要求：
 * <ul>
 *   <li>system:role:list - 查看角色列表</li>
 *   <li>system:role:query - 查看角色详情</li>
 *   <li>system:role:add - 创建角色</li>
 *   <li>system:role:edit - 编辑角色</li>
 *   <li>system:role:remove - 删除角色</li>
 * </ul>
 *
 * <p>所有接口返回值由 ResponseAdvice 自动包装为 {@link Result} 格式。
 *
 * @author xie
 */
@Tag(name = "角色管理", description = "角色CRUD操作接口")
@RestController
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleFacade roleFacade;

    /**
     * 分页查询角色列表
     *
     * @param query 查询条件（角色名称、角色权限字符串、状态等）
     * @return 分页结果（包含角色列表和总记录数），由 ResponseAdvice 自动包装
     */
    @GetMapping
    @Operation(summary = "分页查询角色列表", description = "支持按角色名称、角色权限字符串、状态等条件查询")
    @PreAuthorize("hasAuthority('system:role:list')")
    public PageResult<RoleVO> list(RoleQueryDTO query) {
        return roleFacade.listRoles(query);
    }

    /**
     * 查询角色详情
     *
     * @param id 角色 ID
     * @return 角色详情，由 ResponseAdvice 自动包装
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询角色详情", description = "根据角色ID查询角色详细信息")
    @PreAuthorize("hasAuthority('system:role:query')")
    public RoleVO getDetail(@PathVariable Long id) {
        return roleFacade.getRoleById(id);
    }

    /**
     * 创建角色
     *
     * @param dto 创建角色 DTO（包含角色名称、角色权限字符串、显示顺序等基本信息）
     * @return 创建的角色 ID，由 ResponseAdvice 自动包装
     */
    @PostMapping
    @Operation(summary = "创建角色", description = "创建新角色，可关联菜单权限")
    @PreAuthorize("hasAuthority('system:role:add')")
    public Long create(@Valid @RequestBody RoleCreateDTO dto) {
        return roleFacade.createRole(dto);
    }

    /**
     * 更新角色
     *
     * @param id 角色 ID
     * @param dto 更新角色 DTO（包含需要更新的字段）
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新角色", description = "更新角色基本信息和菜单权限")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public void update(@PathVariable Long id, @Valid @RequestBody RoleUpdateDTO dto) {
        dto.setRoleId(id);
        roleFacade.updateRole(dto);
    }

    /**
     * 删除角色
     *
     * @param id 角色 ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public void delete(@PathVariable Long id) {
        roleFacade.deleteRole(id);
    }

    /**
     * 批量删除角色
     *
     * @param roleIds 角色 ID 数组
     */
    @DeleteMapping
    @Operation(summary = "批量删除角色", description = "根据角色ID数组批量删除角色")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public void deleteBatch(@RequestBody Long[] roleIds) {
        roleFacade.deleteRoles(roleIds);
    }

    /**
     * 修改角色状态
     *
     * @param id 角色 ID
     * @param status 状态（0=正常，1=停用）
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "修改角色状态", description = "启用或停用角色")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        roleFacade.updateStatus(id, status);
    }

    /**
     * 分配菜单权限
     *
     * <p>为角色分配菜单权限，会完全替换现有的菜单权限
     *
     * @param id 角色 ID
     * @param menuIds 菜单权限 ID 数组
     */
    @PutMapping("/{id}/menus")
    @Operation(summary = "分配菜单权限", description = "为角色分配菜单权限，完全替换现有权限")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public void assignMenus(@PathVariable Long id, @RequestBody Long[] menuIds) {
        roleFacade.assignMenus(id, menuIds);
    }
}
