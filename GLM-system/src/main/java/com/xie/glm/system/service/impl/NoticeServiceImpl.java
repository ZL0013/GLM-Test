package com.xie.glm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.common.enums.BusinessStatus;
import com.xie.glm.common.exception.ServiceException;
import com.xie.glm.system.domain.SysNotice;
import com.xie.glm.system.dto.NoticeDTO;
import com.xie.glm.system.dto.query.NoticeQueryDTO;
import com.xie.glm.system.mapper.SysNoticeMapper;
import com.xie.glm.system.service.INoticeService;
import com.xie.glm.system.converter.NoticeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 通知公告服务实现
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements INoticeService {

    private final SysNoticeMapper noticeMapper;
    private final NoticeConverter noticeConverter;

    @Override
    public PageResult<NoticeDTO> listNotices(NoticeQueryDTO query) {
        // 构建查询条件
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getNoticeTitle()),
                SysNotice::getNoticeTitle, query.getNoticeTitle())
                .eq(query.getNoticeType() != null,
                        SysNotice::getNoticeType, query.getNoticeType())
                .eq(query.getStatus() != null,
                        SysNotice::getStatus, query.getStatus())
                .orderByDesc(SysNotice::getCreateTime);

        // 分页查询
        Page<SysNotice> page = noticeMapper.selectPage(
                new Page<>(query.getPageNum(), query.getPageSize()),
                wrapper
        );

        // 转换为 DTO
        return new PageResult<>(
                noticeConverter.toDtoList(page.getRecords()),
                page.getTotal()
        );
    }

    @Override
    public NoticeDTO getNoticeById(Long noticeId) {
        SysNotice notice = noticeMapper.selectById(noticeId);
        if (notice == null) {
            throw new ServiceException(BusinessStatus.NOTICE_NOT_FOUND);
        }
        return noticeConverter.toDto(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotice(NoticeDTO dto) {
        SysNotice notice = noticeConverter.toEntity(dto);
        noticeMapper.insert(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotice(NoticeDTO dto) {
        SysNotice notice = noticeMapper.selectById(dto.getNoticeId());
        if (notice == null) {
            throw new ServiceException(BusinessStatus.NOTICE_NOT_FOUND);
        }

        SysNotice updateNotice = noticeConverter.toEntity(dto);
        updateNotice.setNoticeId(dto.getNoticeId());
        noticeMapper.updateById(updateNotice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotice(Long noticeId) {
        SysNotice notice = noticeMapper.selectById(noticeId);
        if (notice == null) {
            throw new ServiceException(BusinessStatus.NOTICE_NOT_FOUND);
        }
        noticeMapper.deleteById(noticeId);
    }
}
