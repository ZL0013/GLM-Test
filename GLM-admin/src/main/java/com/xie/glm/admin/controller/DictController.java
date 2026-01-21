package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.DictDataFacade;
import com.xie.glm.admin.facade.DictTypeFacade;
import com.xie.glm.admin.vo.DictDataVO;
import com.xie.glm.admin.vo.DictTypeVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.DictDataDTO;
import com.xie.glm.system.dto.DictTypeDTO;
import com.xie.glm.system.dto.query.DictQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典管理控制器
 *
 * <p>提供字典类型和字典数据 CRUD 操作的 REST API。
 *
 * <p>接口列表：
 * <ul>
 *   <li>字典类型接口：
 * *   <ul>
 *     <li>GET /api/system/dict/type - 分页查询字典类型列表</li>
 *     <li>GET /api/system/dict/type/{id} - 查询字典类型详情</li>
 *     <li>POST /api/system/dict/type - 创建字典类型</li>
 *     <li>PUT /api/system/dict/type/{id} - 更新字典类型</li>
 *     <li>DELETE /api/system/dict/type/{id} - 删除字典类型</li>
 *     <li>GET /api/system/dict/type/check/{dictType} - 检查字典类型唯一性</li>
 *   </ul>
 *   <li>字典数据接口：
 *   <ul>
 *     <li>GET /api/system/dict/data - 分页查询字典数据列表</li>
 *     <li>GET /api/system/dict/data/{id} - 查询字典数据详情</li>
 *     <li>GET /api/system/dict/data/type/{dictType} - 根据字典类型查询字典数据</li>
 *     <li>POST /api/system/dict/data - 创建字典数据</li>
 *     <li>PUT /api/system/dict/data/{id} - 更新字典数据</li>
 *     <li>DELETE /api/system/dict/data/{id} - 删除字典数据</li>
 *   </ul>
 * </ul>
 *
 * <p>权限要求：
 * <ul>
 *   <li>system:dict:list - 查看字典列表</li>
 *   <li>system:dict:query - 查看字典详情</li>
 *   <li>system:dict:add - 创建字典</li>
 *   <li>system:dict:edit - 编辑字典</li>
 *   <li>system:dict:remove - 删除字典</li>
 * </ul>
 *
 * <p>所有接口返回值由 ResponseAdvice 自动包装为 {@link Result} 格式。
 *
 * @author xie
 */
@Tag(name = "字典管理", description = "字典类型和字典数据CRUD操作接口")
@RestController
@RequestMapping("/api/system/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictTypeFacade dictTypeFacade;
    private final DictDataFacade dictDataFacade;

    // ==================== 字典类型接口 ====================

    /**
     * 分页查询字典类型列表
     *
     * @param query 查询条件（字典名称、字典类型、状态等）
     * @return 分页结果（包含字典类型列表和总记录数），由 ResponseAdvice 自动包装
     */
    @GetMapping("/type")
    @Operation(summary = "分页查询字典类型列表", description = "支持按字典名称、字典类型、状态等条件查询")
    @PreAuthorize("hasAuthority('system:dict:list')")
    public PageResult<DictTypeVO> listDictTypes(DictQueryDTO query) {
        return dictTypeFacade.listDictTypes(query);
    }

    /**
     * 查询字典类型详情
     *
     * @param id 字典 ID
     * @return 字典类型详情，由 ResponseAdvice 自动包装
     */
    @GetMapping("/type/{id}")
    @Operation(summary = "查询字典类型详情", description = "根据字典ID查询字典类型详细信息")
    @PreAuthorize("hasAuthority('system:dict:query')")
    public DictTypeVO getDictTypeDetail(@PathVariable Long id) {
        return dictTypeFacade.getDictTypeById(id);
    }

    /**
     * 创建字典类型
     *
     * @param dto 字典类型 DTO（包含字典名称、字典类型等基本信息）
     * @return 创建的字典 ID，由 ResponseAdvice 自动包装
     */
    @PostMapping("/type")
    @Operation(summary = "创建字典类型", description = "创建新的字典类型")
    @PreAuthorize("hasAuthority('system:dict:add')")
    public Long createDictType(@Valid @RequestBody DictTypeDTO dto) {
        return dictTypeFacade.createDictType(dto);
    }

    /**
     * 更新字典类型
     *
     * @param id 字典 ID
     * @param dto 字典类型 DTO（包含需要更新的字段）
     */
    @PutMapping("/type/{id}")
    @Operation(summary = "更新字典类型", description = "更新字典类型基本信息")
    @PreAuthorize("hasAuthority('system:dict:edit')")
    public void updateDictType(@PathVariable Long id, @Valid @RequestBody DictTypeDTO dto) {
        dto.setDictId(id);
        dictTypeFacade.updateDictType(dto);
    }

    /**
     * 删除字典类型
     *
     * @param id 字典 ID
     */
    @DeleteMapping("/type/{id}")
    @Operation(summary = "删除字典类型", description = "根据字典ID删除字典类型")
    @PreAuthorize("hasAuthority('system:dict:remove')")
    public void deleteDictType(@PathVariable Long id) {
        dictTypeFacade.deleteDictType(id);
    }

    /**
     * 批量删除字典类型
     *
     * @param dictIds 字典 ID 数组
     */
    @DeleteMapping("/type")
    @Operation(summary = "批量删除字典类型", description = "根据字典ID数组批量删除字典类型")
    @PreAuthorize("hasAuthority('system:dict:remove')")
    public void deleteDictTypesBatch(@RequestBody Long[] dictIds) {
        dictTypeFacade.deleteDictTypes(dictIds);
    }

    /**
     * 检查字典类型唯一性
     *
     * @param dictType 字典类型
     * @return true 表示唯一，false 表示已存在，由 ResponseAdvice 自动包装
     */
    @GetMapping("/type/check/{dictType}")
    @Operation(summary = "检查字典类型唯一性", description = "检查字典类型是否已存在")
    public boolean checkDictTypeUnique(@PathVariable String dictType) {
        return dictTypeFacade.checkDictTypeUnique(dictType);
    }

    // ==================== 字典数据接口 ====================

    /**
     * 分页查询字典数据列表
     *
     * @param query 查询条件（字典标签、字典类型、状态等）
     * @return 分页结果（包含字典数据列表和总记录数），由 ResponseAdvice 自动包装
     */
    @GetMapping("/data")
    @Operation(summary = "分页查询字典数据列表", description = "支持按字典标签、字典类型、状态等条件查询")
    @PreAuthorize("hasAuthority('system:dict:list')")
    public PageResult<DictDataVO> listDictData(DictQueryDTO query) {
        return dictDataFacade.listDictData(query);
    }

    /**
     * 查询字典数据详情
     *
     * @param id 字典编码
     * @return 字典数据详情，由 ResponseAdvice 自动包装
     */
    @GetMapping("/data/{id}")
    @Operation(summary = "查询字典数据详情", description = "根据字典编码查询字典数据详细信息")
    @PreAuthorize("hasAuthority('system:dict:query')")
    public DictDataVO getDictDataDetail(@PathVariable Long id) {
        return dictDataFacade.getDictDataById(id);
    }

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表，由 ResponseAdvice 自动包装
     */
    @GetMapping("/data/type/{dictType}")
    @Operation(summary = "根据字典类型查询字典数据", description = "根据字典类型查询该类型下的所有字典数据")
    public List<DictDataVO> listDictDataByType(@PathVariable String dictType) {
        return dictDataFacade.listDictDataByType(dictType);
    }

    /**
     * 创建字典数据
     *
     * @param dto 字典数据 DTO（包含字典标签、字典键值等基本信息）
     * @return 创建的字典编码，由 ResponseAdvice 自动包装
     */
    @PostMapping("/data")
    @Operation(summary = "创建字典数据", description = "创建新的字典数据")
    @PreAuthorize("hasAuthority('system:dict:add')")
    public Long createDictData(@Valid @RequestBody DictDataDTO dto) {
        return dictDataFacade.createDictData(dto);
    }

    /**
     * 更新字典数据
     *
     * @param id 字典编码
     * @param dto 字典数据 DTO（包含需要更新的字段）
     */
    @PutMapping("/data/{id}")
    @Operation(summary = "更新字典数据", description = "更新字典数据基本信息")
    @PreAuthorize("hasAuthority('system:dict:edit')")
    public void updateDictData(@PathVariable Long id, @Valid @RequestBody DictDataDTO dto) {
        dto.setDictCode(id);
        dictDataFacade.updateDictData(dto);
    }

    /**
     * 删除字典数据
     *
     * @param id 字典编码
     */
    @DeleteMapping("/data/{id}")
    @Operation(summary = "删除字典数据", description = "根据字典编码删除字典数据")
    @PreAuthorize("hasAuthority('system:dict:remove')")
    public void deleteDictData(@PathVariable Long id) {
        dictDataFacade.deleteDictData(id);
    }

    /**
     * 批量删除字典数据
     *
     * @param dictCodes 字典编码数组
     */
    @DeleteMapping("/data")
    @Operation(summary = "批量删除字典数据", description = "根据字典编码数组批量删除字典数据")
    @PreAuthorize("hasAuthority('system:dict:remove')")
    public void deleteDictDataBatch(@RequestBody Long[] dictCodes) {
        dictDataFacade.deleteDictData(dictCodes);
    }
}
