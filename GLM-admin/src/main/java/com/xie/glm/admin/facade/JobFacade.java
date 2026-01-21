package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.JobVoConverter;
import com.xie.glm.admin.vo.JobVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.JobDTO;
import com.xie.glm.system.dto.query.JobQueryDTO;
import com.xie.glm.system.service.IJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务门面类
 *
 * <p>封装定时任务管理的业务逻辑调用，负责 DTO → VO 的转换。
 *
 * <p>职责：
 * <ul>
 *   <li>调用 Service 层获取 DTO 数据</li>
 *   <li>使用 JobVoConverter 将 DTO 转换为 VO</li>
 *   <li>组装展示数据（如状态文本、执行策略文本等）</li>
 *   <li>简化 Controller 的逻辑</li>
 * </ul>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class JobFacade {

    private final IJobService jobService;
    private final JobVoConverter voConverter;

    /**
     * 分页查询任务列表
     *
     * @param query 查询条件
     * @return 分页结果（VO）
     */
    public PageResult<JobVO> listJobs(JobQueryDTO query) {
        // 调用 Service 获取 DTO 分页数据
        PageResult<JobDTO> dtoPage = jobService.listJobs(query);

        // 转换 DTO 为 VO
        List<JobVO> voList = voConverter.toVoList(dtoPage.getRecords());

        // 设置展示文本
        voList.forEach(this::setDisplayTexts);

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 根据 ID 查询任务详情
     *
     * @param jobId 任务 ID
     * @return 任务 VO
     */
    public JobVO getJobById(Long jobId) {
        // 调用 Service 获取 DTO
        JobDTO dto = jobService.getJobById(jobId);

        // 转换 DTO 为 VO
        JobVO vo = dto != null ? voConverter.toVo(dto) : null;

        // 设置展示文本
        if (vo != null) {
            setDisplayTexts(vo);
        }

        return vo;
    }

    /**
     * 创建任务
     *
     * @param dto 任务 DTO
     * @return 创建的任务 ID
     */
    public Long createJob(JobDTO dto) {
        return jobService.createJob(dto);
    }

    /**
     * 更新任务信息
     *
     * @param dto 任务 DTO
     */
    public void updateJob(JobDTO dto) {
        jobService.updateJob(dto);
    }

    /**
     * 删除任务
     *
     * @param jobId 任务 ID
     */
    public void deleteJob(Long jobId) {
        jobService.deleteJob(jobId);
    }

    /**
     * 批量删除任务
     *
     * @param jobIds 任务 ID 数组
     */
    public void deleteJobs(Long[] jobIds) {
        jobService.deleteJobs(jobIds);
    }

    /**
     * 更新任务状态
     *
     * @param jobId 任务 ID
     * @param status 状态（0=正常，1=暂停）
     */
    public void updateStatus(Long jobId, String status) {
        jobService.updateStatus(jobId, status);
    }

    /**
     * 立即执行任务
     *
     * @param jobId 任务 ID
     */
    public void executeJob(Long jobId) {
        jobService.executeJob(jobId);
    }

    /**
     * 检查任务名称是否唯一
     *
     * @param jobName 任务名称
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkJobNameUnique(String jobName) {
        return jobService.checkJobNameUnique(jobName);
    }

    /**
     * 设置展示文本
     *
     * @param vo 任务 VO
     */
    private void setDisplayTexts(JobVO vo) {
        // 设置状态文本
        if ("0".equals(vo.getStatus())) {
            vo.setStatusText("正常");
        } else if ("1".equals(vo.getStatus())) {
            vo.setStatusText("暂停");
        }

        // 设置执行策略文本
        if ("1".equals(vo.getMisfirePolicy())) {
            vo.setMisfirePolicyText("立即执行");
        } else if ("2".equals(vo.getMisfirePolicy())) {
            vo.setMisfirePolicyText("执行一次");
        } else if ("3".equals(vo.getMisfirePolicy())) {
            vo.setMisfirePolicyText("放弃执行");
        }

        // 设置并发标识文本
        if ("0".equals(vo.getConcurrent())) {
            vo.setConcurrentText("禁止");
        } else if ("1".equals(vo.getConcurrent())) {
            vo.setConcurrentText("允许");
        }
    }
}
