package com.xie.glm.admin.vo;

import com.xie.glm.system.dto.MenuDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * 菜单视图对象
 * 用于前端展示，支持树形结构展示，包含子菜单列表
 *
 * <p>字段说明：
 * <ul>
 *   <li>继承 MenuDTO 的所有字段（菜单基本信息、属性、时间字段）</li>
 *   <li>children：子菜单列表，用于构建树形结构</li>
 * </ul>
 *
 * <p>树形结构示例：
 * <pre>
 * MenuVO (系统管理)
 *   ├── children[0] -> MenuVO (用户管理)
 *   │     ├── children[0] -> MenuVO (用户查询)
 *   │     └── children[1] -> MenuVO (用户新增)
 *   ├── children[1] -> MenuVO (角色管理)
 *   └── children[2] -> MenuVO (菜单管理)
 * </pre>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单视图对象")
public class MenuVO extends MenuDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 子菜单列表
     * <p>用于构建树形结构，null 表示没有子菜单或尚未加载
     */
    @Schema(description = "子菜单列表", example = "[{menuId: 2, menuName: '用户管理'}]")
    private List<MenuVO> children;
}
