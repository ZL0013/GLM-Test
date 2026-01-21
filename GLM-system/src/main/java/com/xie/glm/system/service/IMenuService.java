package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.MenuDTO;
import com.xie.glm.system.dto.query.MenuQueryDTO;

import java.util.List;

/**
 * 菜单服务接口
 *
 * <p>定义菜单管理的业务逻辑方法，包括菜单的增删改查、树形结构构建等功能。
 *
 * <p>方法说明：
 * <ul>
 *   <li>分页查询菜单列表</li>
 *   <li>查询所有菜单列表</li>
 *   <li>构建树形菜单结构</li>
 *   <li>根据 ID 查询菜单详情</li>
 *   <li>创建菜单</li>
 *   <li>更新菜单信息</li>
 *   <li>删除菜单</li>
 *   <li>检查菜单名称唯一性</li>
 * </ul>
 *
 * @author xie
 */
public interface IMenuService {

    /**
     * 分页查询菜单列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<MenuDTO> listMenus(MenuQueryDTO query);

    /**
     * 查询所有菜单列表
     *
     * @return 菜单列表
     */
    List<MenuDTO> listAllMenus();

    /**
     * 构建树形菜单结构
     *
     * <p>将菜单列表转换为树形结构，顶级菜单（parentId=0）为根节点
     *
     * @return 树形菜单列表
     */
    List<MenuDTO> buildMenuTree();

    /**
     * 根据 ID 查询菜单详情
     *
     * @param menuId 菜单 ID
     * @return 菜单 DTO
     * @throws ServiceException 如果菜单不存在
     */
    MenuDTO getMenuById(Long menuId);

    /**
     * 创建菜单
     *
     * <p>创建菜单时：
     * <ul>
     *   <li>会检查菜单名称在同一父菜单下的唯一性</li>
     *   <li>会自动设置排序字段</li>
     * </ul>
     *
     * @param dto 菜单 DTO
     * @return 创建的菜单 ID
     * @throws ServiceException 如果菜单名称已存在
     */
    Long createMenu(MenuDTO dto);

    /**
     * 更新菜单信息
     *
     * @param dto 菜单 DTO
     * @throws ServiceException 如果菜单不存在或菜单名称已存在
     */
    void updateMenu(MenuDTO dto);

    /**
     * 删除菜单
     *
     * <p>删除菜单时：
     * <ul>
     *   <li>会检查是否存在子菜单</li>
     *   <li>存在子菜单时不允许删除</li>
     * </ul>
     *
     * @param menuId 菜单 ID
     * @throws ServiceException 如果菜单不存在或存在子菜单
     */
    void deleteMenu(Long menuId);

    /**
     * 检查菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单 ID
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkMenuNameUnique(String menuName, Long parentId);
}
