package com.xie.glm.admin.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JobVO 定时任务视图对象单元测试
 *
 * <p>测试定时任务视图对象的创建和字段设置
 *
 * @author xie
 */
@DisplayName("JobVO 定时任务视图对象单元测试")
class JobVOTest {

    @Test
    @DisplayName("测试 JobVO 创建和字段设置")
    void testJobVOCreation() {
        // When: 创建 JobVO 对象
        JobVO jobVO = JobVO.builder()
                .jobId(1L)
                .jobName("数据清理任务")
                .jobGroup("DEFAULT")
                .status("0")
                .statusText("正常")
                .misfirePolicyText("立即执行")
                .concurrentText("禁止")
                .build();

        // Then: 验证所有字段设置正确
        assertThat(jobVO.getJobId()).isEqualTo(1L);
        assertThat(jobVO.getJobName()).isEqualTo("数据清理任务");
        assertThat(jobVO.getJobGroup()).isEqualTo("DEFAULT");
        assertThat(jobVO.getStatus()).isEqualTo("0");
        assertThat(jobVO.getStatusText()).isEqualTo("正常");
        assertThat(jobVO.getMisfirePolicyText()).isEqualTo("立即执行");
        assertThat(jobVO.getConcurrentText()).isEqualTo("禁止");
    }

    @ParameterizedTest
    @CsvSource({
            "0, 正常",
            "1, 暂停"
    })
    @DisplayName("测试状态文本映射")
    void testStatusTextMapping(String status, String expectedText) {
        JobVO jobVO = JobVO.builder()
                .jobId(1L)
                .status(status)
                .statusText(expectedText)
                .build();

        assertThat(jobVO.getStatus()).isEqualTo(status);
        assertThat(jobVO.getStatusText()).isEqualTo(expectedText);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 立即执行",
            "2, 执行一次",
            "3, 放弃执行"
    })
    @DisplayName("测试执行策略文本映射")
    void testMisfirePolicyTextMapping(String misfirePolicy, String expectedText) {
        JobVO jobVO = JobVO.builder()
                .jobId(1L)
                .misfirePolicy(misfirePolicy)
                .misfirePolicyText(expectedText)
                .build();

        assertThat(jobVO.getMisfirePolicy()).isEqualTo(misfirePolicy);
        assertThat(jobVO.getMisfirePolicyText()).isEqualTo(expectedText);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 禁止",
            "1, 允许"
    })
    @DisplayName("测试并发标识文本映射")
    void testConcurrentTextMapping(String concurrent, String expectedText) {
        JobVO jobVO = JobVO.builder()
                .jobId(1L)
                .concurrent(concurrent)
                .concurrentText(expectedText)
                .build();

        assertThat(jobVO.getConcurrent()).isEqualTo(concurrent);
        assertThat(jobVO.getConcurrentText()).isEqualTo(expectedText);
    }

    @Test
    @DisplayName("测试完整的 JobVO")
    void testCompleteJobVO() {
        LocalDateTime now = LocalDateTime.now();

        JobVO jobVO = JobVO.builder()
                .jobId(1L)
                .jobName("数据清理任务")
                .jobGroup("DEFAULT")
                .invokeTarget("com.xie.glm.task.DataCleanup.run")
                .invokeParam("days=30")
                .cronExpression("0 0 2 * * ?")
                .misfirePolicy("1")
                .misfirePolicyText("立即执行")
                .concurrent("0")
                .concurrentText("禁止")
                .status("0")
                .statusText("正常")
                .beginTime(now)
                .nextValidTime(now.plusDays(1))
                .remark("每天凌晨2点执行数据清理")
                .createTime(now)
                .updateTime(now)
                .build();

        assertThat(jobVO.getJobId()).isEqualTo(1L);
        assertThat(jobVO.getJobName()).isEqualTo("数据清理任务");
        assertThat(jobVO.getStatusText()).isEqualTo("正常");
        assertThat(jobVO.getMisfirePolicyText()).isEqualTo("立即执行");
        assertThat(jobVO.getConcurrentText()).isEqualTo("禁止");
    }

    @Test
    @DisplayName("测试 JobVO 带 null 值")
    void testJobVOWithNullValues() {
        JobVO jobVO = JobVO.builder()
                .jobId(1L)
                .jobName("测试任务")
                .build();

        assertThat(jobVO.getJobId()).isEqualTo(1L);
        assertThat(jobVO.getJobName()).isEqualTo("测试任务");
        assertThat(jobVO.getStatusText()).isNull();
        assertThat(jobVO.getMisfirePolicyText()).isNull();
        assertThat(jobVO.getConcurrentText()).isNull();
    }

    @Test
    @DisplayName("测试继承的字段")
    void testInheritedFields() {
        LocalDateTime now = LocalDateTime.now();

        JobVO jobVO = JobVO.builder()
                .jobId(1L)
                .jobName("测试任务")
                .invokeTarget("com.xie.glm.task.TestTask.run")
                .cronExpression("0 0 2 * * ?")
                .createTime(now)
                .updateTime(now)
                .build();

        // 验证从 JobDTO 继承的字段
        assertThat(jobVO.getJobId()).isEqualTo(1L);
        assertThat(jobVO.getJobName()).isEqualTo("测试任务");
        assertThat(jobVO.getInvokeTarget()).isEqualTo("com.xie.glm.task.TestTask.run");
        assertThat(jobVO.getCronExpression()).isEqualTo("0 0 2 * * ?");
        assertThat(jobVO.getCreateTime()).isEqualTo(now);
        assertThat(jobVO.getUpdateTime()).isEqualTo(now);
    }
}
