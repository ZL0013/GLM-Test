package com.xie.glm.admin.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 刷新令牌请求
 *
 * <p>使用 Refresh Token 刷新 Access Token 的请求参数。
 *
 * @author xie
 * @param refreshToken 刷新令牌
 */
public record RefreshTokenRequest(

        /**
         * 刷新令牌
         */
        @NotBlank(message = "刷新令牌不能为空")
        String refreshToken
) {
}
