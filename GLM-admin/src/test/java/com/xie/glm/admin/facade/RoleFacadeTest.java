package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.RoleVoConverter;
import com.xie.glm.admin.vo.RoleVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.dto.RoleCreateDTO;
import com.xie.glm.system.dto.RoleDTO;
import com.xie.glm.system.dto.RoleUpdateDTO;
import com.xie.glm.system.dto.query.RoleQueryDTO;
import com.xie.glm.system.service.IRoleService;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * RoleFacade 角色门面测试类
 *
 * <p>测试角色门面层，封装 Service 调用和 DTO → VO 转换。
 *
 * <p>测试原则：
 * <ul>
 *   <li>Red-Green-Refactor 循环（TDD）</li>
 *   <li>使用 @ParameterizedTest 进行参数化测试</li>
 *   <li>使用嵌套测试类 (@Nested) 组织相关测试</li>
 * </ul>
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RoleFacade 角色门面单元测试")
class RoleFacadeTest {

    @Mock
    private IRoleService roleService;

    @Mock
    private RoleVoConverter voConverter;

    @InjectMocks
    private RoleFacade roleFacade;

    private RoleDTO testRoleDTO;
    private RoleVO testRoleVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testRoleDTO = createTestRoleDTO(1L, "超级管理员", "admin");
        testRoleVO = createTestRoleVO(1L, "超级管理员", "admin");
    }

    // ==================== 分页查询测试 ====================

    @Nested
    @DisplayName("分页查询角色列表测试")
    class ListRolesTests {

        @Test
        @DisplayName("分页查询角色列表 - 成功")
        void testListRoles_Success() {
            // Given
            RoleQueryDTO query = new RoleQueryDTO();
            query.setPageNum(1);
            query.setPageSize(10);
            query.setRoleName("admin");

            PageResult<RoleDTO> dtoPage = new PageResult<>(
                Arrays.asList(testRoleDTO),
                1L
            );

            when(roleService.listRoles(any(RoleQueryDTO.class)))
                .thenReturn(dtoPage);
            when(voConverter.toVoList(anyList()))
                .thenReturn(Arrays.asList(testRoleVO));

            // When
            PageResult<RoleVO> result = roleFacade.listRoles(query);

            // Then
            assertAll("分页结果验证",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getRecords()).hasSize(1),
                () -> assertThat(result.getTotal()).isEqualTo(1L),
                () -> assertThat(result.getRecords().get(0).getRoleName()).isEqualTo("超级管理员")
            );

            verify(roleService).listRoles(query);
            verify(voConverter).toVoList(Arrays.asList(testRoleDTO));
        }

        @ParameterizedTest
        @MethodSource("providePaginationParams")
        @DisplayName("分页查询角色列表 - 参数化测试")
        void testListRoles_Parameterized(Integer pageNum, Integer pageSize) {
            // Given
            RoleQueryDTO query = new RoleQueryDTO();
            query.setPageNum(pageNum);
            query.setPageSize(pageSize);

            PageResult<RoleDTO> dtoPage = new PageResult<>(Arrays.asList(), 0L);
            when(roleService.listRoles(any(RoleQueryDTO.class))).thenReturn(dtoPage);
            when(voConverter.toVoList(anyList())).thenReturn(Arrays.asList());

            // When
            PageResult<RoleVO> result = roleFacade.listRoles(query);

            // Then
            assertThat(result).isNotNull();
            verify(roleService).listRoles(query);
        }

        private static Stream<Arguments> providePaginationParams() {
            return Stream.of(
                Arguments.of(1, 10),
                Arguments.of(2, 20),
                Arguments.of(1, 50)
            );
        }
    }

    // ==================== 根据ID查询角色测试 ====================

    @Nested
    @DisplayName("根据ID查询角色测试")
    class GetRoleByIdTests {

        @Test
        @DisplayName("根据ID查询角色 - 成功")
        void testGetRoleById_Success() {
            // Given
            Long roleId = 1L;
            when(roleService.getRoleById(roleId)).thenReturn(testRoleDTO);
            when(voConverter.toVo(testRoleDTO)).thenReturn(testRoleVO);

            // When
            RoleVO result = roleFacade.getRoleById(roleId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getRoleId()).isEqualTo(1L);
            assertThat(result.getRoleName()).isEqualTo("超级管理员");

            verify(roleService).getRoleById(roleId);
            verify(voConverter).toVo(testRoleDTO);
        }

        @Test
        @DisplayName("根据ID查询角色 - 角色不存在")
        void testGetRoleById_NotFound() {
            // Given
            Long roleId = 999L;
            when(roleService.getRoleById(roleId))
                .thenThrow(new ServiceException("角色不存在"));

            // When & Then
            assertThatThrownBy(() -> roleFacade.getRoleById(roleId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("角色不存在");
        }
    }

    // ==================== 创建角色测试 ====================

    @Nested
    @DisplayName("创建角色测试")
    class CreateRoleTests {

        @Test
        @DisplayName("创建角色 - 成功")
        void testCreateRole_Success() {
            // Given
            RoleCreateDTO dto = createTestRoleCreateDTO();
            Long expectedRoleId = 1L;
            when(roleService.createRole(dto)).thenReturn(expectedRoleId);

            // When
            roleFacade.createRole(dto);

            // Then
            verify(roleService).createRole(dto);
        }

        @Test
        @DisplayName("创建角色 - 角色名称已存在")
        void testCreateRole_RoleNameExists() {
            // Given
            RoleCreateDTO dto = createTestRoleCreateDTO();
            dto.setRoleName("admin");  // 已存在的角色名称

            when(roleService.createRole(dto))
                .thenThrow(new ServiceException("角色名称已存在"));

            // When & Then
            assertThatThrownBy(() -> roleFacade.createRole(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("角色名称已存在");

            verify(roleService).createRole(dto);
        }
    }

    // ==================== 更新角色测试 ====================

    @Nested
    @DisplayName("更新角色测试")
    class UpdateRoleTests {

        @Test
        @DisplayName("更新角色 - 成功")
        void testUpdateRole_Success() {
            // Given
            RoleUpdateDTO dto = createTestRoleUpdateDTO();

            doNothing().when(roleService).updateRole(any(RoleUpdateDTO.class));

            // When
            roleFacade.updateRole(dto);

            // Then
            verify(roleService).updateRole(dto);
        }

        @Test
        @DisplayName("更新角色 - 角色不存在")
        void testUpdateRole_NotFound() {
            // Given
            RoleUpdateDTO dto = createTestRoleUpdateDTO();
            dto.setRoleId(999L);

            doThrow(new ServiceException("角色不存在"))
                .when(roleService).updateRole(any(RoleUpdateDTO.class));

            // When & Then
            assertThatThrownBy(() -> roleFacade.updateRole(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("角色不存在");

            verify(roleService).updateRole(dto);
        }
    }

    // ==================== 删除角色测试 ====================

    @Nested
    @DisplayName("删除角色测试")
    class DeleteRoleTests {

        @ParameterizedTest
        @CsvSource({"1", "2", "100"})
        @DisplayName("删除角色 - 成功")
        void testDeleteRole_Success(Long roleId) {
            // Given
            doNothing().when(roleService).deleteRole(roleId);

            // When
            roleFacade.deleteRole(roleId);

            // Then
            verify(roleService).deleteRole(roleId);
        }

        @Test
        @DisplayName("批量删除角色 - 成功")
        void testDeleteRoles_Success() {
            // Given
            Long[] roleIds = {1L, 2L, 3L};
            doNothing().when(roleService).deleteRoles(any(Long[].class));

            // When
            roleFacade.deleteRoles(roleIds);

            // Then
            verify(roleService).deleteRoles(roleIds);
        }
    }

    // ==================== 更新状态测试 ====================

    @Nested
    @DisplayName("更新角色状态测试")
    class UpdateStatusTests {

        @ParameterizedTest
        @CsvSource({
            "1, 0, 正常",
            "1, 1, 停用"
        })
        @DisplayName("更新角色状态 - 成功")
        void testUpdateStatus_Success(Long roleId, String status, String description) {
            // Given
            doNothing().when(roleService).updateStatus(roleId, status);

            // When
            roleFacade.updateStatus(roleId, status);

            // Then
            verify(roleService).updateStatus(roleId, status);
        }
    }

    // ==================== 分配菜单权限测试 ====================

    @Nested
    @DisplayName("分配菜单权限测试")
    class AssignMenusTests {

        @Test
        @DisplayName("分配菜单权限 - 成功")
        void testAssignMenus_Success() {
            // Given
            Long roleId = 1L;
            Long[] menuIds = {1L, 2L, 3L};

            doNothing().when(roleService).assignMenus(eq(roleId), any(Long[].class));

            // When
            roleFacade.assignMenus(roleId, menuIds);

            // Then
            verify(roleService).assignMenus(roleId, menuIds);
        }

        @Test
        @DisplayName("分配菜单权限 - 角色不存在")
        void testAssignMenus_RoleNotFound() {
            // Given
            Long roleId = 999L;
            Long[] menuIds = {1L, 2L, 3L};

            doThrow(new ServiceException("角色不存在"))
                .when(roleService).assignMenus(any(Long.class), any(Long[].class));

            // When & Then
            assertThatThrownBy(() -> roleFacade.assignMenus(roleId, menuIds))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("角色不存在");
        }
    }

    // ==================== 唯一性检查测试 ====================

    @Nested
    @DisplayName("唯一性检查测试")
    class CheckUniqueTests {

        @ParameterizedTest
        @CsvSource({
            "newrole, true",
            "admin, false"
        })
        @DisplayName("检查角色名称唯一性")
        void testCheckRoleNameUnique(String roleName, boolean expected) {
            // Given
            when(roleService.checkRoleNameUnique(roleName)).thenReturn(expected);

            // When
            boolean result = roleFacade.checkRoleNameUnique(roleName);

            // Then
            assertThat(result).isEqualTo(expected);
            verify(roleService).checkRoleNameUnique(roleName);
        }

        @ParameterizedTest
        @CsvSource({
            "newkey, true",
            "admin, false"
        })
        @DisplayName("检查角色权限字符串唯一性")
        void testCheckRoleKeyUnique(String roleKey, boolean expected) {
            // Given
            when(roleService.checkRoleKeyUnique(roleKey)).thenReturn(expected);

            // When
            boolean result = roleFacade.checkRoleKeyUnique(roleKey);

            // Then
            assertThat(result).isEqualTo(expected);
            verify(roleService).checkRoleKeyUnique(roleKey);
        }
    }

    // ==================== 辅助方法 ====================

    private RoleDTO createTestRoleDTO(Long roleId, String roleName, String roleKey) {
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(roleId);
        dto.setRoleName(roleName);
        dto.setRoleKey(roleKey);
        dto.setRoleSort(1);
        dto.setDataScope("1");
        dto.setStatus("0");
        dto.setMenuCheckStrictly(true);
        dto.setDeptCheckStrictly(true);
        dto.setRemark("超级管理员");
        dto.setCreateTime(LocalDateTime.now());
        dto.setUpdateTime(LocalDateTime.now());
        dto.setMenuIds(Arrays.asList(1L, 2L, 3L));
        return dto;
    }

    private RoleVO createTestRoleVO(Long roleId, String roleName, String roleKey) {
        return RoleVO.builder()
            .roleId(roleId)
            .roleName(roleName)
            .roleKey(roleKey)
            .roleSort(1)
            .dataScope("1")
            .dataScopeText("全部数据权限")
            .status("0")
            .statusText("正常")
            .menuCheckStrictly(true)
            .deptCheckStrictly(true)
            .remark("超级管理员")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .menuIds(Arrays.asList(1L, 2L, 3L))
            .build();
    }

    private RoleCreateDTO createTestRoleCreateDTO() {
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleName("普通用户");
        dto.setRoleKey("user");
        dto.setRoleSort(2);
        dto.setDataScope("2");
        dto.setStatus("0");
        dto.setMenuCheckStrictly(false);
        dto.setDeptCheckStrictly(false);
        dto.setRemark("普通用户角色");
        dto.setMenuIds(Arrays.asList(4L, 5L));
        return dto;
    }

    private RoleUpdateDTO createTestRoleUpdateDTO() {
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        dto.setRoleName("更新后的角色名");
        dto.setRoleSort(3);
        dto.setRemark("更新后的备注");
        return dto;
    }
}
