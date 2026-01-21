package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysUser;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 用户对象转换器
 *
 * <p>基于 MapStruct 1.6.3 的类型安全对象转换器，用于 Entity 和 DTO 之间的转换。
 *
 * <p>转换方法说明：
 * <ul>
 *   <li>toDto：Entity → DTO（用于查询结果返回）</li>
 *   <li>createDtoToEntity：CreateDTO → Entity（用于创建用户）</li>
 *   <li>updateDtoToEntity：UpdateDTO → Entity（用于更新用户）</li>
 *   <li>toDtoList：Entity List → DTO List（用于列表查询）</li>
 * </ul>
 *
 * <p>注意事项：
 * <ul>
 *   <li>密码字段不会从 Entity 转换到 DTO（安全性）</li>
 *   <li>角色和岗位关联需要单独在 Service 层处理</li>
 *   <li>时间字段自动映射</li>
 * </ul>
 *
 * @author xie
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserConverter {

    /**
     * Entity → DTO 转换
     *
     * <p>用于将数据库查询结果转换为返回给前端的 DTO
     *
     * @param entity 用户实体
     * @return 用户 DTO
     */
    UserDTO toDto(SysUser entity);

    /**
     * Entity List → DTO List 转换
     *
     * <p>用于将用户列表转换为 DTO 列表
     *
     * @param entities 用户实体列表
     * @return 用户 DTO 列表
     */
    List<UserDTO> toDtoList(List<SysUser> entities);

    /**
     * CreateDTO → Entity 转换
     *
     * <p>用于创建用户时将 DTO 转换为 Entity
     *
     * <p>注意：
     * <ul>
     *   <li>密码字段会被转换（明文，后续在 Service 层进行 BCrypt 加密）</li>
     *   <li>roleIds 和 postIds 需要在 Service 层单独处理</li>
     * </ul>
     *
     * @param dto 创建用户 DTO
     * @return 用户实体
     */
    SysUser createDtoToEntity(UserCreateDTO dto);

    /**
     * UpdateDTO → Entity 转换
     *
     * <p>用于更新用户时将 DTO 转换为 Entity
     *
     * @param dto 更新用户 DTO
     * @return 用户实体
     */
    SysUser updateDtoToEntity(UserUpdateDTO dto);

    /**
     * 使用 UpdateDTO 更新现有 Entity
     *
     * <p>用于部分更新用户信息，仅更新 DTO 中非 null 的字段
     *
     * @param dto 更新用户 DTO
     * @param entity 现有用户实体
     */
    void updateEntityFromDto(UserUpdateDTO dto, @MappingTarget SysUser entity);
}
