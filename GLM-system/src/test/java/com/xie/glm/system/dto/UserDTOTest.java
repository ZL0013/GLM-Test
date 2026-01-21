package com.xie.glm.system.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserDTO 用户数据传输对象测试类
 *
 * <p>测试用户 DTO 的各种属性和行为
 * <p>测试原则：
 * <ul>
 *   <li>参数化测试覆盖字段验证</li>
 *   <li>验证 Lombok @Data 注解生成的 getter/setter</li>
 *   <li>测试时间字段的处理</li>
 *   <li>验证业务字段的有效性</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("UserDTO 用户数据传输对象单元测试")
class UserDTOTest {

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        UserDTO dto = new UserDTO();

        // 用户基本信息
        assertNull(dto.getUserId(), "默认用户ID应为null");
        assertNull(dto.getUserName(), "默认用户名应为null");
        assertNull(dto.getNickName(), "默认昵称应为null");
        assertNull(dto.getEmail(), "默认邮箱应为null");
        assertNull(dto.getPhonenumber(), "默认手机号应为null");
        assertNull(dto.getSex(), "默认性别应为null");
        assertNull(dto.getAvatar(), "默认头像应为null");
        assertNull(dto.getDeptId(), "默认部门ID应为null");
        assertNull(dto.getStatus(), "默认状态应为null");
        assertNull(dto.getRemark(), "默认备注应为null");

        // 时间字段
        assertNull(dto.getCreateTime(), "默认创建时间应为null");
        assertNull(dto.getUpdateTime(), "默认更新时间应为null");
        assertNull(dto.getLoginDate(), "默认登录时间应为null");
        assertNull(dto.getPwdUpdateDate(), "默认密码更新时间应为null");

        // 布尔字段
        assertNull(dto.getPasswordChanged(), "默认密码修改标志应为null");
        assertNull(dto.getDefaultPassword(), "默认默认密码标志应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, admin, 管理员, admin@example.com, 13800138000, 0, 100, 0",
            "2, user, 普通用户, user@example.com, 13900139000, 1, 101, 0",
            "3, test, 测试用户, test@example.com, 13700137000, 2, 102, 0"
    })
    @DisplayName("Getter/Setter - 验证基本字段赋值和获取")
    void testGettersSetters(Long userId, String userName, String nickName,
                           String email, String phonenumber, String sex,
                           Long deptId, String status) {
        UserDTO dto = new UserDTO();
        dto.setUserId(userId);
        dto.setUserName(userName);
        dto.setNickName(nickName);
        dto.setEmail(email);
        dto.setPhonenumber(phonenumber);
        dto.setSex(sex);
        dto.setDeptId(deptId);
        dto.setStatus(status);
        dto.setRemark("测试备注");
        dto.setAvatar("http://example.com/avatar.jpg");

        assertEquals(userId, dto.getUserId(), "用户ID应匹配");
        assertEquals(userName, dto.getUserName(), "用户名应匹配");
        assertEquals(nickName, dto.getNickName(), "昵称应匹配");
        assertEquals(email, dto.getEmail(), "邮箱应匹配");
        assertEquals(phonenumber, dto.getPhonenumber(), "手机号应匹配");
        assertEquals(sex, dto.getSex(), "性别应匹配");
        assertEquals(deptId, dto.getDeptId(), "部门ID应匹配");
        assertEquals(status, dto.getStatus(), "状态应匹配");
        assertEquals("测试备注", dto.getRemark(), "备注应匹配");
        assertEquals("http://example.com/avatar.jpg", dto.getAvatar(), "头像应匹配");
    }

    // ==================== 用户 ID 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "100, true",
            "1000, true"
    })
    @DisplayName("用户 ID - 验证用户ID")
    void testUserId(Long userId, boolean isValid) {
        UserDTO dto = new UserDTO();
        dto.setUserId(userId);

        assertEquals(userId, dto.getUserId(), "用户ID应匹配");

        if (isValid) {
            assertNotNull(dto.getUserId(), "用户ID不应为null");
            assertTrue(dto.getUserId() > 0, "用户ID应为正数");
        }
    }

    // ==================== 时间字段测试 ====================

    @Test
    @DisplayName("时间字段 - 验证时间设置")
    void testTimeFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime lastWeek = now.minusWeeks(1);

        UserDTO dto = new UserDTO();
        dto.setCreateTime(now);
        dto.setUpdateTime(yesterday);
        dto.setLoginDate(now);
        dto.setPwdUpdateDate(lastWeek);

        assertEquals(now, dto.getCreateTime(), "创建时间应匹配");
        assertEquals(yesterday, dto.getUpdateTime(), "更新时间应匹配");
        assertEquals(now, dto.getLoginDate(), "登录时间应匹配");
        assertEquals(lastWeek, dto.getPwdUpdateDate(), "密码更新时间应匹配");
    }

    @Test
    @DisplayName("时间字段 - 验证时间顺序")
    void testTimeOrder() {
        LocalDateTime createTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime updateTime = LocalDateTime.of(2024, 1, 2, 0, 0);

        UserDTO dto = new UserDTO();
        dto.setCreateTime(createTime);
        dto.setUpdateTime(updateTime);

        assertTrue(dto.getCreateTime().isBefore(dto.getUpdateTime()),
                "创建时间应早于更新时间");
    }

    // ==================== 布尔字段测试 ====================

    @ParameterizedTest
    @CsvSource({
            "true, true",
            "false, false"
    })
    @DisplayName("布尔字段 - 验证密码相关标志")
    void testBooleanFields(Boolean passwordChanged, Boolean defaultPassword) {
        UserDTO dto = new UserDTO();
        dto.setPasswordChanged(passwordChanged);
        dto.setDefaultPassword(defaultPassword);

        assertEquals(passwordChanged, dto.getPasswordChanged(), "密码修改标志应匹配");
        assertEquals(defaultPassword, dto.getDefaultPassword(), "默认密码标志应匹配");
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
        UserDTO dto = new UserDTO();
        dto.setUserName(userName);

        assertEquals(userName, dto.getUserName(), "用户名应匹配");

        if (isValid) {
            assertNotNull(dto.getUserName(), "用户名不应为null");
            assertFalse(dto.getUserName().isEmpty(), "用户名不应为空");
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
        UserDTO dto = new UserDTO();
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
            ", false"
    })
    @DisplayName("手机号 - 验证手机号格式")
    void testPhonenumber(String phonenumber, boolean shouldHaveValue) {
        UserDTO dto = new UserDTO();
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
        UserDTO dto = new UserDTO();
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
        UserDTO dto = new UserDTO();
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
            ", false"
    })
    @DisplayName("部门 - 验证部门ID")
    void testDeptId(Long deptId, boolean shouldHaveValue) {
        UserDTO dto = new UserDTO();
        dto.setDeptId(deptId);

        assertEquals(deptId, dto.getDeptId(), "部门ID应匹配");

        if (shouldHaveValue) {
            assertNotNull(dto.getDeptId(), "部门ID不应为null");
            assertTrue(dto.getDeptId() > 0, "部门ID应为正数");
        }
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证字段 setter 容错")
    void testNullHandling(String value) {
        UserDTO dto = new UserDTO();

        dto.setUserName(value);
        dto.setNickName(value);
        dto.setEmail(value);
        dto.setPhonenumber(value);
        dto.setSex(value);
        dto.setAvatar(value);
        dto.setRemark(value);

        assertNull(dto.getUserName(), "null 用户名应被接受");
        assertNull(dto.getNickName(), "null 昵称应被接受");
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
        UserDTO dto1 = new UserDTO();
        dto1.setUserId(1L);
        dto1.setUserName("admin");
        dto1.setEmail("admin@example.com");

        UserDTO dto2 = new UserDTO();
        dto2.setUserId(1L);
        dto2.setUserName("admin");
        dto2.setEmail("admin@example.com");

        UserDTO dto3 = new UserDTO();
        dto3.setUserId(2L);
        dto3.setUserName("user");
        dto3.setEmail("user@example.com");

        assertEquals(dto1, dto2, "相同属性的DTO应相等");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(dto1, dto3, "不同属性的DTO应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        UserDTO dto = new UserDTO();
        dto.setUserId(1L);
        dto.setUserName("admin");
        dto.setEmail("admin@example.com");

        String str = dto.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("UserDTO") || str.contains("admin") || str.contains("1"),
                "toString 应包含用户信息");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 完整用户信息")
    void testScenario_CompleteUserInfo() {
        LocalDateTime now = LocalDateTime.now();

        UserDTO dto = new UserDTO();
        dto.setUserId(1L);
        dto.setUserName("admin");
        dto.setNickName("系统管理员");
        dto.setEmail("admin@example.com");
        dto.setPhonenumber("13800138000");
        dto.setSex("0");
        dto.setDeptId(100L);
        dto.setStatus("0");
        dto.setAvatar("http://example.com/avatar.jpg");
        dto.setRemark("系统管理员账号");
        dto.setCreateTime(now);
        dto.setUpdateTime(now);
        dto.setLoginDate(now);
        dto.setPasswordChanged(true);
        dto.setDefaultPassword(false);

        assertEquals(1L, dto.getUserId());
        assertEquals("admin", dto.getUserName());
        assertEquals("系统管理员", dto.getNickName());
        assertEquals("admin@example.com", dto.getEmail());
        assertEquals("13800138000", dto.getPhonenumber());
        assertEquals("0", dto.getSex());
        assertEquals(100L, dto.getDeptId());
        assertEquals("0", dto.getStatus());
        assertTrue(dto.getPasswordChanged());
        assertFalse(dto.getDefaultPassword());
    }

    @Test
    @DisplayName("业务场景 - 新注册用户")
    void testScenario_NewRegisteredUser() {
        LocalDateTime now = LocalDateTime.now();

        UserDTO dto = new UserDTO();
        dto.setUserId(2L);
        dto.setUserName("zhangsan");
        dto.setNickName("张三");
        dto.setEmail("zhangsan@example.com");
        dto.setDeptId(101L);
        dto.setStatus("0");
        dto.setCreateTime(now);
        dto.setPasswordChanged(false);
        dto.setDefaultPassword(true);

        assertEquals(2L, dto.getUserId());
        assertEquals("zhangsan", dto.getUserName());
        assertEquals("张三", dto.getNickName());
        assertFalse(dto.getPasswordChanged(), "新用户未修改密码");
        assertTrue(dto.getDefaultPassword(), "新用户使用默认密码");
        assertNull(dto.getLoginDate(), "新用户未登录");
    }

    @Test
    @DisplayName("业务场景 - 停用用户")
    void testScenario_DisabledUser() {
        UserDTO dto = new UserDTO();
        dto.setUserId(3L);
        dto.setUserName("disabled_user");
        dto.setNickName("已停用用户");
        dto.setStatus("1");  // 停用
        dto.setRemark("违规操作，账号已停用");

        assertEquals(3L, dto.getUserId());
        assertEquals("1", dto.getStatus(), "状态应为停用");
        assertEquals("违规操作，账号已停用", dto.getRemark());
    }

    @Test
    @DisplayName("业务场景 - 密码已修改的用户")
    void testScenario_PasswordChangedUser() {
        LocalDateTime pwdUpdateDate = LocalDateTime.now().minusDays(10);

        UserDTO dto = new UserDTO();
        dto.setUserId(4L);
        dto.setUserName("secure_user");
        dto.setPasswordChanged(true);
        dto.setDefaultPassword(false);
        dto.setPwdUpdateDate(pwdUpdateDate);

        assertTrue(dto.getPasswordChanged(), "密码已修改");
        assertFalse(dto.getDefaultPassword(), "不是默认密码");
        assertEquals(pwdUpdateDate, dto.getPwdUpdateDate(), "密码更新时间应匹配");
    }

    @Test
    @DisplayName("业务场景 - 最近登录的用户")
    void testScenario_RecentlyLoggedInUser() {
        LocalDateTime loginDate = LocalDateTime.now().minusHours(2);

        UserDTO dto = new UserDTO();
        dto.setUserId(5L);
        dto.setUserName("active_user");
        dto.setLoginDate(loginDate);

        assertEquals(loginDate, dto.getLoginDate(), "登录时间应匹配");
        assertTrue(dto.getLoginDate().isBefore(LocalDateTime.now()),
                "登录时间应早于当前时间");
    }

    @Test
    @DisplayName("业务场景 - 无部门用户")
    void testScenario_UserWithoutDepartment() {
        UserDTO dto = new UserDTO();
        dto.setUserId(6L);
        dto.setUserName("nodept_user");
        dto.setDeptId(null);

        assertEquals(6L, dto.getUserId());
        assertNull(dto.getDeptId(), "用户无部门");
    }

    @Test
    @DisplayName("业务场景 - 最小信息用户")
    void testScenario_MinimalUserInfo() {
        UserDTO dto = new UserDTO();
        dto.setUserId(7L);
        dto.setUserName("minimal");

        assertEquals(7L, dto.getUserId());
        assertEquals("minimal", dto.getUserName());
        assertNull(dto.getNickName());
        assertNull(dto.getEmail());
        assertNull(dto.getPhonenumber());
        assertNull(dto.getDeptId());
        assertNull(dto.getCreateTime());
        assertNull(dto.getUpdateTime());
    }
}
