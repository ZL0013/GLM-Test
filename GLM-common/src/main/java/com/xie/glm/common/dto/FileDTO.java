package com.xie.glm.common.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 文件 DTO
 * <p>
 * 用于文件信息传输，包含文件元数据
 *
 * @author xie
 */
public record FileDTO(
    Long id,
    String fileName,
    String filePath,
    String contentType,
    Long fileSize,
    String fileMd5,
    LocalDateTime createTime,
    LocalDateTime updateTime
) {

    /**
     * 创建 builder
     *
     * @return Builder 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder 类
     */
    public static class Builder {
        private Long id;
        private String fileName;
        private String filePath;
        private String contentType;
        private Long fileSize;
        private String fileMd5;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder fileSize(Long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder fileMd5(String fileMd5) {
            this.fileMd5 = fileMd5;
            return this;
        }

        public Builder createTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder updateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public FileDTO build() {
            return new FileDTO(id, fileName, filePath, contentType, fileSize, fileMd5, createTime, updateTime);
        }
    }
}
