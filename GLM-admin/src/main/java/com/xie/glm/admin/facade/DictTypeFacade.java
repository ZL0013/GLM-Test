package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.DictTypeVoConverter;
import com.xie.glm.admin.vo.DictTypeVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.DictTypeDTO;
import com.xie.glm.system.dto.query.DictQueryDTO;
import com.xie.glm.system.service.IDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典类型门面类
 *
 * <p>封装字典类型管理的业务逻辑调用，负责 DTO → VO 的转换。
 *
 * <p>职责：
 * <ul>
 *   <li>调用 Service 层获取 DTO 数据</li>
 *   <li>使用 DictTypeVoConverter 将 DTO 转换为 VO</li>
 *   <li>组装状态文本等展示数据</li>
 *   <li>简化 Controller 的逻辑</li>
 * </ul>
 *
 * <p>数据流向：
 * <pre>
 * Controller → Facade → Service → Mapper
 *     ↓         ↓         ↓
 *   VO  ←  VO  ←  DTO
 * </pre>
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class DictTypeFacade {

    private final IDictTypeService dictTypeService;
    private final DictTypeVoConverter voConverter;

    /**
     * 分页查询字典类型列表
     *
     * @param query 查询条件
     * @return 分页结果（VO）
     */
    public PageResult<DictTypeVO> listDictTypes(DictQueryDTO query) {
        // 调用 Service 获取 DTO 分页数据
        PageResult<DictTypeDTO> dtoPage = dictTypeService.listDictTypes(query);

        // 转换 DTO 为 VO 并设置状态文本
        List<DictTypeVO> voList = dtoPage.getRecords().stream()
                .map(dto -> {
                    DictTypeVO vo = voConverter.toVo(dto);
                    vo.setStatusText("0".equals(dto.getStatus()) ? "正常" : "停用");
                    return vo;
                })
                .toList();

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 根据 ID 查询字典类型详情
     *
     * @param dictId 字典 ID
     * @return 字典类型 VO
     */
    public DictTypeVO getDictTypeById(Long dictId) {
        // 调用 Service 获取 DTO
        DictTypeDTO dto = dictTypeService.getDictTypeById(dictId);

        // 转换 DTO 为 VO
        DictTypeVO vo = voConverter.toVo(dto);
        vo.setStatusText("0".equals(dto.getStatus()) ? "正常" : "停用");

        return vo;
    }

    /**
     * 根据字典类型查询字典类型信息
     *
     * @param dictType 字典类型
     * @return 字典类型 VO
     */
    public DictTypeVO getDictTypeByType(String dictType) {
        // 调用 Service 获取 DTO
        DictTypeDTO dto = dictTypeService.getDictTypeByType(dictType);
        if (dto == null) {
            return null;
        }

        // 转换 DTO 为 VO
        DictTypeVO vo = voConverter.toVo(dto);
        vo.setStatusText("0".equals(dto.getStatus()) ? "正常" : "停用");

        return vo;
    }

    /**
     * 创建字典类型
     *
     * @param dto 字典类型 DTO
     * @return 创建的字典 ID
     */
    public Long createDictType(DictTypeDTO dto) {
        return dictTypeService.createDictType(dto);
    }

    /**
     * 更新字典类型信息
     *
     * @param dto 字典类型 DTO
     */
    public void updateDictType(DictTypeDTO dto) {
        dictTypeService.updateDictType(dto);
    }

    /**
     * 删除字典类型
     *
     * @param dictId 字典 ID
     */
    public void deleteDictType(Long dictId) {
        dictTypeService.deleteDictType(dictId);
    }

    /**
     * 批量删除字典类型
     *
     * @param dictIds 字典 ID 数组
     */
    public void deleteDictTypes(Long[] dictIds) {
        dictTypeService.deleteDictTypes(dictIds);
    }

    /**
     * 检查字典类型是否唯一
     *
     * @param dictType 字典类型
     * @return true 表示唯一，false 表示已存在
     */
    public boolean checkDictTypeUnique(String dictType) {
        return dictTypeService.checkDictTypeUnique(dictType);
    }
}
