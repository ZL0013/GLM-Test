package com.xie.glm.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 配置数据传输对象
 *
 * <p>用于服务层返回给 Facade 层的配置数据，包含配置的基本信息和审计字段。
 *
 * <p>字段说明：
 * <ul>
 *   <li>configId：配置 ID</li>
 *   <li>configName：配置名称</li>
 *   <li>configKey：配置键名</li>
 *   <li>configValue：配置键值</li>
 *   <li>configType：配置类型（Y=系统内置，N=用户自定义）</li>
 *   <li>remark：备注</li>
 *   <li>createTime：创建时间</li>
 *   <li>updateTime：更新时间</li>
 *   <li>createdBy：创建人</li>
 *   <li>updatedBy：更新人</li>
 * </ul>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置 ID
     */
    private Long configId;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置键名
     */
    private String configKey;

    /**
     * 配置键值
     */
    private String configValue;

    /**
     * 配置类型（Y=系统内置，N=用户自定义）
     */
    private String configType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;
}
