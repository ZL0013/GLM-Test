package com.xie.glm.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Token 注册表实体
 *
 * <p>对应数据库表：xie_tm.sys_token_registry
 * <p>用于维护 Access Token 与 Refresh Token 的映射关系
 *
 * @author xie
 */
@Data
@EqualsAndHashCode
@TableName("xie_tm.sys_token_registry")
public class SysTokenRegistry {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Access Token ID（UUID，嵌入 JWT payload）
     */
    private String accessTokenId;

    /**
     * Refresh Token 哈希值（SHA-256）
     */
    private String refreshTokenHash;

    /**
     * 用户 ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 设备指纹
     */
    private String deviceFingerprint;

    /**
     * Access Token 过期时间
     */
    private LocalDateTime accessExprAt;

    /**
     * Refresh Token 过期时间
     */
    private LocalDateTime refreshExprAt;

    /**
     * 是否已撤销
     */
    private Boolean isRevoked;

    /**
     * 撤销时间
     */
    private LocalDateTime revokedAt;

    /**
     * 撤销原因
     */
    private String revokeReason;

    /**
     * 乐观锁版本号
     */
    private Long version;
}
