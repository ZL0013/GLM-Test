package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.RoleConverter;
import com.xie.glm.system.domain.SysRole;
import com.xie.glm.system.dto.RoleCreateDTO;
import com.xie.glm.system.dto.RoleDTO;
import com.xie.glm.system.dto.RoleUpdateDTO;
import com.xie.glm.system.dto.query.RoleQueryDTO;
import com.xie.glm.system.mapper.SysRoleMapper;
import com.xie.glm.system.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 角色服务实现类
 *
 * <p>实现 {@link IRoleService} 接口，提供角色管理的业务逻辑实现。
 *
 * <p>主要功能：
 * <ul>
 *   <li>角色 CRUD 操作</li>
 *   <li>角色权限分配</li>
 *   <li>角色状态管理</li>
 *   <li>唯一性校验</li>
 * </ul>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final SysRoleMapper roleMapper;
    private final RoleConverter roleConverter;

    // ==================== 查询操作 ====================

    @Override
    public PageResult<RoleDTO> listRoles(RoleQueryDTO query) {
        // 构建分页对象
        Page<SysRole> page = new Page<>(query.getPageNum(), query.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        // 角色名称模糊搜索
        if (StringUtils.hasText(query.getRoleName())) {
            wrapper.like(SysRole::getRoleName, query.getRoleName());
        }

        // 角色权限字符串模糊搜索
        if (StringUtils.hasText(query.getRoleKey())) {
            wrapper.like(SysRole::getRoleKey, query.getRoleKey());
        }

        // 状态精确查询
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysRole::getStatus, query.getStatus());
        }

        // 时间范围查询
        if (query.getStartTime() != null) {
            wrapper.ge(SysRole::getCreateTime, query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le(SysRole::getCreateTime, query.getEndTime());
        }

        // 排序
        wrapper.orderByAsc(SysRole::getRoleSort);

        // 执行分页查询
        IPage<SysRole> resultPage = roleMapper.selectPage(page, wrapper);

        // 转换为 DTO
        List<RoleDTO> dtoList = roleConverter.toDtoList(resultPage.getRecords());

        return new PageResult<>(dtoList, resultPage.getTotal());
    }

    @Override
    public RoleDTO getRoleById(Long roleId) {
        SysRole role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new ServiceException(BusinessStatus.ROLE_NOT_FOUND);
        }
        return roleConverter.toDto(role);
    }

    @Override
    public RoleDTO getRoleByName(String roleName) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName, roleName);
        SysRole role = roleMapper.selectOne(wrapper);
        return role != null ? roleConverter.toDto(role) : null;
    }

    @Override
    public RoleDTO getRoleByKey(String roleKey) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleKey, roleKey);
        SysRole role = roleMapper.selectOne(wrapper);
        return role != null ? roleConverter.toDto(role) : null;
    }

    // ==================== 创建操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleCreateDTO dto) {
        // 检查角色名称唯一性
        if (!checkRoleNameUnique(dto.getRoleName())) {
            throw new ServiceException(BusinessStatus.ROLE_NAME_DUPLICATE);
        }

        // 检查角色权限字符串唯一性
        if (!checkRoleKeyUnique(dto.getRoleKey())) {
            throw new ServiceException(BusinessStatus.ROLE_KEY_DUPLICATE);
        }

        // 转换 DTO 为 Entity
        SysRole role = roleConverter.createDtoToEntity(dto);

        // 设置默认值
        if (!StringUtils.hasText(role.getStatus())) {
            role.setStatus("0"); // 默认正常状态
        }

        // 插入角色
        roleMapper.insert(role);

        // TODO: 处理菜单权限关联（需要 SysRoleMenuMapper）

        return role.getRoleId();
    }

    // ==================== 更新操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleUpdateDTO dto) {
        // 检查角色是否存在
        SysRole existingRole = roleMapper.selectById(dto.getRoleId());
        if (existingRole == null) {
            throw new ServiceException(BusinessStatus.ROLE_NOT_FOUND);
        }

        // 如果更新角色名称，检查唯一性
        if (StringUtils.hasText(dto.getRoleName()) &&
            !dto.getRoleName().equals(existingRole.getRoleName())) {
            if (!checkRoleNameUnique(dto.getRoleName())) {
                throw new ServiceException(BusinessStatus.ROLE_NAME_DUPLICATE);
            }
        }

        // 如果更新角色权限字符串，检查唯一性
        if (StringUtils.hasText(dto.getRoleKey()) &&
            !dto.getRoleKey().equals(existingRole.getRoleKey())) {
            if (!checkRoleKeyUnique(dto.getRoleKey())) {
                throw new ServiceException(BusinessStatus.ROLE_KEY_DUPLICATE);
            }
        }

        // 转换 DTO 为 Entity
        SysRole role = roleConverter.updateDtoToEntity(dto);

        // 更新角色
        roleMapper.updateById(role);

        // 如果提供了菜单权限，更新菜单权限关联
        if (dto.getMenuIds() != null) {
            // TODO: 更新菜单权限关联（需要 SysRoleMenuMapper）
        }
    }

    @Override
    public void updateStatus(Long roleId, String status) {
        SysRole role = new SysRole();
        role.setRoleId(roleId);
        role.setStatus(status);
        roleMapper.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(Long roleId, Long[] menuIds) {
        // 检查角色是否存在
        SysRole role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new ServiceException(BusinessStatus.ROLE_NOT_FOUND);
        }

        // TODO: 删除现有菜单权限关联（需要 SysRoleMenuMapper）

        // TODO: 插入新的菜单权限关联（需要 SysRoleMenuMapper）
    }

    // ==================== 删除操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        // 检查角色是否存在
        SysRole role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new ServiceException(BusinessStatus.ROLE_NOT_FOUND);
        }

        // 逻辑删除
        role.setDelFlag("2");
        roleMapper.updateById(role);

        // TODO: 删除角色菜单关联（需要 SysRoleMenuMapper）

        // TODO: 删除用户角色关联（需要 SysUserRoleMapper）
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoles(Long[] roleIds) {
        Arrays.stream(roleIds).forEach(this::deleteRole);
    }

    // ==================== 唯一性校验 ====================

    @Override
    public boolean checkRoleNameUnique(String roleName) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName, roleName);
        return roleMapper.selectCount(wrapper) == 0;
    }

    @Override
    public boolean checkRoleKeyUnique(String roleKey) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleKey, roleKey);
        return roleMapper.selectCount(wrapper) == 0;
    }
}
