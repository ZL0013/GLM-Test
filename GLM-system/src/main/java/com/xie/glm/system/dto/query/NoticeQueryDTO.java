package com.xie.glm.system.dto.query;

import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import com.xie.glm.common.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知查询条件 DTO
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeQueryDTO extends PageQuery {

    /**
     * 通知公告标题
     */
    private String noticeTitle;

    /**
     * 通知公告类型（1通知 2公告）
     */
    private NoticeType noticeType;

    /**
     * 通知公告状态（0正常 1关闭）
     */
    private NoticeStatus status;
}
