package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.RoleVO;
import com.xie.glm.system.dto.RoleDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 角色 VO 转换器
 * 负责将 RoleDTO 转换为 RoleVO（前端展示）
 *
 * <p>转换说明：
 * <ul>
 *   <li>statusText 需要在 Facade 层手动设置（根据 status 码映射）</li>
 *   <li>dataScopeText 需要在 Facade 层手动设置（根据 dataScope 码映射）</li>
 *   <li>其他字段自动映射</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(componentModel = "spring")
public interface RoleVoConverter {

    /**
     * DTO 转 VO
     *
     * <p>将 RoleDTO 转换为 RoleVO，用于前端展示
     *
     * <p>注意：statusText、dataScopeText 需要在 Facade 层手动设置
     *
     * @param dto 角色 DTO
     * @return 角色 VO
     */
    RoleVO toVo(RoleDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * <p>将 RoleDTO 列表转换为 RoleVO 列表
     *
     * @param dtos 角色 DTO 列表
     * @return 角色 VO 列表
     */
    List<RoleVO> toVoList(List<RoleDTO> dtos);
}
