package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.glm.system.domain.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统字典数据 Mapper 接口
 *
 * <p>基于 MyBatis Plus，提供字典数据访问操作。
 *
 * @author xie
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    @Select("SELECT dict_code, dict_sort, dict_label, dict_value, dict_type, " +
            "css_class, list_class, is_default, status, remark, create_time, update_time " +
            "FROM sys_dict_data WHERE dict_type = #{dictType} AND status = '0' " +
            "ORDER BY dict_sort ASC")
    List<SysDictData> selectByDictType(@Param("dictType") String dictType);

    /**
     * 根据字典类型和字典键值查询字典数据
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典数据信息，如果不存在返回 null
     */
    @Select("SELECT dict_code, dict_sort, dict_label, dict_value, dict_type, " +
            "css_class, list_class, is_default, status, remark, create_time, update_time " +
            "FROM sys_dict_data WHERE dict_type = #{dictType} AND dict_value = #{dictValue} " +
            "AND status = '0' LIMIT 1")
    SysDictData selectByDictTypeAndValue(@Param("dictType") String dictType,
                                          @Param("dictValue") String dictValue);
}
