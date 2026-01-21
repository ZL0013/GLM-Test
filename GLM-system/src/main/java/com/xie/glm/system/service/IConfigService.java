package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.ConfigDTO;
import com.xie.glm.system.dto.query.ConfigQueryDTO;

/**
 * 配置服务接口
 *
 * <p>定义参数配置管理的业务逻辑方法，包括配置的增删改查等功能。
 *
 * <p>方法说明：
 * <ul>
 *   <li>分页查询配置列表</li>
 *   <li>根据 ID 查询配置详情</li>
 *   <li>根据配置键查询配置值</li>
 *   <li>创建配置</li>
 *   <li>更新配置</li>
 *   <li>删除配置</li>
 *   <li>批量删除配置</li>
 *   <li>检查配置键名唯一性</li>
 * </ul>
 *
 * @author xie
 */
public interface IConfigService {

    /**
     * 分页查询配置列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<ConfigDTO> listConfigs(ConfigQueryDTO query);

    /**
     * 根据 ID 查询配置详情
     *
     * @param configId 配置 ID
     * @return 配置 DTO
     */
    ConfigDTO getConfigById(Long configId);

    /**
     * 根据配置键查询配置值
     *
     * @param configKey 配置键名
     * @return 配置值，如果不存在返回 null
     */
    String getConfigValueByKey(String configKey);

    /**
     * 创建配置
     *
     * <p>创建配置时：
     * <ul>
     *   <li>会检查配置键名唯一性</li>
     *   <li>默认配置类型为用户自定义（N）</li>
     * </ul>
     *
     * @param dto 配置 DTO
     * @return 创建的配置 ID
     */
    Long createConfig(ConfigDTO dto);

    /**
     * 更新配置
     *
     * <p>更新配置时：
     * <ul>
     *   <li>仅更新 DTO 中非 null 的字段</li>
     *   <li>不允许修改配置键名</li>
     * </ul>
     *
     * @param dto 配置 DTO
     */
    void updateConfig(ConfigDTO dto);

    /**
     * 删除配置
     *
     * <p>删除配置时：
     * <ul>
     *   <li>系统内置配置（configType=Y）不允许删除</li>
     * </ul>
     *
     * @param configId 配置 ID
     */
    void deleteConfig(Long configId);

    /**
     * 批量删除配置
     *
     * @param configIds 配置 ID 列表
     */
    void deleteConfigs(Long[] configIds);

    /**
     * 检查配置键名是否唯一
     *
     * @param configKey 配置键名
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkConfigKeyUnique(String configKey);
}
