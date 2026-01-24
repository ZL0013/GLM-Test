package com.xie.glm.admin.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 刷新令牌请求
 *
 * <p>使用过期的 Access Token 刷新令牌的请求参数。
 *
 * @author xie
 * @param accessToken 过期的访问令牌
 */
public record RefreshTokenRequest(

        /**
         * 过期的访问令牌
         */
        @NotBlank(message = "访问令牌不能为空")
        String accessToken
) {
}
