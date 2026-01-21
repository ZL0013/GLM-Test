package com.xie.glm.common.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 文件上传 DTO
 * <p>
 * 封装文件上传请求，提供文件验证和元数据提取功能
 *
 * @author xie
 */
public record FileUploadDTO(MultipartFile file) {

    /** 允许的图片格式 */
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");

    /** 最大文件大小 (10MB) */
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 构造函数，验证文件不为空
     *
     * @param file 上传的文件
     * @throws IllegalArgumentException 如果文件为空
     */
    public FileUploadDTO {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
    }

    /**
     * 获取原始文件名
     *
     * @return 原始文件名
     */
    public String originalFilename() {
        return file.getOriginalFilename();
    }

    /**
     * 获取文件内容类型
     *
     * @return Content-Type
     */
    public String contentType() {
        return file.getContentType();
    }

    /**
     * 获取文件大小
     *
     * @return 文件大小（字节）
     */
    public long size() {
        return file.getSize();
    }

    /**
     * 获取文件扩展名
     *
     * @return 文件扩展名（不含点），如 "txt", "pdf"
     */
    public String fileExtension() {
        String filename = originalFilename();
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * 验证文件是否有效
     * <p>
     * 检查文件大小是否在允许范围内
     *
     * @return 文件有效返回 true，否则返回 false
     */
    public boolean isValid() {
        return size() > 0 && size() <= MAX_FILE_SIZE;
    }

    /**
     * 判断是否为图片文件
     *
     * @return 是图片返回 true，否则返回 false
     */
    public boolean isImage() {
        return IMAGE_EXTENSIONS.contains(fileExtension());
    }

    /**
     * 获取文件内容字节数组
     *
     * @return 文件内容
     * @throws RuntimeException 如果读取失败
     */
    public byte[] getBytes() {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("读取文件内容失败", e);
        }
    }

    /**
     * 获取输入流
     *
     * @return 文件输入流
     * @throws RuntimeException 如果获取失败
     */
    public java.io.InputStream getInputStream() {
        try {
            return file.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException("获取文件输入流失败", e);
        }
    }
}
