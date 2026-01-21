package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysNotice;
import com.xie.glm.system.dto.NoticeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * 通知公告转换器
 *
 * @author xie
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoticeConverter {

    /**
     * Entity 转 DTO
     *
     * @param entity 实体对象
     * @return DTO 对象
     */
    @Mapping(target = "createBy", source = "createdBy")
    @Mapping(target = "updateBy", source = "updatedBy")
    NoticeDTO toDto(SysNotice entity);

    /**
     * DTO 转 Entity
     *
     * @param dto DTO 对象
     * @return 实体对象
     */
    @Mapping(target = "createdBy", source = "createBy")
    @Mapping(target = "updatedBy", source = "updateBy")
    SysNotice toEntity(NoticeDTO dto);

    /**
     * Entity 列表转 DTO 列表
     *
     * @param entities 实体列表
     * @return DTO 列表
     */
    @Mapping(target = "createBy", source = "createdBy")
    @Mapping(target = "updateBy", source = "updatedBy")
    List<NoticeDTO> toDtoList(List<SysNotice> entities);
}
