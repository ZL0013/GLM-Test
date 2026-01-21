package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.RoleFacade;
import com.xie.glm.admin.vo.RoleVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.dto.RoleCreateDTO;
import com.xie.glm.system.dto.RoleUpdateDTO;
import com.xie.glm.system.dto.query.RoleQueryDTO;
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
 * 角色控制器测试
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
@DisplayName("角色控制器测试")
class RoleControllerTest {

    @Mock
    private RoleFacade roleFacade;

    @InjectMocks
    private RoleController roleController;

    // ==================== 角色查询接口测试 ====================

    @Nested
    @DisplayName("角色查询接口测试")
    class QueryTests {

        @Test
        @DisplayName("分页查询角色列表 - 成功")
        void testListRoles_Success() {
            // Given
            RoleQueryDTO query = new RoleQueryDTO();
            query.setRoleName("admin");

            PageResult<RoleVO> expectedPage = new PageResult<>(
                Arrays.asList(createMockRoleVO()),
                10L
            );
            when(roleFacade.listRoles(query)).thenReturn(expectedPage);

            // When
            PageResult<RoleVO> result = roleController.list(query);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotal()).isEqualTo(10L);
            assertThat(result.getRecords()).hasSize(1);

            verify(roleFacade).listRoles(query);
        }

        @Test
        @DisplayName("分页查询角色列表 - 空结果")
        void testListRoles_EmptyResult() {
            // Given
            RoleQueryDTO query = new RoleQueryDTO();
            PageResult<RoleVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(roleFacade.listRoles(query)).thenReturn(expectedPage);

            // When
            PageResult<RoleVO> result = roleController.list(query);

            // Then
            assertThat(result.getRecords()).isEmpty();
            assertThat(result.getTotal()).isEqualTo(0L);
        }

        @ParameterizedTest
        @MethodSource("provideQueryConditions")
        @DisplayName("分页查询角色列表 - 带条件查询")
        void testListRoles_WithConditions(String roleName, String status) {
            // Given
            RoleQueryDTO query = new RoleQueryDTO();
            query.setRoleName(roleName);
            query.setStatus(status);

            PageResult<RoleVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(roleFacade.listRoles(query)).thenReturn(expectedPage);

            // When
            PageResult<RoleVO> result = roleController.list(query);

            // Then
            assertThat(result).isNotNull();
            verify(roleFacade).listRoles(query);
        }

        private static List<Arguments> provideQueryConditions() {
            return List.of(
                Arguments.of("admin", "0"),
                Arguments.of("user", "1"),
                Arguments.of("", "0")
            );
        }
    }

    // ==================== 角色详情接口测试 ====================

    @Nested
    @DisplayName("角色详情接口测试")
    class DetailTests {

        @Test
        @DisplayName("查询角色详情 - 成功")
        void testGetDetail_Success() {
            // Given
            Long roleId = 1L;
            RoleVO expectedRole = createMockRoleVO();
            when(roleFacade.getRoleById(roleId)).thenReturn(expectedRole);

            // When
            RoleVO result = roleController.getDetail(roleId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getRoleId()).isEqualTo(roleId);

            verify(roleFacade).getRoleById(roleId);
        }

        @Test
        @DisplayName("查询角色详情 - 角色不存在")
        void testGetDetail_RoleNotFound() {
            // Given
            Long roleId = 999L;
            when(roleFacade.getRoleById(roleId))
                .thenThrow(new ServiceException("角色不存在"));

            // When & Then
            assertThatThrownBy(() -> roleController.getDetail(roleId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("角色不存在");
        }
    }

    // ==================== 角色创建接口测试 ====================

    @Nested
    @DisplayName("角色创建接口测试")
    class CreateTests {

        @Test
        @DisplayName("创建角色 - 成功")
        void testCreate_Success() {
            // Given
            RoleCreateDTO dto = createMockRoleCreateDTO();
            Long expectedRoleId = 1L;
            when(roleFacade.createRole(dto)).thenReturn(expectedRoleId);

            // When
            Long result = roleController.create(dto);

            // Then
            assertThat(result).isEqualTo(expectedRoleId);

            verify(roleFacade).createRole(dto);
        }

        @Test
        @DisplayName("创建角色 - 角色名称重复")
        void testCreate_RoleNameDuplicate() {
            // Given
            RoleCreateDTO dto = createMockRoleCreateDTO();
            when(roleFacade.createRole(dto))
                .thenThrow(new ServiceException("角色名称已存在"));

            // When & Then
            assertThatThrownBy(() -> roleController.create(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("角色名称已存在");
        }
    }

    // ==================== 角色更新接口测试 ====================

    @Nested
    @DisplayName("角色更新接口测试")
    class UpdateTests {

        @Test
        @DisplayName("更新角色 - 成功")
        void testUpdate_Success() {
            // Given
            Long roleId = 1L;
            RoleUpdateDTO dto = createMockRoleUpdateDTO();

            // When
            roleController.update(roleId, dto);

            // Then
            assertThat(dto.getRoleId()).isEqualTo(roleId);

            verify(roleFacade).updateRole(dto);
        }

        @Test
        @DisplayName("更新角色 - 角色不存在")
        void testUpdate_RoleNotFound() {
            // Given
            Long roleId = 999L;
            RoleUpdateDTO dto = createMockRoleUpdateDTO();
            doThrow(new ServiceException("角色不存在"))
                .when(roleFacade).updateRole(any(RoleUpdateDTO.class));

            // When & Then
            assertThatThrownBy(() -> roleController.update(roleId, dto))
                .isInstanceOf(ServiceException.class);
        }
    }

    // ==================== 角色删除接口测试 ====================

    @Nested
    @DisplayName("角色删除接口测试")
    class DeleteTests {

        @ParameterizedTest
        @CsvSource({"1", "2", "100"})
        @DisplayName("删除角色 - 成功")
        void testDelete_Success(Long roleId) {
            // Given

            // When
            roleController.delete(roleId);

            // Then
            verify(roleFacade).deleteRole(roleId);
        }

        @Test
        @DisplayName("删除角色 - 角色不存在")
        void testDelete_RoleNotFound() {
            // Given
            Long roleId = 999L;
            doThrow(new ServiceException("角色不存在"))
                .when(roleFacade).deleteRole(roleId);

            // When & Then
            assertThatThrownBy(() -> roleController.delete(roleId))
                .isInstanceOf(ServiceException.class);
        }

        @Test
        @DisplayName("批量删除角色 - 成功")
        void testDeleteBatch_Success() {
            // Given
            Long[] roleIds = {1L, 2L, 3L};

            // When
            roleController.deleteBatch(roleIds);

            // Then
            verify(roleFacade).deleteRoles(roleIds);
        }
    }

    // ==================== 菜单权限分配接口测试 ====================

    @Nested
    @DisplayName("菜单权限分配接口测试")
    class AssignMenusTests {

        @Test
        @DisplayName("分配菜单权限 - 成功")
        void testAssignMenus_Success() {
            // Given
            Long roleId = 1L;
            Long[] menuIds = {1L, 2L, 3L};

            // When
            roleController.assignMenus(roleId, menuIds);

            // Then
            verify(roleFacade).assignMenus(roleId, menuIds);
        }

        @Test
        @DisplayName("分配菜单权限 - 角色不存在")
        void testAssignMenus_RoleNotFound() {
            // Given
            Long roleId = 999L;
            Long[] menuIds = {1L, 2L, 3L};

            doThrow(new ServiceException("角色不存在"))
                .when(roleFacade).assignMenus(any(Long.class), any(Long[].class));

            // When & Then
            assertThatThrownBy(() -> roleController.assignMenus(roleId, menuIds))
                .isInstanceOf(ServiceException.class);
        }
    }

    // ==================== 状态修改接口测试 ====================

    @Nested
    @DisplayName("状态修改接口测试")
    class StatusTests {

        @ParameterizedTest
        @CsvSource({
            "1, 0, 正常",
            "1, 1, 停用"
        })
        @DisplayName("修改角色状态 - 成功")
        void testUpdateStatus_Success(Long roleId, String status, String description) {
            // Given

            // When
            roleController.updateStatus(roleId, status);

            // Then
            verify(roleFacade).updateStatus(roleId, status);
        }

        @Test
        @DisplayName("修改角色状态 - 角色不存在")
        void testUpdateStatus_RoleNotFound() {
            // Given
            Long roleId = 999L;
            String status = "1";
            doThrow(new ServiceException("角色不存在"))
                .when(roleFacade).updateStatus(roleId, status);

            // When & Then
            assertThatThrownBy(() -> roleController.updateStatus(roleId, status))
                .isInstanceOf(ServiceException.class);
        }
    }

    // ==================== Mock 辅助方法 ====================

    private RoleVO createMockRoleVO() {
        return RoleVO.builder()
            .roleId(1L)
            .roleName("超级管理员")
            .roleKey("admin")
            .roleSort(1)
            .dataScope("1")
            .dataScopeText("全部数据权限")
            .status("0")
            .statusText("正常")
            .menuCheckStrictly(true)
            .deptCheckStrictly(true)
            .remark("超级管理员")
            .build();
    }

    private RoleCreateDTO createMockRoleCreateDTO() {
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleName("普通用户");
        dto.setRoleKey("user");
        dto.setRoleSort(2);
        dto.setDataScope("2");
        dto.setStatus("0");
        dto.setRemark("普通用户角色");
        return dto;
    }

    private RoleUpdateDTO createMockRoleUpdateDTO() {
        RoleUpdateDTO dto = new RoleUpdateDTO();
        dto.setRoleId(1L);
        dto.setRoleName("更新后的角色名");
        dto.setRoleSort(3);
        dto.setRemark("更新后的备注");
        return dto;
    }
}
