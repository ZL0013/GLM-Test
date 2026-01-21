package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 定时任务查询条件 DTO
 *
 * <p>用于定时任务列表查询的数据传输对象，继承 {@link PageQuery} 获得分页和排序能力。
 *
 * <p>包含以下查询条件：
 * <ul>
 *   <li>任务基本信息查询条件（任务名称、任务组）</li>
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
public class JobQueryDTO extends PageQuery {

    // ==================== 任务基本信息查询条件 ====================

    /**
     * 任务名称（模糊搜索）
     */
    private String jobName;

    /**
     * 任务组名（精确查询）
     */
    private String jobGroup;

    /**
     * 任务状态（0=正常，1=暂停）
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
