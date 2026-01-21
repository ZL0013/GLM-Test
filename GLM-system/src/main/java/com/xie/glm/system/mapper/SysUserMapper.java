package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.glm.system.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统用户 Mapper 接口
 *
 * <p>基于 MyBatis Plus，提供用户数据访问操作。
 *
 * @author xie
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户信息（包含角色和权限）
     *
     * <p>注意：这是占位方法，实际关联查询需要通过 XML 或多个查询实现
     *
     * @param userName 用户名
     * @return 用户信息，如果不存在返回 null
     */
    @Select("SELECT user_id, user_name, nick_name, email, phonenumber, sex, avatar, password, " +
            "dept_id, status, del_flag, login_ip, login_date " +
            "FROM sys_user WHERE user_name = #{userName} AND del_flag = '0'")
    SysUser selectUserByName(@Param("userName") String userName);

    /**
     * 根据用户 ID 查询用户角色列表
     *
     * <p>返回角色的 role_key（角色权限字符串）。
     *
     * @param userId 用户 ID
     * @return 角色 key 列表（用于 Spring Security 的角色前缀）
     */
    @Select("SELECT r.role_key " +
            "FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.del_flag = '0' AND r.status = '0'")
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户 ID 查询用户权限列表
     *
     * <p>通过角色关联查询菜单权限标识
     *
     * @param userId 用户 ID
     * @return 权限标识列表
     */
    @Select("SELECT DISTINCT m.perms " +
            "FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.status = '0' AND m.perms IS NOT NULL AND m.perms != ''")
    List<String> selectPermsByUserId(@Param("userId") Long userId);

    /**
     * 获取用户的数据权限范围
     *
     * <p>取用户所有角色中最大的数据权限范围（数字越小权限越大）
     * <p>如果用户没有角色，返回 5（仅本人数据权限）
     *
     * @param userId 用户 ID
     * @return 数据权限范围（1=全部，2=自定义，3=本部门，4=本部门及以下，5=仅本人）
     */
    @Select("SELECT COALESCE(MIN(CAST(r.data_scope AS INTEGER)), 5) " +
            "FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.del_flag = '0' AND r.status = '0'")
    Integer selectDataScopeByUserId(@Param("userId") Long userId);
}
