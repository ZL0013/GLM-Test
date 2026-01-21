package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.OperLogDTO;
import com.xie.glm.system.dto.query.LogQueryDTO;

/**
 * 操作日志服务接口
 *
 * <p>定义操作日志管理的业务逻辑方法，包括操作日志的增删改查、清空等功能。
 *
 * <p>方法说明：
 * <ul>
 *   <li>分页查询操作日志列表</li>
 *   <li>根据 ID 查询操作日志详情</li>
 *   <li>保存操作日志</li>
 *   <li>删除操作日志</li>
 *   <li>批量删除操作日志</li>
 *   <li>清空所有操作日志</li>
 * </ul>
 *
 * @author xie
 */
public interface IOperLogService {

    /**
     * 分页查询操作日志列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<OperLogDTO> listOperLogs(LogQueryDTO query);

    /**
     * 根据 ID 查询操作日志详情
     *
     * @param operId 操作日志 ID
     * @return 操作日志 DTO，如果不存在返回 null
     */
    OperLogDTO getOperLogById(Long operId);

    /**
     * 保存操作日志
     *
     * <p>保存操作日志时：
     * <ul>
     *   <li>自动设置操作时间</li>
     *   <li>根据业务类型和状态设置对应的名称</li>
     * </ul>
     *
     * @param dto 操作日志 DTO
     * @return 保存的操作日志 ID
     */
    Long saveOperLog(OperLogDTO dto);

    /**
     * 删除操作日志
     *
     * <p>物理删除，直接从数据库中删除记录
     *
     * @param operId 操作日志 ID
     */
    void deleteOperLog(Long operId);

    /**
     * 批量删除操作日志
     *
     * <p>物理删除，直接从数据库中删除记录
     *
     * @param operIds 操作日志 ID 数组
     */
    void deleteOperLogs(Long[] operIds);

    /**
     * 清空所有操作日志
     *
     * <p>物理删除，清空所有操作日志记录
     */
    void cleanOperLogs();
}
