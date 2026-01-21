package com.xie.glm.system.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SysJobLog 实体类测试
 *
 * @author xie
 */
@DisplayName("SysJobLog 实体测试")
class SysJobLogTest {

    @DisplayName("测试实体基本属性")
    @ParameterizedTest(name = "任务名称: {0}, 任务组: {1}, 调用目标: {2}")
    @CsvSource({
            "数据清理任务, DEFAULT, 'com.xie.glm.system.task.DataCleanupTask.execute'",
            "数据同步任务, SYSTEM, 'com.xie.glm.system.task.DataSyncTask.run'",
            "报表生成任务, REPORT, 'com.xie.glm.system.task.ReportTask.generate'"
    })
    void testBasicProperties(String jobName, String jobGroup, String invokeTarget) {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setJobName(jobName);
        jobLog.setJobGroup(jobGroup);
        jobLog.setInvokeTarget(invokeTarget);

        assertThat(jobLog.getJobName()).isEqualTo(jobName);
        assertThat(jobLog.getJobGroup()).isEqualTo(jobGroup);
        assertThat(jobLog.getInvokeTarget()).isEqualTo(invokeTarget);
    }

    @DisplayName("测试任务执行状态")
    @ParameterizedTest(name = "状态: {0}, 是否成功: {1}")
    @CsvSource({
            "0, true",
            "1, false"
    })
    void testStatus(String status, boolean expectedSuccess) {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setStatus(status);

        assertThat(jobLog.getStatus()).isEqualTo(status);
        boolean isSuccess = "0".equals(status);
        assertThat(isSuccess).isEqualTo(expectedSuccess);
    }

    @DisplayName("测试执行时间信息")
    @Test
    void testExecutionTime() {
        SysJobLog jobLog = new SysJobLog();

        LocalDateTime startTime = LocalDateTime.now().minusMinutes(5);
        LocalDateTime endTime = LocalDateTime.now();

        jobLog.setStartTime(startTime);
        jobLog.setEndTime(endTime);

        assertThat(jobLog.getStartTime()).isEqualTo(startTime);
        assertThat(jobLog.getEndTime()).isEqualTo(endTime);
    }

    @DisplayName("测试执行时长（毫秒）")
    @ParameterizedTest(name = "执行时长: {0} ms")
    @ValueSource(longs = {100L, 1000L, 5000L, 60000L})
    void testExecutionTime(long duration) {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setExecutionTime(duration);

        assertThat(jobLog.getExecutionTime()).isEqualTo(duration);
    }

    @DisplayName("测试任务日志信息")
    @ParameterizedTest(name = "日志信息: {0}")
    @ValueSource(strings = {
            "任务执行成功，处理了100条数据",
            "任务执行失败：NullPointerException",
            "数据同步完成，同步数量：500"
    })
    void testJobLogMessage(String jobLogMessage) {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setJobLogMessage(jobLogMessage);

        assertThat(jobLog.getJobLogMessage()).isEqualTo(jobLogMessage);
    }

    @DisplayName("测试异常信息")
    @ParameterizedTest(name = "异常信息: {0}")
    @ValueSource(strings = {
            "java.lang.NullPointerException: Cannot invoke method",
            "java.sql.SQLException: Connection timeout",
            "org.springframework.dao.DataAccessException: Database error"
    })
    void testExceptionInfo(String exceptionInfo) {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setExceptionInfo(exceptionInfo);

        assertThat(jobLog.getExceptionInfo()).isEqualTo(exceptionInfo);
    }

    @DisplayName("测试是否执行成功")
    @Test
    void testIsSuccess() {
        SysJobLog jobLog = new SysJobLog();

        // 状态为 0 表示成功
        jobLog.setStatus("0");
        boolean isSuccess = "0".equals(jobLog.getStatus());
        assertThat(isSuccess).isTrue();

        // 状态为 1 表示失败
        jobLog.setStatus("1");
        isSuccess = "0".equals(jobLog.getStatus());
        assertThat(isSuccess).isFalse();
    }

    @DisplayName("测试是否执行失败")
    @Test
    void testIsFailed() {
        SysJobLog jobLog = new SysJobLog();

        // 状态为 1 表示失败
        jobLog.setStatus("1");
        boolean isFailed = "1".equals(jobLog.getStatus());
        assertThat(isFailed).isTrue();

        // 状态为 0 表示成功
        jobLog.setStatus("0");
        isFailed = "1".equals(jobLog.getStatus());
        assertThat(isFailed).isFalse();
    }

    @DisplayName("测试停止执行标志")
    @ParameterizedTest(name = "停止标志: {0}, 是否停止: {1}")
    @CsvSource({
            "0, false",
            "1, true"
    })
    void testIsStop(String stopFlag, boolean expectedStopped) {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setStopFlag(stopFlag);

        assertThat(jobLog.getStopFlag()).isEqualTo(stopFlag);
        boolean isStopped = "1".equals(stopFlag);
        assertThat(isStopped).isEqualTo(expectedStopped);
    }

    @DisplayName("测试任务组类型")
    @ParameterizedTest(name = "任务组: {0}")
    @ValueSource(strings = {"DEFAULT", "SYSTEM", "REPORT", "SYNC"})
    void testJobGroup(String jobGroup) {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setJobGroup(jobGroup);

        assertThat(jobLog.getJobGroup()).isEqualTo(jobGroup);
    }

    @DisplayName("测试调用目标参数")
    @ParameterizedTest(name = "参数: {0}")
    @ValueSource(strings = {
            "userId=123",
            "startDate=2024-01-01,endDate=2024-12-31",
            ""
    })
    void testInvokeParam(String invokeParam) {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setInvokeParam(invokeParam);

        assertThat(jobLog.getInvokeParam()).isEqualTo(invokeParam);
    }

    @DisplayName("测试执行时长计算")
    @Test
    void testDurationCalculation() {
        SysJobLog jobLog = new SysJobLog();

        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 1, 10, 0, 5);

        jobLog.setStartTime(startTime);
        jobLog.setEndTime(endTime);

        // 手动设置执行时长（单位：毫秒）
        jobLog.setExecutionTime(5000L);

        assertThat(jobLog.getExecutionTime()).isEqualTo(5000L);
    }

    @DisplayName("测试 Lombok @Data 注解生成的方法")
    @Test
    void testDataAnnotation() {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setJobLogId(1L);
        jobLog.setJobName("测试任务");
        jobLog.setJobGroup("DEFAULT");
        jobLog.setInvokeTarget("com.xie.glm.system.task.TestTask.run");
        jobLog.setInvokeParam("testParam");
        jobLog.setStartTime(LocalDateTime.now());
        jobLog.setEndTime(LocalDateTime.now().plusSeconds(5));
        jobLog.setExecutionTime(5000L);
        jobLog.setStatus("0");
        jobLog.setJobLogMessage("任务执行成功");
        jobLog.setExceptionInfo("");
        jobLog.setStopFlag("0");

        // 测试 toString
        String toString = jobLog.toString();
        assertThat(toString).contains("测试任务", "DEFAULT");

        // 测试 equals 和 hashCode
        SysJobLog jobLog2 = new SysJobLog();
        jobLog2.setJobLogId(1L);
        jobLog2.setJobName("测试任务");
        jobLog2.setJobGroup("DEFAULT");
        jobLog2.setInvokeTarget("com.xie.glm.system.task.TestTask.run");
        jobLog2.setInvokeParam("testParam");
        jobLog2.setStartTime(jobLog.getStartTime());
        jobLog2.setEndTime(jobLog.getEndTime());
        jobLog2.setExecutionTime(5000L);
        jobLog2.setStatus("0");
        jobLog2.setJobLogMessage("任务执行成功");
        jobLog2.setExceptionInfo("");
        jobLog2.setStopFlag("0");

        assertThat(jobLog).isEqualTo(jobLog2);
        assertThat(jobLog.hashCode()).isEqualTo(jobLog2.hashCode());
    }

    @DisplayName("测试失败场景的完整信息")
    @Test
    void testFailedJobLog() {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setJobName("失败任务");
        jobLog.setJobGroup("DEFAULT");
        jobLog.setInvokeTarget("com.xie.glm.system.task.FailedTask.run");
        jobLog.setStartTime(LocalDateTime.now());
        jobLog.setEndTime(LocalDateTime.now().plusSeconds(2));
        jobLog.setExecutionTime(2000L);
        jobLog.setStatus("1"); // 失败
        jobLog.setJobLogMessage("任务执行失败");
        jobLog.setExceptionInfo("java.lang.NullPointerException: Cannot invoke method on null object");
        jobLog.setStopFlag("0");

        assertThat(jobLog.getStatus()).isEqualTo("1");
        assertThat(jobLog.getExceptionInfo()).isNotEmpty();
        assertThat(jobLog.getJobLogMessage()).contains("失败");
    }

    @DisplayName("测试成功场景的完整信息")
    @Test
    void testSuccessJobLog() {
        SysJobLog jobLog = new SysJobLog();
        jobLog.setJobName("成功任务");
        jobLog.setJobGroup("SYSTEM");
        jobLog.setInvokeTarget("com.xie.glm.system.task.SuccessTask.run");
        jobLog.setStartTime(LocalDateTime.now());
        jobLog.setEndTime(LocalDateTime.now().plusSeconds(10));
        jobLog.setExecutionTime(10000L);
        jobLog.setStatus("0"); // 成功
        jobLog.setJobLogMessage("任务执行成功，处理了1000条数据");
        jobLog.setExceptionInfo("");
        jobLog.setStopFlag("0");

        assertThat(jobLog.getStatus()).isEqualTo("0");
        assertThat(jobLog.getExceptionInfo()).isEmpty();
        assertThat(jobLog.getJobLogMessage()).contains("成功");
    }

    @DisplayName("测试任务日志的时序性")
    @Test
    void testLogChronology() {
        SysJobLog jobLog = new SysJobLog();

        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 1, 10, 0, 5);

        jobLog.setStartTime(startTime);
        jobLog.setEndTime(endTime);

        // 开始时间应该早于结束时间
        assertThat(jobLog.getStartTime()).isBefore(jobLog.getEndTime());
    }

    @DisplayName("测试创建任务日志时的必填字段")
    @Test
    void testRequiredFields() {
        SysJobLog jobLog = new SysJobLog();

        // 任务名称、任务组、调用目标是必填的
        jobLog.setJobName("测试任务");
        jobLog.setJobGroup("DEFAULT");
        jobLog.setInvokeTarget("com.xie.glm.system.task.TestTask.run");
        jobLog.setStartTime(LocalDateTime.now());
        jobLog.setEndTime(LocalDateTime.now().plusSeconds(1));
        jobLog.setExecutionTime(1000L);
        jobLog.setStatus("0");

        assertThat(jobLog.getJobName()).isNotEmpty();
        assertThat(jobLog.getJobGroup()).isNotEmpty();
        assertThat(jobLog.getInvokeTarget()).isNotEmpty();
        assertThat(jobLog.getStartTime()).isNotNull();
        assertThat(jobLog.getEndTime()).isNotNull();
        assertThat(jobLog.getExecutionTime()).isNotNull();
        assertThat(jobLog.getStatus()).isNotNull();
    }
}
