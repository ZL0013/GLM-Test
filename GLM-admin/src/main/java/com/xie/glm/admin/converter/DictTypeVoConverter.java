package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.DictTypeVO;
import com.xie.glm.system.dto.DictTypeDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 字典类型 VO 转换器
 *
 * <p>负责将 DictTypeDTO 转换为 DictTypeVO（前端展示）
 *
 * <p>转换说明：
 * <ul>
 *   <li>statusText 需要在 Facade 层根据 status 码手动设置</li>
 *   <li>其他字段自动映射</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(componentModel = "spring")
public interface DictTypeVoConverter {

    /**
     * DTO 转 VO
     *
     * <p>将 DictTypeDTO 转换为 DictTypeVO，用于前端展示
     *
     * <p>注意：statusText 需要在 Facade 层手动设置
     *
     * @param dto 字典类型 DTO
     * @return 字典类型 VO
     */
    DictTypeVO toVo(DictTypeDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * <p>将 DictTypeDTO 列表转换为 DictTypeVO 列表
     *
     * @param dtos 字典类型 DTO 列表
     * @return 字典类型 VO 列表
     */
    List<DictTypeVO> toVoList(List<DictTypeDTO> dtos);
}
