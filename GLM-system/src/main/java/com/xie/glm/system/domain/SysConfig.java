package com.xie.glm.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.glm.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 参数配置实体
 *
 * <p>系统参数配置表，用于存储系统运行时的各类配置参数。
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("xie_tm.sys_config")
public class SysConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(value = "config_id", type = IdType.AUTO)
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
}
