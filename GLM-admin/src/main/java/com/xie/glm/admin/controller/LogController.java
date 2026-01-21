package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.OperLogFacade;
import com.xie.glm.admin.vo.OperLogVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.query.LogQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志管理控制器
 *
 * <p>提供操作日志查询和删除的 REST API。
 *
 * <p>接口列表：
 * <ul>
 *   <li>GET /api/system/oper-logs - 分页查询操作日志列表</li>
 *   <li>GET /api/system/oper-logs/{id} - 查询操作日志详情</li>
 *   <li>DELETE /api/system/oper-logs/{id} - 删除操作日志</li>
 *   <li>DELETE /api/system/oper-logs - 批量删除操作日志</li>
 *   <li>DELETE /api/system/oper-logs/clean - 清空所有操作日志</li>
 * </ul>
 *
 * <p>权限要求：
 * <ul>
 *   <li>system:operlog:list - 查看操作日志列表</li>
 *   <li>system:operlog:query - 查看操作日志详情</li>
 *   <li>system:operlog:remove - 删除操作日志</li>
 * </ul>
 *
 * <p>所有接口返回值由 ResponseAdvice 自动包装为 {@link Result} 格式。
 *
 * @author xie
 */
@Tag(name = "日志管理", description = "操作日志查询和删除接口")
@RestController
@RequestMapping("/api/system/oper-logs")
@RequiredArgsConstructor
public class LogController {

    private final OperLogFacade operLogFacade;

    /**
     * 分页查询操作日志列表
     *
     * @param query 查询条件（标题、业务类型、操作人、状态、时间范围等）
     * @return 分页结果（包含操作日志列表和总记录数），由 ResponseAdvice 自动包装
     */
    @GetMapping
    @Operation(summary = "分页查询操作日志列表", description = "支持按标题、业务类型、操作人、状态、时间范围等条件查询")
    @PreAuthorize("hasAuthority('system:operlog:list')")
    public PageResult<OperLogVO> list(LogQueryDTO query) {
        return operLogFacade.listOperLogs(query);
    }

    /**
     * 查询操作日志详情
     *
     * @param id 操作日志 ID
     * @return 操作日志详情，由 ResponseAdvice 自动包装
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询操作日志详情", description = "根据操作日志ID查询详细信息")
    @PreAuthorize("hasAuthority('system:operlog:query')")
    public OperLogVO getDetail(@PathVariable Long id) {
        return operLogFacade.getOperLogById(id);
    }

    /**
     * 删除操作日志
     *
     * @param id 操作日志 ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除操作日志", description = "根据操作日志ID删除日志")
    @PreAuthorize("hasAuthority('system:operlog:remove')")
    public void delete(@PathVariable Long id) {
        operLogFacade.deleteOperLog(id);
    }

    /**
     * 批量删除操作日志
     *
     * @param operIds 操作日志 ID 数组
     */
    @DeleteMapping
    @Operation(summary = "批量删除操作日志", description = "根据操作日志ID数组批量删除日志")
    @PreAuthorize("hasAuthority('system:operlog:remove')")
    public void deleteBatch(@RequestBody Long[] operIds) {
        operLogFacade.deleteOperLogs(operIds);
    }

    /**
     * 清空所有操作日志
     */
    @DeleteMapping("/clean")
    @Operation(summary = "清空所有操作日志", description = "清空所有操作日志记录")
    @PreAuthorize("hasAuthority('system:operlog:remove')")
    public void clean() {
        operLogFacade.cleanOperLogs();
    }
}
