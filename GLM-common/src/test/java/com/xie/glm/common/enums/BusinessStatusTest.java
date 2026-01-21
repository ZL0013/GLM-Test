package com.xie.glm.common.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BusinessStatus 业务状态码枚举测试
 * <p>
 * 测试原则：
 * 1. 参数化测试覆盖所有枚举值
 * 2. 验证 code 和 message 字段
 * 3. 测试枚举方法行为
 *
 * @author xie
 */
class BusinessStatusTest {

    /**
     * 测试枚举值完整性
     */
    @Test
    void testEnumValues() {
        // 验证基础枚举存在
        assertNotNull(BusinessStatus.SUCCESS);
        assertNotNull(BusinessStatus.ERROR);

        // 验证系统模块枚举存在
        assertNotNull(BusinessStatus.SYSTEM_ERROR);
        assertNotNull(BusinessStatus.UNAUTHORIZED);
        assertNotNull(BusinessStatus.FORBIDDEN);

        // 验证用户模块枚举存在
        assertNotNull(BusinessStatus.USER_NOT_FOUND);
        assertNotNull(BusinessStatus.USER_PASSWORD_ERROR);
        assertNotNull(BusinessStatus.USER_ACCOUNT_DISABLED);

        // 验证角色模块枚举存在
        assertNotNull(BusinessStatus.ROLE_NOT_FOUND);
        assertNotNull(BusinessStatus.ROLE_NAME_DUPLICATE);

        // 验证菜单模块枚举存在
        assertNotNull(BusinessStatus.MENU_NOT_FOUND);
        assertNotNull(BusinessStatus.MENU_HAS_CHILD);

        // 验证部门模块枚举存在
        assertNotNull(BusinessStatus.DEPT_NOT_FOUND);
        assertNotNull(BusinessStatus.DEPT_HAS_CHILD);
        assertNotNull(BusinessStatus.DEPT_HAS_USER);

        // 验证枚举总数
        BusinessStatus[] values = BusinessStatus.values();
        assertTrue(values.length >= 14);
    }

    /**
     * 参数化测试：验证 code 和 message 字段
     */
    @ParameterizedTest
    @MethodSource("provideStatusCodes")
    void testCodeAndMessage(BusinessStatus status, Integer expectedCode, String expectedMessage) {
        assertEquals(expectedCode, status.getCode());
        assertEquals(expectedMessage, status.getMessage());
    }

    /**
     * 测试通过 code 获取枚举
     */
    @Test
    void testGetByCode() {
        assertEquals(BusinessStatus.SUCCESS, BusinessStatus.getByCode(0));
        assertEquals(BusinessStatus.ERROR, BusinessStatus.getByCode(1));
        assertEquals(BusinessStatus.SYSTEM_ERROR, BusinessStatus.getByCode(10000));
        assertEquals(BusinessStatus.UNAUTHORIZED, BusinessStatus.getByCode(10001));
        assertEquals(BusinessStatus.FORBIDDEN, BusinessStatus.getByCode(10002));
        assertEquals(BusinessStatus.USER_NOT_FOUND, BusinessStatus.getByCode(11001));
        assertEquals(BusinessStatus.ROLE_NOT_FOUND, BusinessStatus.getByCode(12001));
        assertEquals(BusinessStatus.MENU_NOT_FOUND, BusinessStatus.getByCode(13001));
        assertEquals(BusinessStatus.DEPT_NOT_FOUND, BusinessStatus.getByCode(14001));
    }

    /**
     * 测试通过不存在的 code 获取枚举
     */
    @Test
    void testGetByCodeNotFound() {
        assertNull(BusinessStatus.getByCode(99999));
        assertNull(BusinessStatus.getByCode(-1));
    }

    /**
     * 测试判断是否成功
     */
    @Test
    void testIsSuccess() {
        assertTrue(BusinessStatus.SUCCESS.isSuccess());
        assertFalse(BusinessStatus.ERROR.isSuccess());
        assertFalse(BusinessStatus.SYSTEM_ERROR.isSuccess());
    }

    /**
     * 测试枚举名称
     */
    @Test
    void testEnumNames() {
        assertEquals("SUCCESS", BusinessStatus.SUCCESS.name());
        assertEquals("ERROR", BusinessStatus.ERROR.name());
        assertEquals("SYSTEM_ERROR", BusinessStatus.SYSTEM_ERROR.name());
        assertEquals("USER_NOT_FOUND", BusinessStatus.USER_NOT_FOUND.name());
    }

    // ==================== 测试数据提供方法 ====================

    /**
     * 提供状态码测试数据
     */
    private static Stream<Arguments> provideStatusCodes() {
        return Stream.of(
            Arguments.of(BusinessStatus.SUCCESS, 0, "成功"),
            Arguments.of(BusinessStatus.ERROR, 1, "失败"),
            Arguments.of(BusinessStatus.SYSTEM_ERROR, 10000, "系统异常"),
            Arguments.of(BusinessStatus.UNAUTHORIZED, 10001, "未认证或令牌已过期"),
            Arguments.of(BusinessStatus.FORBIDDEN, 10002, "无权访问"),
            Arguments.of(BusinessStatus.USER_NOT_FOUND, 11001, "用户不存在"),
            Arguments.of(BusinessStatus.USER_PASSWORD_ERROR, 11002, "密码错误"),
            Arguments.of(BusinessStatus.USER_ACCOUNT_DISABLED, 11003, "账号已禁用"),
            Arguments.of(BusinessStatus.ROLE_NOT_FOUND, 12001, "角色不存在"),
            Arguments.of(BusinessStatus.ROLE_NAME_DUPLICATE, 12002, "角色名称已存在"),
            Arguments.of(BusinessStatus.MENU_NOT_FOUND, 13001, "菜单不存在"),
            Arguments.of(BusinessStatus.MENU_HAS_CHILD, 13002, "菜单存在子菜单，不允许删除"),
            Arguments.of(BusinessStatus.DEPT_NOT_FOUND, 14001, "部门不存在"),
            Arguments.of(BusinessStatus.DEPT_HAS_CHILD, 14002, "部门存在子部门，不允许删除"),
            Arguments.of(BusinessStatus.DEPT_HAS_USER, 14003, "部门存在用户，不允许删除")
        );
    }
}
