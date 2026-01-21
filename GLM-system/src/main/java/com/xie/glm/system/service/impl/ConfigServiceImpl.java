package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.ConfigConverter;
import com.xie.glm.system.domain.SysConfig;
import com.xie.glm.system.dto.ConfigDTO;
import com.xie.glm.system.dto.query.ConfigQueryDTO;
import com.xie.glm.system.mapper.SysConfigMapper;
import com.xie.glm.system.service.IConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 配置服务实现类
 *
 * <p>实现参数配置管理的业务逻辑，包括配置的增删改查等功能。
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements IConfigService {

    private final SysConfigMapper configMapper;
    private final ConfigConverter configConverter;

    @Override
    public PageResult<ConfigDTO> listConfigs(ConfigQueryDTO query) {
        // 构建分页对象
        Page<SysConfig> page = new Page<>(
            query.getPageNum() != null ? query.getPageNum() : 1,
            query.getPageSize() != null ? query.getPageSize() : 10
        );

        // 构建查询条件
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getConfigName()), SysConfig::getConfigName, query.getConfigName())
            .like(StringUtils.hasText(query.getConfigKey()), SysConfig::getConfigKey, query.getConfigKey())
            .eq(StringUtils.hasText(query.getConfigType()), SysConfig::getConfigType, query.getConfigType());

        // 排序处理
        if (StringUtils.hasText(query.getOrderByColumn())) {
            if ("asc".equalsIgnoreCase(query.getIsAsc())) {
                wrapper.orderByAsc(true, SysConfig::getConfigId);
            } else {
                wrapper.orderByDesc(true, SysConfig::getConfigId);
            }
        } else {
            wrapper.orderByDesc(SysConfig::getCreateTime);
        }

        // 执行分页查询
        IPage<SysConfig> pageResult = configMapper.selectPage(page, wrapper);

        // 转换为 DTO
        List<ConfigDTO> records = configConverter.toDtoList(pageResult.getRecords());

        return new PageResult<>(records, pageResult.getTotal());
    }

    @Override
    public ConfigDTO getConfigById(Long configId) {
        SysConfig config = configMapper.selectById(configId);
        if (config == null) {
            throw new ServiceException("配置不存在", null);
        }
        return configConverter.toDto(config);
    }

    @Override
    public String getConfigValueByKey(String configKey) {
        if (!StringUtils.hasText(configKey)) {
            return null;
        }

        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigKey, configKey);
        SysConfig config = configMapper.selectOne(wrapper);

        return config != null ? config.getConfigValue() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createConfig(ConfigDTO dto) {
        // 检查配置键名唯一性
        if (!checkConfigKeyUnique(dto.getConfigKey())) {
            throw new ServiceException(BusinessStatus.CONFIG_KEY_DUPLICATE);
        }

        SysConfig entity = configConverter.toEntity(dto);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        // 默认配置类型为用户自定义
        if (!StringUtils.hasText(entity.getConfigType())) {
            entity.setConfigType("N");
        }

        configMapper.insert(entity);
        return entity.getConfigId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(ConfigDTO dto) {
        if (dto.getConfigId() == null) {
            throw new ServiceException(BusinessStatus.CONFIG_ID_NULL);
        }

        SysConfig existingConfig = configMapper.selectById(dto.getConfigId());
        if (existingConfig == null) {
            throw new ServiceException("配置不存在", null);
        }

        // 系统内置配置不允许修改配置键名
        if ("Y".equals(existingConfig.getConfigType())
            && StringUtils.hasText(dto.getConfigKey())
            && !dto.getConfigKey().equals(existingConfig.getConfigKey())) {
            throw new ServiceException(BusinessStatus.CONFIG_KEY_READONLY);
        }

        // 检查配置键名唯一性（如果修改了键名）
        if (StringUtils.hasText(dto.getConfigKey())
            && !dto.getConfigKey().equals(existingConfig.getConfigKey())
            && !checkConfigKeyUnique(dto.getConfigKey())) {
            throw new ServiceException(BusinessStatus.CONFIG_KEY_DUPLICATE);
        }

        configConverter.updateEntityFromDto(dto, existingConfig);
        existingConfig.setUpdateTime(LocalDateTime.now());
        configMapper.updateById(existingConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfig(Long configId) {
        SysConfig config = configMapper.selectById(configId);
        if (config == null) {
            throw new ServiceException("配置不存在", null);
        }

        // 系统内置配置不允许删除
        if ("Y".equals(config.getConfigType())) {
            throw new ServiceException(BusinessStatus.CONFIG_READONLY);
        }

        configMapper.deleteById(configId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfigs(Long[] configIds) {
        if (configIds == null || configIds.length == 0) {
            return;
        }

        // 检查是否包含系统内置配置
        List<SysConfig> configs = configMapper.selectBatchIds(Arrays.asList(configIds));
        for (SysConfig config : configs) {
            if ("Y".equals(config.getConfigType())) {
                throw new ServiceException("系统内置配置不允许删除: " + config.getConfigName(), null);
            }
        }

        configMapper.deleteBatchIds(Arrays.asList(configIds));
    }

    @Override
    public boolean checkConfigKeyUnique(String configKey) {
        if (!StringUtils.hasText(configKey)) {
            return false;
        }

        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigKey, configKey);
        Long count = configMapper.selectCount(wrapper);

        return count == 0;
    }
}
