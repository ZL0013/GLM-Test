package com.xie.glm.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.admin.facade.FileFacade;
import com.xie.glm.admin.vo.FileVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.core.Result;
import com.xie.glm.common.dto.FileDTO;
import com.xie.glm.common.dto.query.FileQueryDTO;
import com.xie.glm.system.service.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 文件管理控制器
 *
 * @author xie
 */
@Tag(name = "文件管理", description = "文件上传下载管理接口")
@RestController
@RequestMapping("/api/system/files")
@RequiredArgsConstructor
public class FileController {

    private final FileFacade fileFacade;
    private final IFileService fileService;

    /**
     * 上传文件
     */
    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('system:file:upload')")
    public Result<FileVO> upload(@RequestParam("file") MultipartFile file) {
        FileVO vo = fileFacade.uploadFile(file);
        return Result.success(vo);
    }

    /**
     * 下载文件
     */
    @Operation(summary = "下载文件")
    @GetMapping("/download/{fileId}")
    @PreAuthorize("hasAuthority('system:file:download')")
    public void download(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        FileDTO file = fileService.getFileById(fileId);
        if (file == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("文件不存在");
            return;
        }

        response.setContentType(file.contentType());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename*=UTF-8''" + java.net.URLEncoder.encode(file.fileName(), StandardCharsets.UTF_8));

        try (InputStream inputStream = fileService.downloadFile(fileId);
             OutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }
    }

    /**
     * 删除文件
     */
    @Operation(summary = "删除文件")
    @DeleteMapping("/{fileId}")
    @PreAuthorize("hasAuthority('system:file:remove')")
    public Result<Void> delete(@PathVariable Long fileId) {
        fileFacade.deleteFile(fileId);
        return Result.success();
    }

    /**
     * 批量删除文件
     */
    @Operation(summary = "批量删除文件")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('system:file:remove')")
    public Result<Integer> deleteBatch(@RequestBody List<Long> fileIds) {
        int count = fileFacade.deleteFiles(fileIds);
        return Result.success(count);
    }

    /**
     * 查询文件详情
     */
    @Operation(summary = "查询文件详情")
    @GetMapping("/{fileId}")
    @PreAuthorize("hasAuthority('system:file:query')")
    public Result<FileVO> getById(@PathVariable Long fileId) {
        FileVO vo = fileFacade.getFileById(fileId);
        return Result.success(vo);
    }

    /**
     * 分页查询文件列表
     */
    @Operation(summary = "分页查询文件列表")
    @GetMapping
    @PreAuthorize("hasAuthority('system:file:list')")
    public Result<PageResult<FileVO>> list(FileQueryDTO queryDTO) {
        PageResult<FileVO> result = fileFacade.listFiles(queryDTO);
        return Result.success(result);
    }
}
