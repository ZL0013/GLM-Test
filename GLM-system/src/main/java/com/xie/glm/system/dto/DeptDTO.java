package com.xie.glm.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 部门数据传输对象
 *
 * <p>用于服务层返回给前端展示的部门数据。
 *
 * <p>字段说明：
 * <ul>
 *   <li>deptId：部门 ID</li>
 *   <li>deptName：部门名称</li>
 *   <li>parentId：父部门 ID（0=顶级部门）</li>
 *   <li>orderNum：显示顺序</li>
 *   <li>leader：负责人</li>
 *   <li>phone：联系电话</li>
 *   <li>email：邮箱</li>
 *   <li>status：部门状态（0=正常，1=停用）</li>
 *   <li>ancestors：祖级列表（用逗号分隔）</li>
 *   <li>createTime：创建时间</li>
 *   <li>updateTime：更新时间</li>
 * </ul>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DeptDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 部门基本信息 ====================

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 父部门 ID（0=顶级部门）
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    // ==================== 联系信息 ====================

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    // ==================== 状态字段 ====================

    /**
     * 部门状态（0=正常，1=停用）
     */
    private String status;

    /**
     * 祖级列表（用逗号分隔）
     */
    private String ancestors;

    // ==================== 时间字段 ====================

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
