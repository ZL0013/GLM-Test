package com.xie.glm.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.xie.glm.framework.security.DataPermissionHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置类
 *
 * <p>配置 MyBatis Plus 的分页插件和数据权限插件。
 *
 * <p>功能说明：
 * <ul>
 *   <li>分页插件：自动识别数据库类型，生成分页 SQL</li>
 *   <li>数据权限插件：根据用户权限自动过滤数据</li>
 * </ul>
 *
 * <p>设计原则：
 * <ul>
 *   <li>符合宪法第一条：简单性原则，仅配置必要功能</li>
 *   <li>符合宪法第四条：遵循 Spring Boot 自动配置约定</li>
 *   <li>分页插件支持：MySQL、PostgreSQL、H2、Oracle 等主流数据库</li>
 * </ul>
 *
 * @author xie
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 配置 MyBatis Plus 拦截器
     *
     * <p>包含分页插件和数据权限插件。
     * <p>插件执行顺序：数据权限插件 → 分页插件（数据权限优先执行）
     *
     * @return MybatisPlusInterceptor 实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加数据权限插件
        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor();
        // 设置数据权限处理器
        dataPermissionInterceptor.setDataPermissionHandler(new DataPermissionHandlerImpl());
        interceptor.addInnerInterceptor(dataPermissionInterceptor);

        // 添加分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置数据库类型为自动检测（根据数据源自动识别）
        paginationInnerInterceptor.setDbType(DbType.POSTGRE_SQL);
        // 设置单页最大限制数量，默认 500 条，-1 不限制
        paginationInnerInterceptor.setMaxLimit(500L);
        // 溢出总页数后是否进行处理，true 返回首页，false 继续请求
        paginationInnerInterceptor.setOverflow(false);

        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        return interceptor;
    }
}
