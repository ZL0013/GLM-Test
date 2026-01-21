package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.converter.DictDataConverter;
import com.xie.glm.system.domain.SysDictData;
import com.xie.glm.system.dto.DictDataDTO;
import com.xie.glm.system.dto.query.DictQueryDTO;
import com.xie.glm.system.mapper.SysDictDataMapper;
import com.xie.glm.system.service.IDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 字典数据服务实现类
 *
 * <p>实现字典数据管理的业务逻辑，包括字典数据的增删改查等功能。
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class DictDataServiceImpl implements IDictDataService {

    private final SysDictDataMapper dictDataMapper;
    private final DictDataConverter dictDataConverter;

    @Override
    public PageResult<DictDataDTO> listDictData(DictQueryDTO query) {
        // 构建分页对象
        Page<SysDictData> page = new Page<>(
            query.getPageNum() != null ? query.getPageNum() : 1,
            query.getPageSize() != null ? query.getPageSize() : 10
        );

        // 构建查询条件
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getDictLabel()), SysDictData::getDictLabel, query.getDictLabel())
            .eq(StringUtils.hasText(query.getDictType()), SysDictData::getDictType, query.getDictType())
            .eq(StringUtils.hasText(query.getStatus()), SysDictData::getStatus, query.getStatus())
            .ge(query.getStartTime() != null, SysDictData::getCreateTime, query.getStartTime())
            .le(query.getEndTime() != null, SysDictData::getCreateTime, query.getEndTime())
            .orderByAsc(SysDictData::getDictSort);

        // 执行分页查询
        IPage<SysDictData> pageResult = dictDataMapper.selectPage(page, wrapper);

        // 转换为 DTO
        List<DictDataDTO> records = dictDataConverter.toDtoList(pageResult.getRecords());

        return new PageResult<>(records, pageResult.getTotal());
    }

    @Override
    public DictDataDTO getDictDataById(Long dictCode) {
        SysDictData dictData = dictDataMapper.selectById(dictCode);
        if (dictData == null) {
            return null;
        }
        return dictDataConverter.toDto(dictData);
    }

    @Override
    public List<DictDataDTO> listDictDataByType(String dictType) {
        List<SysDictData> entityList = dictDataMapper.selectByDictType(dictType);
        return dictDataConverter.toDtoList(entityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDictData(DictDataDTO dto) {
        // 转换 DTO 为 Entity
        SysDictData dictData = dictDataConverter.dtoToEntity(dto);

        // 设置默认值
        dictData.setStatus(StringUtils.hasText(dictData.getStatus()) ? dictData.getStatus() : "0");
        dictData.setCreateTime(LocalDateTime.now());
        dictData.setUpdateTime(LocalDateTime.now());

        // 插入字典数据
        dictDataMapper.insert(dictData);

        return dictData.getDictCode();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictData(DictDataDTO dto) {
        // 检查字典数据是否存在
        SysDictData existingDictData = dictDataMapper.selectById(dto.getDictCode());
        if (existingDictData == null) {
            return;
        }

        // 转换 DTO 为 Entity
        SysDictData dictData = dictDataConverter.dtoToEntity(dto);

        // 更新时间
        dictData.setUpdateTime(LocalDateTime.now());

        // 更新字典数据
        dictDataMapper.updateById(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictData(Long dictCode) {
        SysDictData dictData = dictDataMapper.selectById(dictCode);
        if (dictData == null) {
            return;
        }

        // 删除字典数据
        dictDataMapper.deleteById(dictCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictData(Long[] dictCodes) {
        Arrays.stream(dictCodes).forEach(dictCode -> {
            SysDictData dictData = dictDataMapper.selectById(dictCode);
            if (dictData != null) {
                dictDataMapper.deleteById(dictCode);
            }
        });
    }
}
