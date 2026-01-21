package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.glm.system.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色Mapper接口
 *
 * <p>基于 MyBatis Plus 的 {@link BaseMapper}，提供基础 CRUD 操作。
 *
 * <p>继承的方法：
 * <ul>
 *   <li>insert：插入角色</li>
 *   <li>deleteById：根据 ID 删除角色</li>
 *   <li>deleteBatchIds：批量删除角色</li>
 *   <li>updateById：根据 ID 更新角色</li>
 *   <li>selectById：根据 ID 查询角色</li>
 *   <li>selectBatchIds：批量查询角色</li>
 *   <li>selectList：查询角色列表</li>
 *   <li>selectPage：分页查询角色</li>
 *   <li>selectCount：查询角色数量</li>
 * </ul>
 *
 * <p>自定义查询方法示例：
 * <ul>
 *   <li>根据角色名称查询</li>
 *   <li>根据角色权限字符串查询</li>
 *   <li>根据状态查询角色列表</li>
 * </ul>
 *
 * @author xie
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    // 继承 BaseMapper 后，MyBatis Plus 自动提供以下方法：
    // int insert(T entity);
    // int deleteById(Serializable id);
    // int deleteBatchIds(Collection<? extends Serializable> idList);
    // int updateById(T entity);
    // T selectById(Serializable id);
    // List<T> selectBatchIds(Collection<? extends Serializable> idList);
    // List<T> selectList(Wrapper<T> queryWrapper);
    // IPage<T> selectPage(IPage<T> page, Wrapper<T> queryWrapper);
    // Long selectCount(Wrapper<T> queryWrapper);
    // T selectOne(Wrapper<T> queryWrapper);
}
