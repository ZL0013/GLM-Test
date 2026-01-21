package com.xie.glm.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.DictDataConverter;
import com.xie.glm.system.domain.SysDictData;
import com.xie.glm.system.dto.DictDataDTO;
import com.xie.glm.system.dto.query.DictQueryDTO;
import com.xie.glm.system.mapper.SysDictDataMapper;
import com.xie.glm.system.service.impl.DictDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * DictDataServiceImpl 测试类
 *
 * <p>测试字典数据服务的业务逻辑
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DictDataServiceImpl 业务逻辑单元测试")
class DictDataServiceImplTest {

    @Mock
    private SysDictDataMapper dictDataMapper;

    @Mock
    private DictDataConverter dictDataConverter;

    @InjectMocks
    private DictDataServiceImpl dictDataService;

    private DictDataDTO testDTO;
    private SysDictData testEntity;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        testDTO = DictDataDTO.builder()
                .dictCode(1L)
                .dictSort(1)
                .dictLabel("男")
                .dictValue("0")
                .dictType("sys_user_sex")
                .cssClass("default")
                .listClass("")
                .isDefault("Y")
                .status("0")
                .remark("性别男")
                .createTime(now)
                .updateTime(now)
                .build();

        testEntity = new SysDictData();
        testEntity.setDictCode(1L);
        testEntity.setDictSort(1);
        testEntity.setDictLabel("男");
        testEntity.setDictValue("0");
        testEntity.setDictType("sys_user_sex");
        testEntity.setCssClass("default");
        testEntity.setListClass("");
        testEntity.setIsDefault("Y");
        testEntity.setStatus("0");
        testEntity.setRemark("性别男");
        testEntity.setCreateTime(now);
        testEntity.setUpdateTime(now);
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询字典数据列表 - 成功")
    void testListDictData_Success() {
        // Given
        DictQueryDTO query = new DictQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictLabel("男");
        query.setDictType("sys_user_sex");
        query.setStatus("0");

        Page<SysDictData> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(testEntity));
        page.setTotal(1);

        when(dictDataMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(dictDataConverter.toDtoList(any())).thenReturn(Arrays.asList(testDTO));

        // When
        PageResult<DictDataDTO> result = dictDataService.listDictData(query);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(1, result.getTotal());
        verify(dictDataMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
        verify(dictDataConverter, times(1)).toDtoList(any());
    }

    @Test
    @DisplayName("分页查询字典数据列表 - 空结果")
    void testListDictData_Empty() {
        // Given
        DictQueryDTO query = new DictQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);

        Page<SysDictData> page = new Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);

        when(dictDataMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(dictDataConverter.toDtoList(any())).thenReturn(List.of());

        // When
        PageResult<DictDataDTO> result = dictDataService.listDictData(query);

        // Then
        assertNotNull(result);
        assertTrue(result.getRecords().isEmpty());
        assertEquals(0, result.getTotal());
    }

    // ==================== 根据 ID 查询测试 ====================

    @Test
    @DisplayName("根据编码查询字典数据 - 成功")
    void testGetDictDataById_Success() {
        // Given
        Long dictCode = 1L;
        when(dictDataMapper.selectById(dictCode)).thenReturn(testEntity);
        when(dictDataConverter.toDto(testEntity)).thenReturn(testDTO);

        // When
        DictDataDTO result = dictDataService.getDictDataById(dictCode);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getDictCode());
        assertEquals("男", result.getDictLabel());
        assertEquals("sys_user_sex", result.getDictType());
        verify(dictDataMapper, times(1)).selectById(dictCode);
        verify(dictDataConverter, times(1)).toDto(testEntity);
    }

    @Test
    @DisplayName("根据编码查询字典数据 - 不存在")
    void testGetDictDataById_NotFound() {
        // Given
        Long dictCode = 999L;
        when(dictDataMapper.selectById(dictCode)).thenReturn(null);

        // When
        DictDataDTO result = dictDataService.getDictDataById(dictCode);

        // Then
        assertNull(result);
        verify(dictDataMapper, times(1)).selectById(dictCode);
        verify(dictDataConverter, never()).toDto(any());
    }

    // ==================== 根据字典类型查询测试 ====================

    @Test
    @DisplayName("根据字典类型查询字典数据列表 - 成功")
    void testListDictDataByType_Success() {
        // Given
        String dictType = "sys_user_sex";
        when(dictDataMapper.selectByDictType(dictType)).thenReturn(Arrays.asList(testEntity));
        when(dictDataConverter.toDtoList(any())).thenReturn(Arrays.asList(testDTO));

        // When
        List<DictDataDTO> result = dictDataService.listDictDataByType(dictType);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("男", result.get(0).getDictLabel());
        verify(dictDataMapper, times(1)).selectByDictType(dictType);
        verify(dictDataConverter, times(1)).toDtoList(any());
    }

    @Test
    @DisplayName("根据字典类型查询字典数据列表 - 空结果")
    void testListDictDataByType_Empty() {
        // Given
        String dictType = "not_exist";
        when(dictDataMapper.selectByDictType(dictType)).thenReturn(List.of());
        when(dictDataConverter.toDtoList(any())).thenReturn(List.of());

        // When
        List<DictDataDTO> result = dictDataService.listDictDataByType(dictType);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dictDataMapper, times(1)).selectByDictType(dictType);
        verify(dictDataConverter, times(1)).toDtoList(any());
    }

    // ==================== 创建字典数据测试 ====================

    @Test
    @DisplayName("创建字典数据 - 成功")
    void testCreateDictData_Success() {
        // Given
        DictDataDTO newDTO = DictDataDTO.builder()
                .dictSort(1)
                .dictLabel("新选项")
                .dictValue("3")
                .dictType("sys_user_sex")
                .status("0")
                .build();

        when(dictDataConverter.dtoToEntity(newDTO)).thenReturn(testEntity);
        when(dictDataMapper.insert(any(SysDictData.class))).thenReturn(1);

        // When
        Long result = dictDataService.createDictData(newDTO);

        // Then
        assertNotNull(result);
        verify(dictDataConverter, times(1)).dtoToEntity(newDTO);
        verify(dictDataMapper, times(1)).insert(any(SysDictData.class));
    }

    // ==================== 更新字典数据测试 ====================

    @Test
    @DisplayName("更新字典数据 - 成功")
    void testUpdateDictData_Success() {
        // Given
        DictDataDTO updateDTO = DictDataDTO.builder()
                .dictCode(1L)
                .dictLabel("更新后的标签")
                .status("0")
                .build();

        when(dictDataMapper.selectById(1L)).thenReturn(testEntity);
        when(dictDataConverter.dtoToEntity(updateDTO)).thenAnswer(invocation -> {
            SysDictData entity = new SysDictData();
            entity.setDictCode(updateDTO.getDictCode());
            entity.setDictLabel(updateDTO.getDictLabel());
            entity.setDictValue(updateDTO.getDictValue());
            entity.setDictType(updateDTO.getDictType());
            entity.setStatus(updateDTO.getStatus());
            entity.setRemark(updateDTO.getRemark());
            return entity;
        });
        when(dictDataMapper.updateById(any(SysDictData.class))).thenReturn(1);

        // When
        dictDataService.updateDictData(updateDTO);

        // Then
        verify(dictDataMapper, times(1)).selectById(1L);
        verify(dictDataMapper, times(1)).updateById(any(SysDictData.class));
    }

    @Test
    @DisplayName("更新字典数据 - 字典不存在")
    void testUpdateDictData_NotFound() {
        // Given
        DictDataDTO updateDTO = DictDataDTO.builder()
                .dictCode(999L)
                .dictLabel("更新后的标签")
                .build();

        when(dictDataMapper.selectById(999L)).thenReturn(null);

        // When
        dictDataService.updateDictData(updateDTO);

        // Then
        verify(dictDataMapper, times(1)).selectById(999L);
        verify(dictDataMapper, never()).updateById(any(SysDictData.class));
    }

    // ==================== 删除字典数据测试 ====================

    @Test
    @DisplayName("删除字典数据 - 成功")
    void testDeleteDictData_Success() {
        // Given
        Long dictCode = 1L;
        when(dictDataMapper.selectById(dictCode)).thenReturn(testEntity);
        when(dictDataMapper.deleteById(dictCode)).thenReturn(1);

        // When
        dictDataService.deleteDictData(dictCode);

        // Then
        verify(dictDataMapper, times(1)).selectById(dictCode);
        verify(dictDataMapper, times(1)).deleteById(dictCode);
    }

    @Test
    @DisplayName("删除字典数据 - 字典不存在")
    void testDeleteDictData_NotFound() {
        // Given
        Long dictCode = 999L;
        when(dictDataMapper.selectById(dictCode)).thenReturn(null);

        // When
        dictDataService.deleteDictData(dictCode);

        // Then
        verify(dictDataMapper, times(1)).selectById(dictCode);
        verify(dictDataMapper, never()).deleteById(any());
    }

    @Test
    @DisplayName("批量删除字典数据 - 成功")
    void testDeleteDictDataBatch_Success() {
        // Given
        Long[] dictCodes = {1L, 2L};
        when(dictDataMapper.selectById(1L)).thenReturn(testEntity);
        when(dictDataMapper.selectById(2L)).thenReturn(testEntity);
        when(dictDataMapper.deleteById(1L)).thenReturn(1);
        when(dictDataMapper.deleteById(2L)).thenReturn(1);

        // When
        dictDataService.deleteDictData(dictCodes);

        // Then
        verify(dictDataMapper, times(1)).selectById(1L);
        verify(dictDataMapper, times(1)).selectById(2L);
        verify(dictDataMapper, times(1)).deleteById(1L);
        verify(dictDataMapper, times(1)).deleteById(2L);
    }
}
