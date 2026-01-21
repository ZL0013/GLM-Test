package com.xie.glm.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 导入导出注解
 * <p>
 * 用于标记实体类字段，指定 Excel 导入导出时的列名、排序、格式等属性
 *
 * @author xie
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    /**
     * Excel 列名
     */
    String name() default "";

    /**
     * 列排序，值越小越靠前
     */
    int sort() default 0;

    /**
     * 列宽
     */
    int columnWidth() default 20;

    /**
     * 是否必填（导入时校验）
     */
    boolean required() default false;

    /**
     * 日期格式（日期类型字段使用）
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 读写模式
     */
    Mode mode() default Mode.READ_WRITE;

    /**
     * 数字格式（数字类型字段使用）
     */
    String numFormat() default "";

    /**
     * 替换文本（用于枚举值转换，如：0=男,1=女）
     */
    String[] replace() default {};

    /**
     * 读写模式枚举
     */
    enum Mode {
        /**
         * 仅读取（从 Excel 导入到数据库）
         */
        READ_ONLY,

        /**
         * 仅写入（从数据库导出到 Excel）
         */
        WRITE_ONLY,

        /**
         * 读写都支持
         */
        READ_WRITE
    }
}
