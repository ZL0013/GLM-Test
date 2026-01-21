package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import com.xie.glm.system.dto.query.UserQueryDTO;

/**
 * 用户服务接口
 *
 * <p>定义用户管理的业务逻辑方法，包括用户的增删改查、密码重置等功能。
 *
 * <p>方法说明：
 * <ul>
 *   <li>分页查询用户列表</li>
 *   <li>根据 ID 查询用户详情</li>
 *   <li>创建用户（含角色和岗位关联）</li>
 *   <li>更新用户信息（含角色和岗位关联）</li>
 *   <li>删除用户（逻辑删除）</li>
 *   <li>重置用户密码</li>
 *   <li>修改用户密码</li>
 *   <li>更新用户状态</li>
 * </ul>
 *
 * @author xie
 */
public interface IUserService {

    /**
     * 分页查询用户列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<UserDTO> listUsers(UserQueryDTO query);

    /**
     * 根据 ID 查询用户详情
     *
     * @param userId 用户 ID
     * @return 用户 DTO
     */
    UserDTO getUserById(Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param userName 用户名
     * @return 用户 DTO，如果不存在返回 null
     */
    UserDTO getUserByUserName(String userName);

    /**
     * 创建用户
     *
     * <p>创建用户时：
     * <ul>
     *   <li>密码会进行 BCrypt 加密</li>
     *   <li>会关联指定的角色和岗位</li>
     *   <li>会检查用户名唯一性</li>
     * </ul>
     *
     * @param dto 创建用户 DTO
     * @return 创建的用户 ID
     */
    Long createUser(UserCreateDTO dto);

    /**
     * 更新用户信息
     *
     * <p>更新用户时：
     * <ul>
     *   <li>仅更新 DTO 中非 null 的字段</li>
     *   <li>会更新角色和岗位关联（如果提供）</li>
     *   <li>不允许修改用户名</li>
     * </ul>
     *
     * @param dto 更新用户 DTO
     */
    void updateUser(UserUpdateDTO dto);

    /**
     * 删除用户
     *
     * <p>逻辑删除，将 del_flag 设置为 "2"
     *
     * @param userId 用户 ID
     */
    void deleteUser(Long userId);

    /**
     * 批量删除用户
     *
     * <p>逻辑删除，将 del_flag 设置为 "2"
     *
     * @param userIds 用户 ID 列表
     */
    void deleteUsers(Long[] userIds);

    /**
     * 重置用户密码
     *
     * <p>将用户密码重置为默认密码（如：123456）
     *
     * @param userId 用户 ID
     */
    void resetPassword(Long userId);

    /**
     * 修改用户密码
     *
     * <p>用户自行修改密码，需要验证旧密码
     *
     * @param userId 用户 ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新用户状态
     *
     * <p>启用或停用用户账号
     *
     * @param userId 用户 ID
     * @param status 状态（0=正常，1=停用）
     */
    void updateStatus(Long userId, String status);

    /**
     * 检查用户名是否唯一
     *
     * @param userName 用户名
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkUserNameUnique(String userName);

    /**
     * 检查邮箱是否唯一
     *
     * @param email 邮箱
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkEmailUnique(String email);

    /**
     * 检查手机号是否唯一
     *
     * @param phonenumber 手机号
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkPhoneUnique(String phonenumber);
}
