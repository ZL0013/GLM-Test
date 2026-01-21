package com.xie.glm.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典类型数据传输对象
 *
 * <p>用于服务层返回给前端展示的字典类型数据。
 *
 * <p>字段说明：
 * <ul>
 *   <li>dictId：字典主键</li>
 *   <li>dictName：字典名称</li>
 *   <li>dictType：字典类型</li>
 *   <li>status：状态（0=正常，1=停用）</li>
 *   <li>remark：备注</li>
 *   <li>createTime：创建时间</li>
 *   <li>updateTime：更新时间</li>
 * </ul>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DictTypeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    private Long dictId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（0=正常，1=停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
