package com.xie.glm.system.dto;

import com.xie.glm.common.validation.annotation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建用户 DTO
 *
 * <p>用于创建用户的数据传输对象，包含用户基本信息和角色、岗位关联数据。
 *
 * <p>字段说明：
 * <ul>
 *   <li>userName：用户账号（必填，唯一）</li>
 *   <li>nickName：用户昵称（必填）</li>
 *   <li>password：密码（必填，后续会进行 BCrypt 加密）</li>
 *   <li>email：用户邮箱（选填，需符合邮箱格式）</li>
 *   <li>phonenumber：手机号码（选填，需符合手机号格式）</li>
 *   <li>sex：性别（0=男，1=女，2=未知）</li>
 *   <li>avatar：头像地址（选填）</li>
 *   <li>deptId：部门 ID（选填）</li>
 *   <li>status：账号状态（0=正常，1=停用，默认为正常）</li>
 *   <li>remark：备注（选填）</li>
 *   <li>roleIds：角色 ID 列表（选填，用于关联用户角色）</li>
 *   <li>postIds：岗位 ID 列表（选填，用于关联用户岗位）</li>
 * </ul>
 *
 * @author xie
 */
@Data
public class UserCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 用户基本信息（必填） ====================

    /**
     * 用户账号（必填，唯一）
     */
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 2, max = 20, message = "用户账号长度必须介于 2 和 20 之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户账号只能包含字母、数字和下划线")
    private String userName;

    /**
     * 用户昵称（必填）
     */
    @NotBlank(message = "用户昵称不能为空")
    @Size(max = 30, message = "用户昵称长度不能超过 30 个字符")
    private String nickName;

    /**
     * 密码（必填，后续会进行 BCrypt 加密）
     */
    @NotBlank(message = "用户密码不能为空")
    @Size(min = 6, max = 20, message = "用户密码长度必须介于 6 和 20 之间")
    private String password;

    // ==================== 用户联系方式（选填） ====================

    /**
     * 用户邮箱（选填）
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过 50 个字符")
    private String email;

    /**
     * 手机号码（选填）
     */
    @Phone(message = "手机号格式不正确")
    private String phonenumber;

    // ==================== 用户基本信息（选填） ====================

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
