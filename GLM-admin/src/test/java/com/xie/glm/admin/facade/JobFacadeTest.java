package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.JobVoConverter;
import com.xie.glm.admin.vo.JobVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.dto.JobDTO;
import com.xie.glm.system.dto.query.JobQueryDTO;
import com.xie.glm.system.service.IJobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JobFacade 定时任务门面测试类
 *
 * <p>测试定时任务门面层，封装 Service 调用和 DTO → VO 转换。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JobFacade 定时任务门面单元测试")
class JobFacadeTest {

    @Mock
    private IJobService jobService;

    @Mock
    private JobVoConverter voConverter;

    @InjectMocks
    private JobFacade jobFacade;

    private JobDTO testJobDTO;
    private JobVO testJobVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testJobDTO = createTestJobDTO(1L, "数据清理任务", "DEFAULT");
        testJobVO = createTestJobVO(1L, "数据清理任务", "DEFAULT");
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询任务列表 - 成功")
    void testListJobs_Success() {
        // Given
        JobQueryDTO query = new JobQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setJobName("清理");

        PageResult<JobDTO> dtoPage = new PageResult<>(
            Arrays.asList(testJobDTO),
            1L
        );

        when(jobService.listJobs(any(JobQueryDTO.class)))
            .thenReturn(dtoPage);
        when(voConverter.toVoList(anyList()))
            .thenReturn(Arrays.asList(testJobVO));

        // When
        PageResult<JobVO> result = jobFacade.listJobs(query);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getRecords().get(0).getJobName()).isEqualTo("数据清理任务");

        verify(jobService).listJobs(query);
        verify(voConverter).toVoList(Arrays.asList(testJobDTO));
    }

    // ==================== 根据ID查询任务测试 ====================

    @Test
    @DisplayName("根据ID查询任务 - 成功")
    void testGetJobById_Success() {
        // Given
        Long jobId = 1L;
        when(jobService.getJobById(jobId)).thenReturn(testJobDTO);
        when(voConverter.toVo(testJobDTO)).thenReturn(testJobVO);

        // When
        JobVO result = jobFacade.getJobById(jobId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getJobId()).isEqualTo(1L);
        assertThat(result.getJobName()).isEqualTo("数据清理任务");
        assertThat(result.getStatusText()).isEqualTo("正常"); // Facade 层设置

        verify(jobService).getJobById(jobId);
        verify(voConverter).toVo(testJobDTO);
    }

    @Test
    @DisplayName("根据ID查询任务 - 任务不存在")
    void testGetJobById_NotFound() {
        // Given
        Long jobId = 999L;
        when(jobService.getJobById(jobId))
            .thenThrow(new ServiceException("任务不存在"));

        // When & Then
        assertThatThrownBy(() -> jobFacade.getJobById(jobId))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("任务不存在");

        verify(jobService).getJobById(jobId);
    }

    // ==================== 创建任务测试 ====================

    @Test
    @DisplayName("创建任务 - 成功")
    void testCreateJob_Success() {
        // Given
        JobDTO dto = new JobDTO();
        dto.setJobName("新任务");
        dto.setJobGroup("DEFAULT");
        dto.setInvokeTarget("com.xie.glm.task.NewTask.run");
        dto.setCronExpression("0 0 1 * * ?");

        Long expectedJobId = 1L;
        when(jobService.createJob(dto)).thenReturn(expectedJobId);

        // When
        Long result = jobFacade.createJob(dto);

        // Then
        assertThat(result).isEqualTo(expectedJobId);
        verify(jobService).createJob(dto);
    }

    @Test
    @DisplayName("创建任务 - 任务名称已存在")
    void testCreateJob_JobNameExists() {
        // Given
        JobDTO dto = new JobDTO();
        dto.setJobName("数据清理任务");  // 已存在的任务名

        when(jobService.createJob(dto))
            .thenThrow(new ServiceException("任务名称已存在"));

        // When & Then
        assertThatThrownBy(() -> jobFacade.createJob(dto))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("任务名称已存在");

        verify(jobService).createJob(dto);
    }

    // ==================== 更新任务测试 ====================

    @Test
    @DisplayName("更新任务 - 成功")
    void testUpdateJob_Success() {
        // Given
        JobDTO dto = new JobDTO();
        dto.setJobId(1L);
        dto.setJobName("更新后的任务");
        dto.setCronExpression("0 0 3 * * ?");

        doNothing().when(jobService).updateJob(any(JobDTO.class));

        // When
        jobFacade.updateJob(dto);

        // Then
        verify(jobService).updateJob(dto);
    }

    @Test
    @DisplayName("更新任务 - 任务不存在")
    void testUpdateJob_NotFound() {
        // Given
        JobDTO dto = new JobDTO();
        dto.setJobId(999L);

        doThrow(new ServiceException("任务不存在"))
            .when(jobService).updateJob(any(JobDTO.class));

        // When & Then
        assertThatThrownBy(() -> jobFacade.updateJob(dto))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("任务不存在");

        verify(jobService).updateJob(dto);
    }

    // ==================== 删除任务测试 ====================

    @Test
    @DisplayName("删除任务 - 成功")
    void testDeleteJob_Success() {
        // Given
        Long jobId = 1L;
        doNothing().when(jobService).deleteJob(jobId);

        // When
        jobFacade.deleteJob(jobId);

        // Then
        verify(jobService).deleteJob(jobId);
    }

    @Test
    @DisplayName("批量删除任务 - 成功")
    void testDeleteJobs_Success() {
        // Given
        Long[] jobIds = {1L, 2L, 3L};
        doNothing().when(jobService).deleteJobs(any(Long[].class));

        // When
        jobFacade.deleteJobs(jobIds);

        // Then
        verify(jobService).deleteJobs(jobIds);
    }

    // ==================== 更新状态测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 0, 正常",
            "1, 1, 暂停"
    })
    @DisplayName("更新任务状态 - 成功")
    void testUpdateStatus_Success(Long jobId, String status, String description) {
        // Given
        doNothing().when(jobService).updateStatus(jobId, status);

        // When
        jobFacade.updateStatus(jobId, status);

        // Then
        verify(jobService).updateStatus(jobId, status);
    }

    // ==================== 立即执行任务测试 ====================

    @Test
    @DisplayName("立即执行任务 - 成功")
    void testExecuteJob_Success() {
        // Given
        Long jobId = 1L;
        doNothing().when(jobService).executeJob(jobId);

        // When
        jobFacade.executeJob(jobId);

        // Then
        verify(jobService).executeJob(jobId);
    }

    // ==================== 唯一性检查测试 ====================

    @ParameterizedTest
    @CsvSource({
            "新任务, true",
            "数据清理任务, false"
    })
    @DisplayName("检查任务名称唯一性")
    void testCheckJobNameUnique(String jobName, boolean expected) {
        // Given
        when(jobService.checkJobNameUnique(jobName)).thenReturn(expected);

        // When
        boolean result = jobFacade.checkJobNameUnique(jobName);

        // Then
        assertThat(result).isEqualTo(expected);
        verify(jobService).checkJobNameUnique(jobName);
    }

    // ==================== 辅助方法 ====================

    private JobDTO createTestJobDTO(Long jobId, String jobName, String jobGroup) {
        JobDTO dto = new JobDTO();
        dto.setJobId(jobId);
        dto.setJobName(jobName);
        dto.setJobGroup(jobGroup);
        dto.setInvokeTarget("com.xie.glm.task.TestTask.run");
        dto.setCronExpression("0 0 2 * * ?");
        dto.setMisfirePolicy("1");
        dto.setConcurrent("0");
        dto.setStatus("0");
        dto.setCreateTime(LocalDateTime.now());
        dto.setUpdateTime(LocalDateTime.now());
        return dto;
    }

    private JobVO createTestJobVO(Long jobId, String jobName, String jobGroup) {
        return JobVO.builder()
            .jobId(jobId)
            .jobName(jobName)
            .jobGroup(jobGroup)
            .invokeTarget("com.xie.glm.task.TestTask.run")
            .cronExpression("0 0 2 * * ?")
            .misfirePolicy("1")
            .concurrent("0")
            .status("0")
            .statusText("正常")
            .misfirePolicyText("立即执行")
            .concurrentText("禁止")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
    }
}
