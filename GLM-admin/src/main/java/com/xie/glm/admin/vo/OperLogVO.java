package com.xie.glm.admin.vo;

import com.xie.glm.system.dto.OperLogDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 操作日志视图对象
 *
 * <p>用于前端展示，继承 OperLogDTO 并添加显示字段。
 *
 * <p>额外字段说明：
 * <ul>
 *   <li>statusText：状态文本（成功/失败），用于前端显示</li>
 *   <li>businessTypeText：业务类型文本，用于前端显示</li>
 * </ul>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "操作日志视图对象")
public class OperLogVO extends OperLogDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态文本（成功/失败）
     */
    @Schema(description = "状态文本（成功/失败）", example = "成功")
    private String statusText;

    /**
     * 业务类型文本
     */
    @Schema(description = "业务类型文本", example = "新增")
    private String businessTypeText;
}
