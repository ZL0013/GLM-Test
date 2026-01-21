package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.DeptVoConverter;
import com.xie.glm.admin.vo.DeptVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.DeptDTO;
import com.xie.glm.system.dto.query.DeptQueryDTO;
import com.xie.glm.system.service.IDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门门面类
 *
 * <p>封装部门管理的业务逻辑调用，负责 DTO → VO 的转换。
 *
 * <p>职责：
 * <ul>
 *   <li>调用 Service 层获取 DTO 数据</li>
 *   <li>使用 DeptVoConverter 将 DTO 转换为 VO</li>
 *   <li>构建部门树形结构</li>
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
 * <p>树形结构构建：
 * <pre>
 * Service 返回的 DTO 树 → Facade 转换为 VO 树 → Controller 返回
 *
 * DTO 树:                         VO 树:
 * DeptDTO (公司)                  DeptVO (公司)
 *   ├── children: List<DeptDTO>    ├── children: List<DeptVO>
 *   │   ├── DeptDTO (研发部)       │   ├── DeptVO (研发部)
 *   │   └── DeptDTO (市场部)       │   └── DeptVO (市场部)
 * </pre>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class DeptFacade {

    private final IDeptService deptService;
    private final DeptVoConverter voConverter;

    /**
     * 分页查询部门列表
     *
     * @param query 查询条件
     * @return 分页结果（VO）
     */
    public PageResult<DeptVO> listDepts(DeptQueryDTO query) {
        // 调用 Service 获取 DTO 分页数据
        PageResult<DeptDTO> dtoPage = deptService.listDepts(query);

        // 转换 DTO 为 VO
        List<DeptVO> voList = voConverter.toVoList(dtoPage.getRecords());

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 查询所有部门列表
     *
     * @return 部门 VO 列表
     */
    public List<DeptVO> listAllDepts() {
        // 调用 Service 获取所有部门
        List<DeptDTO> dtoList = deptService.listAllDepts();

        // 转换 DTO 为 VO
        return voConverter.toVoList(dtoList);
    }

    /**
     * 构建部门树
     *
     * <p>从 Service 获取扁平部门列表，然后在 Facade 层构建树形结构。
     * <p>Service 层返回的是按 orderNum 排序的扁平列表，Facade 负责构建层级关系。
     *
     * @return 部门 VO 树
     */
    public List<DeptVO> buildDeptTree() {
        // 获取 Service 层返回的扁平部门列表
        List<DeptDTO> flatDeptList = deptService.buildDeptTree();

        // 转换 DTO 为 VO
        List<DeptVO> flatVoList = voConverter.toVoList(flatDeptList);

        // 构建 VO 树形结构
        return buildVoTree(flatVoList, 0L);
    }

    /**
     * 根据 ID 查询部门详情
     *
     * @param deptId 部门 ID
     * @return 部门 VO
     */
    public DeptVO getDeptById(Long deptId) {
        // 调用 Service 获取 DTO
        DeptDTO dto = deptService.getDeptById(deptId);

        // 转换 DTO 为 VO
        return voConverter.toVo(dto);
    }

    /**
     * 创建部门
     *
     * <p>创建部门时：
     * <ul>
     *   <li>Service 层会进行唯一性校验</li>
     *   <li>会自动设置 ancestors 字段</li>
     * </ul>
     *
     * @param dto 部门 DTO
     * @return 创建的部门 ID
     */
    public Long createDept(DeptDTO dto) {
        return deptService.createDept(dto);
    }

    /**
     * 更新部门信息
     *
     * @param dto 部门 DTO
     */
    public void updateDept(DeptDTO dto) {
        deptService.updateDept(dto);
    }

    /**
     * 删除部门
     *
     * <p>删除部门时：
     * <ul>
     *   <li>Service 层会检查是否存在子部门</li>
     *   <li>Service 层会检查是否存在用户</li>
     *   <li>存在子部门或用户时不允许删除</li>
     * </ul>
     *
     * @param deptId 部门 ID
     */
    public void deleteDept(Long deptId) {
        deptService.deleteDept(deptId);
    }

    /**
     * 检查部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门 ID
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkDeptNameUnique(String deptName, Long parentId) {
        return deptService.checkDeptNameUnique(deptName, parentId);
    }

    /**
     * 构建 VO 树形结构
     *
     * <p>将扁平的 VO 列表转换为树形结构。
     * <p>通过 parentId 关系递归查找子节点。
     *
     * @param flatList 扁平 VO 列表
     * @param parentId 父部门 ID（0L=顶级部门）
     * @return 树形 VO 列表
     */
    private List<DeptVO> buildVoTree(List<DeptVO> flatList, Long parentId) {
        if (flatList == null || flatList.isEmpty()) {
            return new ArrayList<>();
        }

        return flatList.stream()
            .filter(dept -> parentId.equals(dept.getParentId()))
            .map(dept -> {
                // 递归查找子节点
                List<DeptVO> children = buildVoTree(flatList, dept.getDeptId());
                dept.setChildren(children.isEmpty() ? null : children);
                return dept;
            })
            .collect(Collectors.toList());
    }
}
