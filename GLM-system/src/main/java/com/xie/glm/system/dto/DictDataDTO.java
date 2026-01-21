package com.xie.glm.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典数据传输对象
 *
 * <p>用于服务层返回给前端展示的字典数据。
 *
 * <p>字段说明：
 * <ul>
 *   <li>dictCode：字典编码</li>
 *   <li>dictSort：字典排序</li>
 *   <li>dictLabel：字典标签</li>
 *   <li>dictValue：字典键值</li>
 *   <li>dictType：字典类型</li>
 *   <li>cssClass：样式属性</li>
 *   <li>listClass：表格回显样式</li>
 *   <li>isDefault：是否默认（Y=是，N=否）</li>
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
public class DictDataDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    private Long dictCode;

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（Y=是，N=否）
     */
    private String isDefault;

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
