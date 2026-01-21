package com.xie.glm.common.core;

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
 * </ul>
 *
 * <p>使用 Java 14+ record 实现不可变性（符合宪法第一条第1.4款）
 *
 * @param <T> 记录类型
 * @author xie
 */
public record PageResult<T>(List<T> records, Long total) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 获取当前页的数据列表（传统 getter 方法，兼容旧代码）
     *
     * @return 当前页的数据列表
     */
    public List<T> getRecords() {
        return records;
    }

    /**
     * 获取总记录数（传统 getter 方法，兼容旧代码）
     *
     * @return 总记录数
     */
    public Long getTotal() {
        return total;
    }

    /**
     * 计算总页数
     *
     * @param pageSize 每页大小
     * @return 总页数
     */
    public Long getPages(Long pageSize) {
        if (total == null || pageSize == null || pageSize == 0) {
            return 0L;
        }
        return (total + pageSize - 1) / pageSize;
    }
}
