package com.xie.glm.admin.facade;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.NoticeDTO;
import com.xie.glm.system.dto.query.NoticeQueryDTO;
import com.xie.glm.system.service.INoticeService;
import com.xie.glm.admin.vo.NoticeVO;
import com.xie.glm.admin.converter.NoticeVoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 通知公告门面
 *
 * @author xie
 */
@Service
@RequiredArgsConstructor
public class NoticeFacade {

    private final INoticeService noticeService;
    private final NoticeVoConverter noticeVoConverter;

    /**
     * 分页查询通知公告列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    public PageResult<NoticeVO> listNotices(NoticeQueryDTO query) {
        PageResult<NoticeDTO> dtoPage = noticeService.listNotices(query);
        return new PageResult<>(
                noticeVoConverter.toVoList(dtoPage.getRecords()),
                dtoPage.getTotal()
        );
    }

    /**
     * 根据ID查询通知公告详情
     *
     * @param noticeId 通知ID
     * @return 通知公告详情
     */
    public NoticeVO getNoticeById(Long noticeId) {
        NoticeDTO dto = noticeService.getNoticeById(noticeId);
        return noticeVoConverter.toVo(dto);
    }

    /**
     * 创建通知公告
     *
     * @param dto 通知公告数据
     */
    public void createNotice(NoticeDTO dto) {
        noticeService.createNotice(dto);
    }

    /**
     * 更新通知公告
     *
     * @param dto 通知公告数据
     */
    public void updateNotice(NoticeDTO dto) {
        noticeService.updateNotice(dto);
    }

    /**
     * 删除通知公告
     *
     * @param noticeId 通知ID
     */
    public void deleteNotice(Long noticeId) {
        noticeService.deleteNotice(noticeId);
    }
}
