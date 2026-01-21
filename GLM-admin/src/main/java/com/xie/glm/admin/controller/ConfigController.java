package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.ConfigFacade;
import com.xie.glm.admin.vo.ConfigVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.ConfigDTO;
import com.xie.glm.system.dto.query.ConfigQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数配置管理控制器
 *
 * <p>提供参数配置 CRUD 操作的 REST API。
 *
 * <p>接口列表：
 * <ul>
 *   <li>GET /api/system/configs - 分页查询配置列表</li>
 *   <li>GET /api/system/configs/{id} - 查询配置详情</li>
 *   <li>POST /api/system/configs - 创建配置</li>
 *   <li>PUT /api/system/configs/{id} - 更新配置</li>
 *   <li>DELETE /api/system/configs/{id} - 删除配置</li>
 *   <li>DELETE /api/system/configs - 批量删除配置</li>
 *   <li>GET /api/system/configs/check/key - 检查配置键名唯一性</li>
 *   <li>GET /api/system/configs/config - 根据配置键查询配置值</li>
 * </ul>
 *
 * <p>权限要求：
 * <ul>
 *   <li>system:config:list - 查看配置列表</li>
 *   <li>system:config:query - 查看配置详情</li>
 *   <li>system:config:add - 创建配置</li>
 *   <li>system:config:edit - 编辑配置</li>
 *   <li>system:config:remove - 删除配置</li>
 * </ul>
 *
 * <p>所有接口返回值由 ResponseAdvice 自动包装为统一格式。
 *
 * @author xie
 */
@Tag(name = "参数配置管理", description = "参数配置CRUD操作接口")
@RestController
@RequestMapping("/api/system/configs")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigFacade configFacade;

    /**
     * 分页查询配置列表
     *
     * @param query 查询条件（配置名称、配置键名、配置类型等）
     * @return 分页结果（包含配置列表和总记录数），由 ResponseAdvice 自动包装
     */
    @GetMapping
    @Operation(summary = "分页查询配置列表", description = "支持按配置名称、配置键名、配置类型等条件查询")
    @PreAuthorize("hasAuthority('system:config:list')")
    public PageResult<ConfigVO> list(ConfigQueryDTO query) {
        return configFacade.listConfigs(query);
    }

    /**
     * 查询配置详情
     *
     * @param id 配置 ID
     * @return 配置详情，由 ResponseAdvice 自动包装
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询配置详情", description = "根据配置ID查询配置详细信息")
    @PreAuthorize("hasAuthority('system:config:query')")
    public ConfigVO getDetail(@PathVariable Long id) {
        return configFacade.getConfigById(id);
    }

    /**
     * 创建配置
     *
     * @param dto 配置 DTO（包含配置名称、配置键名、配置值等）
     * @return 创建的配置 ID，由 ResponseAdvice 自动包装
     */
    @PostMapping
    @Operation(summary = "创建配置", description = "创建新配置，会检查配置键名唯一性")
    @PreAuthorize("hasAuthority('system:config:add')")
    public Long create(@Valid @RequestBody ConfigDTO dto) {
        return configFacade.createConfig(dto);
    }

    /**
     * 更新配置
     *
     * @param id 配置 ID
     * @param dto 配置 DTO（包含需要更新的字段）
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新配置", description = "更新配置信息，系统内置配置不允许修改配置键名")
    @PreAuthorize("hasAuthority('system:config:edit')")
    public void update(@PathVariable Long id, @Valid @RequestBody ConfigDTO dto) {
        dto.setConfigId(id);
        configFacade.updateConfig(dto);
    }

    /**
     * 删除配置
     *
     * @param id 配置 ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除配置", description = "删除指定配置，系统内置配置不允许删除")
    @PreAuthorize("hasAuthority('system:config:remove')")
    public void delete(@PathVariable Long id) {
        configFacade.deleteConfig(id);
    }

    /**
     * 批量删除配置
     *
     * @param ids 配置 ID 数组（逗号分隔）
     */
    @DeleteMapping
    @Operation(summary = "批量删除配置", description = "批量删除多个配置，系统内置配置不允许删除")
    @PreAuthorize("hasAuthority('system:config:remove')")
    public void deleteBatch(@RequestParam Long[] ids) {
        configFacade.deleteConfigs(ids);
    }

    /**
     * 检查配置键名唯一性
     *
     * @param configKey 配置键名
     * @return true 表示唯一，false 表示已存在，由 ResponseAdvice 自动包装
     */
    @GetMapping("/check/key")
    @Operation(summary = "检查配置键名唯一性", description = "检查配置键名是否已存在")
    @PreAuthorize("hasAuthority('system:config:query')")
    public boolean checkConfigKeyUnique(@RequestParam String configKey) {
        return configFacade.checkConfigKeyUnique(configKey);
    }

    /**
     * 根据配置键查询配置值
     *
     * @param configKey 配置键名
     * @return 配置值，如果不存在返回 null，由 ResponseAdvice 自动包装
     */
    @GetMapping("/config")
    @Operation(summary = "根据配置键查询配置值", description = "根据配置键名获取配置值")
    @PreAuthorize("hasAuthority('system:config:query')")
    public String getConfigValueByKey(@RequestParam String configKey) {
        return configFacade.getConfigValueByKey(configKey);
    }
}
