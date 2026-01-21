package com.xie.glm.system.security;

import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.framework.security.CustomUserDetails;
import com.xie.glm.system.domain.SysUser;
import com.xie.glm.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户详情服务实现
 *
 * <p>实现 Spring Security 的 UserDetailsService 接口，用于认证时加载用户信息。
 *
 * <p>职责：
 * <ul>
 *   <li>根据用户名查询用户基本信息</li>
 *   <li>加载用户的角色列表</li>
 *   <li>加载用户的权限标识列表</li>
 *   <li>构造 CustomUserDetails 对象供 Spring Security 使用</li>
 * </ul>
 *
 * <p>异常处理策略（符合宪法第五条）：
 * <ul>
 *   <li>内部统一使用 {@link ServiceException} 进行业务异常处理</li>
 *   <li>外层捕获 {@link ServiceException} 并转换为 Spring Security 需要的 {@link UsernameNotFoundException}</li>
 *   <li>所有异常使用异常链传递，保留原始异常信息</li>
 * </ul>
 *
 * @author xie
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper userMapper;

    /**
     * 根据用户名加载用户详情
     *
     * <p>实现流程：
     * <ol>
     *   <li>根据用户名查询用户基本信息</li>
     *   <li>验证用户状态（是否停用、是否已删除）</li>
     *   <li>查询用户角色列表</li>
     *   <li>查询用户权限标识列表</li>
     *   <li>构造 CustomUserDetails 对象</li>
     * </ol>
     *
     * @param username 用户名
     * @return 用户详情对象
     * @throws UsernameNotFoundException 用户不存在或状态异常时抛出（Spring Security 要求）
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return loadUserByUsernameInternal(username);
        } catch (ServiceException e) {
            // 将统一业务异常转换为 Spring Security 需要的异常
            log.warn("认证失败: {}, 原因: {}", username, e.getMessage());
            throw new UsernameNotFoundException(e.getMessage(), e);
        } catch (Exception e) {
            // 其他异常也转换为 UsernameNotFoundException，保留异常链（宪法第三条）
            log.error("加载用户详情失败: {}", username, e);
            throw new UsernameNotFoundException("系统异常，请稍后重试", e);
        }
    }

    /**
     * 内部加载用户详情方法
     *
     * <p>此方法使用统一的 {@link ServiceException} 进行业务异常处理。
     *
     * @param username 用户名
     * @return 用户详情对象
     * @throws ServiceException 业务异常时抛出
     */
    private UserDetails loadUserByUsernameInternal(String username) {
        // 1. 查询用户基本信息
        SysUser user = userMapper.selectUserByName(username);
        if (user == null) {
            throw new ServiceException(BusinessStatus.USER_NOT_FOUND);
        }

        // 2. 验证用户状态
        validateUserStatus(user, username);

        // 3. 查询用户角色列表
        List<String> roles = userMapper.selectRolesByUserId(user.getUserId());
        if (roles == null) {
            roles = List.of();
        }

        // 4. 查询用户权限标识列表
        List<String> permissions = userMapper.selectPermsByUserId(user.getUserId());
        if (permissions == null) {
            permissions = List.of();
        }

        // 5. 查询用户数据权限范围
        Integer dataScope = userMapper.selectDataScopeByUserId(user.getUserId());
        if (dataScope == null) {
            dataScope = 5; // 默认：仅本人数据权限
        }

        // 6. 构造 CustomUserDetails 对象
        boolean enabled = "0".equals(user.getStatus());
        CustomUserDetails userDetails = new CustomUserDetails(
                user.getUserId(),
                user.getUserName(),
                user.getPassword(),
                enabled,
                roles,
                permissions
        );
        userDetails.setDeptId(user.getDeptId());
        userDetails.setDataScope(dataScope);
        return userDetails;
    }

    /**
     * 验证用户状态
     *
     * <p>使用统一的 {@link ServiceException} 抛出业务异常。
     *
     * @param user     用户信息
     * @param username 用户名
     * @throws ServiceException 状态异常时抛出
     */
    private void validateUserStatus(SysUser user, String username) {
        // 检查是否已删除
        if ("1".equals(user.getDelFlag())) {
            log.warn("用户已删除: {}", username);
            throw new ServiceException(BusinessStatus.USER_NOT_FOUND);
        }

        // 检查是否已停用
        if ("1".equals(user.getStatus())) {
            log.warn("用户已停用: {}", username);
            throw new ServiceException(BusinessStatus.USER_ACCOUNT_DISABLED);
        }
    }
}
