package com.xie.glm.common.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求
 *
 * <p>用户登录请求参数，包含用户名和密码。
 *
 * @param username 用户名
 * @param password 密码
 * @author xie
 */
public record LoginRequest(

        /**
         * 用户名
         */
        @NotBlank(message = "用户名不能为空")
        String username,

        /**
         * 密码
         */
        @NotBlank(message = "密码不能为空")
        String password
) {
}
