package com.xie.glm.system.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DictTypeDTO 测试类
 *
 * <p>测试字典类型数据传输对象的各种属性和行为
 *
 * @author xie
 */
@DisplayName("DictTypeDTO 数据传输对象单元测试")
class DictTypeDTOTest {

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        DictTypeDTO dto = new DictTypeDTO();

        assertNull(dto.getDictId(), "默认字典ID应为null");
        assertNull(dto.getDictName(), "默认字典名称应为null");
        assertNull(dto.getDictType(), "默认字典类型应为null");
        assertNull(dto.getStatus(), "默认状态应为null");
        assertNull(dto.getRemark(), "默认备注应为null");
        assertNull(dto.getCreateTime(), "默认创建时间应为null");
        assertNull(dto.getUpdateTime(), "默认更新时间应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 用户性别, sys_user_sex, 0, 系统预设",
            "2, 菜单状态, sys_show_hide, 0, 菜单状态列表",
            "3, 系统开关, sys_normal_disable, 1, 系统开关列表"
    })
    @DisplayName("Getter/Setter - 验证字段赋值和获取")
    void testGettersSetters(Long dictId, String dictName, String dictType, String status, String remark) {
        LocalDateTime now = LocalDateTime.now();

        DictTypeDTO dto = new DictTypeDTO();
        dto.setDictId(dictId);
        dto.setDictName(dictName);
        dto.setDictType(dictType);
        dto.setStatus(status);
        dto.setRemark(remark);
        dto.setCreateTime(now);
        dto.setUpdateTime(now);

        assertEquals(dictId, dto.getDictId(), "字典ID应匹配");
        assertEquals(dictName, dto.getDictName(), "字典名称应匹配");
        assertEquals(dictType, dto.getDictType(), "字典类型应匹配");
        assertEquals(status, dto.getStatus(), "状态应匹配");
        assertEquals(remark, dto.getRemark(), "备注应匹配");
        assertEquals(now, dto.getCreateTime(), "创建时间应匹配");
        assertEquals(now, dto.getUpdateTime(), "更新时间应匹配");
    }

    // ==================== 全参构造方法测试 ====================

    @ParameterizedTest
    @MethodSource("provideConstructorData")
    @DisplayName("全参构造方法 - 验证字段初始化")
    void testAllArgsConstructor(Long dictId, String dictName, String dictType,
                               String status, String remark,
                               LocalDateTime createTime, LocalDateTime updateTime) {
        DictTypeDTO dto = new DictTypeDTO(dictId, dictName, dictType, status, remark, createTime, updateTime);

        assertEquals(dictId, dto.getDictId(), "字典ID应匹配");
        assertEquals(dictName, dto.getDictName(), "字典名称应匹配");
        assertEquals(dictType, dto.getDictType(), "字典类型应匹配");
        assertEquals(status, dto.getStatus(), "状态应匹配");
        assertEquals(remark, dto.getRemark(), "备注应匹配");
        assertEquals(createTime, dto.getCreateTime(), "创建时间应匹配");
        assertEquals(updateTime, dto.getUpdateTime(), "更新时间应匹配");
    }

    private static Stream<Arguments> provideConstructorData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(1L, "用户性别", "sys_user_sex", "0", "系统预设", now, now),
                Arguments.of(2L, "菜单状态", "sys_show_hide", "0", "菜单状态列表", now.minusDays(1), now),
                Arguments.of(3L, "系统开关", "sys_normal_disable", "1", "系统开关列表", null, null)
        );
    }

    // ==================== 状态测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1, false"
    })
    @DisplayName("状态 - 验证状态值含义")
    void testStatus(String status, boolean isNormal) {
        DictTypeDTO dto = new DictTypeDTO();
        dto.setStatus(status);

        assertEquals(status, dto.getStatus(), "状态值应匹配");
        assertEquals(isNormal, "0".equals(dto.getStatus()), "0 表示正常状态");
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证 setter 容错")
    void testNullHandling(String value) {
        DictTypeDTO dto = new DictTypeDTO();

        dto.setDictName(value);
        dto.setDictType(value);
        dto.setStatus(value);
        dto.setRemark(value);

        assertNull(dto.getDictName(), "null 字典名称应被接受");
        assertNull(dto.getDictType(), "null 字典类型应被接受");
        assertNull(dto.getStatus(), "null 状态应被接受");
        assertNull(dto.getRemark(), "null 备注应被接受");
    }

    // ==================== Builder 模式测试 ====================

    @Test
    @DisplayName("Builder 模式 - 验证链式构建")
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();

        DictTypeDTO dto = DictTypeDTO.builder()
                .dictId(1L)
                .dictName("用户性别")
                .dictType("sys_user_sex")
                .status("0")
                .remark("系统预设")
                .createTime(now)
                .updateTime(now)
                .build();

        assertEquals(1L, dto.getDictId(), "字典ID应匹配");
        assertEquals("用户性别", dto.getDictName(), "字典名称应匹配");
        assertEquals("sys_user_sex", dto.getDictType(), "字典类型应匹配");
        assertEquals("0", dto.getStatus(), "状态应匹配");
        assertEquals("系统预设", dto.getRemark(), "备注应匹配");
        assertEquals(now, dto.getCreateTime(), "创建时间应匹配");
        assertEquals(now, dto.getUpdateTime(), "更新时间应匹配");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 创建用户性别字典类型DTO")
    void testCreateUserGenderDictTypeDTO() {
        LocalDateTime now = LocalDateTime.now();

        DictTypeDTO dto = DictTypeDTO.builder()
                .dictId(1L)
                .dictName("用户性别")
                .dictType("sys_user_sex")
                .status("0")
                .remark("系统预设：用户性别列表")
                .createTime(now)
                .build();

        assertEquals(1L, dto.getDictId(), "字典ID应为1");
        assertEquals("用户性别", dto.getDictName(), "字典名称应为'用户性别'");
        assertEquals("sys_user_sex", dto.getDictType(), "字典类型应为'sys_user_sex'");
        assertEquals("0", dto.getStatus(), "状态应为正常");
        assertEquals("系统预设：用户性别列表", dto.getRemark(), "备注应匹配");
        assertEquals(now, dto.getCreateTime(), "创建时间应匹配");
    }

    @Test
    @DisplayName("业务场景 - 停用字典类型DTO")
    void testDisableDictTypeDTO() {
        DictTypeDTO dto = new DictTypeDTO();
        dto.setDictId(2L);
        dto.setDictName("菜单状态");
        dto.setStatus("0");

        // 停用字典类型
        dto.setStatus("1");

        assertEquals("1", dto.getStatus(), "字典类型状态应更新为停用");
        assertFalse("0".equals(dto.getStatus()), "字典类型应不再处于正常状态");
    }

    @Test
    @DisplayName("业务场景 - 更新字典类型DTO")
    void testUpdateDictTypeDTO() {
        LocalDateTime now = LocalDateTime.now();

        DictTypeDTO dto = new DictTypeDTO();
        dto.setDictId(3L);
        dto.setDictName("旧名称");
        dto.setRemark("旧备注");

        // 更新字典类型
        dto.setDictName("新名称");
        dto.setRemark("新备注");
        dto.setUpdateTime(now);

        assertEquals("新名称", dto.getDictName(), "字典名称应更新");
        assertEquals("新备注", dto.getRemark(), "备注应更新");
        assertEquals(now, dto.getUpdateTime(), "更新时间应更新");
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
        DictTypeDTO dto = DictTypeDTO.builder()
                .dictType(dictType)
                .dictName(dictName)
                .status("0")
                .build();

        assertEquals(dictType, dto.getDictType(), "字典类型应匹配");
        assertEquals(dictName, dto.getDictName(), "字典名称应匹配");
        assertEquals("0", dto.getStatus(), "状态应为正常");
    }

    // ==================== 序列化测试 ====================

    @Test
    @DisplayName("serialVersionUID - 验证序列化兼容性")
    void testSerialVersionUID() {
        DictTypeDTO dto = new DictTypeDTO();

        try {
            java.lang.reflect.Field field = dto.getClass().getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            assertNotNull(field.get(null), "DictTypeDTO 应有 serialVersionUID 字段");
        } catch (NoSuchFieldException e) {
            fail("DictTypeDTO 应该有 serialVersionUID 字段");
        } catch (IllegalAccessException e) {
            fail("访问 serialVersionUID 失败");
        }
    }

    // ==================== equals/hashCode/toString 测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        DictTypeDTO dto1 = DictTypeDTO.builder()
                .dictId(1L)
                .dictType("sys_user_sex")
                .build();

        DictTypeDTO dto2 = DictTypeDTO.builder()
                .dictId(1L)
                .dictType("sys_user_sex")
                .build();

        DictTypeDTO dto3 = DictTypeDTO.builder()
                .dictId(2L)
                .dictType("sys_show_hide")
                .build();

        assertEquals(dto1, dto2, "相同 ID 的 DTO 应相等");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(dto1, dto3, "不同 ID 的 DTO 应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        DictTypeDTO dto = DictTypeDTO.builder()
                .dictId(1L)
                .dictName("用户性别")
                .build();

        String str = dto.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("DictTypeDTO") || str.contains("用户性别") || str.contains("1"),
                "toString 应包含字典类型信息");
    }
}
