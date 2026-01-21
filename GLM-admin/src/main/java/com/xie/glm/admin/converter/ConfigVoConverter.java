package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.ConfigVO;
import com.xie.glm.system.dto.ConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 配置 VO 转换器
 *
 * <p>负责将 ConfigDTO 转换为 ConfigVO（前端展示）。
 *
 * <p>转换说明：
 * <ul>
 *   <li>configTypeText 需要在 Facade 层根据 configType 码手动设置</li>
 *   <li>其他字段自动映射</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(componentModel = "spring")
public interface ConfigVoConverter {

    /**
     * DTO 转 VO
     *
     * <p>将 ConfigDTO 转换为 ConfigVO，用于前端展示
     *
     * <p>注意：configTypeText 需要在 Facade 层手动设置
     *
     * @param dto 配置 DTO
     * @return 配置 VO
     */
    @Mapping(target = "configTypeText", ignore = true)
    ConfigVO toVo(ConfigDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * <p>将 ConfigDTO 列表转换为 ConfigVO 列表
     *
     * @param dtos 配置 DTO 列表
     * @return 配置 VO 列表
     */
    @Mapping(target = "configTypeText", ignore = true)
    List<ConfigVO> toVoList(List<ConfigDTO> dtos);
}
