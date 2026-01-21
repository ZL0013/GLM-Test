package com.xie.glm.admin.vo;

import com.xie.glm.system.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * 用户视图对象
 * 用于前端展示，包含部门名称、角色名称等显示字段
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户视图对象")
public class UserVO extends UserDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称", example = "技术部")
    private String deptName;

    /**
     * 角色名称列表
     */
    @Schema(description = "角色名称列表", example = "[\"超级管理员\", \"用户管理员\"]")
    private List<String> roleNames;

    /**
     * 状态文本
     */
    @Schema(description = "状态文本（正常/停用）", example = "正常")
    private String statusText;
}
