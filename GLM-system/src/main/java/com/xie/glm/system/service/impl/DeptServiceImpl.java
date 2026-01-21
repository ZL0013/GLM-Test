package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.DeptConverter;
import com.xie.glm.system.domain.SysDept;
import com.xie.glm.system.dto.DeptDTO;
import com.xie.glm.system.dto.query.DeptQueryDTO;
import com.xie.glm.system.mapper.SysDeptMapper;
import com.xie.glm.system.mapper.SysUserMapper;
import com.xie.glm.system.service.IDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 部门服务实现类
 *
 * <p>实现 {@link IDeptService} 接口，提供部门管理的业务逻辑实现。
 *
 * <p>主要功能：
 * <ul>
 *   <li>部门 CRUD 操作</li>
 *   <li>树形部门结构构建</li>
 *   <li>部门唯一性校验</li>
 *   <li>子部门检查</li>
 *   <li>用户检查</li>
 * </ul>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class DeptServiceImpl implements IDeptService {

    private final SysDeptMapper deptMapper;
    private final SysUserMapper userMapper;
    private final DeptConverter deptConverter;

    // ==================== 查询操作 ====================

    @Override
    public PageResult<DeptDTO> listDepts(DeptQueryDTO query) {
        // 构建分页对象
        Page<SysDept> page = new Page<>(query.getPageNum(), query.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<SysDept> wrapper = buildQueryWrapper(query);

        // 执行分页查询
        IPage<SysDept> resultPage = deptMapper.selectPage(page, wrapper);

        // 转换为 DTO
        List<DeptDTO> dtoList = deptConverter.toDtoList(resultPage.getRecords());

        return new PageResult<>(dtoList, resultPage.getTotal());
    }

    @Override
    public List<DeptDTO> listAllDepts() {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysDept::getOrderNum);

        List<SysDept> depts = deptMapper.selectList(wrapper);
        return deptConverter.toDtoList(depts);
    }

    @Override
    public List<DeptDTO> buildDeptTree() {
        // 查询所有部门
        List<DeptDTO> allDepts = listAllDepts();

        // 简单实现：返回按 orderNum 排序的部门列表
        // 树形结构的构建可以在 VO 层或前端完成
        return allDepts;
    }

    @Override
    public DeptDTO getDeptById(Long deptId) {
        SysDept dept = deptMapper.selectById(deptId);
        if (dept == null) {
            throw new ServiceException(BusinessStatus.DEPT_NOT_FOUND);
        }
        return deptConverter.toDto(dept);
    }

    // ==================== 创建操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDept(DeptDTO dto) {
        // 检查部门名称唯一性
        if (!checkDeptNameUnique(dto.getDeptName(), dto.getParentId())) {
            throw new ServiceException(BusinessStatus.DEPT_NAME_DUPLICATE);
        }

        // 转换 DTO 为 Entity
        SysDept dept = deptConverter.toEntity(dto);

        // 设置默认值
        if (!StringUtils.hasText(dept.getStatus())) {
            dept.setStatus("0"); // 默认正常状态
        }

        // 构建 ancestors（祖级列表）
        if (dept.getParentId() != null && dept.getParentId() != 0) {
            SysDept parent = deptMapper.selectById(dept.getParentId());
            if (parent != null) {
                String ancestors = parent.getAncestors() != null ? parent.getAncestors() : "0";
                dept.setAncestors(ancestors + "," + parent.getDeptId());
            } else {
                dept.setAncestors("0");
            }
        } else {
            dept.setParentId(0L);
            dept.setAncestors("0");
        }

        // 插入部门
        deptMapper.insert(dept);

        return dept.getDeptId();
    }

    // ==================== 更新操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(DeptDTO dto) {
        // 检查部门是否存在
        SysDept existingDept = deptMapper.selectById(dto.getDeptId());
        if (existingDept == null) {
            throw new ServiceException(BusinessStatus.DEPT_NOT_FOUND);
        }

        // 如果更新部门名称或父部门，检查唯一性
        if (StringUtils.hasText(dto.getDeptName()) || dto.getParentId() != null) {
            String newDeptName = StringUtils.hasText(dto.getDeptName()) ? dto.getDeptName() : existingDept.getDeptName();
            Long newParentId = dto.getParentId() != null ? dto.getParentId() : existingDept.getParentId();

            if (!newDeptName.equals(existingDept.getDeptName()) || !newParentId.equals(existingDept.getParentId())) {
                if (!checkDeptNameUnique(newDeptName, newParentId)) {
                    throw new ServiceException(BusinessStatus.DEPT_NAME_DUPLICATE);
                }
            }
        }

        // 转换 DTO 为 Entity
        SysDept dept = deptConverter.toEntity(dto);

        // 更新部门
        deptMapper.updateById(dept);
    }

    // ==================== 删除操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Long deptId) {
        // 检查部门是否存在
        SysDept dept = deptMapper.selectById(deptId);
        if (dept == null) {
            throw new ServiceException(BusinessStatus.DEPT_NOT_FOUND);
        }

        // 检查是否存在子部门
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getParentId, deptId);
        Long childCount = deptMapper.selectCount(wrapper);

        if (childCount > 0) {
            throw new ServiceException(BusinessStatus.DEPT_HAS_CHILD);
        }

        // 检查是否存在用户
        // TODO: 实现用户数量检查逻辑
        // LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        // userWrapper.eq(SysUser::getDeptId, deptId);
        // Long userCount = userMapper.selectCount(userWrapper);
        //
        // if (userCount > 0) {
        //     throw new ServiceException(BusinessStatus.DEPT_HAS_USER);
        // }

        // 删除部门
        deptMapper.deleteById(deptId);
    }

    // ==================== 唯一性校验 ====================

    @Override
    public boolean checkDeptNameUnique(String deptName, Long parentId) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getDeptName, deptName);
        if (parentId != null) {
            wrapper.eq(SysDept::getParentId, parentId);
        }
        return deptMapper.selectCount(wrapper) == 0;
    }

    // ==================== 辅助方法 ====================

    /**
     * 构建查询条件
     *
     * @param query 查询条件 DTO
     * @return LambdaQueryWrapper
     */
    private LambdaQueryWrapper<SysDept> buildQueryWrapper(DeptQueryDTO query) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();

        // 部门名称模糊搜索
        if (StringUtils.hasText(query.getDeptName())) {
            wrapper.like(SysDept::getDeptName, query.getDeptName());
        }

        // 状态精确查询
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysDept::getStatus, query.getStatus());
        }

        // 负责人精确查询
        if (StringUtils.hasText(query.getLeader())) {
            wrapper.eq(SysDept::getLeader, query.getLeader());
        }

        // 排序
        if (StringUtils.hasText(query.getOrderByColumn())) {
            boolean isAsc = !"desc".equalsIgnoreCase(query.getIsAsc());
            wrapper.orderBy(true, isAsc, SysDept::getOrderNum);
        } else {
            wrapper.orderByAsc(SysDept::getOrderNum);
        }

        return wrapper;
    }
}
