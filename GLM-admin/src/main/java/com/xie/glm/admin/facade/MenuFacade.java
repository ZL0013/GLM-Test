package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.MenuVoConverter;
import com.xie.glm.admin.vo.MenuVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.MenuDTO;
import com.xie.glm.system.dto.query.MenuQueryDTO;
import com.xie.glm.system.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单门面类
 *
 * <p>封装菜单管理的业务逻辑调用，负责 DTO → VO 的转换。
 *
 * <p>职责：
 * <ul>
 *   <li>调用 Service 层获取 DTO 数据</li>
 *   <li>使用 MenuVoConverter 将 DTO 转换为 VO</li>
 *   <li>构建菜单树形结构</li>
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
 * MenuDTO (系统管理)              MenuVO (系统管理)
 *   ├── children: List<MenuDTO>    ├── children: List<MenuVO>
 *   │   ├── MenuDTO (用户管理)     │   ├── MenuVO (用户管理)
 *   │   └── MenuDTO (角色管理)     │   └── MenuVO (角色管理)
 * </pre>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class MenuFacade {

    private final IMenuService menuService;
    private final MenuVoConverter voConverter;

    /**
     * 分页查询菜单列表
     *
     * @param query 查询条件
     * @return 分页结果（VO）
     */
    public PageResult<MenuVO> listMenus(MenuQueryDTO query) {
        // 调用 Service 获取 DTO 分页数据
        PageResult<MenuDTO> dtoPage = menuService.listMenus(query);

        // 转换 DTO 为 VO
        List<MenuVO> voList = voConverter.toVoList(dtoPage.getRecords());

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 查询所有菜单列表
     *
     * @return 菜单 VO 列表
     */
    public List<MenuVO> listAllMenus() {
        // 调用 Service 获取所有菜单
        List<MenuDTO> dtoList = menuService.listAllMenus();

        // 转换 DTO 为 VO
        return voConverter.toVoList(dtoList);
    }

    /**
     * 构建菜单树
     *
     * <p>从 Service 获取扁平菜单列表，然后在 Facade 层构建树形结构。
     * <p>Service 层返回的是按 orderNum 排序的扁平列表，Facade 负责构建层级关系。
     *
     * @return 菜单 VO 树
     */
    public List<MenuVO> buildMenuTree() {
        // 获取 Service 层返回的扁平菜单列表
        List<MenuDTO> flatMenuList = menuService.buildMenuTree();

        // 转换 DTO 为 VO
        List<MenuVO> flatVoList = voConverter.toVoList(flatMenuList);

        // 构建 VO 树形结构
        return buildVoTree(flatVoList, 0L);
    }

    /**
     * 根据 ID 查询菜单详情
     *
     * @param menuId 菜单 ID
     * @return 菜单 VO
     */
    public MenuVO getMenuById(Long menuId) {
        // 调用 Service 获取 DTO
        MenuDTO dto = menuService.getMenuById(menuId);

        // 转换 DTO 为 VO
        return voConverter.toVo(dto);
    }

    /**
     * 创建菜单
     *
     * <p>创建菜单时：
     * <ul>
     *   <li>Service 层会进行唯一性校验</li>
     *   <li>会自动设置排序字段</li>
     * </ul>
     *
     * @param dto 菜单 DTO
     * @return 创建的菜单 ID
     */
    public Long createMenu(MenuDTO dto) {
        return menuService.createMenu(dto);
    }

    /**
     * 更新菜单信息
     *
     * @param dto 菜单 DTO
     */
    public void updateMenu(MenuDTO dto) {
        menuService.updateMenu(dto);
    }

    /**
     * 删除菜单
     *
     * <p>删除菜单时：
     * <ul>
     *   <li>Service 层会检查是否存在子菜单</li>
     *   <li>存在子菜单时不允许删除</li>
     * </ul>
     *
     * @param menuId 菜单 ID
     */
    public void deleteMenu(Long menuId) {
        menuService.deleteMenu(menuId);
    }

    /**
     * 检查菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单 ID
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkMenuNameUnique(String menuName, Long parentId) {
        return menuService.checkMenuNameUnique(menuName, parentId);
    }

    /**
     * 构建 VO 树形结构
     *
     * <p>将扁平的 VO 列表转换为树形结构。
     * <p>通过 parentId 关系递归查找子节点。
     *
     * @param flatList 扁平 VO 列表
     * @param parentId 父菜单 ID（0L=顶级菜单）
     * @return 树形 VO 列表
     */
    private List<MenuVO> buildVoTree(List<MenuVO> flatList, Long parentId) {
        if (flatList == null || flatList.isEmpty()) {
            return new ArrayList<>();
        }

        return flatList.stream()
            .filter(menu -> parentId.equals(menu.getParentId()))
            .map(menu -> {
                // 递归查找子节点
                List<MenuVO> children = buildVoTree(flatList, menu.getMenuId());
                menu.setChildren(children.isEmpty() ? null : children);
                return menu;
            })
            .collect(Collectors.toList());
    }
}
