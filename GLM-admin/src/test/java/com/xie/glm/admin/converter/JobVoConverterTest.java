package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.JobVO;
import com.xie.glm.system.dto.JobDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JobVoConverter 定时任务 VO 转换器单元测试
 *
 * <p>测试 JobDTO 到 JobVO 的转换
 *
 * @author xie
 */
@DisplayName("JobVoConverter 定时任务 VO 转换器单元测试")
class JobVoConverterTest {

    /**
     * 获取 JobVoConverter 实例
     */
    private JobVoConverter getJobVoConverter() {
        return new JobVoConverterImpl();
    }

    @Test
    @DisplayName("测试 DTO 转 VO")
    void testToVo() {
        // Given: 准备 JobDTO 数据
        JobDTO jobDTO = JobDTO.builder()
                .jobId(1L)
                .jobName("数据清理任务")
                .jobGroup("DEFAULT")
                .invokeTarget("com.xie.glm.task.DataCleanup.run")
                .cronExpression("0 0 2 * * ?")
                .misfirePolicy("1")
                .concurrent("0")
                .status("0")
                .createTime(LocalDateTime.now())
                .build();

        // When: 执行转换
        JobVO jobVO = getJobVoConverter().toVo(jobDTO);

        // Then: 验证基本字段转换正确
        assertThat(jobVO.getJobId()).isEqualTo(1L);
        assertThat(jobVO.getJobName()).isEqualTo("数据清理任务");
        assertThat(jobVO.getJobGroup()).isEqualTo("DEFAULT");
        assertThat(jobVO.getInvokeTarget()).isEqualTo("com.xie.glm.task.DataCleanup.run");
        assertThat(jobVO.getCronExpression()).isEqualTo("0 0 2 * * ?");
        assertThat(jobVO.getMisfirePolicy()).isEqualTo("1");
        assertThat(jobVO.getConcurrent()).isEqualTo("0");
        assertThat(jobVO.getStatus()).isEqualTo("0");
    }

    @Test
    @DisplayName("测试 DTO 列表转 VO 列表")
    void testToVoList() {
        // Given: 准备 JobDTO 列表
        List<JobDTO> jobDTOs = List.of(
                JobDTO.builder()
                        .jobId(1L)
                        .jobName("数据清理任务")
                        .jobGroup("DEFAULT")
                        .build(),
                JobDTO.builder()
                        .jobId(2L)
                        .jobName("数据同步任务")
                        .jobGroup("SYSTEM")
                        .build()
        );

        // When: 执行转换
        List<JobVO> jobVOs = getJobVoConverter().toVoList(jobDTOs);

        // Then: 验证转换结果
        assertThat(jobVOs).hasSize(2);
        assertThat(jobVOs.get(0).getJobId()).isEqualTo(1L);
        assertThat(jobVOs.get(0).getJobName()).isEqualTo("数据清理任务");
        assertThat(jobVOs.get(1).getJobId()).isEqualTo(2L);
        assertThat(jobVOs.get(1).getJobName()).isEqualTo("数据同步任务");
    }

    @Test
    @DisplayName("测试 null 转换")
    void testNullConversion() {
        // Given: null DTO
        JobDTO jobDTO = null;

        // When: 执行转换
        JobVO jobVO = getJobVoConverter().toVo(jobDTO);

        // Then: 验证返回 null
        assertThat(jobVO).isNull();
    }

    @Test
    @DisplayName("测试 null 列表转换")
    void testNullListConversion() {
        // Given: null DTO 列表
        List<JobDTO> jobDTOs = null;

        // When: 执行转换
        List<JobVO> jobVOs = getJobVoConverter().toVoList(jobDTOs);

        // Then: 验证返回 null
        assertThat(jobVOs).isNull();
    }

    @Test
    @DisplayName("测试空列表转换")
    void testEmptyListConversion() {
        // Given: 空 DTO 列表
        List<JobDTO> jobDTOs = List.of();

        // When: 执行转换
        List<JobVO> jobVOs = getJobVoConverter().toVoList(jobDTOs);

        // Then: 验证返回空列表
        assertThat(jobVOs).isEmpty();
    }

    @Test
    @DisplayName("测试状态码转换需要手动设置 statusText")
    void testStatusTextMapping() {
        // Given: 状态为 "0" 的 DTO
        JobDTO jobDTO = JobDTO.builder()
                .jobId(1L)
                .jobName("数据清理任务")
                .status("0")
                .build();

        // When: 执行转换（注意：MapStruct 不会自动设置 statusText）
        JobVO jobVO = getJobVoConverter().toVo(jobDTO);

        // Then: 验证 status 正确但 statusText 需要手动设置
        assertThat(jobVO.getStatus()).isEqualTo("0");
        assertThat(jobVO.getStatusText()).isNull();  // 需要在 Facade 层手动设置
    }

    @Test
    @DisplayName("测试执行策略转换需要手动设置 misfirePolicyText")
    void testMisfirePolicyTextMapping() {
        // Given: 执行策略为 "1" 的 DTO
        JobDTO jobDTO = JobDTO.builder()
                .jobId(1L)
                .jobName("数据清理任务")
                .misfirePolicy("1")
                .build();

        // When: 执行转换
        JobVO jobVO = getJobVoConverter().toVo(jobDTO);

        // Then: 验证 misfirePolicy 正确但 misfirePolicyText 需要手动设置
        assertThat(jobVO.getMisfirePolicy()).isEqualTo("1");
        assertThat(jobVO.getMisfirePolicyText()).isNull();
    }

    @Test
    @DisplayName("测试并发标识转换需要手动设置 concurrentText")
    void testConcurrentTextMapping() {
        // Given: 并发标识为 "0" 的 DTO
        JobDTO jobDTO = JobDTO.builder()
                .jobId(1L)
                .jobName("数据清理任务")
                .concurrent("0")
                .build();

        // When: 执行转换
        JobVO jobVO = getJobVoConverter().toVo(jobDTO);

        // Then: 验证 concurrent 正确但 concurrentText 需要手动设置
        assertThat(jobVO.getConcurrent()).isEqualTo("0");
        assertThat(jobVO.getConcurrentText()).isNull();
    }
}
