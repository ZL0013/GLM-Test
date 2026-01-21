package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.glm.system.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单 Mapper 接口
 *
 * <p>基于 MyBatis Plus 的 {@link BaseMapper}，提供基础 CRUD 操作。
 *
 * <p>继承的方法：
 * <ul>
 *   <li>insert：插入菜单</li>
 *   <li>deleteById：根据 ID 删除菜单</li>
 *   <li>deleteBatchIds：批量删除菜单</li>
 *   <li>updateById：根据 ID 更新菜单</li>
 *   <li>selectById：根据 ID 查询菜单</li>
 *   <li>selectBatchIds：批量查询菜单</li>
 *   <li>selectList：查询菜单列表</li>
 *   <li>selectPage：分页查询菜单</li>
 *   <li>selectCount：查询菜单数量</li>
 * </ul>
 *
 * <p>自定义查询场景：
 * <ul>
 *   <li>根据菜单名称查询</li>
 *   <li>根据父菜单 ID 查询子菜单列表</li>
 *   <li>根据菜单类型查询（M=目录，C=菜单，F=按钮）</li>
 *   <li>根据状态查询菜单列表</li>
 *   <li>查询顶级菜单（parentId=0）</li>
 * </ul>
 *
 * @author xie
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

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
