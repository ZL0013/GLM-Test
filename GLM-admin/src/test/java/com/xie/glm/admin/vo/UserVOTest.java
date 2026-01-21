package com.xie.glm.admin.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * UserVO 单元测试
 *
 * <p>测试用户视图对象的创建和字段设置
 *
 * @author xie
 */
@DisplayName("UserVO 单元测试")
class UserVOTest {

    @ParameterizedTest
    @MethodSource("provideUserVOData")
    @DisplayName("测试 UserVO 创建和字段设置")
    void testUserVOCreation(Long userId, String userName, String nickName, String email,
                            Long deptId, String status, String deptName,
                            List<String> roleNames, String statusText) {
        // When: 创建 UserVO 对象
        UserVO userVO = UserVO.builder()
                .userId(userId)
                .userName(userName)
                .nickName(nickName)
                .email(email)
                .deptId(deptId)
                .status(status)
                .deptName(deptName)
                .roleNames(roleNames)
                .statusText(statusText)
                .build();

        // Then: 验证所有字段设置正确
        assertAll("UserVO 字段验证",
                () -> assertThat(userVO.getUserId()).isEqualTo(userId),
                () -> assertThat(userVO.getUserName()).isEqualTo(userName),
                () -> assertThat(userVO.getNickName()).isEqualTo(nickName),
                () -> assertThat(userVO.getEmail()).isEqualTo(email),
                () -> assertThat(userVO.getDeptId()).isEqualTo(deptId),
                () -> assertThat(userVO.getStatus()).isEqualTo(status),
                () -> assertThat(userVO.getDeptName()).isEqualTo(deptName),
                () -> assertThat(userVO.getRoleNames()).isEqualTo(roleNames),
                () -> assertThat(userVO.getStatusText()).isEqualTo(statusText)
        );
    }

    @Test
    @DisplayName("测试 UserVO 带 null 部门名称")
    void testUserVOWithNullDeptName() {
        // Given: 部门名称为 null
        String deptName = null;

        // When: 创建 UserVO
        UserVO userVO = UserVO.builder()
                .userId(1L)
                .userName("admin")
                .deptName(deptName)
                .build();

        // Then: 验证 deptName 为 null
        assertThat(userVO.getDeptName()).isNull();
    }

    @Test
    @DisplayName("测试 UserVO 带空角色列表")
    void testUserVOWithEmptyRoleNames() {
        // Given: 空角色列表
        List<String> roleNames = List.of();

        // When: 创建 UserVO
        UserVO userVO = UserVO.builder()
                .userId(1L)
                .userName("admin")
                .roleNames(roleNames)
                .build();

        // Then: 验证 roleNames 为空列表
        assertThat(userVO.getRoleNames()).isEmpty();
    }

    @Test
    @DisplayName("测试 UserVO 状态文本映射")
    void testStatusTextMapping() {
        // Given & When & Then: 测试不同状态码映射
        assertAll("状态文本映射",
                () -> {
                    UserVO vo1 = UserVO.builder().status("0").statusText("正常").build();
                    assertThat(vo1.getStatusText()).isEqualTo("正常");
                },
                () -> {
                    UserVO vo2 = UserVO.builder().status("1").statusText("停用").build();
                    assertThat(vo2.getStatusText()).isEqualTo("停用");
                }
        );
    }

    /**
     * 提供 UserVO 测试数据
     */
    private static Stream<Arguments> provideUserVOData() {
        return Stream.of(
                Arguments.of(
                        1L, "admin", "管理员", "admin@example.com",
                        101L, "0", "技术部", List.of("超级管理员", "用户管理员"), "正常"
                ),
                Arguments.of(
                        2L, "user", "普通用户", "user@example.com",
                        102L, "1", "市场部", List.of("普通用户"), "停用"
                ),
                Arguments.of(
                        3L, "test", "测试用户", "test@example.com",
                        null, "0", null, List.of(), "正常"
                )
        );
    }
}
