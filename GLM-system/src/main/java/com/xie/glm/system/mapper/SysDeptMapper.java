package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.glm.system.domain.SysDept;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门 Mapper 接口
 *
 * <p>基于 MyBatis Plus 的 {@link BaseMapper}，提供部门实体的 CRUD 操作。
 *
 * <p>继承的方法包括：
 * <ul>
 *   <li>insert：插入部门</li>
 *   <li>deleteById：根据 ID 删除部门</li>
 *   <li>updateById：根据 ID 更新部门</li>
 *   <li>selectById：根据 ID 查询部门</li>
 *   <li>selectList：条件查询部门列表</li>
 *   <li>selectPage：分页查询部门列表</li>
 *   <li>selectCount：统计部门数量</li>
 * </ul>
 *
 * @author xie
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
    // MyBatis Plus 自动生成 CRUD 方法，无需手动编写
}
