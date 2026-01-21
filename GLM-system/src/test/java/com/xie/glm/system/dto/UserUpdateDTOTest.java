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
 * UserUpdateDTO 更新用户 DTO 测试类
 *
 * <p>测试更新用户数据传输对象的各种属性和行为
 * <p>测试原则：
 * <ul>
 *   <li>参数化测试覆盖字段验证</li>
 *   <li>验证 Lombok @Data 注解生成的 getter/setter</li>
 *   <li>测试角色和岗位更新</li>
 *   <li>验证可选字段的更新</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("UserUpdateDTO 更新用户 DTO 单元测试")
class UserUpdateDTOTest {

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        UserUpdateDTO dto = new UserUpdateDTO();

        // 用户 ID（必填）
        assertNull(dto.getUserId(), "默认用户ID应为null");

        // 用户基本信息（可选）
        assertNull(dto.getUserName(), "默认用户名应为null");
        assertNull(dto.getNickName(), "默认昵称应为null");
        assertNull(dto.getEmail(), "默认邮箱应为null");
        assertNull(dto.getPhonenumber(), "默认手机号应为null");
        assertNull(dto.getSex(), "默认性别应为null");
        assertNull(dto.getAvatar(), "默认头像应为null");
        assertNull(dto.getDeptId(), "默认部门ID应为null");
        assertNull(dto.getStatus(), "默认状态应为null");
        assertNull(dto.getRemark(), "默认备注应为null");

        // 关联信息（可选）
        assertNull(dto.getRoleIds(), "默认角色ID列表应为null");
        assertNull(dto.getPostIds(), "默认岗位ID列表应为null");
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
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(userId);
        dto.setUserName(userName);
        dto.setNickName(nickName);
        dto.setEmail(email);
        dto.setPhonenumber(phonenumber);
        dto.setSex(sex);
        dto.setDeptId(deptId);
        dto.setStatus(status);
        dto.setRemark("更新备注");
        dto.setAvatar("http://example.com/new-avatar.jpg");

        assertEquals(userId, dto.getUserId(), "用户ID应匹配");
        assertEquals(userName, dto.getUserName(), "用户名应匹配");
        assertEquals(nickName, dto.getNickName(), "昵称应匹配");
        assertEquals(email, dto.getEmail(), "邮箱应匹配");
        assertEquals(phonenumber, dto.getPhonenumber(), "手机号应匹配");
        assertEquals(sex, dto.getSex(), "性别应匹配");
        assertEquals(deptId, dto.getDeptId(), "部门ID应匹配");
        assertEquals(status, dto.getStatus(), "状态应匹配");
        assertEquals("更新备注", dto.getRemark(), "备注应匹配");
        assertEquals("http://example.com/new-avatar.jpg", dto.getAvatar(), "头像应匹配");
    }

    // ==================== 用户 ID 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "100, true",
            "1000, true",
            ", false"
    })
    @DisplayName("用户 ID - 验证必填的用户ID")
    void testUserId(Long userId, boolean shouldHaveValue) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(userId);

        assertEquals(userId, dto.getUserId(), "用户ID应匹配");

        if (shouldHaveValue) {
            assertNotNull(dto.getUserId(), "用户ID不应为null");
            assertTrue(dto.getUserId() > 0, "用户ID应为正数");
        }
    }

    // ==================== 可选字段测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("可选字段 - 验证 null 值处理")
    void testOptionalFieldsNull(String value) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);

        // 以下字段都是可选的，可以设置为 null
        dto.setUserName(value);
        dto.setNickName(value);
        dto.setEmail(value);
        dto.setPhonenumber(value);
        dto.setSex(value);
        dto.setAvatar(value);
        dto.setDeptId(null);
        dto.setStatus(value);
        dto.setRemark(value);

        assertEquals(1L, dto.getUserId(), "用户ID应已设置");
        assertNull(dto.getUserName(), "null 用户名应被接受");
        assertNull(dto.getNickName(), "null 昵称应被接受");
        assertNull(dto.getEmail(), "null 邮箱应被接受");
        assertNull(dto.getPhonenumber(), "null 手机号应被接受");
        assertNull(dto.getSex(), "null 性别应被接受");
        assertNull(dto.getAvatar(), "null 头像应被接受");
        assertNull(dto.getDeptId(), "null 部门应被接受");
        assertNull(dto.getStatus(), "null 状态应被接受");
        assertNull(dto.getRemark(), "null 备注应被接受");
    }

    @Test
    @DisplayName("可选字段 - 验证仅更新部分字段")
    void testPartialFieldUpdate() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setNickName("新昵称");
        // 其他字段保持 null，不更新

        assertEquals(1L, dto.getUserId(), "用户ID应已设置");
        assertEquals("新昵称", dto.getNickName(), "昵称应已更新");
        assertNull(dto.getUserName(), "用户名未更新");
        assertNull(dto.getEmail(), "邮箱未更新");
        assertNull(dto.getPhonenumber(), "手机号未更新");
    }

    // ==================== 角色更新测试 ====================

    @ParameterizedTest
    @MethodSource("provideRoleIdsData")
    @DisplayName("角色更新 - 验证角色ID列表")
    void testRoleIds(List<Long> roleIds, int expectedSize) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
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

    @Test
    @DisplayName("角色更新 - 清空用户角色")
    void testClearRoles() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setRoleIds(List.of());  // 空列表表示清空所有角色

        assertNotNull(dto.getRoleIds(), "角色列表不应为null");
        assertTrue(dto.getRoleIds().isEmpty(), "角色列表应为空");
    }

    // ==================== 岗位更新测试 ====================

    @ParameterizedTest
    @MethodSource("providePostIdsData")
    @DisplayName("岗位更新 - 验证岗位ID列表")
    void testPostIds(List<Long> postIds, int expectedSize) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
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

    @Test
    @DisplayName("岗位更新 - 清空用户岗位")
    void testClearPosts() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setPostIds(List.of());  // 空列表表示清空所有岗位

        assertNotNull(dto.getPostIds(), "岗位列表不应为null");
        assertTrue(dto.getPostIds().isEmpty(), "岗位列表应为空");
    }

    // ==================== 用户名测试 ====================

    @ParameterizedTest
    @CsvSource({
            "new_admin, true",
            "user123, true",
            "test_user, true",
            ", false"
    })
    @DisplayName("用户名 - 验证用户名更新")
    void testUserName(String userName, boolean shouldHaveValue) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
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
            "new@example.com, true",
            "updated@test.com, true",
            ", false"
    })
    @DisplayName("邮箱 - 验证邮箱更新")
    void testEmail(String email, boolean shouldHaveValue) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
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
    @DisplayName("手机号 - 验证手机号更新")
    void testPhonenumber(String phonenumber, boolean shouldHaveValue) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
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
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
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
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
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
    @DisplayName("部门 - 验证部门ID更新")
    void testDeptId(Long deptId, boolean shouldHaveValue) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setDeptId(deptId);

        assertEquals(deptId, dto.getDeptId(), "部门ID应匹配");

        if (shouldHaveValue) {
            assertNotNull(dto.getDeptId(), "部门ID不应为null");
            assertTrue(dto.getDeptId() > 0, "部门ID应为正数");
        }
    }

    // ==================== 角色和岗位组合测试 ====================

    @Test
    @DisplayName("角色和岗位组合 - 同时更新角色和岗位")
    void testUpdateRolesAndPosts() {
        List<Long> roleIds = List.of(2L, 3L);  // 更新为新角色
        List<Long> postIds = List.of(2L);      // 更新为新岗位

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setRoleIds(roleIds);
        dto.setPostIds(postIds);

        assertEquals(2, dto.getRoleIds().size(), "应有2个新角色");
        assertEquals(1, dto.getPostIds().size(), "应有1个新岗位");
        assertTrue(dto.getRoleIds().contains(2L), "应包含角色2");
        assertTrue(dto.getPostIds().contains(2L), "应包含岗位2");
    }

    @Test
    @DisplayName("角色和岗位组合 - 仅更新角色")
    void testUpdateOnlyRoles() {
        List<Long> roleIds = List.of(2L);

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setRoleIds(roleIds);
        // postIds 保持 null，不更新

        assertNotNull(dto.getRoleIds(), "角色ID列表不应为null");
        assertEquals(1, dto.getRoleIds().size(), "应有1个角色");
        assertNull(dto.getPostIds(), "岗位ID列表应为null（不更新）");
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        List<Long> roleIds = List.of(1L, 2L);

        UserUpdateDTO dto1 = new UserUpdateDTO();
        dto1.setUserId(1L);
        dto1.setUserName("admin");
        dto1.setEmail("admin@example.com");
        dto1.setRoleIds(roleIds);

        UserUpdateDTO dto2 = new UserUpdateDTO();
        dto2.setUserId(1L);
        dto2.setUserName("admin");
        dto2.setEmail("admin@example.com");
        dto2.setRoleIds(roleIds);

        UserUpdateDTO dto3 = new UserUpdateDTO();
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
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setUserName("admin");

        String str = dto.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("UserUpdateDTO") || str.contains("admin") || str.contains("1"),
                "toString 应包含用户信息");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 更新用户基本信息")
    void testScenario_UpdateBasicInfo() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setNickName("新昵称");
        dto.setEmail("new@example.com");
        dto.setPhonenumber("13900139000");
        dto.setSex("1");
        dto.setRemark("更新用户信息");

        assertEquals(1L, dto.getUserId());
        assertEquals("新昵称", dto.getNickName());
        assertEquals("new@example.com", dto.getEmail());
        assertEquals("13900139000", dto.getPhonenumber());
        assertEquals("1", dto.getSex());
        assertEquals("更新用户信息", dto.getRemark());
        assertNull(dto.getRoleIds(), "不更新角色");
        assertNull(dto.getPostIds(), "不更新岗位");
    }

    @Test
    @DisplayName("业务场景 - 更新用户角色")
    void testScenario_UpdateUserRoles() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setRoleIds(List.of(2L, 3L));  // 更新为普通用户和访客角色

        assertEquals(1L, dto.getUserId());
        assertEquals(2, dto.getRoleIds().size());
        assertTrue(dto.getRoleIds().contains(2L));
        assertNull(dto.getUserName(), "不更新用户名");
        assertNull(dto.getDeptId(), "不更新部门");
    }

    @Test
    @DisplayName("业务场景 - 停用用户")
    void testScenario_DisableUser() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setStatus("1");  // 停用
        dto.setRemark("违规操作，停用账号");

        assertEquals(1L, dto.getUserId());
        assertEquals("1", dto.getStatus());
        assertEquals("违规操作，停用账号", dto.getRemark());
    }

    @Test
    @DisplayName("业务场景 - 调整用户部门")
    void testScenario_ChangeDepartment() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setDeptId(200L);  // 调整到新部门
        dto.setRemark("组织架构调整，调到新部门");

        assertEquals(1L, dto.getUserId());
        assertEquals(200L, dto.getDeptId());
        assertEquals("组织架构调整，调到新部门", dto.getRemark());
    }

    @Test
    @DisplayName("业务场景 - 清空用户角色和岗位")
    void testScenario_ClearUserRolesAndPosts() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setRoleIds(List.of());  // 清空所有角色
        dto.setPostIds(List.of());  // 清空所有岗位
        dto.setRemark("清空所有权限");

        assertEquals(1L, dto.getUserId());
        assertTrue(dto.getRoleIds().isEmpty(), "角色列表应为空");
        assertTrue(dto.getPostIds().isEmpty(), "岗位列表应为空");
        assertEquals("清空所有权限", dto.getRemark());
    }

    @Test
    @DisplayName("业务场景 - 仅更新用户ID（最小更新）")
    void testScenario_MinimalUpdate() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        // 其他字段都保持 null，不更新

        assertEquals(1L, dto.getUserId());
        assertNull(dto.getUserName());
        assertNull(dto.getNickName());
        assertNull(dto.getEmail());
        assertNull(dto.getPhonenumber());
        assertNull(dto.getDeptId());
        assertNull(dto.getStatus());
        assertNull(dto.getRoleIds());
        assertNull(dto.getPostIds());
    }
}
