package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.UserVO;
import com.xie.glm.system.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 用户 VO 转换器
 * 负责将 UserDTO 转换为 UserVO（前端展示）
 *
 * <p>转换说明：
 * <ul>
 *   <li>deptName 和 roleNames 需要在 Facade 层手动设置（需要额外查询）</li>
 *   <li>statusText 需要在 Facade 层根据 status 码手动设置</li>
 *   <li>其他字段自动映射</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(componentModel = "spring")
public interface UserVoConverter {

    /**
     * DTO 转 VO
     *
     * <p>将 UserDTO 转换为 UserVO，用于前端展示
     *
     * <p>注意：deptName、roleNames、statusText 需要在 Facade 层手动设置
     *
     * @param dto 用户 DTO
     * @return 用户 VO
     */
    UserVO toVo(UserDTO dto);

    /**
     * DTO 列表转 VO 列表
     *
     * <p>将 UserDTO 列表转换为 UserVO 列表
     *
     * @param dtos 用户 DTO 列表
     * @return 用户 VO 列表
     */
    List<UserVO> toVoList(List<UserDTO> dtos);
}
