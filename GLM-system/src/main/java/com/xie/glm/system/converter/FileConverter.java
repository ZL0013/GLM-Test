package com.xie.glm.system.converter;

import com.xie.glm.common.dto.FileDTO;
import com.xie.glm.system.domain.SysFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 文件转换器
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FileConverter {

    /**
     * Entity 转 DTO
     *
     * @param entity 实体
     * @return DTO
     */
    @Mapping(source = "fileId", target = "id")
    FileDTO toDto(SysFile entity);

    /**
     * DTO 转 Entity
     *
     * @param dto DTO
     * @return 实体
     */
    @Mapping(source = "id", target = "fileId")
    SysFile toEntity(FileDTO dto);

    /**
     * Entity 列表转 DTO 列表
     *
     * @param entities 实体列表
     * @return DTO 列表
     */
    List<FileDTO> toDtoList(List<SysFile> entities);
}
