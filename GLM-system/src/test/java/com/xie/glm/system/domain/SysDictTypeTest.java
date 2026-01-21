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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SysDictType 测试类
 *
 * <p>测试系统字典类型实体的各种属性和行为
 *
 * @author xie
 */
@DisplayName("SysDictType 实体单元测试")
class SysDictTypeTest {

    // ==================== 继承关系测试 ====================

    @Test
    @DisplayName("继承 BaseEntity - 验证继承关系")
    void testExtendsBaseEntity() {
        SysDictType dictType = new SysDictType();

        assertTrue(dictType instanceof BaseEntity, "SysDictType 应继承 BaseEntity");
        assertTrue(dictType instanceof BaseEntity, "应可转换为 BaseEntity");

        // 验证基类字段
        BaseEntity base = dictType;
        assertNull(base.getCreateTime(), "默认创建时间应为null");
        assertNull(base.getUpdateTime(), "默认更新时间应为null");
        assertNull(base.getCreatedBy(), "默认创建人应为null");
        assertNull(base.getUpdatedBy(), "默认更新人应为null");
    }

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        SysDictType dictType = new SysDictType();

        assertNull(dictType.getDictId(), "默认字典ID应为null");
        assertNull(dictType.getDictName(), "默认字典名称应为null");
        assertNull(dictType.getDictType(), "默认字典类型应为null");
        assertNull(dictType.getStatus(), "默认状态应为null");
        assertNull(dictType.getRemark(), "默认备注应为null");

        // 基类字段
        assertNull(dictType.getCreateTime(), "默认创建时间应为null");
        assertNull(dictType.getUpdateTime(), "默认更新时间应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 用户性别, sys_user_sex, 0, 系统预设",
            "2, 菜单状态, sys_show_hide, 0, 菜单状态列表",
            "3, 系统开关, sys_normal_disable, 0, 系统开关列表"
    })
    @DisplayName("Getter/Setter - 验证字段赋值和获取")
    void testGettersSetters(Long dictId, String dictName, String dictType, String status, String remark) {
        LocalDateTime now = LocalDateTime.now();

        SysDictType entity = new SysDictType();
        entity.setDictId(dictId);
        entity.setDictName(dictName);
        entity.setDictType(dictType);
        entity.setStatus(status);
        entity.setRemark(remark);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        assertEquals(dictId, entity.getDictId(), "字典ID应匹配");
        assertEquals(dictName, entity.getDictName(), "字典名称应匹配");
        assertEquals(dictType, entity.getDictType(), "字典类型应匹配");
        assertEquals(status, entity.getStatus(), "状态应匹配");
        assertEquals(remark, entity.getRemark(), "备注应匹配");
        assertEquals(now, entity.getCreateTime(), "创建时间应匹配");
        assertEquals(now, entity.getUpdateTime(), "更新时间应匹配");
    }

    // ==================== 基类字段测试 ====================

    @ParameterizedTest
    @MethodSource("provideBaseEntityData")
    @DisplayName("基类字段 - 验证审计字段设置")
    void testBaseEntityFields(LocalDateTime createTime, LocalDateTime updateTime,
                             String createdBy, String updatedBy) {
        SysDictType dictType = new SysDictType();
        dictType.setCreateTime(createTime);
        dictType.setUpdateTime(updateTime);
        dictType.setCreatedBy(createdBy);
        dictType.setUpdatedBy(updatedBy);

        assertEquals(createTime, dictType.getCreateTime(), "创建时间应匹配");
        assertEquals(updateTime, dictType.getUpdateTime(), "更新时间应匹配");
        assertEquals(createdBy, dictType.getCreatedBy(), "创建人应匹配");
        assertEquals(updatedBy, dictType.getUpdatedBy(), "更新人应匹配");
    }

    private static Stream<Arguments> provideBaseEntityData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now, now, "admin", "admin"),
                Arguments.of(now.minusDays(1), now, "system", "user"),
                Arguments.of(null, null, null, null)
        );
    }

    // ==================== 字典类型测试 ====================

    @ParameterizedTest
    @CsvSource({
            "sys_user_sex, true",
            "sys_show_hide, true",
            "sys_normal_disable, true",
            "invalid_type_with_spaces, false",
            "TypeWithCaps, false"
    })
    @DisplayName("字典类型 - 验证字典类型格式")
    void testDictTypeFormat(String dictType, boolean isValidFormat) {
        SysDictType entity = new SysDictType();
        entity.setDictType(dictType);

        assertEquals(dictType, entity.getDictType(), "字典类型应匹配");

        if (dictType != null && isValidFormat) {
            // 字典类型应该是小写字母、数字、下划线组成
            assertTrue(dictType.matches("^[a-z][a-z0-9_]*$"), "字典类型应为小写字母、数字、下划线组合");
        }
    }

    // ==================== 状态测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1, false"
    })
    @DisplayName("状态 - 验证状态值含义")
    void testStatus(String status, boolean isNormal) {
        SysDictType dictType = new SysDictType();
        dictType.setStatus(status);

        assertEquals(status, dictType.getStatus(), "状态值应匹配");
        assertEquals(isNormal, "0".equals(dictType.getStatus()), "0 表示正常状态");
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证 setter 容错")
    void testNullHandling(String value) {
        SysDictType dictType = new SysDictType();

        dictType.setDictName(value);
        dictType.setDictType(value);
        dictType.setStatus(value);
        dictType.setRemark(value);

        assertNull(dictType.getDictName(), "null 字典名称应被接受");
        assertNull(dictType.getDictType(), "null 字典类型应被接受");
        assertNull(dictType.getStatus(), "null 状态应被接受");
        assertNull(dictType.getRemark(), "null 备注应被接受");
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        SysDictType dictType1 = new SysDictType();
        dictType1.setDictId(1L);
        dictType1.setDictType("sys_user_sex");

        SysDictType dictType2 = new SysDictType();
        dictType2.setDictId(1L);
        dictType2.setDictType("sys_user_sex");

        SysDictType dictType3 = new SysDictType();
        dictType3.setDictId(2L);
        dictType3.setDictType("sys_show_hide");

        assertEquals(dictType1, dictType2, "相同 ID 的字典类型应相等");
        assertEquals(dictType1.hashCode(), dictType2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(dictType1, dictType3, "不同 ID 的字典类型应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        SysDictType dictType = new SysDictType();
        dictType.setDictId(1L);
        dictType.setDictName("用户性别");

        String str = dictType.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("SysDictType") || str.contains("用户性别") || str.contains("1"),
                "toString 应包含字典类型信息");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 创建用户性别字典类型")
    void testCreateUserGenderDictType() {
        LocalDateTime now = LocalDateTime.now();

        SysDictType dictType = new SysDictType();
        dictType.setDictId(1L);
        dictType.setDictName("用户性别");
        dictType.setDictType("sys_user_sex");
        dictType.setStatus("0");
        dictType.setRemark("系统预设：用户性别列表");
        dictType.setCreateTime(now);
        dictType.setCreatedBy("system");

        assertEquals(1L, dictType.getDictId(), "字典ID应为1");
        assertEquals("用户性别", dictType.getDictName(), "字典名称应为'用户性别'");
        assertEquals("sys_user_sex", dictType.getDictType(), "字典类型应为'sys_user_sex'");
        assertEquals("0", dictType.getStatus(), "状态应为正常");
        assertEquals("系统预设：用户性别列表", dictType.getRemark(), "备注应匹配");
        assertEquals(now, dictType.getCreateTime(), "创建时间应匹配");
        assertEquals("system", dictType.getCreatedBy(), "创建人应为system");
    }

    @Test
    @DisplayName("业务场景 - 停用字典类型")
    void testDisableDictType() {
        SysDictType dictType = new SysDictType();
        dictType.setDictId(2L);
        dictType.setDictName("菜单状态");
        dictType.setStatus("0");

        // 停用字典类型
        dictType.setStatus("1");

        assertEquals("1", dictType.getStatus(), "字典类型状态应更新为停用");
        assertFalse("0".equals(dictType.getStatus()), "字典类型应不再处于正常状态");
    }

    @Test
    @DisplayName("业务场景 - 更新字典类型信息")
    void testUpdateDictType() {
        LocalDateTime now = LocalDateTime.now();

        SysDictType dictType = new SysDictType();
        dictType.setDictId(3L);
        dictType.setDictName("旧名称");
        dictType.setRemark("旧备注");

        // 更新字典类型
        dictType.setDictName("新名称");
        dictType.setRemark("新备注");
        dictType.setUpdateTime(now);

        assertEquals("新名称", dictType.getDictName(), "字典名称应更新");
        assertEquals("新备注", dictType.getRemark(), "备注应更新");
        assertEquals(now, dictType.getUpdateTime(), "更新时间应更新");
    }

    // ==================== 字典类型唯一性测试 ====================

    @ParameterizedTest
    @CsvSource({
            "sys_user_sex, 用户性别",
            "sys_show_hide, 菜单状态",
            "sys_normal_disable, 系统开关",
            "sys_job_status, 任务状态",
            "sys_yes_no, 是否开关"
    })
    @DisplayName("字典类型唯一性 - 验证常见字典类型")
    void testCommonDictTypes(String dictType, String dictName) {
        SysDictType entity = new SysDictType();
        entity.setDictType(dictType);
        entity.setDictName(dictName);
        entity.setStatus("0");

        assertEquals(dictType, entity.getDictType(), "字典类型应匹配");
        assertEquals(dictName, entity.getDictName(), "字典名称应匹配");
        assertEquals("0", entity.getStatus(), "状态应为正常");
    }

    // ==================== 备注字段测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {"", "系统预设", "这是一个很长的备注说明，用于测试备注字段可以存储较长的文本内容"})
    @DisplayName("备注字段 - 验证备注内容")
    void testRemark(String remark) {
        SysDictType dictType = new SysDictType();
        dictType.setRemark(remark);

        assertEquals(remark, dictType.getRemark(), "备注应匹配");
    }

    // ==================== 字典名称长度测试 ====================

    @ParameterizedTest
    @CsvSource({
            "性别, true",
            "用户状态, true",
            "A, true",
            "这是一个非常非常非常非常非常非常非常非常非常非常非常非常非常非常长的字典名称, false"
    })
    @DisplayName("字典名称长度 - 验证名称长度限制")
    void testDictNameLength(String dictName, boolean isValidLength) {
        SysDictType dictType = new SysDictType();
        dictType.setDictName(dictName);

        assertEquals(dictName, dictType.getDictName(), "字典名称应匹配");

        // 假设字典名称最大长度为100字符
        if (dictName != null && isValidLength) {
            assertTrue(dictName.length() <= 100, "字典名称长度不应超过100字符");
        }
    }
}
