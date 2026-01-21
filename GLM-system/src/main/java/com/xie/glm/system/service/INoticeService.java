package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.NoticeDTO;
import com.xie.glm.system.dto.query.NoticeQueryDTO;

/**
 * 通知公告服务接口
 *
 * @author xie
 */
public interface INoticeService {

    /**
     * 分页查询通知公告列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<NoticeDTO> listNotices(NoticeQueryDTO query);

    /**
     * 根据ID查询通知公告详情
     *
     * @param noticeId 通知ID
     * @return 通知公告详情
     */
    NoticeDTO getNoticeById(Long noticeId);

    /**
     * 创建通知公告
     *
     * @param dto 通知公告数据
     */
    void createNotice(NoticeDTO dto);

    /**
     * 更新通知公告
     *
     * @param dto 通知公告数据
     */
    void updateNotice(NoticeDTO dto);

    /**
     * 删除通知公告
     *
     * @param noticeId 通知ID
     */
    void deleteNotice(Long noticeId);
}
