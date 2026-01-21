package com.xie.glm.admin.controller;

import com.xie.glm.admin.facade.UserFacade;
import com.xie.glm.admin.vo.UserVO;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import com.xie.glm.system.dto.query.UserQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制器
 *
 * <p>提供用户 CRUD 操作的 REST API。
 *
 * <p>接口列表：
 * <ul>
 *   <li>GET /api/system/users - 分页查询用户列表</li>
 *   <li>GET /api/system/users/{id} - 查询用户详情</li>
 *   <li>POST /api/system/users - 创建用户</li>
 *   <li>PUT /api/system/users/{id} - 更新用户</li>
 *   <li>DELETE /api/system/users/{id} - 删除用户</li>
 *   <li>DELETE /api/system/users - 批量删除用户</li>
 *   <li>PUT /api/system/users/{id}/password - 重置用户密码</li>
 *   <li>PUT /api/system/users/{id}/status - 修改用户状态</li>
 * </ul>
 *
 * <p>权限要求：
 * <ul>
 *   <li>system:user:list - 查看用户列表</li>
 *   <li>system:user:query - 查看用户详情</li>
 *   <li>system:user:add - 创建用户</li>
 *   <li>system:user:edit - 编辑用户</li>
 *   <li>system:user:remove - 删除用户</li>
 *   <li>system:user:resetPwd - 重置密码</li>
 * </ul>
 *
 * <p>所有接口返回值由 ResponseAdvice 自动包装为 {@link Result} 格式。
 *
 * @author xie
 */
@Tag(name = "用户管理", description = "用户CRUD操作接口")
@RestController
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    /**
     * 分页查询用户列表
     *
     * @param query 查询条件（用户名、昵称、邮箱、手机号、状态等）
     * @return 分页结果（包含用户列表和总记录数），由 ResponseAdvice 自动包装
     */
    @GetMapping
    @Operation(summary = "分页查询用户列表", description = "支持按用户名、昵称、邮箱、手机号、状态等条件查询")
    @PreAuthorize("hasAuthority('system:user:list')")
    public PageResult<UserVO> list(UserQueryDTO query) {
        return userFacade.listUsers(query);
    }

    /**
     * 查询用户详情
     *
     * @param id 用户 ID
     * @return 用户详情，由 ResponseAdvice 自动包装
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情", description = "根据用户ID查询用户详细信息")
    @PreAuthorize("hasAuthority('system:user:query')")
    public UserVO getDetail(@PathVariable Long id) {
        return userFacade.getUserById(id);
    }

    /**
     * 创建用户
     *
     * @param dto 创建用户 DTO（包含用户名、密码、昵称等基本信息）
     * @return 创建的用户 ID，由 ResponseAdvice 自动包装
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户，密码会进行BCrypt加密")
    @PreAuthorize("hasAuthority('system:user:add')")
    public Long create(@Valid @RequestBody UserCreateDTO dto) {
        return userFacade.createUser(dto);
    }

    /**
     * 更新用户
     *
     * @param id 用户 ID
     * @param dto 更新用户 DTO（包含需要更新的字段）
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新用户基本信息，不包括密码")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public void update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        dto.setUserId(id);
        userFacade.updateUser(dto);
    }

    /**
     * 删除用户
     *
     * @param id 用户 ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public void delete(@PathVariable Long id) {
        userFacade.deleteUser(id);
    }

    /**
     * 批量删除用户
     *
     * @param userIds 用户 ID 数组
     */
    @DeleteMapping
    @Operation(summary = "批量删除用户", description = "根据用户ID数组批量删除用户")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public void deleteBatch(@RequestBody Long[] userIds) {
        userFacade.deleteUsers(userIds);
    }

    /**
     * 重置用户密码
     *
     * <p>将用户密码重置为默认密码（如：123456）
     *
     * @param id 用户 ID
     */
    @PutMapping("/{id}/password")
    @Operation(summary = "重置用户密码", description = "将用户密码重置为默认密码")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public void resetPassword(@PathVariable Long id) {
        userFacade.resetPassword(id);
    }

    /**
     * 修改用户状态
     *
     * @param id 用户 ID
     * @param status 状态（0=正常，1=停用）
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "修改用户状态", description = "启用或停用用户账号")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        userFacade.updateStatus(id, status);
    }
}
