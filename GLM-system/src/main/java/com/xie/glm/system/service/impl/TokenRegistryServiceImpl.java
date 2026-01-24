package com.xie.glm.system.service.impl;

import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.domain.SysTokenRegistry;
import com.xie.glm.system.mapper.SysTokenRegistryMapper;
import com.xie.glm.system.service.ITokenRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Token 注册表服务实现类
 *
 * <p>实现 Access Token 与 Refresh Token 映射关系的业务逻辑。
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class TokenRegistryServiceImpl implements ITokenRegistryService {

    private final SysTokenRegistryMapper tokenRegistryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long registerToken(SysTokenRegistry registry) {
        // 检查 Access Token ID 是否已存在
        SysTokenRegistry existing = tokenRegistryMapper.selectByAccessTokenId(registry.getAccessTokenId());
        if (existing != null) {
            throw new ServiceException("Access Token ID 已存在");
        }

        // 检查 Refresh Token 哈希值是否已存在
        existing = tokenRegistryMapper.selectByRefreshTokenHash(registry.getRefreshTokenHash());
        if (existing != null) {
            throw new ServiceException("Refresh Token 已存在");
        }

        // 设置初始值
        registry.setIsRevoked(false);
        registry.setVersion(0L);

        // 插入记录
        tokenRegistryMapper.insert(registry);
        return registry.getId();
    }

    @Override
    public SysTokenRegistry getByAccessTokenId(String accessTokenId) {
        SysTokenRegistry registry = tokenRegistryMapper.selectByAccessTokenId(accessTokenId);
        if (registry == null) {
            return null;
        }
        // 检查是否已撤销
        if (Boolean.TRUE.equals(registry.getIsRevoked())) {
            return null;
        }
        // 检查是否过期
        if (registry.getAccessExprAt().isBefore(LocalDateTime.now())) {
            return null;
        }
        return registry;
    }

    @Override
    public SysTokenRegistry getByRefreshTokenHash(String refreshTokenHash) {
        SysTokenRegistry registry = tokenRegistryMapper.selectByRefreshTokenHash(refreshTokenHash);
        if (registry == null) {
            return null;
        }
        // 检查是否已撤销
        if (Boolean.TRUE.equals(registry.getIsRevoked())) {
            return null;
        }
        // 检查是否过期
        if (registry.getRefreshExprAt().isBefore(LocalDateTime.now())) {
            return null;
        }
        return registry;
    }

    @Override
    public List<SysTokenRegistry> getValidTokensByUserId(Long userId) {
        return tokenRegistryMapper.selectValidTokensByUserId(userId);
    }

    @Override
    public List<SysTokenRegistry> getValidTokensByDevice(Long userId, String deviceFingerprint) {
        List<SysTokenRegistry> tokens = tokenRegistryMapper.selectValidTokensByDeviceFingerprint(deviceFingerprint);
        // 过滤出属于指定用户的 token
        return tokens.stream()
                .filter(t -> t.getUserId().equals(userId))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysTokenRegistry refreshToken(String refreshTokenHash, String newAccessTokenId, LocalDateTime newAccessExprAt) {
        // 查询 Refresh Token
        SysTokenRegistry registry = tokenRegistryMapper.selectByRefreshTokenHash(refreshTokenHash);
        if (registry == null) {
            throw new ServiceException(BusinessStatus.REFRESH_TOKEN_INVALID);
        }

        // 检查是否已撤销
        if (Boolean.TRUE.equals(registry.getIsRevoked())) {
            throw new ServiceException(BusinessStatus.TOKEN_REVOKED);
        }

        // 检查是否过期
        if (registry.getRefreshExprAt().isBefore(LocalDateTime.now())) {
            throw new ServiceException(BusinessStatus.TOKEN_EXPIRED);
        }

        // 更新 Access Token
        registry.setAccessTokenId(newAccessTokenId);
        registry.setAccessExprAt(newAccessExprAt);

        // 乐观锁更新
        int updated = tokenRegistryMapper.updateById(registry);
        if (updated == 0) {
            throw new ServiceException("更新失败，请重试");
        }

        return registry;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysTokenRegistry refreshTokenByAccessTokenId(String oldAccessTokenId, String newAccessTokenId, LocalDateTime newAccessExprAt) {
        // 查询 Access Token
        SysTokenRegistry registry = tokenRegistryMapper.selectByAccessTokenId(oldAccessTokenId);
        if (registry == null) {
            throw new ServiceException(BusinessStatus.TOKEN_NOT_FOUND);
        }

        // 检查是否已撤销
        if (Boolean.TRUE.equals(registry.getIsRevoked())) {
            throw new ServiceException(BusinessStatus.TOKEN_REVOKED);
        }

        // 检查 Refresh Token 是否过期
        if (registry.getRefreshExprAt().isBefore(LocalDateTime.now())) {
            throw new ServiceException(BusinessStatus.TOKEN_EXPIRED);
        }

        // 更新 Access Token
        registry.setAccessTokenId(newAccessTokenId);
        registry.setAccessExprAt(newAccessExprAt);

        // 乐观锁更新
        int updated = tokenRegistryMapper.updateById(registry);
        if (updated == 0) {
            throw new ServiceException("更新失败，请重试");
        }

        return registry;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeToken(String accessTokenId, String revokeReason) {
        SysTokenRegistry registry = tokenRegistryMapper.selectByAccessTokenId(accessTokenId);
        if (registry == null) {
            throw new ServiceException(BusinessStatus.TOKEN_NOT_FOUND);
        }

        if (Boolean.TRUE.equals(registry.getIsRevoked())) {
            return; // 已经撤销，无需再次操作
        }

        registry.setIsRevoked(true);
        registry.setRevokedAt(LocalDateTime.now());
        registry.setRevokeReason(revokeReason);

        tokenRegistryMapper.updateById(registry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int revokeAllUserTokens(Long userId, String revokeReason) {
        return tokenRegistryMapper.revokeAllTokensByUserId(userId, revokeReason, LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int revokeDeviceTokens(Long userId, String deviceFingerprint, String revokeReason) {
        return tokenRegistryMapper.revokeTokensByDevice(userId, deviceFingerprint, revokeReason, LocalDateTime.now());
    }

    @Override
    public boolean isAccessTokenValid(String accessTokenId) {
        SysTokenRegistry registry = tokenRegistryMapper.selectByAccessTokenId(accessTokenId);
        if (registry == null) {
            return false;
        }
        // 检查是否已撤销
        if (Boolean.TRUE.equals(registry.getIsRevoked())) {
            return false;
        }
        // 检查是否过期
        return !registry.getAccessExprAt().isBefore(LocalDateTime.now());
    }

    @Override
    public boolean isRefreshTokenValid(String refreshTokenHash) {
        SysTokenRegistry registry = tokenRegistryMapper.selectByRefreshTokenHash(refreshTokenHash);
        if (registry == null) {
            return false;
        }
        // 检查是否已撤销
        if (Boolean.TRUE.equals(registry.getIsRevoked())) {
            return false;
        }
        // 检查是否过期
        return !registry.getRefreshExprAt().isBefore(LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanupExpiredTokens() {
        // 查询已过期但未撤销的 token
        List<SysTokenRegistry> expiredTokens = tokenRegistryMapper.selectExpiredNotRevoked(LocalDateTime.now());

        // 批量标记为已撤销
        LocalDateTime now = LocalDateTime.now();
        int count = 0;
        for (SysTokenRegistry token : expiredTokens) {
            token.setIsRevoked(true);
            token.setRevokedAt(now);
            token.setRevokeReason("过期自动清理");
            tokenRegistryMapper.updateById(token);
            count++;
        }

        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteToken(Long id) {
        SysTokenRegistry registry = tokenRegistryMapper.selectById(id);
        if (registry == null) {
            throw new ServiceException(BusinessStatus.TOKEN_NOT_FOUND);
        }

        // 只允许删除已撤销且已过期的 token
        if (!Boolean.TRUE.equals(registry.getIsRevoked())) {
            throw new ServiceException("只能删除已撤销的 token");
        }

        tokenRegistryMapper.deleteById(id);
    }
}
