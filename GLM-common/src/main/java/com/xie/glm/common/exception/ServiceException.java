package com.xie.glm.common.exception;

import com.xie.glm.common.enums.BusinessStatus;
import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常类
 *
 * <p>所有业务异常应继承此类，遵循以下原则：
 * <ul>
 *   <li>继承 RuntimeException（符合宪法第五条）</li>
 *   <li>支持异常链传递（符合宪法第三条明确性原则）</li>
 *   <li>使用异常链：throw new ServiceException("message", cause)</li>
 *   <li>支持 BusinessStatus 状态码枚举</li>
 * </ul>
 *
 * @author xie
 */
@Getter
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 默认构造方法
     */
    public ServiceException() {
        this(BusinessStatus.ERROR);
    }

    /**
     * 带消息的构造方法
     *
     * @param message 异常消息
     */
    public ServiceException(String message) {
        super(message);
        this.code = BusinessStatus.ERROR.getCode();
    }

    /**
     * 带消息和原因的构造方法（异常链）
     *
     * @param message 异常消息
     * @param cause   原始异常
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.code = BusinessStatus.ERROR.getCode();
    }

    /**
     * 基于业务状态码的构造方法
     *
     * @param status 业务状态码枚举
     */
    public ServiceException(BusinessStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    /**
     * 基于业务状态码和原因的构造方法（异常链）
     *
     * @param status 业务状态码枚举
     * @param cause  原始异常
     */
    public ServiceException(BusinessStatus status, Throwable cause) {
        super(status.getMessage(), cause);
        this.code = status.getCode();
    }

    /**
     * 仅带原因的构造方法
     *
     * @param cause 原始异常
     */
    public ServiceException(Throwable cause) {
        super(cause);
        this.code = BusinessStatus.ERROR.getCode();
    }

    /**
     * 带消息、原因和启用/禁用栈跟踪的构造方法
     *
     * @param message            异常消息
     * @param cause              原始异常
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 栈跟踪是否可写
     */
    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = BusinessStatus.ERROR.getCode();
    }
}
