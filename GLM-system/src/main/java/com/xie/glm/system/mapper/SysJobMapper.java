package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.glm.system.domain.SysJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务 Mapper 接口
 *
 * <p>基于 MyBatis Plus 的 {@link BaseMapper}，提供基础 CRUD 操作。
 *
 * <p>继承的方法：
 * <ul>
 *   <li>insert：插入任务</li>
 *   <li>deleteById：根据 ID 删除任务</li>
 *   <li>deleteBatchIds：批量删除任务</li>
 *   <li>updateById：根据 ID 更新任务</li>
 *   <li>selectById：根据 ID 查询任务</li>
 *   <li>selectBatchIds：批量查询任务</li>
 *   <li>selectList：查询任务列表</li>
 *   <li>selectPage：分页查询任务</li>
 *   <li>selectCount：查询任务数量</li>
 * </ul>
 *
 * <p>自定义查询方法示例：
 * <ul>
 *   <li>根据任务名称查询</li>
 *   <li>根据任务组查询</li>
 *   <li>根据状态查询任务列表</li>
 *   <li>根据调用目标查询</li>
 * </ul>
 *
 * @author xie
 */
@Mapper
public interface SysJobMapper extends BaseMapper<SysJob> {

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
