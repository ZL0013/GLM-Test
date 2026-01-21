package com.xie.glm.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务数据传输对象
 *
 * <p>用于服务层返回给前端展示的定时任务数据，包含任务基本信息和状态。
 *
 * <p>字段说明：
 * <ul>
 *   <li>jobId：任务 ID</li>
 *   <li>jobName：任务名称</li>
 *   <li>jobGroup：任务组名</li>
 *   <li>invokeTarget：调用目标字符串</li>
 *   <li>invokeParam：调用目标参数字符串</li>
 *   <li>cronExpression：Cron 执行表达式</li>
 *   <li>misfirePolicy：Cron 计划策略（1=立即执行，2=执行一次，3=放弃执行）</li>
 *   <li>concurrent：是否并发执行（0=禁止，1=允许）</li>
 *   <li>status：任务状态（0=正常，1=暂停）</li>
 *   <li>beginTime：执行开始时间</li>
 *   <li>nextValidTime：下次执行时间</li>
 *   <li>exceptionInfo：异常信息</li>
 *   <li>remark：备注</li>
 *   <li>createTime：创建时间</li>
 *   <li>updateTime：更新时间</li>
 * </ul>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 任务基本信息 ====================

    /**
     * 任务 ID
     */
    private Long jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * 调用目标参数字符串
     */
    private String invokeParam;

    /**
     * Cron 执行表达式
     */
    private String cronExpression;

    /**
     * Cron 计划策略（1=立即执行，2=执行一次，3=放弃执行）
     */
    private String misfirePolicy;

    /**
     * 是否并发执行（0=禁止，1=允许）
     */
    private String concurrent;

    /**
     * 任务状态（0=正常，1=暂停）
     */
    private String status;

    // ==================== 时间字段 ====================

    /**
     * 执行开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 下次执行时间
     */
    private LocalDateTime nextValidTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // ==================== 其他信息 ====================

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 备注
     */
    private String remark;
}
