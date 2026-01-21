package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户查询条件 DTO
 *
 * <p>用于用户列表查询的数据传输对象，继承 {@link PageQuery} 获得分页和排序能力。
 *
 * <p>包含以下查询条件：
 * <ul>
 *   <li>用户基本信息查询条件（用户名、昵称、邮箱、手机号等）</li>
 *   <li>筛选条件（性别、状态、部门）</li>
 *   <li>时间范围查询条件</li>
 * </ul>
 *
 * <p>分页和排序参数继承自父类 {@link PageQuery}：
 * <ul>
 *   <li>pageNum：页码</li>
 *   <li>pageSize：每页大小</li>
 *   <li>orderByColumn：排序列</li>
 *   <li>isAsc：排序方向</li>
 * </ul>
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryDTO extends PageQuery {

    // ==================== 用户基本信息查询条件 ====================

    /**
     * 用户名（模糊搜索）
     */
    private String userName;

    /**
     * 用户昵称（模糊搜索）
     */
    private String nickName;

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
     * 账号状态（0=正常，1=停用）
     */
    private String status;

    /**
     * 部门 ID（精确查询）
     */
    private Long deptId;

    // ==================== 时间范围查询条件 ====================

    /**
     * 查询开始时间
     */
    private LocalDateTime startTime;

    /**
     * 查询结束时间
     */
    private LocalDateTime endTime;
}
