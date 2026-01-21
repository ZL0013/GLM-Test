package com.xie.glm.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.glm.common.core.BaseEntity;
import com.xie.glm.common.enums.NoticeStatus;
import com.xie.glm.common.enums.NoticeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 通知公告实体
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("xie_tm.sys_notice")
public class SysNotice extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知公告ID
     */
    @TableId(value = "notice_id", type = IdType.AUTO)
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
     * 备注
     */
    private String remark;
}
