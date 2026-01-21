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
 * 定时任务实体
 *
 * <p>定时任务调度表，用于管理系统的定时任务配置。
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("xie_tm.sys_job")
public class SysJob extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "job_id", type = IdType.AUTO)
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
     * cron执行表达式
     */
    private String cronExpression;

    /**
     * cron计划策略（1=立即执行，2=执行一次，3=放弃执行）
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

    /**
     * 执行开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 下次执行时间
     */
    private LocalDateTime nextValidTime;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 备注
     */
    private String remark;
}
