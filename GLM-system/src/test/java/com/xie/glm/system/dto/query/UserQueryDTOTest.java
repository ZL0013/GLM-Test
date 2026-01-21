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
 * UserQueryDTO 用户查询条件 DTO 测试类
 *
 * <p>测试用户查询条件数据传输对象的各种属性和行为
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
@DisplayName("UserQueryDTO 用户查询条件 DTO 单元测试")
class UserQueryDTOTest {

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        UserQueryDTO queryDTO = new UserQueryDTO();

        // 用户基本信息查询条件
        assertNull(queryDTO.getUserName(), "默认用户名查询条件应为null");
        assertNull(queryDTO.getNickName(), "默认昵称查询条件应为null");
        assertNull(queryDTO.getEmail(), "默认邮箱查询条件应为null");
        assertNull(queryDTO.getPhonenumber(), "默认手机号查询条件应为null");
        assertNull(queryDTO.getSex(), "默认性别查询条件应为null");
        assertNull(queryDTO.getStatus(), "默认状态查询条件应为null");
        assertNull(queryDTO.getDeptId(), "默认部门ID查询条件应为null");

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
            "admin, 管理员, admin@example.com, 13800138000, 0, 100, 1, 10, user_id, asc",
            "user, 普通用户, user@example.com, 13900139000, 1, 101, 2, 20, create_time, desc",
            "test, 测试, test@example.com, 13700137000, 2, 102, 1, 50, user_name, asc"
    })
    @DisplayName("Getter/Setter - 验证查询条件字段赋值和获取")
    void testGettersSetters(String userName, String nickName, String email,
                           String phonenumber, String sex, Long deptId,
                           Integer pageNum, Integer pageSize,
                           String orderByColumn, String isAsc) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();

        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setUserName(userName);
        queryDTO.setNickName(nickName);
        queryDTO.setEmail(email);
        queryDTO.setPhonenumber(phonenumber);
        queryDTO.setSex(sex);
        queryDTO.setStatus("0");
        queryDTO.setDeptId(deptId);
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        queryDTO.setOrderByColumn(orderByColumn);
        queryDTO.setIsAsc(isAsc);

        assertEquals(userName, queryDTO.getUserName(), "用户名查询条件应匹配");
        assertEquals(nickName, queryDTO.getNickName(), "昵称查询条件应匹配");
        assertEquals(email, queryDTO.getEmail(), "邮箱查询条件应匹配");
        assertEquals(phonenumber, queryDTO.getPhonenumber(), "手机号查询条件应匹配");
        assertEquals(sex, queryDTO.getSex(), "性别查询条件应匹配");
        assertEquals("0", queryDTO.getStatus(), "状态查询条件应匹配");
        assertEquals(deptId, queryDTO.getDeptId(), "部门ID查询条件应匹配");
        assertEquals(startTime, queryDTO.getStartTime(), "开始时间应匹配");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间应匹配");
        assertEquals(pageNum, queryDTO.getPageNum(), "页码应匹配");
        assertEquals(pageSize, queryDTO.getPageSize(), "每页大小应匹配");
        assertEquals(orderByColumn, queryDTO.getOrderByColumn(), "排序列应匹配");
        assertEquals(isAsc, queryDTO.getIsAsc(), "排序方向应匹配");
    }

    // ==================== 用户名模糊查询测试 ====================

    @ParameterizedTest
    @CsvSource({
            "admin, true",
            "adm, true",
            "in, true",
            ", false"
    })
    @DisplayName("用户名模糊查询 - 验证模糊搜索条件")
    void testUserNameFuzzySearch(String userName, boolean shouldHaveCondition) {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setUserName(userName);

        assertEquals(userName, queryDTO.getUserName(), "用户名条件应匹配");

        if (shouldHaveCondition && userName != null) {
            assertNotNull(queryDTO.getUserName(), "模糊搜索条件不应为null");
            assertTrue(queryDTO.getUserName().length() > 0, "模糊搜索条件不应为空字符串");
        }
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
        UserQueryDTO queryDTO = new UserQueryDTO();
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
        UserQueryDTO queryDTO = new UserQueryDTO();

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
        UserQueryDTO queryDTO = new UserQueryDTO();
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

    // ==================== 部门筛选测试 ====================

    @ParameterizedTest
    @CsvSource({
            "100, true",
            "101, true",
            "102, true",
            ", false"
    })
    @DisplayName("部门筛选 - 验证部门ID查询条件")
    void testDepartmentFilter(Long deptId, boolean shouldHaveCondition) {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setDeptId(deptId);

        assertEquals(deptId, queryDTO.getDeptId(), "部门ID应匹配");

        if (shouldHaveCondition) {
            assertNotNull(queryDTO.getDeptId(), "部门ID不应为null");
            assertTrue(queryDTO.getDeptId() > 0, "部门ID应为正数");
        }
    }

    // ==================== 用户状态筛选测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"0", "1"})
    @DisplayName("用户状态筛选 - 验证状态值有效性")
    void testStatusFilter(String status) {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setStatus(status);

        assertEquals(status, queryDTO.getStatus(), "状态值应匹配");
        assertTrue(List.of("0", "1").contains(queryDTO.getStatus()),
                "状态值应为 0=正常 或 1=停用");
    }

    // ==================== 性别筛选测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "2"})
    @DisplayName("性别筛选 - 验证性别值有效性")
    void testSexFilter(String sex) {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setSex(sex);

        assertEquals(sex, queryDTO.getSex(), "性别值应匹配");
        assertTrue(List.of("0", "1", "2").contains(queryDTO.getSex()),
                "性别值应为 0=男，1=女，2=未知");
    }

    // ==================== 排序参数测试 ====================

    @ParameterizedTest
    @CsvSource({
            "user_id, asc",
            "create_time, desc",
            "user_name, asc",
            ", "
    })
    @DisplayName("排序参数 - 验证排序条件")
    void testSortingParameters(String orderByColumn, String isAsc) {
        UserQueryDTO queryDTO = new UserQueryDTO();
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
        UserQueryDTO queryDTO = new UserQueryDTO();

        queryDTO.setUserName(value);
        queryDTO.setNickName(value);
        queryDTO.setEmail(value);
        queryDTO.setPhonenumber(value);
        queryDTO.setSex(value);
        queryDTO.setStatus(value);

        assertNull(queryDTO.getUserName(), "null 用户名应被接受");
        assertNull(queryDTO.getNickName(), "null 昵称应被接受");
        assertNull(queryDTO.getEmail(), "null 邮箱应被接受");
        assertNull(queryDTO.getPhonenumber(), "null 手机号应被接受");
        assertNull(queryDTO.getSex(), "null 性别应被接受");
        assertNull(queryDTO.getStatus(), "null 状态应被接受");
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        UserQueryDTO dto1 = new UserQueryDTO();
        dto1.setUserName("admin");
        dto1.setPageNum(1);
        dto1.setPageSize(10);

        UserQueryDTO dto2 = new UserQueryDTO();
        dto2.setUserName("admin");
        dto2.setPageNum(1);
        dto2.setPageSize(10);

        UserQueryDTO dto3 = new UserQueryDTO();
        dto3.setUserName("user");
        dto3.setPageNum(1);
        dto3.setPageSize(10);

        assertEquals(dto1, dto2, "相同查询条件的DTO应相等");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(dto1, dto3, "不同查询条件的DTO应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setUserName("admin");
        queryDTO.setStatus("0");

        String str = queryDTO.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("UserQueryDTO") || str.contains("admin") || str.contains("0"),
                "toString 应包含查询条件信息");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 精确查询用户名")
    void testScenario_ExactUsernameSearch() {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setUserName("admin");
        queryDTO.setStatus("0");

        assertEquals("admin", queryDTO.getUserName(), "应查询admin用户");
        assertEquals("0", queryDTO.getStatus(), "仅查询正常状态用户");
        assertNull(queryDTO.getDeptId(), "不限制部门");
        assertNull(queryDTO.getStartTime(), "不限制时间范围");
    }

    @Test
    @DisplayName("业务场景 - 部门用户列表查询")
    void testScenario_DepartmentUserList() {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setDeptId(100L);
        queryDTO.setStatus("0");
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(20);
        queryDTO.setOrderByColumn("user_name");
        queryDTO.setIsAsc("asc");

        assertEquals(100L, queryDTO.getDeptId(), "查询部门100的用户");
        assertEquals("0", queryDTO.getStatus(), "仅查询正常状态用户");
        assertEquals(1, queryDTO.getPageNum(), "第1页");
        assertEquals(20, queryDTO.getPageSize(), "每页20条");
        assertEquals("user_name", queryDTO.getOrderByColumn(), "按用户名排序");
        assertEquals("asc", queryDTO.getIsAsc(), "升序排列");
    }

    @Test
    @DisplayName("业务场景 - 时间范围查询")
    void testScenario_TimeRangeSearch() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();

        UserQueryDTO queryDTO = new UserQueryDTO();
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

        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setUserName("adm");  // 模糊搜索
        queryDTO.setDeptId(100L);    // 部门筛选
        queryDTO.setStatus("0");      // 正常状态
        queryDTO.setSex("0");         // 性别筛选
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(10);
        queryDTO.setOrderByColumn("create_time");
        queryDTO.setIsAsc("desc");

        // 验证所有条件
        assertEquals("adm", queryDTO.getUserName(), "用户名模糊条件");
        assertEquals(100L, queryDTO.getDeptId(), "部门条件");
        assertEquals("0", queryDTO.getStatus(), "状态条件");
        assertEquals("0", queryDTO.getSex(), "性别条件");
        assertEquals(startTime, queryDTO.getStartTime(), "开始时间");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间");
        assertEquals(1, queryDTO.getPageNum(), "页码");
        assertEquals(10, queryDTO.getPageSize(), "每页大小");
        assertEquals("create_time", queryDTO.getOrderByColumn(), "排序列");
        assertEquals("desc", queryDTO.getIsAsc(), "排序方向");
    }

    @Test
    @DisplayName("业务场景 - 无条件查询（全部用户）")
    void testScenario_NoConditionSearch() {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(20);

        // 验证无查询条件
        assertNull(queryDTO.getUserName(), "无用户名条件");
        assertNull(queryDTO.getNickName(), "无昵称条件");
        assertNull(queryDTO.getEmail(), "无邮箱条件");
        assertNull(queryDTO.getPhonenumber(), "无手机号条件");
        assertNull(queryDTO.getSex(), "无性别条件");
        assertNull(queryDTO.getStatus(), "无状态条件");
        assertNull(queryDTO.getDeptId(), "无部门条件");
        assertNull(queryDTO.getStartTime(), "无开始时间");
        assertNull(queryDTO.getEndTime(), "无结束时间");
        assertEquals(1, queryDTO.getPageNum(), "默认第1页");
        assertEquals(20, queryDTO.getPageSize(), "默认每页20条");
    }

    @Test
    @DisplayName("业务场景 - 邮箱或手机号查询")
    void testScenario_EmailOrPhoneSearch() {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setEmail("admin@example.com");

        assertEquals("admin@example.com", queryDTO.getEmail(), "按邮箱查询");
        assertNull(queryDTO.getPhonenumber(), "无手机号条件");

        // 切换为手机号查询
        queryDTO.setEmail(null);
        queryDTO.setPhonenumber("13800138000");

        assertNull(queryDTO.getEmail(), "无邮箱条件");
        assertEquals("13800138000", queryDTO.getPhonenumber(), "按手机号查询");
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
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setIsAsc(isAsc);

        assertEquals(isAsc, queryDTO.getIsAsc(), "排序方向应匹配");
        assertEquals(isAscending, "asc".equalsIgnoreCase(queryDTO.getIsAsc()),
                "asc（忽略大小写）表示升序");
    }
}
