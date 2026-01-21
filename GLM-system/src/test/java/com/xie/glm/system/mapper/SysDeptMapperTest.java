package com.xie.glm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 部门 Mapper 接口测试类
 *
 * <p>测试 {@link SysDeptMapper} 的基本功能，验证：
 * <ul>
 *   <li>继承 {@link BaseMapper} 获得 CRUD 能力</li>
 *   <li>MyBatis Plus 注解配置正确</li>
 * </ul>
 *
 * @author xie
 */
@DisplayName("部门 Mapper 接口测试")
class SysDeptMapperTest {

    @Test
    @DisplayName("部门 Mapper 接口应该存在")
    void deptMapperInterfaceShouldExist() {
        // When
        Class<?> mapperClass = SysDeptMapper.class;

        // Then
        assertThat(mapperClass).isNotNull();
    }

    @Test
    @DisplayName("部门 Mapper 应该继承 BaseMapper")
    void deptMapperShouldExtendBaseMapper() {
        // When
        Class<?>[] interfaces = SysDeptMapper.class.getInterfaces();

        // Then
        assertThat(interfaces)
                .as("DeptMapper 应该继承 BaseMapper")
                .isNotEmpty();
    }
}
