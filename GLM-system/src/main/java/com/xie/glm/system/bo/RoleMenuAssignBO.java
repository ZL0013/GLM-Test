package com.xie.glm.system.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 角色菜单权限分配业务对象
 *
 * <p>用于角色分配菜单权限时的内部业务对象，Service 层内部使用。
 *
 * <p>字段说明：
 * <ul>
 *   <li>roleId：角色 ID</li>
 *   <li>menuIds：菜单权限 ID 列表</li>
 *   <li>menuCheckStrictly：菜单树选择项是否关联显示</li>
 * </ul>
 *
 * <p>使用场景：
 * <ul>
 *   <li>创建角色时关联菜单权限</li>
 *   <li>更新角色菜单权限</li>
 *   <li>为角色批量分配菜单权限</li>
 * </ul>
 *
 * @author xie
 */
@Data
public class RoleMenuAssignBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 菜单权限 ID 列表
     *
     * <p>需要分配给角色的菜单权限 ID 列表
     */
    private List<Long> menuIds;

    /**
     * 菜单树选择项是否关联显示
     *
     * <p>如果为 true，父子节点选中状态不关联；如果为 false，父子节点选中状态关联
     */
    private Boolean menuCheckStrictly;
}
