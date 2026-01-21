package com.xie.glm.framework.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CustomUserDetails 测试类
 *
 * <p>测试 Spring Security UserDetails 接口的实现
 *
 * @author xie
 */
@DisplayName("CustomUserDetails 单元测试")
class CustomUserDetailsTest {

    // ==================== 构造方法测试 ====================

    @ParameterizedTest
    @MethodSource("provideConstructorData")
    @DisplayName("构造方法 - 验证字段赋值")
    void testConstructor(Long userId, String username, String password,
                         boolean enabled, List<String> roles, List<String> permissions) {
        CustomUserDetails details = new CustomUserDetails(userId, username, password, enabled, roles, permissions);

        assertEquals(userId, details.getUserId(), "用户ID应匹配");
        assertEquals(username, details.getUsername(), "用户名应匹配");
        assertEquals(password, details.getPassword(), "密码应匹配");
        assertEquals(enabled, details.isEnabled(), "启用状态应匹配");
        assertEquals(roles, details.getRoles(), "角色列表应匹配");
        assertEquals(permissions, details.getPermissions(), "权限列表应匹配");
    }

    private static Stream<Arguments> provideConstructorData() {
        return Stream.of(
                Arguments.of(1L, "admin", "$2a$10$encoded", true,
                        List.of("admin", "common"), List.of("system:user:list", "system:user:add")),
                Arguments.of(2L, "user", "$2a$10$encoded", true,
                        List.of("common"), List.of("system:user:query")),
                Arguments.of(3L, "guest", "$2a$10$encoded", true,
                        List.of(), List.of()),
                Arguments.of(4L, "disabled", "$2a$10$encoded", false,
                        List.of("admin"), List.of("system:config:list")),
                Arguments.of(null, "test", "$2a$10$encoded", true,
                        null, null)
        );
    }

    @Test
    @DisplayName("默认构造方法 - 验证对象创建")
    void testDefaultConstructor() {
        CustomUserDetails details = new CustomUserDetails();

        assertNull(details.getUserId(), "默认用户ID应为null");
        assertNull(details.getUsername(), "默认用户名应为null");
        assertNull(details.getPassword(), "默认密码应为null");
        assertNull(details.getRoles(), "默认角色列表应为null");
        assertNull(details.getPermissions(), "默认权限列表应为null");
    }

    // ==================== getAuthorities 测试 ====================

    @ParameterizedTest
    @MethodSource("provideAuthoritiesData")
    @DisplayName("getAuthorities - 验证权限和角色转换")
    void testGetAuthorities(List<String> roles, List<String> permissions,
                            List<String> expectedAuthorities) {
        CustomUserDetails details = new CustomUserDetails(1L, "admin", "password", true, roles, permissions);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        assertEquals(expectedAuthorities.size(), authorities.size(), "权限数量应匹配");

        List<String> actualAuthorities = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertTrue(actualAuthorities.containsAll(expectedAuthorities),
                "权限列表应包含所有预期权限");
    }

    private static Stream<Arguments> provideAuthoritiesData() {
        return Stream.of(
                // 角色和权限都有
                Arguments.of(
                        List.of("admin", "common"),
                        List.of("system:user:list", "system:user:add"),
                        List.of("system:user:list", "system:user:add", "ROLE_admin", "ROLE_common")
                ),
                // 只有角色
                Arguments.of(
                        List.of("admin"),
                        List.of(),
                        List.of("ROLE_admin")
                ),
                // 只有权限
                Arguments.of(
                        List.of(),
                        List.of("system:user:list"),
                        List.of("system:user:list")
                ),
                // 都没有
                Arguments.of(
                        List.of(),
                        List.of(),
                        List.of()
                ),
                // null 角色
                Arguments.of(
                        null,
                        List.of("system:user:list"),
                        List.of("system:user:list")
                )
        );
    }

    // ==================== UserDetails 接口方法测试 ====================

    @ParameterizedTest
    @CsvSource({
            "admin, true",
            "user, true",
            "guest, true"
    })
    @DisplayName("getUsername - 验证用户名获取")
    void testGetUsername(String username, boolean expectedSuccess) {
        CustomUserDetails details = new CustomUserDetails(1L, username, "password", true, List.of(), List.of());

        assertEquals(username, details.getUsername(), "用户名应匹配");
    }

    @ParameterizedTest
    @CsvSource({
            "$2a$10$encodedPassword, true",
            "password, true",
            ", true"
    })
    @DisplayName("getPassword - 验证密码获取")
    void testGetPassword(String password, boolean expectedSuccess) {
        CustomUserDetails details = new CustomUserDetails(1L, "admin", password, true, List.of(), List.of());

        assertEquals(password, details.getPassword(), "密码应匹配");
    }

    @Test
    @DisplayName("isAccountNonExpired - 应始终返回 true")
    void testIsAccountNonExpired() {
        CustomUserDetails details = new CustomUserDetails(1L, "admin", "password", true, List.of(), List.of());

        assertTrue(details.isAccountNonExpired(), "账户未过期应始终为true");
    }

    @Test
    @DisplayName("isAccountNonLocked - 应始终返回 true")
    void testIsAccountNonLocked() {
        CustomUserDetails details = new CustomUserDetails(1L, "admin", "password", true, List.of(), List.of());

        assertTrue(details.isAccountNonLocked(), "账户未锁定应始终为true");
    }

    @Test
    @DisplayName("isCredentialsNonExpired - 应始终返回 true")
    void testIsCredentialsNonExpired() {
        CustomUserDetails details = new CustomUserDetails(1L, "admin", "password", true, List.of(), List.of());

        assertTrue(details.isCredentialsNonExpired(), "凭证未过期应始终为true");
    }

    @ParameterizedTest
    @CsvSource({
            "true, true",
            "false, false"
    })
    @DisplayName("isEnabled - 验证启用状态")
    void testIsEnabled(boolean enabled, boolean expected) {
        CustomUserDetails details = new CustomUserDetails(1L, "admin", "password", enabled, List.of(), List.of());

        assertEquals(expected, details.isEnabled(), "启用状态应匹配");
    }

    // ==================== Setter 方法测试 ====================

    @Test
    @DisplayName("Setter 方法 - 验证字段修改")
    void testSetters() {
        CustomUserDetails details = new CustomUserDetails();

        details.setUserId(100L);
        details.setUsername("testuser");
        details.setPassword("newpassword");
        details.setEnabled(true);
        details.setRoles(List.of("test"));
        details.setPermissions(List.of("test:read"));

        assertEquals(100L, details.getUserId());
        assertEquals("testuser", details.getUsername());
        assertEquals("newpassword", details.getPassword());
        assertTrue(details.isEnabled());
        assertEquals(List.of("test"), details.getRoles());
        assertEquals(List.of("test:read"), details.getPermissions());
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        CustomUserDetails details1 = new CustomUserDetails(1L, "admin", "password", true,
                List.of("admin"), List.of("system:user:list"));
        CustomUserDetails details2 = new CustomUserDetails(1L, "admin", "password", true,
                List.of("admin"), List.of("system:user:list"));
        CustomUserDetails details3 = new CustomUserDetails(2L, "user", "password", true,
                List.of("user"), List.of("system:user:query"));

        assertEquals(details1, details2, "相同字段的对象应相等");
        assertEquals(details1.hashCode(), details2.hashCode(), "相等对象的hashCode应相同");
        assertNotEquals(details1, details3, "不同字段的对象应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        CustomUserDetails details = new CustomUserDetails(1L, "admin", "password", true,
                List.of("admin"), List.of("system:user:list"));

        String str = details.toString();

        assertNotNull(str, "toString不应返回null");
        assertTrue(str.contains("admin") || str.contains("userId"), "toString应包含用户信息");
    }

    // ==================== 边界条件测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证 getAuthorities 容错")
    void testNullPermissions(List<String> permissions) {
        CustomUserDetails details = new CustomUserDetails(1L, "admin", "password", true,
                List.of("admin"), permissions);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        assertNotNull(authorities, "权限列表不应为null");
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin")),
                "应包含角色权限");
    }

    @Test
    @DisplayName("空列表处理 - 验证空权限和角色")
    void testEmptyLists() {
        CustomUserDetails details = new CustomUserDetails(1L, "admin", "password", true,
                List.of(), List.of());

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        assertNotNull(authorities, "权限列表不应为null");
        assertTrue(authorities.isEmpty(), "空列表应返回空权限集合");
    }

    // ==================== 序列化测试 ====================

    @Test
    @DisplayName("serialVersionUID - 验证序列化兼容性")
    void testSerialVersionUID() {
        // CustomUserDetails 实现了 Serializable，通过反射验证 serialVersionUID 字段
        try {
            java.lang.reflect.Field field = CustomUserDetails.class.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            assertNotNull(field.get(null), "serialVersionUID 应存在");
        } catch (NoSuchFieldException e) {
            fail("CustomUserDetails 应该有 serialVersionUID 字段");
        } catch (IllegalAccessException e) {
            fail("访问 serialVersionUID 失败");
        }
    }
}
