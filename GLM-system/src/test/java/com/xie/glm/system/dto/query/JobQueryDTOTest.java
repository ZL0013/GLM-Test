package com.xie.glm.system.dto.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JobQueryDTO 定时任务查询条件 DTO 测试类
 *
 * <p>测试定时任务查询条件数据传输对象的各种属性和行为
 * <p>测试原则：
 * <ul>
 *   <li>参数化测试覆盖查询条件场景</li>
 *   <li>验证 Lombok @Data 注解生成的 getter/setter</li>
 *   <li>测试分页参数的有效性</li>
 *   <li>验证查询条件的组合使用</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("JobQueryDTO 定时任务查询条件 DTO 单元测试")
class JobQueryDTOTest {

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        JobQueryDTO queryDTO = new JobQueryDTO();

        // 任务基本信息查询条件
        assertNull(queryDTO.getJobName(), "默认任务名称查询条件应为null");
        assertNull(queryDTO.getJobGroup(), "默认任务组查询条件应为null");
        assertNull(queryDTO.getStatus(), "默认状态查询条件应为null");

        // 时间范围查询条件
        assertNull(queryDTO.getStartTime(), "默认开始时间应为null");
        assertNull(queryDTO.getEndTime(), "默认结束时间应为null");

        // 分页参数
        assertNull(queryDTO.getPageNum(), "默认页码应为null");
        assertNull(queryDTO.getPageSize(), "默认每页大小应为null");

        // 排序参数
        assertNull(queryDTO.getOrderByColumn(), "默认排序列应为null");
        assertNull(queryDTO.getIsAsc(), "默认排序方向应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "数据清理任务, DEFAULT, 0, 1, 10, job_id, asc",
            "数据同步任务, SYSTEM, 1, 2, 20, job_name, desc",
            "报表生成任务, REPORT, 0, 1, 50, create_time, asc"
    })
    @DisplayName("Getter/Setter - 验证查询条件字段赋值和获取")
    void testGettersSetters(String jobName, String jobGroup, String status,
                            Integer pageNum, Integer pageSize,
                            String orderByColumn, String isAsc) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();

        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setJobName(jobName);
        queryDTO.setJobGroup(jobGroup);
        queryDTO.setStatus(status);
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        queryDTO.setOrderByColumn(orderByColumn);
        queryDTO.setIsAsc(isAsc);

        assertEquals(jobName, queryDTO.getJobName(), "任务名称查询条件应匹配");
        assertEquals(jobGroup, queryDTO.getJobGroup(), "任务组查询条件应匹配");
        assertEquals(status, queryDTO.getStatus(), "状态查询条件应匹配");
        assertEquals(startTime, queryDTO.getStartTime(), "开始时间应匹配");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间应匹配");
        assertEquals(pageNum, queryDTO.getPageNum(), "页码应匹配");
        assertEquals(pageSize, queryDTO.getPageSize(), "每页大小应匹配");
        assertEquals(orderByColumn, queryDTO.getOrderByColumn(), "排序列应匹配");
        assertEquals(isAsc, queryDTO.getIsAsc(), "排序方向应匹配");
    }

    // ==================== 任务名称模糊查询测试 ====================

    @ParameterizedTest
    @CsvSource({
            "数据清理, true",
            "清理, true",
            "任务, true",
            ", false"
    })
    @DisplayName("任务名称模糊查询 - 验证模糊搜索条件")
    void testJobNameFuzzySearch(String jobName, boolean shouldHaveCondition) {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setJobName(jobName);

        assertEquals(jobName, queryDTO.getJobName(), "任务名称条件应匹配");

        if (shouldHaveCondition && jobName != null) {
            assertNotNull(queryDTO.getJobName(), "模糊搜索条件不应为null");
            assertTrue(queryDTO.getJobName().length() > 0, "模糊搜索条件不应为空字符串");
        }
    }

    // ==================== 任务组筛选测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"DEFAULT", "SYSTEM", "REPORT", "SYNC"})
    @DisplayName("任务组筛选 - 验证任务组查询条件")
    void testJobGroupFilter(String jobGroup) {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setJobGroup(jobGroup);

        assertEquals(jobGroup, queryDTO.getJobGroup(), "任务组应匹配");
        assertTrue(List.of("DEFAULT", "SYSTEM", "REPORT", "SYNC").contains(queryDTO.getJobGroup()),
                "任务组应为预定义的值之一");
    }

    // ==================== 任务状态筛选测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"0", "1"})
    @DisplayName("任务状态筛选 - 验证状态值有效性")
    void testStatusFilter(String status) {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setStatus(status);

        assertEquals(status, queryDTO.getStatus(), "状态值应匹配");
        assertTrue(List.of("0", "1").contains(queryDTO.getStatus()),
                "状态值应为 0=正常 或 1=暂停");
    }

    // ==================== 分页参数测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 10, true",
            "2, 20, true",
            "1, 50, true",
            "0, 10, false",
            "1, 0, false",
            "-1, 10, false",
            "1, -1, false"
    })
    @DisplayName("分页参数 - 验证分页参数有效性")
    void testPaginationParameters(Integer pageNum, Integer pageSize, boolean isValid) {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);

        assertEquals(pageNum, queryDTO.getPageNum(), "页码应匹配");
        assertEquals(pageSize, queryDTO.getPageSize(), "每页大小应匹配");

        if (isValid) {
            assertTrue(queryDTO.getPageNum() > 0, "有效页码应大于0");
            assertTrue(queryDTO.getPageSize() > 0, "有效每页大小应大于0");
        }
    }

    @Test
    @DisplayName("默认分页参数 - 验证默认值设置")
    void testDefaultPaginationValues() {
        JobQueryDTO queryDTO = new JobQueryDTO();

        // 测试设置 null 值后的默认行为
        queryDTO.setPageNum(null);
        queryDTO.setPageSize(null);

        assertNull(queryDTO.getPageNum(), "页码应为null（使用Service层默认值）");
        assertNull(queryDTO.getPageSize(), "每页大小应为null（使用Service层默认值）");
    }

    // ==================== 时间范围查询测试 ====================

    @ParameterizedTest
    @MethodSource("provideTimeRangeData")
    @DisplayName("时间范围查询 - 验证时间范围条件")
    void testTimeRangeQuery(LocalDateTime startTime, LocalDateTime endTime) {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);

        assertEquals(startTime, queryDTO.getStartTime(), "开始时间应匹配");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间应匹配");

        // 验证时间范围的逻辑性
        if (startTime != null && endTime != null) {
            assertTrue(startTime.isBefore(endTime) || startTime.isEqual(endTime),
                    "开始时间应早于或等于结束时间");
        }
    }

    private static Stream<Arguments> provideTimeRangeData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now.minusDays(30), now, "最近30天"),
                Arguments.of(now.minusMonths(1), now, "最近1个月"),
                Arguments.of(now.minusYears(1), now, "最近1年"),
                Arguments.of(null, null, "无时间限制"),
                Arguments.of(now.minusDays(7), null, "只有开始时间")
        );
    }

    // ==================== 排序参数测试 ====================

    @ParameterizedTest
    @CsvSource({
            "job_id, asc",
            "create_time, desc",
            "job_name, asc",
            ", "
    })
    @DisplayName("排序参数 - 验证排序条件")
    void testSortingParameters(String orderByColumn, String isAsc) {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setOrderByColumn(orderByColumn);
        queryDTO.setIsAsc(isAsc);

        assertEquals(orderByColumn, queryDTO.getOrderByColumn(), "排序列应匹配");
        assertEquals(isAsc, queryDTO.getIsAsc(), "排序方向应匹配");

        if (orderByColumn != null && isAsc != null) {
            assertTrue(List.of("asc", "desc").contains(queryDTO.getIsAsc().toLowerCase()),
                    "排序方向应为 asc 或 desc");
        }
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证查询条件 setter 容错")
    void testNullHandling(String value) {
        JobQueryDTO queryDTO = new JobQueryDTO();

        queryDTO.setJobName(value);
        queryDTO.setJobGroup(value);
        queryDTO.setStatus(value);

        assertNull(queryDTO.getJobName(), "null 任务名称应被接受");
        assertNull(queryDTO.getJobGroup(), "null 任务组应被接受");
        assertNull(queryDTO.getStatus(), "null 状态应被接受");
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        JobQueryDTO dto1 = new JobQueryDTO();
        dto1.setJobName("数据清理任务");
        dto1.setJobGroup("DEFAULT");
        dto1.setPageNum(1);
        dto1.setPageSize(10);

        JobQueryDTO dto2 = new JobQueryDTO();
        dto2.setJobName("数据清理任务");
        dto2.setJobGroup("DEFAULT");
        dto2.setPageNum(1);
        dto2.setPageSize(10);

        JobQueryDTO dto3 = new JobQueryDTO();
        dto3.setJobName("数据同步任务");
        dto3.setJobGroup("SYSTEM");
        dto3.setPageNum(1);
        dto3.setPageSize(10);

        assertEquals(dto1, dto2, "相同查询条件的DTO应相等");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(dto1, dto3, "不同查询条件的DTO应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setJobName("数据清理任务");
        queryDTO.setStatus("0");

        String str = queryDTO.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("JobQueryDTO") || str.contains("数据清理任务") || str.contains("0"),
                "toString 应包含查询条件信息");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 查询所有运行中的任务")
    void testScenario_RunningJobs() {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setStatus("0"); // 正常运行
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(20);
        queryDTO.setOrderByColumn("job_id");
        queryDTO.setIsAsc("asc");

        assertEquals("0", queryDTO.getStatus(), "仅查询正常运行状态的任务");
        assertEquals(1, queryDTO.getPageNum(), "第1页");
        assertEquals(20, queryDTO.getPageSize(), "每页20条");
        assertEquals("job_id", queryDTO.getOrderByColumn(), "按任务ID排序");
        assertEquals("asc", queryDTO.getIsAsc(), "升序排列");
    }

    @Test
    @DisplayName("业务场景 - 查询指定任务组的任务")
    void testScenario_JobGroupFilter() {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setJobGroup("SYSTEM");
        queryDTO.setStatus("0");
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(50);

        assertEquals("SYSTEM", queryDTO.getJobGroup(), "查询SYSTEM任务组的任务");
        assertEquals("0", queryDTO.getStatus(), "仅查询正常运行状态任务");
        assertEquals(1, queryDTO.getPageNum(), "第1页");
        assertEquals(50, queryDTO.getPageSize(), "每页50条");
    }

    @Test
    @DisplayName("业务场景 - 模糊搜索任务名称")
    void testScenario_JobNameFuzzySearch() {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setJobName("清理");
        queryDTO.setStatus("0");
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(10);

        assertEquals("清理", queryDTO.getJobName(), "模糊搜索包含'清理'的任务");
        assertEquals("0", queryDTO.getStatus(), "仅查询正常运行状态任务");
    }

    @Test
    @DisplayName("业务场景 - 时间范围查询")
    void testScenario_TimeRangeSearch() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();

        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(50);

        assertEquals(startTime, queryDTO.getStartTime(), "开始时间为30天前");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间为当前");
        assertTrue(queryDTO.getStartTime().isBefore(queryDTO.getEndTime()),
                "开始时间应早于结束时间");
    }

    @Test
    @DisplayName("业务场景 - 多条件组合查询")
    void testScenario_CombinedConditionsSearch() {
        LocalDateTime startTime = LocalDateTime.now().minusMonths(1);
        LocalDateTime endTime = LocalDateTime.now();

        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setJobName("清理"); // 模糊搜索
        queryDTO.setJobGroup("DEFAULT"); // 任务组筛选
        queryDTO.setStatus("0"); // 正常运行状态
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(10);
        queryDTO.setOrderByColumn("create_time");
        queryDTO.setIsAsc("desc");

        // 验证所有条件
        assertEquals("清理", queryDTO.getJobName(), "任务名称模糊条件");
        assertEquals("DEFAULT", queryDTO.getJobGroup(), "任务组条件");
        assertEquals("0", queryDTO.getStatus(), "状态条件");
        assertEquals(startTime, queryDTO.getStartTime(), "开始时间");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间");
        assertEquals(1, queryDTO.getPageNum(), "页码");
        assertEquals(10, queryDTO.getPageSize(), "每页大小");
        assertEquals("create_time", queryDTO.getOrderByColumn(), "排序列");
        assertEquals("desc", queryDTO.getIsAsc(), "排序方向");
    }

    @Test
    @DisplayName("业务场景 - 无条件查询（全部任务）")
    void testScenario_NoConditionSearch() {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(20);

        // 验证无查询条件
        assertNull(queryDTO.getJobName(), "无任务名称条件");
        assertNull(queryDTO.getJobGroup(), "无任务组条件");
        assertNull(queryDTO.getStatus(), "无状态条件");
        assertNull(queryDTO.getStartTime(), "无开始时间");
        assertNull(queryDTO.getEndTime(), "无结束时间");
        assertEquals(1, queryDTO.getPageNum(), "默认第1页");
        assertEquals(20, queryDTO.getPageSize(), "默认每页20条");
    }

    // ==================== 排序方向测试 ====================

    @ParameterizedTest
    @CsvSource({
            "asc, true",
            "ASC, true",
            "desc, false",
            "DESC, false"
    })
    @DisplayName("排序方向 - 验证升序降序标识")
    void testIsAscendingOrder(String isAsc, boolean isAscending) {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setIsAsc(isAsc);

        assertEquals(isAsc, queryDTO.getIsAsc(), "排序方向应匹配");
        assertEquals(isAscending, "asc".equalsIgnoreCase(queryDTO.getIsAsc()),
                "asc（忽略大小写）表示升序");
    }

    // ==================== 任务状态特殊值测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, 正常运行",
            "1, 已暂停"
    })
    @DisplayName("任务状态含义 - 验证状态值的业务含义")
    void testStatusMeaning(String status, String meaning) {
        JobQueryDTO queryDTO = new JobQueryDTO();
        queryDTO.setStatus(status);

        assertEquals(status, queryDTO.getStatus(), "状态值应匹配");

        boolean isPaused = "1".equals(status);
        boolean isRunning = "0".equals(status);

        if ("0".equals(status)) {
            assertTrue(isRunning, "状态0表示正常运行");
        } else {
            assertTrue(isPaused, "状态1表示已暂停");
        }
    }
}
