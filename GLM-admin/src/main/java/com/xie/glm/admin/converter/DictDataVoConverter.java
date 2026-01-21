package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.DictDataVO;
import com.xie.glm.system.dto.DictDataDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 字典数据 VO 转换器
 *
 * <p>负责将 DictDataDTO 转换为 DictDataVO（前端展示）
 *
 * <p>转换说明：
 * <ul>
 *   <li>statusText 需要在 Facade 层根据 status 码手动设置</li>
 *   <li>isDefaultText 需要在 Facade 层根据 isDefault 码手动设置</li>
 *   <li>其他字段自动映射</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(componentModel = "spring")
public interface DictDataVoConverter {

    /**
     * DTO 转 VO
     *
     * <p>将 DictDataDTO 转换为 DictDataVO，用于前端展示
     *
     * <p>注意：statusText、isDefaultText 需要在 Facade 层手动设置
     *
     * @param dto 字典数据 DTO
     * @return 字典数据 VO
     */
    DictDataVO toVo(DictDataDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * <p>将 DictDataDTO 列表转换为 DictDataVO 列表
     *
     * @param dtos 字典数据 DTO 列表
     * @return 字典数据 VO 列表
     */
    List<DictDataVO> toVoList(List<DictDataDTO> dtos);
}
