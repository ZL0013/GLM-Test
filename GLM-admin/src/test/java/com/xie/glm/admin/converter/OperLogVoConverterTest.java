package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.OperLogVO;
import com.xie.glm.system.dto.OperLogDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OperLogVoConverter 测试类
 *
 * <p>测试操作日志 DTO 和 VO 之间的转换。
 *
 * @author xie
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("操作日志 VO 转换器测试")
class OperLogVoConverterTest {

    @Autowired
    private OperLogVoConverter operLogVoConverter;

    @Nested
    @DisplayName("DTO → VO 转换测试")
    class DtoToVoTest {

        @Test
        @DisplayName("应该成功将 DTO 转换为 VO")
        void testToVoSuccess() {
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
            OperLogVO vo = operLogVoConverter.toVo(dto);

            // Assert
            assertNotNull(vo);
            assertEquals(dto.getOperId(), vo.getOperId());
            assertEquals(dto.getTitle(), vo.getTitle());
            assertEquals(dto.getBusinessType(), vo.getBusinessType());
            assertEquals(dto.getBusinessTypeName(), vo.getBusinessTypeName());
            assertEquals(dto.getMethod(), vo.getMethod());
            assertEquals(dto.getRequestMethod(), vo.getRequestMethod());
            assertEquals(dto.getOperatorType(), vo.getOperatorType());
            assertEquals(dto.getOperatorTypeName(), vo.getOperatorTypeName());
            assertEquals(dto.getOperName(), vo.getOperName());
            assertEquals(dto.getDeptName(), vo.getDeptName());
            assertEquals(dto.getOperUrl(), vo.getOperUrl());
            assertEquals(dto.getOperIp(), vo.getOperIp());
            assertEquals(dto.getOperLocation(), vo.getOperLocation());
            assertEquals(dto.getOperParam(), vo.getOperParam());
            assertEquals(dto.getJsonResult(), vo.getJsonResult());
            assertEquals(dto.getStatus(), vo.getStatus());
            assertEquals(dto.getStatusName(), vo.getStatusName());
            assertEquals(dto.getOperTime(), vo.getOperTime());
            assertEquals(dto.getCostTime(), vo.getCostTime());
        }

        @Test
        @DisplayName("转换 null DTO 应该返回 null VO")
        void testToVoNullDto() {
            // Act
            OperLogVO vo = operLogVoConverter.toVo(null);

            // Assert
            assertNull(vo);
        }
    }

    @Nested
    @DisplayName("DTO List → VO List 转换测试")
    class DtoListToVoListTest {

        @Test
        @DisplayName("应该成功将 DTO List 转换为 VO List")
        void testToVoListSuccess() {
            // Arrange
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

            List<OperLogDTO> dtoList = Arrays.asList(dto1, dto2);

            // Act
            List<OperLogVO> voList = operLogVoConverter.toVoList(dtoList);

            // Assert
            assertNotNull(voList);
            assertEquals(2, voList.size());
            assertEquals(dto1.getOperId(), voList.get(0).getOperId());
            assertEquals(dto1.getTitle(), voList.get(0).getTitle());
            assertEquals(dto2.getOperId(), voList.get(1).getOperId());
            assertEquals(dto2.getTitle(), voList.get(1).getTitle());
        }

        @Test
        @DisplayName("转换空列表应该返回空列表")
        void testToVoListEmpty() {
            // Act
            List<OperLogVO> voList = operLogVoConverter.toVoList(Arrays.asList());

            // Assert
            assertNotNull(voList);
            assertTrue(voList.isEmpty());
        }

        @Test
        @DisplayName("转换 null 列表应该返回 null")
        void testToVoListNull() {
            // Act
            List<OperLogVO> voList = operLogVoConverter.toVoList(null);

            // Assert
            assertNull(voList);
        }
    }

    @Nested
    @DisplayName("双向转换一致性测试")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("DTO → VO → DTO 应该保持数据一致（VO 继承 DTO）")
        void testDtoToVoToDtoConsistency() {
            // Arrange
            OperLogDTO originalDto = OperLogDTO.builder()
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
            OperLogVO vo = operLogVoConverter.toVo(originalDto);

            // Assert - VO 继承 DTO，所以所有字段都应该一致
            assertEquals(originalDto.getOperId(), vo.getOperId());
            assertEquals(originalDto.getTitle(), vo.getTitle());
            assertEquals(originalDto.getBusinessType(), vo.getBusinessType());
            assertEquals(originalDto.getBusinessTypeName(), vo.getBusinessTypeName());
            assertEquals(originalDto.getMethod(), vo.getMethod());
            assertEquals(originalDto.getRequestMethod(), vo.getRequestMethod());
            assertEquals(originalDto.getOperatorType(), vo.getOperatorType());
            assertEquals(originalDto.getOperatorTypeName(), vo.getOperatorTypeName());
            assertEquals(originalDto.getOperName(), vo.getOperName());
            assertEquals(originalDto.getDeptName(), vo.getDeptName());
            assertEquals(originalDto.getOperUrl(), vo.getOperUrl());
            assertEquals(originalDto.getOperIp(), vo.getOperIp());
            assertEquals(originalDto.getOperLocation(), vo.getOperLocation());
            assertEquals(originalDto.getOperParam(), vo.getOperParam());
            assertEquals(originalDto.getJsonResult(), vo.getJsonResult());
            assertEquals(originalDto.getStatus(), vo.getStatus());
            assertEquals(originalDto.getStatusName(), vo.getStatusName());
            assertEquals(originalDto.getOperTime(), vo.getOperTime());
            assertEquals(originalDto.getCostTime(), vo.getCostTime());
        }
    }
}
