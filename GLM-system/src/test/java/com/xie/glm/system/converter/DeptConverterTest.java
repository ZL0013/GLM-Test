package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysDept;
import com.xie.glm.system.dto.DeptDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 部门对象转换器测试类
 *
 * <p>测试 {@link DeptConverter} 的基本功能，包括：
 * <ul>
 *   <li>Entity → DTO 转换</li>
 *   <li>DTO → Entity 转换</li>
 *   <li>Entity List → DTO List 转换</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("部门对象转换器测试")
class DeptConverterTest {

    private DeptConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DeptConverterImpl();
    }

    @Nested
    @DisplayName("Entity → DTO 转换测试")
    class EntityToDtoTests {

        @Test
        @DisplayName("应该将 Entity 转换为 DTO")
        void shouldConvertEntityToDTO() {
            // Given
            SysDept entity = new SysDept();
            entity.setDeptId(1L);
            entity.setDeptName("研发部");
            entity.setParentId(0L);
            entity.setOrderNum(1);
            entity.setLeader("张三");
            entity.setPhone("13800138000");
            entity.setEmail("rd@example.com");
            entity.setStatus("0");
            entity.setAncestors("0");
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());

            // When
            DeptDTO dto = converter.toDto(entity);

            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getDeptId()).isEqualTo(1L);
            assertThat(dto.getDeptName()).isEqualTo("研发部");
            assertThat(dto.getParentId()).isEqualTo(0L);
            assertThat(dto.getOrderNum()).isEqualTo(1);
            assertThat(dto.getLeader()).isEqualTo("张三");
            assertThat(dto.getPhone()).isEqualTo("13800138000");
            assertThat(dto.getEmail()).isEqualTo("rd@example.com");
            assertThat(dto.getStatus()).isEqualTo("0");
            assertThat(dto.getAncestors()).isEqualTo("0");
        }

        @Test
        @DisplayName("应该处理 null Entity")
        void shouldHandleNullEntity() {
            // When
            DeptDTO dto = converter.toDto(null);

            // Then
            assertThat(dto).isNull();
        }
    }

    @Nested
    @DisplayName("DTO → Entity 转换测试")
    class DtoToEntityTests {

        @Test
        @DisplayName("应该将 DTO 转换为 Entity")
        void shouldConvertDTOToEntity() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            DeptDTO dto = DeptDTO.builder()
                    .deptId(1L)
                    .deptName("研发部")
                    .parentId(0L)
                    .orderNum(1)
                    .leader("张三")
                    .phone("13800138000")
                    .email("rd@example.com")
                    .status("0")
                    .ancestors("0")
                    .createTime(now)
                    .updateTime(now)
                    .build();

            // When
            SysDept entity = converter.toEntity(dto);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDeptId()).isEqualTo(1L);
            assertThat(entity.getDeptName()).isEqualTo("研发部");
            assertThat(entity.getParentId()).isEqualTo(0L);
            assertThat(entity.getOrderNum()).isEqualTo(1);
            assertThat(entity.getLeader()).isEqualTo("张三");
            assertThat(entity.getPhone()).isEqualTo("13800138000");
            assertThat(entity.getEmail()).isEqualTo("rd@example.com");
            assertThat(entity.getStatus()).isEqualTo("0");
            assertThat(entity.getAncestors()).isEqualTo("0");
        }

        @Test
        @DisplayName("应该处理 null DTO")
        void shouldHandleNullDTO() {
            // When
            SysDept entity = converter.toEntity(null);

            // Then
            assertThat(entity).isNull();
        }

        @Test
        @DisplayName("应该忽略 null 值字段")
        void shouldIgnoreNullFields() {
            // Given
            DeptDTO dto = DeptDTO.builder()
                    .deptName("研发部")
                    .status("0")
                    .build();

            // When
            SysDept entity = converter.toEntity(dto);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDeptName()).isEqualTo("研发部");
            assertThat(entity.getStatus()).isEqualTo("0");
            assertThat(entity.getDeptId()).isNull();
            assertThat(entity.getParentId()).isNull();
        }
    }

    @Nested
    @DisplayName("Entity List → DTO List 转换测试")
    class EntityListToDtoListTests {

        @Test
        @DisplayName("应该将 Entity List 转换为 DTO List")
        void shouldConvertEntityListToDTOList() {
            // Given
            SysDept entity1 = new SysDept();
            entity1.setDeptId(1L);
            entity1.setDeptName("研发部");
            entity1.setStatus("0");

            SysDept entity2 = new SysDept();
            entity2.setDeptId(2L);
            entity2.setDeptName("市场部");
            entity2.setStatus("0");

            List<SysDept> entities = Arrays.asList(entity1, entity2);

            // When
            List<DeptDTO> dtoList = converter.toDtoList(entities);

            // Then
            assertThat(dtoList).hasSize(2);
            assertThat(dtoList.get(0).getDeptId()).isEqualTo(1L);
            assertThat(dtoList.get(0).getDeptName()).isEqualTo("研发部");
            assertThat(dtoList.get(1).getDeptId()).isEqualTo(2L);
            assertThat(dtoList.get(1).getDeptName()).isEqualTo("市场部");
        }

        @Test
        @DisplayName("应该处理空列表")
        void shouldHandleEmptyList() {
            // When
            List<DeptDTO> dtoList = converter.toDtoList(Arrays.asList());

            // Then
            assertThat(dtoList).isEmpty();
        }

        @Test
        @DisplayName("应该处理 null 列表")
        void shouldHandleNullList() {
            // When
            List<DeptDTO> dtoList = converter.toDtoList(null);

            // Then
            assertThat(dtoList).isNull();
        }
    }
}
