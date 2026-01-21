package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DictQueryDTO 测试类
 *
 * <p>测试字典查询条件 DTO 的各种属性和行为
 *
 * @author xie
 */
@DisplayName("DictQueryDTO 查询条件单元测试")
class DictQueryDTOTest {

    // ==================== 继承关系测试 ====================

    @Test
    @DisplayName("继承 PageQuery - 验证继承关系")
    void testExtendsPageQuery() {
        DictQueryDTO queryDTO = new DictQueryDTO();

        assertTrue(queryDTO instanceof PageQuery, "DictQueryDTO 应继承 PageQuery");
        assertTrue(queryDTO instanceof PageQuery, "应可转换为 PageQuery");

        // 验证基类字段
        PageQuery base = queryDTO;
        assertNull(base.getPageNum(), "默认页码应为null");
        assertNull(base.getPageSize(), "默认每页大小应为null");
        assertNull(base.getOrderByColumn(), "默认排序列应为null");
        assertNull(base.getIsAsc(), "默认排序方向应为null");
    }

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        DictQueryDTO queryDTO = new DictQueryDTO();

        // 字典类型查询条件
        assertNull(queryDTO.getDictName(), "默认字典名称应为null");
        assertNull(queryDTO.getDictType(), "默认字典类型应为null");
        assertNull(queryDTO.getStatus(), "默认状态应为null");

        // 时间范围
        assertNull(queryDTO.getStartTime(), "默认开始时间应为null");
        assertNull(queryDTO.getEndTime(), "默认结束时间应为null");

        // 基类分页字段
        assertNull(queryDTO.getPageNum(), "默认页码应为null");
        assertNull(queryDTO.getPageSize(), "默认每页大小应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "用户性别, sys_user_sex, 0",
            "菜单状态, sys_show_hide, 0",
            "系统开关, sys_normal_disable, 1"
    })
    @DisplayName("Getter/Setter - 验证字典类型查询条件")
    void testDictTypeQuerySetters(String dictName, String dictType, String status) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setDictName(dictName);
        queryDTO.setDictType(dictType);
        queryDTO.setStatus(status);
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(10);

        assertEquals(dictName, queryDTO.getDictName(), "字典名称应匹配");
        assertEquals(dictType, queryDTO.getDictType(), "字典类型应匹配");
        assertEquals(status, queryDTO.getStatus(), "状态应匹配");
        assertEquals(startTime, queryDTO.getStartTime(), "开始时间应匹配");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间应匹配");
        assertEquals(1, queryDTO.getPageNum(), "页码应匹配");
        assertEquals(10, queryDTO.getPageSize(), "每页大小应匹配");
    }

    @ParameterizedTest
    @CsvSource({
            "男, sys_user_sex, 0",
            "显示, sys_show_hide, 0",
            "正常, sys_normal_disable, 0"
    })
    @DisplayName("Getter/Setter - 验证字典数据查询条件")
    void testDictDataQuerySetters(String dictLabel, String dictType, String status) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setDictLabel(dictLabel);
        queryDTO.setDictType(dictType);
        queryDTO.setStatus(status);
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(20);

        assertEquals(dictLabel, queryDTO.getDictLabel(), "字典标签应匹配");
        assertEquals(dictType, queryDTO.getDictType(), "字典类型应匹配");
        assertEquals(status, queryDTO.getStatus(), "状态应匹配");
        assertEquals(startTime, queryDTO.getStartTime(), "开始时间应匹配");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间应匹配");
        assertEquals(1, queryDTO.getPageNum(), "页码应匹配");
        assertEquals(20, queryDTO.getPageSize(), "每页大小应匹配");
    }

    // ==================== 分页参数测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 10",
            "2, 20",
            "5, 50",
            "10, 100"
    })
    @DisplayName("分页参数 - 验证分页设置")
    void testPagination(Integer pageNum, Integer pageSize) {
        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);

        assertEquals(pageNum, queryDTO.getPageNum(), "页码应匹配");
        assertEquals(pageSize, queryDTO.getPageSize(), "每页大小应匹配");
        assertTrue(queryDTO.getPageNum() > 0, "页码应大于0");
        assertTrue(queryDTO.getPageSize() > 0, "每页大小应大于0");
    }

    @ParameterizedTest
    @CsvSource({
            "dict_id, asc",
            "dict_name, asc",
            "create_time, desc",
            "dict_sort, asc"
    })
    @DisplayName("排序参数 - 验证排序设置")
    void testOrderBy(String orderByColumn, String isAsc) {
        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setOrderByColumn(orderByColumn);
        queryDTO.setIsAsc(isAsc);

        assertEquals(orderByColumn, queryDTO.getOrderByColumn(), "排序列应匹配");
        assertEquals(isAsc, queryDTO.getIsAsc(), "排序方向应匹配");
    }

    // ==================== 状态过滤测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1, false"
    })
    @DisplayName("状态过滤 - 验证状态筛选")
    void testStatusFilter(String status, boolean isNormal) {
        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setStatus(status);

        assertEquals(status, queryDTO.getStatus(), "状态应匹配");
        assertEquals(isNormal, "0".equals(queryDTO.getStatus()), "0 表示正常状态");
    }

    // ==================== 时间范围测试 ====================

    @Test
    @DisplayName("时间范围 - 验证时间范围查询")
    void testTimeRange() {
        LocalDateTime startTime = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 1, 31, 23, 59, 59);

        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);

        assertEquals(startTime, queryDTO.getStartTime(), "开始时间应匹配");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间应匹配");
        assertTrue(queryDTO.getStartTime().isBefore(queryDTO.getEndTime()), "开始时间应早于结束时间");
    }

    @Test
    @DisplayName("时间范围 - 验证仅设置开始时间")
    void testStartTimeOnly() {
        LocalDateTime startTime = LocalDateTime.now();

        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setStartTime(startTime);

        assertEquals(startTime, queryDTO.getStartTime(), "开始时间应匹配");
        assertNull(queryDTO.getEndTime(), "结束时间应为null");
    }

    @Test
    @DisplayName("时间范围 - 验证仅设置结束时间")
    void testEndTimeOnly() {
        LocalDateTime endTime = LocalDateTime.now();

        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setEndTime(endTime);

        assertNull(queryDTO.getStartTime(), "开始时间应为null");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间应匹配");
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证 setter 容错")
    void testNullHandling(String value) {
        DictQueryDTO queryDTO = new DictQueryDTO();

        queryDTO.setDictName(value);
        queryDTO.setDictType(value);
        queryDTO.setDictLabel(value);
        queryDTO.setStatus(value);

        assertNull(queryDTO.getDictName(), "null 字典名称应被接受");
        assertNull(queryDTO.getDictType(), "null 字典类型应被接受");
        assertNull(queryDTO.getDictLabel(), "null 字典标签应被接受");
        assertNull(queryDTO.getStatus(), "null 状态应被接受");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 查询所有正常状态的字典类型")
    void testQueryAllNormalDictTypes() {
        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setStatus("0");
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(20);
        queryDTO.setOrderByColumn("dict_id");
        queryDTO.setIsAsc("asc");

        assertEquals("0", queryDTO.getStatus(), "应查询正常状态");
        assertEquals(1, queryDTO.getPageNum(), "页码应为1");
        assertEquals(20, queryDTO.getPageSize(), "每页大小应为20");
        assertEquals("dict_id", queryDTO.getOrderByColumn(), "按字典ID排序");
        assertEquals("asc", queryDTO.getIsAsc(), "升序排列");
    }

    @Test
    @DisplayName("业务场景 - 按字典类型查询字典数据")
    void testQueryDictDataByType() {
        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setDictType("sys_user_sex");
        queryDTO.setStatus("0");

        assertEquals("sys_user_sex", queryDTO.getDictType(), "应查询用户性别字典");
        assertEquals("0", queryDTO.getStatus(), "应查询正常状态");
    }

    @Test
    @DisplayName("业务场景 - 按时间范围查询字典数据")
    void testQueryDictDataByTimeRange() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();

        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setStatus("0");

        assertEquals(startTime, queryDTO.getStartTime(), "开始时间应匹配");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间应匹配");
        assertEquals("0", queryDTO.getStatus(), "应查询正常状态");
    }

    @Test
    @DisplayName("业务场景 - 模糊查询字典名称")
    void testQueryDictNameFuzzy() {
        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setDictName("用户");
        queryDTO.setStatus("0");

        assertEquals("用户", queryDTO.getDictName(), "应包含'用户'关键字");
        assertEquals("0", queryDTO.getStatus(), "应查询正常状态");
    }

    @Test
    @DisplayName("业务场景 - 模糊查询字典标签")
    void testQueryDictLabelFuzzy() {
        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setDictLabel("男");
        queryDTO.setDictType("sys_user_sex");
        queryDTO.setStatus("0");

        assertEquals("男", queryDTO.getDictLabel(), "应包含'男'关键字");
        assertEquals("sys_user_sex", queryDTO.getDictType(), "应查询用户性别字典");
        assertEquals("0", queryDTO.getStatus(), "应查询正常状态");
    }

    // ==================== 组合查询测试 ====================

    @Test
    @DisplayName("组合查询 - 多条件组合查询")
    void testCombinedQuery() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setDictName("用户");
        queryDTO.setDictType("sys_user_sex");
        queryDTO.setStatus("0");
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(10);
        queryDTO.setOrderByColumn("create_time");
        queryDTO.setIsAsc("desc");

        assertEquals("用户", queryDTO.getDictName(), "字典名称应匹配");
        assertEquals("sys_user_sex", queryDTO.getDictType(), "字典类型应匹配");
        assertEquals("0", queryDTO.getStatus(), "状态应匹配");
        assertEquals(startTime, queryDTO.getStartTime(), "开始时间应匹配");
        assertEquals(endTime, queryDTO.getEndTime(), "结束时间应匹配");
        assertEquals(1, queryDTO.getPageNum(), "页码应匹配");
        assertEquals(10, queryDTO.getPageSize(), "每页大小应匹配");
        assertEquals("create_time", queryDTO.getOrderByColumn(), "按创建时间排序");
        assertEquals("desc", queryDTO.getIsAsc(), "降序排列");
    }

    // ==================== 排序列测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"dict_id", "dict_name", "dict_type", "dict_sort", "create_time", "update_time"})
    @DisplayName("排序列 - 验证有效排序列")
    void testValidOrderByColumns(String orderByColumn) {
        DictQueryDTO queryDTO = new DictQueryDTO();
        queryDTO.setOrderByColumn(orderByColumn);

        assertEquals(orderByColumn, queryDTO.getOrderByColumn(), "排序列应匹配");
    }
}
