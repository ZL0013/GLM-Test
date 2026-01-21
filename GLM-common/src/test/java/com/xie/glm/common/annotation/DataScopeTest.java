package com.xie.glm.common.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @DataScope 注解测试
 *
 * @author xie
 */
@DisplayName("@DataScope 注解测试")
class DataScopeTest {

    @Test
    @DisplayName("注解应存在于测试方法上")
    void testAnnotationExists() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("testMethod");
        DataScope annotation = method.getAnnotation(DataScope.class);

        assertNotNull(annotation, "@DataScope 注解应该存在");
    }

    @Test
    @DisplayName("注解默认值应为空字符串和dept_id")
    void testAnnotationDefaultValues() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("testMethod");
        DataScope annotation = method.getAnnotation(DataScope.class);

        assertEquals("", annotation.alias(), "alias 默认值应为空字符串");
        assertEquals("dept_id", annotation.column(), "column 默认值应为dept_id");
    }

    @Test
    @DisplayName("注解应支持自定义alias和column值")
    void testAnnotationCustomValues() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("customMethod");
        DataScope annotation = method.getAnnotation(DataScope.class);

        assertEquals("u", annotation.alias(), "alias 应为自定义值 u");
        assertEquals("create_dept_id", annotation.column(), "column 应为自定义值 create_dept_id");
    }

    /**
     * 测试用类
     */
    static class TestClass {

        @DataScope
        public void testMethod() {
        }

        @DataScope(alias = "u", column = "create_dept_id")
        public void customMethod() {
        }
    }
}
