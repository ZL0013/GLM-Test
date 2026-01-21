package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.DictDataDTO;
import com.xie.glm.system.dto.query.DictQueryDTO;

import java.util.List;

/**
 * 字典数据服务接口
 *
 * <p>定义字典数据管理的业务逻辑方法，包括字典数据的增删改查等功能。
 *
 * <p>方法说明：
 * <ul>
 *   <li>分页查询字典数据列表</li>
 *   <li>根据 ID 查询字典数据详情</li>
 *   <li>根据字典类型查询字典数据列表</li>
 *   <li>创建字典数据</li>
 *   <li>更新字典数据信息</li>
 *   <li>删除字典数据</li>
 * </ul>
 *
 * @author xie
 */
public interface IDictDataService {

    /**
     * 分页查询字典数据列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<DictDataDTO> listDictData(DictQueryDTO query);

    /**
     * 根据 ID 查询字典数据详情
     *
     * @param dictCode 字典编码
     * @return 字典数据 DTO
     */
    DictDataDTO getDictDataById(Long dictCode);

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<DictDataDTO> listDictDataByType(String dictType);

    /**
     * 创建字典数据
     *
     * @param dto 字典数据 DTO
     * @return 创建的字典编码
     */
    Long createDictData(DictDataDTO dto);

    /**
     * 更新字典数据信息
     *
     * @param dto 字典数据 DTO
     */
    void updateDictData(DictDataDTO dto);

    /**
     * 删除字典数据
     *
     * @param dictCode 字典编码
     */
    void deleteDictData(Long dictCode);

    /**
     * 批量删除字典数据
     *
     * @param dictCodes 字典编码列表
     */
    void deleteDictData(Long[] dictCodes);
}
