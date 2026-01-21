package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysMenu;
import com.xie.glm.system.dto.MenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 菜单对象转换器
 *
 * <p>基于 MapStruct 1.6.3 的类型安全对象转换器，用于 Entity 和 DTO 之间的转换。
 *
 * <p>转换方法说明：
 * <ul>
 *   <li>toDto：Entity → DTO（用于查询结果返回）</li>
 *   <li>toEntity：DTO → Entity（用于创建/更新菜单）</li>
 *   <li>toDtoList：Entity List → DTO List（用于列表查询）</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MenuConverter {

    /**
     * Entity → DTO 转换
     *
     * <p>用于将数据库查询结果转换为返回给前端的 DTO
     *
     * @param entity 菜单实体
     * @return 菜单 DTO
     */
    MenuDTO toDto(SysMenu entity);

    /**
     * DTO → Entity 转换
     *
     * <p>用于创建/更新菜单时将 DTO 转换为 Entity
     *
     * @param dto 菜单 DTO
     * @return 菜单实体
     */
    SysMenu toEntity(MenuDTO dto);

    /**
     * Entity List → DTO List 转换
     *
     * <p>用于将菜单列表转换为 DTO 列表
     *
     * @param entities 菜单实体列表
     * @return 菜单 DTO 列表
     */
    List<MenuDTO> toDtoList(List<SysMenu> entities);
}
