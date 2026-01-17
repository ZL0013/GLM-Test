package com.xie.glm.common.core;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应包装类
 *
 * <p>所有 Controller 接口必须使用此类的实例作为返回值。
 *
 * @author xie
 *
 * <ul>
 *   <li>code: 0 表示成功，非 0 表示失败</li>
 *   <li>message: 响应消息描述</li>
 *   <li>data: 响应数据（可为 null）</li>
 * </ul>
 *
 * <p>设计原则：
 * <ul>
 *   <li>简单性：使用 Lombok @Data 消除样板代码</li>
 *   <li>明确性：字段清晰，使用 Serializable 支持序列化</li>
 *   <li>不可变性：推荐使用静态工厂方法创建实例</li>
 * </ul>
 *
 * @param <T> 响应数据类型
 */
@Data
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应码：0 表示成功，非 0 表示失败
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 默认构造方法
     */
    public Result() {
    }

    /**
     * 全参数构造方法
     *
     * @param code    响应码
     * @param message 响应消息
     * @param data    响应数据
     */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return 成功响应实例，code=0，message="success"，data=null
     */
    public static <T> Result<T> success() {
        return new Result<>(0, "success", null);
    }

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 成功响应实例，code=0，message="success"
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "success", data);
    }

    /**
     * 失败响应（默认错误码）
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 失败响应实例，code=1
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(1, message, null);
    }

    /**
     * 失败响应（自定义错误码）
     *
     * @param code    错误码（非 0）
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 失败响应实例
     */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }
}
