package com.xie.glm.common.enums;

/**
 * 通知公告状态枚举
 *
 * @author xie
 */
public enum NoticeStatus {
    /**
     * 正常
     */
    NORMAL,

    /**
     * 关闭
     */
    CLOSED;

    /**
     * 获取显示名称
     */
    public String getDisplayName() {
        return switch (this) {
            case NORMAL -> "正常";
            case CLOSED -> "关闭";
        };
    }
}
