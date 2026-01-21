package com.xie.glm.admin.vo;

import com.xie.glm.system.dto.DeptDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * 部门视图对象
 * 用于前端展示，支持树形结构展示，包含子部门列表
 *
 * <p>字段说明：
 * <ul>
 *   <li>继承 DeptDTO 的所有字段（部门基本信息、联系信息、状态字段、时间字段）</li>
 *   <li>children：子部门列表，用于构建树形结构</li>
 * </ul>
 *
 * <p>树形结构示例：
 * <pre>
 * DeptVO (公司)
 *   ├── children[0] -> DeptVO (研发部)
 *   │     ├── children[0] -> DeptVO (前端组)
 *   │     └── children[1] -> DeptVO (后端组)
 *   ├── children[1] -> DeptVO (市场部)
 *   └── children[2] -> DeptVO (财务部)
 * </pre>
 *
 * @author xie
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门视图对象")
public class DeptVO extends DeptDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 子部门列表
     * <p>用于构建树形结构，null 表示没有子部门或尚未加载
     */
    @Schema(description = "子部门列表", example = "[{deptId: 2, deptName: '研发部'}]")
    private List<DeptVO> children;
}
