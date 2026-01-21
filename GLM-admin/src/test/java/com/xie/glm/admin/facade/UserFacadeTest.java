package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.UserVoConverter;
import com.xie.glm.admin.vo.UserVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import com.xie.glm.system.dto.query.UserQueryDTO;
import com.xie.glm.system.service.IUserService;
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
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UserFacade 用户门面测试类
 *
 * <p>测试用户门面层，封装 Service 调用和 DTO → VO 转换。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserFacade 用户门面单元测试")
class UserFacadeTest {

    @Mock
    private IUserService userService;

    @Mock
    private UserVoConverter voConverter;

    @InjectMocks
    private UserFacade userFacade;

    private UserDTO testUserDTO;
    private UserVO testUserVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testUserDTO = createTestUserDTO(1L, "admin", "管理员");
        testUserVO = createTestUserVO(1L, "admin", "管理员");
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询用户列表 - 成功")
    void testListUsers_Success() {
        // Given
        UserQueryDTO query = new UserQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setUserName("admin");

        PageResult<UserDTO> dtoPage = new PageResult<>(
            Arrays.asList(testUserDTO),
            1L
        );

        when(userService.listUsers(any(UserQueryDTO.class)))
            .thenReturn(dtoPage);
        when(voConverter.toVoList(anyList()))
            .thenReturn(Arrays.asList(testUserVO));

        // When
        PageResult<UserVO> result = userFacade.listUsers(query);

        // Then
        assertAll("分页结果验证",
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.getRecords()).hasSize(1),
            () -> assertThat(result.getTotal()).isEqualTo(1L),
            () -> assertThat(result.getRecords().get(0).getUserName()).isEqualTo("admin")
        );

        verify(userService).listUsers(query);
        verify(voConverter).toVoList(Arrays.asList(testUserDTO));
    }

    @ParameterizedTest
    @MethodSource("providePaginationParams")
    @DisplayName("分页查询用户列表 - 参数化测试")
    void testListUsers_Parameterized(Integer pageNum, Integer pageSize) {
        // Given
        UserQueryDTO query = new UserQueryDTO();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);

        PageResult<UserDTO> dtoPage = new PageResult<>(Arrays.asList(), 0L);
        when(userService.listUsers(any(UserQueryDTO.class))).thenReturn(dtoPage);
        when(voConverter.toVoList(anyList())).thenReturn(Arrays.asList());

        // When
        PageResult<UserVO> result = userFacade.listUsers(query);

        // Then
        assertThat(result).isNotNull();
        verify(userService).listUsers(query);
    }

    private static Stream<Arguments> providePaginationParams() {
        return Stream.of(
            Arguments.of(1, 10),
            Arguments.of(2, 20),
            Arguments.of(1, 50)
        );
    }

    // ==================== 根据ID查询用户测试 ====================

    @Test
    @DisplayName("根据ID查询用户 - 成功")
    void testGetUserById_Success() {
        // Given
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(testUserDTO);
        when(voConverter.toVo(testUserDTO)).thenReturn(testUserVO);

        // When
        UserVO result = userFacade.getUserById(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getUserName()).isEqualTo("admin");

        verify(userService).getUserById(userId);
        verify(voConverter).toVo(testUserDTO);
    }

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "999, false"
    })
    @DisplayName("根据ID查询用户 - 用户不存在")
    void testGetUserById_NotFound(Long userId, boolean exists) {
        // Given
        if (exists) {
            when(userService.getUserById(userId)).thenReturn(testUserDTO);
            when(voConverter.toVo(testUserDTO)).thenReturn(testUserVO);
        } else {
            when(userService.getUserById(userId))
                .thenThrow(new ServiceException("用户不存在"));
        }

        // When & Then
        if (exists) {
            UserVO result = userFacade.getUserById(userId);
            assertThat(result).isNotNull();
        } else {
            assertThatThrownBy(() -> userFacade.getUserById(userId))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("用户不存在");
        }
    }

    // ==================== 创建用户测试 ====================

    @Test
    @DisplayName("创建用户 - 成功")
    void testCreateUser_Success() {
        // Given
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("newuser");
        dto.setNickName("新用户");
        dto.setPassword("Password@123");
        dto.setEmail("newuser@example.com");

        Long expectedUserId = 1L;
        when(userService.createUser(dto)).thenReturn(expectedUserId);

        // When
        userFacade.createUser(dto);

        // Then
        verify(userService).createUser(dto);
        // 注意：创建用户返回的是用户ID，不需要转换为VO
    }

    @Test
    @DisplayName("创建用户 - 用户名已存在")
    void testCreateUser_UserNameExists() {
        // Given
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("admin");  // 已存在的用户名

        when(userService.createUser(dto))
            .thenThrow(new ServiceException("用户名已存在"));

        // When & Then
        assertThatThrownBy(() -> userFacade.createUser(dto))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("用户名已存在");

        verify(userService).createUser(dto);
    }

    // ==================== 更新用户测试 ====================

    @Test
    @DisplayName("更新用户 - 成功")
    void testUpdateUser_Success() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setNickName("新昵称");
        dto.setEmail("new@example.com");

        doNothing().when(userService).updateUser(any(UserUpdateDTO.class));

        // When
        userFacade.updateUser(dto);

        // Then
        verify(userService).updateUser(dto);
    }

    @Test
    @DisplayName("更新用户 - 用户不存在")
    void testUpdateUser_NotFound() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(999L);

        doThrow(new ServiceException("用户不存在"))
            .when(userService).updateUser(any(UserUpdateDTO.class));

        // When & Then
        assertThatThrownBy(() -> userFacade.updateUser(dto))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("用户不存在");

        verify(userService).updateUser(dto);
    }

    // ==================== 删除用户测试 ====================

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 100L})
    @DisplayName("删除用户 - 成功")
    void testDeleteUser_Success(Long userId) {
        // Given
        doNothing().when(userService).deleteUser(userId);

        // When
        userFacade.deleteUser(userId);

        // Then
        verify(userService).deleteUser(userId);
    }

    @Test
    @DisplayName("批量删除用户 - 成功")
    void testDeleteUsers_Success() {
        // Given
        Long[] userIds = {1L, 2L, 3L};
        doNothing().when(userService).deleteUsers(any(Long[].class));

        // When
        userFacade.deleteUsers(userIds);

        // Then
        verify(userService).deleteUsers(userIds);
    }

    // ==================== 重置密码测试 ====================

    @Test
    @DisplayName("重置密码 - 成功")
    void testResetPassword_Success() {
        // Given
        Long userId = 1L;
        doNothing().when(userService).resetPassword(userId);

        // When
        userFacade.resetPassword(userId);

        // Then
        verify(userService).resetPassword(userId);
    }

    // ==================== 修改密码测试 ====================

    @Test
    @DisplayName("修改密码 - 成功")
    void testChangePassword_Success() {
        // Given
        Long userId = 1L;
        String oldPassword = "oldpass";
        String newPassword = "newpass";

        doNothing().when(userService)
            .changePassword(userId, oldPassword, newPassword);

        // When
        userFacade.changePassword(userId, oldPassword, newPassword);

        // Then
        verify(userService).changePassword(userId, oldPassword, newPassword);
    }

    // ==================== 更新状态测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 0, 正常",
            "1, 1, 停用"
    })
    @DisplayName("更新用户状态 - 成功")
    void testUpdateStatus_Success(Long userId, String status, String description) {
        // Given
        doNothing().when(userService).updateStatus(userId, status);

        // When
        userFacade.updateStatus(userId, status);

        // Then
        verify(userService).updateStatus(userId, status);
    }

    // ==================== 唯一性检查测试 ====================

    @ParameterizedTest
    @CsvSource({
            "newuser, true",
            "admin, false"
    })
    @DisplayName("检查用户名唯一性")
    void testCheckUserNameUnique(String userName, boolean expected) {
        // Given
        when(userService.checkUserNameUnique(userName)).thenReturn(expected);

        // When
        boolean result = userFacade.checkUserNameUnique(userName);

        // Then
        assertThat(result).isEqualTo(expected);
        verify(userService).checkUserNameUnique(userName);
    }

    @ParameterizedTest
    @CsvSource({
            "new@example.com, true",
            "admin@example.com, false"
    })
    @DisplayName("检查邮箱唯一性")
    void testCheckEmailUnique(String email, boolean expected) {
        // Given
        when(userService.checkEmailUnique(email)).thenReturn(expected);

        // When
        boolean result = userFacade.checkEmailUnique(email);

        // Then
        assertThat(result).isEqualTo(expected);
        verify(userService).checkEmailUnique(email);
    }

    @ParameterizedTest
    @CsvSource({
            "13800138000, true",
            "13900139000, false"
    })
    @DisplayName("检查手机号唯一性")
    void testCheckPhoneUnique(String phonenumber, boolean expected) {
        // Given
        when(userService.checkPhoneUnique(phonenumber)).thenReturn(expected);

        // When
        boolean result = userFacade.checkPhoneUnique(phonenumber);

        // Then
        assertThat(result).isEqualTo(expected);
        verify(userService).checkPhoneUnique(phonenumber);
    }

    // ==================== 辅助方法 ====================

    private UserDTO createTestUserDTO(Long userId, String userName, String nickName) {
        UserDTO dto = new UserDTO();
        dto.setUserId(userId);
        dto.setUserName(userName);
        dto.setNickName(nickName);
        dto.setEmail("admin@example.com");
        dto.setPhonenumber("13800138000");
        dto.setSex("0");
        dto.setDeptId(100L);
        dto.setStatus("0");
        dto.setCreateTime(LocalDateTime.now());
        dto.setUpdateTime(LocalDateTime.now());
        dto.setPasswordChanged(true);
        dto.setDefaultPassword(false);
        return dto;
    }

    private UserVO createTestUserVO(Long userId, String userName, String nickName) {
        return UserVO.builder()
            .userId(userId)
            .userName(userName)
            .nickName(nickName)
            .email("admin@example.com")
            .phonenumber("13800138000")
            .sex("0")
            .deptId(100L)
            .deptName("技术部")
            .status("0")
            .statusText("正常")
            .roleNames(Arrays.asList("超级管理员"))
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .passwordChanged(true)
            .defaultPassword(false)
            .build();
    }
}
