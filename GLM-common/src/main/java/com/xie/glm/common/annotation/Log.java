package com.xie.glm.common.annotation;

import com.xie.glm.common.enums.BusinessType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解
 *
 * <p>用于标记需要进行日志记录的方法或类。
 * 配合 LogAspect 切面使用，自动记录操作日志到数据库。
 *
 * <p>注解属性说明：
 * <ul>
 *   <li>title：模块标题，描述操作的模块名称</li>
 *   <li>businessType：业务类型（新增、修改、删除等）</li>
 *   <li>operatorType：操作人类别（0其它 1后台用户 2手机端用户）</li>
 *   <li>saveParam：是否保存请求参数</li>
 *   <li>saveResult：是否保存响应结果</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * @Log(title = "用户管理", businessType = BusinessType.INSERT)
 * @PostMapping
 * public Result<Void> create(@RequestBody UserCreateDTO dto) {
 *     userService.create(dto);
 *     return Result.success();
 * }
 * }</pre>
 *
 * @author xie
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块标题
     *
     * @return 模块标题，默认为空字符串
     */
    String title() default "";

    /**
     * 业务类型
     *
     * @return 业务类型枚举
     */
    BusinessType businessType();

    /**
     * 操作人类别
     *
     * <p>0=其它, 1=后台用户, 2=手机端用户
     *
     * @return 操作人类别，默认为 0（其它）
     */
    int operatorType() default 0;

    /**
     * 是否保存请求参数
     *
     * <p>默认为 true，保存方法调用时的参数信息
     *
     * @return true=保存参数, false=不保存
     */
    boolean saveParam() default true;

    /**
     * 是否保存响应结果
     *
     * <p>默认为 true，保存方法执行后的返回结果
     *
     * @return true=保存结果, false=不保存
     */
    boolean saveResult() default true;
}
