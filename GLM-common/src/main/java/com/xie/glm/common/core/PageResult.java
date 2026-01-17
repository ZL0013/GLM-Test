package com.xie.glm.common.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页结果包装类
 *
 * <p>用于包装分页查询的结果，包含：
 * <ul>
 *   <li>records：当前页的数据列表</li>
 *   <li>total：总记录数</li>
 *   <li>pageNum：当前页码</li>
 *   <li>pageSize：每页大小</li>
 * </ul>
 *
 * @param <T> 记录类型
 * @author xie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页的数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long pageNum;

    /**
     * 每页大小
     */
    private Long pageSize;

    /**
     * 计算总页数
     *
     * @return 总页数
     */
    public Long getPages() {
        if (total == null || pageSize == null || pageSize == 0) {
            return 0L;
        }
        return (total + pageSize - 1) / pageSize;
    }
}
