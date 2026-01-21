package com.xie.glm.common.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PageResult 分页结果包装类测试
 * <p>
 * 测试原则：
 * 1. 参数化测试覆盖各种分页场景
 * 2. 测试 Java record 的不可变性
 * 3. 验证 records、total 字段
 * 4. 验证 getPages(Long pageSize) 总页数计算
 *
 * @author xie
 */
class PageResultTest {

    /**
     * 测试标准构造方法
     */
    @Test
    void testConstructor() {
        List<String> records = Arrays.asList("item1", "item2", "item3");
        PageResult<String> result = new PageResult<>(records, 100L);

        assertEquals(records, result.records());
        assertEquals(100L, result.total());
    }

    /**
     * 参数化测试：不同分页数据场景
     * <p>
     * 测试数据：
     * - 标准场景
     * - 空记录场景
     * - null 场景
     */
    @ParameterizedTest
    @MethodSource("providePageData")
    <T> void testPageData(List<T> records, Long total) {
        PageResult<T> result = new PageResult<>(records, total);

        assertEquals(records, result.records());
        assertEquals(total, result.total());
    }

    /**
     * 测试 toString 方法
     */
    @Test
    void testToString() {
        List<String> records = Arrays.asList("item1", "item2");
        PageResult<String> result = new PageResult<>(records, 100L);

        String toString = result.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("100"));
    }

    /**
     * 测试 equals 和 hashCode 方法（record 自动生成）
     */
    @Test
    void testEqualsAndHashCode() {
        List<String> records = Arrays.asList("item1", "item2");

        PageResult<String> result1 = new PageResult<>(records, 100L);
        PageResult<String> result2 = new PageResult<>(records, 100L);
        PageResult<String> result3 = new PageResult<>(records, 200L);

        // 相同值应该相等
        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());

        // 不同值不应该相等
        assertNotEquals(result1, result3);
        assertNotEquals(result1.hashCode(), result3.hashCode());
    }

    /**
     * 测试空分页结果
     */
    @Test
    void testEmptyPageResult() {
        PageResult<String> result = new PageResult<>(Collections.emptyList(), 0L);

        assertTrue(result.records().isEmpty());
        assertEquals(0L, result.total());
    }

    /**
     * 测试获取记录数量
     */
    @Test
    void testGetRecordCount() {
        List<String> records = Arrays.asList("item1", "item2", "item3");
        PageResult<String> result = new PageResult<>(records, 100L);

        // 验证可以获取当前页记录数
        assertEquals(3, result.records().size());
    }

    /**
     * 测试 getPages(Long pageSize) 总页数计算
     */
    @Test
    void testGetPages() {
        // 100 条记录，每页 10 条，应该有 10 页
        PageResult<String> result1 = new PageResult<>(Collections.emptyList(), 100L);
        assertEquals(10L, result1.getPages(10L));

        // 95 条记录，每页 10 条，应该有 10 页（向上取整）
        PageResult<String> result2 = new PageResult<>(Collections.emptyList(), 95L);
        assertEquals(10L, result2.getPages(10L));

        // 0 条记录，应该有 0 页
        PageResult<String> result3 = new PageResult<>(Collections.emptyList(), 0L);
        assertEquals(0L, result3.getPages(10L));

        // null 值处理
        PageResult<String> result4 = new PageResult<>(null, null);
        assertEquals(0L, result4.getPages(10L));
    }

    /**
     * 测试 record 的不可变性
     */
    @Test
    void testImmutability() {
        List<String> records = Arrays.asList("item1", "item2");
        PageResult<String> result = new PageResult<>(records, 100L);

        // record 的字段访问
        assertEquals(records, result.records());
        assertEquals(100L, result.total());

        // 传统 getter 方法也可用
        assertEquals(records, result.getRecords());
        assertEquals(100L, result.getTotal());
    }

    // ==================== 测试数据提供方法 ====================

    /**
     * 提供分页测试数据
     */
    private static Stream<Arguments> providePageData() {
        return Stream.of(
            Arguments.of(Arrays.asList("item1", "item2", "item3"), 100L),
            Arguments.of(Collections.emptyList(), 0L),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5), 50L),
            Arguments.of(null, null)
        );
    }
}
