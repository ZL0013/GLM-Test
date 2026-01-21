package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysRole;
import com.xie.glm.system.dto.RoleCreateDTO;
import com.xie.glm.system.dto.RoleDTO;
import com.xie.glm.system.dto.RoleUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 角色对象转换器
 *
 * <p>基于 MapStruct 1.6.3 的类型安全对象转换器，用于 Entity 和 DTO 之间的转换。
 *
 * <p>转换方法说明：
 * <ul>
 *   <li>toDto：Entity → DTO（用于查询结果返回）</li>
 *   <li>createDtoToEntity：CreateDTO → Entity（用于创建角色）</li>
 *   <li>updateDtoToEntity：UpdateDTO → Entity（用于更新角色）</li>
 *   <li>toDtoList：Entity List → DTO List（用于列表查询）</li>
 * </ul>
 *
 * <p>注意事项：
 * <ul>
 *   <li>菜单权限关联需要单独在 Service 层处理</li>
 *   <li>时间字段自动映射</li>
 *   <li>null 值属性会被忽略（由配置控制）</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RoleConverter {

    /**
     * Entity → DTO 转换
     *
     * <p>用于将数据库查询结果转换为返回给前端的 DTO
     *
     * @param entity 角色实体
     * @return 角色 DTO
     */
    RoleDTO toDto(SysRole entity);

    /**
     * Entity List → DTO List 转换
     *
     * <p>用于将角色列表转换为 DTO 列表
     *
     * @param entities 角色实体列表
     * @return 角色 DTO 列表
     */
    List<RoleDTO> toDtoList(List<SysRole> entities);

    /**
     * CreateDTO → Entity 转换
     *
     * <p>用于创建角色时将 DTO 转换为 Entity
     *
     * <p>注意：
     * <ul>
     *   <li>menuIds 需要在 Service 层单独处理</li>
     *   <li>默认状态会设置为 "0"（正常）</li>
     * </ul>
     *
     * @param dto 创建角色 DTO
     * @return 角色实体
     */
    SysRole createDtoToEntity(RoleCreateDTO dto);

    /**
     * UpdateDTO → Entity 转换
     *
     * <p>用于更新角色时将 DTO 转换为 Entity
     *
     * @param dto 更新角色 DTO
     * @return 角色实体
     */
    SysRole updateDtoToEntity(RoleUpdateDTO dto);

    /**
     * 使用 UpdateDTO 更新现有 Entity
     *
     * <p>用于部分更新角色信息，仅更新 DTO 中非 null 的字段
     *
     * @param dto 更新角色 DTO
     * @param entity 现有角色实体
     */
    void updateEntityFromDto(RoleUpdateDTO dto, @MappingTarget SysRole entity);
}
