package com.xie.glm.admin.vo;

import com.xie.glm.system.dto.DictTypeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 字典类型视图对象
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
@Schema(description = "字典类型视图对象")
public class DictTypeVO extends DictTypeDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态文本
     */
    @Schema(description = "状态文本（正常/停用）", example = "正常")
    private String statusText;
}
