package com.xie.glm.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.glm.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统部门实体
 *
 * <p>对应数据库表：xie_tm.sys_dept
 *
 * <p>字段说明：
 * <ul>
 *   <li>dept_id：部门 ID（主键）</li>
 *   <li>dept_name：部门名称</li>
 *   <li>parent_id：父部门 ID（0=顶级部门）</li>
 *   <li>order_num：显示顺序</li>
 *   <li>leader：负责人</li>
 *   <li>phone：联系电话</li>
 *   <li>email：邮箱</li>
 *   <li>status：部门状态（0=正常，1=停用）</li>
 *   <li>ancestors：祖级列表（用逗号分隔，如：0,100,101）</li>
 * </ul>
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("xie_tm.sys_dept")
public class SysDept extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 部门 ID
     */
    @TableId(value = "dept_id", type = IdType.AUTO)
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

    /**
     * 部门状态（0=正常，1=停用）
     */
    private String status;

    /**
     * 祖级列表（用逗号分隔）
     */
    private String ancestors;
}
