package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.RoleVoConverter;
import com.xie.glm.admin.vo.RoleVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.RoleCreateDTO;
import com.xie.glm.system.dto.RoleDTO;
import com.xie.glm.system.dto.RoleUpdateDTO;
import com.xie.glm.system.dto.query.RoleQueryDTO;
import com.xie.glm.system.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色门面类
 *
 * <p>封装角色管理的业务逻辑调用，负责 DTO → VO 的转换。
 *
 * <p>职责：
 * <ul>
 *   <li>调用 Service 层获取 DTO 数据</li>
 *   <li>使用 RoleVoConverter 将 DTO 转换为 VO</li>
 *   <li>组装展示数据（如状态文本、数据范围文本）</li>
 *   <li>简化 Controller 的逻辑</li>
 * </ul>
 *
 * <p>数据流向：
 * <pre>
 * Controller → Facade → Service → Mapper
 *     ↓         ↓         ↓
 *   VO  ←  VO  ←  DTO
 * </pre>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class RoleFacade {

    private final IRoleService roleService;
    private final RoleVoConverter voConverter;

    /**
     * 分页查询角色列表
     *
     * @param query 查询条件
     * @return 分页结果（VO）
     */
    public PageResult<RoleVO> listRoles(RoleQueryDTO query) {
        // 调用 Service 获取 DTO 分页数据
        PageResult<RoleDTO> dtoPage = roleService.listRoles(query);

        // 转换 DTO 为 VO，并设置展示文本
        List<RoleVO> voList = voConverter.toVoList(dtoPage.getRecords());
        voList.forEach(this::enrichRoleVO);

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 根据 ID 查询角色详情
     *
     * @param roleId 角色 ID
     * @return 角色 VO
     */
    public RoleVO getRoleById(Long roleId) {
        // 调用 Service 获取 DTO
        RoleDTO dto = roleService.getRoleById(roleId);

        // 转换 DTO 为 VO，并设置展示文本
        RoleVO vo = voConverter.toVo(dto);
        enrichRoleVO(vo);

        return vo;
    }

    /**
     * 创建角色
     *
     * <p>创建角色时：
     * <ul>
     *   <li>Service 层会进行唯一性校验</li>
     *   <li>会关联指定的菜单权限</li>
     *   <li>会设置默认状态为正常</li>
     * </ul>
     *
     * @param dto 创建角色 DTO
     * @return 创建的角色 ID
     */
    public Long createRole(RoleCreateDTO dto) {
        return roleService.createRole(dto);
    }

    /**
     * 更新角色信息
     *
     * @param dto 更新角色 DTO
     */
    public void updateRole(RoleUpdateDTO dto) {
        roleService.updateRole(dto);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色 ID
     */
    public void deleteRole(Long roleId) {
        roleService.deleteRole(roleId);
    }

    /**
     * 批量删除角色
     *
     * @param roleIds 角色 ID 数组
     */
    public void deleteRoles(Long[] roleIds) {
        roleService.deleteRoles(roleIds);
    }

    /**
     * 更新角色状态
     *
     * @param roleId 角色 ID
     * @param status 状态（0=正常，1=停用）
     */
    public void updateStatus(Long roleId, String status) {
        roleService.updateStatus(roleId, status);
    }

    /**
     * 分配菜单权限
     *
     * <p>为角色分配菜单权限，会完全替换现有的菜单权限
     *
     * @param roleId 角色 ID
     * @param menuIds 菜单权限 ID 数组
     */
    public void assignMenus(Long roleId, Long[] menuIds) {
        roleService.assignMenus(roleId, menuIds);
    }

    /**
     * 检查角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkRoleNameUnique(String roleName) {
        return roleService.checkRoleNameUnique(roleName);
    }

    /**
     * 检查角色权限字符串是否唯一
     *
     * @param roleKey 角色权限字符串
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkRoleKeyUnique(String roleKey) {
        return roleService.checkRoleKeyUnique(roleKey);
    }

    /**
     * 富化角色 VO 信息
     *
     * <p>设置状态文本和数据范围文本，用于前端展示
     *
     * @param vo 角色 VO
     */
    private void enrichRoleVO(RoleVO vo) {
        if (vo == null) {
            return;
        }
        // 设置状态文本
        vo.setStatusText(getStatusText(vo.getStatus()));
        // 设置数据范围文本
        vo.setDataScopeText(getDataScopeText(vo.getDataScope()));
    }

    /**
     * 获取状态文本
     *
     * @param status 状态码
     * @return 状态文本
     */
    private String getStatusText(String status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case "0" -> "正常";
            case "1" -> "停用";
            default -> "未知";
        };
    }

    /**
     * 获取数据范围文本
     *
     * @param dataScope 数据范围码
     * @return 数据范围文本
     */
    private String getDataScopeText(String dataScope) {
        if (dataScope == null) {
            return "自定义数据权限";
        }
        return switch (dataScope) {
            case "1" -> "全部数据权限";
            case "2" -> "自定义数据权限";
            case "3" -> "本部门数据权限";
            case "4" -> "本部门及以下数据权限";
            default -> "自定义数据权限";
        };
    }
}
