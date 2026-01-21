package com.xie.glm.system.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SysLoginInfo 实体类测试
 *
 * @author xie
 */
@DisplayName("登录日志实体测试")
class SysLoginInfoTest {

    @Nested
    @DisplayName("实体构造测试")
    class ConstructorTest {

        @Test
        @DisplayName("无参构造应该创建空对象")
        void testNoArgConstructor() {
            SysLoginInfo loginInfo = new SysLoginInfo();

            assertNotNull(loginInfo);
            assertNull(loginInfo.getInfoId());
            assertNull(loginInfo.getUserName());
            assertNull(loginInfo.getStatus());
            assertNull(loginInfo.getIpAddress());
            assertNull(loginInfo.getLoginLocation());
            assertNull(loginInfo.getBrowser());
            assertNull(loginInfo.getOs());
            assertNull(loginInfo.getMsg());
            assertNull(loginInfo.getLoginTime());
        }

        @Test
        @DisplayName("Builder 模式应该正确构造对象")
        void testBuilder() {
            LocalDateTime now = LocalDateTime.now();

            SysLoginInfo loginInfo = SysLoginInfo.builder()
                    .userName("admin")
                    .status(0)
                    .ipAddress("127.0.0.1")
                    .loginLocation("本地")
                    .browser("Chrome")
                    .os("Windows 10")
                    .msg("登录成功")
                    .loginTime(now)
                    .build();

            assertEquals("admin", loginInfo.getUserName());
            assertEquals(0, loginInfo.getStatus());
            assertEquals("127.0.0.1", loginInfo.getIpAddress());
            assertEquals("本地", loginInfo.getLoginLocation());
            assertEquals("Chrome", loginInfo.getBrowser());
            assertEquals("Windows 10", loginInfo.getOs());
            assertEquals("登录成功", loginInfo.getMsg());
            assertEquals(now, loginInfo.getLoginTime());
        }
    }

    @Nested
    @DisplayName("Setter 和 Getter 测试")
    class SetterGetterTest {

        @Test
        @DisplayName("应该正确设置和获取属性值")
        void testSetterGetter() {
            SysLoginInfo loginInfo = new SysLoginInfo();
            LocalDateTime now = LocalDateTime.now();

            loginInfo.setInfoId(1L);
            loginInfo.setUserName("admin");
            loginInfo.setStatus(0);
            loginInfo.setIpAddress("127.0.0.1");
            loginInfo.setLoginLocation("本地");
            loginInfo.setBrowser("Chrome");
            loginInfo.setOs("Windows 10");
            loginInfo.setMsg("登录成功");
            loginInfo.setLoginTime(now);

            assertEquals(1L, loginInfo.getInfoId());
            assertEquals("admin", loginInfo.getUserName());
            assertEquals(0, loginInfo.getStatus());
            assertEquals("127.0.0.1", loginInfo.getIpAddress());
            assertEquals("本地", loginInfo.getLoginLocation());
            assertEquals("Chrome", loginInfo.getBrowser());
            assertEquals("Windows 10", loginInfo.getOs());
            assertEquals("登录成功", loginInfo.getMsg());
            assertEquals(now, loginInfo.getLoginTime());
        }
    }

    @Nested
    @DisplayName("Equals 和 HashCode 测试")
    class EqualsHashCodeTest {

        @Test
        @DisplayName("相同的对象应该相等且有相同的 hashCode")
        void testEqualsHashCodeSameObject() {
            SysLoginInfo info1 = new SysLoginInfo();
            info1.setInfoId(1L);
            info1.setUserName("admin");

            SysLoginInfo info2 = new SysLoginInfo();
            info2.setInfoId(1L);
            info2.setUserName("admin");

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("不同的对象不应该相等")
        void testEqualsDifferentObject() {
            SysLoginInfo info1 = new SysLoginInfo();
            info1.setInfoId(1L);

            SysLoginInfo info2 = new SysLoginInfo();
            info2.setInfoId(2L);

            assertNotEquals(info1, info2);
        }
    }

    @Nested
    @DisplayName("状态枚举测试")
    class StatusTest {

        @Test
        @DisplayName("状态应该正确对应")
        void testStatus() {
            assertEquals(0, SysLoginInfo.Status.SUCCESS.getCode());
            assertEquals("成功", SysLoginInfo.Status.SUCCESS.getName());

            assertEquals(1, SysLoginInfo.Status.FAIL.getCode());
            assertEquals("失败", SysLoginInfo.Status.FAIL.getName());
        }
    }
}
