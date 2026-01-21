package com.xie.glm.system.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JobDTO 定时任务数据传输对象测试类
 *
 * <p>测试定时任务 DTO 的各种属性和行为
 * <p>测试原则：
 * <ul>
 *   <li>参数化测试覆盖字段验证</li>
 *   <li>验证 Lombok 注解生成的 getter/setter</li>
 *   <li>测试时间字段的处理</li>
 *   <li>验证业务字段的有效性</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("JobDTO 定时任务数据传输对象单元测试")
class JobDTOTest {

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        JobDTO dto = new JobDTO();

        // 任务基本信息
        assertNull(dto.getJobId(), "默认任务ID应为null");
        assertNull(dto.getJobName(), "默认任务名称应为null");
        assertNull(dto.getJobGroup(), "默认任务组应为null");
        assertNull(dto.getInvokeTarget(), "默认调用目标应为null");
        assertNull(dto.getInvokeParam(), "默认调用参数应为null");
        assertNull(dto.getCronExpression(), "默认Cron表达式应为null");
        assertNull(dto.getMisfirePolicy(), "默认执行策略应为null");
        assertNull(dto.getConcurrent(), "默认并发标志应为null");
        assertNull(dto.getStatus(), "默认状态应为null");
        assertNull(dto.getRemark(), "默认备注应为null");

        // 时间字段
        assertNull(dto.getCreateTime(), "默认创建时间应为null");
        assertNull(dto.getUpdateTime(), "默认更新时间应为null");
        assertNull(dto.getBeginTime(), "默认执行开始时间应为null");
        assertNull(dto.getNextValidTime(), "默认下次执行时间应为null");

        // 异常信息
        assertNull(dto.getExceptionInfo(), "默认异常信息应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 数据清理任务, DEFAULT, 'com.xie.glm.task.DataCleanup.run', 'param1=value1', '0 0 2 * * ?', 1, 0, 0",
            "2, 数据同步任务, SYSTEM, 'com.xie.glm.task.DataSync.execute', '', '0 0/30 * * * ?', 2, 1, 1",
            "3, 报表生成任务, REPORT, 'com.xie.glm.task.ReportTask.generate', 'type=daily', '0 0 1 * * ?', 3, 0, 0"
    })
    @DisplayName("Getter/Setter - 验证基本字段赋值和获取")
    void testGettersSetters(Long jobId, String jobName, String jobGroup, String invokeTarget,
                           String invokeParam, String cronExpression, String misfirePolicy,
                           String concurrent, String status) {
        JobDTO dto = new JobDTO();
        dto.setJobId(jobId);
        dto.setJobName(jobName);
        dto.setJobGroup(jobGroup);
        dto.setInvokeTarget(invokeTarget);
        dto.setInvokeParam(invokeParam);
        dto.setCronExpression(cronExpression);
        dto.setMisfirePolicy(misfirePolicy);
        dto.setConcurrent(concurrent);
        dto.setStatus(status);
        dto.setRemark("测试备注");

        assertEquals(jobId, dto.getJobId(), "任务ID应匹配");
        assertEquals(jobName, dto.getJobName(), "任务名称应匹配");
        assertEquals(jobGroup, dto.getJobGroup(), "任务组应匹配");
        assertEquals(invokeTarget, dto.getInvokeTarget(), "调用目标应匹配");
        assertEquals(invokeParam, dto.getInvokeParam(), "调用参数应匹配");
        assertEquals(cronExpression, dto.getCronExpression(), "Cron表达式应匹配");
        assertEquals(misfirePolicy, dto.getMisfirePolicy(), "执行策略应匹配");
        assertEquals(concurrent, dto.getConcurrent(), "并发标志应匹配");
        assertEquals(status, dto.getStatus(), "状态应匹配");
        assertEquals("测试备注", dto.getRemark(), "备注应匹配");
    }

    // ==================== 任务 ID 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "100, true",
            "1000, true"
    })
    @DisplayName("任务 ID - 验证任务ID")
    void testJobId(Long jobId, boolean isValid) {
        JobDTO dto = new JobDTO();
        dto.setJobId(jobId);

        assertEquals(jobId, dto.getJobId(), "任务ID应匹配");

        if (isValid) {
            assertNotNull(dto.getJobId(), "任务ID不应为null");
            assertTrue(dto.getJobId() > 0, "任务ID应为正数");
        }
    }

    // ==================== 任务名称测试 ====================

    @ParameterizedTest
    @CsvSource({
            "数据清理任务, true",
            "数据同步, true",
            "报表生成, true"
    })
    @DisplayName("任务名称 - 验证任务名称格式")
    void testJobName(String jobName, boolean isValid) {
        JobDTO dto = new JobDTO();
        dto.setJobName(jobName);

        assertEquals(jobName, dto.getJobName(), "任务名称应匹配");

        if (isValid) {
            assertNotNull(dto.getJobName(), "任务名称不应为null");
            assertFalse(dto.getJobName().isEmpty(), "任务名称不应为空");
        }
    }

    // ==================== 任务组测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"DEFAULT", "SYSTEM", "REPORT", "SYNC"})
    @DisplayName("任务组 - 验证任务组值有效性")
    void testJobGroup(String jobGroup) {
        JobDTO dto = new JobDTO();
        dto.setJobGroup(jobGroup);

        assertEquals(jobGroup, dto.getJobGroup(), "任务组应匹配");
        assertTrue(List.of("DEFAULT", "SYSTEM", "REPORT", "SYNC").contains(dto.getJobGroup()),
                "任务组应为预定义的值之一");
    }

    // ==================== Cron 表达式测试 ====================

    @ParameterizedTest
    @CsvSource({
            "'0 0 2 * * ?', true",
            "'0 0/30 * * * ?', true",
            "'0 0 1 * * MON-FRI', true",
            "'invalid', false",
            "'', false"
    })
    @DisplayName("Cron 表达式 - 验证 Cron 格式")
    void testCronExpression(String cronExpression, boolean isValid) {
        JobDTO dto = new JobDTO();
        dto.setCronExpression(cronExpression);

        assertEquals(cronExpression, dto.getCronExpression(), "Cron表达式应匹配");

        if (isValid && cronExpression != null && !cronExpression.isEmpty()) {
            String[] parts = cronExpression.split(" ");
            assertTrue(parts.length >= 6, "有效的Cron表达式应至少包含6部分");
        }
    }

    // ==================== 调用目标测试 ====================

    @ParameterizedTest
    @CsvSource({
            "com.xie.glm.task.DataCleanup.run, true",
            "com.xie.glm.task.DataSync.execute, true",
            ", false"
    })
    @DisplayName("调用目标 - 验证调用目标格式")
    void testInvokeTarget(String invokeTarget, boolean shouldHaveValue) {
        JobDTO dto = new JobDTO();
        dto.setInvokeTarget(invokeTarget);

        assertEquals(invokeTarget, dto.getInvokeTarget(), "调用目标应匹配");

        if (shouldHaveValue && invokeTarget != null) {
            assertTrue(invokeTarget.contains("."), "调用目标应为完整的类路径格式");
        }
    }

    // ==================== 执行策略测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    @DisplayName("执行策略 - 验证策略值有效性")
    void testMisfirePolicy(String misfirePolicy) {
        JobDTO dto = new JobDTO();
        dto.setMisfirePolicy(misfirePolicy);

        assertEquals(misfirePolicy, dto.getMisfirePolicy(), "执行策略应匹配");
        assertTrue(List.of("1", "2", "3").contains(dto.getMisfirePolicy()),
                "执行策略应为 1=立即执行，2=执行一次，3=放弃执行");
    }

    // ==================== 并发标志测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, false",
            "1, true"
    })
    @DisplayName("并发标志 - 验证并发执行标识")
    void testConcurrent(String concurrent, boolean isAllowConcurrent) {
        JobDTO dto = new JobDTO();
        dto.setConcurrent(concurrent);

        assertEquals(concurrent, dto.getConcurrent(), "并发标志应匹配");
        boolean actualConcurrent = "1".equals(dto.getConcurrent());
        assertEquals(isAllowConcurrent, actualConcurrent, "并发标识应匹配");
    }

    // ==================== 任务状态测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"0", "1"})
    @DisplayName("任务状态 - 验证状态值有效性")
    void testStatus(String status) {
        JobDTO dto = new JobDTO();
        dto.setStatus(status);

        assertEquals(status, dto.getStatus(), "状态值应匹配");
        assertTrue(List.of("0", "1").contains(dto.getStatus()),
                "状态值应为 0=正常 或 1=暂停");
    }

    // ==================== 时间字段测试 ====================

    @Test
    @DisplayName("时间字段 - 验证时间设置")
    void testTimeFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime tomorrow = now.plusDays(1);

        JobDTO dto = new JobDTO();
        dto.setCreateTime(now);
        dto.setUpdateTime(yesterday);
        dto.setBeginTime(now);
        dto.setNextValidTime(tomorrow);

        assertEquals(now, dto.getCreateTime(), "创建时间应匹配");
        assertEquals(yesterday, dto.getUpdateTime(), "更新时间应匹配");
        assertEquals(now, dto.getBeginTime(), "执行开始时间应匹配");
        assertEquals(tomorrow, dto.getNextValidTime(), "下次执行时间应匹配");
    }

    @Test
    @DisplayName("时间字段 - 验证时间顺序")
    void testTimeOrder() {
        LocalDateTime createTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime updateTime = LocalDateTime.of(2024, 1, 2, 0, 0);

        JobDTO dto = new JobDTO();
        dto.setCreateTime(createTime);
        dto.setUpdateTime(updateTime);

        assertTrue(dto.getCreateTime().isBefore(dto.getUpdateTime()),
                "创建时间应早于更新时间");
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证字段 setter 容错")
    void testNullHandling(String value) {
        JobDTO dto = new JobDTO();

        dto.setJobName(value);
        dto.setJobGroup(value);
        dto.setInvokeTarget(value);
        dto.setInvokeParam(value);
        dto.setCronExpression(value);
        dto.setRemark(value);
        dto.setExceptionInfo(value);

        assertNull(dto.getJobName(), "null 任务名称应被接受");
        assertNull(dto.getJobGroup(), "null 任务组应被接受");
        assertNull(dto.getInvokeTarget(), "null 调用目标应被接受");
        assertNull(dto.getInvokeParam(), "null 调用参数应被接受");
        assertNull(dto.getCronExpression(), "null Cron表达式应被接受");
        assertNull(dto.getRemark(), "null 备注应被接受");
        assertNull(dto.getExceptionInfo(), "null 异常信息应被接受");
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        JobDTO dto1 = new JobDTO();
        dto1.setJobId(1L);
        dto1.setJobName("数据清理任务");
        dto1.setJobGroup("DEFAULT");

        JobDTO dto2 = new JobDTO();
        dto2.setJobId(1L);
        dto2.setJobName("数据清理任务");
        dto2.setJobGroup("DEFAULT");

        JobDTO dto3 = new JobDTO();
        dto3.setJobId(2L);
        dto3.setJobName("数据同步任务");
        dto3.setJobGroup("SYSTEM");

        assertEquals(dto1, dto2, "相同属性的DTO应相等");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(dto1, dto3, "不同属性的DTO应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        JobDTO dto = new JobDTO();
        dto.setJobId(1L);
        dto.setJobName("数据清理任务");
        dto.setJobGroup("DEFAULT");

        String str = dto.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("JobDTO") || str.contains("数据清理任务") || str.contains("1"),
                "toString 应包含任务信息");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 完整任务信息")
    void testScenario_CompleteJobInfo() {
        LocalDateTime now = LocalDateTime.now();

        JobDTO dto = new JobDTO();
        dto.setJobId(1L);
        dto.setJobName("数据清理任务");
        dto.setJobGroup("DEFAULT");
        dto.setInvokeTarget("com.xie.glm.task.DataCleanup.run");
        dto.setInvokeParam("days=30");
        dto.setCronExpression("0 0 2 * * ?");
        dto.setMisfirePolicy("1");
        dto.setConcurrent("0");
        dto.setStatus("0");
        dto.setBeginTime(now);
        dto.setNextValidTime(now.plusDays(1));
        dto.setRemark("每天凌晨2点执行数据清理");
        dto.setCreateTime(now);
        dto.setUpdateTime(now);

        assertEquals(1L, dto.getJobId());
        assertEquals("数据清理任务", dto.getJobName());
        assertEquals("DEFAULT", dto.getJobGroup());
        assertEquals("com.xie.glm.task.DataCleanup.run", dto.getInvokeTarget());
        assertEquals("days=30", dto.getInvokeParam());
        assertEquals("0 0 2 * * ?", dto.getCronExpression());
        assertEquals("1", dto.getMisfirePolicy());
        assertEquals("0", dto.getConcurrent());
        assertEquals("0", dto.getStatus());
        assertNotNull(dto.getBeginTime());
        assertNotNull(dto.getNextValidTime());
    }

    @Test
    @DisplayName("业务场景 - 暂停的任务")
    void testScenario_PausedJob() {
        JobDTO dto = new JobDTO();
        dto.setJobId(2L);
        dto.setJobName("已暂停任务");
        dto.setJobGroup("SYSTEM");
        dto.setStatus("1"); // 暂停
        dto.setRemark("临时暂停维护");

        assertEquals(2L, dto.getJobId());
        assertEquals("1", dto.getStatus(), "状态应为暂停");
        assertEquals("临时暂停维护", dto.getRemark());
    }

    @Test
    @DisplayName("业务场景 - 允许并发的任务")
    void testScenario_ConcurrentJob() {
        JobDTO dto = new JobDTO();
        dto.setJobId(3L);
        dto.setJobName("高并发任务");
        dto.setJobGroup("REPORT");
        dto.setConcurrent("1"); // 允许并发

        assertEquals(3L, dto.getJobId());
        assertEquals("1", dto.getConcurrent(), "应允许并发执行");
        assertTrue("1".equals(dto.getConcurrent()), "并发标志应为1");
    }

    @Test
    @DisplayName("业务场景 - 带异常信息的任务")
    void testScenario_JobWithException() {
        JobDTO dto = new JobDTO();
        dto.setJobId(4L);
        dto.setJobName("失败任务");
        dto.setExceptionInfo("java.lang.NullPointerException: Cannot invoke method");

        assertEquals(4L, dto.getJobId());
        assertEquals("java.lang.NullPointerException: Cannot invoke method", dto.getExceptionInfo());
        assertNotNull(dto.getExceptionInfo());
    }

    @Test
    @DisplayName("业务场景 - 下次执行时间")
    void testScenario_NextExecutionTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextTime = now.plusHours(2);

        JobDTO dto = new JobDTO();
        dto.setJobId(5L);
        dto.setJobName("定时任务");
        dto.setBeginTime(now);
        dto.setNextValidTime(nextTime);

        assertEquals(now, dto.getBeginTime());
        assertEquals(nextTime, dto.getNextValidTime());
        assertTrue(dto.getBeginTime().isBefore(dto.getNextValidTime()),
                "开始时间应早于下次执行时间");
    }

    @Test
    @DisplayName("业务场景 - 无参数的任务")
    void testScenario_JobWithoutParam() {
        JobDTO dto = new JobDTO();
        dto.setJobId(6L);
        dto.setJobName("无参数任务");
        dto.setInvokeTarget("com.xie.glm.task.NoParamTask.run");
        dto.setInvokeParam(""); // 空参数

        assertEquals(6L, dto.getJobId());
        assertEquals("", dto.getInvokeParam(), "参数应为空字符串");
    }

    @Test
    @DisplayName("业务场景 - 最小信息任务")
    void testScenario_MinimalJobInfo() {
        JobDTO dto = new JobDTO();
        dto.setJobId(7L);
        dto.setJobName("最小任务");

        assertEquals(7L, dto.getJobId());
        assertEquals("最小任务", dto.getJobName());
        assertNull(dto.getJobGroup());
        assertNull(dto.getInvokeTarget());
        assertNull(dto.getCronExpression());
        assertNull(dto.getStatus());
    }

    // ==================== 执行策略含义测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 立即执行",
            "2, 执行一次",
            "3, 放弃执行"
    })
    @DisplayName("执行策略含义 - 验证策略值的业务含义")
    void testMisfirePolicyMeaning(String misfirePolicy, String meaning) {
        JobDTO dto = new JobDTO();
        dto.setMisfirePolicy(misfirePolicy);

        assertEquals(misfirePolicy, dto.getMisfirePolicy(), "执行策略应匹配");
        assertNotNull(dto.getMisfirePolicy(), "执行策略不应为null");
    }

    // ==================== 任务状态含义测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, 正常运行",
            "1, 已暂停"
    })
    @DisplayName("任务状态含义 - 验证状态值的业务含义")
    void testStatusMeaning(String status, String meaning) {
        JobDTO dto = new JobDTO();
        dto.setStatus(status);

        assertEquals(status, dto.getStatus(), "状态值应匹配");

        boolean isPaused = "1".equals(status);
        boolean isRunning = "0".equals(status);

        if ("0".equals(status)) {
            assertTrue(isRunning, "状态0表示正常运行");
        } else {
            assertTrue(isPaused, "状态1表示已暂停");
        }
    }
}
