package com.xie.glm.system.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建用户业务对象
 *
 * <p>用于 Service 层内部组合用户基本信息与角色、岗位关联数据。
 * BO（Business Object）仅在 Service 层内部使用，不对外暴露。
 *
 * <p>典型使用场景：
 * <ul>
 *   <li>创建用户时需要组合 SysUser + SysUserRole + SysUserPost</li>
 *   <li>在 UserServiceImpl 中将 UserCreateDTO 转换为 UserCreateBO</li>
 *   <li>Service 方法内部使用 BO 处理多实体组合业务逻辑</li>
 * </ul>
 *
 * <p>字段说明：
 * <ul>
 *   <li>userName：用户账号</li>
 *   <li>nickName：用户昵称</li>
 *   <li>password：密码（明文，后续会进行 BCrypt 加密）</li>
 *   <li>email：用户邮箱</li>
 *   <li>phonenumber：手机号码</li>
 *   <li>sex：性别（0=男，1=女，2=未知）</li>
 *   <li>avatar：头像地址</li>
 *   <li>deptId：部门 ID</li>
 *   <li>status：账号状态（0=正常，1=停用）</li>
 *   <li>remark：备注</li>
 *   <li>roleIds：角色 ID 列表（用于关联用户角色）</li>
 *   <li>postIds：岗位 ID 列表（用于关联用户岗位）</li>
 * </ul>
 *
 * @author xie
 */
@Data
public class UserCreateBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 用户基本信息 ====================

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 密码（明文，后续会进行 BCrypt 加密）
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别（0=男，1=女，2=未知）
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 账号状态（0=正常，1=停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    // ==================== 关联信息 ====================

    /**
     * 角色 ID 列表
     *
     * <p>用于创建用户时关联角色，一个用户可以关联多个角色
     */
    private List<Long> roleIds;

    /**
     * 岗位 ID 列表
     *
     * <p>用于创建用户时关联岗位，一个用户可以关联多个岗位
     */
    private List<Long> postIds;
}
