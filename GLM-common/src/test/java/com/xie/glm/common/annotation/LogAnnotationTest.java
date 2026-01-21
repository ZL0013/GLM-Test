package com.xie.glm.common.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Log 注解测试
 *
 * <p>测试 @Log 注解的定义和属性
 *
 * @author xie
 */
@DisplayName("@Log 注解测试")
class LogAnnotationTest {

    @Test
    @DisplayName("验证 @Log 注解存在于指定包路径")
    void testLogAnnotationExists() {
        Class<?> logAnnotationClass = null;
        try {
            logAnnotationClass = Class.forName("com.xie.glm.common.annotation.Log");
        } catch (ClassNotFoundException e) {
            // 测试会失败，这是预期的 TDD 行为
        }

        assertThat(logAnnotationClass)
                .as("@Log 注解类应存在于 com.xie.glm.common.annotation 包中")
                .isNotNull();
    }

    @Test
    @DisplayName("验证 @Log 注解的保留策略为 RUNTIME")
    void testLogAnnotationRetention() throws ClassNotFoundException {
        Class<?> logAnnotationClass = Class.forName("com.xie.glm.common.annotation.Log");

        Retention retention = logAnnotationClass.getAnnotation(Retention.class);
        assertThat(retention)
                .as("@Log 注解应使用 RUNTIME 保留策略")
                .isNotNull();

        assertThat(retention.value())
                .as("@Log 注解的保留策略应为 RUNTIME")
                .isEqualTo(RetentionPolicy.RUNTIME);
    }

    @Test
    @DisplayName("验证 @Log 注解可用于方法和类")
    void testLogAnnotationTarget() throws ClassNotFoundException {
        Class<?> logAnnotationClass = Class.forName("com.xie.glm.common.annotation.Log");

        Target target = logAnnotationClass.getAnnotation(Target.class);
        assertThat(target)
                .as("@Log 注解应定义目标类型")
                .isNotNull();

        ElementType[] elementTypes = target.value();
        assertThat(elementTypes)
                .as("@Log 注解应支持 METHOD 和 TYPE 目标")
                .containsExactlyInAnyOrder(ElementType.METHOD, ElementType.TYPE);
    }

    @Test
    @DisplayName("验证 @Log 注解包含 title 属性")
    void testLogAnnotationTitleProperty() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> logAnnotationClass = Class.forName("com.xie.glm.common.annotation.Log");

        assertThat(logAnnotationClass.getDeclaredMethod("title"))
                .as("@Log 注解应包含 title 属性")
                .isNotNull();

        assertThat(logAnnotationClass.getDeclaredMethod("title").getDefaultValue())
                .as("title 属性默认值应为空字符串")
                .isEqualTo("");
    }

    @Test
    @DisplayName("验证 @Log 注解包含 businessType 属性")
    void testLogAnnotationBusinessTypeProperty() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> logAnnotationClass = Class.forName("com.xie.glm.common.annotation.Log");

        assertThat(logAnnotationClass.getDeclaredMethod("businessType"))
                .as("@Log 注解应包含 businessType 属性")
                .isNotNull();

        // businessType 应该是 BusinessType 枚举类型
        Class<?> businessTypeReturnType = logAnnotationClass.getDeclaredMethod("businessType").getReturnType();
        assertThat(businessTypeReturnType.getSimpleName())
                .as("businessType 属性应为 BusinessType 枚举类型")
                .isEqualTo("BusinessType");
    }

    @Test
    @DisplayName("验证 @Log 注解包含 operatorType 属性")
    void testLogAnnotationOperatorTypeProperty() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> logAnnotationClass = Class.forName("com.xie.glm.common.annotation.Log");

        assertThat(logAnnotationClass.getDeclaredMethod("operatorType"))
                .as("@Log 注解应包含 operatorType 属性")
                .isNotNull();
    }

    @Test
    @DisplayName("验证 @Log 注解包含 saveParam 和 saveResult 属性")
    void testLogAnnotationSaveProperties() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> logAnnotationClass = Class.forName("com.xie.glm.common.annotation.Log");

        assertThat(logAnnotationClass.getDeclaredMethod("saveParam"))
                .as("@Log 注解应包含 saveParam 属性")
                .isNotNull();

        assertThat(logAnnotationClass.getDeclaredMethod("saveParam").getDefaultValue())
                .as("saveParam 属性默认值应为 true")
                .isEqualTo(true);

        assertThat(logAnnotationClass.getDeclaredMethod("saveResult"))
                .as("@Log 注解应包含 saveResult 属性")
                .isNotNull();

        assertThat(logAnnotationClass.getDeclaredMethod("saveResult").getDefaultValue())
                .as("saveResult 属性默认值应为 true")
                .isEqualTo(true);
    }
}
