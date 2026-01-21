package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.DeptDTO;
import com.xie.glm.system.dto.query.DeptQueryDTO;

import java.util.List;

/**
 * 部门服务接口
 *
 * <p>定义部门管理的业务逻辑方法，包括部门的增删改查、树形结构构建等功能。
 *
 * <p>方法说明：
 * <ul>
 *   <li>分页查询部门列表</li>
 *   <li>查询所有部门列表</li>
 *   <li>构建树形部门结构</li>
 *   <li>根据 ID 查询部门详情</li>
 *   <li>创建部门</li>
 *   <li>更新部门信息</li>
 *   <li>删除部门</li>
 *   <li>检查部门名称唯一性</li>
 * </ul>
 *
 * @author xie
 */
public interface IDeptService {

    /**
     * 分页查询部门列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<DeptDTO> listDepts(DeptQueryDTO query);

    /**
     * 查询所有部门列表
     *
     * @return 部门列表
     */
    List<DeptDTO> listAllDepts();

    /**
     * 构建树形部门结构
     *
     * <p>将部门列表转换为树形结构，顶级部门（parentId=0）为根节点
     *
     * @return 树形部门列表
     */
    List<DeptDTO> buildDeptTree();

    /**
     * 根据 ID 查询部门详情
     *
     * @param deptId 部门 ID
     * @return 部门 DTO
     * @throws com.xie.glm.common.exception.ServiceException 如果部门不存在
     */
    DeptDTO getDeptById(Long deptId);

    /**
     * 创建部门
     *
     * <p>创建部门时：
     * <ul>
     *   <li>会检查部门名称在同一父部门下的唯一性</li>
     *   <li>会自动设置 ancestors 字段（祖级列表）</li>
     * </ul>
     *
     * @param dto 部门 DTO
     * @return 创建的部门 ID
     * @throws com.xie.glm.common.exception.ServiceException 如果部门名称已存在
     */
    Long createDept(DeptDTO dto);

    /**
     * 更新部门信息
     *
     * @param dto 部门 DTO
     * @throws com.xie.glm.common.exception.ServiceException 如果部门不存在或部门名称已存在
     */
    void updateDept(DeptDTO dto);

    /**
     * 删除部门
     *
     * <p>删除部门时：
     * <ul>
     *   <li>会检查是否存在子部门</li>
     *   <li>会检查是否存在用户</li>
     *   <li>存在子部门或用户时不允许删除</li>
     * </ul>
     *
     * @param deptId 部门 ID
     * @throws com.xie.glm.common.exception.ServiceException 如果部门不存在、存在子部门或存在用户
     */
    void deleteDept(Long deptId);

    /**
     * 检查部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门 ID
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkDeptNameUnique(String deptName, Long parentId);
}
