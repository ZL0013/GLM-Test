package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysOperLog;
import com.xie.glm.system.dto.OperLogDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OperLogConverter 测试类
 *
 * <p>测试操作日志 Entity 和 DTO 之间的转换。
 *
 * @author xie
 */
@DisplayName("操作日志转换器测试")
class OperLogConverterTest {

    private final OperLogConverter operLogConverter = new OperLogConverterImpl();

    @Nested
    @DisplayName("Entity → DTO 转换测试")
    class EntityToDtoTest {

        @Test
        @DisplayName("应该成功将 Entity 转换为 DTO")
        void testToDtoSuccess() {
            // Arrange
            SysOperLog entity = SysOperLog.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .method("com.xie.glm.admin.controller.UserController.add()")
                    .requestMethod("POST")
                    .operatorType(1)
                    .operName("admin")
                    .deptName("技术部")
                    .operUrl("/api/system/users")
                    .operIp("127.0.0.1")
                    .operLocation("本地")
                    .operParam("{\"userName\":\"test\"}")
                    .jsonResult("{\"code\":0}")
                    .status(0)
                    .operTime(LocalDateTime.now())
                    .costTime(100L)
                    .build();

            // Act
            OperLogDTO dto = operLogConverter.toDto(entity);

            // Assert
            assertNotNull(dto);
            assertEquals(entity.getOperId(), dto.getOperId());
            assertEquals(entity.getTitle(), dto.getTitle());
            assertEquals(entity.getBusinessType(), dto.getBusinessType());
            assertEquals(entity.getMethod(), dto.getMethod());
            assertEquals(entity.getRequestMethod(), dto.getRequestMethod());
            assertEquals(entity.getOperatorType(), dto.getOperatorType());
            assertEquals(entity.getOperName(), dto.getOperName());
            assertEquals(entity.getDeptName(), dto.getDeptName());
            assertEquals(entity.getOperUrl(), dto.getOperUrl());
            assertEquals(entity.getOperIp(), dto.getOperIp());
            assertEquals(entity.getOperLocation(), dto.getOperLocation());
            assertEquals(entity.getOperParam(), dto.getOperParam());
            assertEquals(entity.getJsonResult(), dto.getJsonResult());
            assertEquals(entity.getStatus(), dto.getStatus());
            assertEquals(entity.getOperTime(), dto.getOperTime());
            assertEquals(entity.getCostTime(), dto.getCostTime());
        }

        @Test
        @DisplayName("转换 null Entity 应该返回 null DTO")
        void testToDtoNullEntity() {
            // Act
            OperLogDTO dto = operLogConverter.toDto(null);

            // Assert
            assertNull(dto);
        }
    }

    @Nested
    @DisplayName("DTO → Entity 转换测试")
    class DtoToEntityTest {

        @Test
        @DisplayName("应该成功将 DTO 转换为 Entity")
        void testToEntitySuccess() {
            // Arrange
            OperLogDTO dto = OperLogDTO.builder()
                    .operId(1L)
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

            // Act
            SysOperLog entity = operLogConverter.toEntity(dto);

            // Assert
            assertNotNull(entity);
            assertEquals(dto.getOperId(), entity.getOperId());
            assertEquals(dto.getTitle(), entity.getTitle());
            assertEquals(dto.getBusinessType(), entity.getBusinessType());
            assertEquals(dto.getMethod(), entity.getMethod());
            assertEquals(dto.getRequestMethod(), entity.getRequestMethod());
            assertEquals(dto.getOperatorType(), entity.getOperatorType());
            assertEquals(dto.getOperName(), entity.getOperName());
            assertEquals(dto.getDeptName(), entity.getDeptName());
            assertEquals(dto.getOperUrl(), entity.getOperUrl());
            assertEquals(dto.getOperIp(), entity.getOperIp());
            assertEquals(dto.getOperLocation(), entity.getOperLocation());
            assertEquals(dto.getOperParam(), entity.getOperParam());
            assertEquals(dto.getJsonResult(), entity.getJsonResult());
            assertEquals(dto.getStatus(), entity.getStatus());
            assertEquals(dto.getOperTime(), entity.getOperTime());
            assertEquals(dto.getCostTime(), entity.getCostTime());
        }

        @Test
        @DisplayName("转换 null DTO 应该返回 null Entity")
        void testToEntityNullDto() {
            // Act
            SysOperLog entity = operLogConverter.toEntity(null);

            // Assert
            assertNull(entity);
        }
    }

    @Nested
    @DisplayName("Entity List → DTO List 转换测试")
    class EntityListToDtoListTest {

        @Test
        @DisplayName("应该成功将 Entity List 转换为 DTO List")
        void testToDtoListSuccess() {
            // Arrange
            SysOperLog entity1 = SysOperLog.builder()
                    .operId(1L)
                    .title("用户管理")
                    .businessType(1)
                    .operName("admin")
                    .build();

            SysOperLog entity2 = SysOperLog.builder()
                    .operId(2L)
                    .title("角色管理")
                    .businessType(2)
                    .operName("admin")
                    .build();

            List<SysOperLog> entities = Arrays.asList(entity1, entity2);

            // Act
            List<OperLogDTO> dtoList = operLogConverter.toDtoList(entities);

            // Assert
            assertNotNull(dtoList);
            assertEquals(2, dtoList.size());
            assertEquals(entity1.getOperId(), dtoList.get(0).getOperId());
            assertEquals(entity1.getTitle(), dtoList.get(0).getTitle());
            assertEquals(entity2.getOperId(), dtoList.get(1).getOperId());
            assertEquals(entity2.getTitle(), dtoList.get(1).getTitle());
        }

        @Test
        @DisplayName("转换空列表应该返回空列表")
        void testToDtoListEmpty() {
            // Act
            List<OperLogDTO> dtoList = operLogConverter.toDtoList(Arrays.asList());

            // Assert
            assertNotNull(dtoList);
            assertTrue(dtoList.isEmpty());
        }

        @Test
        @DisplayName("转换 null 列表应该返回 null")
        void testToDtoListNull() {
            // Act
            List<OperLogDTO> dtoList = operLogConverter.toDtoList(null);

            // Assert
            assertNull(dtoList);
        }
    }

    @Nested
    @DisplayName("业务类型代码转换测试")
    class BusinessTypeConversionTest {

        @Test
        @DisplayName("应该正确转换业务类型代码为中文名称")
        void testBusinessTypeConversion() {
            // Arrange
            SysOperLog entity = SysOperLog.builder()
                    .businessType(1)
                    .build();

            // Act
            OperLogDTO dto = operLogConverter.toDto(entity);

            // Assert
            assertEquals("新增", dto.getBusinessTypeName());
        }
    }

    @Nested
    @DisplayName("操作人类别代码转换测试")
    class OperatorTypeConversionTest {

        @Test
        @DisplayName("应该正确转换操作人类别代码为中文名称")
        void testOperatorTypeConversion() {
            // Arrange
            SysOperLog entity = SysOperLog.builder()
                    .operatorType(1)
                    .build();

            // Act
            OperLogDTO dto = operLogConverter.toDto(entity);

            // Assert
            assertEquals("后台用户", dto.getOperatorTypeName());
        }
    }

    @Nested
    @DisplayName("状态代码转换测试")
    class StatusConversionTest {

        @Test
        @DisplayName("应该正确转换状态代码为中文名称")
        void testStatusConversion() {
            // Arrange
            SysOperLog entity = SysOperLog.builder()
                    .status(0)
                    .build();

            // Act
            OperLogDTO dto = operLogConverter.toDto(entity);

            // Assert
            assertEquals("成功", dto.getStatusName());
        }
    }
}
