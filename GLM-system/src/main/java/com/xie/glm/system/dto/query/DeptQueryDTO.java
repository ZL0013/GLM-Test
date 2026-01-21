package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门查询条件 DTO
 *
 * <p>用于部门列表查询的数据传输对象，继承 {@link PageQuery} 获得分页和排序能力。
 *
 * <p>包含以下查询条件：
 * <ul>
 *   <li>部门名称（模糊搜索）</li>
 *   <li>部门状态筛选（0=正常，1=停用）</li>
 *   <li>负责人筛选</li>
 * </ul>
 *
 * <p>分页和排序参数继承自父类 {@link PageQuery}：
 * <ul>
 *   <li>pageNum：页码</li>
 *   <li>pageSize：每页大小</li>
 *   <li>orderByColumn：排序列（推荐：order_num、dept_id）</li>
 *   <li>isAsc：排序方向（asc/desc）</li>
 * </ul>
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptQueryDTO extends PageQuery {

    // ==================== 部门基本信息查询条件 ====================

    /**
     * 部门名称（模糊搜索）
     */
    private String deptName;

    /**
     * 部门状态（0=正常，1=停用）
     */
    private String status;

    /**
     * 负责人
     */
    private String leader;
}
