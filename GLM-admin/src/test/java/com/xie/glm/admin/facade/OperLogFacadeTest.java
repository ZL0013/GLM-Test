package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.OperLogVoConverter;
import com.xie.glm.admin.vo.OperLogVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.OperLogDTO;
import com.xie.glm.system.dto.query.LogQueryDTO;
import com.xie.glm.system.service.IOperLogService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * OperLogFacade 测试类
 *
 * <p>测试操作日志门面类的业务逻辑。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("操作日志门面测试")
class OperLogFacadeTest {

    @Mock
    private IOperLogService operLogService;

    @Mock
    private OperLogVoConverter voConverter;

    @InjectMocks
    private OperLogFacade operLogFacade;

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
                    .businessTypeName("新增")
                    .status(0)
                    .statusName("成功")
                    .operName("admin")
                    .build();

            OperLogDTO dto2 = OperLogDTO.builder()
                    .operId(2L)
                    .title("角色管理")
                    .businessType(2)
                    .businessTypeName("修改")
                    .status(0)
                    .statusName("成功")
                    .operName("admin")
                    .build();

            PageResult<OperLogDTO> dtoPage = new PageResult<>(Arrays.asList(dto1, dto2), 2L);

            OperLogVO vo1 = OperLogVO.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .businessTypeName("新增")
                    .businessTypeText("新增")
                    .status(0)
                    .statusName("成功")
                    .statusText("成功")
                    .operName("admin")
                    .build();

            OperLogVO vo2 = OperLogVO.builder()
                    .operId(2L)
                    .title("角色管理")
                    .businessType(2)
                    .businessTypeName("修改")
                    .businessTypeText("修改")
                    .status(0)
                    .statusName("成功")
                    .statusText("成功")
                    .operName("admin")
                    .build();

            when(operLogService.listOperLogs(any(LogQueryDTO.class))).thenReturn(dtoPage);
            when(voConverter.toVoList(anyList())).thenReturn(Arrays.asList(vo1, vo2));

            // Act
            PageResult<OperLogVO> result = operLogFacade.listOperLogs(query);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getRecords().size());
            assertEquals(2L, result.getTotal());
            assertEquals("用户管理", result.getRecords().get(0).getTitle());
            assertEquals("角色管理", result.getRecords().get(1).getTitle());
            assertEquals("新增", result.getRecords().get(0).getBusinessTypeText());
            assertEquals("修改", result.getRecords().get(1).getBusinessTypeText());
            assertEquals("成功", result.getRecords().get(0).getStatusText());
            assertEquals("成功", result.getRecords().get(1).getStatusText());

            verify(operLogService, times(1)).listOperLogs(query);
            verify(voConverter, times(1)).toVoList(anyList());
        }

        @Test
        @DisplayName("查询结果为空时应该返回空分页结果")
        void testListOperLogsEmpty() {
            // Arrange
            LogQueryDTO query = new LogQueryDTO();
            query.setPageNum(1);
            query.setPageSize(10);

            PageResult<OperLogDTO> dtoPage = new PageResult<>(Collections.emptyList(), 0L);

            when(operLogService.listOperLogs(any(LogQueryDTO.class))).thenReturn(dtoPage);
            when(voConverter.toVoList(anyList())).thenReturn(Collections.emptyList());

            // Act
            PageResult<OperLogVO> result = operLogFacade.listOperLogs(query);

            // Assert
            assertNotNull(result);
            assertTrue(result.getRecords().isEmpty());
            assertEquals(0L, result.getTotal());

            verify(operLogService, times(1)).listOperLogs(query);
            verify(voConverter, times(1)).toVoList(anyList());
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

            OperLogDTO dto = OperLogDTO.builder()
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

            OperLogVO vo = OperLogVO.builder()
                    .operId(operId)
                    .title("用户管理")
                    .businessType(1)
                    .businessTypeName("新增")
                    .businessTypeText("新增")
                    .status(0)
                    .statusName("成功")
                    .statusText("成功")
                    .operName("admin")
                    .build();

            when(operLogService.getOperLogById(operId)).thenReturn(dto);
            when(voConverter.toVo(dto)).thenReturn(vo);

            // Act
            OperLogVO result = operLogFacade.getOperLogById(operId);

            // Assert
            assertNotNull(result);
            assertEquals(operId, result.getOperId());
            assertEquals("用户管理", result.getTitle());
            assertEquals("新增", result.getBusinessTypeText());
            assertEquals("成功", result.getStatusText());

            verify(operLogService, times(1)).getOperLogById(operId);
            verify(voConverter, times(1)).toVo(dto);
        }

        @Test
        @DisplayName("查询不存在的操作日志应该返回 null")
        void testGetOperLogByIdNotFound() {
            // Arrange
            Long operId = 999L;
            when(operLogService.getOperLogById(operId)).thenReturn(null);

            // Act
            OperLogVO result = operLogFacade.getOperLogById(operId);

            // Assert
            assertNull(result);

            verify(operLogService, times(1)).getOperLogById(operId);
            verify(voConverter, never()).toVo(any());
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
            operLogFacade.deleteOperLog(operId);

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
            operLogFacade.deleteOperLogs(operIds);

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
            operLogFacade.cleanOperLogs();

            // Assert
            verify(operLogService, times(1)).cleanOperLogs();
        }
    }
}
