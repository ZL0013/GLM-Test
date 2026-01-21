package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysUser;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserConverter 用户转换器测试类
 *
 * <p>测试 MapStruct 生成的用户对象转换功能
 * <p>测试原则：
 * <ul>
 *   <li>直接实例化 MapStruct 生成的实现类</li>
 *   <li>验证 Entity ↔ DTO 双向转换</li>
 *   <li>测试列表转换</li>
 *   <li>验证字段映射的完整性</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("UserConverter 用户转换器单元测试")
class UserConverterTest {

    private final UserConverter userConverter = new UserConverterImpl();

    // ==================== Entity → DTO 转换测试 ====================

    @Test
    @DisplayName("Entity → DTO - 验证基本转换")
    void testToDto() {
        // 准备测试数据
        SysUser entity = new SysUser();
        entity.setUserId(1L);
        entity.setUserName("admin");
        entity.setNickName("管理员");
        entity.setEmail("admin@example.com");
        entity.setPhonenumber("13800138000");
        entity.setSex("0");
        entity.setAvatar("http://example.com/avatar.jpg");
        entity.setDeptId(100L);
        entity.setStatus("0");
        entity.setRemark("系统管理员");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setLoginDate(LocalDateTime.now());
        entity.setPwdUpdateDate(LocalDateTime.now());
        entity.setPasswordChanged(true);
        entity.setDefaultPassword(false);

        // 执行转换
        UserDTO dto = userConverter.toDto(entity);

        // 验证结果
        assertNotNull(dto, "DTO不应为null");
        assertEquals(entity.getUserId(), dto.getUserId(), "用户ID应匹配");
        assertEquals(entity.getUserName(), dto.getUserName(), "用户名应匹配");
        assertEquals(entity.getNickName(), dto.getNickName(), "昵称应匹配");
        assertEquals(entity.getEmail(), dto.getEmail(), "邮箱应匹配");
        assertEquals(entity.getPhonenumber(), dto.getPhonenumber(), "手机号应匹配");
        assertEquals(entity.getSex(), dto.getSex(), "性别应匹配");
        assertEquals(entity.getAvatar(), dto.getAvatar(), "头像应匹配");
        assertEquals(entity.getDeptId(), dto.getDeptId(), "部门ID应匹配");
        assertEquals(entity.getStatus(), dto.getStatus(), "状态应匹配");
        assertEquals(entity.getRemark(), dto.getRemark(), "备注应匹配");
        assertEquals(entity.getCreateTime(), dto.getCreateTime(), "创建时间应匹配");
        assertEquals(entity.getUpdateTime(), dto.getUpdateTime(), "更新时间应匹配");
        assertEquals(entity.getLoginDate(), dto.getLoginDate(), "登录时间应匹配");
        assertEquals(entity.getPwdUpdateDate(), dto.getPwdUpdateDate(), "密码更新时间应匹配");
        assertEquals(entity.getPasswordChanged(), dto.getPasswordChanged(), "密码修改标志应匹配");
        assertEquals(entity.getDefaultPassword(), dto.getDefaultPassword(), "默认密码标志应匹配");

        // 验证敏感字段不被转换
        assertNull(dto.getPassword(), "密码不应被转换到DTO中");
    }

    @Test
    @DisplayName("Entity → DTO - 验证 null 值处理")
    void testToDtoWithNullValues() {
        SysUser entity = new SysUser();
        // 所有字段保持默认值 null

        UserDTO dto = userConverter.toDto(entity);

        assertNotNull(dto, "DTO不应为null");
        assertNull(dto.getUserId(), "null字段应保持null");
        assertNull(dto.getUserName(), "null字段应保持null");
        assertNull(dto.getNickName(), "null字段应保持null");
    }

    @ParameterizedTest
    @MethodSource("provideUserEntities")
    @DisplayName("Entity → DTO - 参数化测试")
    void testToDtoParameterized(SysUser entity) {
        UserDTO dto = userConverter.toDto(entity);

        assertNotNull(dto, "DTO不应为null");
        assertEquals(entity.getUserId(), dto.getUserId());
        assertEquals(entity.getUserName(), dto.getUserName());
        assertEquals(entity.getEmail(), dto.getEmail());
    }

    private static Stream<SysUser> provideUserEntities() {
        return Stream.of(
            createTestUser(1L, "admin", "管理员", "admin@example.com"),
            createTestUser(2L, "user", "普通用户", "user@example.com"),
            createTestUser(3L, "test", "测试用户", "test@example.com")
        );
    }

    // ==================== DTO → Entity 转换测试 ====================

    @Test
    @DisplayName("DTO → Entity - 验证基本转换（CreateDTO）")
    void testCreateDtoToEntity() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUserName("admin");
        dto.setNickName("管理员");
        dto.setPassword("Admin@123");
        dto.setEmail("admin@example.com");
        dto.setPhonenumber("13800138000");
        dto.setSex("0");
        dto.setDeptId(100L);
        dto.setStatus("0");
        dto.setRemark("系统管理员");

        SysUser entity = userConverter.createDtoToEntity(dto);

        assertNotNull(entity, "Entity不应为null");
        assertEquals(dto.getUserName(), entity.getUserName(), "用户名应匹配");
        assertEquals(dto.getNickName(), entity.getNickName(), "昵称应匹配");
        assertEquals(dto.getPassword(), entity.getPassword(), "密码应匹配");
        assertEquals(dto.getEmail(), entity.getEmail(), "邮箱应匹配");
        assertEquals(dto.getPhonenumber(), entity.getPhonenumber(), "手机号应匹配");
        assertEquals(dto.getSex(), entity.getSex(), "性别应匹配");
        assertEquals(dto.getDeptId(), entity.getDeptId(), "部门ID应匹配");
        assertEquals(dto.getStatus(), entity.getStatus(), "状态应匹配");
        assertEquals(dto.getRemark(), entity.getRemark(), "备注应匹配");
    }

    @Test
    @DisplayName("DTO → Entity - 验证更新转换（UpdateDTO）")
    void testUpdateDtoToEntity() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(1L);
        dto.setUserName("admin");
        dto.setNickName("新昵称");
        dto.setEmail("new@example.com");
        dto.setPhonenumber("13900139000");
        dto.setSex("1");
        dto.setDeptId(101L);
        dto.setStatus("0");
        dto.setRemark("更新信息");

        SysUser entity = userConverter.updateDtoToEntity(dto);

        assertNotNull(entity, "Entity不应为null");
        assertEquals(dto.getUserId(), entity.getUserId(), "用户ID应匹配");
        assertEquals(dto.getUserName(), entity.getUserName(), "用户名应匹配");
        assertEquals(dto.getNickName(), entity.getNickName(), "昵称应匹配");
        assertEquals(dto.getEmail(), entity.getEmail(), "邮箱应匹配");
        assertEquals(dto.getPhonenumber(), entity.getPhonenumber(), "手机号应匹配");
        assertEquals(dto.getSex(), entity.getSex(), "性别应匹配");
        assertEquals(dto.getDeptId(), entity.getDeptId(), "部门ID应匹配");
        assertEquals(dto.getStatus(), entity.getStatus(), "状态应匹配");
        assertEquals(dto.getRemark(), entity.getRemark(), "备注应匹配");
    }

    // ==================== 列表转换测试 ====================

    @Test
    @DisplayName("Entity List → DTO List - 验证列表转换")
    void testToDtoList() {
        List<SysUser> entities = Arrays.asList(
            createTestUser(1L, "admin", "管理员", "admin@example.com"),
            createTestUser(2L, "user", "普通用户", "user@example.com"),
            createTestUser(3L, "test", "测试用户", "test@example.com")
        );

        List<UserDTO> dtos = userConverter.toDtoList(entities);

        assertNotNull(dtos, "DTO列表不应为null");
        assertEquals(entities.size(), dtos.size(), "列表大小应匹配");

        for (int i = 0; i < entities.size(); i++) {
            assertEquals(entities.get(i).getUserId(), dtos.get(i).getUserId(),
                    "第" + i + "个元素的 userId 应匹配");
            assertEquals(entities.get(i).getUserName(), dtos.get(i).getUserName(),
                    "第" + i + "个元素的 userName 应匹配");
        }
    }

    @Test
    @DisplayName("Entity List → DTO List - 验证空列表转换")
    void testToDtoListWithEmpty() {
        List<SysUser> entities = List.of();

        List<UserDTO> dtos = userConverter.toDtoList(entities);

        assertNotNull(dtos, "DTO列表不应为null");
        assertTrue(dtos.isEmpty(), "DTO列表应为空");
    }

    @Test
    @DisplayName("Entity List → DTO List - 验证 null 列表转换")
    void testToDtoListWithNull() {
        List<UserDTO> dtos = userConverter.toDtoList(null);

        // MapStruct 生成的实现通常返回 null 或空列表
        // 这里验证不会抛出异常
        if (dtos != null) {
            assertTrue(dtos.isEmpty(), "null输入应返回空列表或null");
        }
    }

    // ==================== 业务场景测试 ====================

    @Test
    @DisplayName("业务场景 - 用户列表查询转换")
    void testScenario_UserListQuery() {
        // 模拟数据库查询结果
        List<SysUser> dbResult = Arrays.asList(
            createTestUser(1L, "admin", "系统管理员", "admin@example.com"),
            createTestUser(2L, "zhangsan", "张三", "zhangsan@example.com"),
            createTestUser(3L, "lisi", "李四", "lisi@example.com")
        );

        // 转换为 DTO 返回给前端
        List<UserDTO> dtoList = userConverter.toDtoList(dbResult);

        assertEquals(3, dtoList.size(), "应返回3个用户");

        // 验证第一个用户
        assertEquals(1L, dtoList.get(0).getUserId());
        assertEquals("admin", dtoList.get(0).getUserName());
        assertEquals("系统管理员", dtoList.get(0).getNickName());

        // 验证敏感信息不被泄露
        for (UserDTO dto : dtoList) {
            assertNull(dto.getPassword(), "DTO中不应包含密码字段");
        }
    }

    @Test
    @DisplayName("业务场景 - 创建用户转换")
    void testScenario_CreateUser() {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setUserName("newuser");
        createDTO.setNickName("新用户");
        createDTO.setPassword("Password@123");
        createDTO.setEmail("newuser@example.com");
        createDTO.setPhonenumber("13700137000");
        createDTO.setSex("0");
        createDTO.setDeptId(100L);
        createDTO.setStatus("0");
        createDTO.setRoleIds(Arrays.asList(2L));
        createDTO.setPostIds(Arrays.asList(1L));

        // 转换为 Entity 准备插入数据库
        SysUser entity = userConverter.createDtoToEntity(createDTO);

        assertEquals("newuser", entity.getUserName());
        assertEquals("Password@123", entity.getPassword());
        assertEquals(100L, entity.getDeptId());

        // 注意：roleIds 和 postIds 在 Entity 中没有对应字段
        // 这些关联数据需要在 Service 层单独处理
    }

    @Test
    @DisplayName("业务场景 - 更新用户转换")
    void testScenario_UpdateUser() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setUserId(1L);
        updateDTO.setNickName("更新后的昵称");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setPhonenumber("13900139000");

        SysUser entity = userConverter.updateDtoToEntity(updateDTO);

        assertEquals(1L, entity.getUserId());
        assertEquals("更新后的昵称", entity.getNickName());
        assertEquals("updated@example.com", entity.getEmail());
    }

    // ==================== 辅助方法 ====================

    /**
     * 创建测试用的用户实体
     */
    private static SysUser createTestUser(Long userId, String userName, String nickName, String email) {
        SysUser entity = new SysUser();
        entity.setUserId(userId);
        entity.setUserName(userName);
        entity.setNickName(nickName);
        entity.setEmail(email);
        entity.setPhonenumber("13800138000");
        entity.setSex("0");
        entity.setDeptId(100L);
        entity.setStatus("0");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setPasswordChanged(true);
        entity.setDefaultPassword(false);
        return entity;
    }
}
