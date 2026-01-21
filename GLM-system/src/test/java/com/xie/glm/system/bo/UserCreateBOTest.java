package com.xie.glm.system.bo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserCreateBO 创建用户业务对象测试类
 *
 * <p>测试创建用户的业务对象，用于 Service 层内部组合多实体数据。
 * <p>测试原则：
 * <ul>
 *   <li>参数化测试覆盖字段验证</li>
 *   <li>验证 Lombok @Data 注解生成的 getter/setter</li>
 *   <li>测试角色和岗位关联组合</li>
 *   <li>验证业务逻辑字段</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("UserCreateBO 创建用户业务对象单元测试")
class UserCreateBOTest {

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        UserCreateBO bo = new UserCreateBO();

        // 用户基本信息
        assertNull(bo.getUserName(), "默认用户名应为null");
        assertNull(bo.getNickName(), "默认昵称应为null");
        assertNull(bo.getPassword(), "默认密码应为null");
        assertNull(bo.getEmail(), "默认邮箱应为null");
        assertNull(bo.getPhonenumber(), "默认手机号应为null");
        assertNull(bo.getSex(), "默认性别应为null");
        assertNull(bo.getAvatar(), "默认头像应为null");
        assertNull(bo.getDeptId(), "默认部门ID应为null");
        assertNull(bo.getStatus(), "默认状态应为null");
        assertNull(bo.getRemark(), "默认备注应为null");

        // 关联信息
        assertNull(bo.getRoleIds(), "默认角色ID列表应为null");
        assertNull(bo.getPostIds(), "默认岗位ID列表应为null");
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
        UserCreateBO bo = new UserCreateBO();
        bo.setUserName(userName);
        bo.setNickName(nickName);
        bo.setPassword(password);
        bo.setEmail(email);
        bo.setPhonenumber(phonenumber);
        bo.setSex(sex);
        bo.setDeptId(deptId);
        bo.setStatus(status);
        bo.setRemark("测试备注");
        bo.setAvatar("http://example.com/avatar.jpg");

        assertEquals(userName, bo.getUserName(), "用户名应匹配");
        assertEquals(nickName, bo.getNickName(), "昵称应匹配");
        assertEquals(password, bo.getPassword(), "密码应匹配");
        assertEquals(email, bo.getEmail(), "邮箱应匹配");
        assertEquals(phonenumber, bo.getPhonenumber(), "手机号应匹配");
        assertEquals(sex, bo.getSex(), "性别应匹配");
        assertEquals(deptId, bo.getDeptId(), "部门ID应匹配");
        assertEquals(status, bo.getStatus(), "状态应匹配");
        assertEquals("测试备注", bo.getRemark(), "备注应匹配");
        assertEquals("http://example.com/avatar.jpg", bo.getAvatar(), "头像应匹配");
    }

    // ==================== 角色关联测试 ====================

    @ParameterizedTest
    @MethodSource("provideRoleIdsData")
    @DisplayName("角色关联 - 验证角色ID列表")
    void testRoleIds(List<Long> roleIds, int expectedSize) {
        UserCreateBO bo = new UserCreateBO();
        bo.setRoleIds(roleIds);

        assertEquals(roleIds, bo.getRoleIds(), "角色ID列表应匹配");

        if (roleIds != null) {
            assertEquals(expectedSize, bo.getRoleIds().size(), "角色数量应匹配");
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
        UserCreateBO bo = new UserCreateBO();
        bo.setPostIds(postIds);

        assertEquals(postIds, bo.getPostIds(), "岗位ID列表应匹配");

        if (postIds != null) {
            assertEquals(expectedSize, bo.getPostIds().size(), "岗位数量应匹配");
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
    @CsvSource({
            "password123, true",
            "Admin@123, true",
            "Test#456, true",
            ", false"
    })
    @DisplayName("密码字段 - 验证密码设置")
    void testPasswordField(String password, boolean shouldHaveValue) {
        UserCreateBO bo = new UserCreateBO();
        bo.setPassword(password);

        assertEquals(password, bo.getPassword(), "密码应匹配");

        if (shouldHaveValue) {
            assertNotNull(bo.getPassword(), "密码不应为null");
            assertFalse(bo.getPassword().isEmpty(), "密码不应为空");
        }
    }

    // ==================== 用户名测试 ====================

    @ParameterizedTest
    @CsvSource({
            "admin, true",
            "user123, true",
            "test_user, true"
    })
    @DisplayName("用户名 - 验证用户名格式")
    void testUserName(String userName, boolean isValid) {
        UserCreateBO bo = new UserCreateBO();
        bo.setUserName(userName);

        assertEquals(userName, bo.getUserName(), "用户名应匹配");

        if (isValid) {
            assertNotNull(bo.getUserName(), "用户名不应为null");
            assertFalse(bo.getUserName().isEmpty(), "用户名不应为空");
        }
    }

    // ==================== 邮箱测试 ====================

    @ParameterizedTest
    @CsvSource({
            "admin@example.com, true",
            "user@test.com, true",
            ", false"
    })
    @DisplayName("邮箱 - 验证邮箱格式")
    void testEmail(String email, boolean shouldHaveValue) {
        UserCreateBO bo = new UserCreateBO();
        bo.setEmail(email);

        assertEquals(email, bo.getEmail(), "邮箱应匹配");

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
            ", false"
    })
    @DisplayName("手机号 - 验证手机号格式")
    void testPhonenumber(String phonenumber, boolean shouldHaveValue) {
        UserCreateBO bo = new UserCreateBO();
        bo.setPhonenumber(phonenumber);

        assertEquals(phonenumber, bo.getPhonenumber(), "手机号应匹配");

        if (shouldHaveValue && phonenumber != null) {
            assertEquals(11, phonenumber.length(), "手机号应为11位");
            assertTrue(phonenumber.matches("\\d+"), "手机号应全为数字");
        }
    }

    // ==================== 性别测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, 男",
            "1, 女",
            "2, 未知"
    })
    @DisplayName("性别 - 验证性别值")
    void testSex(String sex, String description) {
        UserCreateBO bo = new UserCreateBO();
        bo.setSex(sex);

        assertEquals(sex, bo.getSex(), description + "性别值应匹配");
    }

    // ==================== 状态测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, 正常",
            "1, 停用"
    })
    @DisplayName("状态 - 验证状态值")
    void testStatus(String status, String description) {
        UserCreateBO bo = new UserCreateBO();
        bo.setStatus(status);

        assertEquals(status, bo.getStatus(), description + "状态值应匹配");
    }

    // ==================== 部门测试 ====================

    @ParameterizedTest
    @CsvSource({
            "100, true",
            "101, true",
            ", false"
    })
    @DisplayName("部门 - 验证部门ID")
    void testDeptId(Long deptId, boolean shouldHaveValue) {
        UserCreateBO bo = new UserCreateBO();
        bo.setDeptId(deptId);

        assertEquals(deptId, bo.getDeptId(), "部门ID应匹配");

        if (shouldHaveValue) {
            assertNotNull(bo.getDeptId(), "部门ID不应为null");
            assertTrue(bo.getDeptId() > 0, "部门ID应为正数");
        }
    }

    // ==================== 角色和岗位组合测试 ====================

    @Test
    @DisplayName("角色和岗位组合 - 验证多角色多岗位")
    void testMultipleRolesAndPosts() {
        List<Long> roleIds = List.of(1L, 2L, 3L);
        List<Long> postIds = List.of(1L, 2L);

        UserCreateBO bo = new UserCreateBO();
        bo.setUserName("admin");
        bo.setRoleIds(roleIds);
        bo.setPostIds(postIds);

        assertEquals(3, bo.getRoleIds().size(), "应有3个角色");
        assertEquals(2, bo.getPostIds().size(), "应有2个岗位");
        assertTrue(bo.getRoleIds().contains(1L), "应包含角色1");
        assertTrue(bo.getPostIds().contains(1L), "应包含岗位1");
    }

    @Test
    @DisplayName("角色和岗位组合 - 仅角色无岗位")
    void testRolesWithoutPosts() {
        List<Long> roleIds = List.of(1L);

        UserCreateBO bo = new UserCreateBO();
        bo.setUserName("user");
        bo.setRoleIds(roleIds);
        bo.setPostIds(null);

        assertNotNull(bo.getRoleIds(), "角色ID列表不应为null");
        assertEquals(1, bo.getRoleIds().size(), "应有1个角色");
        assertNull(bo.getPostIds(), "岗位ID列表应为null");
    }

    @Test
    @DisplayName("角色和岗位组合 - 仅岗位无角色")
    void testPostsWithoutRoles() {
        List<Long> postIds = List.of(1L, 2L);

        UserCreateBO bo = new UserCreateBO();
        bo.setUserName("user");
        bo.setRoleIds(null);
        bo.setPostIds(postIds);

        assertNull(bo.getRoleIds(), "角色ID列表应为null");
        assertNotNull(bo.getPostIds(), "岗位ID列表不应为null");
        assertEquals(2, bo.getPostIds().size(), "应有2个岗位");
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证字段 setter 容错")
    void testNullHandling(String value) {
        UserCreateBO bo = new UserCreateBO();

        bo.setUserName(value);
        bo.setNickName(value);
        bo.setPassword(value);
        bo.setEmail(value);
        bo.setPhonenumber(value);
        bo.setSex(value);
        bo.setAvatar(value);
        bo.setRemark(value);

        assertNull(bo.getUserName(), "null 用户名应被接受");
        assertNull(bo.getNickName(), "null 昵称应被接受");
        assertNull(bo.getPassword(), "null 密码应被接受");
        assertNull(bo.getEmail(), "null 邮箱应被接受");
        assertNull(bo.getPhonenumber(), "null 手机号应被接受");
        assertNull(bo.getSex(), "null 性别应被接受");
        assertNull(bo.getAvatar(), "null 头像应被接受");
        assertNull(bo.getRemark(), "null 备注应被接受");
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        List<Long> roleIds = List.of(1L, 2L);
        List<Long> postIds = List.of(1L);

        UserCreateBO bo1 = new UserCreateBO();
        bo1.setUserName("admin");
        bo1.setEmail("admin@example.com");
        bo1.setRoleIds(roleIds);
        bo1.setPostIds(postIds);

        UserCreateBO bo2 = new UserCreateBO();
        bo2.setUserName("admin");
        bo2.setEmail("admin@example.com");
        bo2.setRoleIds(roleIds);
        bo2.setPostIds(postIds);

        UserCreateBO bo3 = new UserCreateBO();
        bo3.setUserName("user");
        bo3.setEmail("user@example.com");
        bo3.setRoleIds(roleIds);
        bo3.setPostIds(postIds);

        assertEquals(bo1, bo2, "相同属性的BO应相等");
        assertEquals(bo1.hashCode(), bo2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(bo1, bo3, "不同属性的BO应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        UserCreateBO bo = new UserCreateBO();
        bo.setUserName("admin");
        bo.setEmail("admin@example.com");

        String str = bo.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("UserCreateBO") || str.contains("admin") || str.contains("admin@example.com"),
                "toString 应包含用户信息");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 创建管理员用户")
    void testScenario_CreateAdminUser() {
        List<Long> roleIds = List.of(1L);  // 管理员角色

        UserCreateBO bo = new UserCreateBO();
        bo.setUserName("admin");
        bo.setNickName("系统管理员");
        bo.setPassword("Admin@123");
        bo.setEmail("admin@example.com");
        bo.setPhonenumber("13800138000");
        bo.setSex("0");
        bo.setDeptId(100L);
        bo.setStatus("0");
        bo.setRoleIds(roleIds);

        assertEquals("admin", bo.getUserName(), "用户名为admin");
        assertEquals("系统管理员", bo.getNickName(), "昵称为系统管理员");
        assertEquals("Admin@123", bo.getPassword(), "密码已设置");
        assertEquals("admin@example.com", bo.getEmail(), "邮箱已设置");
        assertEquals("13800138000", bo.getPhonenumber(), "手机号已设置");
        assertEquals("0", bo.getSex(), "性别为男");
        assertEquals(100L, bo.getDeptId(), "部门ID为100");
        assertEquals("0", bo.getStatus(), "状态为正常");
        assertEquals(1, bo.getRoleIds().size(), "有1个角色");
    }

    @Test
    @DisplayName("业务场景 - 创建普通用户")
    void testScenario_CreateNormalUser() {
        List<Long> roleIds = List.of(2L);  // 普通用户角色
        List<Long> postIds = List.of(1L);  // 普通员工岗位

        UserCreateBO bo = new UserCreateBO();
        bo.setUserName("zhangsan");
        bo.setNickName("张三");
        bo.setPassword("User@123");
        bo.setEmail("zhangsan@example.com");
        bo.setPhonenumber("13900139000");
        bo.setSex("0");
        bo.setDeptId(101L);
        bo.setStatus("0");
        bo.setRoleIds(roleIds);
        bo.setPostIds(postIds);
        bo.setRemark("新入职员工");

        assertEquals("zhangsan", bo.getUserName());
        assertEquals("张三", bo.getNickName());
        assertEquals("User@123", bo.getPassword());
        assertEquals(101L, bo.getDeptId());
        assertEquals(1, bo.getRoleIds().size());
        assertEquals(1, bo.getPostIds().size());
        assertEquals("新入职员工", bo.getRemark());
    }

    @Test
    @DisplayName("业务场景 - 创建最小信息用户")
    void testScenario_CreateMinimalUser() {
        UserCreateBO bo = new UserCreateBO();
        bo.setUserName("minimal");
        bo.setPassword("Pass@123");

        assertEquals("minimal", bo.getUserName(), "仅有用户名");
        assertEquals("Pass@123", bo.getPassword(), "仅有密码");
        assertNull(bo.getNickName(), "无昵称");
        assertNull(bo.getEmail(), "无邮箱");
        assertNull(bo.getPhonenumber(), "无手机号");
        assertNull(bo.getDeptId(), "无部门");
        assertNull(bo.getRoleIds(), "无角色");
        assertNull(bo.getPostIds(), "无岗位");
    }

    @Test
    @DisplayName("业务场景 - 批量创建用户")
    void testScenario_BatchCreateUsers() {
        // 模拟批量创建场景
        UserCreateBO bo1 = new UserCreateBO();
        bo1.setUserName("user1");
        bo1.setPassword("Pass@123");
        bo1.setDeptId(100L);

        UserCreateBO bo2 = new UserCreateBO();
        bo2.setUserName("user2");
        bo2.setPassword("Pass@123");
        bo2.setDeptId(100L);

        List<UserCreateBO> users = List.of(bo1, bo2);

        assertEquals(2, users.size(), "批量创建2个用户");
        assertEquals("user1", users.get(0).getUserName());
        assertEquals("user2", users.get(1).getUserName());
    }
}
