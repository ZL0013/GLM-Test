package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.UserVO;
import com.xie.glm.system.dto.UserDTO;
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
 * UserVoConverter 单元测试
 *
 * <p>测试 UserDTO 到 UserVO 的转换
 *
 * @author xie
 */
@DisplayName("UserVoConverter 单元测试")
class UserVoConverterTest {

    /**
     * 获取 UserVoConverter 实例
     * MapStruct 会在 target/generated-sources/annotations 下生成实现类
     */
    private UserVoConverter getUserVoConverter() {
        // 使用 Mappers 工厂获取实例（不使用 Spring）
        return new UserVoConverterImpl();
    }

    @ParameterizedTest
    @MethodSource("provideUserDTOData")
    @DisplayName("测试 DTO 转 VO")
    void testToVo(Long userId, String userName, String nickName, String email,
                   Long deptId, String status) {
        // Given: 准备 UserDTO 数据
        UserDTO userDTO = UserDTO.builder()
                .userId(userId)
                .userName(userName)
                .nickName(nickName)
                .email(email)
                .deptId(deptId)
                .status(status)
                .createTime(LocalDateTime.now())
                .build();

        // When: 执行转换
        UserVO userVO = getUserVoConverter().toVo(userDTO);

        // Then: 验证基本字段转换正确
        assertAll("UserVO 基本字段验证",
                () -> assertThat(userVO.getUserId()).isEqualTo(userId),
                () -> assertThat(userVO.getUserName()).isEqualTo(userName),
                () -> assertThat(userVO.getNickName()).isEqualTo(nickName),
                () -> assertThat(userVO.getEmail()).isEqualTo(email),
                () -> assertThat(userVO.getDeptId()).isEqualTo(deptId),
                () -> assertThat(userVO.getStatus()).isEqualTo(status)
        );
    }

    @Test
    @DisplayName("测试 DTO 列表转 VO 列表")
    void testToVoList() {
        // Given: 准备 UserDTO 列表
        List<UserDTO> userDTOs = List.of(
                UserDTO.builder()
                        .userId(1L)
                        .userName("admin")
                        .nickName("管理员")
                        .build(),
                UserDTO.builder()
                        .userId(2L)
                        .userName("user")
                        .nickName("普通用户")
                        .build()
        );

        // When: 执行转换
        List<UserVO> userVOs = getUserVoConverter().toVoList(userDTOs);

        // Then: 验证转换结果
        assertAll("VO 列表验证",
                () -> assertThat(userVOs).hasSize(2),
                () -> assertThat(userVOs.get(0).getUserId()).isEqualTo(1L),
                () -> assertThat(userVOs.get(0).getUserName()).isEqualTo("admin"),
                () -> assertThat(userVOs.get(1).getUserId()).isEqualTo(2L),
                () -> assertThat(userVOs.get(1).getUserName()).isEqualTo("user")
        );
    }

    @Test
    @DisplayName("测试 null 转换")
    void testNullConversion() {
        // Given: null DTO
        UserDTO userDTO = null;

        // When: 执行转换
        UserVO userVO = getUserVoConverter().toVo(userDTO);

        // Then: 验证返回 null
        assertThat(userVO).isNull();
    }

    @Test
    @DisplayName("测试 null 列表转换")
    void testNullListConversion() {
        // Given: null DTO 列表
        List<UserDTO> userDTOs = null;

        // When: 执行转换
        List<UserVO> userVOs = getUserVoConverter().toVoList(userDTOs);

        // Then: 验证返回 null
        assertThat(userVOs).isNull();
    }

    @Test
    @DisplayName("测试空列表转换")
    void testEmptyListConversion() {
        // Given: 空 DTO 列表
        List<UserDTO> userDTOs = List.of();

        // When: 执行转换
        List<UserVO> userVOs = getUserVoConverter().toVoList(userDTOs);

        // Then: 验证返回空列表
        assertThat(userVOs).isEmpty();
    }

    @Test
    @DisplayName("测试 deptId 为 null 时 deptName 为 null")
    void testNullDeptId() {
        // Given: deptId 为 null 的 DTO
        UserDTO userDTO = UserDTO.builder()
                .userId(1L)
                .userName("admin")
                .deptId(null)
                .build();

        // When: 执行转换
        UserVO userVO = getUserVoConverter().toVo(userDTO);

        // Then: 验证 deptId 和 deptName 都为 null
        assertAll("null deptId 验证",
                () -> assertThat(userVO.getDeptId()).isNull(),
                () -> assertThat(userVO.getDeptName()).isNull()
        );
    }

    @Test
    @DisplayName("测试状态码转换需要手动设置 statusText")
    void testStatusTextMapping() {
        // Given: 状态为 "0" 的 DTO
        UserDTO userDTO = UserDTO.builder()
                .userId(1L)
                .userName("admin")
                .status("0")
                .build();

        // When: 执行转换（注意：MapStruct 不会自动设置 statusText）
        UserVO userVO = getUserVoConverter().toVo(userDTO);

        // Then: 验证 status 正确但 statusText 需要手动设置
        assertAll("状态字段验证",
                () -> assertThat(userVO.getStatus()).isEqualTo("0"),
                () -> assertThat(userVO.getStatusText()).isNull()  // 需要在 Facade 层手动设置
        );
    }

    /**
     * 提供 UserDTO 测试数据
     */
    private static Stream<Arguments> provideUserDTOData() {
        return Stream.of(
                Arguments.of(1L, "admin", "管理员", "admin@example.com", 101L, "0"),
                Arguments.of(2L, "user", "普通用户", "user@example.com", 102L, "1"),
                Arguments.of(3L, "test", "测试用户", "test@example.com", null, "0")
        );
    }
}
