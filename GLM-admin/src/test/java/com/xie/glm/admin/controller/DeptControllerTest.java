package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.DeptFacade;
import com.xie.glm.admin.vo.DeptVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.dto.DeptDTO;
import com.xie.glm.system.dto.query.DeptQueryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 部门控制器测试
 *
 * <p>采用 TDD 方式开发，使用 Mockito 进行单元测试。
 *
 * <p>测试原则：
 * <ul>
 *   <li>Red-Green-Refactor 循环</li>
 *   <li>使用 @ParameterizedTest 进行参数化测试</li>
 *   <li>使用嵌套测试类 (@Nested) 组织相关测试</li>
 * </ul>
 *
 * <p>注意：Controller 直接返回数据对象，ResponseAdvice 会自动包装为 Result 格式。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("部门控制器测试")
class DeptControllerTest {

    @Mock
    private DeptFacade deptFacade;

    @InjectMocks
    private DeptController deptController;

    // ==================== 部门查询接口测试 ====================

    @Nested
    @DisplayName("部门查询接口测试")
    class QueryTests {

        @Test
        @DisplayName("分页查询部门列表 - 成功")
        void testListDepts_Success() {
            // Given
            DeptQueryDTO query = new DeptQueryDTO();
            query.setDeptName("研发部");

            PageResult<DeptVO> expectedPage = new PageResult<>(
                Arrays.asList(createMockDeptVO()),
                10L
            );
            when(deptFacade.listDepts(query)).thenReturn(expectedPage);

            // When
            PageResult<DeptVO> result = deptController.list(query);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotal()).isEqualTo(10L);
            assertThat(result.getRecords()).hasSize(1);

            verify(deptFacade).listDepts(query);
        }

        @Test
        @DisplayName("分页查询部门列表 - 空结果")
        void testListDepts_EmptyResult() {
            // Given
            DeptQueryDTO query = new DeptQueryDTO();
            PageResult<DeptVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(deptFacade.listDepts(query)).thenReturn(expectedPage);

            // When
            PageResult<DeptVO> result = deptController.list(query);

            // Then
            assertThat(result.getRecords()).isEmpty();
            assertThat(result.getTotal()).isEqualTo(0L);
        }

        @ParameterizedTest
        @MethodSource("provideQueryConditions")
        @DisplayName("分页查询部门列表 - 带条件查询")
        void testListDepts_WithConditions(String deptName, String status, String leader) {
            // Given
            DeptQueryDTO query = new DeptQueryDTO();
            query.setDeptName(deptName);
            query.setStatus(status);
            query.setLeader(leader);

            PageResult<DeptVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(deptFacade.listDepts(query)).thenReturn(expectedPage);

            // When
            PageResult<DeptVO> result = deptController.list(query);

            // Then
            assertThat(result).isNotNull();
            verify(deptFacade).listDepts(query);
        }

        private static List<Arguments> provideQueryConditions() {
            return List.of(
                Arguments.of("研发部", "0", "张三"),
                Arguments.of("市场部", "0", "李四"),
                Arguments.of("财务部", "1", "王五")
            );
        }

        @Test
        @DisplayName("构建部门树 - 成功")
        void testTree_Success() {
            // Given
            List<DeptVO> expectedTree = Arrays.asList(
                createMockDeptVOWithChildren()
            );
            when(deptFacade.buildDeptTree()).thenReturn(expectedTree);

            // When
            List<DeptVO> result = deptController.tree();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);

            verify(deptFacade).buildDeptTree();
        }

        @Test
        @DisplayName("查询所有部门列表 - 成功")
        void testListAll_Success() {
            // Given
            List<DeptVO> expectedList = Arrays.asList(
                createMockDeptVO(),
                createMockDeptVO()
            );
            when(deptFacade.listAllDepts()).thenReturn(expectedList);

            // When
            List<DeptVO> result = deptController.listAll();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);

            verify(deptFacade).listAllDepts();
        }
    }

    // ==================== 部门详情接口测试 ====================

    @Nested
    @DisplayName("部门详情接口测试")
    class DetailTests {

        @Test
        @DisplayName("查询部门详情 - 成功")
        void testGetDetail_Success() {
            // Given
            Long deptId = 1L;
            DeptVO expectedDept = createMockDeptVO();
            when(deptFacade.getDeptById(deptId)).thenReturn(expectedDept);

            // When
            DeptVO result = deptController.getDetail(deptId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getDeptId()).isEqualTo(deptId);

            verify(deptFacade).getDeptById(deptId);
        }

        @Test
        @DisplayName("查询部门详情 - 部门不存在")
        void testGetDetail_DeptNotFound() {
            // Given
            Long deptId = 999L;
            when(deptFacade.getDeptById(deptId))
                .thenThrow(new ServiceException("部门不存在"));

            // When & Then
            assertThatThrownBy(() -> deptController.getDetail(deptId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("部门不存在");
        }
    }

    // ==================== 部门创建接口测试 ====================

    @Nested
    @DisplayName("部门创建接口测试")
    class CreateTests {

        @Test
        @DisplayName("创建部门 - 成功")
        void testCreate_Success() {
            // Given
            DeptDTO dto = createMockDeptDTO();
            Long expectedDeptId = 1L;
            when(deptFacade.createDept(dto)).thenReturn(expectedDeptId);

            // When
            Long result = deptController.create(dto);

            // Then
            assertThat(result).isEqualTo(expectedDeptId);

            verify(deptFacade).createDept(dto);
        }

        @ParameterizedTest
        @CsvSource({
            "研发部, 0, 张三",
            "市场部, 1, 李四",
            "前端组, 2, 王五"
        })
        @DisplayName("创建部门 - 不同父部门")
        void testCreate_DifferentParents(String deptName, Long parentId, String leader) {
            // Given
            DeptDTO dto = createMockDeptDTO();
            dto.setDeptName(deptName);
            dto.setParentId(parentId);
            dto.setLeader(leader);

            Long expectedDeptId = 1L;
            when(deptFacade.createDept(dto)).thenReturn(expectedDeptId);

            // When
            Long result = deptController.create(dto);

            // Then
            assertThat(result).isEqualTo(expectedDeptId);

            verify(deptFacade).createDept(dto);
        }

        @Test
        @DisplayName("创建部门 - 部门名称重复")
        void testCreate_DeptNameDuplicate() {
            // Given
            DeptDTO dto = createMockDeptDTO();
            when(deptFacade.createDept(dto))
                .thenThrow(new ServiceException("部门名称已存在"));

            // When & Then
            assertThatThrownBy(() -> deptController.create(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("部门名称已存在");
        }
    }

    // ==================== 部门更新接口测试 ====================

    @Nested
    @DisplayName("部门更新接口测试")
    class UpdateTests {

        @Test
        @DisplayName("更新部门 - 成功")
        void testUpdate_Success() {
            // Given
            Long deptId = 1L;
            DeptDTO dto = createMockDeptDTO();
            dto.setDeptId(deptId);

            // When
            deptController.update(dto);

            // Then
            verify(deptFacade).updateDept(dto);
        }

        @Test
        @DisplayName("更新部门 - 部门不存在")
        void testUpdate_DeptNotFound() {
            // Given
            Long deptId = 999L;
            DeptDTO dto = createMockDeptDTO();
            dto.setDeptId(deptId);

            doThrow(new ServiceException("部门不存在"))
                .when(deptFacade).updateDept(any(DeptDTO.class));

            // When & Then
            assertThatThrownBy(() -> deptController.update(dto))
                .isInstanceOf(ServiceException.class);
        }
    }

    // ==================== 部门删除接口测试 ====================

    @Nested
    @DisplayName("部门删除接口测试")
    class DeleteTests {

        @ParameterizedTest
        @CsvSource({"1", "2", "100"})
        @DisplayName("删除部门 - 成功")
        void testDelete_Success(Long deptId) {
            // Given

            // When
            deptController.delete(deptId);

            // Then
            verify(deptFacade).deleteDept(deptId);
        }

        @Test
        @DisplayName("删除部门 - 部门不存在")
        void testDelete_DeptNotFound() {
            // Given
            Long deptId = 999L;
            doThrow(new ServiceException("部门不存在"))
                .when(deptFacade).deleteDept(deptId);

            // When & Then
            assertThatThrownBy(() -> deptController.delete(deptId))
                .isInstanceOf(ServiceException.class);
        }

        @Test
        @DisplayName("删除部门 - 存在子部门")
        void testDelete_HasChildren() {
            // Given
            Long deptId = 1L;
            doThrow(new ServiceException("部门存在子部门，不允许删除"))
                .when(deptFacade).deleteDept(deptId);

            // When & Then
            assertThatThrownBy(() -> deptController.delete(deptId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("存在子部门");
        }

        @Test
        @DisplayName("删除部门 - 存在用户")
        void testDelete_HasUsers() {
            // Given
            Long deptId = 1L;
            doThrow(new ServiceException("部门存在用户，不允许删除"))
                .when(deptFacade).deleteDept(deptId);

            // When & Then
            assertThatThrownBy(() -> deptController.delete(deptId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("存在用户");
        }
    }

    // ==================== 部门名称唯一性校验接口测试 ====================

    @Nested
    @DisplayName("部门名称唯一性校验接口测试")
    class UniqueTests {

        @ParameterizedTest
        @CsvSource({
            "研发部, 0, true",
            "市场部, 1, true",
            "财务部, 0, false"
        })
        @DisplayName("检查部门名称唯一性")
        void testCheckDeptNameUnique(String deptName, Long parentId, boolean expected) {
            // Given
            when(deptFacade.checkDeptNameUnique(deptName, parentId)).thenReturn(expected);

            // When
            boolean result = deptController.checkDeptNameUnique(deptName, parentId);

            // Then
            assertThat(result).isEqualTo(expected);

            verify(deptFacade).checkDeptNameUnique(deptName, parentId);
        }
    }

    // ==================== Mock 辅助方法 ====================

    private DeptVO createMockDeptVO() {
        return DeptVO.builder()
            .deptId(1L)
            .deptName("研发部")
            .parentId(0L)
            .orderNum(1)
            .leader("张三")
            .phone("13800138000")
            .email("dev@example.com")
            .status("0")
            .ancestors("0")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
    }

    private DeptVO createMockDeptVOWithChildren() {
        DeptVO parent = DeptVO.builder()
            .deptId(1L)
            .deptName("研发部")
            .parentId(0L)
            .orderNum(1)
            .leader("张三")
            .phone("13800138000")
            .email("dev@example.com")
            .status("0")
            .ancestors("0")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();

        DeptVO child1 = DeptVO.builder()
            .deptId(2L)
            .deptName("前端组")
            .parentId(1L)
            .orderNum(1)
            .leader("李四")
            .phone("13800138001")
            .email("frontend@example.com")
            .status("0")
            .ancestors("0,1")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();

        DeptVO child2 = DeptVO.builder()
            .deptId(3L)
            .deptName("后端组")
            .parentId(1L)
            .orderNum(2)
            .leader("王五")
            .phone("13800138002")
            .email("backend@example.com")
            .status("0")
            .ancestors("0,1")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();

        parent.setChildren(Arrays.asList(child1, child2));
        return parent;
    }

    private DeptDTO createMockDeptDTO() {
        DeptDTO dto = new DeptDTO();
        dto.setDeptName("测试部门");
        dto.setParentId(0L);
        dto.setOrderNum(1);
        dto.setLeader("测试负责人");
        dto.setPhone("13900139000");
        dto.setEmail("test@example.com");
        dto.setStatus("0");
        return dto;
    }
}
