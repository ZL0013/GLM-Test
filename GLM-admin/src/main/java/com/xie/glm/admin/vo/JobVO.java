package com.xie.glm.admin.vo;

import com.xie.glm.system.dto.JobDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 定时任务视图对象
 *
 * <p>用于前端展示，包含状态文本、下次执行时间等展示字段
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "定时任务视图对象")
public class JobVO extends JobDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态文本
     */
    @Schema(description = "状态文本（正常/暂停）", example = "正常")
    private String statusText;

    /**
     * 执行策略文本
     */
    @Schema(description = "执行策略文本", example = "立即执行")
    private String misfirePolicyText;

    /**
     * 并发标识文本
     */
    @Schema(description = "并发标识文本", example = "禁止")
    private String concurrentText;
}
