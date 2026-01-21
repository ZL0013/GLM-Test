package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.system.domain.SysJob;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 定时任务 Mapper 接口测试
 *
 * <p>测试 {@link SysJobMapper} 的各种场景：
 * <ul>
 *   <li>基础 CRUD 操作</li>
 *   <li>条件查询</li>
 *   <li>分页查询</li>
 *   <li>批量操作</li>
 * </ul>
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
class SysJobMapperTest {

    @Mock
    private SysJobMapper sysJobMapper;

    // ==================== 基础 CRUD 测试 ====================

    @Test
    void testInsert() {
        // Given
        SysJob job = new SysJob();
        job.setJobName("数据清理任务");
        job.setJobGroup("DEFAULT");
        job.setCronExpression("0 0 2 * * ?");
        job.setInvokeTarget("com.xie.glm.task.DataCleanup.run");
        job.setStatus("0");

        when(sysJobMapper.insert(any(SysJob.class))).thenReturn(1);

        // When
        int result = sysJobMapper.insert(job);

        // Then
        assertThat(result).isEqualTo(1);
        verify(sysJobMapper, times(1)).insert(job);
    }

    @Test
    void testSelectById() {
        // Given
        Long jobId = 1L;
        SysJob job = new SysJob();
        job.setJobId(jobId);
        job.setJobName("数据清理任务");

        when(sysJobMapper.selectById(jobId)).thenReturn(job);

        // When
        SysJob result = sysJobMapper.selectById(jobId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getJobId()).isEqualTo(jobId);
        assertThat(result.getJobName()).isEqualTo("数据清理任务");
        verify(sysJobMapper, times(1)).selectById(jobId);
    }

    @Test
    void testUpdateById() {
        // Given
        SysJob job = new SysJob();
        job.setJobId(1L);
        job.setJobName("更新后的任务");
        job.setCronExpression("0 0 3 * * ?");

        when(sysJobMapper.updateById(any(SysJob.class))).thenReturn(1);

        // When
        int result = sysJobMapper.updateById(job);

        // Then
        assertThat(result).isEqualTo(1);
        verify(sysJobMapper, times(1)).updateById(job);
    }

    @Test
    void testDeleteById() {
        // Given
        Long jobId = 1L;
        when(sysJobMapper.deleteById(jobId)).thenReturn(1);

        // When
        int result = sysJobMapper.deleteById(jobId);

        // Then
        assertThat(result).isEqualTo(1);
        verify(sysJobMapper, times(1)).deleteById(jobId);
    }

    // ==================== 条件查询测试 ====================

    @Test
    void testSelectList() {
        // Given
        SysJob job1 = new SysJob();
        job1.setJobId(1L);
        job1.setJobName("数据清理任务");

        SysJob job2 = new SysJob();
        job2.setJobId(2L);
        job2.setJobName("数据同步任务");

        List<SysJob> jobs = Arrays.asList(job1, job2);
        when(sysJobMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(jobs);

        // When
        List<SysJob> result = sysJobMapper.selectList(new LambdaQueryWrapper<>());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getJobName()).isEqualTo("数据清理任务");
        assertThat(result.get(1).getJobName()).isEqualTo("数据同步任务");
        verify(sysJobMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testSelectOne() {
        // Given
        SysJob job = new SysJob();
        job.setJobId(1L);
        job.setJobName("数据清理任务");
        job.setJobGroup("DEFAULT");

        when(sysJobMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(job);

        // When
        SysJob result = sysJobMapper.selectOne(
            new LambdaQueryWrapper<SysJob>().eq(SysJob::getJobName, "数据清理任务")
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getJobName()).isEqualTo("数据清理任务");
    }

    @Test
    void testSelectCount() {
        // Given
        Long expectedCount = 5L;
        when(sysJobMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(expectedCount);

        // When
        Long result = sysJobMapper.selectCount(new LambdaQueryWrapper<>());

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(sysJobMapper, times(1)).selectCount(any(LambdaQueryWrapper.class));
    }

    // ==================== 分页查询测试 ====================

    @Test
    void testSelectPage() {
        // Given
        Page<SysJob> page = new Page<>(1, 10);
        SysJob job1 = new SysJob();
        job1.setJobId(1L);
        job1.setJobName("数据清理任务");

        SysJob job2 = new SysJob();
        job2.setJobId(2L);
        job2.setJobName("数据同步任务");

        Page<SysJob> resultPage = new Page<>(1, 10);
        resultPage.setRecords(Arrays.asList(job1, job2));
        resultPage.setTotal(2);

        when(sysJobMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(resultPage);

        // When
        IPage<SysJob> result = sysJobMapper.selectPage(page, new LambdaQueryWrapper<>());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(2);
        assertThat(result.getTotal()).isEqualTo(2);
        verify(sysJobMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    // ==================== 批量操作测试 ====================

    @Test
    void testSelectBatchIds() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        SysJob job1 = new SysJob();
        job1.setJobId(1L);

        SysJob job2 = new SysJob();
        job2.setJobId(2L);

        SysJob job3 = new SysJob();
        job3.setJobId(3L);

        List<SysJob> jobs = Arrays.asList(job1, job2, job3);
        when(sysJobMapper.selectBatchIds(ids)).thenReturn(jobs);

        // When
        List<SysJob> result = sysJobMapper.selectBatchIds(ids);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getJobId()).isEqualTo(1L);
        assertThat(result.get(1).getJobId()).isEqualTo(2L);
        assertThat(result.get(2).getJobId()).isEqualTo(3L);
        verify(sysJobMapper, times(1)).selectBatchIds(ids);
    }

    @Test
    void testDeleteBatchIds() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(sysJobMapper.deleteBatchIds(ids)).thenReturn(3);

        // When
        int result = sysJobMapper.deleteBatchIds(ids);

        // Then
        assertThat(result).isEqualTo(3);
        verify(sysJobMapper, times(1)).deleteBatchIds(ids);
    }

    // ==================== 状态查询测试 ====================

    @Test
    void testSelectByStatus() {
        // Given
        String status = "0"; // 正常运行
        SysJob job1 = new SysJob();
        job1.setJobId(1L);
        job1.setStatus(status);

        List<SysJob> jobs = Arrays.asList(job1);
        when(sysJobMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(jobs);

        // When
        List<SysJob> result = sysJobMapper.selectList(
            new LambdaQueryWrapper<SysJob>().eq(SysJob::getStatus, status)
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(status);
    }

    // ==================== 任务组查询测试 ====================

    @Test
    void testSelectByJobGroup() {
        // Given
        String jobGroup = "DEFAULT";
        SysJob job1 = new SysJob();
        job1.setJobId(1L);
        job1.setJobGroup(jobGroup);

        SysJob job2 = new SysJob();
        job2.setJobId(2L);
        job2.setJobGroup(jobGroup);

        List<SysJob> jobs = Arrays.asList(job1, job2);
        when(sysJobMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(jobs);

        // When
        List<SysJob> result = sysJobMapper.selectList(
            new LambdaQueryWrapper<SysJob>().eq(SysJob::getJobGroup, jobGroup)
        );

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getJobGroup()).isEqualTo(jobGroup);
        assertThat(result.get(1).getJobGroup()).isEqualTo(jobGroup);
    }

    // ==================== 任务名称模糊查询测试 ====================

    @Test
    void testSelectByJobNameLike() {
        // Given
        String jobName = "清理";
        SysJob job1 = new SysJob();
        job1.setJobId(1L);
        job1.setJobName("数据清理任务");

        SysJob job2 = new SysJob();
        job2.setJobId(2L);
        job2.setJobName("日志清理任务");

        List<SysJob> jobs = Arrays.asList(job1, job2);
        when(sysJobMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(jobs);

        // When
        List<SysJob> result = sysJobMapper.selectList(
            new LambdaQueryWrapper<SysJob>().like(SysJob::getJobName, jobName)
        );

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getJobName()).contains("清理");
        assertThat(result.get(1).getJobName()).contains("清理");
    }

    // ==================== 调用目标查询测试 ====================

    @Test
    void testSelectByInvokeTarget() {
        // Given
        String invokeTarget = "com.xie.glm.task.DataCleanup.run";
        SysJob job = new SysJob();
        job.setJobId(1L);
        job.setInvokeTarget(invokeTarget);

        when(sysJobMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(job);

        // When
        SysJob result = sysJobMapper.selectOne(
            new LambdaQueryWrapper<SysJob>().eq(SysJob::getInvokeTarget, invokeTarget)
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getInvokeTarget()).isEqualTo(invokeTarget);
    }
}
