package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysConfig;
import com.xie.glm.system.dto.ConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 配置对象转换器
 *
 * <p>基于 MapStruct 1.6.3 的类型安全对象转换器，用于 Entity 和 DTO 之间的转换。
 *
 * <p>转换方法说明：
 * <ul>
 *   <li>toDto：Entity → DTO（用于查询结果返回）</li>
 *   <li>toEntity：DTO → Entity（用于创建和更新配置）</li>
 *   <li>toDtoList：Entity List → DTO List（用于列表查询）</li>
 *   <li>updateEntityFromDto：使用 DTO 更新现有 Entity（用于部分更新）</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ConfigConverter {

    /**
     * Entity → DTO 转换
     *
     * <p>用于将数据库查询结果转换为返回给 Facade 层的 DTO
     *
     * @param entity 配置实体
     * @return 配置 DTO
     */
    ConfigDTO toDto(SysConfig entity);

    /**
     * DTO → Entity 转换
     *
     * <p>用于创建或更新配置时将 DTO 转换为 Entity
     *
     * @param dto 配置 DTO
     * @return 配置实体
     */
    SysConfig toEntity(ConfigDTO dto);

    /**
     * Entity List → DTO List 转换
     *
     * <p>用于将配置列表转换为 DTO 列表
     *
     * @param entities 配置实体列表
     * @return 配置 DTO 列表
     */
    List<ConfigDTO> toDtoList(List<SysConfig> entities);

    /**
     * 使用 DTO 更新现有 Entity
     *
     * <p>用于部分更新配置信息，仅更新 DTO 中非 null 的字段
     *
     * @param dto 配置 DTO
     * @param entity 现有配置实体
     */
    void updateEntityFromDto(ConfigDTO dto, @MappingTarget SysConfig entity);
}
