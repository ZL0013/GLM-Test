package com.xie.glm.system.service;

import com.xie.glm.system.domain.SysTokenRegistry;

import java.util.List;

/**
 * Token 注册表服务接口
 *
 * <p>定义 Access Token 与 Refresh Token 映射关系的业务逻辑方法，支持 JWT 双 token 机制。
 *
 * <p>主要功能：
 * <ul>
 *   <li>注册新的 token 对（Access Token + Refresh Token）</li>
 *   <li>根据 Access Token 或 Refresh Token 查询</li>
 *   <li>刷新 Access Token</li>
 *   <li>撤销 token（按用户、设备、单个 token）</li>
 *   <li>验证 token 有效性</li>
 *   <li>清理过期 token</li>
 * </ul>
 *
 * @author xie
 */
public interface ITokenRegistryService {

    /**
     * 注册新的 token 对
     *
     * <p>创建 Access Token 与 Refresh Token 的映射关系。
     *
     * @param registry token 注册信息
     * @return 创建的 token 注册记录 ID
     */
    Long registerToken(SysTokenRegistry registry);

    /**
     * 根据 Access Token ID 查询 token 注册信息
     *
     * @param accessTokenId Access Token ID（UUID）
     * @return token 注册信息，如果不存在或已撤销返回 null
     */
    SysTokenRegistry getByAccessTokenId(String accessTokenId);

    /**
     * 根据 Refresh Token 哈希值查询 token 注册信息
     *
     * @param refreshTokenHash Refresh Token SHA-256 哈希值
     * @return token 注册信息，如果不存在或已撤销返回 null
     */
    SysTokenRegistry getByRefreshTokenHash(String refreshTokenHash);

    /**
     * 查询用户所有有效的 token 注册信息
     *
     * @param userId 用户 ID
     * @return token 注册信息列表
     */
    List<SysTokenRegistry> getValidTokensByUserId(Long userId);

    /**
     * 查询指定设备所有有效的 token 注册信息
     *
     * @param userId 用户 ID
     * @param deviceFingerprint 设备指纹
     * @return token 注册信息列表
     */
    List<SysTokenRegistry> getValidTokensByDevice(Long userId, String deviceFingerprint);

    /**
     * 刷新 Access Token
     *
     * <p>验证 Refresh Token 有效性后，生成新的 Access Token。
     *
     * @param refreshTokenHash Refresh Token SHA-256 哈希值
     * @param newAccessTokenId 新的 Access Token ID
     * @param newAccessExprAt 新的 Access Token 过期时间
     * @return 更新后的 token 注册信息，如果 Refresh Token 无效返回 null
     */
    SysTokenRegistry refreshToken(String refreshTokenHash, String newAccessTokenId, java.time.LocalDateTime newAccessExprAt);

    /**
     * 根据 Access Token ID 刷新 Access Token
     *
     * <p>验证 Access Token ID 有效性后，生成新的 Access Token。
     *
     * @param oldAccessTokenId 旧的 Access Token ID
     * @param newAccessTokenId 新的 Access Token ID
     * @param newAccessExprAt 新的 Access Token 过期时间
     * @return 更新后的 token 注册信息
     */
    SysTokenRegistry refreshTokenByAccessTokenId(String oldAccessTokenId, String newAccessTokenId, java.time.LocalDateTime newAccessExprAt);

    /**
     * 撤销指定 Access Token
     *
     * @param accessTokenId Access Token ID
     * @param revokeReason 撤销原因
     */
    void revokeToken(String accessTokenId, String revokeReason);

    /**
     * 撤销用户的所有 token
     *
     * <p>用于用户退出登录、修改密码等场景。
     *
     * @param userId 用户 ID
     * @param revokeReason 撤销原因
     * @return 撤销的 token 数量
     */
    int revokeAllUserTokens(Long userId, String revokeReason);

    /**
     * 撤销用户指定设备的所有 token
     *
     * <p>用于用户远程登出指定设备。
     *
     * @param userId 用户 ID
     * @param deviceFingerprint 设备指纹
     * @param revokeReason 撤销原因
     * @return 撤销的 token 数量
     */
    int revokeDeviceTokens(Long userId, String deviceFingerprint, String revokeReason);

    /**
     * 验证 Access Token 是否有效
     *
     * <p>检查 token 是否存在、未撤销、未过期。
     *
     * @param accessTokenId Access Token ID
     * @return true 表示有效，false 表示无效
     */
    boolean isAccessTokenValid(String accessTokenId);

    /**
     * 验证 Refresh Token 是否有效
     *
     * <p>检查 token 是否存在、未撤销、未过期。
     *
     * @param refreshTokenHash Refresh Token SHA-256 哈希值
     * @return true 表示有效，false 表示无效
     */
    boolean isRefreshTokenValid(String refreshTokenHash);

    /**
     * 清理已过期但未撤销的 token
     *
     * <p>用于定时清理任务，将过期的 token 标记为已撤销。
     *
     * @return 清理的 token 数量
     */
    int cleanupExpiredTokens();

    /**
     * 删除 token 注册记录
     *
     * <p>物理删除，仅用于清理已过期且已撤销的记录。
     *
     * @param id token 注册记录 ID
     */
    void deleteToken(Long id);
}
