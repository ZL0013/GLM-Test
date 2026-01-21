package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysJob;
import com.xie.glm.system.dto.JobDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 定时任务对象转换器
 *
 * <p>基于 MapStruct 1.6.3 的类型安全对象转换器，用于 Entity 和 DTO 之间的转换。
 *
 * <p>转换方法说明：
 * <ul>
 *   <li>toDto：Entity → DTO（用于查询结果返回）</li>
 *   <li>toEntity：DTO → Entity（用于创建/更新任务）</li>
 *   <li>toDtoList：Entity List → DTO List（用于列表查询）</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface JobConverter {

    /**
     * Entity → DTO 转换
     *
     * <p>用于将数据库查询结果转换为返回给前端的 DTO
     *
     * @param entity 任务实体
     * @return 任务 DTO
     */
    JobDTO toDto(SysJob entity);

    /**
     * DTO → Entity 转换
     *
     * <p>用于创建/更新任务时将 DTO 转换为 Entity
     *
     * @param dto 任务 DTO
     * @return 任务实体
     */
    SysJob toEntity(JobDTO dto);

    /**
     * Entity List → DTO List 转换
     *
     * <p>用于将任务列表转换为 DTO 列表
     *
     * @param entities 任务实体列表
     * @return 任务 DTO 列表
     */
    List<JobDTO> toDtoList(List<SysJob> entities);

    /**
     * 使用 DTO 更新现有 Entity
     *
     * <p>用于部分更新任务信息，仅更新 DTO 中非 null 的字段
     *
     * @param dto 任务 DTO
     * @param entity 现有任务实体
     */
    void updateEntityFromDto(JobDTO dto, @MappingTarget SysJob entity);
}
