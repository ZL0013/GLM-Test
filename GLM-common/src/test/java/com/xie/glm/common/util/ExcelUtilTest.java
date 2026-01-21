package com.xie.glm.common.util;

import com.xie.glm.common.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Excel 工具类测试
 *
 * @author xie
 */
@DisplayName("Excel 工具类测试")
class ExcelUtilTest {

    @TempDir
    Path tempDir;

    // ==================== 导出测试 ====================

    @Test
    @DisplayName("导出简单数据到 Excel")
    void testExportSimpleData() throws IOException {
        // 准备测试数据
        List<String> headers = Arrays.asList("姓名", "年龄", "邮箱");
        List<List<Object>> data = Arrays.asList(
            Arrays.asList("张三", 25, "zhangsan@example.com"),
            Arrays.asList("李四", 30, "lisi@example.com"),
            Arrays.asList("王五", 28, "wangwu@example.com")
        );

        // 创建输出文件
        File outputFile = tempDir.resolve("test_export.xlsx").toFile();

        // 执行导出
        ExcelUtil.export(outputFile, headers, data);

        // 验证文件存在
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }

    @Test
    @DisplayName("导出空数据到 Excel")
    void testExportEmptyData() throws IOException {
        List<String> headers = Arrays.asList("姓名", "年龄", "邮箱");
        List<List<Object>> data = new ArrayList<>();

        File outputFile = tempDir.resolve("test_empty.xlsx").toFile();

        ExcelUtil.export(outputFile, headers, data);

        assertTrue(outputFile.exists());
    }

    @Test
    @DisplayName("导出单行数据到 Excel")
    void testExportSingleRow() throws IOException {
        List<String> headers = Arrays.asList("姓名", "年龄", "邮箱");
        List<List<Object>> data = List.of(
            Arrays.asList("张三", 25, "zhangsan@example.com")
        );

        File outputFile = tempDir.resolve("test_single.xlsx").toFile();

        ExcelUtil.export(outputFile, headers, data);

        assertTrue(outputFile.exists());
    }

    // ==================== 导入测试 ====================

    @Test
    @DisplayName("从 Excel 导入数据")
    void testImportData() throws IOException {
        // 先准备测试文件
        List<String> headers = Arrays.asList("姓名", "年龄", "邮箱");
        List<List<Object>> originalData = Arrays.asList(
            Arrays.asList("张三", 25, "zhangsan@example.com"),
            Arrays.asList("李四", 30, "lisi@example.com"),
            Arrays.asList("王五", 28, "wangwu@example.com")
        );

        File testFile = tempDir.resolve("test_import.xlsx").toFile();
        ExcelUtil.export(testFile, headers, originalData);

        // 执行导入
        List<List<Object>> importedData = ExcelUtil.importData(testFile);

        // 验证导入的数据
        assertNotNull(importedData);
        assertEquals(3, importedData.size());

        // 验证第一行数据（Excel 读取的数据都是 String 类型）
        List<Object> firstRow = importedData.get(0);
        assertEquals("张三", firstRow.get(0));
        assertEquals("25", firstRow.get(1)); // 数字会被读取为字符串
        assertEquals("zhangsan@example.com", firstRow.get(2));
    }

    @Test
    @DisplayName("从空 Excel 文件导入")
    void testImportEmptyFile() throws IOException {
        File testFile = tempDir.resolve("test_empty_import.xlsx").toFile();
        ExcelUtil.export(testFile, Arrays.asList("列1", "列2"), new ArrayList<>());

        List<List<Object>> importedData = ExcelUtil.importData(testFile);

        assertNotNull(importedData);
        assertTrue(importedData.isEmpty());
    }

    // ==================== 异常处理测试 ====================

    @Test
    @DisplayName("导出 - null 文件处理")
    void testExportWithNullFile() {
        List<String> headers = Arrays.asList("姓名", "年龄");
        List<List<Object>> data = List.of(Arrays.asList("张三", 25));

        assertThrows(ServiceException.class, () ->
            ExcelUtil.export(null, headers, data)
        );
    }

    @Test
    @DisplayName("导出 - null 表头处理")
    void testExportWithNullHeaders() throws IOException {
        File outputFile = tempDir.resolve("test.xlsx").toFile();
        List<List<Object>> data = List.of(Arrays.asList("张三", 25));

        assertThrows(ServiceException.class, () ->
            ExcelUtil.export(outputFile, null, data)
        );
    }

    @Test
    @DisplayName("导出 - null 数据处理")
    void testExportWithNullData() throws IOException {
        File outputFile = tempDir.resolve("test.xlsx").toFile();
        List<String> headers = Arrays.asList("姓名", "年龄");

        // null 数据应该被当作空数据处理
        assertDoesNotThrow(() ->
            ExcelUtil.export(outputFile, headers, null)
        );
        assertTrue(outputFile.exists());
    }

    @Test
    @DisplayName("导入 - 文件不存在")
    void testImportNonExistentFile() {
        File nonExistentFile = tempDir.resolve("non_existent.xlsx").toFile();

        assertThrows(ServiceException.class, () ->
            ExcelUtil.importData(nonExistentFile)
        );
    }

    @Test
    @DisplayName("导入 - null 文件处理")
    void testImportWithNullFile() {
        assertThrows(ServiceException.class, () ->
            ExcelUtil.importData(null)
        );
    }

    // ==================== 往返测试 ====================

    @Test
    @DisplayName("导出导入往返测试")
    void testExportImportRoundTrip() throws IOException {
        // 准备原始数据
        List<String> headers = Arrays.asList("姓名", "年龄", "邮箱", "部门");
        List<List<Object>> originalData = Arrays.asList(
            Arrays.asList("张三", 25, "zhangsan@example.com", "技术部"),
            Arrays.asList("李四", 30, "lisi@example.com", "市场部"),
            Arrays.asList("王五", 28, "wangwu@example.com", "财务部"),
            Arrays.asList("赵六", 32, "zhaoliu@example.com", "人事部")
        );

        // 导出
        File testFile = tempDir.resolve("test_roundtrip.xlsx").toFile();
        ExcelUtil.export(testFile, headers, originalData);

        // 导入
        List<List<Object>> importedData = ExcelUtil.importData(testFile);

        // 验证数据一致性（Excel 读取的数据都是 String 类型）
        assertNotNull(importedData);
        assertEquals(originalData.size(), importedData.size());

        for (int i = 0; i < originalData.size(); i++) {
            List<Object> originalRow = originalData.get(i);
            List<Object> importedRow = importedData.get(i);
            assertEquals(originalRow.size(), importedRow.size());

            for (int j = 0; j < originalRow.size(); j++) {
                // 将原始数据转换为字符串进行比较
                String expected = String.valueOf(originalRow.get(j));
                String actual = String.valueOf(importedRow.get(j));
                assertEquals(expected, actual);
            }
        }
    }

    // ==================== 数据类型测试 ====================

    @Test
    @DisplayName("导出不同数据类型")
    void testExportDifferentDataTypes() throws IOException {
        List<String> headers = Arrays.asList("字符串", "整数", "小数", "布尔值", "日期字符串");
        List<List<Object>> data = List.of(
            Arrays.asList("测试", 100, 3.14, true, "2024-01-15")
        );

        File outputFile = tempDir.resolve("test_types.xlsx").toFile();
        ExcelUtil.export(outputFile, headers, data);

        assertTrue(outputFile.exists());

        // 导入并验证
        List<List<Object>> importedData = ExcelUtil.importData(outputFile);
        assertNotNull(importedData);
        assertEquals(1, importedData.size());
    }
}
