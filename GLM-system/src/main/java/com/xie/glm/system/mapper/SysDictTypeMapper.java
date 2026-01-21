package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.glm.system.domain.SysDictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统字典类型 Mapper 接口
 *
 * <p>基于 MyBatis Plus，提供字典类型数据访问操作。
 *
 * @author xie
 */
@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

    /**
     * 根据字典类型查询字典类型信息
     *
     * @param dictType 字典类型
     * @return 字典类型信息，如果不存在返回 null
     */
    @Select("SELECT dict_id, dict_name, dict_type, status, remark, create_time, update_time " +
            "FROM sys_dict_type WHERE dict_type = #{dictType}")
    SysDictType selectByDictType(@Param("dictType") String dictType);

    /**
     * 查询所有字典类型列表
     *
     * @return 字典类型列表
     */
    @Select("SELECT dict_id, dict_name, dict_type, status, remark, create_time, update_time " +
            "FROM sys_dict_type ORDER BY dict_id")
    List<SysDictType> selectAllDictTypes();
}
