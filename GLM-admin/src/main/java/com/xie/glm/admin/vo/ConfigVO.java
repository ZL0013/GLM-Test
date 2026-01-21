package com.xie.glm.admin.vo;

import com.xie.glm.system.dto.ConfigDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 配置视图对象
 *
 * <p>用于前端展示的配置数据，继承 ConfigDTO 包含所有配置信息，并提供配置类型文本显示。
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "配置视图对象")
public class ConfigVO extends ConfigDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置类型文本（系统内置/用户自定义）
     */
    @Schema(description = "配置类型文本", example = "系统内置")
    private String configTypeText;
}
