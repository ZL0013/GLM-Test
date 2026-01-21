package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.MenuVO;
import com.xie.glm.system.dto.MenuDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 菜单 VO 转换器
 * 负责将 MenuDTO 转换为 MenuVO（前端展示）
 *
 * <p>转换说明：
 * <ul>
 *   <li>children 字段不会自动映射，需要在 Facade 层手动构建树形结构</li>
 *   <li>其他字段自动映射</li>
 * </ul>
 *
 * <p>树形结构构建示例：
 * <pre>
 * // 1. 转换 DTO 列表为 VO 列表
 * List&lt;MenuVO&gt; menuVOs = menuVoConverter.toVoList(menuDTOs);
 *
 * // 2. 在 Facade 层构建树形结构
 * List&lt;MenuVO&gt; tree = buildMenuTree(menuVOs, 0L);
 * </pre>
 *
 * @author xie
 */
@Mapper(componentModel = "spring")
public interface MenuVoConverter {

    /**
     * DTO 转 VO
     *
     * <p>将 MenuDTO 转换为 MenuVO，用于前端展示
     *
     * <p>注意：children 字段需要在 Facade 层手动构建
     *
     * @param dto 菜单 DTO
     * @return 菜单 VO
     */
    MenuVO toVo(MenuDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * <p>将 MenuDTO 列表转换为 MenuVO 列表
     *
     * @param dtos 菜单 DTO 列表
     * @return 菜单 VO 列表
     */
    List<MenuVO> toVoList(List<MenuDTO> dtos);
}
