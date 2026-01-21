package com.xie.glm.admin.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知公告 VO
 *
 * @author xie
 */
@Data
public class NoticeVO implements Serializable {

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
     * 通知公告类型名称（通知/公告）
     */
    private String noticeTypeName;

    /**
     * 通知公告内容
     */
    private String noticeContent;

    /**
     * 通知公告状态名称（正常/关闭）
     */
    private String statusName;

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
