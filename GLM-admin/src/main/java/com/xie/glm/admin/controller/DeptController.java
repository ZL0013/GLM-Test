package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.DeptFacade;
import com.xie.glm.admin.vo.DeptVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.DeptDTO;
import com.xie.glm.system.dto.query.DeptQueryDTO;
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

import java.util.List;

/**
 * 部门管理控制器
 *
 * <p>提供部门 CRUD 操作的 REST API。
 *
 * <p>接口列表：
 * <ul>
 *   <li>GET /api/system/depts - 分页查询部门列表</li>
 *   <li>GET /api/system/depts/list - 查询所有部门列表</li>
 *   <li>GET /api/system/depts/tree - 构建部门树</li>
 *   <li>GET /api/system/depts/{id} - 查询部门详情</li>
 *   <li>POST /api/system/depts - 创建部门</li>
 *   <li>PUT /api/system/depts - 更新部门</li>
 *   <li>DELETE /api/system/depts/{id} - 删除部门</li>
 *   <li>GET /api/system/depts/check-unique - 检查部门名称唯一性</li>
 * </ul>
 *
 * <p>权限要求：
 * <ul>
 *   <li>system:dept:list - 查看部门列表</li>
 *   <li>system:dept:query - 查看部门详情</li>
 *   <li>system:dept:add - 创建部门</li>
 *   <li>system:dept:edit - 编辑部门</li>
 *   <li>system:dept:remove - 删除部门</li>
 * </ul>
 *
 * <p>所有接口返回值由 ResponseAdvice 自动包装为 {@link com.xie.glm.common.core.Result} 格式。
 *
 * @author xie
 */
@Tag(name = "部门管理", description = "部门CRUD操作接口")
@RestController
@RequestMapping("/api/system/depts")
@RequiredArgsConstructor
public class DeptController {

    private final DeptFacade deptFacade;

    /**
     * 分页查询部门列表
     *
     * @param query 查询条件（部门名称、状态、负责人等）
     * @return 分页结果（包含部门列表和总记录数），由 ResponseAdvice 自动包装
     */
    @GetMapping
    @Operation(summary = "分页查询部门列表", description = "支持按部门名称、状态、负责人等条件查询")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public PageResult<DeptVO> list(DeptQueryDTO query) {
        return deptFacade.listDepts(query);
    }

    /**
     * 查询所有部门列表
     *
     * <p>返回所有部门的扁平列表，不包含树形结构
     *
     * @return 部门列表，由 ResponseAdvice 自动包装
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有部门列表", description = "返回所有部门的扁平列表")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public List<DeptVO> listAll() {
        return deptFacade.listAllDepts();
    }

    /**
     * 构建部门树
     *
     * <p>返回部门的树形结构，用于前端展示
     *
     * @return 树形部门列表，由 ResponseAdvice 自动包装
     */
    @GetMapping("/tree")
    @Operation(summary = "构建部门树", description = "返回部门的树形结构")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public List<DeptVO> tree() {
        return deptFacade.buildDeptTree();
    }

    /**
     * 查询部门详情
     *
     * @param id 部门 ID
     * @return 部门详情，由 ResponseAdvice 自动包装
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询部门详情", description = "根据部门ID查询部门详细信息")
    @PreAuthorize("hasAuthority('system:dept:query')")
    public DeptVO getDetail(@PathVariable Long id) {
        return deptFacade.getDeptById(id);
    }

    /**
     * 创建部门
     *
     * @param dto 创建部门 DTO（包含部门名称、父部门ID、负责人等基本信息）
     * @return 创建的部门 ID，由 ResponseAdvice 自动包装
     */
    @PostMapping
    @Operation(summary = "创建部门", description = "创建新部门，支持多级部门结构")
    @PreAuthorize("hasAuthority('system:dept:add')")
    public Long create(@Valid @RequestBody DeptDTO dto) {
        return deptFacade.createDept(dto);
    }

    /**
     * 更新部门
     *
     * @param dto 更新部门 DTO（包含需要更新的字段，必须包含 deptId）
     */
    @PutMapping
    @Operation(summary = "更新部门", description = "更新部门基本信息")
    @PreAuthorize("hasAuthority('system:dept:edit')")
    public void update(@Valid @RequestBody DeptDTO dto) {
        deptFacade.updateDept(dto);
    }

    /**
     * 删除部门
     *
     * <p>删除部门时，会检查是否存在子部门和用户，存在子部门或用户时不允许删除
     *
     * @param id 部门 ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门", description = "根据部门ID删除部门，存在子部门或用户时不允许删除")
    @PreAuthorize("hasAuthority('system:dept:remove')")
    public void delete(@PathVariable Long id) {
        deptFacade.deleteDept(id);
    }

    /**
     * 检查部门名称唯一性
     *
     * <p>用于前端表单校验，检查同一父部门下部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门 ID
     * @return true 表示唯一，false 表示已存在，由 ResponseAdvice 自动包装
     */
    @GetMapping("/check-unique")
    @Operation(summary = "检查部门名称唯一性", description = "检查同一父部门下部门名称是否唯一")
    @PreAuthorize("hasAuthority('system:dept:query')")
    public boolean checkDeptNameUnique(
        @RequestParam String deptName,
        @RequestParam Long parentId
    ) {
        return deptFacade.checkDeptNameUnique(deptName, parentId);
    }
}
