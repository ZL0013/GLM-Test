package com.xie.glm.common.query;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询基类
 *
 * <p>所有查询条件 DTO 都应继承此类，包含通用的分页和排序参数：
 * <ul>
 *   <li>pageNum：页码（从 1 开始）</li>
 *   <li>pageSize：每页大小</li>
 *   <li>orderByColumn：排序列名</li>
 *   <li>isAsc：排序方向（asc=升序，desc=降序）</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * public class UserQueryDTO extends PageQuery {
 *     private String userName;
 *     private String status;
 *     // ... 其他查询条件
 * }
 * }</pre>
 *
 * @author xie
 */
@Data
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 页码（从 1 开始）
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 排序列（如：user_id、create_time、user_name）
     */
    private String orderByColumn;

    /**
     * 排序方向（asc=升序，desc=降序）
     */
    private String isAsc;
}
