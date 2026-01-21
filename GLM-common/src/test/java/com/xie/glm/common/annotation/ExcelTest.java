package com.xie.glm.common.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Excel 注解测试
 *
 * @author xie
 */
@DisplayName("@Excel 注解测试")
class ExcelTest {

    @Test
    @DisplayName("注解应该可以正确标记在字段上")
    void testAnnotationOnField() throws NoSuchFieldException {
        Field nameField = TestEntity.class.getDeclaredField("name");
        Field ageField = TestEntity.class.getDeclaredField("age");
        Field emailField = TestEntity.class.getDeclaredField("email");

        assertTrue(nameField.isAnnotationPresent(Excel.class));
        assertTrue(ageField.isAnnotationPresent(Excel.class));
        assertTrue(emailField.isAnnotationPresent(Excel.class));
    }

    @Test
    @DisplayName("注解属性应该正确读取")
    void testAnnotationProperties() throws NoSuchFieldException {
        Field nameField = TestEntity.class.getDeclaredField("name");
        Excel nameAnnotation = nameField.getAnnotation(Excel.class);

        assertEquals("姓名", nameAnnotation.name());
        assertEquals(0, nameAnnotation.sort());
        assertEquals(20, nameAnnotation.columnWidth());
        assertFalse(nameAnnotation.required());
    }

    @Test
    @DisplayName("注解应该支持必填字段")
    void testRequiredField() throws NoSuchFieldException {
        Field emailField = TestEntity.class.getDeclaredField("email");
        Excel emailAnnotation = emailField.getAnnotation(Excel.class);

        assertTrue(emailAnnotation.required());
        assertEquals("邮箱", emailAnnotation.name());
    }

    @Test
    @DisplayName("注解应该支持排序")
    void testSortOrder() throws NoSuchFieldException {
        Field nameField = TestEntity.class.getDeclaredField("name");
        Field ageField = TestEntity.class.getDeclaredField("age");

        Excel nameAnnotation = nameField.getAnnotation(Excel.class);
        Excel ageAnnotation = ageField.getAnnotation(Excel.class);

        assertEquals(0, nameAnnotation.sort());
        assertEquals(1, ageAnnotation.sort());
    }

    @Test
    @DisplayName("注解应该支持日期格式")
    void testDateFormat() throws NoSuchFieldException {
        Field birthDateField = TestEntity.class.getDeclaredField("birthDate");
        Excel annotation = birthDateField.getAnnotation(Excel.class);

        assertEquals("yyyy-MM-dd", annotation.dateFormat());
        assertEquals("生日", annotation.name());
    }

    @Test
    @DisplayName("注解应该支持读写模式配置")
    void testReadWriteMode() throws NoSuchFieldException {
        Field idField = TestEntity.class.getDeclaredField("id");
        Excel annotation = idField.getAnnotation(Excel.class);

        assertEquals(Excel.Mode.WRITE_ONLY, annotation.mode());
    }

    // 测试用的实体类
    static class TestEntity {
        @Excel(name = "ID", sort = 0, mode = Excel.Mode.WRITE_ONLY)
        private Long id;

        @Excel(name = "姓名", sort = 0, columnWidth = 20)
        private String name;

        @Excel(name = "年龄", sort = 1)
        private Integer age;

        @Excel(name = "邮箱", sort = 2, required = true)
        private String email;

        @Excel(name = "生日", sort = 3, dateFormat = "yyyy-MM-dd")
        private String birthDate;
    }
}
