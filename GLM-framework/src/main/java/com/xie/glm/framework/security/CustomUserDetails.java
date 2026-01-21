package com.xie.glm.framework.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义用户详情
 *
 * <p>实现 Spring Security 的 UserDetails 接口，封装用户认证信息。
 *
 * <p>包含内容：
 * <ul>
 *   <li>用户基本信息（用户名、密码、状态）</li>
 *   <li>用户 ID（用于业务操作）</li>
 *   <li>部门 ID（用于数据权限过滤）</li>
 *   <li>角色列表（用于权限校验）</li>
 *   <li>权限标识列表（用于细粒度权限控制）</li>
 *   <li>数据权限范围（用于数据权限过滤）</li>
 *   <li>自定义部门 ID 列表（用于自定义数据权限）</li>
 * </ul>
 *
 * @author xie
 */
public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（BCrypt 加密）
     */
    private String password;

    /**
     * 用户状态（true=正常，false=停用）
     */
    private boolean enabled;

    /**
     * 角色列表（role_key 形式，如 "admin", "common"）
     */
    private List<String> roles;

    /**
     * 权限标识列表（如 "system:user:list", "system:user:add"）
     */
    private List<String> permissions;

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 数据权限范围（1=全部数据权限, 2=自定义数据权限, 3=本部门数据权限,
     * 4=本部门及以下数据权限, 5=仅本人数据权限）
     */
    private Integer dataScope;

    /**
     * 自定义部门 ID 列表（当 dataScope=2 时使用）
     */
    private List<Long> dataScopeDeptIds;

    /**
     * 默认构造方法
     */
    public CustomUserDetails() {
    }

    /**
     * 完整构造方法
     *
     * @param userId      用户 ID
     * @param username    用户名
     * @param password    密码
     * @param enabled     是否启用
     * @param roles       角色列表
     * @param permissions 权限标识列表
     */
    public CustomUserDetails(Long userId, String username, String password,
                             boolean enabled, List<String> roles, List<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.permissions = permissions;
    }

    /**
     * 获取权限列表
     *
     * <p>Spring Security 需要的权限格式，将角色和权限标识统一转换为 GrantedAuthority。
     * <p>角色需要添加 "ROLE_" 前缀。
     *
     * @return 权限列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new java.util.ArrayList<>();

        // 处理权限标识（可能为 null）
        if (permissions != null) {
            permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
        }

        // 处理角色（需要添加 ROLE_ 前缀）
        if (roles != null) {
            roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .forEach(authorities::add);
        }

        return authorities;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账户是否未过期
     *
     * <p>当前实现固定返回 true，后续可扩展为基于数据库字段判断
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     *
     * <p>当前实现固定返回 true，后续可扩展为基于数据库字段判断
     *
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证是否未过期
     *
     * <p>当前实现固定返回 true，后续可扩展为基于密码有效期判断
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否启用
     *
     * @return true=启用, false=停用
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public List<Long> getDataScopeDeptIds() {
        return dataScopeDeptIds;
    }

    public void setDataScopeDeptIds(List<Long> dataScopeDeptIds) {
        this.dataScopeDeptIds = dataScopeDeptIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomUserDetails that = (CustomUserDetails) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", enabled=" + enabled +
                ", deptId=" + deptId +
                ", dataScope=" + dataScope +
                '}';
    }
}
