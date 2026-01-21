package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.UserVoConverter;
import com.xie.glm.admin.vo.UserVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import com.xie.glm.system.dto.query.UserQueryDTO;
import com.xie.glm.system.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户门面类
 *
 * <p>封装用户管理的业务逻辑调用，负责 DTO → VO 的转换。
 *
 * <p>职责：
 * <ul>
 *   <li>调用 Service 层获取 DTO 数据</li>
 *   <li>使用 UserVoConverter 将 DTO 转换为 VO</li>
 *   <li>组装复杂的展示数据（如部门名称、角色名称列表）</li>
 *   <li>简化 Controller 的逻辑</li>
 * </ul>
 *
 * <p>数据流向：
 * <pre>
 * Controller → Facade → Service → Mapper
 *     ↓         ↓         ↓
 *   VO  ←  VO  ←  DTO
 * </pre>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class UserFacade {

    private final IUserService userService;
    private final UserVoConverter voConverter;

    /**
     * 分页查询用户列表
     *
     * @param query 查询条件
     * @return 分页结果（VO）
     */
    public PageResult<UserVO> listUsers(UserQueryDTO query) {
        // 调用 Service 获取 DTO 分页数据
        PageResult<UserDTO> dtoPage = userService.listUsers(query);

        // 转换 DTO 为 VO
        List<UserVO> voList = voConverter.toVoList(dtoPage.getRecords());

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 根据 ID 查询用户详情
     *
     * @param userId 用户 ID
     * @return 用户 VO
     */
    public UserVO getUserById(Long userId) {
        // 调用 Service 获取 DTO
        UserDTO dto = userService.getUserById(userId);

        // 转换 DTO 为 VO
        return voConverter.toVo(dto);
    }

    /**
     * 创建用户
     *
     * <p>创建用户时：
     * <ul>
     *   <li>Service 层会进行唯一性校验</li>
     *   <li>密码会进行 BCrypt 加密</li>
     *   <li>会关联指定的角色和岗位</li>
     * </ul>
     *
     * @param dto 创建用户 DTO
     * @return 创建的用户 ID
     */
    public Long createUser(UserCreateDTO dto) {
        return userService.createUser(dto);
    }

    /**
     * 更新用户信息
     *
     * @param dto 更新用户 DTO
     */
    public void updateUser(UserUpdateDTO dto) {
        userService.updateUser(dto);
    }

    /**
     * 删除用户
     *
     * @param userId 用户 ID
     */
    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }

    /**
     * 批量删除用户
     *
     * @param userIds 用户 ID 数组
     */
    public void deleteUsers(Long[] userIds) {
        userService.deleteUsers(userIds);
    }

    /**
     * 重置用户密码
     *
     * <p>将用户密码重置为默认密码（如：123456）
     *
     * @param userId 用户 ID
     */
    public void resetPassword(Long userId) {
        userService.resetPassword(userId);
    }

    /**
     * 修改用户密码
     *
     * <p>用户自行修改密码，需要验证旧密码
     *
     * @param userId 用户 ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        userService.changePassword(userId, oldPassword, newPassword);
    }

    /**
     * 更新用户状态
     *
     * @param userId 用户 ID
     * @param status 状态（0=正常，1=停用）
     */
    public void updateStatus(Long userId, String status) {
        userService.updateStatus(userId, status);
    }

    /**
     * 检查用户名是否唯一
     *
     * @param userName 用户名
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkUserNameUnique(String userName) {
        return userService.checkUserNameUnique(userName);
    }

    /**
     * 检查邮箱是否唯一
     *
     * @param email 邮箱
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkEmailUnique(String email) {
        return userService.checkEmailUnique(email);
    }

    /**
     * 检查手机号是否唯一
     *
     * @param phonenumber 手机号
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkPhoneUnique(String phonenumber) {
        return userService.checkPhoneUnique(phonenumber);
    }
}
