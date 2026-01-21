package com.xie.glm.admin.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.admin.converter.FileVoConverter;
import com.xie.glm.admin.vo.FileVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.dto.FileDTO;
import com.xie.glm.common.dto.FileUploadDTO;
import com.xie.glm.common.dto.query.FileQueryDTO;
import com.xie.glm.system.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件门面
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class FileFacade {

    private final IFileService fileService;
    private final FileVoConverter fileVoConverter;

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return 文件 VO
     */
    public FileVO uploadFile(MultipartFile file) {
        FileDTO dto = fileService.uploadFile(file);
        return fileVoConverter.toVo(dto);
    }

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @return 文件输入流
     */
    public InputStream downloadFile(Long fileId) {
        return fileService.downloadFile(fileId);
    }

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     */
    public void deleteFile(Long fileId) {
        fileService.deleteFile(fileId);
    }

    /**
     * 批量删除文件
     *
     * @param fileIds 文件ID列表
     * @return 删除成功的数量
     */
    public int deleteFiles(List<Long> fileIds) {
        return fileService.deleteFiles(fileIds);
    }

    /**
     * 根据ID查询文件
     *
     * @param fileId 文件ID
     * @return 文件 VO
     */
    public FileVO getFileById(Long fileId) {
        FileDTO dto = fileService.getFileById(fileId);
        return dto != null ? fileVoConverter.toVo(dto) : null;
    }

    /**
     * 分页查询文件列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    public PageResult<FileVO> listFiles(FileQueryDTO queryDTO) {
        Page<FileDTO> dtoPage = fileService.listFiles(queryDTO);

        List<FileVO> voList = fileVoConverter.toVoList(dtoPage.getRecords());

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 检查文件是否存在
     *
     * @param fileId 文件ID
     * @return 存在返回 true，否则返回 false
     */
    public boolean exists(Long fileId) {
        return fileService.exists(fileId);
    }
}
