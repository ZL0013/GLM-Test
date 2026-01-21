package com.xie.glm.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单数据传输对象
 *
 * <p>用于服务层返回给前端展示的菜单数据。
 *
 * <p>字段说明：
 * <ul>
 *   <li>menuId：菜单 ID</li>
 *   <li>menuName：菜单名称</li>
 *   <li>parentId：父菜单 ID（0=顶级菜单）</li>
 *   <li>orderNum：显示顺序</li>
 *   <li>path：路由地址</li>
 *   <li>component：组件路径</li>
 *   <li>query：路由参数</li>
 *   <li>routeName：路由名称</li>
 *   <li>isFrame：是否为外链（0=否，1=是）</li>
 *   <li>isCache：是否缓存（0=缓存，1=不缓存）</li>
 *   <li>menuType：菜单类型（M=目录，C=菜单，F=按钮）</li>
 *   <li>visible：显示状态（0=显示，1=隐藏）</li>
 *   <li>status：菜单状态（0=正常，1=停用）</li>
 *   <li>perms：权限标识</li>
 *   <li>icon：菜单图标</li>
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
public class MenuDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 菜单基本信息 ====================

    /**
     * 菜单 ID
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单 ID（0=顶级菜单）
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 路由名称
     */
    private String routeName;

    // ==================== 菜单属性 ====================

    /**
     * 是否为外链（0=否，1=是）
     */
    private Integer isFrame;

    /**
     * 是否缓存（0=缓存，1=不缓存）
     */
    private Integer isCache;

    /**
     * 菜单类型（M=目录，C=菜单，F=按钮）
     */
    private String menuType;

    /**
     * 显示状态（0=显示，1=隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0=正常，1=停用）
     */
    private String status;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    // ==================== 时间字段 ====================

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
