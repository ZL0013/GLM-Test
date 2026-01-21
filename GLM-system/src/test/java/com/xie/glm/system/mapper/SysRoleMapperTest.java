package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.glm.system.domain.SysRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 角色 Mapper 接口测试
 *
 * <p>测试 {@link SysRoleMapper} 的各种场景：
 * <ul>
 *   <li>基础 CRUD 操作</li>
 *   <li>条件查询</li>
 *   <li>分页查询</li>
 *   <li>批量操作</li>
 * </ul>
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
class SysRoleMapperTest {

    @Mock
    private SysRoleMapper sysRoleMapper;

    // ==================== 基础 CRUD 测试 ====================

    @Test
    void testInsert() {
        // Given
        SysRole role = new SysRole();
        role.setRoleName("测试角色");
        role.setRoleKey("test");
        role.setRoleSort(1);
        role.setStatus("0");

        when(sysRoleMapper.insert(any(SysRole.class))).thenReturn(1);

        // When
        int result = sysRoleMapper.insert(role);

        // Then
        assertThat(result).isEqualTo(1);
        verify(sysRoleMapper, times(1)).insert(role);
    }

    @Test
    void testSelectById() {
        // Given
        Long roleId = 1L;
        SysRole role = new SysRole();
        role.setRoleId(roleId);
        role.setRoleName("管理员");

        when(sysRoleMapper.selectById(roleId)).thenReturn(role);

        // When
        SysRole result = sysRoleMapper.selectById(roleId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRoleId()).isEqualTo(roleId);
        assertThat(result.getRoleName()).isEqualTo("管理员");
        verify(sysRoleMapper, times(1)).selectById(roleId);
    }

    @Test
    void testUpdateById() {
        // Given
        SysRole role = new SysRole();
        role.setRoleId(1L);
        role.setRoleName("更新后的角色");

        when(sysRoleMapper.updateById(any(SysRole.class))).thenReturn(1);

        // When
        int result = sysRoleMapper.updateById(role);

        // Then
        assertThat(result).isEqualTo(1);
        verify(sysRoleMapper, times(1)).updateById(role);
    }

    @Test
    void testDeleteById() {
        // Given
        Long roleId = 1L;
        when(sysRoleMapper.deleteById(roleId)).thenReturn(1);

        // When
        int result = sysRoleMapper.deleteById(roleId);

        // Then
        assertThat(result).isEqualTo(1);
        verify(sysRoleMapper, times(1)).deleteById(roleId);
    }

    // ==================== 条件查询测试 ====================

    @Test
    void testSelectList() {
        // Given
        SysRole role1 = new SysRole();
        role1.setRoleId(1L);
        role1.setRoleName("管理员");

        SysRole role2 = new SysRole();
        role2.setRoleId(2L);
        role2.setRoleName("用户");

        List<SysRole> roles = Arrays.asList(role1, role2);
        when(sysRoleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(roles);

        // When
        List<SysRole> result = sysRoleMapper.selectList(new LambdaQueryWrapper<>());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRoleName()).isEqualTo("管理员");
        assertThat(result.get(1).getRoleName()).isEqualTo("用户");
        verify(sysRoleMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testSelectOne() {
        // Given
        SysRole role = new SysRole();
        role.setRoleId(1L);
        role.setRoleKey("admin");

        when(sysRoleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(role);

        // When
        SysRole result = sysRoleMapper.selectOne(
            new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, "admin")
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRoleKey()).isEqualTo("admin");
    }

    @Test
    void testSelectCount() {
        // Given
        Long expectedCount = 5L;
        when(sysRoleMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(expectedCount);

        // When
        Long result = sysRoleMapper.selectCount(new LambdaQueryWrapper<>());

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(sysRoleMapper, times(1)).selectCount(any(LambdaQueryWrapper.class));
    }

    // ==================== 分页查询测试 ====================

    @Test
    void testSelectPage() {
        // Given
        Page<SysRole> page = new Page<>(1, 10);
        SysRole role1 = new SysRole();
        role1.setRoleId(1L);
        role1.setRoleName("管理员");

        SysRole role2 = new SysRole();
        role2.setRoleId(2L);
        role2.setRoleName("用户");

        Page<SysRole> resultPage = new Page<>(1, 10);
        resultPage.setRecords(Arrays.asList(role1, role2));
        resultPage.setTotal(2);

        when(sysRoleMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(resultPage);

        // When
        IPage<SysRole> result = sysRoleMapper.selectPage(page, new LambdaQueryWrapper<>());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(2);
        assertThat(result.getTotal()).isEqualTo(2);
        verify(sysRoleMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    // ==================== 批量操作测试 ====================

    @Test
    void testSelectBatchIds() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        SysRole role1 = new SysRole();
        role1.setRoleId(1L);

        SysRole role2 = new SysRole();
        role2.setRoleId(2L);

        SysRole role3 = new SysRole();
        role3.setRoleId(3L);

        List<SysRole> roles = Arrays.asList(role1, role2, role3);
        when(sysRoleMapper.selectBatchIds(ids)).thenReturn(roles);

        // When
        List<SysRole> result = sysRoleMapper.selectBatchIds(ids);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getRoleId()).isEqualTo(1L);
        assertThat(result.get(1).getRoleId()).isEqualTo(2L);
        assertThat(result.get(2).getRoleId()).isEqualTo(3L);
        verify(sysRoleMapper, times(1)).selectBatchIds(ids);
    }

    @Test
    void testDeleteBatchIds() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(sysRoleMapper.deleteBatchIds(ids)).thenReturn(3);

        // When
        int result = sysRoleMapper.deleteBatchIds(ids);

        // Then
        assertThat(result).isEqualTo(3);
        verify(sysRoleMapper, times(1)).deleteBatchIds(ids);
    }

    // ==================== 状态查询测试 ====================

    @Test
    void testSelectByStatus() {
        // Given
        String status = "0";
        SysRole role1 = new SysRole();
        role1.setRoleId(1L);
        role1.setStatus(status);

        List<SysRole> roles = Arrays.asList(role1);
        when(sysRoleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(roles);

        // When
        List<SysRole> result = sysRoleMapper.selectList(
            new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus, status)
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(status);
    }

    // ==================== 角色名称查询测试 ====================

    @Test
    void testSelectByRoleName() {
        // Given
        String roleName = "管理员";
        SysRole role = new SysRole();
        role.setRoleId(1L);
        role.setRoleName(roleName);

        when(sysRoleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(role);

        // When
        SysRole result = sysRoleMapper.selectOne(
            new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, roleName)
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRoleName()).isEqualTo(roleName);
    }

    // ==================== 角色权限字符串查询测试 ====================

    @Test
    void testSelectByRoleKey() {
        // Given
        String roleKey = "admin";
        SysRole role = new SysRole();
        role.setRoleId(1L);
        role.setRoleKey(roleKey);

        when(sysRoleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(role);

        // When
        SysRole result = sysRoleMapper.selectOne(
            new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, roleKey)
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRoleKey()).isEqualTo(roleKey);
    }
}
