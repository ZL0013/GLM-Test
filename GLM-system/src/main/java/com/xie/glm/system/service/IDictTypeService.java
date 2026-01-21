package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.DictTypeDTO;
import com.xie.glm.system.dto.query.DictQueryDTO;

/**
 * 字典类型服务接口
 *
 * <p>定义字典类型管理的业务逻辑方法，包括字典类型的增删改查等功能。
 *
 * <p>方法说明：
 * <ul>
 *   <li>分页查询字典类型列表</li>
 *   <li>根据 ID 查询字典类型详情</li>
 *   <li>创建字典类型</li>
 *   <li>更新字典类型信息</li>
 *   <li>删除字典类型</li>
 *   <li>检查字典类型唯一性</li>
 * </ul>
 *
 * @author xie
 */
public interface IDictTypeService {

    /**
     * 分页查询字典类型列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<DictTypeDTO> listDictTypes(DictQueryDTO query);

    /**
     * 根据 ID 查询字典类型详情
     *
     * @param dictId 字典 ID
     * @return 字典类型 DTO
     */
    DictTypeDTO getDictTypeById(Long dictId);

    /**
     * 根据字典类型查询字典类型信息
     *
     * @param dictType 字典类型
     * @return 字典类型 DTO，如果不存在返回 null
     */
    DictTypeDTO getDictTypeByType(String dictType);

    /**
     * 创建字典类型
     *
     * @param dto 字典类型 DTO
     * @return 创建的字典 ID
     */
    Long createDictType(DictTypeDTO dto);

    /**
     * 更新字典类型信息
     *
     * @param dto 字典类型 DTO
     */
    void updateDictType(DictTypeDTO dto);

    /**
     * 删除字典类型
     *
     * @param dictId 字典 ID
     */
    void deleteDictType(Long dictId);

    /**
     * 批量删除字典类型
     *
     * @param dictIds 字典 ID 列表
     */
    void deleteDictTypes(Long[] dictIds);

    /**
     * 检查字典类型是否唯一
     *
     * @param dictType 字典类型
     * @return true 表示唯一，false 表示已存在
     */
    boolean checkDictTypeUnique(String dictType);
}
