package com.xie.glm.system.domain;

import com.xie.glm.common.core.BaseEntity;
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
 * SysUser 测试类
 *
 * <p>测试系统用户实体的各种属性和行为
 *
 * @author xie
 */
@DisplayName("SysUser 实体单元测试")
class SysUserTest {

    // ==================== 继承关系测试 ====================

    @Test
    @DisplayName("继承 BaseEntity - 验证继承关系")
    void testExtendsBaseEntity() {
        SysUser user = new SysUser();

        assertTrue(user instanceof BaseEntity, "SysUser 应继承 BaseEntity");
        assertTrue(user instanceof BaseEntity, "应可转换为 BaseEntity");

        // 验证基类字段
        BaseEntity base = user;
        assertNull(base.getCreateTime(), "默认创建时间应为null");
        assertNull(base.getUpdateTime(), "默认更新时间应为null");
        assertNull(base.getCreatedBy(), "默认创建人应为null");
        assertNull(base.getUpdatedBy(), "默认更新人应为null");
    }

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        SysUser user = new SysUser();

        // 用户基本信息
        assertNull(user.getUserId(), "默认用户ID应为null");
        assertNull(user.getDeptId(), "默认部门ID应为null");
        assertNull(user.getUserName(), "默认用户名应为null");
        assertNull(user.getNickName(), "默认昵称应为null");
        assertNull(user.getUserType(), "默认用户类型应为null");
        assertNull(user.getEmail(), "默认邮箱应为null");
        assertNull(user.getPhonenumber(), "默认手机号应为null");
        assertNull(user.getSex(), "默认性别应为null");
        assertNull(user.getAvatar(), "默认头像应为null");
        assertNull(user.getPassword(), "默认密码应为null");
        assertNull(user.getStatus(), "默认状态应为null");
        assertNull(user.getDelFlag(), "默认删除标志应为null");
        assertNull(user.getLoginIp(), "默认登录IP应为null");
        assertNull(user.getLoginDate(), "默认登录时间应为null");
        assertNull(user.getPwdUpdateDate(), "默认密码更新时间应为null");
        assertNull(user.getRemark(), "默认备注应为null");
        assertNull(user.getPasswordChanged(), "默认是否修改密码应为null");
        assertNull(user.getDefaultPassword(), "默认是否默认密码应为null");
        assertNull(user.getLastPasswordChangeTime(), "默认最后密码变更时间应为null");

        // 基类字段
        assertNull(user.getCreateTime(), "默认创建时间应为null");
        assertNull(user.getUpdateTime(), "默认更新时间应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 100, admin, 管理员, 00, admin@example.com, 13800138000, 0",
            "2, 101, user, 普通用户, 00, user@example.com, 13900139000, 1",
            "3, 102, test, 测试用户, 00, test@example.com, 13700137000, 2"
    })
    @DisplayName("Getter/Setter - 验证字段赋值和获取")
    void testGettersSetters(Long userId, Long deptId, String userName, String nickName,
                           String userType, String email, String phonenumber, String sex) {
        LocalDateTime now = LocalDateTime.now();

        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setDeptId(deptId);
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setUserType(userType);
        user.setEmail(email);
        user.setPhonenumber(phonenumber);
        user.setSex(sex);
        user.setAvatar("/avatar/default.png");
        user.setPassword("$2a$10$encodedPassword");
        user.setStatus("0");
        user.setDelFlag("0");
        user.setLoginIp("192.168.1.1");
        user.setLoginDate(now);
        user.setPwdUpdateDate(now);
        user.setRemark("测试用户");
        user.setPasswordChanged(true);
        user.setDefaultPassword(false);
        user.setLastPasswordChangeTime(now);

        assertEquals(userId, user.getUserId(), "用户ID应匹配");
        assertEquals(deptId, user.getDeptId(), "部门ID应匹配");
        assertEquals(userName, user.getUserName(), "用户名应匹配");
        assertEquals(nickName, user.getNickName(), "昵称应匹配");
        assertEquals(userType, user.getUserType(), "用户类型应匹配");
        assertEquals(email, user.getEmail(), "邮箱应匹配");
        assertEquals(phonenumber, user.getPhonenumber(), "手机号应匹配");
        assertEquals(sex, user.getSex(), "性别应匹配");
        assertEquals("/avatar/default.png", user.getAvatar(), "头像应匹配");
        assertEquals("$2a$10$encodedPassword", user.getPassword(), "密码应匹配");
        assertEquals("0", user.getStatus(), "状态应匹配");
        assertEquals("0", user.getDelFlag(), "删除标志应匹配");
        assertEquals("192.168.1.1", user.getLoginIp(), "登录IP应匹配");
        assertEquals(now, user.getLoginDate(), "登录时间应匹配");
        assertEquals(now, user.getPwdUpdateDate(), "密码更新时间应匹配");
        assertEquals("测试用户", user.getRemark(), "备注应匹配");
        assertEquals(true, user.getPasswordChanged(), "是否修改密码应匹配");
        assertEquals(false, user.getDefaultPassword(), "是否默认密码应匹配");
        assertEquals(now, user.getLastPasswordChangeTime(), "最后密码变更时间应匹配");
    }

    // ==================== 新增字段测试 ====================

    @ParameterizedTest
    @CsvSource({
            "00, true",
            "01, false",
            "02, false"
    })
    @DisplayName("用户类型 - 验证用户类型值")
    void testUserType(String userType, boolean isSystemUser) {
        SysUser user = new SysUser();
        user.setUserType(userType);

        assertEquals(userType, user.getUserType(), "用户类型应匹配");
        assertEquals(isSystemUser, "00".equals(user.getUserType()), "00 表示系统用户");
    }

    @ParameterizedTest
    @CsvSource({
            "true, true",
            "false, false"
    })
    @DisplayName("密码修改标识 - 验证布尔字段")
    void testPasswordBooleanFields(boolean passwordChanged, boolean defaultPassword) {
        SysUser user = new SysUser();
        user.setPasswordChanged(passwordChanged);
        user.setDefaultPassword(defaultPassword);

        assertEquals(passwordChanged, user.getPasswordChanged(), "是否修改密码应匹配");
        assertEquals(defaultPassword, user.getDefaultPassword(), "是否默认密码应匹配");
    }

    @Test
    @DisplayName("时间字段 - 验证 LocalDateTime 类型")
    void testDateTimeFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);

        SysUser user = new SysUser();
        user.setLoginDate(now);
        user.setPwdUpdateDate(yesterday);
        user.setLastPasswordChangeTime(now);

        assertEquals(now, user.getLoginDate(), "登录时间应匹配");
        assertEquals(yesterday, user.getPwdUpdateDate(), "密码更新时间应匹配");
        assertEquals(now, user.getLastPasswordChangeTime(), "最后密码变更时间应匹配");
    }

    @Test
    @DisplayName("备注字段 - 验证备注内容")
    void testRemark() {
        SysUser user = new SysUser();
        String remark = "这是一个测试用户，用于系统验证";

        user.setRemark(remark);

        assertEquals(remark, user.getRemark(), "备注应匹配");
        assertNotNull(user.getRemark(), "备注不应为null");
    }

    // ==================== 基类字段测试 ====================

    @ParameterizedTest
    @MethodSource("provideBaseEntityData")
    @DisplayName("基类字段 - 验证审计字段设置")
    void testBaseEntityFields(LocalDateTime createTime, LocalDateTime updateTime,
                             String createdBy, String updatedBy) {
        SysUser user = new SysUser();
        user.setCreateTime(createTime);
        user.setUpdateTime(updateTime);
        user.setCreatedBy(createdBy);
        user.setUpdatedBy(updatedBy);

        assertEquals(createTime, user.getCreateTime(), "创建时间应匹配");
        assertEquals(updateTime, user.getUpdateTime(), "更新时间应匹配");
        assertEquals(createdBy, user.getCreatedBy(), "创建人应匹配");
        assertEquals(updatedBy, user.getUpdatedBy(), "更新人应匹配");
    }

    private static Stream<Arguments> provideBaseEntityData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now, now, "admin", "admin"),
                Arguments.of(now.minusDays(1), now, "system", "user"),
                Arguments.of(null, null, null, null)
        );
    }

    // ==================== 用户状态测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1, false"
    })
    @DisplayName("用户状态 - 验证状态值含义")
    void testUserStatus(String status, boolean isNormal) {
        SysUser user = new SysUser();
        user.setStatus(status);

        assertEquals(status, user.getStatus(), "状态值应匹配");
        assertEquals(isNormal, "0".equals(user.getStatus()), "0 表示正常状态");
    }

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "2, false"
    })
    @DisplayName("删除标志 - 验证删除标志值含义（数据库：0=存在，2=删除）")
    void testDelFlag(String delFlag, boolean isNotDeleted) {
        SysUser user = new SysUser();
        user.setDelFlag(delFlag);

        assertEquals(delFlag, user.getDelFlag(), "删除标志应匹配");
        assertEquals(isNotDeleted, "0".equals(user.getDelFlag()), "0 表示未删除");
    }

    // ==================== 性别测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "2"})
    @DisplayName("性别枚举 - 验证性别值有效性")
    void testSexValue(String sex) {
        SysUser user = new SysUser();
        user.setSex(sex);

        assertEquals(sex, user.getSex(), "性别值应匹配");
        assertTrue(List.of("0", "1", "2").contains(user.getSex()),
                "性别值应为 0=男，1=女，2=未知");
    }

    // ==================== 密码加密格式测试 ====================

    @ParameterizedTest
    @CsvSource({
            "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy, true",
            "$2a$12$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy, true",
            "plainPassword, false",
            ", false"
    })
    @DisplayName("密码格式 - 验证 BCrypt 加密格式")
    void testPasswordFormat(String password, boolean isBCrypt) {
        SysUser user = new SysUser();
        user.setPassword(password);

        assertEquals(password, user.getPassword(), "密码应匹配");

        if (password != null && isBCrypt) {
            assertTrue(password.startsWith("$2a$"), "BCrypt 密码应以 $2a$ 开头");
            assertTrue(password.length() == 60, "BCrypt 密码长度应为 60 字符");
        }
    }

    // ==================== 手机号格式测试 ====================

    @ParameterizedTest
    @CsvSource({
            "13800138000, true",
            "13900139000, true",
            "18812345678, true",
            "12345, false",
            "abcdefghij, false"
    })
    @DisplayName("手机号格式 - 验证手机号格式")
    void testPhoneNumberFormat(String phonenumber, boolean isValid) {
        SysUser user = new SysUser();
        user.setPhonenumber(phonenumber);

        assertEquals(phonenumber, user.getPhonenumber(), "手机号应匹配");

        if (phonenumber != null && isValid) {
            assertTrue(phonenumber.matches("^1[3-9]\\d{9}$"), "应为有效手机号格式");
        }
    }

    // ==================== 邮箱格式测试 ====================

    @ParameterizedTest
    @CsvSource({
            "admin@example.com, true",
            "user@test.com, true",
            "invalid-email, false",
            "@example.com, false",
            "test@, false"
    })
    @DisplayName("邮箱格式 - 验证邮箱格式")
    void testEmailFormat(String email, boolean isValid) {
        SysUser user = new SysUser();
        user.setEmail(email);

        assertEquals(email, user.getEmail(), "邮箱应匹配");

        if (email != null && isValid) {
            assertTrue(email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"),
                    "应为有效邮箱格式");
        }
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证 setter 容错")
    void testNullHandling(String value) {
        SysUser user = new SysUser();

        user.setUserName(value);
        user.setEmail(value);
        user.setPhonenumber(value);
        user.setUserType(value);
        user.setRemark(value);

        assertNull(user.getUserName(), "null 用户名应被接受");
        assertNull(user.getEmail(), "null 邮箱应被接受");
        assertNull(user.getPhonenumber(), "null 手机号应被接受");
        assertNull(user.getUserType(), "null 用户类型应被接受");
        assertNull(user.getRemark(), "null 备注应被接受");
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        SysUser user1 = new SysUser();
        user1.setUserId(1L);
        user1.setUserName("admin");

        SysUser user2 = new SysUser();
        user2.setUserId(1L);
        user2.setUserName("admin");

        SysUser user3 = new SysUser();
        user3.setUserId(2L);
        user3.setUserName("user");

        assertEquals(user1, user2, "相同 ID 的用户应相等");
        assertEquals(user1.hashCode(), user2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(user1, user3, "不同 ID 的用户应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUserName("admin");

        String str = user.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("SysUser") || str.contains("admin") || str.contains("1"),
                "toString 应包含用户信息");
    }

    // ==================== 序列化测试 ====================

    @Test
    @DisplayName("serialVersionUID - 验证序列化兼容性")
    void testSerialVersionUID() {
        SysUser user = new SysUser();

        try {
            // 验证 BaseEntity 的 serialVersionUID
            java.lang.reflect.Field field = BaseEntity.class.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            assertNotNull(field.get(null), "BaseEntity 应有 serialVersionUID 字段");
        } catch (NoSuchFieldException e) {
            fail("BaseEntity 应该有 serialVersionUID 字段");
        } catch (IllegalAccessException e) {
            fail("访问 serialVersionUID 失败");
        }
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 创建管理员用户")
    void testCreateAdminUser() {
        LocalDateTime now = LocalDateTime.now();

        SysUser admin = new SysUser();
        admin.setUserId(1L);
        admin.setDeptId(100L);
        admin.setUserName("admin");
        admin.setNickName("管理员");
        admin.setUserType("00");
        admin.setEmail("admin@example.com");
        admin.setPhonenumber("13800138000");
        admin.setSex("0");
        admin.setAvatar("/avatar/admin.png");
        admin.setPassword("$2a$10$encodedAdminPassword");
        admin.setStatus("0");
        admin.setDelFlag("0");
        admin.setLoginIp("192.168.1.1");
        admin.setLoginDate(now);
        admin.setPwdUpdateDate(now);
        admin.setRemark("系统管理员");
        admin.setPasswordChanged(true);
        admin.setDefaultPassword(false);
        admin.setLastPasswordChangeTime(now);
        admin.setCreateTime(now);
        admin.setCreatedBy("system");

        assertEquals(1L, admin.getUserId(), "管理员ID应为1");
        assertEquals("admin", admin.getUserName(), "管理员用户名应为admin");
        assertEquals("00", admin.getUserType(), "管理员应为系统用户");
        assertEquals("0", admin.getStatus(), "管理员状态应为正常");
        assertEquals("0", admin.getDelFlag(), "管理员应未删除");
        assertEquals(true, admin.getPasswordChanged(), "管理员应已修改密码");
        assertEquals(false, admin.getDefaultPassword(), "管理员不应使用默认密码");
        assertEquals(now, admin.getCreateTime(), "创建时间应匹配");
        assertEquals("system", admin.getCreatedBy(), "创建人应为system");
    }

    @Test
    @DisplayName("业务场景 - 用户登录后更新信息")
    void testUserLoginUpdate() {
        LocalDateTime now = LocalDateTime.now();

        SysUser user = new SysUser();
        user.setUserId(2L);
        user.setUserName("testuser");

        // 模拟登录后更新
        user.setLoginIp("192.168.1.100");
        user.setLoginDate(now);

        assertEquals("192.168.1.100", user.getLoginIp(), "登录IP应更新");
        assertEquals(now, user.getLoginDate(), "登录时间应更新");
    }

    @Test
    @DisplayName("业务场景 - 用户修改密码")
    void testUserChangePassword() {
        LocalDateTime now = LocalDateTime.now();

        SysUser user = new SysUser();
        user.setUserId(3L);
        user.setPasswordChanged(false);
        user.setDefaultPassword(true);

        // 修改密码
        user.setPassword("$2a$10$newEncodedPassword");
        user.setPwdUpdateDate(now);
        user.setPasswordChanged(true);
        user.setDefaultPassword(false);
        user.setLastPasswordChangeTime(now);

        assertEquals("$2a$10$newEncodedPassword", user.getPassword(), "密码应更新");
        assertEquals(now, user.getPwdUpdateDate(), "密码更新时间应更新");
        assertEquals(true, user.getPasswordChanged(), "应标记为已修改密码");
        assertEquals(false, user.getDefaultPassword(), "应标记为非默认密码");
        assertEquals(now, user.getLastPasswordChangeTime(), "最后密码变更时间应更新");
    }

    @Test
    @DisplayName("业务场景 - 用户停用")
    void testDisableUser() {
        SysUser user = new SysUser();
        user.setUserId(4L);
        user.setStatus("0");

        // 停用用户
        user.setStatus("1");

        assertEquals("1", user.getStatus(), "用户状态应更新为停用");
        assertFalse("0".equals(user.getStatus()), "用户应不再处于正常状态");
    }

    @Test
    @DisplayName("业务场景 - 逻辑删除用户")
    void testLogicalDeleteUser() {
        SysUser user = new SysUser();
        user.setUserId(5L);
        user.setDelFlag("0");

        // 逻辑删除（数据库使用 2 表示删除）
        user.setDelFlag("2");

        assertEquals("2", user.getDelFlag(), "删除标志应更新为已删除");
        assertFalse("0".equals(user.getDelFlag()), "用户应标记为已删除");
    }

    // ==================== 链式调用测试 ====================

    @Test
    @DisplayName("字段设置 - 验证多次调用正确性")
    void testMultipleSetters() {
        SysUser user = new SysUser();

        // Lombok @Data 不生成链式调用，但可以验证 setter 的正确性
        user.setUserId(1L);
        user.setUserName("admin");
        user.setEmail("admin@example.com");
        user.setUserType("00");
        user.setRemark("管理员账号");

        assertEquals(1L, user.getUserId());
        assertEquals("admin", user.getUserName());
        assertEquals("admin@example.com", user.getEmail());
        assertEquals("00", user.getUserType());
        assertEquals("管理员账号", user.getRemark());
    }

    // ==================== 部门关联测试 ====================

    @Test
    @DisplayName("部门关联 - 验证用户部门关系")
    void testDepartmentRelation() {
        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setDeptId(100L);
        user.setUserName("admin");

        assertEquals(100L, user.getDeptId(), "用户应属于部门100");
        assertNotNull(user.getDeptId(), "部门ID不应为null");
    }
}
