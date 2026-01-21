package com.xie.glm.system.dto;

import com.xie.glm.common.validation.annotation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 更新用户 DTO
 *
 * <p>用于更新用户的数据传输对象，所有字段都是可选的（除了 userId）。
 * 仅设置需要更新的字段，未设置的字段不会被更新。
 *
 * <p>字段说明：
 * <ul>
 *   <li>userId：用户 ID（必填，用于指定要更新的用户）</li>
 *   <li>userName：用户账号（可选，通常不允许修改）</li>
 *   <li>nickName：用户昵称（可选）</li>
 *   <li>email：用户邮箱（可选）</li>
 *   <li>phonenumber：手机号码（可选）</li>
 *   <li>sex：性别（0=男，1=女，2=未知）</li>
 *   <li>avatar：头像地址（可选）</li>
 *   <li>deptId：部门 ID（可选）</li>
 *   <li>status：账号状态（0=正常，1=停用）</li>
 *   <li>remark：备注（可选）</li>
 *   <li>roleIds：角色 ID 列表（可选，null 表示不更新，空列表表示清空）</li>
 *   <li>postIds：岗位 ID 列表（可选，null 表示不更新，空列表表示清空）</li>
 * </ul>
 *
 * @author xie
 */
@Data
public class UserUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 必填字段 ====================

    /**
     * 用户 ID（必填，用于指定要更新的用户）
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    // ==================== 用户基本信息（可选） ====================

    /**
     * 用户账号（可选，通常不允许修改）
     */
    @Size(min = 2, max = 20, message = "用户账号长度必须介于 2 和 20 之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户账号只能包含字母、数字和下划线")
    private String userName;

    /**
     * 用户昵称（可选）
     */
    @Size(max = 30, message = "用户昵称长度不能超过 30 个字符")
    private String nickName;

    // ==================== 用户联系方式（可选） ====================

    /**
     * 用户邮箱（可选）
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过 50 个字符")
    private String email;

    /**
     * 手机号码（可选）
     */
    @Phone(message = "手机号格式不正确")
    private String phonenumber;

    // ==================== 用户基本信息（可选） ====================

    /**
     * 用户性别（0=男，1=女，2=未知）
     */
    private String sex;

    /**
     * 头像地址
     */
    @Size(max = 100, message = "头像地址长度不能超过 100 个字符")
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
    @Size(max = 500, message = "备注长度不能超过 500 个字符")
    private String remark;

    // ==================== 关联信息（可选） ====================

    /**
     * 角色 ID 列表
     *
     * <p>用于更新用户关联的角色：
     * <ul>
     *   <li>null：不更新角色</li>
     *   <li>空列表：清空所有角色</li>
     *   <li>非空列表：更新为指定角色列表</li>
     * </ul>
     */
    private List<Long> roleIds;

    /**
     * 岗位 ID 列表
     *
     * <p>用于更新用户关联的岗位：
     * <ul>
     *   <li>null：不更新岗位</li>
     *   <li>空列表：清空所有岗位</li>
     *   <li>非空列表：更新为指定岗位列表</li>
     * </ul>
     */
    private List<Long> postIds;
}
