package com.xie.glm.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * GLM-Test 快速开发框架启动类
 *
 * @author xie
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.xie.glm")
@MapperScan("com.xie.glm.*.mapper")
@EnableAsync
public class GlmApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlmApplication.class, args);
    }
}
