package com.xie.glm.common.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 *
 * <p>所有实体类都应继承此类，包含统一的审计字段：
 * <ul>
 *   <li>createTime：创建时间</li>
 *   <li>updateTime：更新时间</li>
 *   <li>createdBy：创建人</li>
 *   <li>updatedBy：更新人</li>
 * </ul>
 *
 * <p>注意：主键字段应由各实体类根据业务需求自行定义，不在基类中包含。
 *
 * @author xie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;
}
