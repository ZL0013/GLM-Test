package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.dto.FileDTO;
import com.xie.glm.common.dto.FileUploadDTO;
import com.xie.glm.common.dto.query.FileQueryDTO;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.FileConverter;
import com.xie.glm.system.domain.SysFile;
import com.xie.glm.system.mapper.SysFileMapper;
import com.xie.glm.system.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.List;

/**
 * 文件服务实现
 *
 * @author xie
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService {

    private final SysFileMapper fileMapper;
    private final FileConverter fileConverter;

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileDTO uploadFile(FileUploadDTO uploadDTO) {
        try {
            // 1. 计算文件MD5
            String md5 = calculateMD5(uploadDTO.getBytes());

            // 2. 检查文件是否已存在（根据MD5）
            SysFile existingFile = fileMapper.selectOne(
                new LambdaQueryWrapper<SysFile>()
                    .eq(SysFile::getFileMd5, md5)
            );

            if (existingFile != null) {
                log.info("文件已存在，直接返回: {}", existingFile.getFileName());
                return fileConverter.toDto(existingFile);
            }

            // 3. 生成文件存储路径
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = datePath + "/" + md5 + "_" + uploadDTO.originalFilename();
            String fullPath = uploadPath + "/" + relativePath;

            // 4. 确保目录存在
            Path targetPath = Paths.get(fullPath);
            Files.createDirectories(targetPath.getParent());

            // 5. 保存文件到磁盘
            Files.write(targetPath, uploadDTO.getBytes());

            // 6. 保存文件记录到数据库
            SysFile sysFile = new SysFile();
            sysFile.setFileName(uploadDTO.originalFilename());
            sysFile.setFilePath(relativePath);
            sysFile.setContentType(uploadDTO.contentType());
            sysFile.setFileSize(uploadDTO.size());
            sysFile.setFileMd5(md5);
            sysFile.setCreateTime(LocalDateTime.now());
            sysFile.setUpdateTime(LocalDateTime.now());

            fileMapper.insert(sysFile);

            log.info("文件上传成功: {}", sysFile.getFileName());
            return fileConverter.toDto(sysFile);

        } catch (IOException e) {
            log.error("文件上传失败: {}", uploadDTO.originalFilename(), e);
            throw new ServiceException("文件上传失败", e);
        }
    }

    @Override
    public InputStream downloadFile(Long fileId) {
        SysFile sysFile = fileMapper.selectById(fileId);
        if (sysFile == null) {
            throw new ServiceException("文件不存在: " + fileId, null);
        }

        try {
            String fullPath = uploadPath + "/" + sysFile.getFilePath();
            File file = new File(fullPath);

            if (!file.exists()) {
                throw new ServiceException("文件不存在: " + sysFile.getFileName(), null);
            }

            return new FileInputStream(file);

        } catch (IOException e) {
            log.error("读取文件失败: {}", sysFile.getFileName(), e);
            throw new ServiceException("读取文件失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Long fileId) {
        SysFile sysFile = fileMapper.selectById(fileId);
        if (sysFile == null) {
            throw new ServiceException("文件不存在: " + fileId, null);
        }

        try {
            // 删除物理文件
            String fullPath = uploadPath + "/" + sysFile.getFilePath();
            File file = new File(fullPath);
            if (file.exists()) {
                Files.deleteIfExists(file.toPath());
            }

            // 删除数据库记录
            fileMapper.deleteById(fileId);

            log.info("文件删除成功: {}", sysFile.getFileName());

        } catch (IOException e) {
            log.error("删除文件失败: {}", sysFile.getFileName(), e);
            throw new ServiceException("删除文件失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFiles(List<Long> fileIds) {
        if (fileIds == null || fileIds.isEmpty()) {
            return 0;
        }

        int deletedCount = 0;
        for (Long fileId : fileIds) {
            try {
                deleteFile(fileId);
                deletedCount++;
            } catch (Exception e) {
                log.error("删除文件失败: {}", fileId, e);
            }
        }

        return deletedCount;
    }

    @Override
    public FileDTO getFileById(Long fileId) {
        SysFile sysFile = fileMapper.selectById(fileId);
        return sysFile != null ? fileConverter.toDto(sysFile) : null;
    }

    @Override
    public Page<FileDTO> listFiles(FileQueryDTO queryDTO) {
        Page<SysFile> page = fileMapper.selectPage(
            queryDTO.toPage(),
            buildQueryWrapper(queryDTO)
        );

        Page<FileDTO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(fileConverter.toDtoList(page.getRecords()));

        return result;
    }

    @Override
    public boolean exists(Long fileId) {
        return fileMapper.selectById(fileId) != null;
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<SysFile> buildQueryWrapper(FileQueryDTO queryDTO) {
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<>();

        // 文件名模糊查询
        if (queryDTO.fileName() != null && !queryDTO.fileName().isBlank()) {
            wrapper.like(SysFile::getFileName, queryDTO.fileName());
        }

        // 时间范围查询
        if (queryDTO.startDate() != null) {
            wrapper.ge(SysFile::getCreateTime, queryDTO.startDate());
        }
        if (queryDTO.endDate() != null) {
            wrapper.le(SysFile::getCreateTime, queryDTO.endDate());
        }

        // 文件大小范围查询
        if (queryDTO.minSize() != null) {
            wrapper.ge(SysFile::getFileSize, queryDTO.minSize());
        }
        if (queryDTO.maxSize() != null) {
            wrapper.le(SysFile::getFileSize, queryDTO.maxSize());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(SysFile::getCreateTime);

        return wrapper;
    }

    /**
     * 计算文件的 MD5 值
     */
    private String calculateMD5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(data);
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("MD5计算失败", e);
        }
    }
}
