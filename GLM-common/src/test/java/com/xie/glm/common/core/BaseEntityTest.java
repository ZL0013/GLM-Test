package com.xie.glm.common.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BaseEntity 实体基类测试
 * <p>
 * 测试原则：
 * 1. 参数化测试覆盖字段赋值场景
 * 2. 测试 Lombok @Data 注解生成的 getter/setter
 * 3. 验证所有审计字段的功能
 *
 * @author xie
 */
class BaseEntityTest {

    /**
     * 测试默认构造方法
     */
    @Test
    void testDefaultConstructor() {
        BaseEntity entity = new BaseEntity();

        assertNull(entity.getCreateTime());
        assertNull(entity.getUpdateTime());
        assertNull(entity.getCreatedBy());
        assertNull(entity.getUpdatedBy());
    }

    /**
     * 测试全参数构造方法
     */
    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        BaseEntity entity = new BaseEntity(now, now, "admin", "admin");

        assertEquals(now, entity.getCreateTime());
        assertEquals(now, entity.getUpdateTime());
        assertEquals("admin", entity.getCreatedBy());
        assertEquals("admin", entity.getUpdatedBy());
    }

    /**
     * 参数化测试：Setter 方法
     * <p>
     * 测试数据：
     * - 标准场景
     * - null 值场景
     */
    @ParameterizedTest
    @MethodSource("provideEntityData")
    void testSetters(LocalDateTime createTime, LocalDateTime updateTime,
                     String createdBy, String updatedBy) {
        BaseEntity entity = new BaseEntity();

        entity.setCreateTime(createTime);
        entity.setUpdateTime(updateTime);
        entity.setCreatedBy(createdBy);
        entity.setUpdatedBy(updatedBy);

        assertEquals(createTime, entity.getCreateTime());
        assertEquals(updateTime, entity.getUpdateTime());
        assertEquals(createdBy, entity.getCreatedBy());
        assertEquals(updatedBy, entity.getUpdatedBy());
    }

    /**
     * 测试 toString 方法
     */
    @Test
    void testToString() {
        BaseEntity entity = new BaseEntity();
        entity.setCreatedBy("admin");
        entity.setUpdatedBy("user1");

        String toString = entity.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("admin"));
        assertTrue(toString.contains("user1"));
    }

    /**
     * 测试 equals 和 hashCode 方法
     */
    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        BaseEntity entity1 = new BaseEntity(now, now, "admin", "admin");
        BaseEntity entity2 = new BaseEntity(now, now, "admin", "admin");
        BaseEntity entity3 = new BaseEntity(now, now, "user1", "user1");

        // 相同值应该相等
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());

        // 不同值不应该相等
        assertNotEquals(entity1, entity3);
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    /**
     * 测试 canEqual 方法
     */
    @Test
    void testCanEqual() {
        BaseEntity entity = new BaseEntity();
        BaseEntity other = new BaseEntity();

        assertTrue(entity.canEqual(other));
        assertFalse(entity.canEqual("not an entity"));
    }

    // ==================== 测试数据提供方法 ====================

    /**
     * 提供实体测试数据
     */
    private static Stream<Arguments> provideEntityData() {
        LocalDateTime now = LocalDateTime.now();

        return Stream.of(
                Arguments.of(now, now, "admin", "admin"),
                Arguments.of(null, null, null, null),
                Arguments.of(now.minusHours(1), now, "user1", "user2")
        );
    }
}
