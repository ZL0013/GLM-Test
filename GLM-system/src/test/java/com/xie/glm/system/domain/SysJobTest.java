package com.xie.glm.system.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SysJob 实体类测试
 *
 * @author xie
 */
@DisplayName("SysJob 实体测试")
class SysJobTest {

    @DisplayName("测试实体基本属性")
    @ParameterizedTest(name = "任务名称: {0}, 任务组: {1}, Cron表达式: {2}")
    @CsvSource({
            "数据清理任务, DEFAULT, '0 0 2 * * ?'",
            "数据同步任务, SYSTEM, '0 0/30 * * * ?'",
            "报表生成任务, REPORT, '0 0 1 * * ?'"
    })
    void testBasicProperties(String jobName, String jobGroup, String cronExpression) {
        SysJob job = new SysJob();
        job.setJobName(jobName);
        job.setJobGroup(jobGroup);
        job.setCronExpression(cronExpression);

        assertThat(job.getJobName()).isEqualTo(jobName);
        assertThat(job.getJobGroup()).isEqualTo(jobGroup);
        assertThat(job.getCronExpression()).isEqualTo(cronExpression);
    }

    @DisplayName("测试任务状态")
    @ParameterizedTest(name = "状态: {0}, 预期是否暂停: {1}")
    @CsvSource({
            "0, false",
            "1, true"
    })
    void testStatus(String status, boolean expectedPaused) {
        SysJob job = new SysJob();
        job.setStatus(status);

        assertThat(job.getStatus()).isEqualTo(status);
        boolean isPaused = "1".equals(status);
        assertThat(isPaused).isEqualTo(expectedPaused);
    }

    @DisplayName("测试并发执行标志")
    @ParameterizedTest(name = "并发执行: {0}, 预期: {1}")
    @CsvSource({
            "0, false",
            "1, true"
    })
    void testConcurrent(String concurrent, boolean expectedAllowConcurrent) {
        SysJob job = new SysJob();
        job.setConcurrent(concurrent);

        assertThat(job.getConcurrent()).isEqualTo(concurrent);
        boolean allowConcurrent = "1".equals(concurrent);
        assertThat(allowConcurrent).isEqualTo(expectedAllowConcurrent);
    }

    @DisplayName("测试任务组类型")
    @ParameterizedTest(name = "任务组: {0}")
    @ValueSource(strings = {"DEFAULT", "SYSTEM", "REPORT", "SYNC"})
    void testJobGroup(String jobGroup) {
        SysJob job = new SysJob();
        job.setJobGroup(jobGroup);

        assertThat(job.getJobGroup()).isEqualTo(jobGroup);
    }

    @DisplayName("测试 Cron 表达式格式")
    @ParameterizedTest(name = "Cron表达式: {0}, 是否有效: {1}")
    @MethodSource("provideCronExpressions")
    void testCronExpression(String cronExpression, boolean expectedValid) {
        SysJob job = new SysJob();
        job.setCronExpression(cronExpression);

        assertThat(job.getCronExpression()).isEqualTo(cronExpression);

        // 简单验证：有效的Cron表达式应该包含6或7个部分
        if (expectedValid) {
            String[] parts = cronExpression.split(" ");
            assertThat(parts.length).isGreaterThanOrEqualTo(6);
        }
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideCronExpressions() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("0 0 2 * * ?", true),
                org.junit.jupiter.params.provider.Arguments.of("0 0/30 * * * ?", true),
                org.junit.jupiter.params.provider.Arguments.of("0 0 1 * * MON-FRI", true),
                org.junit.jupiter.params.provider.Arguments.of("invalid", false),
                org.junit.jupiter.params.provider.Arguments.of("", false)
        );
    }

    @DisplayName("测试调用目标字符串")
    @ParameterizedTest(name = "调用目标: {0}")
    @ValueSource(strings = {
            "com.xie.glm.system.task.DataCleanupTask.execute",
            "com.xie.glm.system.task.DataSyncTask.run",
            "com.xie.glm.system.task.ReportTask.generate"
    })
    void testInvokeTarget(String invokeTarget) {
        SysJob job = new SysJob();
        job.setInvokeTarget(invokeTarget);

        assertThat(job.getInvokeTarget()).isEqualTo(invokeTarget);
    }

    @DisplayName("测试任务执行策略")
    @ParameterizedTest(name = "执行策略: {0}")
    @ValueSource(strings = {"1", "2", "3"})
    void testMisfirePolicy(String misfirePolicy) {
        SysJob job = new SysJob();
        job.setMisfirePolicy(misfirePolicy);

        assertThat(job.getMisfirePolicy()).isEqualTo(misfirePolicy);
    }

    @DisplayName("测试是否允许并发")
    @Test
    void testAllowConcurrent() {
        SysJob job = new SysJob();
        job.setConcurrent("1");

        boolean allowConcurrent = "1".equals(job.getConcurrent());
        assertThat(allowConcurrent).isTrue();

        job.setConcurrent("0");
        allowConcurrent = "1".equals(job.getConcurrent());
        assertThat(allowConcurrent).isFalse();
    }

    @DisplayName("测试是否暂停")
    @Test
    void testIsPaused() {
        SysJob job = new SysJob();
        job.setStatus("1");

        boolean isPaused = "1".equals(job.getStatus());
        assertThat(isPaused).isTrue();

        job.setStatus("0");
        isPaused = "1".equals(job.getStatus());
        assertThat(isPaused).isFalse();
    }

    @DisplayName("测试 Lombok @Data 注解生成的方法")
    @Test
    void testDataAnnotation() {
        SysJob job = new SysJob();
        job.setJobId(1L);
        job.setJobName("测试任务");
        job.setJobGroup("DEFAULT");
        job.setCronExpression("0 0 2 * * ?");
        job.setInvokeTarget("com.xie.glm.system.task.TestTask.run");
        job.setInvokeParam("testParam");
        job.setMisfirePolicy("1");
        job.setConcurrent("0");
        job.setStatus("0");
        job.setRemark("测试任务备注");

        // 测试 toString
        String toString = job.toString();
        assertThat(toString).contains("测试任务", "DEFAULT");

        // 测试 equals 和 hashCode
        SysJob job2 = new SysJob();
        job2.setJobId(1L);
        job2.setJobName("测试任务");
        job2.setJobGroup("DEFAULT");
        job2.setCronExpression("0 0 2 * * ?");
        job2.setInvokeTarget("com.xie.glm.system.task.TestTask.run");
        job2.setInvokeParam("testParam");
        job2.setMisfirePolicy("1");
        job2.setConcurrent("0");
        job2.setStatus("0");
        job2.setRemark("测试任务备注");

        assertThat(job).isEqualTo(job2);
        assertThat(job.hashCode()).isEqualTo(job2.hashCode());
    }

    @DisplayName("测试任务异常信息")
    @ParameterizedTest(name = "异常信息: {0}")
    @ValueSource(strings = {
            "NullPointerException",
            "Connection timeout",
            ""
    })
    void testExceptionInfo(String exceptionInfo) {
        SysJob job = new SysJob();
        job.setExceptionInfo(exceptionInfo);

        assertThat(job.getExceptionInfo()).isEqualTo(exceptionInfo);
    }

    @DisplayName("测试执行开始和结束时间")
    @Test
    void testExecutionTimes() {
        SysJob job = new SysJob();

        // 测试初始值为 null
        assertThat(job.getBeginTime()).isNull();
        assertThat(job.getNextValidTime()).isNull();
    }

    @DisplayName("测试创建任务时的必填字段")
    @Test
    void testRequiredFields() {
        SysJob job = new SysJob();

        // 任务名称、任务组、Cron表达式、调用目标是必填的
        job.setJobName("测试任务");
        job.setJobGroup("DEFAULT");
        job.setCronExpression("0 0 2 * * ?");
        job.setInvokeTarget("com.xie.glm.system.task.TestTask.run");

        assertThat(job.getJobName()).isNotEmpty();
        assertThat(job.getJobGroup()).isNotEmpty();
        assertThat(job.getCronExpression()).isNotEmpty();
        assertThat(job.getInvokeTarget()).isNotEmpty();
    }
}
