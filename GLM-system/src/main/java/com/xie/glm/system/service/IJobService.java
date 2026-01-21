package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.JobDTO;
import com.xie.glm.system.dto.query.JobQueryDTO;

/**
 * 定时任务服务接口
 *
 * <p>定义定时任务管理的业务逻辑方法，包括任务的增删改查、执行控制等功能。
 *
 * <p>方法说明：
 * <ul>
 *   <li>分页查询任务列表</li>
 *   <li>根据 ID 查询任务详情</li>
 *   <li>创建任务</li>
 *   <li>更新任务信息</li>
 *   <li>删除任务</li>
 *   <li>批量删除任务</li>
 *   <li>更新任务状态（暂停/恢复）</li>
 *   <li>立即执行任务</li>
 *   <li>查询任务日志列表</li>
 * </ul>
 *
 * @author xie
 */
public interface IJobService {

    /**
     * 分页查询任务列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<JobDTO> listJobs(JobQueryDTO query);

    /**
     * 根据 ID 查询任务详情
     *
     * @param jobId 任务 ID
     * @return 任务 DTO，如果不存在返回 null
     */
    JobDTO getJobById(Long jobId);

    /**
     * 创建任务
     *
     * <p>创建任务时：
     * <ul>
     *   <li>会验证 Cron 表达式格式</li>
     *   <li>会验证调用目标类存在性</li>
     *   <li>会设置默认状态为暂停</li>
     * </ul>
     *
     * @param dto 任务 DTO
     * @return 创建的任务 ID
     */
    Long createJob(JobDTO dto);

    /**
     * 更新任务信息
     *
     * <p>更新任务时：
     * <ul>
     *   <li>仅更新 DTO 中非 null 的字段</li>
     *   <li>不允许修改任务 ID</li>
     * </ul>
     *
     * @param dto 任务 DTO
     */
    void updateJob(JobDTO dto);

    /**
     * 删除任务
     *
     * @param jobId 任务 ID
     */
    void deleteJob(Long jobId);

    /**
     * 批量删除任务
     *
     * @param jobIds 任务 ID 列表
     */
    void deleteJobs(Long[] jobIds);

    /**
     * 更新任务状态
     *
     * <p>暂停或恢复任务
     *
     * @param jobId 任务 ID
     * @param status 状态（0=正常，1=暂停）
     */
    void updateStatus(Long jobId, String status);

    /**
     * 立即执行任务
     *
     * @param jobId 任务 ID
     */
    void executeJob(Long jobId);

    /**
     * 检查任务名称是否唯一
     *
     * @param jobName 任务名称
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkJobNameUnique(String jobName);
}
