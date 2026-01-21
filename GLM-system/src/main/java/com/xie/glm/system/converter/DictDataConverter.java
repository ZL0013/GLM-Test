package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysDictData;
import com.xie.glm.system.dto.DictDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 字典数据对象转换器
 *
 * <p>基于 MapStruct 1.6.3 的类型安全对象转换器，用于 Entity 和 DTO 之间的转换。
 *
 * <p>转换方法说明：
 * <ul>
 *   <li>toDto：Entity → DTO（用于查询结果返回）</li>
 *   <li>dtoToEntity：DTO → Entity（用于创建/更新）</li>
 *   <li>toDtoList：Entity List → DTO List（用于列表查询）</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DictDataConverter {

    /**
     * Entity → DTO 转换
     *
     * <p>用于将数据库查询结果转换为返回给前端的 DTO
     *
     * @param entity 字典数据实体
     * @return 字典数据 DTO
     */
    DictDataDTO toDto(SysDictData entity);

    /**
     * Entity List → DTO List 转换
     *
     * <p>用于将字典数据列表转换为 DTO 列表
     *
     * @param entities 字典数据实体列表
     * @return 字典数据 DTO 列表
     */
    List<DictDataDTO> toDtoList(List<SysDictData> entities);

    /**
     * DTO → Entity 转换
     *
     * <p>用于创建字典数据时将 DTO 转换为 Entity
     *
     * @param dto 字典数据 DTO
     * @return 字典数据实体
     */
    SysDictData dtoToEntity(DictDataDTO dto);

    /**
     * 使用 DTO 更新现有 Entity
     *
     * <p>用于部分更新字典数据信息，仅更新 DTO 中非 null 的字段
     *
     * @param dto 字典数据 DTO
     * @param entity 现有字典数据实体
     */
    void updateEntityFromDto(DictDataDTO dto, @MappingTarget SysDictData entity);
}
