package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.OperLogVO;
import com.xie.glm.system.dto.OperLogDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 操作日志 VO 转换器
 *
 * <p>负责将 OperLogDTO 转换为 OperLogVO（前端展示）。
 *
 * <p>转换说明：
 * <ul>
 *   <li>statusText 需要在 Facade 层根据 status 码手动设置</li>
 *   <li>businessTypeText 需要在 Facade 层根据 businessType 码手动设置</li>
 *   <li>其他字段自动映射</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(componentModel = "spring")
public interface OperLogVoConverter {

    /**
     * DTO 转 VO
     *
     * <p>将 OperLogDTO 转换为 OperLogVO，用于前端展示
     *
     * <p>注意：statusText 和 businessTypeText 需要在 Facade 层手动设置
     *
     * @param dto 操作日志 DTO
     * @return 操作日志 VO
     */
    OperLogVO toVo(OperLogDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * <p>将 OperLogDTO 列表转换为 OperLogVO 列表
     *
     * @param dtos 操作日志 DTO 列表
     * @return 操作日志 VO 列表
     */
    List<OperLogVO> toVoList(List<OperLogDTO> dtos);
}
