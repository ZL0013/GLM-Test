package com.xie.glm.common.util;

import cn.idev.excel.FastExcel;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel 工具类
 *
 * <p>基于 FastExcel (cn.idev.excel:fastexcel) 实现，提供 Excel 导入导出功能。
 *
 * @author xie
 */
public final class ExcelUtil {

    /**
     * 私有构造函数，防止实例化
     */
    private ExcelUtil() {
        // 工具类，不允许实例化
    }

    // ==================== 导出 ====================

    /**
     * 导出数据到 Excel 文件
     *
     * @param file    输出文件
     * @param headers 表头列表
     * @param data    数据列表（每个 List<Object> 代表一行）
     * @throws ServiceException 如果 file 或 headers 为 null，或导出失败
     */
    public static void export(File file, List<String> headers, List<List<Object>> data) {
        validateFile(file);
        validateHeaders(headers);
        if (data == null) {
            data = new ArrayList<>();
        }

        try {
            // 创建动态表头
            List<List<String>> headList = new ArrayList<>();
            for (String header : headers) {
                headList.add(List.of(header));
            }

            // 写入 Excel
            FastExcel.write(file)
                .head(headList)
                .sheet()
                .doWrite(data);
        } catch (Exception e) {
            throw new ServiceException(BusinessStatus.EXCEL_EXPORT_FAILED, e);
        }
    }

    // ==================== 导入 ====================

    /**
     * 从 Excel 文件导入数据
     *
     * @param file Excel 文件
     * @return 数据列表（每个 List<Object> 代表一行）
     * @throws ServiceException 如果 file 为 null、文件不存在或导入失败
     */
    public static List<List<Object>> importData(File file) {
        validateFileExists(file);
        return importData(file, 0);
    }

    /**
     * 从 Excel 文件导入数据（指定 sheet）
     *
     * @param file    Excel 文件
     * @param sheetNo sheet 编号（从 0 开始）
     * @return 数据列表（每个 List<Object> 代表一行）
     * @throws ServiceException 如果 file 为 null、文件不存在或导入失败
     */
    public static List<List<Object>> importData(File file, int sheetNo) {
        validateFileExists(file);
        return importData(file, sheetNo, new ArrayList<>());
    }

    /**
     * 内部方法：从 Excel 文件导入数据
     *
     * @param file    Excel 文件
     * @param sheetNo sheet 编号
     * @param dataList 数据收集列表
     * @return 数据列表
     */
    private static List<List<Object>> importData(File file, int sheetNo, List<List<Object>> dataList) {
        try {
            FastExcel.read(new FileInputStream(file))
                .sheet(sheetNo)
                .registerReadListener(createDataCollectorListener(dataList))
                .doRead();
        } catch (IOException e) {
            throw new ServiceException(BusinessStatus.EXCEL_IMPORT_FAILED, e);
        }
        return dataList;
    }

    /**
     * 创建数据收集监听器
     *
     * @param dataList 数据收集列表
     * @return ReadListener
     */
    private static ReadListener<Map<Integer, String>> createDataCollectorListener(List<List<Object>> dataList) {
        return new ReadListener<Map<Integer, String>>() {
            @Override
            public void invoke(Map<Integer, String> data, AnalysisContext context) {
                // 将 Map<Integer, String> 转换为 List<Object>
                List<Object> row = new ArrayList<>(data.values());
                dataList.add(row);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                // 读取完成
            }
        };
    }

    // ==================== 私有校验方法 ====================

    /**
     * 校验文件参数不为空
     *
     * @param file 文件
     * @throws ServiceException 如果 file 为 null
     */
    private static void validateFile(File file) {
        if (file == null) {
            throw new ServiceException(BusinessStatus.FILE_PARAM_NULL);
        }
    }

    /**
     * 校验文件参数不为空且文件存在
     *
     * @param file 文件
     * @throws ServiceException 如果 file 为 null 或文件不存在
     */
    private static void validateFileExists(File file) {
        validateFile(file);
        if (!file.exists()) {
            throw new ServiceException(BusinessStatus.FILE_NOT_FOUND);
        }
    }

    /**
     * 校验表头参数不为空
     *
     * @param headers 表头
     * @throws ServiceException 如果 headers 为 null 或空
     */
    private static void validateHeaders(List<String> headers) {
        if (headers == null || headers.isEmpty()) {
            throw new ServiceException(BusinessStatus.PARAM_NULL);
        }
    }
}
