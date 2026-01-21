package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.MenuFacade;
import com.xie.glm.admin.vo.MenuVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.MenuDTO;
import com.xie.glm.system.dto.query.MenuQueryDTO;
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
 * 菜单管理控制器
 *
 * <p>提供菜单 CRUD 操作的 REST API。
 *
 * <p>接口列表：
 * <ul>
 *   <li>GET /api/system/menus - 分页查询菜单列表</li>
 *   <li>GET /api/system/menus/list - 查询所有菜单列表</li>
 *   <li>GET /api/system/menus/tree - 构建菜单树</li>
 *   <li>GET /api/system/menus/{id} - 查询菜单详情</li>
 *   <li>POST /api/system/menus - 创建菜单</li>
 *   <li>PUT /api/system/menus - 更新菜单</li>
 *   <li>DELETE /api/system/menus/{id} - 删除菜单</li>
 *   <li>GET /api/system/menus/check-unique - 检查菜单名称唯一性</li>
 * </ul>
 *
 * <p>权限要求：
 * <ul>
 *   <li>system:menu:list - 查看菜单列表</li>
 *   <li>system:menu:query - 查看菜单详情</li>
 *   <li>system:menu:add - 创建菜单</li>
 *   <li>system:menu:edit - 编辑菜单</li>
 *   <li>system:menu:remove - 删除菜单</li>
 * </ul>
 *
 * <p>所有接口返回值由 ResponseAdvice 自动包装为 {@link Result} 格式。
 *
 * @author xie
 */
@Tag(name = "菜单管理", description = "菜单CRUD操作接口")
@RestController
@RequestMapping("/api/system/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuFacade menuFacade;

    /**
     * 分页查询菜单列表
     *
     * @param query 查询条件（菜单名称、菜单类型、状态等）
     * @return 分页结果（包含菜单列表和总记录数），由 ResponseAdvice 自动包装
     */
    @GetMapping
    @Operation(summary = "分页查询菜单列表", description = "支持按菜单名称、菜单类型、状态等条件查询")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public PageResult<MenuVO> list(MenuQueryDTO query) {
        return menuFacade.listMenus(query);
    }

    /**
     * 查询所有菜单列表
     *
     * <p>返回所有菜单的扁平列表，不包含树形结构
     *
     * @return 菜单列表，由 ResponseAdvice 自动包装
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有菜单列表", description = "返回所有菜单的扁平列表")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public List<MenuVO> listAll() {
        return menuFacade.listAllMenus();
    }

    /**
     * 构建菜单树
     *
     * <p>返回菜单的树形结构，用于前端展示
     *
     * @return 树形菜单列表，由 ResponseAdvice 自动包装
     */
    @GetMapping("/tree")
    @Operation(summary = "构建菜单树", description = "返回菜单的树形结构")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public List<MenuVO> tree() {
        return menuFacade.buildMenuTree();
    }

    /**
     * 查询菜单详情
     *
     * @param id 菜单 ID
     * @return 菜单详情，由 ResponseAdvice 自动包装
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询菜单详情", description = "根据菜单ID查询菜单详细信息")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public MenuVO getDetail(@PathVariable Long id) {
        return menuFacade.getMenuById(id);
    }

    /**
     * 创建菜单
     *
     * @param dto 创建菜单 DTO（包含菜单名称、父菜单ID、路由地址等基本信息）
     * @return 创建的菜单 ID，由 ResponseAdvice 自动包装
     */
    @PostMapping
    @Operation(summary = "创建菜单", description = "创建新菜单，支持目录、菜单、按钮三种类型")
    @PreAuthorize("hasAuthority('system:menu:add')")
    public Long create(@Valid @RequestBody MenuDTO dto) {
        return menuFacade.createMenu(dto);
    }

    /**
     * 更新菜单
     *
     * @param dto 更新菜单 DTO（包含需要更新的字段，必须包含 menuId）
     */
    @PutMapping
    @Operation(summary = "更新菜单", description = "更新菜单基本信息")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public void update(@Valid @RequestBody MenuDTO dto) {
        menuFacade.updateMenu(dto);
    }

    /**
     * 删除菜单
     *
     * <p>删除菜单时，会检查是否存在子菜单，存在子菜单时不允许删除
     *
     * @param id 菜单 ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单", description = "根据菜单ID删除菜单，存在子菜单时不允许删除")
    @PreAuthorize("hasAuthority('system:menu:remove')")
    public void delete(@PathVariable Long id) {
        menuFacade.deleteMenu(id);
    }

    /**
     * 检查菜单名称唯一性
     *
     * <p>用于前端表单校验，检查同一父菜单下菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单 ID
     * @return true 表示唯一，false 表示已存在，由 ResponseAdvice 自动包装
     */
    @GetMapping("/check-unique")
    @Operation(summary = "检查菜单名称唯一性", description = "检查同一父菜单下菜单名称是否唯一")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public boolean checkMenuNameUnique(
        @RequestParam String menuName,
        @RequestParam Long parentId
    ) {
        return menuFacade.checkMenuNameUnique(menuName, parentId);
    }
}
