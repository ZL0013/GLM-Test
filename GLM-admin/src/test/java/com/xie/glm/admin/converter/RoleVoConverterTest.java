package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.RoleVO;
import com.xie.glm.system.dto.RoleDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * RoleVoConverter 单元测试
 *
 * <p>测试 RoleDTO 到 RoleVO 的转换
 *
 * @author xie
 */
@DisplayName("RoleVoConverter 单元测试")
class RoleVoConverterTest {

    /**
     * 获取 RoleVoConverter 实例
     * MapStruct 会在 target/generated-sources/annotations 下生成实现类
     */
    private RoleVoConverter getRoleVoConverter() {
        // 使用 Mappers 工厂获取实例（不使用 Spring）
        return new RoleVoConverterImpl();
    }

    @ParameterizedTest
    @MethodSource("provideRoleDTOData")
    @DisplayName("测试 DTO 转 VO")
    void testToVo(Long roleId, String roleName, String roleKey, Integer roleSort,
                  String dataScope, Boolean menuCheckStrictly, Boolean deptCheckStrictly,
                  String status, String remark) {
        // Given: 准备 RoleDTO 数据
        RoleDTO roleDTO = RoleDTO.builder()
                .roleId(roleId)
                .roleName(roleName)
                .roleKey(roleKey)
                .roleSort(roleSort)
                .dataScope(dataScope)
                .menuCheckStrictly(menuCheckStrictly)
                .deptCheckStrictly(deptCheckStrictly)
                .status(status)
                .remark(remark)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // When: 执行转换
        RoleVO roleVO = getRoleVoConverter().toVo(roleDTO);

        // Then: 验证基本字段转换正确
        assertAll("RoleVO 基本字段验证",
                () -> assertThat(roleVO.getRoleId()).isEqualTo(roleId),
                () -> assertThat(roleVO.getRoleName()).isEqualTo(roleName),
                () -> assertThat(roleVO.getRoleKey()).isEqualTo(roleKey),
                () -> assertThat(roleVO.getRoleSort()).isEqualTo(roleSort),
                () -> assertThat(roleVO.getDataScope()).isEqualTo(dataScope),
                () -> assertThat(roleVO.getMenuCheckStrictly()).isEqualTo(menuCheckStrictly),
                () -> assertThat(roleVO.getDeptCheckStrictly()).isEqualTo(deptCheckStrictly),
                () -> assertThat(roleVO.getStatus()).isEqualTo(status),
                () -> assertThat(roleVO.getRemark()).isEqualTo(remark)
        );
    }

    @Test
    @DisplayName("测试 DTO 列表转 VO 列表")
    void testToVoList() {
        // Given: 准备 RoleDTO 列表
        List<RoleDTO> roleDTOs = List.of(
                RoleDTO.builder()
                        .roleId(1L)
                        .roleName("超级管理员")
                        .roleKey("admin")
                        .build(),
                RoleDTO.builder()
                        .roleId(2L)
                        .roleName("普通角色")
                        .roleKey("common")
                        .build()
        );

        // When: 执行转换
        List<RoleVO> roleVOs = getRoleVoConverter().toVoList(roleDTOs);

        // Then: 验证转换结果
        assertAll("VO 列表验证",
                () -> assertThat(roleVOs).hasSize(2),
                () -> assertThat(roleVOs.get(0).getRoleId()).isEqualTo(1L),
                () -> assertThat(roleVOs.get(0).getRoleName()).isEqualTo("超级管理员"),
                () -> assertThat(roleVOs.get(1).getRoleId()).isEqualTo(2L),
                () -> assertThat(roleVOs.get(1).getRoleName()).isEqualTo("普通角色")
        );
    }

    @Test
    @DisplayName("测试 null 转换")
    void testNullConversion() {
        // Given: null DTO
        RoleDTO roleDTO = null;

        // When: 执行转换
        RoleVO roleVO = getRoleVoConverter().toVo(roleDTO);

        // Then: 验证返回 null
        assertThat(roleVO).isNull();
    }

    @Test
    @DisplayName("测试 null 列表转换")
    void testNullListConversion() {
        // Given: null DTO 列表
        List<RoleDTO> roleDTOs = null;

        // When: 执行转换
        List<RoleVO> roleVOs = getRoleVoConverter().toVoList(roleDTOs);

        // Then: 验证返回 null
        assertThat(roleVOs).isNull();
    }

    @Test
    @DisplayName("测试空列表转换")
    void testEmptyListConversion() {
        // Given: 空 DTO 列表
        List<RoleDTO> roleDTOs = List.of();

        // When: 执行转换
        List<RoleVO> roleVOs = getRoleVoConverter().toVoList(roleDTOs);

        // Then: 验证返回空列表
        assertThat(roleVOs).isEmpty();
    }

    @Test
    @DisplayName("测试 menuIds 字段自动映射")
    void testMenuIdsMapping() {
        // Given: 包含 menuIds 的 DTO
        List<Long> menuIds = List.of(1L, 2L, 3L);
        RoleDTO roleDTO = RoleDTO.builder()
                .roleId(1L)
                .roleName("超级管理员")
                .menuIds(menuIds)
                .build();

        // When: 执行转换
        RoleVO roleVO = getRoleVoConverter().toVo(roleDTO);

        // Then: 验证 menuIds 自动映射
        assertThat(roleVO.getMenuIds()).isEqualTo(menuIds);
    }

    @Test
    @DisplayName("测试状态码转换需要手动设置 statusText")
    void testStatusTextMapping() {
        // Given: 状态为 "0" 的 DTO
        RoleDTO roleDTO = RoleDTO.builder()
                .roleId(1L)
                .roleName("超级管理员")
                .status("0")
                .build();

        // When: 执行转换（注意：MapStruct 不会自动设置 statusText）
        RoleVO roleVO = getRoleVoConverter().toVo(roleDTO);

        // Then: 验证 status 正确但 statusText 需要手动设置
        assertAll("状态字段验证",
                () -> assertThat(roleVO.getStatus()).isEqualTo("0"),
                () -> assertThat(roleVO.getStatusText()).isNull()  // 需要在 Facade 层手动设置
        );
    }

    @Test
    @DisplayName("测试 dataScope 字段转换需要手动设置 dataScopeText")
    void testDataScopeTextMapping() {
        // Given: dataScope 为 "1" 的 DTO
        RoleDTO roleDTO = RoleDTO.builder()
                .roleId(1L)
                .roleName("超级管理员")
                .dataScope("1")
                .build();

        // When: 执行转换（注意：MapStruct 不会自动设置 dataScopeText）
        RoleVO roleVO = getRoleVoConverter().toVo(roleDTO);

        // Then: 验证 dataScope 正确但 dataScopeText 需要手动设置
        assertAll("数据范围字段验证",
                () -> assertThat(roleVO.getDataScope()).isEqualTo("1"),
                () -> assertThat(roleVO.getDataScopeText()).isNull()  // 需要在 Facade 层手动设置
        );
    }

    /**
     * 提供 RoleDTO 测试数据
     */
    private static Stream<Arguments> provideRoleDTOData() {
        return Stream.of(
                Arguments.of(1L, "超级管理员", "admin", 1, "1", true, true, "0", "超级管理员"),
                Arguments.of(2L, "普通角色", "common", 2, "2", false, false, "0", "普通角色"),
                Arguments.of(3L, "测试角色", "test", 3, "3", null, null, "1", "测试")
        );
    }
}
