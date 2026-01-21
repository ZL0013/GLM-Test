package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.FileVO;
import com.xie.glm.common.dto.FileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 文件 VO 转换器
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FileVoConverter {

    /**
     * DTO 转 VO
     *
     * @param dto DTO
     * @return VO
     */
    @Mapping(target = "fileSizeFormatted", expression = "java(formatFileSize(dto.fileSize()))")
    FileVO toVo(FileDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * @param dtos DTO 列表
     * @return VO 列表
     */
    List<FileVO> toVoList(List<FileDTO> dtos);

    /**
     * 格式化文件大小
     *
     * @param size 文件大小（字节）
     * @return 格式化后的字符串
     */
    default String formatFileSize(Long size) {
        if (size == null) {
            return "0 B";
        }

        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double fileSize = size.doubleValue();

        while (fileSize >= 1024 && unitIndex < units.length - 1) {
            fileSize /= 1024;
            unitIndex++;
        }

        return String.format("%.1f %s", fileSize, units[unitIndex]);
    }
}
