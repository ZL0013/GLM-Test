package com.xie.glm.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xie.glm.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统字典数据实体
 *
 * <p>对应数据库表：xie_tm.sys_dict_data
 *
 * <p>用于管理系统中的字典数据，字典数据是字典类型下的具体选项。
 * 例如：字典类型"用户性别"下有"男"、"女"等字典数据。
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("xie_tm.sys_dict_data")
public class SysDictData extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    @TableId(value = "dict_code", type = IdType.AUTO)
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
}
