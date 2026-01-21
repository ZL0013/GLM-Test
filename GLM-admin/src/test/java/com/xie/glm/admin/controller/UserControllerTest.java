package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.UserFacade;
import com.xie.glm.admin.vo.UserVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import com.xie.glm.system.dto.query.UserQueryDTO;
import com.xie.glm.common.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 用户控制器测试
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
@DisplayName("用户控制器测试")
class UserControllerTest {

    @Mock
    private UserFacade userFacade;

    @InjectMocks
    private UserController userController;

    // ==================== 用户查询接口测试 ====================

    @Nested
    @DisplayName("用户查询接口测试")
    class QueryTests {

        @Test
        @DisplayName("分页查询用户列表 - 成功")
        void listUsers_Success() {
            // Given
            UserQueryDTO query = new UserQueryDTO();
            query.setUserName("admin");

            PageResult<UserVO> expectedPage = new PageResult<>(List.of(createMockUserVO()), 10L);
            when(userFacade.listUsers(query)).thenReturn(expectedPage);

            // When
            PageResult<UserVO> result = userController.list(query);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotal()).isEqualTo(10L);
            assertThat(result.getRecords()).hasSize(1);

            verify(userFacade).listUsers(query);
        }

        @Test
        @DisplayName("分页查询用户列表 - 空结果")
        void listUsers_EmptyResult() {
            // Given
            UserQueryDTO query = new UserQueryDTO();
            PageResult<UserVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(userFacade.listUsers(query)).thenReturn(expectedPage);

            // When
            PageResult<UserVO> result = userController.list(query);

            // Then
            assertThat(result.getRecords()).isEmpty();
            assertThat(result.getTotal()).isEqualTo(0L);
        }

        @ParameterizedTest
        @MethodSource("provideQueryConditions")
        @DisplayName("分页查询用户列表 - 带条件查询")
        void listUsers_WithConditions(String userName, String status) {
            // Given
            UserQueryDTO query = new UserQueryDTO();
            query.setUserName(userName);
            query.setStatus(status);

            PageResult<UserVO> expectedPage = new PageResult<>(List.of(), 0L);
            when(userFacade.listUsers(query)).thenReturn(expectedPage);

            // When
            PageResult<UserVO> result = userController.list(query);

            // Then
            assertThat(result).isNotNull();
            verify(userFacade).listUsers(query);
        }

        private static List<Arguments> provideQueryConditions() {
            return List.of(
                Arguments.of("admin", "0"),
                Arguments.of("user", "1"),
                Arguments.of("", "0")
            );
        }
    }

    // ==================== 用户详情接口测试 ====================

    @Nested
    @DisplayName("用户详情接口测试")
    class DetailTests {

        @Test
        @DisplayName("查询用户详情 - 成功")
        void getDetail_Success() {
            // Given
            Long userId = 1L;
            UserVO expectedUser = createMockUserVO();
            when(userFacade.getUserById(userId)).thenReturn(expectedUser);

            // When
            UserVO result = userController.getDetail(userId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getUserId()).isEqualTo(userId);

            verify(userFacade).getUserById(userId);
        }

        @Test
        @DisplayName("查询用户详情 - 用户不存在")
        void getDetail_UserNotFound() {
            // Given
            Long userId = 999L;
            when(userFacade.getUserById(userId))
                .thenThrow(new ServiceException("用户不存在"));

            // When & Then
            assertThatThrownBy(() -> userController.getDetail(userId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("用户不存在");
        }
    }

    // ==================== 用户创建接口测试 ====================

    @Nested
    @DisplayName("用户创建接口测试")
    class CreateTests {

        @Test
        @DisplayName("创建用户 - 成功")
        void create_Success() {
            // Given
            UserCreateDTO dto = createMockUserCreateDTO();
            Long expectedUserId = 1L;
            when(userFacade.createUser(dto)).thenReturn(expectedUserId);

            // When
            Long result = userController.create(dto);

            // Then
            assertThat(result).isEqualTo(expectedUserId);

            verify(userFacade).createUser(dto);
        }

        @Test
        @DisplayName("创建用户 - 用户名重复")
        void create_UserNameDuplicate() {
            // Given
            UserCreateDTO dto = createMockUserCreateDTO();
            when(userFacade.createUser(dto))
                .thenThrow(new ServiceException("用户名已存在"));

            // When & Then
            assertThatThrownBy(() -> userController.create(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("用户名已存在");
        }
    }

    // ==================== 用户更新接口测试 ====================

    @Nested
    @DisplayName("用户更新接口测试")
    class UpdateTests {

        @Test
        @DisplayName("更新用户 - 成功")
        void update_Success() {
            // Given
            Long userId = 1L;
            UserUpdateDTO dto = createMockUserUpdateDTO();

            // When
            userController.update(userId, dto);

            // Then
            assertThat(dto.getUserId()).isEqualTo(userId);

            verify(userFacade).updateUser(dto);
        }

        @Test
        @DisplayName("更新用户 - 用户不存在")
        void update_UserNotFound() {
            // Given
            Long userId = 999L;
            UserUpdateDTO dto = createMockUserUpdateDTO();
            doThrow(new ServiceException("用户不存在"))
                .when(userFacade).updateUser(any(UserUpdateDTO.class));

            // When & Then
            assertThatThrownBy(() -> userController.update(userId, dto))
                .isInstanceOf(ServiceException.class);
        }
    }

    // ==================== 用户删除接口测试 ====================

    @Nested
    @DisplayName("用户删除接口测试")
    class DeleteTests {

        @Test
        @DisplayName("删除用户 - 成功")
        void delete_Success() {
            // Given
            Long userId = 1L;

            // When
            userController.delete(userId);

            // Then
            verify(userFacade).deleteUser(userId);
        }

        @Test
        @DisplayName("删除用户 - 用户不存在")
        void delete_UserNotFound() {
            // Given
            Long userId = 999L;
            doThrow(new ServiceException("用户不存在"))
                .when(userFacade).deleteUser(userId);

            // When & Then
            assertThatThrownBy(() -> userController.delete(userId))
                .isInstanceOf(ServiceException.class);
        }

        @Test
        @DisplayName("批量删除用户 - 成功")
        void deleteBatch_Success() {
            // Given
            Long[] userIds = {1L, 2L, 3L};

            // When
            userController.deleteBatch(userIds);

            // Then
            verify(userFacade).deleteUsers(userIds);
        }
    }

    // ==================== 密码重置接口测试 ====================

    @Nested
    @DisplayName("密码重置接口测试")
    class PasswordTests {

        @Test
        @DisplayName("重置用户密码 - 成功")
        void resetPassword_Success() {
            // Given
            Long userId = 1L;

            // When
            userController.resetPassword(userId);

            // Then
            verify(userFacade).resetPassword(userId);
        }

        @Test
        @DisplayName("重置用户密码 - 用户不存在")
        void resetPassword_UserNotFound() {
            // Given
            Long userId = 999L;
            doThrow(new ServiceException("用户不存在"))
                .when(userFacade).resetPassword(userId);

            // When & Then
            assertThatThrownBy(() -> userController.resetPassword(userId))
                .isInstanceOf(ServiceException.class);
        }
    }

    // ==================== 状态修改接口测试 ====================

    @Nested
    @DisplayName("状态修改接口测试")
    class StatusTests {

        @ParameterizedTest
        @MethodSource("provideStatusValues")
        @DisplayName("修改用户状态 - 成功")
        void updateStatus_Success(String status) {
            // Given
            Long userId = 1L;

            // When
            userController.updateStatus(userId, status);

            // Then
            verify(userFacade).updateStatus(userId, status);
        }

        @Test
        @DisplayName("修改用户状态 - 用户不存在")
        void updateStatus_UserNotFound() {
            // Given
            Long userId = 999L;
            String status = "1";
            doThrow(new ServiceException("用户不存在"))
                .when(userFacade).updateStatus(userId, status);

            // When & Then
            assertThatThrownBy(() -> userController.updateStatus(userId, status))
                .isInstanceOf(ServiceException.class);
        }

        private static List<Arguments> provideStatusValues() {
            return List.of(
                Arguments.of("0"),  // 正常
                Arguments.of("1")   // 停用
            );
        }
    }

    // ==================== Mock 辅助方法 ====================

    private UserVO createMockUserVO() {
        return UserVO.builder()
            .userId(1L)
            .userName("admin")
            .nickName("管理员")
            .email("admin@example.com")
            .status("0")
            .build();
    }

    private UserCreateDTO createMockUserCreateDTO() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("testuser");
        dto.setNickName("测试用户");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        return dto;
    }

    private UserUpdateDTO createMockUserUpdateDTO() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setNickName("更新后的昵称");
        dto.setEmail("updated@example.com");
        return dto;
    }
}
