package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.framework.config.UserSecurityProperties;
import com.xie.glm.system.converter.UserConverter;
import com.xie.glm.system.domain.SysUser;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import com.xie.glm.system.dto.query.UserQueryDTO;
import com.xie.glm.system.mapper.SysUserMapper;
import com.xie.glm.system.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 用户服务实现类
 *
 * <p>实现用户管理的业务逻辑，包括用户的增删改查、密码重置等功能。
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final SysUserMapper userMapper;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final UserSecurityProperties userSecurityProperties;

    @Override
    public PageResult<UserDTO> listUsers(UserQueryDTO query) {
        // 构建分页对象
        Page<SysUser> page = new Page<>(
            query.getPageNum() != null ? query.getPageNum() : 1,
            query.getPageSize() != null ? query.getPageSize() : 10
        );

        // 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUserName()), SysUser::getUserName, query.getUserName())
            .like(StringUtils.hasText(query.getNickName()), SysUser::getNickName, query.getNickName())
            .eq(StringUtils.hasText(query.getEmail()), SysUser::getEmail, query.getEmail())
            .eq(StringUtils.hasText(query.getPhonenumber()), SysUser::getPhonenumber, query.getPhonenumber())
            .eq(StringUtils.hasText(query.getSex()), SysUser::getSex, query.getSex())
            .eq(StringUtils.hasText(query.getStatus()), SysUser::getStatus, query.getStatus())
            .eq(query.getDeptId() != null, SysUser::getDeptId, query.getDeptId())
            .ge(query.getStartTime() != null, SysUser::getCreateTime, query.getStartTime())
            .le(query.getEndTime() != null, SysUser::getCreateTime, query.getEndTime())
            .eq(SysUser::getDelFlag, "0")
            .orderByDesc(SysUser::getCreateTime);

        // 执行分页查询
        IPage<SysUser> pageResult = userMapper.selectPage(page, wrapper);

        // 转换为 DTO
        List<UserDTO> records = userConverter.toDtoList(pageResult.getRecords());

        return new PageResult<>(records, pageResult.getTotal());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null || "2".equals(user.getDelFlag())) {
            throw new ServiceException(BusinessStatus.USER_NOT_FOUND);
        }
        return userConverter.toDto(user);
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        SysUser user = userMapper.selectUserByName(userName);
        if (user == null || "2".equals(user.getDelFlag())) {
            return null;
        }
        return userConverter.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateDTO dto) {
        // 检查用户名唯一性
        if (!checkUserNameUnique(dto.getUserName())) {
            throw new ServiceException(BusinessStatus.USER_NAME_DUPLICATE);
        }

        // 检查邮箱唯一性
        if (StringUtils.hasText(dto.getEmail()) && !checkEmailUnique(dto.getEmail())) {
            throw new ServiceException(BusinessStatus.USER_EMAIL_DUPLICATE);
        }

        // 检查手机号唯一性
        if (StringUtils.hasText(dto.getPhonenumber()) && !checkPhoneUnique(dto.getPhonenumber())) {
            throw new ServiceException(BusinessStatus.USER_PHONE_DUPLICATE);
        }

        // 转换 DTO 为 Entity
        SysUser user = userConverter.createDtoToEntity(dto);

        // 加密密码
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 设置默认值
        user.setStatus(StringUtils.hasText(user.getStatus()) ? user.getStatus() : "0");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDelFlag("0");

        // 插入用户
        userMapper.insert(user);

        // TODO: 处理角色和岗位关联（需要 SysUserRoleMapper 和 SysUserPostMapper）
        // if (CollectionUtils.isNotEmpty(dto.getRoleIds())) {
        //     dto.getRoleIds().forEach(roleId -> {
        //         SysUserRole userRole = new SysUserRole();
        //         userRole.setUserId(user.getUserId());
        //         userRole.setRoleId(roleId);
        //         userRoleMapper.insert(userRole);
        //     });
        // }

        return user.getUserId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateDTO dto) {
        // 检查用户是否存在
        SysUser existingUser = userMapper.selectById(dto.getUserId());
        if (existingUser == null || "2".equals(existingUser.getDelFlag())) {
            throw new ServiceException(BusinessStatus.USER_NOT_FOUND);
        }

        // 检查邮箱唯一性（排除自己）
        if (StringUtils.hasText(dto.getEmail()) && !checkEmailUnique(dto.getEmail(), dto.getUserId())) {
            throw new ServiceException(BusinessStatus.USER_EMAIL_DUPLICATE);
        }

        // 检查手机号唯一性（排除自己）
        if (StringUtils.hasText(dto.getPhonenumber()) && !checkPhoneUnique(dto.getPhonenumber(), dto.getUserId())) {
            throw new ServiceException(BusinessStatus.USER_PHONE_DUPLICATE);
        }

        // 转换 DTO 为 Entity
        SysUser user = userConverter.updateDtoToEntity(dto);

        // 更新时间
        user.setUpdateTime(LocalDateTime.now());

        // 更新用户
        userMapper.updateById(user);

        // TODO: 更新角色和岗位关联
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException(BusinessStatus.USER_NOT_FOUND);
        }

        // 逻辑删除
        user.setDelFlag("2");
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        // TODO: 删除角色和岗位关联
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(Long[] userIds) {
        Arrays.stream(userIds).forEach(userId -> {
            SysUser user = userMapper.selectById(userId);
            if (user != null) {
                user.setDelFlag("2");
                user.setUpdateTime(LocalDateTime.now());
                userMapper.updateById(user);
            }
        });

        // TODO: 批量删除角色和岗位关联
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null || "2".equals(user.getDelFlag())) {
            throw new ServiceException(BusinessStatus.USER_NOT_FOUND);
        }

        // 重置密码为配置的默认密码
        user.setPassword(passwordEncoder.encode(userSecurityProperties.getDefaultPassword()));
        user.setPwdUpdateDate(LocalDateTime.now());
        user.setPasswordChanged(false);
        user.setDefaultPassword(true);
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = userMapper.selectById(userId);
        if (user == null || "2".equals(user.getDelFlag())) {
            throw new ServiceException(BusinessStatus.USER_NOT_FOUND);
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ServiceException(BusinessStatus.USER_OLD_PASSWORD_ERROR);
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPwdUpdateDate(LocalDateTime.now());
        user.setPasswordChanged(true);
        user.setDefaultPassword(false);
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long userId, String status) {
        SysUser user = userMapper.selectById(userId);
        if (user == null || "2".equals(user.getDelFlag())) {
            throw new ServiceException(BusinessStatus.USER_NOT_FOUND);
        }

        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateById(user);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        Long count = userMapper.selectCount(
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, userName)
                .eq(SysUser::getDelFlag, "0")
        );
        return count == 0;
    }

    @Override
    public boolean checkEmailUnique(String email) {
        Long count = userMapper.selectCount(
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, email)
                .eq(SysUser::getDelFlag, "0")
        );
        return count == 0;
    }

    @Override
    public boolean checkPhoneUnique(String phonenumber) {
        Long count = userMapper.selectCount(
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhonenumber, phonenumber)
                .eq(SysUser::getDelFlag, "0")
        );
        return count == 0;
    }

    /**
     * 检查邮箱唯一性（排除指定用户）
     */
    private boolean checkEmailUnique(String email, Long excludeUserId) {
        Long count = userMapper.selectCount(
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, email)
                .ne(SysUser::getUserId, excludeUserId)
                .eq(SysUser::getDelFlag, "0")
        );
        return count == 0;
    }

    /**
     * 检查手机号唯一性（排除指定用户）
     */
    private boolean checkPhoneUnique(String phonenumber, Long excludeUserId) {
        Long count = userMapper.selectCount(
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhonenumber, phonenumber)
                .ne(SysUser::getUserId, excludeUserId)
                .eq(SysUser::getDelFlag, "0")
        );
        return count == 0;
    }
}
