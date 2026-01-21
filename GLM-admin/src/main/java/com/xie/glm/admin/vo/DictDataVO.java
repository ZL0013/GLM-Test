package com.xie.glm.admin.vo;

import com.xie.glm.system.dto.DictDataDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 字典数据视图对象
 *
 * <p>用于前端展示，包含状态文本等显示字段
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典数据视图对象")
public class DictDataVO extends DictDataDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态文本
     */
    @Schema(description = "状态文本（正常/停用）", example = "正常")
    private String statusText;

    /**
     * 是否默认文本
     */
    @Schema(description = "是否默认文本（是/否）", example = "是")
    private String isDefaultText;
}
