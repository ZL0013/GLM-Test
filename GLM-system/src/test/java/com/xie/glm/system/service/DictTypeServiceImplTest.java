package com.xie.glm.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.DictTypeConverter;
import com.xie.glm.system.domain.SysDictType;
import com.xie.glm.system.dto.DictTypeDTO;
import com.xie.glm.system.dto.query.DictQueryDTO;
import com.xie.glm.system.mapper.SysDictTypeMapper;
import com.xie.glm.system.service.impl.DictTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * DictTypeServiceImpl 测试类
 *
 * <p>测试字典类型服务的业务逻辑
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DictTypeServiceImpl 业务逻辑单元测试")
class DictTypeServiceImplTest {

    @Mock
    private SysDictTypeMapper dictTypeMapper;

    @Mock
    private DictTypeConverter dictTypeConverter;

    @InjectMocks
    private DictTypeServiceImpl dictTypeService;

    private DictTypeDTO testDTO;
    private SysDictType testEntity;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        testDTO = DictTypeDTO.builder()
                .dictId(1L)
                .dictName("用户性别")
                .dictType("sys_user_sex")
                .status("0")
                .remark("系统预设")
                .createTime(now)
                .updateTime(now)
                .build();

        testEntity = new SysDictType();
        testEntity.setDictId(1L);
        testEntity.setDictName("用户性别");
        testEntity.setDictType("sys_user_sex");
        testEntity.setStatus("0");
        testEntity.setRemark("系统预设");
        testEntity.setCreateTime(now);
        testEntity.setUpdateTime(now);
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询字典类型列表 - 成功")
    void testListDictTypes_Success() {
        // Given
        DictQueryDTO query = new DictQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictName("用户");
        query.setStatus("0");

        Page<SysDictType> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(testEntity));
        page.setTotal(1);

        when(dictTypeMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(dictTypeConverter.toDtoList(any())).thenReturn(Arrays.asList(testDTO));

        // When
        PageResult<DictTypeDTO> result = dictTypeService.listDictTypes(query);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(1, result.getTotal());
        verify(dictTypeMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
        verify(dictTypeConverter, times(1)).toDtoList(any());
    }

    @Test
    @DisplayName("分页查询字典类型列表 - 空结果")
    void testListDictTypes_Empty() {
        // Given
        DictQueryDTO query = new DictQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);

        Page<SysDictType> page = new Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);

        when(dictTypeMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(dictTypeConverter.toDtoList(any())).thenReturn(List.of());

        // When
        PageResult<DictTypeDTO> result = dictTypeService.listDictTypes(query);

        // Then
        assertNotNull(result);
        assertTrue(result.getRecords().isEmpty());
        assertEquals(0, result.getTotal());
    }

    // ==================== 根据 ID 查询测试 ====================

    @Test
    @DisplayName("根据 ID 查询字典类型 - 成功")
    void testGetDictTypeById_Success() {
        // Given
        Long dictId = 1L;
        when(dictTypeMapper.selectById(dictId)).thenReturn(testEntity);
        when(dictTypeConverter.toDto(testEntity)).thenReturn(testDTO);

        // When
        DictTypeDTO result = dictTypeService.getDictTypeById(dictId);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getDictId());
        assertEquals("用户性别", result.getDictName());
        assertEquals("sys_user_sex", result.getDictType());
        verify(dictTypeMapper, times(1)).selectById(dictId);
        verify(dictTypeConverter, times(1)).toDto(testEntity);
    }

    @Test
    @DisplayName("根据 ID 查询字典类型 - 不存在")
    void testGetDictTypeById_NotFound() {
        // Given
        Long dictId = 999L;
        when(dictTypeMapper.selectById(dictId)).thenReturn(null);

        // When & Then
        assertThrows(ServiceException.class, () -> dictTypeService.getDictTypeById(dictId));
        verify(dictTypeMapper, times(1)).selectById(dictId);
        verify(dictTypeConverter, never()).toDto(any());
    }

    // ==================== 根据字典类型查询测试 ====================

    @Test
    @DisplayName("根据字典类型查询 - 成功")
    void testGetDictTypeByType_Success() {
        // Given
        String dictType = "sys_user_sex";
        when(dictTypeMapper.selectByDictType(dictType)).thenReturn(testEntity);
        when(dictTypeConverter.toDto(testEntity)).thenReturn(testDTO);

        // When
        DictTypeDTO result = dictTypeService.getDictTypeByType(dictType);

        // Then
        assertNotNull(result);
        assertEquals("sys_user_sex", result.getDictType());
        verify(dictTypeMapper, times(1)).selectByDictType(dictType);
        verify(dictTypeConverter, times(1)).toDto(testEntity);
    }

    @Test
    @DisplayName("根据字典类型查询 - 不存在")
    void testGetDictTypeByType_NotFound() {
        // Given
        String dictType = "not_exist";
        when(dictTypeMapper.selectByDictType(dictType)).thenReturn(null);

        // When
        DictTypeDTO result = dictTypeService.getDictTypeByType(dictType);

        // Then
        assertNull(result);
        verify(dictTypeMapper, times(1)).selectByDictType(dictType);
        verify(dictTypeConverter, never()).toDto(any());
    }

    // ==================== 创建字典类型测试 ====================

    @Test
    @DisplayName("创建字典类型 - 成功")
    void testCreateDictType_Success() {
        // Given
        DictTypeDTO newDTO = DictTypeDTO.builder()
                .dictName("新字典类型")
                .dictType("new_dict_type")
                .status("0")
                .remark("新字典")
                .build();

        when(dictTypeMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(dictTypeConverter.dtoToEntity(newDTO)).thenReturn(testEntity);
        when(dictTypeMapper.insert(any(SysDictType.class))).thenReturn(1);

        // When
        Long result = dictTypeService.createDictType(newDTO);

        // Then
        assertNotNull(result);
        verify(dictTypeMapper, times(1)).selectCount(any(LambdaQueryWrapper.class));
        verify(dictTypeConverter, times(1)).dtoToEntity(newDTO);
        verify(dictTypeMapper, times(1)).insert(any(SysDictType.class));
    }

    @Test
    @DisplayName("创建字典类型 - 字典类型已存在")
    void testCreateDictType_DictTypeExists() {
        // Given
        DictTypeDTO newDTO = DictTypeDTO.builder()
                .dictName("用户性别")
                .dictType("sys_user_sex")
                .status("0")
                .build();

        when(dictTypeMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // When & Then
        assertThrows(ServiceException.class, () -> dictTypeService.createDictType(newDTO));
        verify(dictTypeMapper, times(1)).selectCount(any(LambdaQueryWrapper.class));
        verify(dictTypeConverter, never()).dtoToEntity(any(DictTypeDTO.class));
        verify(dictTypeMapper, never()).insert(any(SysDictType.class));
    }

    // ==================== 更新字典类型测试 ====================

    @Test
    @DisplayName("更新字典类型 - 成功")
    void testUpdateDictType_Success() {
        // Given
        DictTypeDTO updateDTO = DictTypeDTO.builder()
                .dictId(1L)
                .dictName("更新后的名称")
                .status("0")
                .build();

        when(dictTypeMapper.selectById(1L)).thenReturn(testEntity);
        when(dictTypeConverter.dtoToEntity(updateDTO)).thenAnswer(invocation -> {
            SysDictType entity = new SysDictType();
            entity.setDictId(updateDTO.getDictId());
            entity.setDictName(updateDTO.getDictName());
            entity.setDictType(updateDTO.getDictType());
            entity.setStatus(updateDTO.getStatus());
            entity.setRemark(updateDTO.getRemark());
            return entity;
        });
        when(dictTypeMapper.updateById(any(SysDictType.class))).thenReturn(1);

        // When
        dictTypeService.updateDictType(updateDTO);

        // Then
        verify(dictTypeMapper, times(1)).selectById(1L);
        verify(dictTypeMapper, times(1)).updateById(any(SysDictType.class));
    }

    @Test
    @DisplayName("更新字典类型 - 字典不存在")
    void testUpdateDictType_NotFound() {
        // Given
        DictTypeDTO updateDTO = DictTypeDTO.builder()
                .dictId(999L)
                .dictName("更新后的名称")
                .build();

        when(dictTypeMapper.selectById(999L)).thenReturn(null);

        // When & Then
        assertThrows(ServiceException.class, () -> dictTypeService.updateDictType(updateDTO));
        verify(dictTypeMapper, times(1)).selectById(999L);
        verify(dictTypeMapper, never()).updateById(any(SysDictType.class));
    }

    // ==================== 删除字典类型测试 ====================

    @Test
    @DisplayName("删除字典类型 - 成功")
    void testDeleteDictType_Success() {
        // Given
        Long dictId = 1L;
        when(dictTypeMapper.selectById(dictId)).thenReturn(testEntity);
        when(dictTypeMapper.deleteById(dictId)).thenReturn(1);

        // When
        dictTypeService.deleteDictType(dictId);

        // Then
        verify(dictTypeMapper, times(1)).selectById(dictId);
        verify(dictTypeMapper, times(1)).deleteById(dictId);
    }

    @Test
    @DisplayName("删除字典类型 - 字典不存在")
    void testDeleteDictType_NotFound() {
        // Given
        Long dictId = 999L;
        when(dictTypeMapper.selectById(dictId)).thenReturn(null);

        // When & Then
        assertThrows(ServiceException.class, () -> dictTypeService.deleteDictType(dictId));
        verify(dictTypeMapper, times(1)).selectById(dictId);
        verify(dictTypeMapper, never()).deleteById(any());
    }

    @Test
    @DisplayName("批量删除字典类型 - 成功")
    void testDeleteDictTypes_Success() {
        // Given
        Long[] dictIds = {1L, 2L};
        when(dictTypeMapper.selectById(1L)).thenReturn(testEntity);
        when(dictTypeMapper.selectById(2L)).thenReturn(testEntity);
        when(dictTypeMapper.deleteById(1L)).thenReturn(1);
        when(dictTypeMapper.deleteById(2L)).thenReturn(1);

        // When
        dictTypeService.deleteDictTypes(dictIds);

        // Then
        verify(dictTypeMapper, times(1)).selectById(1L);
        verify(dictTypeMapper, times(1)).selectById(2L);
        verify(dictTypeMapper, times(1)).deleteById(1L);
        verify(dictTypeMapper, times(1)).deleteById(2L);
    }

    // ==================== 检查唯一性测试 ====================

    @Test
    @DisplayName("检查字典类型唯一性 - 唯一")
    void testCheckDictTypeUnique_Unique() {
        // Given
        String dictType = "new_dict_type";
        when(dictTypeMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        // When
        boolean result = dictTypeService.checkDictTypeUnique(dictType);

        // Then
        assertTrue(result);
        verify(dictTypeMapper, times(1)).selectCount(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("检查字典类型唯一性 - 已存在")
    void testCheckDictTypeUnique_NotUnique() {
        // Given
        String dictType = "sys_user_sex";
        when(dictTypeMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // When
        boolean result = dictTypeService.checkDictTypeUnique(dictType);

        // Then
        assertFalse(result);
        verify(dictTypeMapper, times(1)).selectCount(any(LambdaQueryWrapper.class));
    }
}
