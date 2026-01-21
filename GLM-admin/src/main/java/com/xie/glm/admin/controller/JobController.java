package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.JobFacade;
import com.xie.glm.admin.vo.JobVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.JobDTO;
import com.xie.glm.system.dto.query.JobQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务管理控制器
 *
 * <p>提供定时任务 CRUD 操作的 REST API。
 *
 * <p>接口列表：
 * <ul>
 *   <li>GET /api/system/jobs - 分页查询任务列表</li>
 *   <li>GET /api/system/jobs/{id} - 查询任务详情</li>
 *   <li>POST /api/system/jobs - 创建任务</li>
 *   <li>PUT /api/system/jobs/{id} - 更新任务</li>
 *   <li>DELETE /api/system/jobs/{id} - 删除任务</li>
 *   <li>DELETE /api/system/jobs - 批量删除任务</li>
 *   <li>PUT /api/system/jobs/{id}/status - 修改任务状态</li>
 *   <li>POST /api/system/jobs/{id}/execute - 立即执行任务</li>
 * </ul>
 *
 * <p>权限要求：
 * <ul>
 *   <li>system:job:list - 查看任务列表</li>
 *   <li>system:job:query - 查看任务详情</li>
 *   <li>system:job:add - 创建任务</li>
 *   <li>system:job:edit - 编辑任务</li>
 *   <li>system:job:remove - 删除任务</li>
 * </ul>
 *
 * <p>所有接口返回值由 ResponseAdvice 自动包装为 {@link Result} 格式。
 *
 * @author xie
 */
@Tag(name = "定时任务管理", description = "定时任务CRUD操作接口")
@RestController
@RequestMapping("/api/system/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobFacade jobFacade;

    /**
     * 分页查询任务列表
     *
     * @param query 查询条件（任务名称、任务组、状态等）
     * @return 分页结果（包含任务列表和总记录数），由 ResponseAdvice 自动包装
     */
    @GetMapping
    @Operation(summary = "分页查询任务列表", description = "支持按任务名称、任务组、状态等条件查询")
    @PreAuthorize("hasAuthority('system:job:list')")
    public PageResult<JobVO> list(JobQueryDTO query) {
        return jobFacade.listJobs(query);
    }

    /**
     * 查询任务详情
     *
     * @param id 任务 ID
     * @return 任务详情，由 ResponseAdvice 自动包装
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询任务详情", description = "根据任务ID查询任务详细信息")
    @PreAuthorize("hasAuthority('system:job:query')")
    public JobVO getDetail(@PathVariable Long id) {
        return jobFacade.getJobById(id);
    }

    /**
     * 创建任务
     *
     * @param dto 创建任务 DTO（包含任务名称、Cron表达式、调用目标等）
     * @return 创建的任务 ID，由 ResponseAdvice 自动包装
     */
    @PostMapping
    @Operation(summary = "创建任务", description = "创建新的定时任务")
    @PreAuthorize("hasAuthority('system:job:add')")
    public Long create(@Valid @RequestBody JobDTO dto) {
        return jobFacade.createJob(dto);
    }

    /**
     * 更新任务
     *
     * @param id 任务 ID
     * @param dto 更新任务 DTO（包含需要更新的字段）
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新任务", description = "更新任务基本信息")
    @PreAuthorize("hasAuthority('system:job:edit')")
    public void update(@PathVariable Long id, @Valid @RequestBody JobDTO dto) {
        dto.setJobId(id);
        jobFacade.updateJob(dto);
    }

    /**
     * 删除任务
     *
     * @param id 任务 ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除任务", description = "根据任务ID删除任务")
    @PreAuthorize("hasAuthority('system:job:remove')")
    public void delete(@PathVariable Long id) {
        jobFacade.deleteJob(id);
    }

    /**
     * 批量删除任务
     *
     * @param jobIds 任务 ID 数组
     */
    @DeleteMapping
    @Operation(summary = "批量删除任务", description = "根据任务ID数组批量删除任务")
    @PreAuthorize("hasAuthority('system:job:remove')")
    public void deleteBatch(@RequestBody Long[] jobIds) {
        jobFacade.deleteJobs(jobIds);
    }

    /**
     * 修改任务状态
     *
     * @param id 任务 ID
     * @param status 状态（0=正常，1=暂停）
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "修改任务状态", description = "暂停或恢复任务")
    @PreAuthorize("hasAuthority('system:job:edit')")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        jobFacade.updateStatus(id, status);
    }

    /**
     * 立即执行任务
     *
     * @param id 任务 ID
     */
    @PostMapping("/{id}/execute")
    @Operation(summary = "立即执行任务", description = "立即执行指定的定时任务")
    @PreAuthorize("hasAuthority('system:job:edit')")
    public void execute(@PathVariable Long id) {
        jobFacade.executeJob(id);
    }
}
