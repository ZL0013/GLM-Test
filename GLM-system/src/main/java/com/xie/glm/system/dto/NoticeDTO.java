package com.xie.glm.system.dto;

import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知公告 DTO
 *
 * @author xie
 */
@Data
public class NoticeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知公告ID
     */
    private Long noticeId;

    /**
     * 通知公告标题
     */
    private String noticeTitle;

    /**
     * 通知公告类型（通知/公告）
     */
    private NoticeType noticeType;

    /**
     * 通知公告内容
     */
    private String noticeContent;

    /**
     * 通知公告状态（正常/关闭）
     */
    private NoticeStatus status;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}
