package com.xie.glm.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 业务操作类型枚举
 *
 * <p>用于日志记录，标识业务的操作类型
 *
 * @author xie
 */
@Getter
@AllArgsConstructor
public enum BusinessType {

    /**
     * 新增
     */
    INSERT(1, "新增"),

    /**
     * 修改
     */
    UPDATE(2, "修改"),

    /**
     * 删除
     */
    DELETE(3, "删除"),

    /**
     * 导出
     */
    EXPORT(4, "导出"),

    /**
     * 导入
     */
    IMPORT(5, "导入"),

    /**
     * 授权
     */
    GRANT(6, "授权"),

    /**
     * 其他
     */
    OTHER(9, "其他");

    /**
     * 操作类型编码
     */
    private final Integer code;

    /**
     * 操作类型描述
     */
    private final String description;

    /**
     * 根据 code 获取枚举
     *
     * @param code 操作类型编码
     * @return 对应的枚举值，不存在则返回 null
     */
    public static BusinessType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(type -> type.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
