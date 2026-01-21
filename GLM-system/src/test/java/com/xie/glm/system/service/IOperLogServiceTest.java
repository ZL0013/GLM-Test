package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.OperLogDTO;
import com.xie.glm.system.dto.query.LogQueryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * IOperLogService 接口测试
 *
 * <p>此测试定义了操作日志服务的契约规范，确保所有实现类都遵循相同的行为。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("操作日志服务接口测试")
class IOperLogServiceTest {

    @Mock
    private IOperLogService operLogService;

    @Nested
    @DisplayName("分页查询操作日志列表")
    class ListOperLogsTest {

        @Test
        @DisplayName("应该成功分页查询操作日志列表")
        void testListOperLogsSuccess() {
            // Arrange
            LogQueryDTO query = new LogQueryDTO();
            query.setPageNum(1);
            query.setPageSize(10);

            OperLogDTO dto1 = OperLogDTO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .operName("admin")
                    .build();

            OperLogDTO dto2 = OperLogDTO.builder()
                    .operId(2L)
                    .title("角色管理")
                    .businessType(2)
                    .operName("admin")
                    .build();

            PageResult<OperLogDTO> expected = new PageResult<>(Arrays.asList(dto1, dto2), 2L);

            when(operLogService.listOperLogs(any(LogQueryDTO.class))).thenReturn(expected);

            // Act
            PageResult<OperLogDTO> result = operLogService.listOperLogs(query);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getRecords().size());
            assertEquals(2L, result.getTotal());
            assertEquals("用户管理", result.getRecords().get(0).getTitle());
            assertEquals("角色管理", result.getRecords().get(1).getTitle());

            verify(operLogService, times(1)).listOperLogs(query);
        }

        @Test
        @DisplayName("查询结果为空时应该返回空分页结果")
        void testListOperLogsEmpty() {
            // Arrange
            LogQueryDTO query = new LogQueryDTO();
            query.setPageNum(1);
            query.setPageSize(10);

            PageResult<OperLogDTO> expected = new PageResult<>(Collections.emptyList(), 0L);

            when(operLogService.listOperLogs(any(LogQueryDTO.class))).thenReturn(expected);

            // Act
            PageResult<OperLogDTO> result = operLogService.listOperLogs(query);

            // Assert
            assertNotNull(result);
            assertTrue(result.getRecords().isEmpty());
            assertEquals(0L, result.getTotal());

            verify(operLogService, times(1)).listOperLogs(query);
        }
    }

    @Nested
    @DisplayName("根据 ID 查询操作日志详情")
    class GetOperLogByIdTest {

        @Test
        @DisplayName("应该成功查询操作日志详情")
        void testGetOperLogByIdSuccess() {
            // Arrange
            Long operId = 1L;
            OperLogDTO expected = OperLogDTO.builder()
                    .operId(operId)
                    .title("用户管理")
                    .businessType(1)
                    .businessTypeName("新增")
                    .method("com.xie.glm.admin.controller.UserController.add()")
                    .requestMethod("POST")
                    .operatorType(1)
                    .operatorTypeName("后台用户")
                    .operName("admin")
                    .deptName("技术部")
                    .operUrl("/api/system/users")
                    .operIp("127.0.0.1")
                    .operLocation("本地")
                    .operParam("{\"userName\":\"test\"}")
                    .jsonResult("{\"code\":0}")
                    .status(0)
                    .statusName("成功")
                    .operTime(LocalDateTime.now())
                    .costTime(100L)
                    .build();

            when(operLogService.getOperLogById(operId)).thenReturn(expected);

            // Act
            OperLogDTO result = operLogService.getOperLogById(operId);

            // Assert
            assertNotNull(result);
            assertEquals(operId, result.getOperId());
            assertEquals("用户管理", result.getTitle());
            assertEquals(1, result.getBusinessType());
            assertEquals("新增", result.getBusinessTypeName());
            assertEquals("成功", result.getStatusName());

            verify(operLogService, times(1)).getOperLogById(operId);
        }

        @Test
        @DisplayName("查询不存在的操作日志应该返回 null")
        void testGetOperLogByIdNotFound() {
            // Arrange
            Long operId = 999L;
            when(operLogService.getOperLogById(operId)).thenReturn(null);

            // Act
            OperLogDTO result = operLogService.getOperLogById(operId);

            // Assert
            assertNull(result);

            verify(operLogService, times(1)).getOperLogById(operId);
        }
    }

    @Nested
    @DisplayName("保存操作日志")
    class SaveOperLogTest {

        @Test
        @DisplayName("应该成功保存操作日志")
        void testSaveOperLogSuccess() {
            // Arrange
            OperLogDTO dto = OperLogDTO.builder()
                    .title("用户管理")
                    .businessType(1)
                    .operName("admin")
                    .build();

            when(operLogService.saveOperLog(any(OperLogDTO.class))).thenReturn(1L);

            // Act
            Long result = operLogService.saveOperLog(dto);

            // Assert
            assertEquals(1L, result);

            verify(operLogService, times(1)).saveOperLog(dto);
        }
    }

    @Nested
    @DisplayName("删除操作日志")
    class DeleteOperLogTest {

        @Test
        @DisplayName("应该成功删除单条操作日志")
        void testDeleteOperLogSuccess() {
            // Arrange
            Long operId = 1L;
            doNothing().when(operLogService).deleteOperLog(operId);

            // Act
            operLogService.deleteOperLog(operId);

            // Assert
            verify(operLogService, times(1)).deleteOperLog(operId);
        }

        @Test
        @DisplayName("应该成功批量删除操作日志")
        void testDeleteOperLogsSuccess() {
            // Arrange
            Long[] operIds = {1L, 2L, 3L};
            doNothing().when(operLogService).deleteOperLogs(operIds);

            // Act
            operLogService.deleteOperLogs(operIds);

            // Assert
            verify(operLogService, times(1)).deleteOperLogs(operIds);
        }
    }

    @Nested
    @DisplayName("清空操作日志")
    class CleanOperLogsTest {

        @Test
        @DisplayName("应该成功清空所有操作日志")
        void testCleanOperLogsSuccess() {
            // Arrange
            doNothing().when(operLogService).cleanOperLogs();

            // Act
            operLogService.cleanOperLogs();

            // Assert
            verify(operLogService, times(1)).cleanOperLogs();
        }
    }
}
