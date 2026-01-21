package com.xie.glm.framework.event;

import org.springframework.context.ApplicationEvent;

/**
 * 操作日志事件
 *
 * <p>当操作发生时，LogAspect 会发布此事件。
 * GLM-system 模块可以通过监听此事件来保存日志到数据库。
 *
 * <p>使用示例：
 * <pre>{@code
 * @Component
 * public class OperLogListener {
 *
 *     @EventListener
 *     public void handleOperLogEvent(OperLogEvent event) {
 *         // 保存日志到数据库
 *         operLogService.saveOperLog(event.getOperLogData());
 *     }
 * }
 * }</pre>
 *
 * @author xie
 */
public class OperLogEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    /**
     * 操作日志数据（JSON 格式字符串）
     */
    private final String operLogData;

    /**
     * 操作日志对象（作为附件传输）
     */
    private final Object operLogObject;

    /**
     * 创建操作日志事件
     *
     * @param source         事件源
     * @param operLogData    操作日志数据（JSON 格式）
     * @param operLogObject  操作日志对象
     */
    public OperLogEvent(Object source, String operLogData, Object operLogObject) {
        super(source);
        this.operLogData = operLogData;
        this.operLogObject = operLogObject;
    }

    /**
     * 获取操作日志数据
     *
     * @return JSON 格式的日志数据
     */
    public String getOperLogData() {
        return operLogData;
    }

    /**
     * 获取操作日志对象
     *
     * @return 日志对象
     */
    public Object getOperLogObject() {
        return operLogObject;
    }
}
