package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.glm.system.domain.SysConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 参数配置 Mapper 接口
 *
 * <p>基于 MyBatis Plus，提供配置数据访问操作。
 *
 * @author xie
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {
    // 配置管理的 CRUD 操作由 MyBatis Plus BaseMapper 提供
}
