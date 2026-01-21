package com.xie.glm.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用户安全配置属性
 *
 * <p>从 application.yml 中读取用户安全相关配置：
 * <ul>
 *   <li>default-password: 重置密码时的默认密码</li>
 * </ul>
 *
 * <p>生产环境应该使用强密码并通过环境变量注入
 *
 * @author xie
 */
@Component
@ConfigurationProperties(prefix = "user.security")
public class UserSecurityProperties {

    /**
     * 默认密码
     * <p>用于重置用户密码时的默认值
     * <p>默认值：123456
     */
    private String defaultPassword = "123456";

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }
}
