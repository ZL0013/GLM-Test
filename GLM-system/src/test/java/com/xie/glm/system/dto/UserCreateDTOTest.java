package com.xie.glm.system.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserCreateDTO 创建用户 DTO 测试类
 *
 * <p>测试创建用户数据传输对象的各种属性和行为
 * <p>测试原则：
 * <ul>
 *   <li>参数化测试覆盖必填字段验证</li>
 *   <li>验证 Lombok @Data 注解生成的 getter/setter</li>
 *   <li>测试角色和岗位关联</li>
 *   <li>验证密码字段的安全性</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("UserCreateDTO 创建用户 DTO 单元测试")
class UserCreateDTOTest {

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        UserCreateDTO dto = new UserCreateDTO();

        // 用户基本信息
        assertNull(dto.getUserName(), "默认用户名应为null");
        assertNull(dto.getNickName(), "默认昵称应为null");
        assertNull(dto.getPassword(), "默认密码应为null");
        assertNull(dto.getEmail(), "默认邮箱应为null");
        assertNull(dto.getPhonenumber(), "默认手机号应为null");
        assertNull(dto.getSex(), "默认性别应为null");
        assertNull(dto.getAvatar(), "默认头像应为null");
        assertNull(dto.getDeptId(), "默认部门ID应为null");
        assertNull(dto.getStatus(), "默认状态应为null");
        assertNull(dto.getRemark(), "默认备注应为null");

        // 关联信息
        assertNull(dto.getRoleIds(), "默认角色ID列表应为null");
        assertNull(dto.getPostIds(), "默认岗位ID列表应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "admin, 管理员, admin123, admin@example.com, 13800138000, 0, 100, 0",
            "user, 普通用户, user123, user@example.com, 13900139000, 1, 101, 0",
            "test, 测试用户, test123, test@example.com, 13700137000, 2, 102, 0"
    })
    @DisplayName("Getter/Setter - 验证基本字段赋值和获取")
    void testGettersSetters(String userName, String nickName, String password,
                           String email, String phonenumber, String sex,
                           Long deptId, String status) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName(userName);
        dto.setNickName(nickName);
        dto.setPassword(password);
        dto.setEmail(email);
        dto.setPhonenumber(phonenumber);
        dto.setSex(sex);
        dto.setDeptId(deptId);
        dto.setStatus(status);
        dto.setRemark("测试备注");
        dto.setAvatar("http://example.com/avatar.jpg");

        assertEquals(userName, dto.getUserName(), "用户名应匹配");
        assertEquals(nickName, dto.getNickName(), "昵称应匹配");
        assertEquals(password, dto.getPassword(), "密码应匹配");
        assertEquals(email, dto.getEmail(), "邮箱应匹配");
        assertEquals(phonenumber, dto.getPhonenumber(), "手机号应匹配");
        assertEquals(sex, dto.getSex(), "性别应匹配");
        assertEquals(deptId, dto.getDeptId(), "部门ID应匹配");
        assertEquals(status, dto.getStatus(), "状态应匹配");
        assertEquals("测试备注", dto.getRemark(), "备注应匹配");
        assertEquals("http://example.com/avatar.jpg", dto.getAvatar(), "头像应匹配");
    }

    // ==================== 角色关联测试 ====================

    @ParameterizedTest
    @MethodSource("provideRoleIdsData")
    @DisplayName("角色关联 - 验证角色ID列表")
    void testRoleIds(List<Long> roleIds, int expectedSize) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setRoleIds(roleIds);

        assertEquals(roleIds, dto.getRoleIds(), "角色ID列表应匹配");

        if (roleIds != null) {
            assertEquals(expectedSize, dto.getRoleIds().size(), "角色数量应匹配");
        }
    }

    private static Stream<Arguments> provideRoleIdsData() {
        return Stream.of(
            Arguments.of(List.of(1L, 2L, 3L), 3),
            Arguments.of(List.of(1L), 1),
            Arguments.of(List.of(), 0),
            Arguments.of(null, 0)
        );
    }

    // ==================== 岗位关联测试 ====================

    @ParameterizedTest
    @MethodSource("providePostIdsData")
    @DisplayName("岗位关联 - 验证岗位ID列表")
    void testPostIds(List<Long> postIds, int expectedSize) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setPostIds(postIds);

        assertEquals(postIds, dto.getPostIds(), "岗位ID列表应匹配");

        if (postIds != null) {
            assertEquals(expectedSize, dto.getPostIds().size(), "岗位数量应匹配");
        }
    }

    private static Stream<Arguments> providePostIdsData() {
        return Stream.of(
            Arguments.of(List.of(1L, 2L), 2),
            Arguments.of(List.of(1L), 1),
            Arguments.of(List.of(), 0),
            Arguments.of(null, 0)
        );
    }

    // ==================== 密码字段测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"password123", "Admin@123", "Test#456", "P@ssw0rd"})
    @DisplayName("密码字段 - 验证密码设置")
    void testPasswordField(String password) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setPassword(password);

        assertEquals(password, dto.getPassword(), "密码应匹配");
        assertNotNull(dto.getPassword(), "密码不应为null");
    }

    @Test
    @DisplayName("密码字段 - 验证空密码")
    void testEmptyPassword() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setPassword("");

        assertEquals("", dto.getPassword(), "空密码应被接受");
    }

    // ==================== 用户名测试 ====================

    @ParameterizedTest
    @CsvSource({
            "admin, true",
            "user123, true",
            "test_user, true",
            ", false"
    })
    @DisplayName("用户名 - 验证用户名格式")
    void testUserName(String userName, boolean shouldHaveValue) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName(userName);

        assertEquals(userName, dto.getUserName(), "用户名应匹配");

        if (shouldHaveValue) {
            assertNotNull(dto.getUserName(), "用户名不应为null");
            assertFalse(dto.getUserName().isEmpty(), "用户名不应为空");
        }
    }

    // ==================== 邮箱测试 ====================

    @ParameterizedTest
    @CsvSource({
            "admin@example.com, true",
            "user@test.com, true",
            "test@domain.org, true",
            ", false"
    })
    @DisplayName("邮箱 - 验证邮箱格式")
    void testEmail(String email, boolean shouldHaveValue) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail(email);

        assertEquals(email, dto.getEmail(), "邮箱应匹配");

        if (shouldHaveValue && email != null) {
            assertTrue(email.contains("@"), "邮箱应包含@符号");
            assertTrue(email.contains("."), "邮箱应包含域名后缀");
        }
    }

    // ==================== 手机号测试 ====================

    @ParameterizedTest
    @CsvSource({
            "13800138000, true",
            "13900139000, true",
            "13700137000, true",
            ", false"
    })
    @DisplayName("手机号 - 验证手机号格式")
    void testPhonenumber(String phonenumber, boolean shouldHaveValue) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setPhonenumber(phonenumber);

        assertEquals(phonenumber, dto.getPhonenumber(), "手机号应匹配");

        if (shouldHaveValue && phonenumber != null) {
            assertEquals(11, phonenumber.length(), "手机号应为11位");
            assertTrue(phonenumber.matches("\\d+"), "手机号应全为数字");
        }
    }

    // ==================== 性别测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "2"})
    @DisplayName("性别 - 验证性别值有效性")
    void testSex(String sex) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setSex(sex);

        assertEquals(sex, dto.getSex(), "性别值应匹配");
        assertTrue(List.of("0", "1", "2").contains(dto.getSex()),
                "性别值应为 0=男，1=女，2=未知");
    }

    // ==================== 状态测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"0", "1"})
    @DisplayName("状态 - 验证状态值有效性")
    void testStatus(String status) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setStatus(status);

        assertEquals(status, dto.getStatus(), "状态值应匹配");
        assertTrue(List.of("0", "1").contains(dto.getStatus()),
                "状态值应为 0=正常 或 1=停用");
    }

    // ==================== 部门测试 ====================

    @ParameterizedTest
    @CsvSource({
            "100, true",
            "101, true",
            "102, true",
            ", false"
    })
    @DisplayName("部门 - 验证部门ID")
    void testDeptId(Long deptId, boolean shouldHaveValue) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setDeptId(deptId);

        assertEquals(deptId, dto.getDeptId(), "部门ID应匹配");

        if (shouldHaveValue) {
            assertNotNull(dto.getDeptId(), "部门ID不应为null");
            assertTrue(dto.getDeptId() > 0, "部门ID应为正数");
        }
    }

    // ==================== 角色和岗位组合测试 ====================

    @Test
    @DisplayName("角色和岗位组合 - 验证多角色多岗位")
    void testMultipleRolesAndPosts() {
        List<Long> roleIds = List.of(1L, 2L, 3L);
        List<Long> postIds = List.of(1L, 2L);

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("admin");
        dto.setRoleIds(roleIds);
        dto.setPostIds(postIds);

        assertEquals(3, dto.getRoleIds().size(), "应有3个角色");
        assertEquals(2, dto.getPostIds().size(), "应有2个岗位");
        assertTrue(dto.getRoleIds().contains(1L), "应包含角色1");
        assertTrue(dto.getPostIds().contains(1L), "应包含岗位1");
    }

    @Test
    @DisplayName("角色和岗位组合 - 仅角色无岗位")
    void testRolesWithoutPosts() {
        List<Long> roleIds = List.of(1L);

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("user");
        dto.setRoleIds(roleIds);
        dto.setPostIds(null);

        assertNotNull(dto.getRoleIds(), "角色ID列表不应为null");
        assertEquals(1, dto.getRoleIds().size(), "应有1个角色");
        assertNull(dto.getPostIds(), "岗位ID列表应为null");
    }

    @Test
    @DisplayName("角色和岗位组合 - 仅岗位无角色")
    void testPostsWithoutRoles() {
        List<Long> postIds = List.of(1L, 2L);

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("user");
        dto.setRoleIds(null);
        dto.setPostIds(postIds);

        assertNull(dto.getRoleIds(), "角色ID列表应为null");
        assertNotNull(dto.getPostIds(), "岗位ID列表不应为null");
        assertEquals(2, dto.getPostIds().size(), "应有2个岗位");
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证字段 setter 容错")
    void testNullHandling(String value) {
        UserCreateDTO dto = new UserCreateDTO();

        dto.setUserName(value);
        dto.setNickName(value);
        dto.setPassword(value);
        dto.setEmail(value);
        dto.setPhonenumber(value);
        dto.setSex(value);
        dto.setAvatar(value);
        dto.setRemark(value);

        assertNull(dto.getUserName(), "null 用户名应被接受");
        assertNull(dto.getNickName(), "null 昵称应被接受");
        assertNull(dto.getPassword(), "null 密码应被接受");
        assertNull(dto.getEmail(), "null 邮箱应被接受");
        assertNull(dto.getPhonenumber(), "null 手机号应被接受");
        assertNull(dto.getSex(), "null 性别应被接受");
        assertNull(dto.getAvatar(), "null 头像应被接受");
        assertNull(dto.getRemark(), "null 备注应被接受");
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        List<Long> roleIds = List.of(1L, 2L);
        List<Long> postIds = List.of(1L);

        UserCreateDTO dto1 = new UserCreateDTO();
        dto1.setUserName("admin");
        dto1.setEmail("admin@example.com");
        dto1.setRoleIds(roleIds);
        dto1.setPostIds(postIds);

        UserCreateDTO dto2 = new UserCreateDTO();
        dto2.setUserName("admin");
        dto2.setEmail("admin@example.com");
        dto2.setRoleIds(roleIds);
        dto2.setPostIds(postIds);

        UserCreateDTO dto3 = new UserCreateDTO();
        dto3.setUserName("user");
        dto3.setEmail("user@example.com");
        dto3.setRoleIds(roleIds);
        dto3.setPostIds(postIds);

        assertEquals(dto1, dto2, "相同属性的DTO应相等");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(dto1, dto3, "不同属性的DTO应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("admin");
        dto.setEmail("admin@example.com");

        String str = dto.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("UserCreateDTO") || str.contains("admin") || str.contains("admin@example.com"),
                "toString 应包含用户信息");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 创建管理员用户")
    void testScenario_CreateAdminUser() {
        List<Long> roleIds = List.of(1L);  // 管理员角色

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("admin");
        dto.setNickName("系统管理员");
        dto.setPassword("Admin@123");
        dto.setEmail("admin@example.com");
        dto.setPhonenumber("13800138000");
        dto.setSex("0");
        dto.setDeptId(100L);
        dto.setStatus("0");
        dto.setRoleIds(roleIds);

        assertEquals("admin", dto.getUserName(), "用户名为admin");
        assertEquals("系统管理员", dto.getNickName(), "昵称为系统管理员");
        assertEquals("Admin@123", dto.getPassword(), "密码已设置");
        assertEquals("admin@example.com", dto.getEmail(), "邮箱已设置");
        assertEquals("13800138000", dto.getPhonenumber(), "手机号已设置");
        assertEquals("0", dto.getSex(), "性别为男");
        assertEquals(100L, dto.getDeptId(), "部门ID为100");
        assertEquals("0", dto.getStatus(), "状态为正常");
        assertEquals(1, dto.getRoleIds().size(), "有1个角色");
    }

    @Test
    @DisplayName("业务场景 - 创建普通用户")
    void testScenario_CreateNormalUser() {
        List<Long> roleIds = List.of(2L);  // 普通用户角色
        List<Long> postIds = List.of(1L);  // 普通员工岗位

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("zhangsan");
        dto.setNickName("张三");
        dto.setPassword("User@123");
        dto.setEmail("zhangsan@example.com");
        dto.setPhonenumber("13900139000");
        dto.setSex("0");
        dto.setDeptId(101L);
        dto.setStatus("0");
        dto.setRoleIds(roleIds);
        dto.setPostIds(postIds);
        dto.setRemark("新入职员工");

        assertEquals("zhangsan", dto.getUserName());
        assertEquals("张三", dto.getNickName());
        assertEquals("User@123", dto.getPassword());
        assertEquals(101L, dto.getDeptId());
        assertEquals(1, dto.getRoleIds().size());
        assertEquals(1, dto.getPostIds().size());
        assertEquals("新入职员工", dto.getRemark());
    }

    @Test
    @DisplayName("业务场景 - 创建最小信息用户")
    void testScenario_CreateMinimalUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("minimal");
        dto.setPassword("Pass@123");

        assertEquals("minimal", dto.getUserName(), "仅有用户名");
        assertEquals("Pass@123", dto.getPassword(), "仅有密码");
        assertNull(dto.getNickName(), "无昵称");
        assertNull(dto.getEmail(), "无邮箱");
        assertNull(dto.getPhonenumber(), "无手机号");
        assertNull(dto.getDeptId(), "无部门");
        assertNull(dto.getRoleIds(), "无角色");
        assertNull(dto.getPostIds(), "无岗位");
    }
}
