package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 角色查询条件 DTO
 *
 * <p>用于角色列表查询的数据传输对象，继承 {@link PageQuery} 获得分页和排序能力。
 *
 * <p>包含以下查询条件：
 * <ul>
 *   <li>角色基本信息查询条件（角色名称、角色权限字符串）</li>
 *   <li>筛选条件（状态）</li>
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
public class RoleQueryDTO extends PageQuery {

    // ==================== 角色基本信息查询条件 ====================

    /**
     * 角色名称（模糊搜索）
     */
    private String roleName;

    /**
     * 角色权限字符串（模糊搜索）
     */
    private String roleKey;

    // ==================== 筛选条件 ====================

    /**
     * 角色状态（0=正常，1=停用）
     */
    private String status;

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
