package com.xie.glm.system.dto.query;

import com.xie.glm.common.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单查询条件 DTO
 *
 * <p>用于菜单列表查询的数据传输对象，继承 {@link PageQuery} 获得分页和排序能力。
 *
 * <p>包含以下查询条件：
 * <ul>
 *   <li>菜单名称（模糊搜索）</li>
 *   <li>菜单类型筛选（M=目录，C=菜单，F=按钮）</li>
 *   <li>菜单状态筛选（0=正常，1=停用）</li>
 *   <li>显示状态筛选（0=显示，1=隐藏）</li>
 *   <li>父菜单ID筛选（0=顶级菜单）</li>
 * </ul>
 *
 * <p>分页和排序参数继承自父类 {@link PageQuery}：
 * <ul>
 *   <li>pageNum：页码</li>
 *   <li>pageSize：每页大小</li>
 *   <li>orderByColumn：排序列（推荐：order_num、menu_id）</li>
 *   <li>isAsc：排序方向（asc/desc）</li>
 * </ul>
 *
 * @author xie
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuQueryDTO extends PageQuery {

    // ==================== 菜单基本信息查询条件 ====================

    /**
     * 菜单名称（模糊搜索）
     */
    private String menuName;

    /**
     * 菜单类型（M=目录，C=菜单，F=按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0=正常，1=停用）
     */
    private String status;

    /**
     * 显示状态（0=显示，1=隐藏）
     */
    private String visible;

    /**
     * 父菜单 ID（0=顶级菜单）
     */
    private Long parentId;
}
