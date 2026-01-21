package com.xie.glm.admin.controller;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.core.Result;
import com.xie.glm.system.dto.NoticeDTO;
import com.xie.glm.system.dto.query.NoticeQueryDTO;
import com.xie.glm.admin.facade.NoticeFacade;
import com.xie.glm.admin.vo.NoticeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 通知公告控制器
 *
 * @author xie
 */
@Tag(name = "通知公告管理")
@RestController
@RequestMapping("/api/system/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeFacade noticeFacade;

    /**
     * 分页查询通知公告列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询通知公告列表")
    @GetMapping
    @PreAuthorize("hasAuthority('system:notice:list')")
    public Result<PageResult<NoticeVO>> list(NoticeQueryDTO query) {
        return Result.success(noticeFacade.listNotices(query));
    }

    /**
     * 根据ID查询通知公告详情
     *
     * @param noticeId 通知ID
     * @return 通知公告详情
     */
    @Operation(summary = "查询通知公告详情")
    @GetMapping("/{noticeId}")
    @PreAuthorize("hasAuthority('system:notice:query')")
    public Result<NoticeVO> getInfo(@PathVariable Long noticeId) {
        return Result.success(noticeFacade.getNoticeById(noticeId));
    }

    /**
     * 创建通知公告
     *
     * @param dto 通知公告数据
     * @return 成功结果
     */
    @Operation(summary = "创建通知公告")
    @PostMapping
    @PreAuthorize("hasAuthority('system:notice:add')")
    public Result<Void> create(@Valid @RequestBody NoticeDTO dto) {
        noticeFacade.createNotice(dto);
        return Result.success();
    }

    /**
     * 更新通知公告
     *
     * @param noticeId 通知ID
     * @param dto 通知公告数据
     * @return 成功结果
     */
    @Operation(summary = "更新通知公告")
    @PutMapping("/{noticeId}")
    @PreAuthorize("hasAuthority('system:notice:edit')")
    public Result<Void> update(@PathVariable Long noticeId, @Valid @RequestBody NoticeDTO dto) {
        dto.setNoticeId(noticeId);
        noticeFacade.updateNotice(dto);
        return Result.success();
    }

    /**
     * 删除通知公告
     *
     * @param noticeId 通知ID
     * @return 成功结果
     */
    @Operation(summary = "删除通知公告")
    @DeleteMapping("/{noticeId}")
    @PreAuthorize("hasAuthority('system:notice:remove')")
    public Result<Void> delete(@PathVariable Long noticeId) {
        noticeFacade.deleteNotice(noticeId);
        return Result.success();
    }
}
