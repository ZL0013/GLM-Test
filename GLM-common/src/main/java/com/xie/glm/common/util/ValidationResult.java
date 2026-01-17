package com.xie.glm.common.util;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 密码校验结果
 *
 * @author xie
 */
public record ValidationResult(boolean valid, List<String> errors) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建成功结果
     */
    public static ValidationResult success() {
        return new ValidationResult(true, Collections.emptyList());
    }

    /**
     * 创建失败结果
     *
     * @param errors 错误消息列表
     */
    public static ValidationResult fail(List<String> errors) {
        return new ValidationResult(false, errors);
    }
}
