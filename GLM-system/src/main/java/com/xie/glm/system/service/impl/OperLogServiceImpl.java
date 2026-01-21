package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.converter.OperLogConverter;
import com.xie.glm.system.domain.SysOperLog;
import com.xie.glm.system.dto.OperLogDTO;
import com.xie.glm.system.dto.query.LogQueryDTO;
import com.xie.glm.system.mapper.SysOperLogMapper;
import com.xie.glm.system.service.IOperLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 操作日志服务实现类
 *
 * <p>实现操作日志管理的业务逻辑，包括操作日志的增删改查、清空等功能。
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class OperLogServiceImpl implements IOperLogService {

    private final SysOperLogMapper operLogMapper;
    private final OperLogConverter operLogConverter;

    @Override
    public PageResult<OperLogDTO> listOperLogs(LogQueryDTO query) {
        // 构建分页对象
        Page<SysOperLog> page = new Page<>(
            query.getPageNum() != null ? query.getPageNum() : 1,
            query.getPageSize() != null ? query.getPageSize() : 10
        );

        // 构建查询条件
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getTitle()), SysOperLog::getTitle, query.getTitle())
            .eq(query.getBusinessType() != null, SysOperLog::getBusinessType, query.getBusinessType())
            .like(StringUtils.hasText(query.getOperName()), SysOperLog::getOperName, query.getOperName())
            .eq(query.getStatus() != null, SysOperLog::getStatus, query.getStatus())
            .ge(query.getStartTime() != null, SysOperLog::getOperTime, query.getStartTime())
            .le(query.getEndTime() != null, SysOperLog::getOperTime, query.getEndTime())
            .orderByDesc(SysOperLog::getOperTime);

        // 执行分页查询
        IPage<SysOperLog> pageResult = operLogMapper.selectPage(page, wrapper);

        // 转换为 DTO
        List<OperLogDTO> records = operLogConverter.toDtoList(pageResult.getRecords());

        return new PageResult<>(records, pageResult.getTotal());
    }

    @Override
    public OperLogDTO getOperLogById(Long operId) {
        SysOperLog operLog = operLogMapper.selectById(operId);
        if (operLog == null) {
            return null;
        }
        return operLogConverter.toDto(operLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOperLog(OperLogDTO dto) {
        // 转换 DTO 为 Entity
        SysOperLog operLog = operLogConverter.toEntity(dto);

        // 设置操作时间
        if (operLog.getOperTime() == null) {
            operLog.setOperTime(LocalDateTime.now());
        }

        // 插入操作日志
        operLogMapper.insert(operLog);

        return operLog.getOperId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOperLog(Long operId) {
        SysOperLog operLog = operLogMapper.selectById(operId);
        if (operLog == null) {
            throw new ServiceException(BusinessStatus.OPER_LOG_NOT_FOUND);
        }

        // 物理删除
        operLogMapper.deleteById(operId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOperLogs(Long[] operIds) {
        // 批量物理删除
        operLogMapper.deleteBatchIds(Arrays.asList(operIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanOperLogs() {
        // 清空所有操作日志（物理删除）
        operLogMapper.delete(null);
    }
}
