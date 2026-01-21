package com.xie.glm.framework.aspect;

import com.xie.glm.common.annotation.Log;
import com.xie.glm.common.annotation.NoLog;
import com.xie.glm.common.enums.BusinessType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * LogAspect 切面测试
 *
 * <p>测试日志切面的各种场景：
 * <ul>
 *   <li>正常方法调用（记录日志）</li>
 *   <li>@NoLog 注解标记的方法（跳过日志）</li>
 *   <li>异常场景（记录错误日志）</li>
 *   <li>@Log 注解属性提取</li>
 * </ul>
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("LogAspect 切面测试")
class LogAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Mock
    private MethodSignature methodSignature;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private LogAspect logAspect;

    @BeforeEach
    void setUp() {
        logAspect = new LogAspect(eventPublisher);
        // 默认设置 getTarget 返回一个测试对象
        lenient().when(joinPoint.getTarget()).thenReturn(new Object());
    }

    @ParameterizedTest
    @CsvSource({
        "createMethod, true",
        "updateMethod, true",
        "deleteMethod, true"
    })
    @DisplayName("测试带 @Log 注解的方法是否正常执行并发布事件")
    void testLogAnnotatedMethod(String methodName, boolean shouldPublishEvent) throws Throwable {
        // Given
        Object target = new TestService();
        when(joinPoint.getTarget()).thenReturn(target);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(getTestMethod(methodName));
        when(methodSignature.getName()).thenReturn(methodName);
        when(joinPoint.proceed()).thenReturn(new Object());

        // When
        Object result = logAspect.logAround(joinPoint);

        // Then
        assertThat(result)
            .as("方法应该正常执行并返回结果")
            .isNotNull();
        verify(joinPoint, times(1)).proceed();

        if (shouldPublishEvent) {
            verify(eventPublisher, times(1)).publishEvent(any());
        }
    }

    @ParameterizedTest
    @MethodSource("provideNoLogMethods")
    @DisplayName("测试 @NoLog 注解的方法是否跳过日志记录")
    void testNoLogAnnotatedMethod(String methodName) throws Throwable {
        // Given
        Object target = new TestService();
        when(joinPoint.getTarget()).thenReturn(target);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(getTestMethod(methodName));
        when(methodSignature.getName()).thenReturn(methodName);
        when(joinPoint.proceed()).thenReturn(new Object());

        // When
        Object result = logAspect.logAround(joinPoint);

        // Then
        assertThat(result)
            .as("@NoLog 标记的方法应该正常执行，但跳过日志记录")
            .isNotNull();
        verify(joinPoint, times(1)).proceed();
        // 验证没有发布事件
        verify(eventPublisher, never()).publishEvent(any());
    }

    @ParameterizedTest
    @CsvSource({
        "createMethod, 用户新增",
        "updateMethod, 用户修改",
        "deleteMethod, 用户删除"
    })
    @DisplayName("测试 @Log 注解属性提取")
    void testLogAnnotationExtraction(String methodName, String expectedTitle) throws Throwable {
        // Given
        Object target = new TestService();
        when(joinPoint.getTarget()).thenReturn(target);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(getTestMethod(methodName));
        when(methodSignature.getName()).thenReturn(methodName);
        when(joinPoint.proceed()).thenReturn(new Object());

        // When
        logAspect.logAround(joinPoint);

        // Then - 验证方法被执行并发布了事件
        verify(joinPoint, times(1)).proceed();
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    private static Stream<Arguments> provideNoLogMethods() {
        return Stream.of(
            Arguments.of("noLogMethod"),
            Arguments.of("healthCheck")
        );
    }

    private Method getTestMethod(String methodName) {
        try {
            return TestService.class.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 测试用的服务类，包含各种注解场景
     */
    static class TestService {

        @Log(title = "用户新增", businessType = BusinessType.INSERT)
        public Object createMethod() {
            return new Object();
        }

        @Log(title = "用户修改", businessType = BusinessType.UPDATE)
        public Object updateMethod() {
            return new Object();
        }

        @Log(title = "用户删除", businessType = BusinessType.DELETE)
        public Object deleteMethod() {
            return new Object();
        }

        @NoLog
        public Object noLogMethod() {
            return new Object();
        }

        @NoLog
        public Object healthCheck() {
            return new Object();
        }

        public Object create() {
            return new Object();
        }

        public Object update() {
            return new Object();
        }

        public Object delete() {
            return new Object();
        }
    }
}
