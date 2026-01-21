package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.DeptVO;
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
 * 部门视图对象转换器测试类
 *
 * <p>测试 {@link DeptVoConverter} 的基本功能，包括：
 * <ul>
 *   <li>DTO → VO 转换</li>
 *   <li>DTO List → VO List 转换</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("部门视图对象转换器测试")
class DeptVoConverterTest {

    private DeptVoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DeptVoConverterImpl();
    }

    @Nested
    @DisplayName("DTO → VO 转换测试")
    class DtoToVoTests {

        @Test
        @DisplayName("应该将 DTO 转换为 VO")
        void shouldConvertDTOToVO() {
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
            DeptVO vo = converter.toVo(dto);

            // Then
            assertThat(vo).isNotNull();
            assertThat(vo.getDeptId()).isEqualTo(1L);
            assertThat(vo.getDeptName()).isEqualTo("研发部");
            assertThat(vo.getParentId()).isEqualTo(0L);
            assertThat(vo.getOrderNum()).isEqualTo(1);
            assertThat(vo.getLeader()).isEqualTo("张三");
            assertThat(vo.getPhone()).isEqualTo("13800138000");
            assertThat(vo.getEmail()).isEqualTo("rd@example.com");
            assertThat(vo.getStatus()).isEqualTo("0");
            assertThat(vo.getAncestors()).isEqualTo("0");
            assertThat(vo.getChildren()).isNull();
        }

        @Test
        @DisplayName("应该处理 null DTO")
        void shouldHandleNullDTO() {
            // When
            DeptVO vo = converter.toVo(null);

            // Then
            assertThat(vo).isNull();
        }
    }

    @Nested
    @DisplayName("DTO List → VO List 转换测试")
    class DtoListToVoListTests {

        @Test
        @DisplayName("应该将 DTO List 转换为 VO List")
        void shouldConvertDTOListToVOList() {
            // Given
            DeptDTO dto1 = DeptDTO.builder()
                    .deptId(1L)
                    .deptName("研发部")
                    .status("0")
                    .build();

            DeptDTO dto2 = DeptDTO.builder()
                    .deptId(2L)
                    .deptName("市场部")
                    .status("0")
                    .build();

            List<DeptDTO> dtoList = Arrays.asList(dto1, dto2);

            // When
            List<DeptVO> voList = converter.toVoList(dtoList);

            // Then
            assertThat(voList).hasSize(2);
            assertThat(voList.get(0).getDeptId()).isEqualTo(1L);
            assertThat(voList.get(0).getDeptName()).isEqualTo("研发部");
            assertThat(voList.get(1).getDeptId()).isEqualTo(2L);
            assertThat(voList.get(1).getDeptName()).isEqualTo("市场部");
        }

        @Test
        @DisplayName("应该处理空列表")
        void shouldHandleEmptyList() {
            // When
            List<DeptVO> voList = converter.toVoList(Arrays.asList());

            // Then
            assertThat(voList).isEmpty();
        }

        @Test
        @DisplayName("应该处理 null 列表")
        void shouldHandleNullList() {
            // When
            List<DeptVO> voList = converter.toVoList(null);

            // Then
            assertThat(voList).isNull();
        }
    }

    @Nested
    @DisplayName("树形结构转换测试")
    class TreeStructureTests {

        @Test
        @DisplayName("转换后的 VO 应该可以构建树形结构")
        void convertedVOShouldBuildTreeStructure() {
            // Given - 准备三个部门 DTO
            DeptDTO rootDTO = DeptDTO.builder()
                    .deptId(1L)
                    .deptName("公司")
                    .parentId(0L)
                    .build();

            DeptDTO childDTO1 = DeptDTO.builder()
                    .deptId(2L)
                    .deptName("研发部")
                    .parentId(1L)
                    .build();

            DeptDTO childDTO2 = DeptDTO.builder()
                    .deptId(3L)
                    .deptName("市场部")
                    .parentId(1L)
                    .build();

            List<DeptDTO> dtoList = Arrays.asList(rootDTO, childDTO1, childDTO2);

            // When - 转换为 VO
            List<DeptVO> voList = converter.toVoList(dtoList);

            // Then - 手动构建树形结构
            DeptVO root = voList.get(0);
            DeptVO child1 = voList.get(1);
            DeptVO child2 = voList.get(2);

            root.setChildren(Arrays.asList(child1, child2));

            // 验证树形结构
            assertThat(root.getChildren()).hasSize(2);
            assertThat(root.getChildren().get(0).getDeptName()).isEqualTo("研发部");
            assertThat(root.getChildren().get(1).getDeptName()).isEqualTo("市场部");
        }
    }
}
