package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.DictDataVoConverter;
import com.xie.glm.admin.vo.DictDataVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.DictDataDTO;
import com.xie.glm.system.dto.query.DictQueryDTO;
import com.xie.glm.system.service.IDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典数据门面类
 *
 * <p>封装字典数据管理的业务逻辑调用，负责 DTO → VO 的转换。
 *
 * <p>职责：
 * <ul>
 *   <li>调用 Service 层获取 DTO 数据</li>
 *   <li>使用 DictDataVoConverter 将 DTO 转换为 VO</li>
 *   <li>组装状态文本、是否默认文本等展示数据</li>
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
public class DictDataFacade {

    private final IDictDataService dictDataService;
    private final DictDataVoConverter voConverter;

    /**
     * 分页查询字典数据列表
     *
     * @param query 查询条件
     * @return 分页结果（VO）
     */
    public PageResult<DictDataVO> listDictData(DictQueryDTO query) {
        // 调用 Service 获取 DTO 分页数据
        PageResult<DictDataDTO> dtoPage = dictDataService.listDictData(query);

        // 转换 DTO 为 VO 并设置状态文本、是否默认文本
        List<DictDataVO> voList = dtoPage.getRecords().stream()
                .map(dto -> {
                    DictDataVO vo = voConverter.toVo(dto);
                    vo.setStatusText("0".equals(dto.getStatus()) ? "正常" : "停用");
                    vo.setIsDefaultText("Y".equalsIgnoreCase(dto.getIsDefault()) ? "是" : "否");
                    return vo;
                })
                .toList();

        return new PageResult<>(voList, dtoPage.getTotal());
    }

    /**
     * 根据 ID 查询字典数据详情
     *
     * @param dictCode 字典编码
     * @return 字典数据 VO
     */
    public DictDataVO getDictDataById(Long dictCode) {
        // 调用 Service 获取 DTO
        DictDataDTO dto = dictDataService.getDictDataById(dictCode);

        // 转换 DTO 为 VO
        DictDataVO vo = voConverter.toVo(dto);
        vo.setStatusText("0".equals(dto.getStatus()) ? "正常" : "停用");
        vo.setIsDefaultText("Y".equalsIgnoreCase(dto.getIsDefault()) ? "是" : "否");

        return vo;
    }

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据 VO 列表
     */
    public List<DictDataVO> listDictDataByType(String dictType) {
        // 调用 Service 获取 DTO 列表
        List<DictDataDTO> dtoList = dictDataService.listDictDataByType(dictType);

        // 转换 DTO 列表为 VO 列表
        return dtoList.stream()
                .map(dto -> {
                    DictDataVO vo = voConverter.toVo(dto);
                    vo.setStatusText("0".equals(dto.getStatus()) ? "正常" : "停用");
                    vo.setIsDefaultText("Y".equalsIgnoreCase(dto.getIsDefault()) ? "是" : "否");
                    return vo;
                })
                .toList();
    }

    /**
     * 创建字典数据
     *
     * @param dto 字典数据 DTO
     * @return 创建的字典编码
     */
    public Long createDictData(DictDataDTO dto) {
        return dictDataService.createDictData(dto);
    }

    /**
     * 更新字典数据信息
     *
     * @param dto 字典数据 DTO
     */
    public void updateDictData(DictDataDTO dto) {
        dictDataService.updateDictData(dto);
    }

    /**
     * 删除字典数据
     *
     * @param dictCode 字典编码
     */
    public void deleteDictData(Long dictCode) {
        dictDataService.deleteDictData(dictCode);
    }

    /**
     * 批量删除字典数据
     *
     * @param dictCodes 字典编码数组
     */
    public void deleteDictData(Long[] dictCodes) {
        dictDataService.deleteDictData(dictCodes);
    }
}
