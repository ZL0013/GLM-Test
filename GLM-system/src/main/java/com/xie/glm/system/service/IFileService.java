package com.xie.glm.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.dto.FileDTO;
import com.xie.glm.common.dto.FileUploadDTO;
import com.xie.glm.common.dto.query.FileQueryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件服务接口
 * <p>
 * 提供文件上传、下载、查询、删除等功能
 *
 * @author xie
 */
public interface IFileService {

    /**
     * 上传文件
     *
     * @param uploadDTO 文件上传 DTO
     * @return 文件 DTO
     * @throws RuntimeException 上传失败时抛出
     */
    FileDTO uploadFile(FileUploadDTO uploadDTO);

    /**
     * 上传文件（便捷方法）
     *
     * @param file 上传的文件
     * @return 文件 DTO
     * @throws RuntimeException 上传失败时抛出
     */
    default FileDTO uploadFile(MultipartFile file) {
        return uploadFile(new FileUploadDTO(file));
    }

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @return 文件输入流
     * @throws RuntimeException 文件不存在或读取失败时抛出
     */
    InputStream downloadFile(Long fileId);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @throws RuntimeException 文件不存在或删除失败时抛出
     */
    void deleteFile(Long fileId);

    /**
     * 批量删除文件
     *
     * @param fileIds 文件ID列表
     * @return 删除成功的数量
     */
    int deleteFiles(java.util.List<Long> fileIds);

    /**
     * 根据ID查询文件
     *
     * @param fileId 文件ID
     * @return 文件 DTO，不存在返回 null
     */
    FileDTO getFileById(Long fileId);

    /**
     * 分页查询文件列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<FileDTO> listFiles(FileQueryDTO queryDTO);

    /**
     * 检查文件是否存在
     *
     * @param fileId 文件ID
     * @return 存在返回 true，否则返回 false
     */
    boolean exists(Long fileId);
}
