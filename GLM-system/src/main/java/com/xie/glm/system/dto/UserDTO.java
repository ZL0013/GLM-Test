package com.xie.glm.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户数据传输对象
 *
 * <p>用于服务层返回给前端展示的用户数据，包含用户基本信息和状态。
 *
 * <p>字段说明：
 * <ul>
 *   <li>userId：用户 ID</li>
 *   <li>userName：用户账号</li>
 *   <li>nickName：用户昵称</li>
 *   <li>email：用户邮箱</li>
 *   <li>phonenumber：手机号码</li>
 *   <li>sex：性别（0=男，1=女，2=未知）</li>
 *   <li>avatar：头像地址</li>
 *   <li>deptId：部门 ID</li>
 *   <li>status：账号状态（0=正常，1=停用）</li>
 *   <li>remark：备注</li>
 *   <li>createTime：创建时间</li>
 *   <li>updateTime：更新时间</li>
 *   <li>loginDate：最后登录时间</li>
 *   <li>pwdUpdateDate：密码最后更新时间</li>
 *   <li>passwordChanged：是否修改过默认密码</li>
 *   <li>defaultPassword：是否为默认密码</li>
 * </ul>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 用户基本信息 ====================

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户密码（内部使用，不应暴露给前端）
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

    // ==================== 时间字段 ====================

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    /**
     * 密码最后更新时间
     */
    private LocalDateTime pwdUpdateDate;

    // ==================== 状态标志 ====================

    /**
     * 是否修改过默认密码
     */
    private Boolean passwordChanged;

    /**
     * 是否为默认密码
     */
    private Boolean defaultPassword;
}
