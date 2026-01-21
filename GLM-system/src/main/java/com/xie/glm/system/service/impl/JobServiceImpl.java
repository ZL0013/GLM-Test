package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.domain.SysJob;
import com.xie.glm.system.dto.JobDTO;
import com.xie.glm.system.dto.query.JobQueryDTO;
import com.xie.glm.system.mapper.SysJobMapper;
import com.xie.glm.system.service.IJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 定时任务服务实现
 *
 * <p>实现定时任务的增删改查、执行控制等业务逻辑。
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements IJobService {

    private final SysJobMapper jobMapper;

    @Override
    public PageResult<JobDTO> listJobs(JobQueryDTO query) {
        // 构建分页对象
        Page<SysJob> page = new Page<>(
            query.getPageNum() != null ? query.getPageNum() : 1,
            query.getPageSize() != null ? query.getPageSize() : 10
        );

        // 构建查询条件
        LambdaQueryWrapper<SysJob> wrapper = new LambdaQueryWrapper<>();

        // 任务名称模糊搜索
        if (query.getJobName() != null && !query.getJobName().isEmpty()) {
            wrapper.like(SysJob::getJobName, query.getJobName());
        }

        // 任务组精确查询
        if (query.getJobGroup() != null) {
            wrapper.eq(SysJob::getJobGroup, query.getJobGroup());
        }

        // 状态查询
        if (query.getStatus() != null) {
            wrapper.eq(SysJob::getStatus, query.getStatus());
        }

        // 时间范围查询
        if (query.getStartTime() != null) {
            wrapper.ge(SysJob::getCreateTime, query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le(SysJob::getCreateTime, query.getEndTime());
        }

        // 排序
        if (query.getOrderByColumn() != null && query.getIsAsc() != null) {
            // 根据排序列名选择对应的 Lambda 表达式
            if ("job_name".equalsIgnoreCase(query.getOrderByColumn())) {
                if ("asc".equals(query.getIsAsc())) {
                    wrapper.orderByAsc(SysJob::getJobName);
                } else {
                    wrapper.orderByDesc(SysJob::getJobName);
                }
            } else if ("job_group".equalsIgnoreCase(query.getOrderByColumn())) {
                if ("asc".equals(query.getIsAsc())) {
                    wrapper.orderByAsc(SysJob::getJobGroup);
                } else {
                    wrapper.orderByDesc(SysJob::getJobGroup);
                }
            } else if ("status".equalsIgnoreCase(query.getOrderByColumn())) {
                if ("asc".equals(query.getIsAsc())) {
                    wrapper.orderByAsc(SysJob::getStatus);
                } else {
                    wrapper.orderByDesc(SysJob::getStatus);
                }
            } else if ("create_time".equalsIgnoreCase(query.getOrderByColumn())) {
                if ("asc".equals(query.getIsAsc())) {
                    wrapper.orderByAsc(SysJob::getCreateTime);
                } else {
                    wrapper.orderByDesc(SysJob::getCreateTime);
                }
            } else {
                wrapper.orderByDesc(SysJob::getJobId);
            }
        } else {
            wrapper.orderByDesc(SysJob::getJobId);
        }

        // 执行分页查询
        IPage<SysJob> resultPage = jobMapper.selectPage(page, wrapper);

        // 转换为 DTO（这里简化处理，实际应使用 Converter）
        List<JobDTO> dtoList = resultPage.getRecords().stream()
            .map(this::convertToDTO)
            .toList();

        return new PageResult<>(dtoList, resultPage.getTotal());
    }

    @Override
    public JobDTO getJobById(Long jobId) {
        SysJob job = jobMapper.selectById(jobId);
        return job != null ? convertToDTO(job) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createJob(JobDTO dto) {
        // 检查任务名称唯一性
        if (!checkJobNameUnique(dto.getJobName())) {
            throw new ServiceException(BusinessStatus.JOB_NAME_DUPLICATE);
        }

        // 验证 Cron 表达式
        if (dto.getCronExpression() == null || dto.getCronExpression().isEmpty()) {
            throw new ServiceException(BusinessStatus.JOB_CRON_NULL);
        }

        // 验证调用目标
        if (dto.getInvokeTarget() == null || dto.getInvokeTarget().isEmpty()) {
            throw new ServiceException(BusinessStatus.JOB_TARGET_NULL);
        }

        // 转换为实体
        SysJob job = convertToEntity(dto);

        // 设置默认状态为暂停
        if (job.getStatus() == null) {
            job.setStatus("1");
        }

        // 插入数据库
        jobMapper.insert(job);

        return job.getJobId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(JobDTO dto) {
        if (dto.getJobId() == null) {
            throw new ServiceException(BusinessStatus.JOB_ID_NULL);
        }

        // 检查任务是否存在
        SysJob existingJob = jobMapper.selectById(dto.getJobId());
        if (existingJob == null) {
            throw new ServiceException("任务不存在: " + dto.getJobId(), null);
        }

        // 检查任务名称唯一性（排除自身）
        LambdaQueryWrapper<SysJob> wrapper = new LambdaQueryWrapper<SysJob>()
            .eq(SysJob::getJobName, dto.getJobName())
            .ne(SysJob::getJobId, dto.getJobId());
        Long count = jobMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ServiceException("任务名称已存在: " + dto.getJobName(), null);
        }

        // 转换并更新
        SysJob job = convertToEntity(dto);
        jobMapper.updateById(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(Long jobId) {
        int result = jobMapper.deleteById(jobId);
        if (result == 0) {
            throw new ServiceException("删除任务失败，任务不存在: " + jobId, null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobs(Long[] jobIds) {
        int result = jobMapper.deleteBatchIds(Arrays.asList(jobIds));
        if (result == 0) {
            throw new ServiceException("批量删除任务失败", null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long jobId, String status) {
        SysJob job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new ServiceException("任务不存在: " + jobId, null);
        }

        job.setStatus(status);
        jobMapper.updateById(job);
    }

    @Override
    public void executeJob(Long jobId) {
        SysJob job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new ServiceException("任务不存在: " + jobId, null);
        }

        if ("1".equals(job.getStatus())) {
            throw new ServiceException(BusinessStatus.JOB_PAUSED);
        }

        // TODO: 实现任务执行逻辑
        // 这里需要集成定时任务框架（如 Spring Task、Quartz 等）
        throw new ServiceException(BusinessStatus.JOB_NOT_IMPLEMENTED);
    }

    @Override
    public boolean checkJobNameUnique(String jobName) {
        LambdaQueryWrapper<SysJob> wrapper = new LambdaQueryWrapper<SysJob>()
            .eq(SysJob::getJobName, jobName);
        Long count = jobMapper.selectCount(wrapper);
        return count == 0;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 将实体转换为 DTO
     */
    private JobDTO convertToDTO(SysJob entity) {
        JobDTO dto = new JobDTO();
        dto.setJobId(entity.getJobId());
        dto.setJobName(entity.getJobName());
        dto.setJobGroup(entity.getJobGroup());
        dto.setInvokeTarget(entity.getInvokeTarget());
        dto.setInvokeParam(entity.getInvokeParam());
        dto.setCronExpression(entity.getCronExpression());
        dto.setMisfirePolicy(entity.getMisfirePolicy());
        dto.setConcurrent(entity.getConcurrent());
        dto.setStatus(entity.getStatus());
        dto.setBeginTime(entity.getBeginTime());
        dto.setNextValidTime(entity.getNextValidTime());
        dto.setExceptionInfo(entity.getExceptionInfo());
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }

    /**
     * 将 DTO 转换为实体
     */
    private SysJob convertToEntity(JobDTO dto) {
        SysJob entity = new SysJob();
        entity.setJobId(dto.getJobId());
        entity.setJobName(dto.getJobName());
        entity.setJobGroup(dto.getJobGroup());
        entity.setInvokeTarget(dto.getInvokeTarget());
        entity.setInvokeParam(dto.getInvokeParam());
        entity.setCronExpression(dto.getCronExpression());
        entity.setMisfirePolicy(dto.getMisfirePolicy());
        entity.setConcurrent(dto.getConcurrent());
        entity.setStatus(dto.getStatus());
        entity.setBeginTime(dto.getBeginTime());
        entity.setNextValidTime(dto.getNextValidTime());
        entity.setExceptionInfo(dto.getExceptionInfo());
        entity.setRemark(dto.getRemark());
        return entity;
    }

}
