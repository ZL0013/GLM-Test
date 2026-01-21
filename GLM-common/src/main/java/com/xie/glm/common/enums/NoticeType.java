package com.xie.glm.common.enums;

/**
 * 通知公告类型枚举
 *
 * @author xie
 */
public enum NoticeType {
    /**
     * 通知
     */
    NOTICE,

    /**
     * 公告
     */
    ANNOUNCEMENT;

    /**
     * 获取显示名称
     */
    public String getDisplayName() {
        return switch (this) {
            case NOTICE -> "通知";
            case ANNOUNCEMENT -> "公告";
        };
    }
}
