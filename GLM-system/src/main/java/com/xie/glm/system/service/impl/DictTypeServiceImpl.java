package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.DictTypeConverter;
import com.xie.glm.system.domain.SysDictType;
import com.xie.glm.system.dto.DictTypeDTO;
import com.xie.glm.system.dto.query.DictQueryDTO;
import com.xie.glm.system.mapper.SysDictTypeMapper;
import com.xie.glm.system.service.IDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 字典类型服务实现类
 *
 * <p>实现字典类型管理的业务逻辑，包括字典类型的增删改查等功能。
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class DictTypeServiceImpl implements IDictTypeService {

    private final SysDictTypeMapper dictTypeMapper;
    private final DictTypeConverter dictTypeConverter;

    @Override
    public PageResult<DictTypeDTO> listDictTypes(DictQueryDTO query) {
        // 构建分页对象
        Page<SysDictType> page = new Page<>(
            query.getPageNum() != null ? query.getPageNum() : 1,
            query.getPageSize() != null ? query.getPageSize() : 10
        );

        // 构建查询条件
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getDictName()), SysDictType::getDictName, query.getDictName())
            .like(StringUtils.hasText(query.getDictType()), SysDictType::getDictType, query.getDictType())
            .eq(StringUtils.hasText(query.getStatus()), SysDictType::getStatus, query.getStatus())
            .ge(query.getStartTime() != null, SysDictType::getCreateTime, query.getStartTime())
            .le(query.getEndTime() != null, SysDictType::getCreateTime, query.getEndTime())
            .orderByDesc(SysDictType::getCreateTime);

        // 执行分页查询
        IPage<SysDictType> pageResult = dictTypeMapper.selectPage(page, wrapper);

        // 转换为 DTO
        List<DictTypeDTO> records = dictTypeConverter.toDtoList(pageResult.getRecords());

        return new PageResult<>(records, pageResult.getTotal());
    }

    @Override
    public DictTypeDTO getDictTypeById(Long dictId) {
        SysDictType dictType = dictTypeMapper.selectById(dictId);
        if (dictType == null) {
            throw new ServiceException("字典类型不存在", null);
        }
        return dictTypeConverter.toDto(dictType);
    }

    @Override
    public DictTypeDTO getDictTypeByType(String dictType) {
        SysDictType entity = dictTypeMapper.selectByDictType(dictType);
        if (entity == null) {
            return null;
        }
        return dictTypeConverter.toDto(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDictType(DictTypeDTO dto) {
        // 检查字典类型唯一性
        if (!checkDictTypeUnique(dto.getDictType())) {
            throw new ServiceException(BusinessStatus.DICT_TYPE_DUPLICATE);
        }

        // 转换 DTO 为 Entity
        SysDictType dictType = dictTypeConverter.dtoToEntity(dto);

        // 设置默认值
        dictType.setStatus(StringUtils.hasText(dictType.getStatus()) ? dictType.getStatus() : "0");
        dictType.setCreateTime(LocalDateTime.now());
        dictType.setUpdateTime(LocalDateTime.now());

        // 插入字典类型
        dictTypeMapper.insert(dictType);

        return dictType.getDictId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictType(DictTypeDTO dto) {
        // 检查字典类型是否存在
        SysDictType existingDictType = dictTypeMapper.selectById(dto.getDictId());
        if (existingDictType == null) {
            throw new ServiceException("字典类型不存在", null);
        }

        // 转换 DTO 为 Entity
        SysDictType dictType = dictTypeConverter.dtoToEntity(dto);

        // 更新时间
        dictType.setUpdateTime(LocalDateTime.now());

        // 更新字典类型
        dictTypeMapper.updateById(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictType(Long dictId) {
        SysDictType dictType = dictTypeMapper.selectById(dictId);
        if (dictType == null) {
            throw new ServiceException("字典类型不存在", null);
        }

        // 删除字典类型
        dictTypeMapper.deleteById(dictId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictTypes(Long[] dictIds) {
        Arrays.stream(dictIds).forEach(dictId -> {
            SysDictType dictType = dictTypeMapper.selectById(dictId);
            if (dictType != null) {
                dictTypeMapper.deleteById(dictId);
            }
        });
    }

    @Override
    public boolean checkDictTypeUnique(String dictType) {
        Long count = dictTypeMapper.selectCount(
            new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDictType, dictType)
        );
        return count == 0;
    }
}
