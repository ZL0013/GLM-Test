package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置查询条件 DTO
 *
 * <p>用于参数配置列表查询的数据传输对象，继承 {@link PageQuery} 获得分页和排序能力。
 *
 * <p>包含以下查询条件：
 * <ul>
 *   <li>配置名称（模糊搜索）</li>
 *   <li>配置键名（模糊搜索）</li>
 *   <li>配置类型筛选（Y=系统内置，N=用户自定义）</li>
 * </ul>
 *
 * <p>分页和排序参数继承自父类 {@link PageQuery}：
 * <ul>
 *   <li>pageNum：页码</li>
 *   <li>pageSize：每页大小</li>
 *   <li>orderByColumn：排序列</li>
 *   <li>isAsc：排序方向</li>
 * </ul>
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigQueryDTO extends PageQuery {

    /**
     * 配置名称（模糊搜索）
     */
    private String configName;

    /**
     * 配置键名（模糊搜索）
     */
    private String configKey;

    /**
     * 配置类型（Y=系统内置，N=用户自定义）
     */
    private String configType;
}
