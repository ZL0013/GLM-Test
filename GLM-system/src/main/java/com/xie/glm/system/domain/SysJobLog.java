package com.xie.glm.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.glm.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 定时任务执行日志实体
 *
 * <p>定时任务执行日志表，记录定时任务的执行情况。
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("xie_tm.sys_job_log")
public class SysJobLog extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "job_log_id", type = IdType.AUTO)
    private Long jobLogId;

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
     * 执行开始时间
     */
    private LocalDateTime startTime;

    /**
     * 执行结束时间
     */
    private LocalDateTime endTime;

    /**
     * 执行时长（毫秒）
     */
    private Long executionTime;

    /**
     * 任务状态（0=正常，1=失败）
     */
    private String status;

    /**
     * 日志信息
     */
    private String jobLogMessage;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 停止标志（0=正常，1=停止）
     */
    private String stopFlag;
}
