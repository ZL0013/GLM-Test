package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.glm.system.domain.SysTokenRegistry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Token 注册表 Mapper 接口
 *
 * <p>基于 MyBatis Plus，提供 Access Token 与 Refresh Token 映射关系的数据访问操作。
 *
 * @author xie
 */
@Mapper
public interface SysTokenRegistryMapper extends BaseMapper<SysTokenRegistry> {

    /**
     * 根据 Access Token ID 查询 token 注册信息
     *
     * @param accessTokenId Access Token ID（UUID）
     * @return token 注册信息，如果不存在返回 null
     */
    @Select("SELECT id, access_token_id, refresh_token_hash, \"user_id\", username, device_fingerprint, " +
            "access_expr_at, refresh_expr_at, is_revoked, revoked_at, revoke_reason, version " +
            "FROM xie_tm.sys_token_registry WHERE access_token_id = #{accessTokenId}")
    SysTokenRegistry selectByAccessTokenId(@Param("accessTokenId") String accessTokenId);

    /**
     * 根据 Refresh Token 哈希值查询 token 注册信息
     *
     * @param refreshTokenHash Refresh Token SHA-256 哈希值
     * @return token 注册信息，如果不存在返回 null
     */
    @Select("SELECT id, access_token_id, refresh_token_hash, \"user_id\", username, device_fingerprint, " +
            "access_expr_at, refresh_expr_at, is_revoked, revoked_at, revoke_reason, version " +
            "FROM xie_tm.sys_token_registry WHERE refresh_token_hash = #{refreshTokenHash}")
    SysTokenRegistry selectByRefreshTokenHash(@Param("refreshTokenHash") String refreshTokenHash);

    /**
     * 根据用户 ID 查询其所有有效的 token 注册信息
     *
     * @param userId 用户 ID
     * @return token 注册信息列表
     */
    @Select("SELECT id, access_token_id, refresh_token_hash, \"user_id\", username, device_fingerprint, " +
            "access_expr_at, refresh_expr_at, is_revoked, revoked_at, revoke_reason, version " +
            "FROM xie_tm.sys_token_registry WHERE \"user_id\" = #{userId} AND is_revoked = false")
    List<SysTokenRegistry> selectValidTokensByUserId(@Param("userId") Long userId);

    /**
     * 根据设备指纹查询所有有效的 token 注册信息
     *
     * @param deviceFingerprint 设备指纹
     * @return token 注册信息列表
     */
    @Select("SELECT id, access_token_id, refresh_token_hash, \"user_id\", username, device_fingerprint, " +
            "access_expr_at, refresh_expr_at, is_revoked, revoked_at, revoke_reason, version " +
            "FROM xie_tm.sys_token_registry WHERE device_fingerprint = #{deviceFingerprint} AND is_revoked = false")
    List<SysTokenRegistry> selectValidTokensByDeviceFingerprint(@Param("deviceFingerprint") String deviceFingerprint);

    /**
     * 查询已过期但未撤销的 token 注册信息（用于清理任务）
     *
     * @param now 当前时间
     * @return token 注册信息列表
     */
    @Select("SELECT id, access_token_id, refresh_token_hash, \"user_id\", username, device_fingerprint, " +
            "access_expr_at, refresh_expr_at, is_revoked, revoked_at, revoke_reason, version " +
            "FROM xie_tm.sys_token_registry " +
            "WHERE is_revoked = false AND (access_expr_at < #{now} OR refresh_expr_at < #{now})")
    List<SysTokenRegistry> selectExpiredNotRevoked(@Param("now") LocalDateTime now);

    /**
     * 撤销指定用户的所有 token
     *
     * @param userId 用户 ID
     * @param revokeReason 撤销原因
     * @param now 撤销时间
     * @return 影响行数
     */
    @Update("UPDATE xie_tm.sys_token_registry SET is_revoked = true, revoked_at = #{now}, " +
            "revoke_reason = #{revokeReason} " +
            "WHERE \"user_id\" = #{userId} AND is_revoked = false")
    int revokeAllTokensByUserId(@Param("userId") Long userId,
                                 @Param("revokeReason") String revokeReason,
                                 @Param("now") LocalDateTime now);

    /**
     * 撤销指定设备的所有 token
     *
     * @param userId 用户 ID
     * @param deviceFingerprint 设备指纹
     * @param revokeReason 撤销原因
     * @param now 撤销时间
     * @return 影响行数
     */
    @Update("UPDATE xie_tm.sys_token_registry SET is_revoked = true, revoked_at = #{now}, " +
            "revoke_reason = #{revokeReason} " +
            "WHERE \"user_id\" = #{userId} AND device_fingerprint = #{deviceFingerprint} AND is_revoked = false")
    int revokeTokensByDevice(@Param("userId") Long userId,
                             @Param("deviceFingerprint") String deviceFingerprint,
                             @Param("revokeReason") String revokeReason,
                             @Param("now") LocalDateTime now);
}
