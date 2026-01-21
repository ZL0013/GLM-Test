package com.xie.glm.common.dto;

/**
 * 登录响应
 *
 * <p>用户登录成功后的响应数据。
 * <p>Refresh Token 通过 HttpOnly Cookie 返回，不包含在响应体中。
 *
 * @author xie
 * @param accessToken 访问令牌，用于 API 认证
 * @param tokenType   令牌类型，固定为 "Bearer"
 */
public record LoginResponse(

        /**
         * 访问令牌
         */
        String accessToken,

        /**
         * 令牌类型
         */
        String tokenType
) {
    /**
     * 创建登录响应
     *
     * @param accessToken 访问令牌
     * @return 登录响应实例
     */
    public static LoginResponse of(String accessToken) {
        return new LoginResponse(accessToken, "Bearer");
    }
}
