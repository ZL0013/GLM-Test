package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.JobFacade;
import com.xie.glm.admin.vo.JobVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.dto.JobDTO;
import com.xie.glm.system.dto.query.JobQueryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 定时任务控制器测试
 *
 * <p>采用 TDD 方式开发，使用 Mockito 进行单元测试。
 *
 * <p>测试原则：
 * <ul>
 *   <li>Red-Green-Refactor 循环</li>
 *   <li>使用 @ParameterizedTest 进行参数化测试</li>
 *   <li>使用嵌套测试类 (@Nested) 组织相关测试</li>
 * </ul>
 *
 * <p>注意：Controller 直接返回数据对象，ResponseAdvice 会自动包装为 Result 格式。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("定时任务控制器测试")
class JobControllerTest {

    @Mock
    private JobFacade jobFacade;

    @InjectMocks
    private JobController jobController;

    // ==================== 任务查询接口测试 ====================

    @Nested
    @DisplayName("任务查询接口测试")
    class QueryTests {

        @Test
        @DisplayName("分页查询任务列表 - 成功")
        void listJobs_Success() {
            // Given
            JobQueryDTO query = new JobQueryDTO();
            query.setJobName("清理");

            PageResult<JobVO> expectedPage = new PageResult<>(List.of(createMockJobVO()), 10L);
            when(jobFacade.listJobs(query)).thenReturn(expectedPage);

            // When
            PageResult<JobVO> result = jobController.list(query);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotal()).isEqualTo(10L);
            assertThat(result.getRecords()).hasSize(1);

            verify(jobFacade).listJobs(query);
        }

        @Test
        @DisplayName("分页查询任务列表 - 空结果")
        void listJobs_EmptyResult() {
            // Given
            JobQueryDTO query = new JobQueryDTO();
            PageResult<JobVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(jobFacade.listJobs(query)).thenReturn(expectedPage);

            // When
            PageResult<JobVO> result = jobController.list(query);

            // Then
            assertThat(result.getRecords()).isEmpty();
            assertThat(result.getTotal()).isEqualTo(0L);
        }

        @ParameterizedTest
        @MethodSource("provideQueryConditions")
        @DisplayName("分页查询任务列表 - 带条件查询")
        void listJobs_WithConditions(String jobName, String status) {
            // Given
            JobQueryDTO query = new JobQueryDTO();
            query.setJobName(jobName);
            query.setStatus(status);

            PageResult<JobVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(jobFacade.listJobs(query)).thenReturn(expectedPage);

            // When
            PageResult<JobVO> result = jobController.list(query);

            // Then
            assertThat(result).isNotNull();
            verify(jobFacade).listJobs(query);
        }

        private static List<Arguments> provideQueryConditions() {
            return List.of(
                Arguments.of("数据清理", "0"),
                Arguments.of("数据同步", "1"),
                Arguments.of("", "0")
            );
        }
    }

    // ==================== 任务详情接口测试 ====================

    @Nested
    @DisplayName("任务详情接口测试")
    class DetailTests {

        @Test
        @DisplayName("查询任务详情 - 成功")
        void getDetail_Success() {
            // Given
            Long jobId = 1L;
            JobVO expectedJob = createMockJobVO();
            when(jobFacade.getJobById(jobId)).thenReturn(expectedJob);

            // When
            JobVO result = jobController.getDetail(jobId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getJobId()).isEqualTo(jobId);

            verify(jobFacade).getJobById(jobId);
        }

        @Test
        @DisplayName("查询任务详情 - 任务不存在")
        void getDetail_JobNotFound() {
            // Given
            Long jobId = 999L;
            when(jobFacade.getJobById(jobId))
                .thenThrow(new ServiceException("任务不存在"));

            // When & Then
            assertThatThrownBy(() -> jobController.getDetail(jobId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("任务不存在");
        }
    }

    // ==================== 任务创建接口测试 ====================

    @Nested
    @DisplayName("任务创建接口测试")
    class CreateTests {

        @Test
        @DisplayName("创建任务 - 成功")
        void create_Success() {
            // Given
            JobDTO dto = createMockJobDTO();
            Long expectedJobId = 1L;
            when(jobFacade.createJob(dto)).thenReturn(expectedJobId);

            // When
            Long result = jobController.create(dto);

            // Then
            assertThat(result).isEqualTo(expectedJobId);

            verify(jobFacade).createJob(dto);
        }

        @Test
        @DisplayName("创建任务 - 任务名称重复")
        void create_JobNameDuplicate() {
            // Given
            JobDTO dto = createMockJobDTO();
            when(jobFacade.createJob(dto))
                .thenThrow(new ServiceException("任务名称已存在"));

            // When & Then
            assertThatThrownBy(() -> jobController.create(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("任务名称已存在");
        }
    }

    // ==================== 任务更新接口测试 ====================

    @Nested
    @DisplayName("任务更新接口测试")
    class UpdateTests {

        @Test
        @DisplayName("更新任务 - 成功")
        void update_Success() {
            // Given
            Long jobId = 1L;
            JobDTO dto = createMockJobDTO();

            // When
            jobController.update(jobId, dto);

            // Then
            assertThat(dto.getJobId()).isEqualTo(jobId);

            verify(jobFacade).updateJob(dto);
        }

        @Test
        @DisplayName("更新任务 - 任务不存在")
        void update_JobNotFound() {
            // Given
            Long jobId = 999L;
            JobDTO dto = createMockJobDTO();
            doThrow(new ServiceException("任务不存在"))
                .when(jobFacade).updateJob(any(JobDTO.class));

            // When & Then
            assertThatThrownBy(() -> jobController.update(jobId, dto))
                .isInstanceOf(ServiceException.class);
        }
    }

    // ==================== 任务删除接口测试 ====================

    @Nested
    @DisplayName("任务删除接口测试")
    class DeleteTests {

        @Test
        @DisplayName("删除任务 - 成功")
        void delete_Success() {
            // Given
            Long jobId = 1L;

            // When
            jobController.delete(jobId);

            // Then
            verify(jobFacade).deleteJob(jobId);
        }

        @Test
        @DisplayName("删除任务 - 任务不存在")
        void delete_JobNotFound() {
            // Given
            Long jobId = 999L;
            doThrow(new ServiceException("任务不存在"))
                .when(jobFacade).deleteJob(jobId);

            // When & Then
            assertThatThrownBy(() -> jobController.delete(jobId))
                .isInstanceOf(ServiceException.class);
        }

        @Test
        @DisplayName("批量删除任务 - 成功")
        void deleteBatch_Success() {
            // Given
            Long[] jobIds = {1L, 2L, 3L};

            // When
            jobController.deleteBatch(jobIds);

            // Then
            verify(jobFacade).deleteJobs(jobIds);
        }
    }

    // ==================== 状态修改接口测试 ====================

    @Nested
    @DisplayName("状态修改接口测试")
    class StatusTests {

        @ParameterizedTest
        @MethodSource("provideStatusValues")
        @DisplayName("修改任务状态 - 成功")
        void updateStatus_Success(String status) {
            // Given
            Long jobId = 1L;

            // When
            jobController.updateStatus(jobId, status);

            // Then
            verify(jobFacade).updateStatus(jobId, status);
        }

        @Test
        @DisplayName("修改任务状态 - 任务不存在")
        void updateStatus_JobNotFound() {
            // Given
            Long jobId = 999L;
            String status = "1";
            doThrow(new ServiceException("任务不存在"))
                .when(jobFacade).updateStatus(jobId, status);

            // When & Then
            assertThatThrownBy(() -> jobController.updateStatus(jobId, status))
                .isInstanceOf(ServiceException.class);
        }

        private static List<Arguments> provideStatusValues() {
            return List.of(
                Arguments.of("0"),  // 正常
                Arguments.of("1")   // 暂停
            );
        }
    }

    // ==================== 立即执行接口测试 ====================

    @Nested
    @DisplayName("立即执行接口测试")
    class ExecuteTests {

        @Test
        @DisplayName("立即执行任务 - 成功")
        void execute_Success() {
            // Given
            Long jobId = 1L;

            // When
            jobController.execute(jobId);

            // Then
            verify(jobFacade).executeJob(jobId);
        }

        @Test
        @DisplayName("立即执行任务 - 任务不存在")
        void execute_JobNotFound() {
            // Given
            Long jobId = 999L;
            doThrow(new ServiceException("任务不存在"))
                .when(jobFacade).executeJob(jobId);

            // When & Then
            assertThatThrownBy(() -> jobController.execute(jobId))
                .isInstanceOf(ServiceException.class);
        }

        @Test
        @DisplayName("立即执行任务 - 任务已暂停")
        void execute_JobPaused() {
            // Given
            Long jobId = 1L;
            doThrow(new ServiceException("任务已暂停，无法执行"))
                .when(jobFacade).executeJob(jobId);

            // When & Then
            assertThatThrownBy(() -> jobController.execute(jobId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("任务已暂停");
        }
    }

    // ==================== Mock 辅助方法 ====================

    private JobVO createMockJobVO() {
        return JobVO.builder()
            .jobId(1L)
            .jobName("数据清理任务")
            .jobGroup("DEFAULT")
            .invokeTarget("com.xie.glm.task.DataCleanup.run")
            .cronExpression("0 0 2 * * ?")
            .misfirePolicy("1")
            .misfirePolicyText("立即执行")
            .concurrent("0")
            .concurrentText("禁止")
            .status("0")
            .statusText("正常")
            .build();
    }

    private JobDTO createMockJobDTO() {
        JobDTO dto = new JobDTO();
        dto.setJobName("新任务");
        dto.setJobGroup("DEFAULT");
        dto.setInvokeTarget("com.xie.glm.task.NewTask.run");
        dto.setCronExpression("0 0 1 * * ?");
        dto.setMisfirePolicy("1");
        dto.setConcurrent("0");
        dto.setStatus("1"); // 默认暂停
        return dto;
    }
}
