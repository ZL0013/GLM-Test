package com.xie.glm.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.framework.config.UserSecurityProperties;
import com.xie.glm.system.converter.UserConverter;
import com.xie.glm.system.domain.SysUser;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import com.xie.glm.system.dto.query.UserQueryDTO;
import com.xie.glm.system.mapper.SysUserMapper;
import com.xie.glm.system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.*;

/**
 * UserService 用户服务测试类
 *
 * <p>使用 Mockito 进行单元测试，测试用户服务的业务逻辑。
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 用户服务单元测试")
class UserServiceTest {

    @Mock
    private SysUserMapper userMapper;

    @Mock
    private UserConverter userConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserSecurityProperties userSecurityProperties;

    @InjectMocks
    private UserServiceImpl userService;

    private SysUser testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testUser = createTestUser(1L, "admin", "管理员", "admin@example.com");
        testUserDTO = createUserDTO(1L, "admin", "管理员", "admin@example.com");
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询用户 - 成功")
    void testListUsers_Success() {
        // 准备测试数据
        UserQueryDTO query = new UserQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setUserName("admin");
        query.setStatus("0");

        Page<SysUser> pageResult = new Page<>(1, 10);
        pageResult.setRecords(Arrays.asList(testUser));
        pageResult.setTotal(1);

        when(userMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(pageResult);
        when(userConverter.toDtoList(anyList()))
            .thenReturn(Arrays.asList(testUserDTO));

        // 执行测试
        PageResult<UserDTO> result = userService.listUsers(query);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(1, result.getTotal());
        assertEquals("admin", result.getRecords().get(0).getUserName());

        verify(userMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
        verify(userConverter).toDtoList(anyList());
    }

    @ParameterizedTest
    @MethodSource("providePaginationParams")
    @DisplayName("分页查询用户 - 参数化测试")
    void testListUsers_Parameterized(Integer pageNum, Integer pageSize, String status) {
        UserQueryDTO query = new UserQueryDTO();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        query.setStatus(status);

        Page<SysUser> pageResult = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        pageResult.setRecords(Arrays.asList());

        when(userMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(pageResult);

        PageResult<UserDTO> result = userService.listUsers(query);

        assertNotNull(result);
        verify(userMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    private static Stream<Arguments> providePaginationParams() {
        return Stream.of(
            Arguments.of(1, 10, "0"),
            Arguments.of(2, 20, "1"),
            Arguments.of(null, null, "0"),
            Arguments.of(1, 50, null)
        );
    }

    // ==================== 根据 ID 查询用户测试 ====================

    @Test
    @DisplayName("根据 ID 查询用户 - 成功")
    void testGetUserById_Success() {
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userConverter.toDto(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("admin", result.getUserName());
        verify(userMapper).selectById(1L);
        verify(userConverter).toDto(testUser);
    }

    @Test
    @DisplayName("根据 ID 查询用户 - 用户不存在")
    void testGetUserById_NotFound() {
        when(userMapper.selectById(999L)).thenReturn(null);

        assertThrows(ServiceException.class, () -> userService.getUserById(999L));
        verify(userMapper).selectById(999L);
        verify(userConverter, never()).toDto(any(SysUser.class));
    }

    @Test
    @DisplayName("根据 ID 查询用户 - 用户已删除")
    void testGetUserById_Deleted() {
        SysUser deletedUser = createTestUser(1L, "deleted", "已删除", "deleted@example.com");
        deletedUser.setDelFlag("2");

        when(userMapper.selectById(1L)).thenReturn(deletedUser);

        assertThrows(ServiceException.class, () -> userService.getUserById(1L));
    }

    // ==================== 根据用户名查询测试 ====================

    @Test
    @DisplayName("根据用户名查询 - 成功")
    void testGetUserByUserName_Success() {
        when(userMapper.selectUserByName("admin")).thenReturn(testUser);
        when(userConverter.toDto(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.getUserByUserName("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUserName());
        verify(userMapper).selectUserByName("admin");
    }

    @Test
    @DisplayName("根据用户名查询 - 用户不存在")
    void testGetUserByUserName_NotFound() {
        when(userMapper.selectUserByName("nonexistent")).thenReturn(null);

        UserDTO result = userService.getUserByUserName("nonexistent");

        assertNull(result);
        verify(userMapper).selectUserByName("nonexistent");
    }

    // ==================== 创建用户测试 ====================

    @Test
    @DisplayName("创建用户 - 成功")
    void testCreateUser_Success() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("newuser");
        dto.setNickName("新用户");
        dto.setPassword("Password@123");
        dto.setEmail("newuser@example.com");

        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(userConverter.createDtoToEntity(dto)).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encoded");
        when(userMapper.insert(any(SysUser.class))).thenReturn(1);

        Long userId = userService.createUser(dto);

        assertNotNull(userId);
        verify(userMapper, times(2)).selectCount(any(LambdaQueryWrapper.class));
        verify(passwordEncoder).encode("Password@123");
        verify(userMapper).insert(any(SysUser.class));
    }

    @Test
    @DisplayName("创建用户 - 用户名已存在")
    void testCreateUser_UserNameExists() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("admin");
        dto.setNickName("管理员");
        dto.setPassword("Password@123");

        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        assertThrows(ServiceException.class, () -> userService.createUser(dto),
            "用户名已存在");
        verify(userMapper).selectCount(any(LambdaQueryWrapper.class));
        verify(userMapper, never()).insert(any(SysUser.class));
    }

    @Test
    @DisplayName("创建用户 - 邮箱已被使用")
    void testCreateUser_EmailExists() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("newuser");
        dto.setEmail("admin@example.com");

        when(userMapper.selectCount(any(LambdaQueryWrapper.class)))
            .thenReturn(0L)  // 用户名唯一
            .thenReturn(1L); // 邮箱已存在

        assertThrows(ServiceException.class, () -> userService.createUser(dto),
            "邮箱已被使用");
    }

    // ==================== 更新用户测试 ====================

    @Test
    @DisplayName("更新用户 - 成功")
    void testUpdateUser_Success() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setNickName("新昵称");
        dto.setEmail("new@example.com");

        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(userConverter.updateDtoToEntity(dto)).thenReturn(testUser);
        when(userMapper.updateById(any(SysUser.class))).thenReturn(1);

        userService.updateUser(dto);

        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(SysUser.class));
    }

    @Test
    @DisplayName("更新用户 - 用户不存在")
    void testUpdateUser_NotFound() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(999L);

        when(userMapper.selectById(999L)).thenReturn(null);

        assertThrows(ServiceException.class, () -> userService.updateUser(dto));
        verify(userMapper).selectById(999L);
        verify(userMapper, never()).updateById(any(SysUser.class));
    }

    // ==================== 删除用户测试 ====================

    @Test
    @DisplayName("删除用户 - 成功")
    void testDeleteUser_Success() {
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(SysUser.class))).thenReturn(1);

        userService.deleteUser(1L);

        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(ArgumentMatchers.<SysUser>argThat(user -> "2".equals(user.getDelFlag())));
    }

    @Test
    @DisplayName("删除用户 - 用户不存在")
    void testDeleteUser_NotFound() {
        when(userMapper.selectById(999L)).thenReturn(null);

        assertThrows(ServiceException.class, () -> userService.deleteUser(999L));
        verify(userMapper).selectById(999L);
        verify(userMapper, never()).updateById(any(SysUser.class));
    }

    @Test
    @DisplayName("批量删除用户 - 成功")
    void testDeleteUsers_Success() {
        Long[] userIds = {1L, 2L, 3L};

        when(userMapper.selectById(anyLong())).thenReturn(testUser);
        when(userMapper.updateById(any(SysUser.class))).thenReturn(1);

        userService.deleteUsers(userIds);

        verify(userMapper, times(3)).selectById(anyLong());
        verify(userMapper, times(3)).updateById(any(SysUser.class));
    }

    // ==================== 重置密码测试 ====================

    @Test
    @DisplayName("重置密码 - 成功")
    void testResetPassword_Success() {
        when(userSecurityProperties.getDefaultPassword()).thenReturn("123456");
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$encoded");
        when(userMapper.updateById(any(SysUser.class))).thenReturn(1);

        userService.resetPassword(1L);

        verify(userMapper).selectById(1L);
        verify(passwordEncoder).encode("123456");
        verify(userMapper).updateById(ArgumentMatchers.<SysUser>argThat(user ->
            "$2a$10$encoded".equals(user.getPassword()) &&
            Boolean.FALSE.equals(user.getPasswordChanged()) &&
            Boolean.TRUE.equals(user.getDefaultPassword())
        ));
    }

    // ==================== 修改密码测试 ====================

    @Test
    @DisplayName("修改密码 - 成功")
    void testChangePassword_Success() {
        testUser.setPassword("$2a$10$oldencoded");

        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(passwordEncoder.matches("oldpass", "$2a$10$oldencoded")).thenReturn(true);
        when(passwordEncoder.encode("newpass")).thenReturn("$2a$10$newencoded");
        when(userMapper.updateById(any(SysUser.class))).thenReturn(1);

        userService.changePassword(1L, "oldpass", "newpass");

        verify(passwordEncoder).matches("oldpass", "$2a$10$oldencoded");
        verify(passwordEncoder).encode("newpass");
        verify(userMapper).updateById(ArgumentMatchers.<SysUser>argThat(user ->
            "$2a$10$newencoded".equals(user.getPassword()) &&
            Boolean.TRUE.equals(user.getPasswordChanged())
        ));
    }

    @Test
    @DisplayName("修改密码 - 旧密码错误")
    void testChangePassword_WrongOldPassword() {
        testUser.setPassword("$2a$10$oldencoded");

        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(passwordEncoder.matches("wrongpass", "$2a$10$oldencoded")).thenReturn(false);

        assertThrows(ServiceException.class, () -> userService.changePassword(1L, "wrongpass", "newpass"),
            "旧密码错误");
        verify(passwordEncoder).matches("wrongpass", "$2a$10$oldencoded");
        verify(passwordEncoder, never()).encode(anyString());
    }

    // ==================== 更新状态测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, 正常",
            "1, 停用"
    })
    @DisplayName("更新用户状态 - 成功")
    void testUpdateStatus_Success(String status, String description) {
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(SysUser.class))).thenReturn(1);

        userService.updateStatus(1L, status);

        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(ArgumentMatchers.<SysUser>argThat(user -> status.equals(user.getStatus())));
    }

    // ==================== 唯一性检查测试 ====================

    @ParameterizedTest
    @CsvSource({
            "newuser, true",
            "admin, false"
    })
    @DisplayName("检查用户名唯一性")
    void testCheckUserNameUnique(String userName, boolean expected) {
        when(userMapper.selectCount(any(LambdaQueryWrapper.class)))
            .thenReturn(expected ? 0L : 1L);

        boolean result = userService.checkUserNameUnique(userName);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "new@example.com, true",
            "admin@example.com, false"
    })
    @DisplayName("检查邮箱唯一性")
    void testCheckEmailUnique(String email, boolean expected) {
        when(userMapper.selectCount(any(LambdaQueryWrapper.class)))
            .thenReturn(expected ? 0L : 1L);

        boolean result = userService.checkEmailUnique(email);

        assertEquals(expected, result);
    }

    // ==================== 辅助方法 ====================

    private SysUser createTestUser(Long userId, String userName, String nickName, String email) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setEmail(email);
        user.setPhonenumber("13800138000");
        user.setSex("0");
        user.setDeptId(100L);
        user.setStatus("0");
        user.setDelFlag("0");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setPasswordChanged(true);
        user.setDefaultPassword(false);
        return user;
    }

    private UserDTO createUserDTO(Long userId, String userName, String nickName, String email) {
        UserDTO dto = new UserDTO();
        dto.setUserId(userId);
        dto.setUserName(userName);
        dto.setNickName(nickName);
        dto.setEmail(email);
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
}
