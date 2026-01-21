package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 操作日志查询条件 DTO
 *
 * <p>用于操作日志列表查询的数据传输对象，继承 {@link PageQuery} 获得分页和排序能力。
 *
 * <p>包含以下查询条件：
 * <ul>
 *   <li>模块标题（模糊搜索）</li>
 *   <li>业务类型（精确查询）</li>
 *   <li>操作人员（模糊搜索）</li>
 *   <li>操作状态（精确查询）</li>
 *   <li>操作时间范围（范围查询）</li>
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
public class LogQueryDTO extends PageQuery {

    /**
     * 模块标题（模糊搜索）
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）
     */
    private Integer businessType;

    /**
     * 操作人员（模糊搜索）
     */
    private String operName;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;

    /**
     * 操作开始时间
     */
    private LocalDateTime startTime;

    /**
     * 操作结束时间
     */
    private LocalDateTime endTime;
}
