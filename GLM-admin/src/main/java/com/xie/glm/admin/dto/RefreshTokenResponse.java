package com.xie.glm.admin.dto;

/**
 * 刷新令牌响应
 *
 * <p>刷新 Access Token 成功后的响应数据。
 *
 * @author xie
 * @param accessToken 新的访问令牌
 * @param tokenType   令牌类型，固定为 "Bearer"
 */
public record RefreshTokenResponse(

        /**
         * 新的访问令牌
         */
        String accessToken,

        /**
         * 令牌类型
         */
        String tokenType
) {
    /**
     * 创建刷新令牌响应
     *
     * @param accessToken 新的访问令牌
     * @return 刷新令牌响应实例
     */
    public static RefreshTokenResponse of(String accessToken) {
        return new RefreshTokenResponse(accessToken, "Bearer");
    }
}
