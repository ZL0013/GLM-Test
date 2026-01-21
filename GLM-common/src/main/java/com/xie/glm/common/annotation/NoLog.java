package com.xie.glm.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排除日志注解
 *
 * <p>用于标记不需要进行日志记录的方法。
 * 当方法或类被此注解标记时，LogAspect 切面将跳过日志记录。
 *
 * <p>使用场景：
 * <ul>
 *   <li>频繁调用的查询方法，避免日志过多</li>
 *   <li>包含敏感信息的接口</li>
 *   <li>健康检查、心跳接口</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * @NoLog
 * @GetMapping("/health")
 * public Result<Void> health() {
 *     return Result.success();
 * }
 * }</pre>
 *
 * @author xie
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoLog {
}
