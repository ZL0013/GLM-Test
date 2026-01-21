package com.xie.glm.system.security;

import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.framework.security.CustomUserDetails;
import com.xie.glm.system.domain.SysUser;
import com.xie.glm.system.mapper.SysUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

/**
 * UserDetailsService 实现类测试
 *
 * <p>测试场景：
 * <ul>
 *   <li>正常加载用户详情</li>
 *   <li>用户不存在抛出 ServiceException 并转换为 UsernameNotFoundException</li>
 *   <li>用户已停用抛出 ServiceException 并转换为 UsernameNotFoundException</li>
 *   <li>用户已删除抛出 ServiceException 并转换为 UsernameNotFoundException</li>
 *   <li>正确加载用户角色和权限</li>
 *   <li>数据库异常使用异常链传递</li>
 * </ul>
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImpl 测试")
class UserDetailsServiceImplTest {

    @Mock
    private SysUserMapper userMapper;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private SysUser normalUser;
    private SysUser disabledUser;
    private SysUser deletedUser;

    @BeforeEach
    void setUp() {
        // 正常用户
        normalUser = new SysUser();
        normalUser.setUserId(1L);
        normalUser.setUserName("admin");
        normalUser.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH");
        normalUser.setStatus("0");
        normalUser.setDelFlag("0");

        // 停用用户
        disabledUser = new SysUser();
        disabledUser.setUserId(2L);
        disabledUser.setUserName("disabled");
        disabledUser.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH");
        disabledUser.setStatus("1");
        disabledUser.setDelFlag("0");

        // 已删除用户
        deletedUser = new SysUser();
        deletedUser.setUserId(3L);
        deletedUser.setUserName("deleted");
        deletedUser.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH");
        deletedUser.setStatus("0");
        deletedUser.setDelFlag("1");
    }

    @ParameterizedTest
    @MethodSource("provideUserLoadData")
    @DisplayName("加载用户详情 - 参数化测试")
    void testLoadUserByUsername(String username, SysUser userFromDb,
                                 List<String> roles, List<String> permissions,
                                 boolean shouldSucceed, String expectedMessage) {
        // Arrange
        lenient().when(userMapper.selectUserByName(anyString())).thenReturn(userFromDb);
        lenient().when(userMapper.selectRolesByUserId(anyLong())).thenReturn(roles);
        lenient().when(userMapper.selectPermsByUserId(anyLong())).thenReturn(permissions);

        // Act & Assert
        if (shouldSucceed) {
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

            assertThat(userDetails).isNotNull();
            assertThat(userDetails.getUserId()).isEqualTo(userFromDb.getUserId());
            assertThat(userDetails.getUsername()).isEqualTo(username);
            assertThat(userDetails.getPassword()).isEqualTo(userFromDb.getPassword());
            assertThat(userDetails.isEnabled()).isTrue();
            assertThat(userDetails.getRoles()).isEqualTo(roles);
            assertThat(userDetails.getPermissions()).isEqualTo(permissions);

            verify(userMapper).selectUserByName(username);
            verify(userMapper).selectRolesByUserId(userFromDb.getUserId());
            verify(userMapper).selectPermsByUserId(userFromDb.getUserId());
        } else {
            assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessageContaining(expectedMessage);
        }
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("加载用户详情 - 用户名为 null")
    void testLoadUserByUsernameWithNull(String username) {
        // Arrange - 使用 isNull() 匹配 null 值
        when(userMapper.selectUserByName(isNull())).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("用户不存在");
    }

    @Test
    @DisplayName("加载用户详情 - 用户已停用应抛出异常")
    void testLoadUserByUsernameWhenDisabled() {
        // Arrange
        when(userMapper.selectUserByName("disabled")).thenReturn(disabledUser);

        // Act & Assert
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("disabled"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("账号已禁用");
    }

    @Test
    @DisplayName("加载用户详情 - 用户已删除应抛出异常")
    void testLoadUserByUsernameWhenDeleted() {
        // Arrange
        when(userMapper.selectUserByName("deleted")).thenReturn(deletedUser);

        // Act & Assert
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("deleted"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("用户不存在");
    }

    @Test
    @DisplayName("加载用户详情 - ServiceException 应转换为 UsernameNotFoundException")
    void testServiceExceptionConversion() {
        // Arrange - 模拟用户不存在的情况
        when(userMapper.selectUserByName("unknown")).thenReturn(null);

        // Act & Assert - 内部 ServiceException 应被转换为 UsernameNotFoundException
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(BusinessStatus.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("加载用户详情 - 数据库异常应使用异常链")
    void testLoadUserByUsernameWhenMapperThrowsException() {
        // Arrange
        RuntimeException dbException = new RuntimeException("数据库连接失败");
        when(userMapper.selectUserByName(anyString())).thenThrow(dbException);

        // Act & Assert - 异常应被包装在 UsernameNotFoundException 中，保留原始异常
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("admin"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasCause(dbException);
    }

    private static Stream<Arguments> provideUserLoadData() {
        return Stream.of(
                // 正常用户，有角色和权限
                Arguments.of(
                        "admin",
                        createNormalUser(1L, "admin"),
                        List.of("admin", "common"),
                        List.of("system:user:list", "system:user:add", "system:role:list"),
                        true,
                        null
                ),
                // 正常用户，无角色
                Arguments.of(
                        "user1",
                        createNormalUser(2L, "user1"),
                        List.of(),
                        List.of("system:user:list"),
                        true,
                        null
                ),
                // 正常用户，无权限
                Arguments.of(
                        "user2",
                        createNormalUser(3L, "user2"),
                        List.of("common"),
                        List.of(),
                        true,
                        null
                ),
                // 用户不存在
                Arguments.of(
                        "unknown",
                        null,
                        null,
                        null,
                        false,
                        "用户不存在"
                )
        );
    }

    private static SysUser createNormalUser(Long userId, String username) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setUserName(username);
        user.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH");
        user.setStatus("0");
        user.setDelFlag("0");
        return user;
    }
}
