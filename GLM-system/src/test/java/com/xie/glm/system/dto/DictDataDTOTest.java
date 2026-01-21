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
 * DictDataDTO 测试类
 *
 * <p>测试字典数据传输对象的各种属性和行为
 *
 * @author xie
 */
@DisplayName("DictDataDTO 数据传输对象单元测试")
class DictDataDTOTest {

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        DictDataDTO dto = new DictDataDTO();

        assertNull(dto.getDictCode(), "默认字典编码应为null");
        assertNull(dto.getDictSort(), "默认字典排序应为null");
        assertNull(dto.getDictLabel(), "默认字典标签应为null");
        assertNull(dto.getDictValue(), "默认字典键值应为null");
        assertNull(dto.getDictType(), "默认字典类型应为null");
        assertNull(dto.getCssClass(), "默认样式属性应为null");
        assertNull(dto.getListClass(), "默认表格回显样式应为null");
        assertNull(dto.getIsDefault(), "默认是否默认应为null");
        assertNull(dto.getStatus(), "默认状态应为null");
        assertNull(dto.getRemark(), "默认备注应为null");
        assertNull(dto.getCreateTime(), "默认创建时间应为null");
        assertNull(dto.getUpdateTime(), "默认更新时间应为null");
    }

    // ==================== Getter/Setter 测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, 1, 男, 0, sys_user_sex, default, , Y, 0, 男",
            "2, 2, 女, 1, sys_user_sex, default, , N, 0, 女",
            "3, 1, 显示, 0, sys_show_hide, primary, , N, 0, 显示",
            "4, 2, 隐藏, 1, sys_show_hide, danger, , N, 0, 隐藏"
    })
    @DisplayName("Getter/Setter - 验证字段赋值和获取")
    void testGettersSetters(Long dictCode, Integer dictSort, String dictLabel, String dictValue,
                           String dictType, String cssClass, String listClass, String isDefault, String status, String remark) {
        LocalDateTime now = LocalDateTime.now();

        DictDataDTO dto = new DictDataDTO();
        dto.setDictCode(dictCode);
        dto.setDictSort(dictSort);
        dto.setDictLabel(dictLabel);
        dto.setDictValue(dictValue);
        dto.setDictType(dictType);
        dto.setCssClass(cssClass);
        dto.setListClass(listClass);
        dto.setIsDefault(isDefault);
        dto.setStatus(status);
        dto.setRemark(remark);
        dto.setCreateTime(now);
        dto.setUpdateTime(now);

        assertEquals(dictCode, dto.getDictCode(), "字典编码应匹配");
        assertEquals(dictSort, dto.getDictSort(), "字典排序应匹配");
        assertEquals(dictLabel, dto.getDictLabel(), "字典标签应匹配");
        assertEquals(dictValue, dto.getDictValue(), "字典键值应匹配");
        assertEquals(dictType, dto.getDictType(), "字典类型应匹配");
        assertEquals(cssClass, dto.getCssClass(), "样式属性应匹配");
        assertEquals(listClass, dto.getListClass(), "表格回显样式应匹配");
        assertEquals(isDefault, dto.getIsDefault(), "是否默认应匹配");
        assertEquals(status, dto.getStatus(), "状态应匹配");
        assertEquals(remark, dto.getRemark(), "备注应匹配");
        assertEquals(now, dto.getCreateTime(), "创建时间应匹配");
        assertEquals(now, dto.getUpdateTime(), "更新时间应匹配");
    }

    // ==================== 全参构造方法测试 ====================

    @ParameterizedTest
    @MethodSource("provideConstructorData")
    @DisplayName("全参构造方法 - 验证字段初始化")
    void testAllArgsConstructor(Long dictCode, Integer dictSort, String dictLabel, String dictValue,
                               String dictType, String cssClass, String listClass,
                               String isDefault, String status, String remark,
                               LocalDateTime createTime, LocalDateTime updateTime) {
        DictDataDTO dto = new DictDataDTO(dictCode, dictSort, dictLabel, dictValue, dictType,
                cssClass, listClass, isDefault, status, remark, createTime, updateTime);

        assertEquals(dictCode, dto.getDictCode(), "字典编码应匹配");
        assertEquals(dictSort, dto.getDictSort(), "字典排序应匹配");
        assertEquals(dictLabel, dto.getDictLabel(), "字典标签应匹配");
        assertEquals(dictValue, dto.getDictValue(), "字典键值应匹配");
        assertEquals(dictType, dto.getDictType(), "字典类型应匹配");
        assertEquals(cssClass, dto.getCssClass(), "样式属性应匹配");
        assertEquals(listClass, dto.getListClass(), "表格回显样式应匹配");
        assertEquals(isDefault, dto.getIsDefault(), "是否默认应匹配");
        assertEquals(status, dto.getStatus(), "状态应匹配");
        assertEquals(remark, dto.getRemark(), "备注应匹配");
        assertEquals(createTime, dto.getCreateTime(), "创建时间应匹配");
        assertEquals(updateTime, dto.getUpdateTime(), "更新时间应匹配");
    }

    private static Stream<Arguments> provideConstructorData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(1L, 1, "男", "0", "sys_user_sex", "default", "", "Y", "0", "男", now, now),
                Arguments.of(2L, 2, "女", "1", "sys_user_sex", "default", "", "N", "0", "女", now.minusDays(1), now),
                Arguments.of(3L, 1, "显示", "0", "sys_show_hide", "primary", "", "N", "0", "显示", null, null)
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
        DictDataDTO dto = new DictDataDTO();
        dto.setStatus(status);

        assertEquals(status, dto.getStatus(), "状态值应匹配");
        assertEquals(isNormal, "0".equals(dto.getStatus()), "0 表示正常状态");
    }

    // ==================== 默认值标识测试 ====================

    @ParameterizedTest
    @CsvSource({
            "Y, true",
            "N, false",
            "y, true",
            "n, false"
    })
    @DisplayName("是否默认 - 验证默认值标识")
    void testIsDefault(String isDefault, boolean isDefaultFlag) {
        DictDataDTO dto = new DictDataDTO();
        dto.setIsDefault(isDefault);

        assertEquals(isDefault, dto.getIsDefault(), "是否默认应匹配");
        assertEquals(isDefaultFlag, "Y".equalsIgnoreCase(dto.getIsDefault()), "Y 表示默认值");
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证 setter 容错")
    void testNullHandling(String value) {
        DictDataDTO dto = new DictDataDTO();

        dto.setDictLabel(value);
        dto.setDictValue(value);
        dto.setDictType(value);
        dto.setCssClass(value);
        dto.setListClass(value);
        dto.setIsDefault(value);
        dto.setStatus(value);
        dto.setRemark(value);

        assertNull(dto.getDictLabel(), "null 字典标签应被接受");
        assertNull(dto.getDictValue(), "null 字典键值应被接受");
        assertNull(dto.getDictType(), "null 字典类型应被接受");
        assertNull(dto.getCssClass(), "null 样式类应被接受");
        assertNull(dto.getListClass(), "null 表格样式应被接受");
        assertNull(dto.getIsDefault(), "null 是否默认应被接受");
        assertNull(dto.getStatus(), "null 状态应被接受");
        assertNull(dto.getRemark(), "null 备注应被接受");
    }

    // ==================== Builder 模式测试 ====================

    @Test
    @DisplayName("Builder 模式 - 验证链式构建")
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();

        DictDataDTO dto = DictDataDTO.builder()
                .dictCode(1L)
                .dictSort(1)
                .dictLabel("男")
                .dictValue("0")
                .dictType("sys_user_sex")
                .cssClass("default")
                .listClass("")
                .isDefault("Y")
                .status("0")
                .remark("性别男")
                .createTime(now)
                .updateTime(now)
                .build();

        assertEquals(1L, dto.getDictCode(), "字典编码应匹配");
        assertEquals(1, dto.getDictSort(), "字典排序应匹配");
        assertEquals("男", dto.getDictLabel(), "字典标签应匹配");
        assertEquals("0", dto.getDictValue(), "字典键值应匹配");
        assertEquals("sys_user_sex", dto.getDictType(), "字典类型应匹配");
        assertEquals("default", dto.getCssClass(), "样式类应匹配");
        assertEquals("", dto.getListClass(), "表格样式应匹配");
        assertEquals("Y", dto.getIsDefault(), "是否默认应匹配");
        assertEquals("0", dto.getStatus(), "状态应匹配");
        assertEquals("性别男", dto.getRemark(), "备注应匹配");
        assertEquals(now, dto.getCreateTime(), "创建时间应匹配");
        assertEquals(now, dto.getUpdateTime(), "更新时间应匹配");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 创建用户性别字典数据DTO")
    void testCreateUserGenderDictDataDTO() {
        LocalDateTime now = LocalDateTime.now();

        DictDataDTO dto = DictDataDTO.builder()
                .dictCode(1L)
                .dictSort(1)
                .dictLabel("男")
                .dictValue("0")
                .dictType("sys_user_sex")
                .cssClass("default")
                .listClass("")
                .isDefault("Y")
                .status("0")
                .remark("性别男")
                .createTime(now)
                .build();

        assertEquals(1L, dto.getDictCode(), "字典编码应为1");
        assertEquals(1, dto.getDictSort(), "排序应为1");
        assertEquals("男", dto.getDictLabel(), "字典标签应为'男'");
        assertEquals("0", dto.getDictValue(), "字典键值应为'0'");
        assertEquals("sys_user_sex", dto.getDictType(), "字典类型应为'sys_user_sex'");
        assertEquals("default", dto.getCssClass(), "样式类应为'default'");
        assertEquals("Y", dto.getIsDefault(), "应为默认值");
        assertEquals("0", dto.getStatus(), "状态应为正常");
        assertEquals("性别男", dto.getRemark(), "备注应匹配");
        assertEquals(now, dto.getCreateTime(), "创建时间应匹配");
    }

    @Test
    @DisplayName("业务场景 - 停用字典数据DTO")
    void testDisableDictDataDTO() {
        DictDataDTO dto = new DictDataDTO();
        dto.setDictCode(2L);
        dto.setDictLabel("女");
        dto.setStatus("0");

        // 停用字典数据
        dto.setStatus("1");

        assertEquals("1", dto.getStatus(), "字典数据状态应更新为停用");
        assertFalse("0".equals(dto.getStatus()), "字典数据应不再处于正常状态");
    }

    @Test
    @DisplayName("业务场景 - 更新字典数据DTO")
    void testUpdateDictDataDTO() {
        LocalDateTime now = LocalDateTime.now();

        DictDataDTO dto = new DictDataDTO();
        dto.setDictCode(3L);
        dto.setDictLabel("旧标签");
        dto.setDictSort(1);
        dto.setRemark("旧备注");

        // 更新字典数据
        dto.setDictLabel("新标签");
        dto.setDictSort(2);
        dto.setRemark("新备注");
        dto.setUpdateTime(now);

        assertEquals("新标签", dto.getDictLabel(), "字典标签应更新");
        assertEquals(2, dto.getDictSort(), "排序应更新");
        assertEquals("新备注", dto.getRemark(), "备注应更新");
        assertEquals(now, dto.getUpdateTime(), "更新时间应更新");
    }

    // ==================== 字典数据关联测试 ====================

    @Test
    @DisplayName("字典数据关联 - 验证与字典类型的关系")
    void testDictDataRelation() {
        DictDataDTO dto = DictDataDTO.builder()
                .dictCode(1L)
                .dictType("sys_user_sex")
                .dictLabel("男")
                .dictValue("0")
                .build();

        assertEquals("sys_user_sex", dto.getDictType(), "字典数据应关联字典类型");
        assertNotNull(dto.getDictType(), "字典类型不应为null");
        assertEquals("男", dto.getDictLabel(), "字典标签应正确");
        assertEquals("0", dto.getDictValue(), "字典键值应正确");
    }

    // ==================== 排序测试 ====================

    @Test
    @DisplayName("排序功能 - 验证多个字典数据的排序")
    void testDictSortOrder() {
        DictDataDTO dto1 = DictDataDTO.builder()
                .dictCode(1L)
                .dictSort(1)
                .dictLabel("第一")
                .build();

        DictDataDTO dto2 = DictDataDTO.builder()
                .dictCode(2L)
                .dictSort(2)
                .dictLabel("第二")
                .build();

        DictDataDTO dto3 = DictDataDTO.builder()
                .dictCode(3L)
                .dictSort(3)
                .dictLabel("第三")
                .build();

        assertTrue(dto1.getDictSort() < dto2.getDictSort(), "第一个排序应小于第二个");
        assertTrue(dto2.getDictSort() < dto3.getDictSort(), "第二个排序应小于第三个");
    }

    // ==================== 序列化测试 ====================

    @Test
    @DisplayName("serialVersionUID - 验证序列化兼容性")
    void testSerialVersionUID() {
        DictDataDTO dto = new DictDataDTO();

        try {
            java.lang.reflect.Field field = dto.getClass().getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            assertNotNull(field.get(null), "DictDataDTO 应有 serialVersionUID 字段");
        } catch (NoSuchFieldException e) {
            fail("DictDataDTO 应该有 serialVersionUID 字段");
        } catch (IllegalAccessException e) {
            fail("访问 serialVersionUID 失败");
        }
    }

    // ==================== equals/hashCode/toString 测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        DictDataDTO dto1 = DictDataDTO.builder()
                .dictCode(1L)
                .dictType("sys_user_sex")
                .build();

        DictDataDTO dto2 = DictDataDTO.builder()
                .dictCode(1L)
                .dictType("sys_user_sex")
                .build();

        DictDataDTO dto3 = DictDataDTO.builder()
                .dictCode(2L)
                .dictType("sys_show_hide")
                .build();

        assertEquals(dto1, dto2, "相同编码的 DTO 应相等");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(dto1, dto3, "不同编码的 DTO 应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        DictDataDTO dto = DictDataDTO.builder()
                .dictCode(1L)
                .dictLabel("男")
                .build();

        String str = dto.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("DictDataDTO") || str.contains("男") || str.contains("1"),
                "toString 应包含字典数据信息");
    }
}
