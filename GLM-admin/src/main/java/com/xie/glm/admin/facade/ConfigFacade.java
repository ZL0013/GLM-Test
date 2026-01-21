package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.ConfigVoConverter;
import com.xie.glm.admin.vo.ConfigVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.ConfigDTO;
import com.xie.glm.system.dto.query.ConfigQueryDTO;
import com.xie.glm.system.service.IConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配置门面类
 *
 * <p>封装参数配置管理的业务逻辑调用，负责 DTO → VO 的转换。
 *
 * <p>职责：
 * <ul>
 *   <li>调用 Service 层获取 DTO 数据</li>
 *   <li>使用 ConfigVoConverter 将 DTO 转换为 VO</li>
 *   <li>设置 configTypeText（配置类型文本）</li>
 *   <li>简化 Controller 的逻辑</li>
 * </ul>
 *
 * <p>数据流向：
 * <pre>
 * Controller → Facade → Service → Mapper
 *     ↓         ↓         ↓
 *   VO  ←  VO  ←  DTO
 * </pre>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class ConfigFacade {

    private final IConfigService configService;
    private final ConfigVoConverter voConverter;

    /**
     * 分页查询配置列表
     *
     * @param query 查询条件
     * @return 分页结果（VO）
     */
    public PageResult<ConfigVO> listConfigs(ConfigQueryDTO query) {
        // 调用 Service 获取 DTO 分页数据
        PageResult<ConfigDTO> dtoPage = configService.listConfigs(query);

        // 转换 DTO 为 VO
        List<ConfigVO> voList = voConverter.toVoList(dtoPage.getRecords());

        // 设置 configTypeText
        voList.forEach(vo -> vo.setConfigTypeText("Y".equals(vo.getConfigType()) ? "系统内置" : "用户自定义"));

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 根据 ID 查询配置详情
     *
     * @param configId 配置 ID
     * @return 配置 VO
     */
    public ConfigVO getConfigById(Long configId) {
        // 调用 Service 获取 DTO
        ConfigDTO dto = configService.getConfigById(configId);

        // 转换 DTO 为 VO
        ConfigVO vo = voConverter.toVo(dto);

        // 设置 configTypeText
        vo.setConfigTypeText("Y".equals(vo.getConfigType()) ? "系统内置" : "用户自定义");

        return vo;
    }

    /**
     * 创建配置
     *
     * <p>创建配置时：
     * <ul>
     *   <li>Service 层会检查配置键名唯一性</li>
     *   <li>默认配置类型为用户自定义（N）</li>
     * </ul>
     *
     * @param dto 配置 DTO
     * @return 创建的配置 ID
     */
    public Long createConfig(ConfigDTO dto) {
        return configService.createConfig(dto);
    }

    /**
     * 更新配置
     *
     * <p>更新配置时：
     * <ul>
     *   <li>Service 层会检查配置键名唯一性（如果修改了键名）</li>
     *   <li>系统内置配置不允许修改配置键名</li>
     * </ul>
     *
     * @param dto 配置 DTO
     */
    public void updateConfig(ConfigDTO dto) {
        configService.updateConfig(dto);
    }

    /**
     * 删除配置
     *
     * <p>删除配置时：
     * <ul>
     *   <li>Service 层会检查是否为系统内置配置</li>
     *   <li>系统内置配置不允许删除</li>
     * </ul>
     *
     * @param configId 配置 ID
     */
    public void deleteConfig(Long configId) {
        configService.deleteConfig(configId);
    }

    /**
     * 批量删除配置
     *
     * @param configIds 配置 ID 数组
     */
    public void deleteConfigs(Long[] configIds) {
        configService.deleteConfigs(configIds);
    }

    /**
     * 检查配置键名是否唯一
     *
     * @param configKey 配置键名
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkConfigKeyUnique(String configKey) {
        return configService.checkConfigKeyUnique(configKey);
    }

    /**
     * 根据配置键查询配置值
     *
     * @param configKey 配置键名
     * @return 配置值，如果不存在返回 null
     */
    public String getConfigValueByKey(String configKey) {
        return configService.getConfigValueByKey(configKey);
    }
}
