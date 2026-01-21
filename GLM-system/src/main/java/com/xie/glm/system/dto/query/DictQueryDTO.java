package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 字典查询条件 DTO
 *
 * <p>用于字典类型和字典数据列表查询的数据传输对象，继承 {@link PageQuery} 获得分页和排序能力。
 *
 * <p>包含以下查询条件：
 * <ul>
 *   <li>字典类型查询条件（字典名称、字典类型）</li>
 *   <li>字典数据查询条件（字典标签、字典类型）</li>
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
public class DictQueryDTO extends PageQuery {

    // ==================== 字典类型查询条件 ====================

    /**
     * 字典名称（模糊搜索）
     */
    private String dictName;

    /**
     * 字典类型（精确搜索）
     */
    private String dictType;

    // ==================== 字典数据查询条件 ====================

    /**
     * 字典标签（模糊搜索）
     */
    private String dictLabel;

    // ==================== 通用筛选条件 ====================

    /**
     * 状态（0=正常，1=停用）
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
