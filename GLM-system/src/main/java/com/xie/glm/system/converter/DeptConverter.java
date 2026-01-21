package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysDept;
import com.xie.glm.system.dto.DeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 部门对象转换器
 *
 * <p>基于 MapStruct 1.6.3 的类型安全对象转换器，用于 Entity 和 DTO 之间的转换。
 *
 * <p>转换方法说明：
 * <ul>
 *   <li>toDto：Entity → DTO（用于查询结果返回）</li>
 *   <li>toEntity：DTO → Entity（用于创建/更新部门）</li>
 *   <li>toDtoList：Entity List → DTO List（用于列表查询）</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DeptConverter {

    /**
     * Entity → DTO 转换
     *
     * <p>用于将数据库查询结果转换为返回给前端的 DTO
     *
     * @param entity 部门实体
     * @return 部门 DTO
     */
    DeptDTO toDto(SysDept entity);

    /**
     * DTO → Entity 转换
     *
     * <p>用于创建/更新部门时将 DTO 转换为 Entity
     *
     * @param dto 部门 DTO
     * @return 部门实体
     */
    SysDept toEntity(DeptDTO dto);

    /**
     * Entity List → DTO List 转换
     *
     * <p>用于将部门列表转换为 DTO 列表
     *
     * @param entities 部门实体列表
     * @return 部门 DTO 列表
     */
    List<DeptDTO> toDtoList(List<SysDept> entities);
}
