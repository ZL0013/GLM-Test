package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.DeptVoConverter;
import com.xie.glm.admin.vo.DeptVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.dto.DeptDTO;
import com.xie.glm.system.dto.query.DeptQueryDTO;
import com.xie.glm.system.service.IDeptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * DeptFacade 部门门面测试类
 *
 * <p>测试部门门面层，封装 Service 调用和 DTO → VO 转换。
 *
 * <p>测试覆盖：
 * <ul>
 *   <li>分页查询部门列表</li>
 *   <li>查询所有部门列表</li>
 *   <li>构建部门树</li>
 *   <li>根据 ID 查询部门详情</li>
 *   <li>创建部门</li>
 *   <li>更新部门信息</li>
 *   <li>删除部门</li>
 *   <li>检查部门名称唯一性</li>
 * </ul>
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeptFacade 部门门面单元测试")
class DeptFacadeTest {

    @Mock
    private IDeptService deptService;

    @Mock
    private DeptVoConverter voConverter;

    @InjectMocks
    private DeptFacade deptFacade;

    private DeptDTO testDeptDTO;
    private DeptVO testDeptVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testDeptDTO = createTestDeptDTO(1L, "技术部", 0L);
        testDeptVO = createTestDeptVO(1L, "技术部", 0L);
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询部门列表 - 成功")
    void testListDepts_Success() {
        // Given
        DeptQueryDTO query = new DeptQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeptName("技术");

        PageResult<DeptDTO> dtoPage = new PageResult<>(
            Arrays.asList(testDeptDTO),
            1L
        );

        when(deptService.listDepts(any(DeptQueryDTO.class)))
            .thenReturn(dtoPage);
        when(voConverter.toVoList(anyList()))
            .thenReturn(Arrays.asList(testDeptVO));

        // When
        PageResult<DeptVO> result = deptFacade.listDepts(query);

        // Then
        assertAll("分页结果验证",
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.getRecords()).hasSize(1),
            () -> assertThat(result.getTotal()).isEqualTo(1L),
            () -> assertThat(result.getRecords().get(0).getDeptName()).isEqualTo("技术部")
        );

        verify(deptService).listDepts(query);
        verify(voConverter).toVoList(Arrays.asList(testDeptDTO));
    }

    @ParameterizedTest
    @MethodSource("providePaginationParams")
    @DisplayName("分页查询部门列表 - 参数化测试")
    void testListDepts_Parameterized(Integer pageNum, Integer pageSize) {
        // Given
        DeptQueryDTO query = new DeptQueryDTO();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);

        PageResult<DeptDTO> dtoPage = new PageResult<>(Collections.emptyList(), 0L);
        when(deptService.listDepts(any(DeptQueryDTO.class))).thenReturn(dtoPage);
        when(voConverter.toVoList(anyList())).thenReturn(Collections.emptyList());

        // When
        PageResult<DeptVO> result = deptFacade.listDepts(query);

        // Then
        assertThat(result).isNotNull();
        verify(deptService).listDepts(query);
    }

    private static Stream<Arguments> providePaginationParams() {
        return Stream.of(
            Arguments.of(1, 10),
            Arguments.of(2, 20),
            Arguments.of(1, 50)
        );
    }

    // ==================== 查询所有部门测试 ====================

    @Test
    @DisplayName("查询所有部门列表 - 成功")
    void testListAllDepts_Success() {
        // Given
        List<DeptDTO> dtoList = Arrays.asList(
            createTestDeptDTO(1L, "技术部", 0L),
            createTestDeptDTO(2L, "市场部", 0L)
        );
        List<DeptVO> voList = Arrays.asList(
            createTestDeptVO(1L, "技术部", 0L),
            createTestDeptVO(2L, "市场部", 0L)
        );

        when(deptService.listAllDepts()).thenReturn(dtoList);
        when(voConverter.toVoList(dtoList)).thenReturn(voList);

        // When
        List<DeptVO> result = deptFacade.listAllDepts();

        // Then
        assertAll("部门列表验证",
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result).hasSize(2),
            () -> assertThat(result.get(0).getDeptName()).isEqualTo("技术部"),
            () -> assertThat(result.get(1).getDeptName()).isEqualTo("市场部")
        );

        verify(deptService).listAllDepts();
        verify(voConverter).toVoList(dtoList);
    }

    @Test
    @DisplayName("查询所有部门列表 - 空列表")
    void testListAllDepts_Empty() {
        // Given
        when(deptService.listAllDepts()).thenReturn(Collections.emptyList());
        when(voConverter.toVoList(anyList())).thenReturn(Collections.emptyList());

        // When
        List<DeptVO> result = deptFacade.listAllDepts();

        // Then
        assertThat(result).isNotNull().isEmpty();
        verify(deptService).listAllDepts();
    }

    // ==================== 构建部门树测试 ====================

    @Test
    @DisplayName("构建部门树 - 两层结构")
    void testBuildDeptTree_TwoLevels() {
        // Given - 扁平部门列表
        List<DeptDTO> flatDtoList = Arrays.asList(
            createTestDeptDTO(1L, "公司总部", 0L),
            createTestDeptDTO(2L, "技术部", 1L),
            createTestDeptDTO(3L, "市场部", 1L)
        );

        List<DeptVO> flatVoList = Arrays.asList(
            createTestDeptVO(1L, "公司总部", 0L),
            createTestDeptVO(2L, "技术部", 1L),
            createTestDeptVO(3L, "市场部", 1L)
        );

        when(deptService.buildDeptTree()).thenReturn(flatDtoList);
        when(voConverter.toVoList(flatDtoList)).thenReturn(flatVoList);

        // When
        List<DeptVO> result = deptFacade.buildDeptTree();

        // Then
        assertAll("树形结构验证",
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.get(0).getDeptName()).isEqualTo("公司总部"),
            () -> assertThat(result.get(0).getChildren()).isNotNull(),
            () -> assertThat(result.get(0).getChildren()).hasSize(2),
            () -> assertThat(result.get(0).getChildren().get(0).getDeptName()).isEqualTo("技术部"),
            () -> assertThat(result.get(0).getChildren().get(1).getDeptName()).isEqualTo("市场部")
        );

        verify(deptService).buildDeptTree();
        verify(voConverter).toVoList(flatDtoList);
    }

    @Test
    @DisplayName("构建部门树 - 三层结构")
    void testBuildDeptTree_ThreeLevels() {
        // Given - 扁平部门列表
        List<DeptDTO> flatDtoList = Arrays.asList(
            createTestDeptDTO(1L, "公司总部", 0L),
            createTestDeptDTO(2L, "技术部", 1L),
            createTestDeptDTO(3L, "研发组", 2L),
            createTestDeptDTO(4L, "测试组", 2L)
        );

        List<DeptVO> flatVoList = Arrays.asList(
            createTestDeptVO(1L, "公司总部", 0L),
            createTestDeptVO(2L, "技术部", 1L),
            createTestDeptVO(3L, "研发组", 2L),
            createTestDeptVO(4L, "测试组", 2L)
        );

        when(deptService.buildDeptTree()).thenReturn(flatDtoList);
        when(voConverter.toVoList(flatDtoList)).thenReturn(flatVoList);

        // When
        List<DeptVO> result = deptFacade.buildDeptTree();

        // Then
        assertAll("三层树形结构验证",
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.get(0).getDeptName()).isEqualTo("公司总部"),
            () -> assertThat(result.get(0).getChildren()).hasSize(1),
            () -> assertThat(result.get(0).getChildren().get(0).getDeptName()).isEqualTo("技术部"),
            () -> assertThat(result.get(0).getChildren().get(0).getChildren()).hasSize(2),
            () -> assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getDeptName()).isEqualTo("研发组"),
            () -> assertThat(result.get(0).getChildren().get(0).getChildren().get(1).getDeptName()).isEqualTo("测试组")
        );

        verify(deptService).buildDeptTree();
    }

    @Test
    @DisplayName("构建部门树 - 空列表")
    void testBuildDeptTree_Empty() {
        // Given
        when(deptService.buildDeptTree()).thenReturn(Collections.emptyList());
        when(voConverter.toVoList(anyList())).thenReturn(Collections.emptyList());

        // When
        List<DeptVO> result = deptFacade.buildDeptTree();

        // Then
        assertThat(result).isNotNull().isEmpty();
        verify(deptService).buildDeptTree();
    }

    // ==================== 根据ID查询部门测试 ====================

    @Test
    @DisplayName("根据ID查询部门 - 成功")
    void testGetDeptById_Success() {
        // Given
        Long deptId = 1L;
        when(deptService.getDeptById(deptId)).thenReturn(testDeptDTO);
        when(voConverter.toVo(testDeptDTO)).thenReturn(testDeptVO);

        // When
        DeptVO result = deptFacade.getDeptById(deptId);

        // Then
        assertAll("部门详情验证",
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.getDeptId()).isEqualTo(1L),
            () -> assertThat(result.getDeptName()).isEqualTo("技术部")
        );

        verify(deptService).getDeptById(deptId);
        verify(voConverter).toVo(testDeptDTO);
    }

    @ParameterizedTest
    @CsvSource({
        "1, true",
        "999, false"
    })
    @DisplayName("根据ID查询部门 - 部门不存在")
    void testGetDeptById_NotFound(Long deptId, boolean exists) {
        // Given
        if (exists) {
            when(deptService.getDeptById(deptId)).thenReturn(testDeptDTO);
            when(voConverter.toVo(testDeptDTO)).thenReturn(testDeptVO);
        } else {
            when(deptService.getDeptById(deptId))
                .thenThrow(new ServiceException("部门不存在"));
        }

        // When & Then
        if (exists) {
            DeptVO result = deptFacade.getDeptById(deptId);
            assertThat(result).isNotNull();
        } else {
            assertThatThrownBy(() -> deptFacade.getDeptById(deptId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("部门不存在");
        }
    }

    // ==================== 创建部门测试 ====================

    @Test
    @DisplayName("创建部门 - 成功")
    void testCreateDept_Success() {
        // Given
        DeptDTO dto = createTestDeptDTO(null, "新部门", 1L);
        dto.setLeader("张三");
        dto.setPhone("13800138000");
        dto.setEmail("newdept@example.com");

        Long expectedDeptId = 10L;
        when(deptService.createDept(dto)).thenReturn(expectedDeptId);

        // When
        Long result = deptFacade.createDept(dto);

        // Then
        assertThat(result).isEqualTo(expectedDeptId);
        verify(deptService).createDept(dto);
    }

    @Test
    @DisplayName("创建部门 - 部门名称已存在")
    void testCreateDept_DeptNameExists() {
        // Given
        DeptDTO dto = createTestDeptDTO(null, "技术部", 1L);

        when(deptService.createDept(dto))
            .thenThrow(new ServiceException("部门名称已存在"));

        // When & Then
        assertThatThrownBy(() -> deptFacade.createDept(dto))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("部门名称已存在");

        verify(deptService).createDept(dto);
    }

    // ==================== 更新部门测试 ====================

    @Test
    @DisplayName("更新部门 - 成功")
    void testUpdateDept_Success() {
        // Given
        DeptDTO dto = createTestDeptDTO(1L, "更新后的部门", 1L);
        dto.setLeader("李四");
        dto.setPhone("13900139000");

        doNothing().when(deptService).updateDept(any(DeptDTO.class));

        // When
        deptFacade.updateDept(dto);

        // Then
        verify(deptService).updateDept(dto);
    }

    @Test
    @DisplayName("更新部门 - 部门不存在")
    void testUpdateDept_NotFound() {
        // Given
        DeptDTO dto = createTestDeptDTO(999L, "不存在的部门", 1L);

        doThrow(new ServiceException("部门不存在"))
            .when(deptService).updateDept(any(DeptDTO.class));

        // When & Then
        assertThatThrownBy(() -> deptFacade.updateDept(dto))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("部门不存在");

        verify(deptService).updateDept(dto);
    }

    // ==================== 删除部门测试 ====================

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 100L})
    @DisplayName("删除部门 - 成功")
    void testDeleteDept_Success(Long deptId) {
        // Given
        doNothing().when(deptService).deleteDept(deptId);

        // When
        deptFacade.deleteDept(deptId);

        // Then
        verify(deptService).deleteDept(deptId);
    }

    @Test
    @DisplayName("删除部门 - 存在子部门")
    void testDeleteDept_HasChildren() {
        // Given
        Long deptId = 1L;
        doThrow(new ServiceException("部门存在子部门，不允许删除"))
            .when(deptService).deleteDept(deptId);

        // When & Then
        assertThatThrownBy(() -> deptFacade.deleteDept(deptId))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("子部门");

        verify(deptService).deleteDept(deptId);
    }

    @Test
    @DisplayName("删除部门 - 存在用户")
    void testDeleteDept_HasUsers() {
        // Given
        Long deptId = 1L;
        doThrow(new ServiceException("部门存在用户，不允许删除"))
            .when(deptService).deleteDept(deptId);

        // When & Then
        assertThatThrownBy(() -> deptFacade.deleteDept(deptId))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("用户");

        verify(deptService).deleteDept(deptId);
    }

    // ==================== 唯一性检查测试 ====================

    @ParameterizedTest
    @CsvSource({
        "新部门, 1, true",
        "技术部, 1, false"
    })
    @DisplayName("检查部门名称唯一性")
    void testCheckDeptNameUnique(String deptName, Long parentId, boolean expected) {
        // Given
        when(deptService.checkDeptNameUnique(deptName, parentId)).thenReturn(expected);

        // When
        boolean result = deptFacade.checkDeptNameUnique(deptName, parentId);

        // Then
        assertThat(result).isEqualTo(expected);
        verify(deptService).checkDeptNameUnique(deptName, parentId);
    }

    // ==================== 辅助方法 ====================

    /**
     * 创建测试用部门 DTO
     */
    private DeptDTO createTestDeptDTO(Long deptId, String deptName, Long parentId) {
        return DeptDTO.builder()
            .deptId(deptId)
            .deptName(deptName)
            .parentId(parentId)
            .orderNum(1)
            .leader("负责人")
            .phone("13800138000")
            .email("dept@example.com")
            .status("0")
            .ancestors("0")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
    }

    /**
     * 创建测试用部门 VO
     */
    private DeptVO createTestDeptVO(Long deptId, String deptName, Long parentId) {
        return DeptVO.builder()
            .deptId(deptId)
            .deptName(deptName)
            .parentId(parentId)
            .orderNum(1)
            .leader("负责人")
            .phone("13800138000")
            .email("dept@example.com")
            .status("0")
            .ancestors("0")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .children(null)
            .build();
    }
}
