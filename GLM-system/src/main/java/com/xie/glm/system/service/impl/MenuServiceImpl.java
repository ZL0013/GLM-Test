package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.MenuConverter;
import com.xie.glm.system.domain.SysMenu;
import com.xie.glm.system.dto.MenuDTO;
import com.xie.glm.system.dto.query.MenuQueryDTO;
import com.xie.glm.system.mapper.SysMenuMapper;
import com.xie.glm.system.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 *
 * <p>实现 {@link IMenuService} 接口，提供菜单管理的业务逻辑实现。
 *
 * <p>主要功能：
 * <ul>
 *   <li>菜单 CRUD 操作</li>
 *   <li>树形菜单结构构建</li>
 *   <li>菜单唯一性校验</li>
 *   <li>子菜单检查</li>
 * </ul>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements IMenuService {

    private final SysMenuMapper menuMapper;
    private final MenuConverter menuConverter;

    // ==================== 查询操作 ====================

    @Override
    public PageResult<MenuDTO> listMenus(MenuQueryDTO query) {
        // 构建分页对象
        Page<SysMenu> page = new Page<>(query.getPageNum(), query.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<SysMenu> wrapper = buildQueryWrapper(query);

        // 执行分页查询
        IPage<SysMenu> resultPage = menuMapper.selectPage(page, wrapper);

        // 转换为 DTO
        List<MenuDTO> dtoList = menuConverter.toDtoList(resultPage.getRecords());

        return new PageResult<>(dtoList, resultPage.getTotal());
    }

    @Override
    public List<MenuDTO> listAllMenus() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysMenu::getOrderNum);

        List<SysMenu> menus = menuMapper.selectList(wrapper);
        return menuConverter.toDtoList(menus);
    }

    @Override
    public List<MenuDTO> buildMenuTree() {
        // 查询所有菜单
        List<MenuDTO> allMenus = listAllMenus();

        // 简单实现：返回按 orderNum 排序的菜单列表
        // 树形结构的构建可以在 VO 层或前端完成
        return allMenus;
    }

    @Override
    public MenuDTO getMenuById(Long menuId) {
        SysMenu menu = menuMapper.selectById(menuId);
        if (menu == null) {
            throw new ServiceException(BusinessStatus.MENU_NOT_FOUND);
        }
        return menuConverter.toDto(menu);
    }

    // ==================== 创建操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMenu(MenuDTO dto) {
        // 检查菜单名称唯一性
        if (!checkMenuNameUnique(dto.getMenuName(), dto.getParentId())) {
            throw new ServiceException(BusinessStatus.MENU_NAME_DUPLICATE);
        }

        // 转换 DTO 为 Entity
        SysMenu menu = menuConverter.toEntity(dto);

        // 设置默认值
        if (!StringUtils.hasText(menu.getStatus())) {
            menu.setStatus("0"); // 默认正常状态
        }
        if (!StringUtils.hasText(menu.getVisible())) {
            menu.setVisible("0"); // 默认显示
        }

        // 插入菜单
        menuMapper.insert(menu);

        return menu.getMenuId();
    }

    // ==================== 更新操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuDTO dto) {
        // 检查菜单是否存在
        SysMenu existingMenu = menuMapper.selectById(dto.getMenuId());
        if (existingMenu == null) {
            throw new ServiceException(BusinessStatus.MENU_NOT_FOUND);
        }

        // 如果更新菜单名称或父菜单，检查唯一性
        if (StringUtils.hasText(dto.getMenuName()) || dto.getParentId() != null) {
            String newMenuName = StringUtils.hasText(dto.getMenuName()) ? dto.getMenuName() : existingMenu.getMenuName();
            Long newParentId = dto.getParentId() != null ? dto.getParentId() : existingMenu.getParentId();

            if (!newMenuName.equals(existingMenu.getMenuName()) || !Objects.equals(newParentId, existingMenu.getParentId())) {
                if (!checkMenuNameUnique(newMenuName, newParentId)) {
                    throw new ServiceException(BusinessStatus.MENU_NAME_DUPLICATE);
                }
            }
        }

        // 转换 DTO 为 Entity
        SysMenu menu = menuConverter.toEntity(dto);

        // 更新菜单
        menuMapper.updateById(menu);
    }

    // ==================== 删除操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long menuId) {
        // 检查菜单是否存在
        SysMenu menu = menuMapper.selectById(menuId);
        if (menu == null) {
            throw new ServiceException(BusinessStatus.MENU_NOT_FOUND);
        }

        // 检查是否存在子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, menuId);
        Long childCount = menuMapper.selectCount(wrapper);

        if (childCount > 0) {
            throw new ServiceException(BusinessStatus.MENU_HAS_CHILD);
        }

        // 删除菜单
        menuMapper.deleteById(menuId);
    }

    // ==================== 唯一性校验 ====================

    @Override
    public boolean checkMenuNameUnique(String menuName, Long parentId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getMenuName, menuName);
        if (parentId != null) {
            wrapper.eq(SysMenu::getParentId, parentId);
        }
        return menuMapper.selectCount(wrapper) == 0;
    }

    // ==================== 辅助方法 ====================

    /**
     * 构建查询条件
     *
     * @param query 查询条件 DTO
     * @return LambdaQueryWrapper
     */
    private LambdaQueryWrapper<SysMenu> buildQueryWrapper(MenuQueryDTO query) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();

        // 菜单名称模糊搜索
        if (StringUtils.hasText(query.getMenuName())) {
            wrapper.like(SysMenu::getMenuName, query.getMenuName());
        }

        // 菜单类型精确查询
        if (StringUtils.hasText(query.getMenuType())) {
            wrapper.eq(SysMenu::getMenuType, query.getMenuType());
        }

        // 状态精确查询
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysMenu::getStatus, query.getStatus());
        }

        // 显示状态精确查询
        if (StringUtils.hasText(query.getVisible())) {
            wrapper.eq(SysMenu::getVisible, query.getVisible());
        }

        // 父菜单 ID 精确查询
        if (query.getParentId() != null) {
            wrapper.eq(SysMenu::getParentId, query.getParentId());
        }

        // 排序
        if (StringUtils.hasText(query.getOrderByColumn())) {
            boolean isAsc = !"desc".equalsIgnoreCase(query.getIsAsc());
            wrapper.orderBy(true, isAsc, SysMenu::getOrderNum);
        } else {
            wrapper.orderByAsc(SysMenu::getOrderNum);
        }

        return wrapper;
    }
}
