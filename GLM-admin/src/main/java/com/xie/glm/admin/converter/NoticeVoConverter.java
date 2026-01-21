package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.NoticeVO;
import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import com.xie.glm.system.dto.NoticeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

/**
 * 通知公告 VO 转换器
 *
 * @author xie
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoticeVoConverter {

    /**
     * DTO 转 VO
     *
     * @param dto DTO 对象
     * @return VO 对象
     */
    @Mapping(target = "noticeTypeName", source = "noticeType", qualifiedByName = "typeToName")
    @Mapping(target = "statusName", source = "status", qualifiedByName = "statusToName")
    NoticeVO toVo(NoticeDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * @param dtos DTO 列表
     * @return VO 列表
     */
    @Mapping(target = "noticeTypeName", source = "noticeType", qualifiedByName = "typeToName")
    @Mapping(target = "statusName", source = "status", qualifiedByName = "statusToName")
    List<NoticeVO> toVoList(List<NoticeDTO> dtos);

    /**
     * 通知类型转名称
     *
     * @param type 通知类型
     * @return 类型名称
     */
    @Named("typeToName")
    default String typeToName(NoticeType type) {
        if (type == null) {
            return null;
        }
        return type.getDisplayName();
    }

    /**
     * 通知状态转名称
     *
     * @param status 通知状态
     * @return 状态名称
     */
    @Named("statusToName")
    default String statusToName(NoticeStatus status) {
        if (status == null) {
            return null;
        }
        return status.getDisplayName();
    }
}
