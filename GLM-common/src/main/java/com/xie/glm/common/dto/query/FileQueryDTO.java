package com.xie.glm.common.dto.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 文件查询 DTO
 * <p>
 * 用于文件列表查询的条件封装
 *
 * @author xie
 */
public record FileQueryDTO(
    String fileName,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Long minSize,
    Long maxSize,
    Integer pageNum,
    Integer pageSize
) {

    /** 默认页码 */
    private static final int DEFAULT_PAGE_NUM = 1;

    /** 默认每页大小 */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 无参构造函数（使用默认值）
     */
    public FileQueryDTO() {
        this(null, null, null, null, null, DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
    }

    /**
     * 紧凑构造函数（参数验证和默认值）
     */
    public FileQueryDTO {
        if (pageNum == null || pageNum < 1) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
    }

    /**
     * 创建 builder
     *
     * @return Builder 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 转换为 MyBatis Plus Page 对象
     *
     * @param <T> 实体类型
     * @return Page 对象
     */
    public <T> Page<T> toPage() {
        return new Page<>(pageNum, pageSize);
    }

    /**
     * Builder 类
     */
    public static class Builder {
        private String fileName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Long minSize;
        private Long maxSize;
        private Integer pageNum = DEFAULT_PAGE_NUM;
        private Integer pageSize = DEFAULT_PAGE_SIZE;

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder minSize(Long minSize) {
            this.minSize = minSize;
            return this;
        }

        public Builder maxSize(Long maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder pageNum(Integer pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public FileQueryDTO build() {
            return new FileQueryDTO(fileName, startDate, endDate, minSize, maxSize, pageNum, pageSize);
        }
    }
}
