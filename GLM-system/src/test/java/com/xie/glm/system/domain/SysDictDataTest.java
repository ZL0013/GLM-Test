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
 * SysDictData 测试类
 *
 * <p>测试系统字典数据实体的各种属性和行为
 *
 * @author xie
 */
@DisplayName("SysDictData 实体单元测试")
class SysDictDataTest {

    // ==================== 继承关系测试 ====================

    @Test
    @DisplayName("继承 BaseEntity - 验证继承关系")
    void testExtendsBaseEntity() {
        SysDictData dictData = new SysDictData();

        assertTrue(dictData instanceof BaseEntity, "SysDictData 应继承 BaseEntity");
        assertTrue(dictData instanceof BaseEntity, "应可转换为 BaseEntity");

        // 验证基类字段
        BaseEntity base = dictData;
        assertNull(base.getCreateTime(), "默认创建时间应为null");
        assertNull(base.getUpdateTime(), "默认更新时间应为null");
        assertNull(base.getCreatedBy(), "默认创建人应为null");
        assertNull(base.getUpdatedBy(), "默认更新人应为null");
    }

    // ==================== 默认构造方法测试 ====================

    @Test
    @DisplayName("默认构造方法 - 验证字段初始化")
    void testDefaultConstructor() {
        SysDictData dictData = new SysDictData();

        assertNull(dictData.getDictCode(), "默认字典编码应为null");
        assertNull(dictData.getDictSort(), "默认字典排序应为null");
        assertNull(dictData.getDictLabel(), "默认字典标签应为null");
        assertNull(dictData.getDictValue(), "默认字典键值应为null");
        assertNull(dictData.getDictType(), "默认字典类型应为null");
        assertNull(dictData.getCssClass(), "默认样式属性应为null");
        assertNull(dictData.getListClass(), "默认表格回显样式应为null");
        assertNull(dictData.getIsDefault(), "默认是否默认应为null");
        assertNull(dictData.getStatus(), "默认状态应为null");
        assertNull(dictData.getRemark(), "默认备注应为null");

        // 基类字段
        assertNull(dictData.getCreateTime(), "默认创建时间应为null");
        assertNull(dictData.getUpdateTime(), "默认更新时间应为null");
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

        SysDictData entity = new SysDictData();
        entity.setDictCode(dictCode);
        entity.setDictSort(dictSort);
        entity.setDictLabel(dictLabel);
        entity.setDictValue(dictValue);
        entity.setDictType(dictType);
        entity.setCssClass(cssClass);
        entity.setListClass(listClass);
        entity.setIsDefault(isDefault);
        entity.setStatus(status);
        entity.setRemark(remark);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        assertEquals(dictCode, entity.getDictCode(), "字典编码应匹配");
        assertEquals(dictSort, entity.getDictSort(), "字典排序应匹配");
        assertEquals(dictLabel, entity.getDictLabel(), "字典标签应匹配");
        assertEquals(dictValue, entity.getDictValue(), "字典键值应匹配");
        assertEquals(dictType, entity.getDictType(), "字典类型应匹配");
        assertEquals(cssClass, entity.getCssClass(), "样式属性应匹配");
        assertEquals(listClass, entity.getListClass(), "表格回显样式应匹配");
        assertEquals(isDefault, entity.getIsDefault(), "是否默认应匹配");
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
        SysDictData dictData = new SysDictData();
        dictData.setCreateTime(createTime);
        dictData.setUpdateTime(updateTime);
        dictData.setCreatedBy(createdBy);
        dictData.setUpdatedBy(updatedBy);

        assertEquals(createTime, dictData.getCreateTime(), "创建时间应匹配");
        assertEquals(updateTime, dictData.getUpdateTime(), "更新时间应匹配");
        assertEquals(createdBy, dictData.getCreatedBy(), "创建人应匹配");
        assertEquals(updatedBy, dictData.getUpdatedBy(), "更新人应匹配");
    }

    private static Stream<Arguments> provideBaseEntityData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now, now, "admin", "admin"),
                Arguments.of(now.minusDays(1), now, "system", "user"),
                Arguments.of(null, null, null, null)
        );
    }

    // ==================== 字典排序测试 ====================

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "10, true",
            "100, true",
            "0, true",
            "-1, false"
    })
    @DisplayName("字典排序 - 验证排序值有效性")
    void testDictSort(Integer dictSort, boolean isValid) {
        SysDictData dictData = new SysDictData();
        dictData.setDictSort(dictSort);

        assertEquals(dictSort, dictData.getDictSort(), "字典排序应匹配");

        if (dictSort != null && isValid) {
            assertTrue(dictSort >= 0, "字典排序应大于等于0");
        }
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
        SysDictData dictData = new SysDictData();
        dictData.setIsDefault(isDefault);

        assertEquals(isDefault, dictData.getIsDefault(), "是否默认应匹配");
        assertEquals(isDefaultFlag, "Y".equalsIgnoreCase(dictData.getIsDefault()), "Y 表示默认值");
    }

    // ==================== 状态测试 ====================

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1, false"
    })
    @DisplayName("状态 - 验证状态值含义")
    void testStatus(String status, boolean isNormal) {
        SysDictData dictData = new SysDictData();
        dictData.setStatus(status);

        assertEquals(status, dictData.getStatus(), "状态值应匹配");
        assertEquals(isNormal, "0".equals(dictData.getStatus()), "0 表示正常状态");
    }

    // ==================== 样式类测试 ====================

    @ParameterizedTest
    @CsvSource({
            "default, true",
            "primary, true",
            "success, true",
            "info, true",
            "warning, true",
            "danger, true",
            "custom, true",
            ", true"
    })
    @DisplayName("样式类 - 验证样式属性")
    void testCssClass(String cssClass, boolean isValid) {
        SysDictData dictData = new SysDictData();
        dictData.setCssClass(cssClass);

        assertEquals(cssClass, dictData.getCssClass(), "样式类应匹配");
        // cssClass 可以是任意字符串，这里只验证赋值成功
    }

    @ParameterizedTest
    @CsvSource({
            "default, true",
            "primary, true",
            "success, true",
            "info, true",
            "warning, true",
            "danger, true",
            ", true"
    })
    @DisplayName("表格回显样式 - 验证列表样式")
    void testListClass(String listClass, boolean isValid) {
        SysDictData dictData = new SysDictData();
        dictData.setListClass(listClass);

        assertEquals(listClass, dictData.getListClass(), "表格回显样式应匹配");
        // listClass 可以是任意字符串，这里只验证赋值成功
    }

    // ==================== null 值处理测试 ====================

    @ParameterizedTest
    @NullSource
    @DisplayName("null 值处理 - 验证 setter 容错")
    void testNullHandling(String value) {
        SysDictData dictData = new SysDictData();

        dictData.setDictLabel(value);
        dictData.setDictValue(value);
        dictData.setDictType(value);
        dictData.setCssClass(value);
        dictData.setListClass(value);
        dictData.setIsDefault(value);
        dictData.setStatus(value);
        dictData.setRemark(value);

        assertNull(dictData.getDictLabel(), "null 字典标签应被接受");
        assertNull(dictData.getDictValue(), "null 字典键值应被接受");
        assertNull(dictData.getDictType(), "null 字典类型应被接受");
        assertNull(dictData.getCssClass(), "null 样式类应被接受");
        assertNull(dictData.getListClass(), "null 表格样式应被接受");
        assertNull(dictData.getIsDefault(), "null 是否默认应被接受");
        assertNull(dictData.getStatus(), "null 状态应被接受");
        assertNull(dictData.getRemark(), "null 备注应被接受");
    }

    // ==================== Lombok 生成方法测试 ====================

    @Test
    @DisplayName("equals 和 hashCode - 验证对象相等性")
    void testEqualsAndHashCode() {
        SysDictData dictData1 = new SysDictData();
        dictData1.setDictCode(1L);
        dictData1.setDictType("sys_user_sex");

        SysDictData dictData2 = new SysDictData();
        dictData2.setDictCode(1L);
        dictData2.setDictType("sys_user_sex");

        SysDictData dictData3 = new SysDictData();
        dictData3.setDictCode(2L);
        dictData3.setDictType("sys_show_hide");

        assertEquals(dictData1, dictData2, "相同 ID 的字典数据应相等");
        assertEquals(dictData1.hashCode(), dictData2.hashCode(), "相等对象的 hashCode 应相同");
        assertNotEquals(dictData1, dictData3, "不同 ID 的字典数据应不相等");
    }

    @Test
    @DisplayName("toString - 验证字符串表示")
    void testToString() {
        SysDictData dictData = new SysDictData();
        dictData.setDictCode(1L);
        dictData.setDictLabel("男");

        String str = dictData.toString();

        assertNotNull(str, "toString 不应返回 null");
        assertTrue(str.contains("SysDictData") || str.contains("男") || str.contains("1"),
                "toString 应包含字典数据信息");
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 创建用户性别字典数据")
    void testCreateUserGenderDictData() {
        LocalDateTime now = LocalDateTime.now();

        SysDictData dictData = new SysDictData();
        dictData.setDictCode(1L);
        dictData.setDictSort(1);
        dictData.setDictLabel("男");
        dictData.setDictValue("0");
        dictData.setDictType("sys_user_sex");
        dictData.setCssClass("default");
        dictData.setListClass("");
        dictData.setIsDefault("Y");
        dictData.setStatus("0");
        dictData.setRemark("性别男");
        dictData.setCreateTime(now);
        dictData.setCreatedBy("system");

        assertEquals(1L, dictData.getDictCode(), "字典编码应为1");
        assertEquals(1, dictData.getDictSort(), "排序应为1");
        assertEquals("男", dictData.getDictLabel(), "字典标签应为'男'");
        assertEquals("0", dictData.getDictValue(), "字典键值应为'0'");
        assertEquals("sys_user_sex", dictData.getDictType(), "字典类型应为'sys_user_sex'");
        assertEquals("default", dictData.getCssClass(), "样式类应为'default'");
        assertEquals("Y", dictData.getIsDefault(), "应为默认值");
        assertEquals("0", dictData.getStatus(), "状态应为正常");
        assertEquals("性别男", dictData.getRemark(), "备注应匹配");
        assertEquals(now, dictData.getCreateTime(), "创建时间应匹配");
        assertEquals("system", dictData.getCreatedBy(), "创建人应为system");
    }

    @Test
    @DisplayName("业务场景 - 停用字典数据")
    void testDisableDictData() {
        SysDictData dictData = new SysDictData();
        dictData.setDictCode(2L);
        dictData.setDictLabel("女");
        dictData.setStatus("0");

        // 停用字典数据
        dictData.setStatus("1");

        assertEquals("1", dictData.getStatus(), "字典数据状态应更新为停用");
        assertFalse("0".equals(dictData.getStatus()), "字典数据应不再处于正常状态");
    }

    @Test
    @DisplayName("业务场景 - 更新字典数据信息")
    void testUpdateDictData() {
        LocalDateTime now = LocalDateTime.now();

        SysDictData dictData = new SysDictData();
        dictData.setDictCode(3L);
        dictData.setDictLabel("旧标签");
        dictData.setDictSort(1);
        dictData.setRemark("旧备注");

        // 更新字典数据
        dictData.setDictLabel("新标签");
        dictData.setDictSort(2);
        dictData.setRemark("新备注");
        dictData.setUpdateTime(now);

        assertEquals("新标签", dictData.getDictLabel(), "字典标签应更新");
        assertEquals(2, dictData.getDictSort(), "排序应更新");
        assertEquals("新备注", dictData.getRemark(), "备注应更新");
        assertEquals(now, dictData.getUpdateTime(), "更新时间应更新");
    }

    // ==================== 字典数据关联测试 ====================

    @Test
    @DisplayName("字典数据关联 - 验证与字典类型的关系")
    void testDictDataRelation() {
        SysDictData dictData = new SysDictData();
        dictData.setDictCode(1L);
        dictData.setDictType("sys_user_sex");
        dictData.setDictLabel("男");
        dictData.setDictValue("0");

        assertEquals("sys_user_sex", dictData.getDictType(), "字典数据应关联字典类型");
        assertNotNull(dictData.getDictType(), "字典类型不应为null");
        assertEquals("男", dictData.getDictLabel(), "字典标签应正确");
        assertEquals("0", dictData.getDictValue(), "字典键值应正确");
    }

    // ==================== 字典标签和键值测试 ====================

    @ParameterizedTest
    @CsvSource({
            "男, 0",
            "女, 1",
            "未知, 2",
            "正常, 0",
            "停用, 1",
            "显示, 0",
            "隐藏, 1"
    })
    @DisplayName("字典标签和键值 - 验证标签键值对")
    void testDictLabelAndValue(String dictLabel, String dictValue) {
        SysDictData dictData = new SysDictData();
        dictData.setDictLabel(dictLabel);
        dictData.setDictValue(dictValue);

        assertEquals(dictLabel, dictData.getDictLabel(), "字典标签应匹配");
        assertEquals(dictValue, dictData.getDictValue(), "字典键值应匹配");
        assertNotNull(dictData.getDictLabel(), "字典标签不应为null");
        assertNotNull(dictData.getDictValue(), "字典键值不应为null");
    }

    // ==================== 排序测试 ====================

    @Test
    @DisplayName("排序功能 - 验证多个字典数据的排序")
    void testDictSortOrder() {
        SysDictData dictData1 = new SysDictData();
        dictData1.setDictCode(1L);
        dictData1.setDictSort(1);
        dictData1.setDictLabel("第一");

        SysDictData dictData2 = new SysDictData();
        dictData2.setDictCode(2L);
        dictData2.setDictSort(2);
        dictData2.setDictLabel("第二");

        SysDictData dictData3 = new SysDictData();
        dictData3.setDictCode(3L);
        dictData3.setDictSort(3);
        dictData3.setDictLabel("第三");

        assertTrue(dictData1.getDictSort() < dictData2.getDictSort(), "第一个排序应小于第二个");
        assertTrue(dictData2.getDictSort() < dictData3.getDictSort(), "第二个排序应小于第三个");
    }

    // ==================== 常见字典样式测试 ====================

    @ParameterizedTest
    @CsvSource({
            "default, 默认样式",
            "primary, 主要样式",
            "success, 成功样式",
            "info, 信息样式",
            "warning, 警告样式",
            "danger, 危险样式"
    })
    @DisplayName("常见字典样式 - 验证 Bootstrap 样式类")
    void testCommonCssClasses(String cssClass, String description) {
        SysDictData dictData = new SysDictData();
        dictData.setCssClass(cssClass);

        assertEquals(cssClass, dictData.getCssClass(), "样式类应匹配");
    }
}
